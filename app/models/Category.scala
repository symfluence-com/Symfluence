package models;

import org.joda.time._
import org.joda.time.format._

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.UUID


case class Category(
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
          SELECT users.* FROM users, users_categories WHERE users_categories.category_id = {category_id} AND
          users.id = users_categories.user_id
          """
        ).on("category_id" -> this.id).as(User.simple *)
      })
      users
    }

  }

  def delete={
    val count = DB.withTransaction{ implicit connection => 
        SQL(
          """ 
            DELETE FROM categories where id = {category_id}
          """
        ).on("category_id"  -> this.id).executeUpdate 
    }
    count
  }

}

object Category{
    val simple = {
        get[Pk[String]]("categories.id") ~
        get[String]("categories.name") ~
        get[String]("categories.profile_pic_id") ~
        get[String]("categories.description") map {
            case id  ~ name ~ profilePicId ~ description => Category(id, name, profilePicId, description)
        }
    }

    def findById(id: String): Option[Category] = {
        DB.withConnection { implicit connection =>
        SQL("select * from categories where id = {id}").on("id" -> id).as(Category.simple.singleOpt)
        }
    }

    def findByName(name: String): Option[Category] = {
        DB.withConnection { implicit connection =>
            SQL("select * from categories where name = {name}").on("name" -> name).as(Category.simple.singleOpt)
        }
    }

    def insert(category: Category) = {
        DB.withTransaction { implicit connection =>
            SQL(
                """
                insert into categories(id, name, profile_pic_id, description) 
                values ({id}, {name}, {profile_pic_id}, {description})
                """
            ).on(
                "id" -> category.id, 
                "name"-> category.name, 
                "profile_pic_id" -> category.profilePicId,
                "description" -> category.description
            ).executeUpdate()
        }
    }


    def findAll(): Seq[Category] = {
        DB.withConnection { implicit connection =>
            SQL("select * from categories").as(Category.simple *) 
        }
    }



}

// vim: set ts=4 sw=4 et:
