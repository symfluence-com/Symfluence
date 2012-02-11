package test.models.Post

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

object PostSpec extends Specification {
  import models._

  "Post" should {
    "be retrieved by id" in {
      running(FakeApplication()) {

        val Post = Post.findById("1").get
        Post.name must equalTo("Post_Name_1")
        Post.profilePicId must equalTo("Post_Pic_1")
      }

    }

    "be retrieved by name" in {
      running(FakeApplication()) {

        val Post = Post.findByName("Post_Name_1").get
        Post.name must equalTo("Post_Name_1")
        Post.profilePicId must equalTo("Post_Pic_1")
      }

    }

    "all be retrieved" in{
      running(FakeApplication()) {
        val Posts = Post.findAll
        Posts.size must equalTo(14)
      }

    }

    "be inserted" in {
        running(FakeApplication()) {
          val Post = Post(
            name="Post_Name_Temp",
            PostPic="Post_Name_Temp",
            description="Post_Name_Temp_Description")
          Post.insert(Post) must equalTo(1)
        }
    }

    "be removable in" in {
        running(FakeApplication()) {
          val Post = Post.findByName(name="Post_Name_Temp").get
          Post.delete must equalTo(1)
        }
      }

    "be retrievable of all its Users" in {
        running(FakeApplication()){
          val Post = Post.findById("1")
          Post.get.asInstanceOf[Post].getUsers.get.length must equalTo(10)
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

    "be updateable" in {
      running(FakeApplication()) {
        true
      }
    }



  }
}

