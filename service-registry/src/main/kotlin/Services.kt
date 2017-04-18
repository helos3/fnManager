/**
 * Created by berlogic on 18.04.17.
 */
import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.obj
import com.github.salomonbrys.kotson.registerTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import spark.Spark.*
import spark.Request

fun register() {

    fun gson() =
        GsonBuilder()
                .registerTypeAdapter<Map<String, String>>{
                    deserialize {
                        it.json.obj.entrySet().map { it.key to it.value.asString }.toMap()
                    }
                }.create()



    get("/register", { req, res ->
        val success = ServiceRegistryApplication.register(
                mapOf(gson().fromJson(req.body())
                )
        )



        mutableMapOf("x" to "hui")
    })




}