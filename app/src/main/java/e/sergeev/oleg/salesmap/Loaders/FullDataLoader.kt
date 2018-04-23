package e.sergeev.oleg.salesmap.Loaders

import android.util.Log
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

    fun downloadBuyersPoint(status : String) : JSONArray{
        var data = StringBuilder()
        var jsonResponse = JSONArray()
        if (status.equals("0") or status.equals("1")){
            try {
                val connection = URL(baseBuyersUrl + status + territoryStr + territoryName).openConnection() as HttpURLConnection
                try {
                    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                        connection.inputStream.bufferedReader().forEachLine { it -> data.append(it) }
                        jsonResponse = JSONObject(data.toString()).getJSONArray("features")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    connection.disconnect()
                }
            }catch (e : Exception){
                e.printStackTrace()
            }
        }else{
            Log.i("Courutines", "status not weong status")
        }
        return jsonResponse
    }
    fun downloadBorders() : JSONArray {
        var coordinates = JSONArray()
        var stringBuilder = StringBuilder()
        var jsonResponse: JSONObject
        try {
            val connection = URL(baseBorderUrl + territoryName).openConnection() as HttpURLConnection
            try {
                connection.inputStream.bufferedReader().forEachLine { it -> stringBuilder.append(it) }
                jsonResponse = JSONObject(stringBuilder.toString())
                coordinates = jsonResponse.getJSONArray("features")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONArray("coordinates")
                        .getJSONArray(0)
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