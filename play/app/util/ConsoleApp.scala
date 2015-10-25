package util

import play.api.{Play, Mode, Environment, ApplicationLoader, Application}

object ConsoleApp {

  def start = {
    val env = Environment(new java.io.File("."), this.getClass.getClassLoader, Mode.Dev)
    val context = ApplicationLoader.createContext(env)
    val loader = ApplicationLoader(context)
    val app = loader.load(context)
    Play.start(app)
    app
  }

  /**
   * Stops the running Play instance.
   */
  def stop(app :Application) = Play.stop(app)
}
