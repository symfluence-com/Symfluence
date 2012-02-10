package test.models.UserSpec

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

object UserSpec extends Specification {
  import models._

  "User" should {
    "be retrieved by id" in {
      running(FakeApplication()) {

        val user = User.findById("47891d6453f411e1b371040cced6719e").get
        user.email must equalTo("frank47891d6453f411e1b371040cced6719e@email.com")
        user.name must equalTo("Frank47891d6453f411e1b371040cced6719e")
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
          val user = User(email="frank_awesomeness@gmail.com", name="Frank Awesomeness", profilePicId="frankAwesomeness", points=100, credits=100, fbToken="fdjsklfjds")
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
        val post = Post(text=Some("Test Post"), imageId=None, latitude=Some(0.001), longitude=Some(0.002),
          userId= user.id.toString, groupId = user.getGroups.head.id.toString, mainPostId= None, commentIds=None)
        user.post(post)
        user.asInstanceOf[User].getPosts.get.length must equalTo(4)
      }
    }

    "can get all Posts by Users" in {
      running(FakeApplication()){
        val user = User.findById("4789200253f411e1b371040cced6719e")
        user.get.asInstanceOf[User].getPosts.get.length must equalTo(4)
      }
    }

   "can delete post by user" in {
      running(FakeApplication()){
        val user = User.findById("4789200253f411e1b371040cced6719e").get
        val post = user.getPosts.get.head
        user.deletePost(post)
        user.getPosts.get.length must equalTo(3)
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
        true
      }
    }

    "can get Followers" in {
      running(FakeApplication()){
        true
      }
    }

    "can get group" in {
      running(FakeApplication()){
        true
      }
    }


  }

}

