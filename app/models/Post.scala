package models

import persistence.Mongo

import org.bson.types.ObjectId
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._

case class Post(id: String = ObjectId.get.toString,
                text:Option[String],
                imageId:Option[String],
                coordinates:Option[Tuple2[Double, Double]],
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
  Mongo.posts.ensureIndex(MongoDBObject("timestamp" -> 1, "user_id" -> 1, "group_id" -> 1), "PostIndex")

  def postMapper(rawObject:DBObject):Post={
    val coordinatesList = rawObject.getAs[BasicDBList]("coordinates")
    val coordinates = if (coordinatesList.isDefined){
      Some(coordinatesList.get.get(0).asInstanceOf[Double],
          coordinatesList.get.get(1).asInstanceOf[Double])
    }
    else{
      None
    }
    val post = Post(rawObject.getAs[String]("_id").get, rawObject.getAs[String]("text"), rawObject.getAs[String]("image_id"), 
      coordinates, rawObject.getAs[String]("user_id").get, rawObject.getAs[String]("group_id").get,
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

  def findPostsInGroup(groups:List[Group], user:Option[User]=None, offset:Int=0, limit:Int=20) ={
    val groupIds = groups.map(group => group.id.toString)
    val query = if(user.isDefined){
        (("group_id" $in groupIds) ++ ("user_id" -> user.get.id.toString))
    }
    else{
        ("group_id" $in groupIds)
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

// vim: set ts=4 sw=4 et:
