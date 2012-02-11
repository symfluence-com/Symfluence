package models;


import org.joda.time._
import org.joda.time.format._

import persistence._
import exception._

import play.api.db._
import play.api.Play.current

import com.mongodb.casbah.Imports._

import anorm._
import anorm.SqlParser._

import java.util.UUID
import java.util.Date

case class User(
    id: Pk[String]= Id(UUID.randomUUID.toString.replace("-", "")), 
    email: String, 
    userName: String,
    firstName: String, 
    lastName: String,
    profilePicId: String, 
    mailingAddress: Option[String],
    gender: Option[String],
    dateOfBirth: Option[Date],
    occupation:Option[String],
    income:Option[Double],
    points: Int, 
    credits: Int, 
    fbToken: String,
    createdAt: DateTime = DateTime.now(DateTimeZone.UTC),
    updatedAt: DateTime = DateTime.now(DateTimeZone.UTC)){

  private var posts:Option[List[Post]]= None
  private var followers:Option[List[User]] = None

  def getPosts:Option[List[Post]]={
    if (posts.isDefined){
      posts
    }
    else {
      val query = MongoDBObject("user_id" -> this.id.toString)
      posts =Some(Mongo.posts.find(query).toList.map(rawObject => Post.postMapper(rawObject)))
      posts
    }
  }

  def post(postObject:Post) ={
    val rawObject = postObject.toRawObject
    Mongo.posts.save(rawObject)
    val count = DB.withConnection{ implicit connection =>
      SQL(
          """
            INSERT INTO users_posts
            (user_id, post_id, group_id, created_at, updated_at) 
            VALUES({user_id}, {post_id}, {group_id}, {created_at}, {updated_at})
          """
        ).on("user_id" -> this.id, "post_id" -> postObject.id, "group_id"-> postObject.groupId, "created_at"-> new Date(), "updated_at" -> new Date()).executeUpdate()
    }
    if (posts.isDefined){
        posts = Some(postObject::posts.get)
    }

  }

  def deletePost(post:Post){
    if(post.userId == this.id.toString){
      Mongo.posts.remove(MongoDBObject("_id" -> post.id))
       val count = DB.withConnection{ implicit connection =>
          SQL(
              """
                delete from users_posts where users_posts.post_id ={post_id}
              """
            ).on("post_id" -> post.id).executeUpdate()
      }
      if (posts.isDefined){
          posts = Some(posts.get.filterNot(postExisting => { postExisting == post }))
      }

    }
    else{
      throw new NotAuthorizedException()
    }
  }

  def followUser(user:User){
    val count = if(this.getFollowers.isDefined && this.getFollowers.get.filter(follower =>  follower.id == user.id).isEmpty){
      if (followers.isDefined){
        followers = Some(user::followers.get)
      }
      else{
        followers = Some(user::Nil)
      }
      DB.withConnection{ implicit connection =>
          SQL(
          "INSERT INTO users_followers(user_id, follower_id, created_at, updated_at) VALUES({user_id}, {follower_id},{created_at}, {updated_at})"
        ).on("user_id" -> this.id, "follower_id" -> user.id, "created_at" -> new Date(), "updated_at" -> new Date()).executeUpdate()
      }
    }
    else{
      1
    }
    if(count > 0){
      true
    }
    else{
      false
    }

  }

  def unfollowUser(user:User){
    val count = DB.withConnection{ implicit connection =>
      SQL(
        "DELETE FROM users_followers WHERE user_id={user_id} and  follower_id={follower_id}"
      ).on("user_id" -> this.id, "follower_id" -> user.id).executeUpdate()
    }
    if(count > 0){
       if (followers.isDefined){
        followers = Some(followers.get.filterNot(follower => follower.id ==user.id))
      }
      true
    }
    else{
      false
    }

  }
  def getFollowers:Option[List[User]]={
    if (followers.isDefined){
      followers
    }
    else{
      followers = Some(DB.withConnection{ implicit connection =>
        SQL(
          """
          SELECT users.* FROM users, users_followers WHERE users_followers.user_id = {user_id}
          AND users.id  = users_followers.follower_id
          """
        ).on("user_id" -> this.id).as(User.simple *)

      })
      followers
    }
  }

  def addGroup(group:Group){
    val count = DB.withConnection{ implicit connection =>
      SQL(
        """
          INSERT INTO users_groups(user_id, group_id, created_at, updated_at) 
          VALUES({user_id}, {group_id}, {created_at}, {updated_at})
        """
    ).on("user_id" -> this.id, "group_id" -> group.id, "created_at" -> DateTime.now(DateTimeZone.UTC), "updated_at" -> DateTime.now(DateTimeZone.UTC)).executeUpdate()
    }
    if(count > 0){
        true
    }
    else{
        false
    }
  }

  def getGroups:Seq[Group]={
      val groups = DB.withConnection{implicit connection =>
        SQL(
           """
             SELECT groups.* from groups, users_groups where users_groups.user_id =
             {user_id} and groups.id = users_groups.group_id
           """
       ).on("user_id" -> this.id).as(Group.simple *)
      }
      groups
  }


  def delete={
    val count = DB.withConnection{ implicit connection => 
          SQL(
            """ 
              DELETE FROM users where id = {user_id}
            """
          ).on("user_id"  -> this.id).executeUpdate 
      }
      count
  }
}

