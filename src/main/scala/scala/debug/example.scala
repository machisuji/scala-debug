package scala.debug

object Example extends Example {
  def main(args: Array[String]): Unit = {
    println("Hallo Welt")

    debug(this)

    println("Auf Wiedersehen Welt")
  }
}

class Example {
  val i = 42
  val guy = Guy("Hans", i)
}

case class Guy(name: String, age: Int) {
  def greeting = s"Hello $name!"

  def greet = println(greeting)
}
