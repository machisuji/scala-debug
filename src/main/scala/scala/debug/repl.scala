package scala.debug

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.ILoop

import java.io.File
import java.net.URL
import java.net.URLClassLoader

object REPL {

  val settings = {
    val set = new Settings
    set.usejavacp.value = true
    set
  }

  def addClasspath(file: String) {
    val loader = ClassLoader.getSystemClassLoader.asInstanceOf[URLClassLoader]
    val cl = loader.getClass
    val addURL = cl.getDeclaredMethod("addURL", Array[Class[_]](classOf[URL]): _*)

    addURL.setAccessible(true)
    addURL.invoke(loader, Array[AnyRef](new File(file).toURI.toURL))
  }

  def open(context: AnyRef = null) {
    new ScalaDebugLoop(if (context ne null) Some(context) else None).process(settings)
  }
}

class ScalaDebugLoop(context: Option[AnyRef]) extends ILoop {
  override def prompt = "\nscala-debug> "

  override def printWelcome() {
    this echo """
      scala-debug 0.1:
    """
    .split("\n").map(_.trim).init.mkString("\n")
  }

  addThunk {
    intp.beQuietDuring {
      context.foreach { con =>
        // later it should be the actual type; but: how to properly load class into ILoop?
        intp.bind("self", "AnyRef", con)

        // later all context methods and fields should be available just like that
        intp.bind("get", "(String) => AnyRef", (fun: String) => {
          val method = (con.getClass.getDeclaredMethods ++ con.getClass.getMethods).find(_.getName.endsWith(fun))

          method.map(m => m.invoke(con)).getOrElse(null)
        })
      }
    }
  }
}
