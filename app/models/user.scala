package models;

import persistence._

import play.api.db._
import play.api.Play.current

import com.mongodb.casbah.Imports._

import anorm._
import anorm.SqlParser._

import java.util.UUID

case class User(
  id: Pk[String]= Id(UUID.randomUUID.toString), 
  email: String, name: String, profilePicId: String, points: Int, credits: Int, fbToken: String){

  def posts ={
    val query = MongoDBObject("user_id" -> this.id)
    val cursor = Mongo.posts.find(query)
    cursor
    
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
        get[String]("users.fb_token") map {
            case id ~ email ~ name ~ profilePicId ~ points ~ credits ~ fbToken => User(id, email, name, profilePicId, points, credits, fbToken)
        }
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
                insert into users(id, email, name, profile_pic_id, points, credits, fb_token) 
                values ({id}, {email}, {name}, {profile_pic_id}, {points}, {credits}, {fb_token})
                """
            ).on(
                "id" -> user.id, 
                "email" -> user.email, 
                "name"-> user.name, 
                "profile_pic_id" -> user.profilePicId,
                "points" -> user.points,
                "credits" -> user.credits,
                "fb_token" -> user.fbToken
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
