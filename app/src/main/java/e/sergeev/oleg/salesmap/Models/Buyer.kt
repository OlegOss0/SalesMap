package e.sergeev.oleg.salesmap.Models

/**
 * Created by o.sergeev on 23.04.2018.
 */
data class Buyer (val id: Int?, val coordinates: MyPoint?) {
    var countPhones = 0
    var name = String()
    var adres = String()
    var status = String()
    var lastDate = String()
    var territory = String()
    var phones: ArrayList<String> = ArrayList()
}