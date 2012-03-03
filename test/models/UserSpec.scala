package test.models.UserSpec

import org.specs2.mutable._
import org.specs2.matcher.MatchResult

import play.api.test._
import play.api.test.Helpers._

import java.util.Date

object UserSpec extends Specification {
  import models._

  def withCategory[A](testFunction:(User, Category)=> MatchResult[A])={
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
    val category = Category.findAll.tail.head
    user.joinCategory(category)
    val result =testFunction(user, category)
    user.leaveCategory(category)
    user.delete
    result
  }

  def withPost[A](testFunction:(User, Category, Post) => MatchResult[A])={
    val addPostFn=(user:User, category:Category) =>{
      val post = Post(
        text=Some("Test Post"),
        imageId=None, 
        coordinates=Some(0.001, 0.002),
        userId= user.id.toString,
        categoryId = category.id.toString,
        mainPostId= None,
        commentIds=None,
        tags=None)

        user.post(post)
        val result =testFunction(user, category, post)
        user.deletePost(post)
        result
    }
    withCategory[A](addPostFn)
  }

  "User" should {
    "be retrieved by id" in {
      running(FakeApplication()) {

        val user = User.findById("47891d6453f411e1b371040cced6719e").get
        user.email must equalTo("frank47891d6453f411e1b371040cced6719e@email.com")
        user.firstName must equalTo("Frank47891d6453f411e1b371040cced6719e")
        user.profilePicId must equalTo("4f352adb2d26dd38be000015")
        user.points must equalTo(62)
        user.credits must equalTo(95)
        user.fbToken must equalTo("47891d8c53f411e1b371040cced6719e")

      }

    }

    "all be retrieved" in{
      running(FakeApplication()) {
        val users = User.findAll
        users.size must equalTo(10)
      }

    }

    "can be inserted" in {
        running(FakeApplication()) {
          val user = 
            User( email="frank_awesomeness@gmail.com", 
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
          User.insert(user) must equalTo(1)
        }

    }

    "can find user by email" in {
      running(FakeApplication()){
        val user = User.findByEmail("frank_awesomeness@gmail.com").get
        user mustNotEqual None

      }
    }

    "can Post" in {
      running(FakeApplication()){
        val user = User.findById("4789200253f411e1b371040cced6719e").get
        val oldPostCount = user.getPosts.get.size
        val post = Post(text=Some("Test Post"), imageId=None, coordinates=Some(0.001, 0.002),
          userId= user.id.toString, categoryId = user.getCategories.head.id.toString, mainPostId= None, commentIds=None, tags=None)
        user.post(post)
        user.asInstanceOf[User].getPosts.get.length must equalTo(oldPostCount+1)
      }
    }

    "can Fav Post" in {
      running(FakeApplication()){
        val testfn = (user:User, category:Category, post:Post) => {
          val oldfavpostcount = user.getFavPosts.size
          user.favPost(post)
          val result = user.getFavPosts.size must equalTo(oldfavpostcount+1)
          user.unfavPost(post)
          result
        }
        withPost(testfn)
      }
    }

    "can unFav Post" in {
       running(FakeApplication()){
        val testfn = (user:User, category:Category, post:Post) => {
          user.favPost(post)
          val oldfavpostcount = user.getFavPosts.size
          user.unfavPost(post)
          user.getFavPosts.size must equalTo(oldfavpostcount-1)
        }
        withPost(testfn)
      }
    }

    "can dislike Post" in {
      running(FakeApplication()){
        val testfn = (user:User, category:Category, post:Post) => {
          val olddislikepostcount = user.getDislikePosts.size
          user.dislikePost(post)
          val result = user.getDislikePosts.size must equalTo(olddislikepostcount+1)
          user.unDislikePost(post)
          result
        }
        withPost(testfn)
      }
    }


    "can undislike Post" in {
      running(FakeApplication()){
        val testfn = (user:User, category:Category, post:Post) => {
          user.dislikePost(post)
          val olddislikepostcount = user.getDislikePosts.size
          user.unDislikePost(post)
          user.getDislikePosts.size must equalTo(olddislikepostcount-1)
        }
        withPost(testfn)
      }

    }

   "can delete post by user" in {
      running(FakeApplication()){
        val user = User.findById("4789200253f411e1b371040cced6719e").get
        val userPosts = user.getPosts.get
        val oldPostCount = userPosts.size
        val post = userPosts.head
        user.deletePost(post)
        user.getPosts.get.length must equalTo(oldPostCount-1)
      }
   }

    "can delete user" in {
      running(FakeApplication()){
        val user = User.findByEmail("frank_awesomeness@gmail.com").get
        user.delete must equalTo(1)

      }
    }


    "can follow User" in {
      running(FakeApplication()){
        val user1 = User.findById("4789200253f411e1b371040cced6719e").get
        val user2 = User.findById("47891fc653f411e1b371040cced6719e").get
        user1.followUser(user2)
        user1.getFollowers.get.length must equalTo(1);
      }
    }

    "can get Followers" in {
      running(FakeApplication()){
        val user = User.findById("4789200253f411e1b371040cced6719e").get
        user.getFollowers.get.length must equalTo(1);
      }
    }

    "can delete Followers" in {
      running(FakeApplication()){
        val user1 = User.findById("4789200253f411e1b371040cced6719e").get
        val user2 = User.findById("47891fc653f411e1b371040cced6719e").get
        user1.unfollowUser(user2)
        user1.getFollowers.get.length must equalTo(0);
      }
    }

    "can get category" in {
      running(FakeApplication()){
        val user1 = User.findById("4789200253f411e1b371040cced6719e").get
        user1.getCategories.size must equalTo(1);
      }
    }

    "can join category" in {
      running(FakeApplication()){
        val testFunction = (user:User, category:Category) => {
          category.getUsers.size must equalTo(1)
          user.getCategories.size must equalTo(1)
        }
        withCategory(testFunction)
      }
    }
  }

}

