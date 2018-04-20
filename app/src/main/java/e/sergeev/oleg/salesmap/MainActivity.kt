package e.sergeev.oleg.salesmap

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import e.sergeev.oleg.salesmap.Loaders.FullDataLoader
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var loader = FullDataLoader("r121")
        val downLoadThread = async(CommonPool) {
            loader.downloadBorders()
        }
        /*val downLoadThread2 = async(CommonPool) {
            val borders = loader.downloadBorders()
            loader.downloadActiveBuyers()
        }*/
        launch (UI){
            val result = downLoadThread.await()
            print("Ok")
        }


    }
}
