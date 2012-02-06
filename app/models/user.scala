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
    name: String, 
    profilePicId: String, 
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
      val query = MongoDBObject("user_id" -> this.id)
      posts =Some(Mongo.posts.find(query).toList.map(rawObject => Post.postMapper(rawObject)))
      posts
    }
  }

  def post(post:Post) ={
    val rawObject = post.toRawObject
    Mongo.posts.save(rawObject)
  }

  def deletePost(post:Post){
    if(post.userId == this.id.toString){
      Mongo.posts.remove(MongoDBObject("_id" -> post.id))
    }
    else{
      throw new NotAuthorizedException()
    }
  }

  def followUser(user:User){
    val count = DB.withConnection{ implicit connection =>
      SQL(
        "INSERT INTO users_followers(user_id, follower_id) VALUES({user_id}, {follower_id})"
      ).on("user_id" -> this.id, "follower_id" -> user.id).executeUpdate()
    }
    if(count > 0){
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
          AND users.id  = users_followers.follower_id"
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



}

object User {

    val simple = {
        get[Pk[String]]("users.id") ~
        get[String]("users.email") ~
        get[String]("users.name") ~
        get[String]("users.profile_pic_id") ~
        get[Int]("users.points") ~
        get[Int]("users.credits") ~
        get[String]("users.fb_token") ~
        get[Date]("users.created_at") ~
        get[Date]("users.updated_at") map {
            case id ~ email ~ name ~ profilePicId ~ points ~ credits ~ fbToken ~ createdAt ~ updatedAt=> User(id, email, name, profilePicId, points, credits, fbToken, new DateTime(createdAt), new DateTime(updatedAt))
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

    def insert(user: User) = {
        DB.withConnection { implicit connection =>
            SQL(
                """
                INSERT INTO users(id, email, name, profile_pic_id, points, credits, fb_token, created_at, updated_at) 
                VALUES ({id}, {email}, {name}, {profile_pic_id}, {points}, {credits}, {fb_token}, {created_at}, {updated_at})
                """
            ).on(
                "id" -> user.id, 
                "email" -> user.email, 
                "name"-> user.name, 
                "profile_pic_id" -> user.profilePicId,
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
