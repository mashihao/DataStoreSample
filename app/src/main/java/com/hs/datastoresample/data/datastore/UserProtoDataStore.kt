package com.hs.datastoresample.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.hs.datastoresample.datasotre.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

class UserProtoDataStore(context: Context)  {

    companion object {
        const val FILENAME = "user.pb"
    }

    public val dataStore by lazy {
        context.createDataStore(
            fileName = FILENAME,
            serializer = UserSerializer
        )
    }


    object UserSerializer : Serializer<User> {

        override val defaultValue: User
            get() = User.getDefaultInstance()

        override fun readFrom(input: InputStream): User {
            try {
                return User.parseFrom(input)
            } catch (e: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", e)
            }
        }

        override fun writeTo(t: User, output: OutputStream) {
            t.writeTo(output)
        }
    }

     suspend fun setUsername(name: String) {
        dataStore.updateData {
            Log.e("MSH","setUsername")
            it.toBuilder()
                .setUsername(name)
                .build()
        }
    }

     fun getUsername(): Flow<String?> {
        return dataStore.data.map {
            it.username
        }

    }

     suspend fun setPassword(password: String) {
        dataStore.updateData {
            it.toBuilder()
                .setPassword(password)
                .build()
        }
    }

    fun getPassword(): Flow<String?> {
        return dataStore.data.map {
            it.password
        }
    }

    fun getAll(): Flow<String?> {
        return dataStore.data.map {
            it.toString()
        }
    }

    override fun toString(): String {
        return "[UserProtoDataStore]"
    }

}