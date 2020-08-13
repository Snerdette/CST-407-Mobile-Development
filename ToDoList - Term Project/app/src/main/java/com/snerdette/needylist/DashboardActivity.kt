package com.snerdette.needylist

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.snerdette.needylist.DTO.ToDo
import com.snerdette.needylist.DTO.ToDoItem
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.dialog_dashboard.*

class DashboardActivity : AppCompatActivity() {

    lateinit var dbHandler : DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(dashboard_toolbar)
        title = "Dashboard"
        dbHandler = DBHandler(this)
        rv_dashboard.layoutManager = LinearLayoutManager(this)
        fab_dashboard.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Add ToDo")
            val view = layoutInflater.inflate(R.layout.dialog_dashboard, null)
            val toDoName = view.findViewById<EditText>(R.id.ev_todo)
            var toDoDate = view.findViewById<DatePicker>(R.id.datePicker1)
            val year = toDoDate.year.toString()
            val yearInt = toDoDate.year
            val month = toDoDate.month.toString()
            val monthInt = toDoDate.month
            val day = toDoDate.dayOfMonth.toString()
            val dayInt = toDoDate.dayOfMonth
            val newDate = month.plus('/').plus(day).plus('/').plus(year)
            datePicker1?.updateDate(yearInt, monthInt, dayInt)
                dialog.setView(view)
            dialog.setPositiveButton("Add") { _: DialogInterface, _: Int ->
               if(toDoName.text.isNotEmpty()){
                   val toDo = ToDo()
                   var toDoDate = view.findViewById<DatePicker>(R.id.datePicker1)
                   val year = toDoDate.year.toString()
                   val yearInt = toDoDate.year
                   val month = toDoDate.month.toString()
                   val monthInt = toDoDate.month
                   val day = toDoDate.dayOfMonth.toString()
                   val dayInt = toDoDate.dayOfMonth
                   val newDate = month.plus('/').plus(day).plus('/').plus(year)
                   datePicker1?.updateDate(yearInt, monthInt, dayInt)
                   toDo.name = toDoName.text.toString()
                   toDo.dueDate = newDate
                   //toDoDate.dayOfMonth
                   dbHandler.addToDo(toDo)
                   refreshList()
               }
            }
            dialog.setNegativeButton("Cancel") {
                _: DialogInterface,
                _: Int ->
            }
            dialog.show()
        }
    }

    fun updateToDo(toDo: ToDo) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Update ToDo")
        val view = layoutInflater.inflate(R.layout.dialog_dashboard, null)
        val toDoName = view?.findViewById<EditText>(R.id.ev_todo)
        val toDoDate = view.findViewById<DatePicker>(R.id.datePicker1)
        val year = toDoDate.year.toString()
        val yearInt = toDoDate.year
        val month = toDoDate.month.toString()
        val monthInt = toDoDate.month
        val day = toDoDate.dayOfMonth.toString()
        val dayInt = toDoDate.dayOfMonth
        val newDate = month.plus('/').plus(day).plus('/').plus(year)
        datePicker1?.updateDate(yearInt, monthInt, dayInt)
        toDoName?.setText(toDo.name)

        dialog.setView(view)
        dialog.setPositiveButton("Update") { _: DialogInterface, _: Int ->
            if (toDoName!!.text.isNotEmpty()) {
                toDo.name = toDoName?.text.toString()
                toDo.dueDate = newDate
                dbHandler.updateToDo(toDo)
                refreshList()
            }
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
        }
        dialog.show()
    }

    override fun onResume(){
        refreshList()
        super.onResume()
    }

    private fun refreshList() {
        rv_dashboard.adapter = DashboardAdapter(this, dbHandler.getToDos())
    }

    class DashboardAdapter (val activity: DashboardActivity, val list: MutableList<ToDo>) :
        RecyclerView.Adapter<DashboardAdapter.ViewHolder>(){
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_dashboard, p0, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
            holder.toDoName.text = list[p1].name

            holder.toDoName.setOnClickListener{
                val intent = Intent(activity, ItemActivity::class.java)
                intent.putExtra(INTENT_TODO_ID, list[p1].id)
                intent.putExtra(INTENT_TODO_NAME, list[p1].name)
                intent.putExtra(INTENT_TODO_DUE_DATE, list[p1].dueDate)
                activity.startActivity(intent)
            }

            holder.menu.setOnClickListener{
                val popup = PopupMenu(activity, holder.menu)
                popup.inflate(R.menu.dashboard_child)
                popup.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_edit->{
                            activity.updateToDo(list[p1])
                        }
                        R.id.menu_delete->{
                            val dialog = AlertDialog.Builder(activity)
                            dialog.setTitle("Are you sure")
                            dialog.setMessage("Do you want to delete this task?")
                            dialog.setPositiveButton("Continue") { _: DialogInterface, _: Int ->
                                activity.dbHandler.deleteToDo(list[p1].id)
                                activity.refreshList()
                            }
                            dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
                            }
                            dialog.show()
                        }
                        R.id.menu_mark_as_completed->{
                            activity.dbHandler.updateToDoItemCompletedStatus(list[p1].id,  true)
                        }
                        R.id.menu_reset->{
                            activity.dbHandler.updateToDoItemCompletedStatus(list[p1].id,  false)
                        }
                    }
                    true
                }
                popup.show()
            }
        }

        class ViewHolder(v:View) : RecyclerView.ViewHolder(v) {
            val toDoName: TextView = v.findViewById(R.id.tv_todo_name)
            val menu : ImageView = v.findViewById(R.id.iv_menu)
        }
    }
}