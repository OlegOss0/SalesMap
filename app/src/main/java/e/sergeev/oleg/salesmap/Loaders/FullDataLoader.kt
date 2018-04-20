package e.sergeev.oleg.salesmap.Loaders

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by o.sergeev on 05.04.2018.
 */
class FullDataLoader(territoryName : String) {
    val baseBorderUrl = "https://iup.balmiko.ru/map_test/getareas.php?&territory=" //add territory name
    val baseBuyersUrl = "https://iup.balmiko.ru/map_test/getpoints.php?status="// add status(1 -activ, 2-sleep), add "&territory=",add territory name
    val territoryStr = "&territory="
    val territoryName = territoryName
    lateinit var borders : Array<Double>

    fun downloadActiveBuyers() {
        var data = StringBuilder()
        var jsonResponse: JSONObject
        var statusActive = "1"
        try {
            val connection = URL(baseBuyersUrl + statusActive + territoryStr + territoryName).openConnection() as HttpURLConnection
            try {
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    var line: String = ""
                    connection.inputStream.bufferedReader().forEachLine { it -> data.append(it) }
                    jsonResponse = JSONObject(data.toString())
                    print("ok")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection.disconnect()
            }
        }catch (e : Exception){

        }
    }
    fun downloadBorders() : JSONArray {
        lateinit var coordinates : JSONArray
        var stringBuilder = StringBuilder()
        var jsonResponse: JSONObject
        try {
            val connection = URL(baseBorderUrl + territoryName).openConnection() as HttpURLConnection
            try {
                var line: String = ""
                connection.inputStream.bufferedReader().forEachLine { it -> stringBuilder.append(it) }
                jsonResponse = JSONObject(stringBuilder.toString())
                coordinates = jsonResponse.getJSONArray("features")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONArray("coordinates")
                        .getJSONArray(0)
                val coordinatesCount = coordinates.length()
                print("ok")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection.disconnect()
            }
        } catch (e: Exception) {

        }
        return coordinates
    }

}