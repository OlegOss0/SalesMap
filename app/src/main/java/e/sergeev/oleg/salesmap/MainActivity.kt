package e.sergeev.oleg.salesmap

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.mapview.MapView
import e.sergeev.oleg.salesmap.Loaders.FullDataLoader
import org.json.JSONArray
import com.yandex.mapkit.map.CameraPosition
import e.sergeev.oleg.salesmap.Models.Buyer


class MainActivity : AppCompatActivity() {

    var result = JSONArray()
    private lateinit var borderCoordinates : Array<Point>
    private lateinit var activeBuyers : Array<Buyer>
    private lateinit var contentFragment: Fragment
    private lateinit var gMapFragment: GMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gMapFragment = GMapFragment()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, gMapFragment)
                    .commit()
        }

        var loader = FullDataLoader("r121")




        /*val downLoadBordersThread = async(CommonPool) {
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
        print("ok")*/

    }
}
