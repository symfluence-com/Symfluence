package models

import persistence.Mongo

import org.bson.types.ObjectId
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._

case class Post(id: ObjectId = ObjectId.get, text:Option[String], image_id:Option[String], 
  latitude:Option[Double], longitude:Option[Double], userId:String, mainPostId:Option[ObjectId], 
  commentIds:Option[List[ObjectId]], timestamp:Long=System.currentTimeMillis) {

  def comments:List[Post]={
    val q = "_id" $in commentIds
    Mongo.posts.find(q).toList.map(rawObject => Post.postMapper(rawObject))
  }

  
  def toRawObject={
    val builder = MongoDBObject.newBuilder
    builder += "text" -> this.text
    builder += "image_id" -> this.image_id
    builder += "latitude" -> this.latitude
    builder += "longitude" -> this.longitude
    builder += "user_id" -> this.userId
    val commentListBuilder =  MongoDBList.newBuilder
    this.commentIds.getOrElse(List()).foreach{id => 
    commentListBuilder +=  id
    }
    builder += "main_this_id" -> this.mainPostId
    builder += "comment_ids" -> commentListBuilder.result
    builder += "timestamp"  -> this.timestamp
    builder.result
  }


}

object Post{
  def postMapper(rawObject:DBObject):Post={
    Post(rawObject.getAs[ObjectId]("_id").get, rawObject.getAs[String]("text"), rawObject.getAs[String]("image_id"), 
      rawObject.getAs[Double]("latitude"), rawObject.getAs[Double]("longitude"), rawObject.getAs[String]("user_id").get,
      rawObject.getAs[ObjectId]("main_post_id"), rawObject.getAs[List[ObjectId]]("comment_ids"),
      rawObject.getAs[Long]("timestamp").get)
  }
}

// vim: set ts=4 sw=4 et:
