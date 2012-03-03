package test.models.Merchant

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

object MerchantSpec extends Specification {
  import models._

  "Merchant" should {
    "be retrieved by id" in {
      running(FakeApplication()) {

        val merchant = Merchant.findById("1").get
        merchant.name must equalTo("Merchant_Name_Temp1")
        merchant.profilePicId must equalTo("fdsjklsf")
      }

    }

    "be retrieved by name" in {
      running(FakeApplication()) {

        val merchant = Merchant.findByName("Merchant_Name_Temp1").get
        merchant.name must equalTo("Merchant_Name_Temp1")
        merchant.profilePicId must equalTo("fdsjklsf")
      }

    }

    "all be retrieved" in{
      running(FakeApplication()) {
        val merchants = Merchant.findAll
        merchants.size must equalTo(3)
      }

    }

    "be inserted" in {
        running(FakeApplication()) {
          val merchant = Merchant(
            name="Merchant_Name_Temp11",
            profilePicId="fdsjklsf")
          Merchant.insert(merchant) must equalTo(1)
        }
    }

    "be removable in" in {
        running(FakeApplication()) {
          val merchant = Merchant.findByName(name="Merchant_Name_Temp11").get
          merchant.delete must equalTo(1)
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

