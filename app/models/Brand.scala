package models;

import org.joda.time._
import org.joda.time.format._

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.UUID


case class Brand(
  id: Pk[Long] = NotAssigned,
  name: String, profilePicId: String,    
  createdAt: DateTime = DateTime.now(DateTimeZone.UTC),
  updatedAt: DateTime = DateTime.now(DateTimeZone.UTC)
){
  private var users:Option[List[User]] = None

  def getUsers={
    1  
  }

  def delete={
    val count = DB.withTransaction{ implicit connection => 
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
        get[Pk[Long]]("Brands.id") ~
        get[String]("Brands.name") ~
        get[String]("Brands.profile_pic_id") map {
            case id  ~ name ~ profilePicId 
             => Brand(id, name, profilePicId)
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
        DB.withTransaction { implicit connection =>
            SQL(
                """
                insert into Brands(id, name, profile_pic_id) 
                values ({id}, {name}, {profile_pic_id})
                """
            ).on(
                "id" -> Brand.id, 
                "name"-> Brand.name, 
                "profile_pic_id" -> Brand.profilePicId
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
