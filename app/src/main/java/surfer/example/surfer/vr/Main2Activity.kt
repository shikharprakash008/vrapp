package surfer.example.surfer.vr

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main2.*
import processing.android.PFragment
import processing.core.PApplet
import processing.core.PConstants
import processing.core.PShape
import surfer.example.surfer.vr.R.attr.background
import java.io.File

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        displayAsset();

    }

    private fun displayAsset() {
        val canvas3D = object : PApplet() {

            var polyAsset: PShape? = null

            override fun settings() {
                fullScreen(PConstants.P3D)
            }

            override fun setup() {
                polyAsset = loadShape(File(filesDir, "globeAsset.obj").absolutePath)
            }

            override fun draw() {
                background(0)

                scale(-10f)
                translate(-50f,-100f, 10f)
                shape(polyAsset)
            }
        }
        val assetView = PFragment(canvas3D)
        assetView.setView(asset_view, this)
    }


}
