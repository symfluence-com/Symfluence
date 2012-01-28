package persistence

import com.mongodb.casbah.Imports._


import play.api.Play.current


object Mongo{
  val connection = MongoConnection(current.configuration.getString("mongo.url").getOrElse("localhost"), current.configuration.getInt("mongo.port").getOrElse(27017))
  val db = connection(current.configuration.getString("mongo.db").getOrElse("symfluence"))
  val posts = db("posts")

}

// vim: set ts=4 sw=4 et:
