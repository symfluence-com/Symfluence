package test.models.GroupSpec

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

object GroupSpec extends Specification {
  import models._

  "Group" should {
    "be retrieved by id" in {
      running(FakeApplication()) {

        val group = Group.findById("47891bac53f411e1b371040cced6719e").get
        group.name must equalTo("Frank Fantastic Group47891bac53f411e1b371040cced6719e")
        group.profilePicId must equalTo("4f352adb2d26dd38be000003")
        group.description must equalTo("Frank Awesome Group47891bac53f411e1b371040cced6719e")
      }

    }

    "be retrieved by name" in {
      running(FakeApplication()) {

        val group = Group.findByName("Frank Fantastic Group47891bac53f411e1b371040cced6719e").get
        group.profilePicId must equalTo("4f352adb2d26dd38be000003")
        group.description must equalTo("Frank Awesome Group47891bac53f411e1b371040cced6719e")
      }

    }

    "all be retrieved" in{
      running(FakeApplication()) {
        val groups = Group.findAll
        groups.size must equalTo(10)
      }

    }

    "can be inserted" in {
        running(FakeApplication()) {
          val group = Group(name="Paper Planes", profilePicId="pic_paperplane", description="Paper Plane Description")
          Group.insert(group) must equalTo(1)
        }
    }

    "can get all Users in Group" in {
        running(FakeApplication()){
          val group = Group.findById("47891c6053f411e1b371040cced6719e")
          group.get.asInstanceOf[Group].getUsers.get.length must equalTo(10)
        }
    }

    "can remove Group" in {
        running(FakeApplication()) {
          val group = Group.findByName(name="Paper Planes").get
          group.delete must equalTo(1)
        }
      }

  }
}

