package e.sergeev.oleg.salesmap

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import e.sergeev.oleg.salesmap.Loaders.FullDataLoader
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    var result = JSONArray()
    private lateinit var borderCoordinates : Array<Point>
    private lateinit var activeBuyers : Array<Buyer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var loader = FullDataLoader("r121")

        val downLoadBordersThread = async(CommonPool) {
            result =  loader.downloadBorders()
            try{
                borderCoordinates = Array(result.length(),{i -> (Point(result.getJSONArray(i).getDouble(0),
                        result.getJSONArray(i).getDouble(1)))})
            }catch (e : Exception){
                e.printStackTrace()
            }
        }

        val downLoadActiveBuyersThread = async(CommonPool) {
            result = loader.downloadBuyersPoint("1") as JSONArray
            try{
                activeBuyers = Array(result.length(), {i -> (Buyer(result.getJSONObject(i).getInt("id"),
                        Point(result.getJSONObject(i)
                                .getJSONObject("geometry")
                                .getJSONArray("coordinates")
                                .getDouble(0)
                                ,
                                result.getJSONObject(i)
                                        .getJSONObject("geometry")
                                        .getJSONArray("coordinates")
                                        .getDouble(1)
                                )))})

            }catch (e : Exception){
                e.printStackTrace()
            }
            print("ok")
        }


        launch {
            downLoadBordersThread.await()
            downLoadActiveBuyersThread.await()
        }
        print("ok")

    }
}