object User {

    val simple = {
        get[Pk[String]]("users.id") ~
        get[String]("users.email") ~
        get[String]("users.username") ~
        get[String]("users.first_name") ~
        get[String]("users.last_name") ~
        get[String]("users.profile_pic_id") ~
        get[Option[String]]("users.mailing_address") ~
        get[Option[String]]("users.gender") ~
        get[Option[Date]]("date_Of_birth") ~
        get[Option[String]]("occupation") ~
        get[Option[Double]]("income") ~
        get[Int]("users.points") ~
        get[Int]("users.credits") ~
        get[String]("users.fb_token") ~
        get[Date]("users.created_at") ~
        get[Date]("users.updated_at") map {
            case id ~ email ~ userName ~ firstName ~ lastName ~ profilePicId ~ mailingAddress ~ gender ~ dateOfBirth ~ occupation ~  income ~ points ~ credits ~ fbToken ~ createdAt ~ updatedAt => 
            User(id, email, userName, firstName, lastName, profilePicId, mailingAddress, gender, dateOfBirth, occupation, income, points, credits, fbToken, new DateTime(createdAt), new DateTime(updatedAt))
        }
    }

    val withFollowers = User.simple ~ (User.simple ?) map {
      case user~follower => (user, follower)
    }



    def findById(id: String): Option[User] = {
        DB.withConnection { implicit connection =>
        SQL("select * from users where id = {id}").on("id" -> id).as(User.simple.singleOpt)
        }
    }


    def findByEmail(email: String):Option[User] ={
        DB.withConnection{ implicit connection =>
            SQL("select * from users where email = {email}").on("email" -> email).as(User.simple.singleOpt)
        }
    }

    def insert(user: User) = {
        DB.withConnection { implicit connection =>
            SQL(
                """
                INSERT INTO users(
                  id, 
                  email, 
                  username,
                  first_name,
                  last_name,
                  profile_pic_id,
                  mailing_address,
                  gender,
                  date_of_birth,
                  occupation,
                  income,
                  points, 
                  credits, 
                  fb_token, 
                  created_at, 
                  updated_at) 
                VALUES (
                  {id}, 
                  {email}, 
                  {user_name},
                  {first_name},
                  {last_name},
                  {profile_pic_id},
                  {mailing_address},
                  {gender},
                  {date_of_birth},
                  {occupation},
                  {income},
                  {points},
                  {credits},
                  {fb_token},
                  {created_at},
                  {updated_at})
                """
            ).on(
                "id" -> user.id,
                "email" -> user.email,
                "user_name" -> user.userName,
                "first_name" -> user.firstName,
                "last_name"  -> user.lastName,
                "profile_pic_id" -> user.profilePicId,
                "mailing_address" -> user.mailingAddress,
                "gender" -> user.gender,
                "date_of_birth" -> user.dateOfBirth,
                "occupation" -> user.occupation,
                "income" -> user.income,
                "points" -> user.points,
                "credits" -> user.credits,
                "fb_token" -> user.fbToken,
                "created_at" -> user.createdAt.toDate.asInstanceOf[java.util.Date],
                "updated_at" -> user.updatedAt.toDate.asInstanceOf[java.util.Date]
            ).executeUpdate()
        }
    }


    def findAll(): Seq[User] = {
        DB.withConnection { implicit connection =>
            SQL("select * from users").as(User.simple *) 
        }
    }


}

// vim: set ts=4 sw=4 et:
