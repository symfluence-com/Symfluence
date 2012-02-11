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
        brand.name must equalTo("Brand_Name_Temp1")
        brand.profilePicId must equalTo("fdsjklsf")
      }

    }

    "be retrieved by name" in {
      running(FakeApplication()) {

        val brand = Brand.findByName("Brand_Name_Temp1").get
        brand.name must equalTo("Brand_Name_Temp1")
        brand.profilePicId must equalTo("fdsjklsf")
      }

    }

    "all be retrieved" in{
      running(FakeApplication()) {
        val brands = Brand.findAll
        brands.size must equalTo(3)
      }

    }

    "be inserted" in {
        running(FakeApplication()) {
          val brand = Brand(
            name="Brand_Name_Temp11",
            profilePicId="fdsjklsf")
          Brand.insert(brand) must equalTo(1)
        }
    }

    "be removable in" in {
        running(FakeApplication()) {
          val brand = Brand.findByName(name="Brand_Name_Temp11").get
          brand.delete must equalTo(1)
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

