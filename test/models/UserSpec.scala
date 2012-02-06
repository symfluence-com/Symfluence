package test.models.UserSpec

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

object UserSpec extends Specification {
  import models._

  "User" should {
    "be retrieved by id" in {
      running(FakeApplication()) {

        val user = User.findById("67326c74500c11e1a2ce040cced6719e").get
        user.email must equalTo("frank67326c74500c11e1a2ce040cced6719e@email.com")
        user.name must equalTo("Frank67326c74500c11e1a2ce040cced6719e")
        user.profilePicId must equalTo("4f2e9d5735e8121735000051")
        user.points must equalTo(11)
        user.credits must equalTo(50)
        user.fbToken must equalTo("67326c9c500c11e1a2ce040cced6719e")

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
        val user = User.findById("67326e22500c11e1a2ce040cced6719e")
        user.get.asInstanceOf[User].getPosts.get.size must equalTo(10)
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

