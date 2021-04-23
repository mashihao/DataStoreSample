package com.hs.datastoresample

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.observe
import com.google.protobuf.ByteString
import com.hs.datastoresample.data.datastore.UserProtoDataStore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initDataStore()
    }


    val usernameLazy = MediatorLiveData<String?>().also { detailData ->
        detailData.addSource(TeslaManager.getInstance(this).username) {
            if (detailData.value == null) {
                detailData.value = it
            }
        }

    }

    val passwordlazy = MediatorLiveData<ByteString?>().also { detailData ->
        detailData.addSource(TeslaManager.getInstance(this).password) {
            if (detailData.value == null) {
                detailData.value = it
            }
        }

    }

    //1.0 临时写法，最好写在ViewModel里面
    private fun initDataStore() {
//
//
//        password.observe(this) {
//
//        }
//        username.observe(this) {
//
//        }
        usernameLazy.observe(this) {

        }
    }

    private fun initView() {

    }


    data class User(var username: String)

    var name = User("123")

    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.bt_set_username -> {

                GlobalScope.launch(Dispatchers.IO) {
                    val username = "username-->${Random().nextInt(1000)}"
                    name.username = username
                    TeslaManager.getInstance(this@MainActivity).userDataStore?.setUsername(name.toString())

                }

            }
            R.id.bt_set_password -> {
                GlobalScope.launch(Dispatchers.Main) {
                    val username = "password-->${Random().nextInt(1000)}"
                    Toast.makeText(applicationContext, username, Toast.LENGTH_SHORT).show()

                }
            }

            R.id.bt_set_getall -> {
                show()
            }
        }
    }

    public fun show() {


        val text = findViewById<TextView>(R.id.tv_log)
        GlobalScope.launch(Dispatchers.Main) {

        }
    }

    //同注释1.0
    private fun setUsername() {
        GlobalScope.launch(Dispatchers.Main) {
            Toast
                .makeText(applicationContext, getString(R.string.set_success), Toast.LENGTH_SHORT)
                .show()
        }
    }

}