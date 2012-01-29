package models;

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.UUID


case class Group(
  id: Pk[String] = Id(UUID.randomUUID.toString),
  name: String, profilePicId: String, description: String
){
  private var users:Option[List[User]] = None

  def getUsers={
    if (users.isDefined){
      users
    }
    else{
      DB.withConnection{ implicit connection => 
        SQL(
          """
          SELECT users.* FROM users, users_groups WHERE users_groups.group_id = {group_id} AND
          users.id = users_groups.user_id
          """
        ).on("group_id" -> this.id).as(User.simple *)
      }
    }

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
            SQL("select * from users").as(Group.simple *) 
        }
    }

}

// vim: set ts=4 sw=4 et:
