package fxplayermobilerelease_opengl.scaleform.com.java

import org.junit.Test
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by crxc on 19-1-17.
 * Email:jdxbwsbf@gmail.com
 */
class SudokuUtil {
    data class Position(val x: Int, val y: Int)

    private lateinit var list: ArrayList<Position>
    private var map: HashMap<Position, Int> = HashMap()

    init {
        initMap()
//        map[Position(1,3)]=8
//        map[Position(7, 9)] = 6
//        map[Position(2,1)]=2
    }

//    private fun initMap() {
//        map[Position(1, 1)] = 7
//        map[Position(1, 2)] = 9
//        map[Position(1, 5)] = 1
//        map[Position(1, 8)] = 6
//        map[Position(2, 3)] = 5
//        map[Position(2, 6)] = 8
//        map[Position(2, 9)] = 9
//        map[Position(3, 2)] = 1
//        map[Position(3, 3)] = 3
//        map[Position(3, 5)] = 6
//        map[Position(3, 6)] = 9
//        map[Position(4, 1)] = 6
//        map[Position(4, 3)] = 4
//        map[Position(4, 8)] = 9
//        map[Position(5, 2)] = 5
//        map[Position(5, 8)] = 3
//        map[Position(6, 2)] = 7
//        map[Position(6, 7)] = 6
//        map[Position(6, 9)] = 8
//        map[Position(7, 4)] = 3
//        map[Position(7, 5)] = 8
//        map[Position(7, 7)] = 9
//        map[Position(7, 8)] = 5
//        map[Position(8, 4)] = 9
//        map[Position(8, 7)] = 7
//        map[Position(8, 8)] = 8
//        map[Position(9, 2)] = 8
//        map[Position(9, 5)] = 4
//        map[Position(9, 8)] = 2
//        map[Position(9, 9)] = 1
//    }

    private fun initMap() {
        map[Position(1, 1)] = 6
        map[Position(1, 5)] = 5
        map[Position(1, 6)] = 2
        map[Position(2, 2)] = 5
        map[Position(2, 4)] = 3
        map[Position(2, 9)] = 1
        map[Position(3, 4)] = 7
        map[Position(3, 6)] = 8
        map[Position(3, 8)] = 5
        map[Position(3, 9)] = 2
        map[Position(4, 2)] = 7
        map[Position(4, 7)] = 4
        map[Position(5, 1)] = 8
        map[Position(5, 2)] = 6
        map[Position(5, 8)] = 3
        map[Position(5, 9)] = 5
        map[Position(6, 3)] = 2
        map[Position(6, 8)] = 8
        map[Position(7, 1)] = 9
        map[Position(7, 2)] = 4
        map[Position(7, 4)] = 6
        map[Position(7, 6)] = 1
        map[Position(8, 1)] = 7
        map[Position(8, 6)] = 3
        map[Position(8, 8)] = 4
        map[Position(9, 4)] = 4
        map[Position(9, 5)] = 7
        map[Position(9, 9)] = 9
    }

    @Test
    fun main(): Unit {
//        printMap()
        (1..100).forEach {
            (1..100).forEach {
                start()
            }
            if (map.size != 81) {
                map.clear()
                initMap()
            } else {
                printMap()
                return
            }
        }
    }

    private var needGuess = false
    private var guessNum = 0
    private fun start() {
        var need = needGuess
        loop@ for (x in 1..9) {
            loop2@ for (y in 1..9) {
                needGuess = true
                val position = Position(x, y)
                if (map[position] != null) {
                    continue@loop2
                } else {
                    val numList = fetchNumList(position)
                    if (numList.size == 1) {
                        map[position] = numList[0]
                        needGuess = false
//                        println("(${position.x},${position.y}) = ${numList[0]}")
                        break@loop
                    }
                    if (need && numList.size == 2) {
                        map[position] = numList[Random().nextInt(2)]
                        needGuess = false
                        guessNum++
                        break@loop
                    }
                }
            }
        }
        verify()
    }

    private fun verify() {
        for (xFactor in 0..2) {
            for (yFactor in 0..2) {
                val hashMap = HashMap<Int, Int>()
                val hashMap2 = HashMap<Position, ArrayList<Int>>()
                for (x in (xFactor * 3 + 1)..(xFactor * 3 + 3)) {
                    for (y in (yFactor * 3 + 1)..(yFactor * 3 + 3)) {
                        val position = Position(x, y)
                        if (map[position] != null) {
                            continue
                        }
                        val numList = fetchNumList(position)
                        hashMap2[position] = numList
                        numList.forEach {
                            if (hashMap.containsKey(it)) {
                                hashMap[it] = hashMap[it]!! + 1
                            } else {
                                hashMap[it] = 1
                            }
                        }
                    }
                }
                hashMap.forEach { entry ->
                    if (entry.value == 1) {
                        hashMap2.forEach { entry2 ->
                            if (entry2.value.contains(entry.key)) {
                                map[entry2.key] = entry.key
                                needGuess = false
//                                println("(${entry2.key.x},${entry2.key.y}) = ${entry.key}")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun fetchNumList(position: Position): ArrayList<Int> {
        val arrayListOf = arrayListOf<Int>()
        if (map[position] != null) {
            map[position]?.let { arrayListOf.add(it) }
            return arrayListOf
        } else {
            (1..9).forEach {
                arrayListOf.add(it)
            }
            val quadList = fetchPositionQuadList(position)
            quadList.forEach { p ->
                map[p]?.let { num ->
                    arrayListOf.remove(num)
                }
            }
            (1..9).forEach {
                map[Position(position.x, it)]?.let { num ->
                    arrayListOf.remove(num)
                }
            }
            (1..9).forEach {
                map[Position(it, position.y)]?.let { num ->
                    arrayListOf.remove(num)
                }
            }
        }
        return arrayListOf
    }

    private fun fetchPositionQuadList(position: Position): ArrayList<Position> {
        val arrayListOf = arrayListOf<Position>()
        val xStart = ((position.x + 2) / 3 - 1) * 3 + 1
        val xEnd = xStart + 2
        val yStart = ((position.y + 2) / 3 - 1) * 3 + 1
        val yEnd = yStart + 2
        (xStart..xEnd).forEach { x ->
            (yStart..yEnd).forEach { y ->
                arrayListOf.add(Position(x, y))
            }
        }
        return arrayListOf
    }

    @Test
    public fun testPositionQuad() {
//        fetchPositionQuadList(Position(4, 1))
//            .forEach {
//                print("(${it.x},${it.y})")
//            }
        fetchNumList(Position(2, 1))
            .forEach {
                print("$it ")
            }
    }

    private fun printMap() {
        (1..9).forEach { y ->
            val builder = StringBuilder()
            (1..9).forEach { x ->
                var num = map[Position(x, y)]
                if (num == null) {
                    builder.append(" . ")
                } else {
                    builder.append(" $num ")
                }
            }
            println(builder.toString())
        }
    }
}