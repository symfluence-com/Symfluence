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

    "can get all Posts by Users" in {
      running(FakeApplication()){
        val user = User.findById("4789200253f411e1b371040cced6719e")
        user.get.asInstanceOf[User].getPosts.get.length must equalTo(10)
      }
    }

    "can Post" in {
      running(FakeApplication()){
        true
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

