package test.models.PostSpec

import org.specs2.mutable._
import org.specs2.matcher.MatchResult

import play.api.test._
import play.api.test.Helpers._

object PostSpec extends Specification {
  import models._


  def withPost[A](testFunction:(User, Post, Group)=> MatchResult[A])={
    val user = User.findAll.head
    val group = Group.findAll.head
    user.joinGroup(group)
    val post = Post(text=Some("Test Post"),
      imageId=None, 
      coordinates=Some((0.001, 0.002)),
      userId= user.id.toString,
      groupId =group.id.toString,
      mainPostId= None,
      commentIds=None,
      tags=None)
    user.post(post)
    val result =testFunction(user, post, group)
    user.deletePost(post)
    user.leaveGroup(group)
    result
  }


  "Post" should {
    "be retrieved by id" in {
      running(FakeApplication()) {
        val testFunction = (user:User, post:Post, group:Group)=>{
           val foundPost = Post.findById(post.id).get
           foundPost must equalTo(post)
        }
        withPost[Post](testFunction)
      }

    }

    "be retrieve by group" in {
      running(FakeApplication()){
        val testFunction = (user:User, post:Post, group:Group) => {
            val posts = Post.findPostsInGroup(group)
            posts.size must equalTo(1)
        }
        withPost[Int](testFunction)
      }

    }

    "all be retrieved" in{
      running(FakeApplication()) {
        val testFunction = (user:User, post:Post, group:Group) => {
          val posts = Post.findAll
          posts.get.size must equalTo(1)
        }
        withPost[Int](testFunction)
      }

    }

  }

}

// vim: set ts=4 sw=4 et:
