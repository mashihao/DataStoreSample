package com.hs.datastoresample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.observe
import com.hs.datastoresample.data.datastore.UserProtoDataStore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val userDataStore = UserProtoDataStore(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initDataStore()
    }

    private val user by lazy {
        userDataStore.dataStore.data.distinctUntilChanged().onEach {
            Log.e("MSH", "user update")
        }.flowOn(Dispatchers.Main)
    }

    val password by lazy {
        user.map { it.password }.distinctUntilChanged()
            .flowOn(Dispatchers.Main)
            .onEach {
                Log.e("MSH","passwrod update")
                findViewById<TextView>(R.id.bt_set_password).setText(it)
            }.asLiveData()
    }

    val username by lazy {
        user.map { it.username }.distinctUntilChanged()
            .flowOn(Dispatchers.Main)
            .onEach {
                Log.e("MSH","username update")
                findViewById<TextView>(R.id.bt_set_username).setText(it)
            }
            .asLiveData()
    }

    //1.0 临时写法，最好写在ViewModel里面
    private fun initDataStore() {


        password.observe(this){

        }
        username.observe(this){

        }

    }

    private fun initView() {

    }

    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.bt_set_username -> {

                GlobalScope.launch(Dispatchers.Main) {
                    val username = "username-->${Random().nextInt(1000)}"
                    userDataStore?.setUsername(username)
                    Toast.makeText(applicationContext, username, Toast.LENGTH_SHORT).show()

                }

            }
            R.id.bt_set_password -> {
                GlobalScope.launch(Dispatchers.Main) {
                    val username = "password-->${Random().nextInt(1000)}"
                    userDataStore?.setPassword(username)
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
            userDataStore?.getAll()?.collect {
                text.setText(it)
            }

        }
    }

    //同注释1.0
    private fun setUsername() {
        GlobalScope.launch(Dispatchers.Main) {
            val username = "${userDataStore}danledian${Random().nextInt(1000)}"
            userDataStore?.setUsername(username)
            Toast
                .makeText(applicationContext, getString(R.string.set_success), Toast.LENGTH_SHORT)
                .show()
        }
    }

}