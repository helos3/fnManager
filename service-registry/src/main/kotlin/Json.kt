import com.ecwid.consul.v1.health.model.HealthService
import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.obj
import com.github.salomonbrys.kotson.registerTypeAdapter
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement

fun JsonElement.toMap() = obj.entrySet().map { it.key to it.value.asString }.toMap()

fun toMapGson() = GsonBuilder()
        .registerTypeAdapter<Map<String, String>>
        { deserialize { it.json.toMap() } }
        .create()

fun HealthService.toJson() =
        jsonObject("address" to service.address,
                "port" to service.port,
                "name" to service.service,
                "id" to service.id,
                "tags" to service.tags.fold("") { res, str -> "$res,$str" })

