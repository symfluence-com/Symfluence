package test.models.UserSpec

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

object UserSpec extends Specification {
  import models._

  "User" should {
    "be retrieved by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val user = User.findById("frankleeisagenius").get
        user.email must equalTo("frank.lee@gmail.com")
        user.name must equalTo("Frank Lee")
        user.profilePicId must equalTo("frankleespectacularpix")
        user.points must equalTo(23)
        user.credits must equalTo(45)
        user.fbToken must equalTo("frankleefbtoken")

      }

    }

    "can be inserted" in {
        running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
          true
        }

    }

    "all be retrieved" in{
        running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
          true
        }

    }

  }

}

