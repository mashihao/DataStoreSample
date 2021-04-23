package com.hs.datastoresample

import android.content.Context
import android.util.Log
import androidx.lifecycle.asLiveData
import com.hs.datastoresample.data.datastore.UserProtoDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

/**
 * @author : 马世豪
 * time : 4/22/21 16
 * email : ma_shihao@yeah.net
 * des :
 */
class TeslaManager {

    companion object {
        private var instance: TeslaManager? = null
        lateinit var context:Context
        fun getInstance(context1: Context): TeslaManager {
            context = context1
            return instance ?: synchronized(this) {
                TeslaManager().apply {
                    instance = this
                }
            }
        }
    }

    public val userDataStore by lazy {  UserProtoDataStore(context) }
    private val user by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        userDataStore.dataStore.data.onEach {
            Log.e("MSH", "user update")
        }.flowOn(Dispatchers.Main)
    }

    val password by lazy {
        user.map { it.password }.distinctUntilChanged()
            .flowOn(Dispatchers.Main)
            .onEach {
                Log.e("MSH", "passwrod update")
            }.asLiveData()
    }

    val username by lazy {
        user.map { it.username }.distinctUntilChanged()
            .onEach {
                Log.e("MSH", "username update")
            }
            .flowOn(Dispatchers.Main)
            .map {
                it
            }
            .flowOn(Dispatchers.IO)

            .catch { emit("") }
            .asLiveData()
    }

}