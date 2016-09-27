package controllers

/**
  * Created by @gutiory on 15/9/16.
  */

import javax.inject.Inject

import scala.concurrent.Future
import scala.concurrent._
import ExecutionContext.Implicits.global

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Logger
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo._
import play.modules.reactivemongo.json._
import reactivemongo.play.json.collection.JSONCollection



class EventController @Inject() (val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with MongoController with ReactiveMongoComponents{

  def collection: JSONCollection = db.collection[JSONCollection]("Event")

  def index = Action{ Ok("works")}

  def create(id: Int, pkgName: String, eventType: Int, timeTag: Double) = Action.async{
    val json = Json.obj(
      "id" -> id,
      "pkgName" -> pkgName,
      "eventType" -> eventType,
      "timeTag" -> timeTag)
    collection.insert(json).map(lastError => Ok("Mongo LastError: %s".format(lastError)))
  }

  def createFromJson = Action.async(parse.json) { request =>
    import play.api.libs.json.Reads._
    /*
     * request.body is a JsValue.
     * There is an implicit Writes that turns this JsValue as a JsObject,
     * so you can call insert() with this JsValue.
     * (insert() takes a JsObject as parameter, or anything that can be
     * turned into a JsObject using a Writes.)
     */
    val transformer: Reads[JsObject] =
      Reads.jsPickBranch[JsNumber](__ \ "id") and
        Reads.jsPickBranch[JsString](__ \ "pkgName") and
        Reads.jsPickBranch[JsNumber](__ \ "eventType") and
        Reads.jsPickBranch[JsNumber](__ \ "timeTag") reduce

    request.body.transform(transformer).map { result =>
      collection.insert(result).map { lastError =>
        Logger.debug(s"Successfully inserted with LastError: $lastError")
        Created
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }
}
