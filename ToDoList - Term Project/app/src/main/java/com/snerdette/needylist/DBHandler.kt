package com.snerdette.needylist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.snerdette.needylist.DTO.ToDo
import com.snerdette.needylist.DTO.ToDoItem
import java.security.AccessControlContext

class DBHandler(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){
    override fun onCreate(db: SQLiteDatabase) {
        val createToDoTable =
            "CREATE TABLE $TABLE_TODO (" +
                "$COL_ID integer PRIMARY KEY AUTOINCREMENT,"+
                "$COL_CREATED_AT datetime DEFAULT CURRENT_TIMESTAMP,"+
                "$COL_NAME varchar);"

        val createToDoItemTable =
            "CREATE TABLE $TABLE_TODO_ITEM (" +
                "$COL_ID integer PRIMARY KEY AUTOINCREMENT,"+
                "$COL_CREATED_AT datetime DEFAULT CURRENT_TIMESTAMP,"+
                "$COL_TODO_ID integer,"+
                "$COL_ITEM_NAME varchar,"+
                "$COL_IS_COMPLETED integer);"

        db.execSQL(createToDoTable)
        db.execSQL(createToDoItemTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun addToDo(toDo: ToDo): Boolean{
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, toDo.name)
        cv.put(COL_NAME, toDo.dueDate)
        val result = db.insert(TABLE_TODO, null, cv)
        return result != (-1).toLong()
    }

    fun updateToDo(toDo: ToDo) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, toDo.name)
        cv.put(COL_NAME, toDo.dueDate)
        db.update(TABLE_TODO, cv, "$COL_ID=?", arrayOf(toDo.id.toString()))
    }

    fun deleteToDo(todoId: Long){
        val db = writableDatabase
        db.delete(TABLE_TODO_ITEM, "$COL_TODO_ID=?", arrayOf(todoId.toString()))
        db.delete(TABLE_TODO, "$COL_ID=?", arrayOf(todoId.toString()))
    }

    fun updateToDoItemCompletedStatus(todoId: Long, isCompleted: Boolean){
        val db = writableDatabase
        val queryResult = db.rawQuery("SELECT * from $TABLE_TODO_ITEM WHERE $COL_TODO_ID=$todoId", null)

        if(queryResult.moveToFirst()){
            do {
                val item = ToDoItem()
                item.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                item.toDoId = queryResult.getLong(queryResult.getColumnIndex(COL_TODO_ID))
                item.itemName = queryResult.getString(queryResult.getColumnIndex(COL_ITEM_NAME))
                item.isCompleted = isCompleted
                updateToDoItem(item)
            } while (queryResult.moveToNext())
        }
        queryResult.close()
    }

    fun getToDos() : MutableList<ToDo>{
        val result : MutableList<ToDo> = ArrayList()
        val db = readableDatabase
        val queryResult = db.rawQuery("SELECT * from $TABLE_TODO", null)
        if(queryResult.moveToFirst()){
            do{
                val todo = ToDo()
                todo.id  = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                todo.name  = queryResult.getString(queryResult.getColumnIndex(COL_NAME))
                todo.dueDate  = queryResult.getString(queryResult.getColumnIndex(COL_NAME))
                result.add(todo)
            } while (queryResult.moveToNext())
        }
        queryResult.close()

        return result
    }

    fun addToDoItem(item : ToDoItem) : Boolean {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_ITEM_NAME, item.itemName)
        cv.put(COL_TODO_ID, item.toDoId)
        if(item.isCompleted)
            cv.put(COL_IS_COMPLETED, true) // or 1?
        else
            cv.put(COL_IS_COMPLETED, false) // or 0?

        val result = db.insert(TABLE_TODO_ITEM, null, cv)

        return result != (-1).toLong()
    }

    fun updateToDoItem(item : ToDoItem) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_ITEM_NAME, item.itemName)
        cv.put(COL_TODO_ID, item.toDoId)
        cv.put(COL_IS_COMPLETED, true)

        db.update(TABLE_TODO_ITEM, cv,"$COL_ID=?", arrayOf(item.id.toString()))
    }

    fun deleteToDoItem(itemId: Long) {
        val db = writableDatabase
        db.delete(TABLE_TODO_ITEM, "$COL_ID=?", arrayOf(itemId.toString()))
    }

    fun getToDoItems(todoId : Long) : MutableList<ToDoItem>{
        val result : MutableList<ToDoItem> = ArrayList()

        val db = readableDatabase
        val queryResult = db.rawQuery("SELECT * from $TABLE_TODO_ITEM WHERE $COL_TODO_ID=$todoId", null)

        if(queryResult.moveToFirst()){
            do {
                val item = ToDoItem()
                item.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                item.toDoId = queryResult.getLong(queryResult.getColumnIndex(COL_TODO_ID))
                item.itemName = queryResult.getString(queryResult.getColumnIndex(COL_ITEM_NAME))
                item.isCompleted = queryResult.getInt(queryResult.getColumnIndex(COL_IS_COMPLETED)) == 1
                result.add(item)
            } while (queryResult.moveToNext())
        }
        queryResult.close()
        return  result
    }
}