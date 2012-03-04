package models

import persistence.Mongo

import org.bson.types.ObjectId
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._

import java.util.Date


trait BasePost{
    def id:String
    def postType:String
    def text:Option[String]
    def imageId:Option[String]
    def coordinates:Option[Tuple2[Double, Double]]
    def userId:String
    def categoryId:String
    def mainPostId:Option[String]
    def commentIds:Option[List[String]]
    def tags:Option[List[String]]
    def timestamp:Long
}

case class Post(id: String = ObjectId.get.toString,
                postType:String="normal",
                text:Option[String],
                imageId:Option[String],
                coordinates:Option[Tuple2[Double, Double]],
                userId:String,
                categoryId:String, 
                mainPostId:Option[String],
                commentIds:Option[List[String]], 
                tags:Option[List[String]],
                timestamp:Long=System.currentTimeMillis)
            extends BasePost{

  def comments:List[Post]={
    val q = "_id" $in commentIds
    Mongo.posts.find(q).toList.map(rawObject => Post.postMapper(rawObject))
  }


  def toRawObject={
    val builder = MongoDBObject.newBuilder
    builder += "_id" -> this.id
    builder += "post_type" -> this.postType
    builder += "text" -> this.text
    builder += "image_id" -> this.imageId
    if(this.coordinates.isDefined){
      val coordinateListBuilder = MongoDBList.newBuilder
      coordinateListBuilder += this.coordinates.get._1
      coordinateListBuilder += this.coordinates.get._2
      builder += "coordinates" -> coordinateListBuilder.result
    }
    else{
        builder += "coordinates" -> None
    }
    builder += "user_id" -> this.userId
    builder += "category_id" -> this.categoryId
    val commentListBuilder =  MongoDBList.newBuilder
    if(this.commentIds.isDefined){
        val commentListBuilder =  MongoDBList.newBuilder
        this.commentIds.get.foreach(id => { 
            commentListBuilder += id
        })
        builder += "commentIds" -> commentListBuilder.result
    }
    else{
        builder += "commentIds" -> None
    }
    if(this.tags.isDefined){
        val tagsListBuilder = MongoDBList.newBuilder
        this.tags.get.foreach(tag =>{
                tagsListBuilder += tag
            })
        builder += "tags" -> tagsListBuilder.result
    }
    else{
      builder += "tags" -> None
    }

    builder += "main_this_id" -> this.mainPostId
    builder += "timestamp"  -> this.timestamp
    builder.result
  }

}

object Post{

  Mongo.posts.ensureIndex(MongoDBObject("coordinates" -> "2d"), "locationIndex")
  Mongo.posts.ensureIndex(MongoDBObject("timestamp" -> 1, "user_id" -> 1, "category_id" -> 1), "PostIndex")

  def postMapper(rawObject:DBObject):Post={
    val coordinatesList = rawObject.getAs[BasicDBList]("coordinates")
    val coordinates = if (coordinatesList.isDefined){
      Some(coordinatesList.get.get(0).asInstanceOf[Double],
          coordinatesList.get.get(1).asInstanceOf[Double])
    }
    else{
      None
    }
    val post = Post(rawObject.getAs[String]("_id").get, rawObject.getAs[String]("post_type").get,
      rawObject.getAs[String]("text"), rawObject.getAs[String]("image_id"), 
      coordinates, rawObject.getAs[String]("user_id").get, rawObject.getAs[String]("category_id").get,
      rawObject.getAs[String]("main_post_id"), rawObject.getAs[List[String]]("comment_ids"),
      rawObject.getAs[List[String]]("tags"), rawObject.getAs[Long]("timestamp").get.toLong)
    post
  }

  def findAll(offset:Int=0, limit:Int=10):Option[List[Post]] = {
    Some(
        Mongo.posts.find().
        sort(MongoDBObject("timestamp" -> -1)).
        skip(offset).
        limit(limit).
        toList.map(rawObject => Post.postMapper(rawObject)))
  }

  def findPostsInCategory(categories:List[Category], user:Option[User]=None, offset:Int=0, limit:Int=20) ={
    val categoryIds = categories.map(category => category.id.toString)
    val query = if(user.isDefined){
        (("category_id" $in categoryIds) ++ ("user_id" -> user.get.id.toString))
    }
    else{
        ("category_id" $in categoryIds)
    }
    val postsQuery = Mongo.posts.find(query).limit(limit).skip(offset).sort(MongoDBObject("timestamp"-> -1)) 
    Some(postsQuery.toList.map(rawObject=>Post.postMapper(rawObject)))
  }



  def findById(id:String):Option[Post]={
    val rawObject = Mongo.posts.findOne(MongoDBObject("_id" -> id))
    if (rawObject.isDefined){
        Some(Post.postMapper(rawObject.get))
    }
    else{
        None
    }

  }
}


case class CouponPost(id: String = ObjectId.get.toString,
                postType:String="coupon",
                headline:String,
                text:Option[String],
                imageId:Option[String],
                coordinates:Option[Tuple2[Double, Double]],
                userId:String,
                categoryId:String, 
                mainPostId:Option[String],
                commentIds:Option[List[String]], 
                tags:Option[List[String]],
                validTill:Date,
                timestamp:Long=System.currentTimeMillis)
            extends BasePost{

}

// vim: set ts=4 sw=4 et:
