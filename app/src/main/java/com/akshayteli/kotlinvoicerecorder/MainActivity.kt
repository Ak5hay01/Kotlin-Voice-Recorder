package com.akshayteli.kotlinvoicerecorder

import android.Manifest.permission
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.lang.Exception


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "MainActivity"

    //creating a variable for media recorder object class.
    private var mRecorder: MediaRecorder? = null

    // creating a variable for media player class
    private var mPlayer: MediaPlayer? = null

    //string variable is created for storing a file name
    private var mFileName: String? = null

    // constant for storing audio permission
    val REQUEST_AUDIO_PERMISSION_CODE = 1

    private val sharedPrefFile = "kotlinsharedpreference"

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

    }

    private fun init(){
        btnStop.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.gray))
        btnPlay.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.gray))
        btnStopPlay.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.gray))

        btnRecord.setOnClickListener(this)
        btnStop.setOnClickListener (this)
        btnPlay.setOnClickListener (this)
        btnStopPlay.setOnClickListener (this)

        sharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)

    }

    override fun onClick(p0: View?) {
        val itemId = p0?.id
        when (itemId) {
              R.id.btnRecord->{
                  try {
                      startRecording()
                  } catch (ex: Exception) {
                      Log.e(TAG, "onClick: ", ex)
                  }
              }
              R.id.btnPlay->{
                  try {
                      playAudio()
                  } catch (ex: Exception) {
                      Log.e(TAG, "onClick: ", ex)
                  }

              }
              R.id.btnStop->{
                  try {
                      pauseRecording()
                  } catch (ex: Exception) {
                      Log.e(TAG, "onClick: ", ex)
                  }

              }
              R.id.btnStopPlay->{
                  try {
                      pausePlaying()
                  } catch (ex: Exception) {
                      Log.e(TAG, "onClick: ", ex)
                  }

              }

        }
    }

    private fun pausePlaying() {

        //this method will release the media player class and pause the playing of our recorded audio.
        mPlayer!!.release()
        mPlayer = null

        btnStop.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.gray))
        btnPlay.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.purple_200))
        btnStopPlay.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.gray))
        btnRecord.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.purple_200))

        txtStatus.text =  "Recording Play Stopped"
    }

    private fun pauseRecording() {

        btnStop.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.gray))
        btnPlay.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.purple_200))
        btnStopPlay.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.purple_200))
        btnRecord.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.purple_200))

        //below method will stop the audio recording.
        //below method will stop the audio recording.
        mRecorder!!.stop()
        //below method will release the media recorder class.
        //below method will release the media recorder class.
        mRecorder!!.release()
        mRecorder = null
        txtStatus.text =  "Recording Stopped"
    }

    private fun playAudio() {

        btnStop.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.gray))
        btnPlay.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.gray))
        btnStopPlay.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.purple_200))
        btnRecord.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.purple_200))

        //for playing our recorded audio we are using media player class.
        //for playing our recorded audio we are using media player class.
        mPlayer = MediaPlayer()
        try {
            //below method is used to set the data source which will be our file name
            mPlayer!!.setDataSource(mFileName)
            //below method will prepare our media player
            mPlayer!!.prepare()
            //below method will start our media player.
            mPlayer!!.start()
            txtStatus.text = "Recording Started Playing"
        } catch (e: IOException) {
            Log.e("TAG", "prepare() failed")
        }
    }

    private fun startRecording() {
        // check permission method is used to check that the user has granted permission to record nd store the audio.

        // check permission method is used to check that the user has granted permission to record nd store the audio.
        if (checkPermissions()) {
            //setbackgroundcolor method will change the background color of text view.
        } else {
            //if audio recording permissions are not granted by user below method will ask for runtime permission for mic and storage.
            requestPermissions()
        }
    }


    fun record() {

        btnStop.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.purple_200))
        btnPlay.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.gray))
        btnStopPlay.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.gray))
        btnRecord.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.gray))

        //we are here initializing our filename variable with the path of the recorded audio file.
        mFileName = Environment.getExternalStorageDirectory().absolutePath
        mFileName += "/AudioRecording.flac"
        //below method is used to initialize the media recorder clss
        mRecorder = MediaRecorder()
        //below method is used to set the audio source which we are using a mic.
        mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        //below method is used to set the output format of the audio.
        mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        //below method is used to set the audio encoder for our recorded audio.
        mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        //below method is used to set the output file location for our recorded audio
        mRecorder!!.setOutputFile(mFileName)
        try {
            //below mwthod will prepare our audio recorder class
            mRecorder!!.prepare()
        } catch (e: IOException) {
            Log.e("TAG", "prepare() failed")
        }
        // start method will start the audio recording.
        mRecorder!!.start()

        txtStatus.text =  "Recording Started"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // this method is called when user will grant the permission for audio recording.
        when (requestCode) {
            REQUEST_AUDIO_PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                val permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED
                val permissionToRead = grantResults[2] == PackageManager.PERMISSION_GRANTED
                val permissionToManage = grantResults[3] == PackageManager.PERMISSION_GRANTED
                if (permissionToRecord && permissionToStore && permissionToRead && permissionToManage) {
                    Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2296) {
            if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success

                    val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                    editor.putInt("got_permissions",1)
                    editor.apply()
                    editor.commit()
                    record()
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun checkPermissions(): Boolean {
        //this method is used to check permission
        val result =  ContextCompat.checkSelfPermission(applicationContext, permission.WRITE_EXTERNAL_STORAGE)
        val result1 = ContextCompat.checkSelfPermission(applicationContext, permission.RECORD_AUDIO)
        val result2 = ContextCompat.checkSelfPermission(applicationContext, permission.READ_EXTERNAL_STORAGE)
        val result3 = ContextCompat.checkSelfPermission(applicationContext, permission.MANAGE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        // this method is used to request the permission for audio recording and storage.
        //ActivityCompat.requestPermissions(MainActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE,MANAGE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);

        val sharedIdValue = sharedPreferences.getInt("got_permissions",0)
        if (VERSION.SDK_INT >= Build.VERSION_CODES.R ) {
            if(sharedIdValue ==1){
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(
                        permission.RECORD_AUDIO,
                        permission.WRITE_EXTERNAL_STORAGE,
                        permission.READ_EXTERNAL_STORAGE,
                        permission.MANAGE_EXTERNAL_STORAGE
                    ),
                    REQUEST_AUDIO_PERMISSION_CODE
                )
                record()
                return
            }
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(String.format("package:%s", applicationContext.packageName))
                startActivityForResult(intent, 2296)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivityForResult(intent, 2296)
            }

            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                    permission.RECORD_AUDIO,
                    permission.WRITE_EXTERNAL_STORAGE,
                    permission.READ_EXTERNAL_STORAGE,
                    permission.MANAGE_EXTERNAL_STORAGE
                ),
                REQUEST_AUDIO_PERMISSION_CODE
            )


        } else {
            //below android 11
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                    permission.RECORD_AUDIO,
                    permission.WRITE_EXTERNAL_STORAGE,
                    permission.READ_EXTERNAL_STORAGE,
                    permission.MANAGE_EXTERNAL_STORAGE
                ),
                REQUEST_AUDIO_PERMISSION_CODE
            )
        }
    }

}