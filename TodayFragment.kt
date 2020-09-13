package com.morichan.studyschedule

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.morichan.studyschedule.databinding.FragmentTodayBinding
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_today.*
import java.time.LocalDate
import java.util.function.LongFunction


class TodayFragment : Fragment(){

    val realm: Realm = Realm.getDefaultInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    //***
   // var localDate  =LocalDate.now()

    var task: MutableList<TaskCreate> = mutableListOf()

    var maxInttask: MutableList<Int> = mutableListOf()
    var progressInttask: MutableList<Int> = mutableListOf()

    var maxIntGraph = 0
    var progressIntGraph = 0
    var localDatedateInt  =0

    /*----------削除-----------
    var deletetitle=""



    var deadlinetask:MutableList<String> = mutableListOf()

    var titleTask :MutableList<String> = mutableListOf()
    private val adapter = CustomAdapter(titleTask,deadlinetask,true)

    ------------------------*/





    val titleTask = readAll() //------------追加----------------


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_today, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskplus.setOnClickListener {
            val intent = Intent(activity, TodayListCreateActivity::class.java)
            startActivity(intent)
        }
    }






    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        //***
     //   localDate = LocalDate.now()


        var query: RealmQuery<TaskCreate> = realm.where(TaskCreate::class.java)
        query.equalTo("complete",false)
            .equalTo("radioButtoncheck", "today")
            .or().equalTo("radioButtoncheck","everyday")

        var result: RealmResults<TaskCreate> = query.findAll()





        //titleTask.clear()  ---------削除----------

        //deadlinetask.clear()  ---------削除----------

        maxInttask.clear()
        progressInttask.clear()

        //***
     //   var number = localDate.dayOfYear

        var number = 0





        for(item in result) {
         /*   -----------削除------------
            titleTask.addAll(listOf(item.title))
            deadlinetask.add(
                item.calendarHour.toString()
                        + "時" + item.calendarMinute.toString() + "分まで"
            )
        --------------------------------- */
            maxInttask.add(item.seekBarMaxInt)
            progressInttask.add(item.seekBarprogress)

//            if (item.radioButtoncheck.equals("everyday")&&
//                    item.calendarDate !==number){
//                item.seekBarprogress = 0
//            }


        }

        task.addAll(result)


        maxIntGraph = maxInttask.sum()
        progressIntGraph = progressInttask.sum()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        /* ------------削除-------------
        adapter.setOnItemClickListener(
            object : CustomAdapter.OnItemClickListener {
                override fun onItemClickListener(
                    view: View, position: Int, clickedText: String
                ) {
                    val intent = Intent(requireContext(), TodayEditActivity::class.java)
                    intent.putExtra("titletask",titleTask[position]?.title)
                    intent.putExtra("radiochecktask",task[position].radioButtoncheck)


                    startActivity(intent)


                }
            })
         ------------------------------*/

        //-----------↑の書き換え------------
        val adapter =
            CustomAdapter(titleTask, object:CustomAdapter.OnItemClickListener{
                override fun onItemClickListener(item: TaskCreate) {

                    val intent = Intent(requireContext(), TodayEditActivity::class.java)
                    intent.putExtra("titletask",item.title)
                    intent.putExtra("radiochecktask",item.radioButtoncheck)

                    startActivity(intent)
                }
            },true)
        //---------------------------------

        recyclerView.adapter = adapter
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun saveGraph(localDate: Int, maxInt: Int, progressInt: Int){

        realm.executeTransaction {



            val newGraphdata:Graph = it.createObject(Graph::class.java)
            newGraphdata.localDate = localDate
            newGraphdata.maxInt = maxInt
            newGraphdata.progressInt = progressInt

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()

  //      localDatedateInt = localDate.dayOfYear
        saveGraph(localDatedateInt,maxIntGraph,progressIntGraph)

        Log.d("aiueo",progressIntGraph.toString())
        Log.d("aiue",maxIntGraph.toString())
        Log.d("aiu",localDatedateInt.toString())

    }

    //------------追加-------------
    fun readAll(): RealmResults<TaskCreate>{
        return realm.where(TaskCreate::class.java).findAll()
    }
    //-----------------------------

}



//進捗ごとの背景色
//onitemlongclick
//seekBarをrecyclerviewで
//realmrecyclerViewAdapterをつかう

