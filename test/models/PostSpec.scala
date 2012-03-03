package test.models.PostSpec

import org.specs2.mutable._
import org.specs2.matcher.MatchResult

import play.api.test._
import play.api.test.Helpers._

import scala.util.Random

import java.util.Date

object PostSpec extends Specification {
  import models._


  def withPost[A](testFunction:(User, List[Post], List[Category])=> MatchResult[A])={
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
    val allCategories = Category.findAll
    val categories = allCategories.take(Random.nextInt(allCategories.size)+1).toList
    categories.foreach(category => user.joinCategory(category))
    val posts = 1.to(10).map( _ => {
      Post(text=Some("Test Post"),
        imageId=None, 
        coordinates=Some((0.001, 0.002)),
        userId= user.id.toString,
        categoryId =categories(Random.nextInt(categories.size)).id.toString,
        mainPostId= None,
        commentIds=None,
        tags=None)
    }).toList
    posts.foreach(post =>{
      user.post(post)
    })
    val result =testFunction(user, posts, categories)
    posts.foreach(post => user.deletePost(post))
    categories.foreach(category => user.leaveCategory(category))
    user.delete
    result
  }


  "Post" should {
    "be retrieved by id" in {
      running(FakeApplication()) {
        val testFunction = (user:User, posts:List[Post], categories:List[Category])=>{
           val foundPost = Post.findById(posts.head.id).get
           foundPost must equalTo(posts.head)
        }
        withPost[Post](testFunction)
      }

    }

    "be retrieve by category" in {
      running(FakeApplication()){
        val testFunction = (user:User, posts:List[Post], categories:List[Category]) => {
            val posts = Post.findPostsInCategory(categories)
            posts.size must equalTo(posts.size)
        }
        withPost[Int](testFunction)
      }

    }

    "all be retrieved" in{
      running(FakeApplication()) {
        val testFunction = (user:User, posts:List[Post], categories:List[Category]) => {
          val foundPosts = Post.findAll()
          foundPosts.get.size must equalTo(posts.size)
        }
        withPost[Int](testFunction)
      }

    }

  }

}

// vim: set ts=4 sw=4 et:
