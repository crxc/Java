package fxplayermobilerelease_opengl.scaleform.com.java

import org.junit.Test
import java.io.*
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.andR.string.com/tools/testing).
 */
class TextUtils {
    val path = "/home/crxc/Android/gitProject/MediConCenDoctor_Android/base/src/main/java/com/yijie/crxc/base/api"
    val output = "/home/crxc/Android/gitProject/MediConCenDoctor_Android/base/src/main/res/values/strings.xml"
    val cache = "/home/crxc/cache"

    data class Bean(val path: String, val prefix: String)
    data class StringBean(val string: String, val id: String)

    private var list: ArrayList<Bean> = ArrayList()

    init {
        list.add(Bean(path, "base"))
    }

    private var map: HashMap<String, Int> = HashMap()
    private var stringMap: HashMap<String, MutableSet<StringBean>> = HashMap()

    @Test
    fun main() {
        list.forEach {
            map[it.prefix] = 0
            stringMap[it.prefix] = HashSet()
            val file = File(it.path)
            extractText(file, it.prefix)
        }
    }

    private fun extractText(file: File, prefix: String) {
        file.listFiles { pathname ->
            '.' != pathname.name[0]
        }.forEach {
            if (it.isDirectory) {
                extractText(it, prefix)
            } else {
                analyze(it, prefix)
            }
        }
        repairOutPutFile()
    }

    private fun repairOutPutFile() {
        val reader = BufferedReader(FileReader(outFile))
        val backupFile = File("$cache/${outFile.name}")
        val writer = BufferedWriter(FileWriter(backupFile))
        var line: String? = ""
        while (reader.readLine().also { line = it } != null) {
            if (!line!!.contains("</resources>")) {
                writer.write(line)
                writer.newLine()
            }
        }
        writer.write("</resources>")
        writer.close()
        reader.close()
        moveFile(backupFile, outFile)
    }

    val regex = "\"[^\"]*?[\\u4e00-\\u9fa5]+[^\"]*?\""
    private val pattern = Pattern.compile(regex)
    private val outFile = File(output)
    private val template = "fetchString"

    // "测试文字" "测试文字" "测试文字"
    private fun analyze(file: File, prefix: String) {
        var reader: BufferedReader? = null
        var writer: BufferedWriter? = null
        var backupsWriter: BufferedWriter? = null
        val backupFile = File("$cache/${file.name}")
        var num = 0
        try {
            reader = BufferedReader(FileReader(file))
            writer = BufferedWriter(FileWriter(outFile, true))
            backupsWriter = BufferedWriter(FileWriter(backupFile))

            while (true) {
                var s = reader.readLine() ?: break
                num++
                if (num == 2) {
//                    backupsWriter.write("import com.yijie.crxc.yijie.utils.MyStringUtils;")
//                    backupsWriter.newLine()
                }
                val find = HashSet<String>()
                val matcher = pattern.matcher(s)
                while (matcher.find()) {
                    val group = matcher.group()
                    println("find$group")
                    find.add(group.replace("", ""))
                }
                if (find.isEmpty()) {
                    backupsWriter.write(s)
                } else {
                    find.forEach {
                        val stringBeanIn = it.getStringBeanIn(stringMap[prefix])
                        if (stringBeanIn != null) {
                            when {
                                file.name.endsWith("java") -> {
                                    s = s.replace(
                                        it,
                                        "$template(R.string.$prefix${stringBeanIn.id})"
                                    )
                                }
                                file.name.endsWith("kt") -> {
                                    s = s.replace(
                                        it,
                                        "$template(R.string.$prefix${stringBeanIn.id})"
                                    )
                                }
                                file.name.endsWith("xml") -> {
                                    s = s.replace(it, "\"@string/$prefix${stringBeanIn.id}\"")
                                }
                            }
                        } else {
                            writer.newLine()
                            val fetchIndex = fetchIndex(prefix)
                            writer.write("    <string name=\"$prefix$fetchIndex\">$it</string>")
                            when {
                                file.name.endsWith("java") -> {
                                    s = s.replace(
                                        it,
                                        "$template(R.string.$prefix$fetchIndex)"
                                    )
                                }
                                file.name.endsWith("kt") -> {
                                    s = s.replace(it, "$template(R.string.$prefix$fetchIndex)")
                                }
                                file.name.endsWith("xml") -> {
                                    s = s.replace(it, "\"@string/$prefix$fetchIndex\"")
                                }
                            }
                            stringMap[prefix]!!.add(StringBean(it, fetchIndex))
                        }
                    }
                    backupsWriter.write(s)
                }
                backupsWriter.newLine()
            }
        } catch (e: Exception) {
            println(e.message)
        } finally {
            reader?.close()
            writer?.close()
            backupsWriter?.close()
            moveFile(backupFile, file)
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
