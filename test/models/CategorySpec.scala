package test.models.CategorySpec

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

object CategorySpec extends Specification {
  import models._

  "Category" should {
    "be retrieved by id" in {
      running(FakeApplication()) {

        val category = Category.findById("47891bac53f411e1b371040cced6719e").get
        category.name must equalTo("Frank Fantastic Group47891bac53f411e1b371040cced6719e")
        category.profilePicId must equalTo("4f352adb2d26dd38be000003")
        category.description must equalTo("Frank Awesome Group47891bac53f411e1b371040cced6719e")
      }

    }

    "be retrieved by name" in {
      running(FakeApplication()) {

        val category = Category.findByName("Frank Fantastic Group47891bac53f411e1b371040cced6719e").get
        category.profilePicId must equalTo("4f352adb2d26dd38be000003")
        category.description must equalTo("Frank Awesome Group47891bac53f411e1b371040cced6719e")
      }

    }

    "all be retrieved" in{
      running(FakeApplication()) {
        val categories = Category.findAll
        categories.size must equalTo(10)
      }

    }

    "can be inserted" in {
        running(FakeApplication()) {
          val category = Category(name="Paper Planes", profilePicId="pic_paperplane", description="Paper Plane Description")
          Category.insert(category) must equalTo(1)
        }
    }

    "can get all Users in Category" in {
        running(FakeApplication()){
          val category = Category.findById("47891c6053f411e1b371040cced6719e")
          category.get.asInstanceOf[Category].getUsers.get.length must equalTo(10)
        }
    }

    "can remove Category" in {
        running(FakeApplication()) {
          val category = Category.findByName(name="Paper Planes").get
          category.delete must equalTo(1)
        }
      }

  }
}

