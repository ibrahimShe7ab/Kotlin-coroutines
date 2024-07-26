package com.example.couroutinestoutourial

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    private val TAG = "tag"

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // Example of starting a new thread
       val thread = object : Thread() {
           override fun run() {
               super.run()
               Log.d(TAG, "Thread implementation")
           }
       }
       thread.start()

       // Example of using GlobalScope to launch a coroutine
       GlobalScope.launch {
           delay(1000L)
           Log.d(TAG, "implementation coroutines ${Thread.currentThread().name}")
       }

       // Using different dispatchers with lifecycleScope
       lifecycleScope.launch(Dispatchers.Unconfined) {
           delay(1000L)
           Log.d(TAG, "implementation Unconfined coroutines ${Thread.currentThread().name}")
       }

       lifecycleScope.launch(Dispatchers.Main) {
           delay(1000L)
           Log.d(TAG, "implementation Main coroutines ${Thread.currentThread().name}")
       }

       lifecycleScope.launch(Dispatchers.IO) {
           delay(1000L)
           Log.d(TAG, "implementation IO coroutines ${Thread.currentThread().name}")
       }

       lifecycleScope.launch(Dispatchers.Default) {
           delay(1000L)
           Log.d(TAG, "implementation Default coroutines ${Thread.currentThread().name}")
       }

       // Example of nested coroutines
       lifecycleScope.launch(Dispatchers.IO) {
           Log.d(TAG, "implementation IO coroutines ${Thread.currentThread().name}")

           withContext(Dispatchers.Main) {
               delay(1000L)
               Log.d(TAG, "implementation Main coroutines ${Thread.currentThread().name}")
           }
           withContext(Dispatchers.Default) {
               delay(1000L)
               Log.d(TAG, "implementation Default coroutines ${Thread.currentThread().name}")
           }
       }

       // Example of a suspend function
       suspend fun loadImage(): String {
           return "done!"
       }

       // Using runBlocking to bridge the non-coroutine world to the coroutine world
       runBlocking {
           launch(Dispatchers.Main) {
               val result = loadImage()
               Log.d(TAG, result)

               launch(Dispatchers.IO) {
                   Log.d(TAG, "Run Block IO${Thread.currentThread().name}")
               }
           }
       }

       // Example of using Job to manage coroutine lifecycle
       val myJob: Job = GlobalScope.launch(Dispatchers.Default) {
           if (isActive) {
               repeat(5) {
                   Log.d(TAG, "JOB still working")
                   delay(2000L)
               }
           }
       }

       // Run a blocking coroutine
       runBlocking {
           Log.d(TAG, "start coroutine")
           myJob.join() // Wait until myJob completes
           myJob.cancel() // Cancel this job
           Log.d(TAG, "finishing coroutine")
       }

       // Example of measuring execution time of coroutines
       suspend fun num1(): Int {
           return 10
       }

       suspend fun num2(): Int {
           return 10
       }

       runBlocking {
           val time = measureTimeMillis {
               val num1Deferred = async { num1() }
               val num2Deferred = async { num2() }

               if (num1Deferred.await() == num2Deferred.await()) {
                   Log.d(TAG, "equal")
               }
           }
           Log.d(TAG, "time response $time Millis")
       }











    }
}
