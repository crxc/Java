package fxplayermobilerelease_opengl.scaleform.com.java

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        (1..1000).forEach {
            Thread {
                println(it)
            }.start()
        }
        Thread.sleep(1000)
    }
}
