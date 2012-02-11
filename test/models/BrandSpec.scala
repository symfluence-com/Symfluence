package test.models.Brand

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

object BrandSpec extends Specification {
  import models._

  "Brand" should {
    "be retrieved by id" in {
      running(FakeApplication()) {

        val brand = Brand.findById("1").get
        brand.name must equalTo("Brand_Name_1")
        brand.profilePicId must equalTo("Brand_Pic_1")
      }

    }

    "be retrieved by name" in {
      running(FakeApplication()) {

        val brand = Brand.findByName("Brand_Name_1").get
        brand.name must equalTo("Brand_Name_1")
        brand.profilePicId must equalTo("Brand_Pic_1")
      }

    }

    "all be retrieved" in{
      running(FakeApplication()) {
        val brands = Brand.findAll
        brands.size must equalTo(14)
      }

    }

    "be inserted" in {
        running(FakeApplication()) {
          val brand = Brand(
            name="Brand_Name_Temp",
            profilePicId="fdsjklsf",
            description="Brand_Name_Temp_Description")
          Brand.insert(brand) must equalTo(1)
        }
    }

    "be removable in" in {
        running(FakeApplication()) {
          val brand = Brand.findByName(name="Brand_Name_Temp").get
          brand.delete must equalTo(1)
        }
      }

    "be retrievable of all its Users" in {
        running(FakeApplication()){
          val brand = Brand.findById("1")
          brand.get.asInstanceOf[Brand].getUsers.get.length must equalTo(10)
        }
    }


    "be assignable to user" in {
        running(FakeApplication()) {
          true
        }
    }
    "be removable from user" in {
        running(FakeApplication()) {
          true
        }
      }



  }
}

