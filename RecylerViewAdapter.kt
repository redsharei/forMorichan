package com.morichan.studyschedule


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_today_edit.view.*
import kotlinx.android.synthetic.main.item_task.view.*

//----------書き換え-----------
open class CustomAdapter(
    private val task: OrderedRealmCollection<TaskCreate>,
   // private val deadlinetask: MutableList<String>,
    private var listener: OnItemClickListener,
    private val autoUpdate: Boolean
) :
    RealmRecyclerViewAdapter<TaskCreate, CustomAdapter.CustomViewHolder>(
        task,
        autoUpdate
    ) {
//-------------------------------
    
    //lateinit var listener: OnItemClickListener  //----------削除-----------

    // ViewHolderクラス(別ファイルに書いてもOK)
    class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val titleView = view.textView2
        val deadlineview = view.deadline
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.item_task, parent, false)
        return CustomViewHolder(item)
    }

    // recyclerViewのコンテンツのサイズ
    override fun getItemCount(): Int {
        return task.size
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        //----------書き換え-----------
        val task: TaskCreate = task?.get(position) ?: return
        holder.view.textView2.text = task.title   // TaskCreateクラスのtitleを持って来る
        holder.view.deadline.text =  task.calendarHour.toString() + "時" + task.calendarMinute.toString() + "分まで"
        //----------------------------

        /* -------------解説------------------
          49行目
            example ?: return
          は、

            if(example == null) {
                return
            }
          と同じ意味
         */


        // タップしたとき
        holder.view.setOnClickListener {
            listener.onItemClickListener(task)
            Log.d("position", position.toString())

        }

    }

    interface OnItemClickListener {
        fun onItemClickListener(item: TaskCreate)
    }


    // リスナー
/*  ----------削除-----------
     fun setOnItemClickListener(listener: OnItemClickListener){
         this.listener = listener
     }

*/

}
