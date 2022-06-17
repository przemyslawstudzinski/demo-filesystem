package com.rtjvm.scala.commands

import com.rtjvm.scala.filesystem.State

trait Command {
  def apply(state: State): State
}

object Command {
  val MKDIR = "mkdir"

  def emptyCommand: Command = new Command {
    override def apply(state: State): State = state
  }

  def incompleteCommand(name: String): Command = new Command {
    override def apply(state: State): State = {
      state.setMessage(name + ": incomplete command!")
    }
  }


  def from(input: String): Command = {
    val tokens: Array[String] = input.split(" ")
    if (input.isEmpty || tokens.isEmpty) {
      emptyCommand
    } else if (tokens(0) == MKDIR) {
      if (tokens.length < 2) {
        incompleteCommand(MKDIR)
      } else {
        new Mkdir(tokens(1))
      }
    } else {
      new UnknowCommand
    }
  }
}