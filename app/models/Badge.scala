package models;

import org.joda.time._
import org.joda.time.format._

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.UUID


case class Badge(
  id: Pk[String] = Id(UUID.randomUUID.toString.replace("-", "")),
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
          SELECT users.* FROM users, users_Badges WHERE users_Badges.Badge_id = {Badge_id} AND
          users.id = users_Badges.user_id
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
        get[Pk[String]]("Badges.id") ~
        get[String]("Badges.name") ~
        get[String]("Badges.profile_pic_id") ~
        get[String]("Badges.description") map {
            case id  ~ name ~ profilePicId ~ description => Badge(id, name, profilePicId, description)
        }
    }

    def findById(id: String): Option[Badge] = {
        DB.withConnection { implicit connection =>
        SQL("select * from Badges where id = {id}").on("id" -> id).as(Badge.simple.singleOpt)
        }
    }

    def findByName(name: String): Option[Badge] = {
        DB.withConnection { implicit connection =>
        SQL("select * from Badges where name = {name}").on("name" -> name).as(Badge.simple.singleOpt)
        }
    }

    def insert(Badge: Badge) = {
        DB.withConnection { implicit connection =>
            SQL(
                """
                insert into Badges(id, name, profile_pic_id, description) 
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
            SQL("select * from Badges").as(Badge.simple *) 
        }
    }



}

// vim: set ts=4 sw=4 et:
