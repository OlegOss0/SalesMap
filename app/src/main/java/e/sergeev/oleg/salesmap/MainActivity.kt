package e.sergeev.oleg.salesmap


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import e.sergeev.oleg.salesmap.Loaders.FullDataLoader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var gMapFragment: GMapFragment
    private lateinit var yMapFragment: YaMapFragment
    private lateinit var loader: FullDataLoader
    private lateinit var terId: String
    private lateinit var mapController: MapController

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
        //TODO
        /*mapController = MapController(loader, gMapFragment, this)*/
        mapController = MapController.instance
        mapController.loader = loader
        mapController.myMap = gMapFragment
        mapController.activity = this


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
                mapController.showBorders()
            }
            R.id.show_active_byers -> {
                mapController.showBuyers()
            }
            R.id.show_list -> {
                Toast.makeText(this, "Not ready", Toast.LENGTH_SHORT).show()
            }
            R.id.add_buyer -> {
                Toast.makeText(this, "Not ready", Toast.LENGTH_SHORT).show()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun getLoader() : FullDataLoader{
        return loader
    }
}
