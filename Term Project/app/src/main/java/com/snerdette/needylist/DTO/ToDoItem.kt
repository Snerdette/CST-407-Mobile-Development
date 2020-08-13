package com.snerdette.needylist.DTO

import java.time.LocalDate
import java.util.*

class ToDoItem () {
    var id : Long = -1
    var toDoId : Long = -1
    var itemName = ""
    var date : String? = null
    var isCompleted = false
}