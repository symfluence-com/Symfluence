package test.models.UserSpec

import org.specs2.mutable._
import org.specs2.matcher.MatchResult

import play.api.test._
import play.api.test.Helpers._

import java.util.Date

object UserSpec extends Specification {
  import models._

  def withGroup[A](testFunction:(User, Group)=> MatchResult[A])={
    val user = User( email="frank_awesomeness@gmail.com", 
                userName = "frank",
                firstName= "Frank Awesomeness", 
                lastName= "Lee",
                profilePicId = "frankAwesomeness", 
                mailingAddress = Some("singapore"),
                gender = Some("M"),
                dateOfBirth = Some(new Date()),
                occupation = Some("Boss"),
                income = Some(10000000.0),
                points=100, 
                credits=100, 
                fbToken="fdjsklfjds"
          )
    User.insert(user)
    val group = Group.findAll.tail.head
    user.joinGroup(group)
    val result =testFunction(user, group)
    user.leaveGroup(group)
    user.delete
    result
  }

  "User" should {
    "be retrieved by id" in {
      running(FakeApplication()) {

        val user = User.findById("47891d6453f411e1b371040cced6719e").get
        user.email must equalTo("frank47891d6453f411e1b371040cced6719e@email.com")
        user.firstName must equalTo("Frank47891d6453f411e1b371040cced6719e")
        user.profilePicId must equalTo("4f352adb2d26dd38be000015")
        user.points must equalTo(62)
        user.credits must equalTo(95)
        user.fbToken must equalTo("47891d8c53f411e1b371040cced6719e")

      }

    }

    "all be retrieved" in{
      running(FakeApplication()) {
        val users = User.findAll
        users.size must equalTo(10)
      }

    }

    "can be inserted" in {
        running(FakeApplication()) {
          val user = 
            User( email="frank_awesomeness@gmail.com", 
                  userName = "frank",
                  firstName= "Frank Awesomeness", 
                  lastName= "Lee",
                  profilePicId = "frankAwesomeness", 
                  mailingAddress = Some("singapore"),
                  gender = Some("M"),
                  dateOfBirth = Some(new Date()),
                  occupation = Some("Boss"),
                  income = Some(10000000.0),
                  points=100, 
                  credits=100, 
                  fbToken="fdjsklfjds"
            )
          User.insert(user) must equalTo(1)
        }

    }

    "can find user by email" in {
      running(FakeApplication()){
        val user = User.findByEmail("frank_awesomeness@gmail.com").get
        user mustNotEqual None

      }
    }

    "can Post" in {
      running(FakeApplication()){
        val user = User.findById("4789200253f411e1b371040cced6719e").get
        val post = Post(text=Some("Test Post"), imageId=None, coordinates=Some(0.001, 0.002),
          userId= user.id.toString, groupId = user.getGroups.head.id.toString, mainPostId= None, commentIds=None, tags=None)
        user.post(post)
        user.asInstanceOf[User].getPosts.get.length must equalTo(1)
      }
    }

    "can get all Posts by Users" in {
      running(FakeApplication()){
        val user = User.findById("4789200253f411e1b371040cced6719e")
        user.get.asInstanceOf[User].getPosts.get.length must equalTo(1)
      }
    }

   "can delete post by user" in {
      running(FakeApplication()){
        val user = User.findById("4789200253f411e1b371040cced6719e").get
        val post = user.getPosts.get.head
        user.deletePost(post)
        user.getPosts.get.length must equalTo(0)
      }
   }

    "can delete user" in {
      running(FakeApplication()){
        val user = User.findByEmail("frank_awesomeness@gmail.com").get
        user.delete must equalTo(1)

      }
    }


    "can follow User" in {
      running(FakeApplication()){
        val user1 = User.findById("4789200253f411e1b371040cced6719e").get
        val user2 = User.findById("47891fc653f411e1b371040cced6719e").get
        user1.followUser(user2)
        user1.getFollowers.get.length must equalTo(1);
      }
    }

    "can get Followers" in {
      running(FakeApplication()){
        val user = User.findById("4789200253f411e1b371040cced6719e").get
        user.getFollowers.get.length must equalTo(1);
      }
    }

    "can delete Followers" in {
      running(FakeApplication()){
        val user1 = User.findById("4789200253f411e1b371040cced6719e").get
        val user2 = User.findById("47891fc653f411e1b371040cced6719e").get
        user1.unfollowUser(user2)
        user1.getFollowers.get.length must equalTo(0);
      }
    }

    "can get group" in {
      running(FakeApplication()){
        val user1 = User.findById("4789200253f411e1b371040cced6719e").get
        user1.getGroups.size must equalTo(1);
      }
    }

    "can join group" in {
      running(FakeApplication()){
        val testFunction = (user:User, group:Group) => {
          group.getUsers.size must equalTo(1)
          user.getGroups.size must equalTo(1)
        }
        withGroup(testFunction)
      }
    }
  }

}

