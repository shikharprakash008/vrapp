package surfer.example.surfer.vr

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpDownload
import com.github.kittinunf.fuel.httpGet
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    companion object {
        const val APIKey = "AIzaSyCUcW_9TGO3bQyYjtpKETdNueYq1vwzTOQ"
        val assetURL = "https://poly.google.com/view/9WMqTkHGQSl"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (APIKey.startsWith("INSERT")) {
            Toast.makeText(this, "You haven't updated your API key", Toast.LENGTH_SHORT).show()
        } else {
            assetURL.httpGet(listOf("key" to APIKey)).responseJson { request, response, result ->

                result.fold({
                    val asset = it.obj()

                    var objectURL: String? = null
                    var materialLibraryName: String? = null
                    var materialLibraryURL: String? = null
                    val assetFormats = asset.getJSONArray("formats")

                    for (i in 0 until assetFormats.length()) {
                        val currentFormat = assetFormats.getJSONObject(i)

                        if (currentFormat.getString("formatType") == "OBJ") {
                            objectURL = currentFormat.getJSONObject("root")
                                    .getString("url")

                            materialLibraryName = currentFormat.getJSONArray("resources")
                                    .getJSONObject(0)
                                    .getString("relativePath")

                            materialLibraryURL = currentFormat.getJSONArray("resources")
                                    .getJSONObject(0)
                                    .getString("url")
                            break
                        }
                    }

                    objectURL!!.httpDownload().destination { _, _ ->
                        File(filesDir, "globeAsset.obj")
                    }.response { _, _, result ->
                        result.fold({},
                                {
                                    Toast.makeText(this, "Unable to download resource", Toast.LENGTH_SHORT).show()
                                })
                    }

                    materialLibraryURL!!.httpDownload().destination { _, _ ->
                        File(filesDir, materialLibraryName)
                    }.response { _, _, result ->
                        result.fold({}, {
                            Toast.makeText(this, "Unable to download resource", Toast.LENGTH_SHORT).show()
                        })
                    }

                }, {
                    Toast.makeText(this, "Unable to download resource", Toast.LENGTH_SHORT).show()
                })
            }



            vr.setOnClickListener {
                val intent = Intent(this, Main2Activity::class.java)
                startActivity(intent);

            }

            }

        }
    }

