import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.config.server.EnableConfigServer

/**
 * Created by rushan on 2/12/2017.
 */

@SpringBootApplication
@EnableConfigServer
class ConfigApplication {
    fun main(args: Array<String>) {
        SpringApplication.run(ConfigApplication::class.java, *args)
    }
}
