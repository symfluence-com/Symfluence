package models

import persistence.Mongo

import org.bson.types.ObjectId
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._

case class Post(id: String = ObjectId.get.toString,
                text:Option[String],
                imageId:Option[String],
                latitude:Option[Double], 
                longitude:Option[Double], 
                userId:String,
                groupId:String, 
                mainPostId:Option[String],
                commentIds:Option[List[String]], 
                tags:Option[List[String]],
                timestamp:Long=System.currentTimeMillis) {

  def comments:List[Post]={
    val q = "_id" $in commentIds
    Mongo.posts.find(q).toList.map(rawObject => Post.postMapper(rawObject))
  }


  def toRawObject={
    val builder = MongoDBObject.newBuilder
    builder += "_id" -> this.id
    builder += "text" -> this.text
    builder += "image_id" -> this.imageId
    builder += "latitude" -> this.latitude
    builder += "longitude" -> this.longitude
    builder += "user_id" -> this.userId
    builder += "group_id" -> this.groupId
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
    // this.commentIds.getOrElse(List()).foreach{id => 
    //     commentListBuilder +=  id
    // }
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
    // builder += "comment_ids" -> commentListBuilder.result
    // builder += "tags" -> tagsListBuilder.result
    builder += "timestamp"  -> this.timestamp
    builder.result
  }

  def delete{
    1
  }



}

object Post{
  def postMapper(rawObject:DBObject):Post={
    Post(rawObject.getAs[String]("_id").get, rawObject.getAs[String]("text"), rawObject.getAs[String]("image_id"), 
      rawObject.getAs[Double]("latitude"), rawObject.getAs[Double]("longitude"), rawObject.getAs[String]("user_id").get, rawObject.getAs[String]("group_id").get,
      rawObject.getAs[String]("main_post_id"), rawObject.getAs[List[String]]("comment_ids"),
      rawObject.getAs[List[String]]("tags"), rawObject.getAs[Long]("timestamp").get.toLong)
  }

  def findAll:Option[List[Post]] = {
    Some(Mongo.posts.find.toList.map(rawObject => Post.postMapper(rawObject)))
  }

  def findPostsInGroup(group:Group) ={
    val posts = Some(Mongo.posts.find(MongoDBObject("group_id" -> group.id)).toList.map(rawObject=>
      Post.postMapper(rawObject)))
    posts

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

// vim: set ts=4 sw=4 et:
