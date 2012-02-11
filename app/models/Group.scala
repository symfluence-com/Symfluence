package models;

import org.joda.time._
import org.joda.time.format._

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.UUID


case class Group(
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
          SELECT users.* FROM users, users_groups WHERE users_groups.group_id = {group_id} AND
          users.id = users_groups.user_id
          """
        ).on("group_id" -> this.id).as(User.simple *)
      })
      users
    }

  }

  def delete={
    val count = DB.withConnection{ implicit connection => 
        SQL(
          """ 
            DELETE FROM groups where id = {group_id}
          """
        ).on("group_id"  -> this.id).executeUpdate 
    }
  count
  }

}

object Group{
    val simple = {
        get[Pk[String]]("groups.id") ~
        get[String]("groups.name") ~
        get[String]("groups.profile_pic_id") ~
        get[String]("groups.description") map {
            case id  ~ name ~ profilePicId ~ description => Group(id, name, profilePicId, description)
        }
    }

    def findById(id: String): Option[Group] = {
        DB.withConnection { implicit connection =>
        SQL("select * from groups where id = {id}").on("id" -> id).as(Group.simple.singleOpt)
        }
    }

    def findByName(name: String): Option[Group] = {
        DB.withConnection { implicit connection =>
        SQL("select * from groups where name = {name}").on("name" -> name).as(Group.simple.singleOpt)
        }
    }

    def insert(group: Group) = {
        DB.withConnection { implicit connection =>
            SQL(
                """
                insert into groups(id, name, profile_pic_id, description) 
                values ({id}, {name}, {profile_pic_id}, {description})
                """
            ).on(
                "id" -> group.id, 
                "name"-> group.name, 
                "profile_pic_id" -> group.profilePicId,
                "description" -> group.description
            ).executeUpdate()
        }
    }

  
    def findAll(): Seq[Group] = {
        DB.withConnection { implicit connection =>
            SQL("select * from groups").as(Group.simple *) 
        }
    }



}

// vim: set ts=4 sw=4 et:
