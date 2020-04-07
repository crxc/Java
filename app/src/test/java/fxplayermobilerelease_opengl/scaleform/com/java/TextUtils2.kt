package fxplayermobilerelease_opengl.scaleform.com.java

import org.junit.Test
import java.io.*
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.andR.string.com/tools/testing).
 */
class TextUtils2 {
    val path = "/home/crxc/Android/project/project/svn/andriod/wanpingmarket"
    val output = "/home/crxc/code.txt"
    val cache = "/home/crxc/cache"

    init {
    }

    private var map: HashMap<String, Int> = HashMap()

    val outfile = File(output)
    @Test
    fun main() {
        val file = File(path)
        if(file.exists()){
            analyze(file)
        }else{
            print("1111")
        }
    }

    fun analyze(file: File) {
        file.listFiles().forEach {
            if (it.isDirectory) {
                analyze(it)
            } else if (it.name.endsWith(".kt")) {
                copykt(it)
            }
        }
    }

    private fun copykt(dir: File) {
        var reader: BufferedReader? = null
        var writer: BufferedWriter? = null
        var num = 0
        try {
            reader = BufferedReader(FileReader(dir))
            writer = BufferedWriter(FileWriter(outfile, true))
            while (true) {
                val s = reader.readLine() ?: break
                num++
                if(s.replace(" ","").isNotEmpty()){
                    writer.write(s)
                    writer.newLine()
                }
            }
        } catch (e: Exception) {
            println(e.message)
        } finally {
            reader?.close()
            writer?.close()
        }
    }


    private fun copyFile(oldFile: File, newFile: File) {
        val inputStream = BufferedInputStream(FileInputStream(oldFile))
        val outputStream = BufferedOutputStream(FileOutputStream(newFile))
        val buffer = ByteArray(1024)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            outputStream.write(buffer, 0, len)
        }
        inputStream.close()
        outputStream.close()
    }

    private fun delFile(file: File) {
        file.delete()
    }

    private fun moveFile(oldFile: File, newFile: File) {
        copyFile(oldFile, newFile)
        delFile(oldFile)
    }

    private fun fetchIndex(prefix: String): String {
        val i = map[prefix]
        map[prefix] = i!! + 1
        return i.toString()
    }

    fun getSubFile(file: File): Array<File> {
        return file.listFiles { pathname ->
            !("." == pathname.name[0] + "" && !pathname.isDirectory)
        }
    }


}

private fun String.getStringBeanIn(mutableSet: MutableSet<TextUtils.StringBean>?): TextUtils.StringBean? {
    mutableSet!!.forEach {
        if (it.string == this) {
            return it
        }
    }
    return null
}
