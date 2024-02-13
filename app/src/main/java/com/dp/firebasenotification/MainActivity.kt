package com.dp.firebasenotification

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic"

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivityTag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        val btnSend: Button = findViewById(R.id.btnSend)
        val eTitle: EditText = findViewById(R.id.title_message)
        val eMessage: EditText = findViewById(R.id.message_content)
        val eToken: EditText = findViewById(R.id.token_content)


        btnSend.setOnClickListener{
            val title = eTitle.text.toString()
            val message = eMessage.text.toString()
            if (title.isNotEmpty() && message.isNotEmpty()){
                PushNotification(
                    NotificationData(title,message),
                    TOPIC
                ).also {
                    sendNotification(it)
                }
            }
        }
    }
    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            }
            else{
                Log.e(TAG, response.errorBody().toString())
            }
        }
        catch (e: Exception){
            Log.v(TAG, e.toString())
        }
    }
}