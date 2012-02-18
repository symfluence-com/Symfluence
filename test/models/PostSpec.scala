package test.models.PostSpec

import org.specs2.mutable._
import org.specs2.matcher.MatchResult

import play.api.test._
import play.api.test.Helpers._

import scala.util.Random

import java.util.Date

object PostSpec extends Specification {
  import models._


  def withPost[A](testFunction:(User, List[Post], List[Group])=> MatchResult[A])={
      val user = User( email="frank_awesomeness@gmail.com", 
          userName = "frank",
          firstName= "Frank Awesomeness", 
          lastName= "Lee",
          profilePicId = "frankAwesomeness", 
          mailingAddress = Some("singapore"),
          gender = Some("M"),
          dateOfBirth = Some(new Date()),
          occupation = Some("Boss"),
          income = Some(10000000.0),
          points=100, 
          credits=100, 
          fbToken="fdjsklfjds"
      )
    User.insert(user)
    val allGroups = Group.findAll
    val groups = allGroups.take(Random.nextInt(allGroups.size)+1).toList
    groups.foreach(group => user.joinGroup(group))
    val posts = 1.to(10).map( _ => {
      Post(text=Some("Test Post"),
        imageId=None, 
        coordinates=Some((0.001, 0.002)),
        userId= user.id.toString,
        groupId =groups(Random.nextInt(groups.size)).id.toString,
        mainPostId= None,
        commentIds=None,
        tags=None)
    }).toList
    posts.foreach(post =>{
      user.post(post)
    })
    val result =testFunction(user, posts, groups)
    posts.foreach(post => user.deletePost(post))
    groups.foreach(group => user.leaveGroup(group))
    user.delete
    result
  }


  "Post" should {
    "be retrieved by id" in {
      running(FakeApplication()) {
        val testFunction = (user:User, posts:List[Post], groups:List[Group])=>{
           val foundPost = Post.findById(posts.head.id).get
           foundPost must equalTo(posts.head)
        }
        withPost[Post](testFunction)
      }

    }

    "be retrieve by group" in {
      running(FakeApplication()){
        val testFunction = (user:User, posts:List[Post], groups:List[Group]) => {
            val posts = Post.findPostsInGroup(groups)
            posts.size must equalTo(posts.size)
        }
        withPost[Int](testFunction)
      }

    }

    "all be retrieved" in{
      running(FakeApplication()) {
        val testFunction = (user:User, posts:List[Post], groups:List[Group]) => {
          val foundPosts = Post.findAll()
          foundPosts.get.size must equalTo(posts.size)
        }
        withPost[Int](testFunction)
      }

    }

  }

}

// vim: set ts=4 sw=4 et:
