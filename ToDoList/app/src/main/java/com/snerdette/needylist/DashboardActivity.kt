package com.snerdette.needylist

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.snerdette.needylist.DTO.ToDo
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    lateinit var dbHandler : DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        dbHandler = DBHandler(this)
        fab_dashboard.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_dashboard, null)
            val toDoName = view.findViewById<EditText>(R.id.ev_todo)
            dialog.setPositiveButton("Add") { _: DialogInterface, _: Int ->
               if(toDoName.text.isNotEmpty()){
                   val toDo = ToDo()
                   toDo.name = toDoName.text.toString()
                   dbHandler.addToDo(toDo)
               }
            }
            dialog.setNegativeButton("Cancel") {
                _: DialogInterface,
                _: Int -> 
            }
            dialog.show()
        }
    }
}