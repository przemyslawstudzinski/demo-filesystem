package com.rtjvm.scala.commands

import com.rtjvm.scala.file.{DirEntry, Directory}
import com.rtjvm.scala.filesystem.State

class Mkdir(val name: String) extends Command {

  def checkIllegal(name: String): Boolean = {
    name.contains(".")
  }

  override def apply(state: State): State = {
    val wd = state.wd
    if (wd.hasEntry(name)) {
      state.setMessage("Entry " + name + " already exists!")
    } else if (name.contains(Directory.SEPARATOR)) {
      state.setMessage(name + " must not contain separator!")
    } else if (checkIllegal(name)) {
      state.setMessage(name + ": illegal entry name!")
    } else {
      doMkDir(state, name)
    }
  }

  def doMkDir(state: State, str: String): State = {
    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
      if (path.isEmpty) {
        currentDirectory.addEntry(newEntry)
      } else {
        // a/b
        //  /c
        //    /d
        // currentDirectory = /a
        // path = "b"
        val oldEntry = currentDirectory.findEntry(path.head).asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))


      }

    }

    val wd = state.wd
    val fullPath = wd.path

    // 1. All the directories in the full path
    val allDirsInPath = wd.getAllFoldersInPath

    // 2. Create new directory entry in the wd
    val newDir = Directory.empty(wd.path, name)

    // 3. Update the whole directory structure starting from the root
    // (The directory structure is IMMUTABLE)
    val newRoot = updateStructure(state.root, allDirsInPath, newDir)

    // 4. Find new working directory INSTANCE given wd's path, in the NEW directory structure
    val newWd = newRoot.findDescendant(allDirsInPath)

    State(newRoot, newWd)
  }
}
