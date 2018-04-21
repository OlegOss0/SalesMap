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
    var result = JSONArray()
    private lateinit var borderCoordinates : Array<Point>
    lateinit var p : Point

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var loader = FullDataLoader("r121")

        val downLoadBordersThread = async(CommonPool) {
            result =  loader.downloadBorders()
            try{
                return@async Array(result.length(),{i -> (Point(result.getJSONArray(i).getDouble(0),
                        result.getJSONArray(i).getDouble(1)))})

            }catch (e : Exception){
                e.printStackTrace()
            }
        }

        val downLoadActiveBuyersThread = async(CommonPool) {
            val result = loader.downloadActiveBuyers()
            print("ok")
        }
        launch {
            loader.downloadActiveBuyers()
            borderCoordinates = downLoadBordersThread.await() as Array<Point>
        }

        print(borderCoordinates.get(0).lat)
        print("ok")

    }
}
