package com.example.neuraldrive

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.EditText
import androidx.core.view.isVisible
import com.example.neuraldrive.databinding.ActivityMainBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import android.text.format.Formatter
import java.net.NetworkInterface


class MainActivity : Activity(), SensorEventListener{



    private lateinit var binding: ActivityMainBinding

    private val client = OkHttpClient()
    private var ipAddressServer: String = "192.168.0.53"
    private var handler: Handler = Handler()
    var runnable: Runnable? = null
    private var delay = 500
    private var stack = "["
    private var index = 0
    //private var stack_int = mutableListOf<Float>()

    private val sensorAccelFeature: String = PackageManager.FEATURE_SENSOR_ACCELEROMETER
    private val sensorGyroFeature: String = PackageManager.FEATURE_SENSOR_GYROSCOPE

    private val doesSensorsExist: Boolean
        get() = packageManager.hasSystemFeature(sensorAccelFeature) and packageManager.hasSystemFeature(sensorGyroFeature)

    private lateinit var sensorManager: SensorManager
    private var accelSensor: Sensor? = null
    private var gyroSensor: Sensor? = null

    private var gyroX: Float = 0.0f; private var gyroY: Float = 0.0f ; private var gyroZ: Float = 0.0f
    private var accelX: Float = 0.0f; private var accelY: Float = 0.0f; private var accelZ: Float = 0.0f



    lateinit var wifiManager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val interfaces = NetworkInterface.getNetworkInterfaces()
        val list = interfaces.toList()
        //look into the interface's ipaddresses (ipv4,ipv6)
        val ip = list[1].inetAddresses.toList().get(1).hostAddress

        println(ip)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.enableData.isVisible = true

        if (checkSelfPermission(Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.BODY_SENSORS), 1)
        } else {
            Log.d(TAG, "ALREADY GRANTED")
        }

        binding.ipAddress.findViewById<EditText>(R.id.ipAddress)
        binding.connectServer.setOnClickListener{
            ipAddressServer = binding.ipAddress.text.toString()
            //Send something to server to verify connection

        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        binding.enableData.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.enableData.setText(R.string.enable)
                if((sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)  != null)and(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)  != null)) {
                    accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
                    sensorManager.registerListener(this,accelSensor,SensorManager.SENSOR_DELAY_GAME)
                    gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
                    sensorManager.registerListener(this,gyroSensor,SensorManager.SENSOR_DELAY_GAME)
                }else{
                    //Fail to get
                    Log.d("Fail:", doesSensorsExist.toString())

                }
                onResume()
            } else {
                binding.enableData.setText(R.string.disable)
                onPause()
            }
        }
    }

    companion object {
        val MEDIA_TYPE_MARKDOWN = "text/x-markdown; charset=utf-8".toMediaType()
    }
    fun add_data_to_stack(acc_x : Float, acc_y : Float, acc_z : Float, gir_x : Float, gir_y : Float,gir_z : Float){
        val data = "{"+
                "\"acc_x\"" + ":" + "\""+acc_x.toString() +"\""+ ","+
                "\"acc_y\"" + ":" + "\""+acc_y.toString() +"\""+ ","+
                "\"acc_z\"" + ":" + "\""+acc_z.toString() +"\""+ ","+
                "\"gir_x\"" + ":" + "\""+gir_x.toString() +"\""+ ","+
                "\"gir_y\"" + ":" + "\""+gir_y.toString() +"\""+ ","+
                "\"gir_z\"" + ":" + "\""+gir_z.toString() +"\""+
                "},"
        this.stack += data
    }

    fun sendData(){
        this.stack = this.stack.dropLast(1)
        this.stack += "]"
        val postBody = this.stack.trimMargin()

        val request = Request.Builder()
            .url("http://$ipAddressServer:5000/watch_packet/")
            .post(postBody.toRequestBody(MEDIA_TYPE_MARKDOWN))
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    println(response.body!!.string())
                }
            }
        })
        this.stack = "["
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
//            Toast.makeText(this@MainActivity, "This method will run every 5 seconds", Toast.LENGTH_SHORT).show()
            println(this.stack)
            index = 0
            this.sendData()
        }.also { runnable = it }, delay.toLong())
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable!!)
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER){
            accelX = event.values[0]
            accelY = event.values[1]
            accelZ = event.values[2]
            //Log.d("Accelerometer:", "$accelX,$accelY,$accelZ")
        }

        if (event.sensor.type == Sensor.TYPE_GYROSCOPE){
            gyroX = event.values[0]
            gyroY = event.values[1]
            gyroZ = event.values[2]
            //Log.d("Gyroscope:", "$gyroX,$gyroY,$gyroZ")
        }
        if(index < 50) {
            add_data_to_stack(accelX, accelY, accelZ, gyroX, gyroY, gyroZ)
            index+=1
        }
    }

    override fun onAccuracyChanged(event: Sensor?, p1: Int) = Unit
}