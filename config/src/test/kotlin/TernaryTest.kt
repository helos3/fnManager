import org.junit.Test
import java.util.stream.Stream

/**
 * Created by berlogic on 20.04.17.
 */


class TernaryTest {

    @Test
    fun abc() {
        var start = System.currentTimeMillis()
        if (false) Stream.iterate(1, {it + 1}).limit(1000000) else 5
        println(System.currentTimeMillis() - start)

        start = System.currentTimeMillis()
        Stream.iterate(1, {it + 1}).limit(1000000).takeIf { false } ?: 5
        println(System.currentTimeMillis() - start)

    }
}