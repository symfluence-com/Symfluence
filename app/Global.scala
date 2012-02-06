import play.api._

import java.util.TimeZone

object Global extends GlobalSettings {

override def onStart(app: Application) {
    Logger.info("Application has started")
    val default = TimeZone.getDefault();
    try
    {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
    finally
    {
        TimeZone.setDefault(default);
    }
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

}

// vim: set ts=4 sw=4 et:
