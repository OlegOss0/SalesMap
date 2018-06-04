package e.sergeev.oleg.salesmap

import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import e.sergeev.oleg.salesmap.Loaders.FullDataLoader
import e.sergeev.oleg.salesmap.Models.Buyer
import e.sergeev.oleg.salesmap.Models.MyPoint
import e.sergeev.oleg.salesmap.R.drawable.loader
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

/**
 * Created by o.sergeev on 03.06.2018.
 */
class MapController() {
    init {

    }
    private object Holder {
        val INSTANCE = MapController()}


    private lateinit var borderCoordinates : Array<MyPoint>
    private lateinit var activeBuyers : Array<Buyer>



    fun showBorders(){
        if(!myMap!!.isBorderCreated()){
            val downLoadBordersThread = async(CommonPool) {
                val result =  loader!!.downloadBorders()
                try{
                    borderCoordinates = Array(result.length(),{i -> (MyPoint(result.getJSONArray(i).getDouble(0),
                            result.getJSONArray(i).getDouble(1)))})
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }
            launch(UI){
                downLoadBordersThread.await()
                myMap!!.createBorder(borderCoordinates)
            }
        }else{
            if(myMap!!.isBorderVisible()){
                myMap!!.setBorderVisible(false)
            }else{
                myMap!!.setBorderVisible(true)
            }
        }
    }
    fun showBuyers(){
        if(!myMap!!.isBuyersMarketsCreated()){
            val downLoadActiveBuyersThread = async(CommonPool) {
                val result = loader!!.downloadBuyersPoint("1")
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
                myMap!!.createBuyersMarkets(activeBuyers)
                showBuyerInfo()
            }
        }else{
            if(myMap!!.isBuyersMarketsVisible()){
                myMap!!.setBuyersMarketsVisible(false)
            }else{
                myMap!!.setBuyersMarketsVisible(true)
            }
        }
    }

    fun showBuyerInfo(id : String){
        val downLoadBuyerInfoThread = async(CommonPool) {
            val buyer = loader!!.downloadBuyerInfo("105325")
        }
        launch(UI){
            downLoadBuyerInfoThread.await()
            val bottomSheetBehavior = BottomSheetBehavior.from<View>(activity!!.findViewById(R.id.bottom_sheet))
            bottomSheetBehavior.peekHeight = 70
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

            val info = getActivity()!!.findViewById(R.id.info) as LinearLayout
            info.setOnClickListener {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                }
            }

            Toast.makeText(getActivity(), "Clicked buyer id $clickCount", Toast.LENGTH_LONG).show()


        }


    }
    companion object {
        val instance: MapController by lazy {Holder.INSTANCE }
    }
    var loader : FullDataLoader? = null
    var myMap : MapControllerListener? = null
    var activity : AppCompatActivity? = null


}