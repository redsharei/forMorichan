package com.morichan.studyschedule

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.morichan.studyschedule.databinding.ActivityMainBinding
import com.morichan.studyschedule.databinding.FragmentTodayBinding
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_today_edit.*
import kotlinx.android.synthetic.main.activity_today_edit.radioButtontoday
import kotlinx.android.synthetic.main.activity_today_edit.radioButtontomorrow
import kotlinx.android.synthetic.main.activity_today_edit.saveButton
import kotlinx.android.synthetic.main.activity_today_list_create.*
import kotlinx.android.synthetic.main.fragment_today.*
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class TodayEditActivity : AppCompatActivity() {

    var stringtitle = ""
    var stringcheckradio = ""

    var seekBarint = 0
    var seekBarmaxInt = 0

    var complete = false
    var delete = false

    @RequiresApi(Build.VERSION_CODES.O)
    //***
//    var localDateTime = LocalDateTime.now()
    var localDateTime = 0

    val realm: Realm = Realm.getDefaultInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_edit)





        stringtitle = intent.getStringExtra("titletask")
        stringcheckradio = intent.getStringExtra("radiochecktask")

        var seekBarrealm = realm.where(TaskCreate::class.java).equalTo("title", stringtitle)
            .equalTo("complete", false)
            .findFirst()


        //------------書き換え------------
        if (seekBarrealm?.seekBarprogress != null) {  //seekBarrealmはnullの可能性があるので、nullじゃない時にデータ読み込み
            seekBarint = seekBarrealm.seekBarprogress

            seekBarmaxInt = seekBarrealm.seekBarMaxInt

            progressBar.max = seekBarmaxInt
            progressBar.progress = seekBarint

        }

        //*** ごめんlocakDateTimeクラスがAPIレベルの問題でrunできないので一旦コメントアウトしています
/*
        if (seekBarrealm.calendarHour<localDateTime.hour&&
                seekBarrealm.radioButtoncheck !=="tomorrow"){
            titleTextView.setTextColor(Color.parseColor("#ff4500"))
        }
        if (seekBarrealm.calendarHour==localDateTime.hour&&
                seekBarrealm.calendarMinute<localDateTime.minute&&
                    seekBarrealm.radioButtoncheck !=="tomorrow"
           ){
            titleTextView.setTextColor(Color.parseColor("#ff4500"))
        }
        if (seekBarrealm.calendarDate<localDateTime.dayOfYear&&
                seekBarrealm.radioButtoncheck !== "tomorrow"){

            titleTextView.setTextColor(Color.parseColor("#ff4500"))
        }
  */
        else {
            titleTextView.setTextColor(Color.parseColor("#000000"))

        }


/*-------- ↑(64行目あたり)へ移動------------
        seekBarmaxInt = seekBarrealm?.seekBarMaxInt

        progressBar.max = seekBarmaxInt
        progressBar.progress = seekBarint
-----------------------------------*/

        titleTextView.text = stringtitle

        if (stringcheckradio.equals("today")) {
            radioButtontoday.isChecked = true
        } else if (stringcheckradio.equals("tomorrow")) {
            radioButtontomorrow.isChecked = true
        } else if (stringcheckradio.equals("everyday")) {
            radioButtonEveryday.isChecked = true
        }


        saveButton.setOnClickListener {

            //----------下でdelete()メソッドをつくった---------------
            /*if (seekBarint == seekBarmaxInt) {
                complete = true

                var result = realm.where(TaskCreate::class.java)
                    .equalTo("title", stringtitle).findAll()
                realm.executeTransaction {
                    result.deleteAllFromRealm()  //deleteAllだと、realmから全てのデータが消されてしまう

                    finish()
                }
            }*/







            if (radioButtontoday.isChecked == true) {
                stringcheckradio = "today"
            } else if (radioButtontomorrow.isChecked == true) {
                stringcheckradio = "tomorrow"
            } else if (radioButtonEveryday.isChecked == true) {
                stringcheckradio = "everyday"
            }

            seekBarint = progressBar.progress

            //--------------書き換え-----------
            //seekbarがmaxの時
            if(seekBarint==seekBarmaxInt){
                complete=true
                Log.d("seekbarint==max","true")
                delete(stringtitle)
            }else if(seekBarrealm!=null){ //seekbarがmaxじゃなく、nullじゃないときupdate
                update(
                    title = stringtitle, radioButtonString = stringcheckradio,
                    seekBarint = seekBarint, complete = complete
                )
            }
            //----------------------------

            Log.d("complete", complete.toString())



            finish()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }


    fun update(title: String, radioButtonString: String, seekBarint: Int, complete: Boolean) {
        realm.executeTransaction {
            var string = realm.where(TaskCreate::class.java).equalTo("title", title).findFirst()
            string!!.title = title
            string.radioButtoncheck = radioButtonString
            string.seekBarprogress = seekBarint
            string.complete = complete
        }
    }

    //---------------追加----------------
    fun delete(title: String){
        realm.executeTransaction {
            val task = realm.where(TaskCreate::class.java).equalTo("title",title).findFirst()
                ?: return@executeTransaction
            task.deleteFromRealm()
            //クリックされたタイトルと一致するタイトルの要素をデータベースから削除
        }
    }
    //-------------------------------


}



