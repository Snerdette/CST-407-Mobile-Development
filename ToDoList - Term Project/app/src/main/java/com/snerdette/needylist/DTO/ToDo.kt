package com.snerdette.needylist.DTO

class ToDo {
    var id: Long = -1
    var name = ""
    var createdAt = ""
    var dueDate : String? = null
    var items: MutableList<ToDoItem> = ArrayList()
}