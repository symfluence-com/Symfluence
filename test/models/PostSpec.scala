package test.models.PostSpec

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

object PostSpec extends Specification{
  import models._

  "Post" should {
    "be retrieve by id" in {
      running(FakeApplication()){
        true
      }
    }

    "find all posts" in {
      running(FakeApplication()){
        true
      }
    }

  }

}

// vim: set ts=4 sw=4 et:
