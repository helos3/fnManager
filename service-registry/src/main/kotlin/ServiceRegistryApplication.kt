import com.ecwid.consul.v1.ConsulClient
import com.ecwid.consul.v1.QueryParams
import com.ecwid.consul.v1.agent.model.NewService
import spark.Spark
import java.util.*
import javax.imageio.spi.ServiceRegistry

/**
 * Created by berlogic on 18.04.17.
 */


class ServiceRegistryApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            register()
        }

        val client = ConsulClient()


        fun register(params : Map<String, String>) : Boolean {
            val service = NewService()
            service.address = params["address"] ?: return false
            service.id = UUID.randomUUID().toString()
            service.name = params["name"] ?: return false
            service.tags = params["tags"]?.split(",") ?: return false
            service.port = params["port"]?.toInt() ?: return false

            val check = NewService.Check()
            check.script = "ping -c1 ${service.address}:${service.port}"
            check.interval = "30"
            service.check = check

            client.agentServiceRegister(service)
            return true
        }
    }
}


//fun main(args: Array<String>) : Boolean {
//    val a  = mapOf<String, String>("hui" to "nahui")
//    val abc = a["nehui"] ?: return false
//    println("huita")
//    return true
//}