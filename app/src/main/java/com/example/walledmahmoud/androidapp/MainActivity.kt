package com.example.walledmahmoud.androidapp

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


    // Make Function To Get The SunSet

    fun GetSunset(view:View) {

        var city = CityText.text.toString()
        var url  = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+ city +"%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"

        MyAsyncTask().execute(url)
    }

    inner class MyAsyncTask: AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            // Before Execute
        }

        override fun doInBackground(vararg p0: String?): String {

            try {
                val url = URL(p0[0])
                val urlconnect = url.openConnection() as HttpURLConnection
                urlconnect.connectTimeout = 7000

                // Convert Data To String
                var inString = ConvertStreamToString(urlconnect.inputStream)

                // Publish The UI
                publishProgress(inString)

            } catch(ex:Exception) {}

            return " "
        }

        // On Progress
        override fun onProgressUpdate(vararg values: String?) {

            try {

                var json  = JSONObject(values[0])
                val query = json.getJSONObject("query")
                val results = query.getJSONObject("results")
                val channel = results.getJSONObject("channel")
                val astronomy = channel.getJSONObject("astronomy")
                val sunset = astronomy.getString("sunset")

                SunsetTime.setText(sunset + " هتفطر النهاردة الساعة\n ")




            } catch (ex:Exception){}
        }


        override fun onPostExecute(result: String?) {
            // After Execute
        }



        fun ConvertStreamToString(inputStream:InputStream):String {

            val bufferReader = BufferedReader(InputStreamReader(inputStream))

            var line:String
            var AllString:String = ""

            try {
                do {

                    line = bufferReader.readLine()
                    if(line != null) {
                        AllString += line
                    }
                } while (line != null)

                inputStream.close()
            } catch (ex:Exception) {}
            return AllString
        }
    }
}