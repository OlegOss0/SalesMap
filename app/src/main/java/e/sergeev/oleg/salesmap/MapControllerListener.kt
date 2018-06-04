package e.sergeev.oleg.salesmap

import e.sergeev.oleg.salesmap.Models.Buyer
import e.sergeev.oleg.salesmap.Models.MyPoint

/**
 * Created by o.sergeev on 07.05.2018.
 */
interface MapControllerListener {

    fun createBorder(borderCoordinates : Array<MyPoint>)

    fun isBorderCreated() : Boolean

    fun isBorderVisible() : Boolean

    fun setBorderVisible(visible : Boolean)

    fun createBuyersMarkets(buyers : Array<Buyer>)

    fun isBuyersMarketsCreated() : Boolean

    fun isBuyersMarketsVisible() : Boolean

    fun setBuyersMarketsVisible(visible : Boolean)


   // fun setActiveBuyers(activeBuyers : Array<Buyer>)

    //fun setSleepBuyers()

    //fun clearAll()
}