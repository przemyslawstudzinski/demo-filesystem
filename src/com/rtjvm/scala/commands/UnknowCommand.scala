package com.rtjvm.scala.commands

import com.rtjvm.scala.filesystem.State

class UnknowCommand extends Command {
  override def apply(state: State): State = {
    state.setMessage("Command not found")
  }
}
