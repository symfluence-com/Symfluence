package models;

import org.joda.time._
import org.joda.time.format._

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.UUID


case class Badge(
  id: Pk[Long] = NotAssigned,
  name: String, profilePicId: String, description: String,    
  createdAt: DateTime = DateTime.now(DateTimeZone.UTC),
  updatedAt: DateTime = DateTime.now(DateTimeZone.UTC)
){
  private var users:Option[List[User]] = None

  def getUsers={
    if (users.isDefined){
      users
    }
    else{
      users = Some(DB.withConnection{ implicit connection => 
        SQL(
          """
          SELECT users.* FROM users, users_badges WHERE users_Badges.badge_id = {Badge_id} AND
          users.id = users_badges.user_id
          """
        ).on("Badge_id" -> this.id).as(User.simple *)
      })
      users
    }

  }

  def delete={
    val count = DB.withConnection{ implicit connection => 
        SQL(
          """ 
            DELETE FROM Badges where id = {Badge_id}
          """
        ).on("Badge_id"  -> this.id).executeUpdate 
    }
  count
  }

}

object Badge{
    val simple = {
        get[Pk[Long]]("badges.id") ~
        get[String]("badges.name") ~
        get[String]("badges.profile_pic_id") ~
        get[String]("badges.description") map {
            case id  ~ name ~ profilePicId ~ description => Badge(id, name, profilePicId, description)
        }
    }

    def findById(id: Int): Option[Badge] = {
        DB.withConnection { implicit connection =>
        SQL("select * from badges where id = {id}").on("id" -> id).as(Badge.simple.singleOpt)
        }
    }

    def findByName(name: String): Option[Badge] = {
        DB.withConnection { implicit connection =>
        SQL("select * from badges where name = {name}").on("name" -> name).as(Badge.simple.singleOpt)
        }
    }

    def insert(Badge: Badge) = {
        DB.withConnection { implicit connection =>
            SQL(
                """
                insert into badges(id, name, profile_pic_id, description) 
                values ({id}, {name}, {profile_pic_id}, {description})
                """
            ).on(
                "id" -> Badge.id, 
                "name"-> Badge.name, 
                "profile_pic_id" -> Badge.profilePicId,
                "description" -> Badge.description
            ).executeUpdate()
        }
    }

  
    def findAll(): Seq[Badge] = {
        DB.withConnection { implicit connection =>
            SQL("select * from badges").as(Badge.simple *) 
        }
    }



}

// vim: set ts=4 sw=4 et:
