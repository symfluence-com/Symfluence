package test.models.Badge

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

object BadgeSpec extends Specification {
  import models._

  "Badge" should {
    "be retrieved by id" in {
      running(FakeApplication()) {

        val badge = Badge.findById("1").get
        badge.name must equalTo("Badge_Name_1")
        badge.profilePicId must equalTo("Badge_Pic_1")
      }

    }

    "be retrieved by name" in {
      running(FakeApplication()) {

        val badge = Badge.findByName("Badge_Name_1").get
        badge.name must equalTo("Badge_Name_1")
        badge.profilePicId must equalTo("Badge_Pic_1")
      }

    }

    "all be retrieved" in{
      running(FakeApplication()) {
        val  badges = Badge.findAll
        badges.size must equalTo(14)
      }

    }

    "can be inserted" in {
        running(FakeApplication()) {
          val badge = Badge(
            name="Badge_Name_Temp",
            profilePicId="fdsfds",
            description="Badge_Name_Temp_Description")
          Badge.insert(badge) must equalTo(1)
        }
    }

    "can get all Users of Badge" in {
        running(FakeApplication()){
          val badge = Badge.findById("1")
          badge.get.asInstanceOf[Badge].getUsers.get.length must equalTo(10)
        }
    }

    "can remove Badge" in {
        running(FakeApplication()) {
          val badge = Badge.findByName(name="Badge_Name_Temp").get
          badge.delete must equalTo(1)
        }
      }

    "can remove Badge from user" in {
        running(FakeApplication()) {
          true
        }
      }

    "can assign Badge to user" in {
        running(FakeApplication()) {
          true
        }
    }


  }
}

