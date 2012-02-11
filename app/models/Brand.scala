package models;

import org.joda.time._
import org.joda.time.format._

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.UUID


case class Brand(
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
          SELECT users.* FROM users, users_Brands WHERE users_Brands.Brand_id = {Brand_id} AND
          users.id = users_Brands.user_id
          """
        ).on("Brand_id" -> this.id).as(User.simple *)
      })
      users
    }

  }

  def delete={
    val count = DB.withConnection{ implicit connection => 
        SQL(
          """ 
            DELETE FROM Brands where id = {Brand_id}
          """
        ).on("Brand_id"  -> this.id).executeUpdate 
    }
  count
  }

}

object Brand{
    val simple = {
        get[Pk[String]]("Brands.id") ~
        get[String]("Brands.name") ~
        get[String]("Brands.profile_pic_id") ~
        get[String]("Brands.description") map {
            case id  ~ name ~ profilePicId ~ description => Brand(id, name, profilePicId, description)
        }
    }

    def findById(id: String): Option[Brand] = {
        DB.withConnection { implicit connection =>
        SQL("select * from Brands where id = {id}").on("id" -> id).as(Brand.simple.singleOpt)
        }
    }

    def findByName(name: String): Option[Brand] = {
        DB.withConnection { implicit connection =>
        SQL("select * from Brands where name = {name}").on("name" -> name).as(Brand.simple.singleOpt)
        }
    }

    def insert(Brand: Brand) = {
        DB.withConnection { implicit connection =>
            SQL(
                """
                insert into Brands(id, name, profile_pic_id, description) 
                values ({id}, {name}, {profile_pic_id}, {description})
                """
            ).on(
                "id" -> Brand.id, 
                "name"-> Brand.name, 
                "profile_pic_id" -> Brand.profilePicId,
                "description" -> Brand.description
            ).executeUpdate()
        }
    }

  
    def findAll(): Seq[Brand] = {
        DB.withConnection { implicit connection =>
            SQL("select * from Brands").as(Brand.simple *) 
        }
    }



}

// vim: set ts=4 sw=4 et:
