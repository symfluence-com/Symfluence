package models;

import org.joda.time._
import org.joda.time.format._

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.UUID


case class Merchant(
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
            DELETE FROM merchants where id = {merchants_id}
          """
        ).on("merchants_id"  -> this.id).executeUpdate 
    }
  count
  }

}

object Merchant{
    val simple = {
        get[Pk[Long]]("merchants.id") ~
        get[String]("merchants.name") ~
        get[String]("merchants.profile_pic_id") map {
            case id  ~ name ~ profilePicId 
             => Merchant(id, name, profilePicId)
        }
    }

    def findById(id: String): Option[Merchant] = {
        DB.withConnection { implicit connection =>
        SQL("select * from merchants where id = {id}").on("id" -> id).as(Merchant.simple.singleOpt)
        }
    }

    def findByName(name: String): Option[Merchant] = {
        DB.withConnection { implicit connection =>
        SQL("select * from merchants where name = {name}").on("name" -> name).as(Merchant.simple.singleOpt)
        }
    }

    def insert(merchant: Merchant) = {
        DB.withTransaction { implicit connection =>
            SQL(
                """
                insert into merchants(id, name, profile_pic_id) 
                values ({id}, {name}, {profile_pic_id})
                """
            ).on(
                "id" -> merchant.id, 
                "name"-> merchant.name, 
                "profile_pic_id" -> merchant.profilePicId
            ).executeUpdate()
        }
    }

  
    def findAll(): Seq[Merchant] = {
        DB.withConnection { implicit connection =>
            SQL("select * from merchants").as(Merchant.simple *) 
        }
    }



}

// vim: set ts=4 sw=4 et:
