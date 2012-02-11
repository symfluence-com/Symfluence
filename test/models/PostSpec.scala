package test.models.PostSpec

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

object PostSpec extends Specification {
  import models._

  "Post" should {
    "be retrieved by id" in {
      running(FakeApplication()) {
           val user = User.findAll.head
           val post = Post(text=Some("Test Post"), imageId=None, latitude=Some(0.001), longitude=Some(0.002), userId= user.id.toString, groupId = user.getGroups.head.id.toString, mainPostId= None, commentIds=None, tags=None)
          user.post(post)
          val foundPost = Post.findById(post.id)

        foundPost must equalTo(post)
      }

    }

    "all be retrieved" in{
      running(FakeApplication()) {
        val posts = Post.findAll
        posts.size must equalTo(14)
      }

    }

    "be inserted" in {
      running(FakeApplication()) {
        true
      }
    }

    "be removable in" in {
      running(FakeApplication()) {
        val post = Post.findAll.get.head
        post.delete must equalTo(1)
      }
    }

    "be retrievable of all its Users" in {
      running(FakeApplication()){
        true
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

// vim: set ts=4 sw=4 et:
