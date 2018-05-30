package e.sergeev.oleg.salesmap

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import e.sergeev.oleg.salesmap.Loaders.FullDataLoader
import e.sergeev.oleg.salesmap.Models.Buyer
import e.sergeev.oleg.salesmap.Models.MyPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.json.JSONArray

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var result = JSONArray()
    private lateinit var borderCoordinates : Array<MyPoint>
    private lateinit var activeBuyers : Array<Buyer>
    private lateinit var contentFragment: Fragment
    private lateinit var gMapFragment: GMapFragment
    private lateinit var yMapFragment: YaMapFragment
    private lateinit var loader: FullDataLoader
    private lateinit var terId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO проверить на null
        terId = intent.getStringExtra("terId")
        loader = FullDataLoader(terId)


        gMapFragment = GMapFragment()
        /*yMapFragment = YaMapFragment()*/
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, gMapFragment)
                    .commit()
        }

        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        tv_terr_id_in_nav_header.setText("Территория " + terId)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.show_borders-> {
                if(gMapFragment.border == null){
                    val downLoadBordersThread = async(CommonPool) {
                        result =  loader.downloadBorders()
                        try{
                            borderCoordinates = Array(result.length(),{i -> (MyPoint(result.getJSONArray(i).getDouble(0),
                                    result.getJSONArray(i).getDouble(1)))})
                        }catch (e : Exception){
                            e.printStackTrace()
                        }
                    }
                    launch(UI){
                        downLoadBordersThread.await()
                        gMapFragment.createBorderPolygon(borderCoordinates)
                    }
                }else{
                    var borderVisible = gMapFragment.border.isVisible
                    if(borderVisible){
                        gMapFragment.setBorderVisible(false)
                    }else{
                        gMapFragment.setBorderVisible(true)
                    }
                }


            }
            R.id.show_active_byers -> {
                if(gMapFragment.activeBuyersMarkers == null){
                    val downLoadActiveBuyersThread = async(CommonPool) {
                        result = loader.downloadBuyersPoint("1") as JSONArray
                        try{
                            activeBuyers = Array(result.length(), {i -> (Buyer(result.getJSONObject(i).getInt("id"),
                                    MyPoint(result.getJSONObject(i)
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
                    }
                    launch(UI){
                        downLoadActiveBuyersThread.await()
                        gMapFragment.createActiveBuyersMarkets(activeBuyers)
                    }
                }else{
                    var activeBuyersVisible = gMapFragment.isActiveBuyersVisible
                    if(activeBuyersVisible){
                        gMapFragment.setActiveBuyersVisible(false)
                    }else{
                        gMapFragment.setActiveBuyersVisible(true)
                    }
                }

            }
            R.id.show_sleep_byers -> {
                if(gMapFragment.sleepBuyersMarkers == null){
                    val downLoadSleepBuyersThread = async(CommonPool) {
                        result = loader.downloadBuyersPoint("0") as JSONArray
                        try{
                            activeBuyers = Array(result.length(), {i -> (Buyer(result.getJSONObject(i).getInt("id"),
                                    MyPoint(result.getJSONObject(i)
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
                    }
                    launch(UI){
                        downLoadSleepBuyersThread.await()
                        gMapFragment.createSleepBuyersMarkets(activeBuyers)
                    }
                }else{
                    var sleepBuyersVisible = gMapFragment.isSleepBuyersVisible
                    if(sleepBuyersVisible){
                        gMapFragment.setSleepBuyersVisible(false)
                    }else{
                        gMapFragment.setSleepBuyersVisible(true)
                    }
                }

            }
            R.id.show_list -> {

            }
            R.id.add_buyer -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /*fun downLoadBorders(){
        val downLoadBordersThread = async(CommonPool) {
            result =  loader.downloadBorders()
            try{
                borderCoordinates = Array(result.length(),{i -> (MyPoint(result.getJSONArray(i).getDouble(0),
                        result.getJSONArray(i).getDouble(1)))})
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
        launch(UI) { downLoadBordersThread.await() }
    }*/



    /*val downLoadActiveBuyersThread = async(CommonPool) {
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
