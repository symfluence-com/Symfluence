package models

import org.bson.types.ObjectId
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._

case class Post(id: ObjectId, text:Option[String], image_id:Option[String], 
  latitude:Option[Double], longitude:Option[Double], userId:String, comments:Option[List[String]])

object Post{
  def postMapper(rawObject:DBObject):Post={
    Post(rawObject.getAs[ObjectId]("_id").get, rawObject.getAs[String]("text"), rawObject.getAs[String]("image_id"), 
      rawObject.getAs[Double]("latitude"), rawObject.getAs[Double]("longitude"), rawObject.getAs[String]("user_id").get,
      rawObject.getAs[List[String]]("comment_ids"))
  }
}

// vim: set ts=4 sw=4 et:
