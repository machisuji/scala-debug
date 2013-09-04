package scala

import scala.debug.REPL

package object debug {
  /**
   * Would be good to somehow register all debug calls and
   * print them globally at the start of the application so that it
   * is obvious when you still have debug statements left.
   *
   * e.g.
   *
   * [scala-debug] debug statement at com.mycompany.db.Database.scala:283
   *
   * Also as to how to have all variables available:
   *
   * Use reflection, pass in `this` and forward calls?
   */
  def debug(context: AnyRef) = REPL.open(context)
  def debug() = REPL.open()
}
