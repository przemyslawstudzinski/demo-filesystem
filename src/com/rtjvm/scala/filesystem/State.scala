package com.rtjvm.scala.filesystem

import com.rtjvm.scala.file.Directory

class State(val root: Directory, val wd: Directory, val output: String) {

  def show(): Unit = {
    println(output)
    print(State.SHELL_TOKEN)
  }

  def setMessage(message: String): State = {
    // call apply method in companion object
    State(root, wd, message)
  }
}

object State {
  val SHELL_TOKEN = "$ "

  def apply(root: Directory, wd: Directory, output: String = ""): State = {
    new State(root, wd, output)
  }

}