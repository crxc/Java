package fxplayermobilerelease_opengl.scaleform.com.java

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class YGOUtil {
    @Test
    fun main() {
        print(fetchProbability(6, 4))
        print(fetchProbability(3, 4))
    }

    /**
     * num:卡牌数量
     * handsNum:起手手牌数量
     * total:卡组卡牌数量
     */
    fun fetchProbability(num: Int, handsNum: Int = 4, total: Int = 20) {
        val i = Math.min(num, handsNum)
        print("假设牌组总共${total}张牌，起手摸${handsNum}张牌，某牌A有${num}张，则：\n")
        (1..i).forEach {
            fetchPossibility(num, handsNum, it, total)
        }
    }

    private fun fetchPossibility(num: Int, handsNum: Int, it: Int, total: Int) {
        val p =
            combinationNum(handsNum - it, total - num) * combinationNum(it, num) * 1f / combinationNum(handsNum, total)
        print("起手${handsNum}张牌，有${it}张所选牌的概率为$p,大约每${1/p}把会出现一次\n")
    }

    fun combinationNum(num: Int, total: Int): Long {
        return factorial(total) / factorial(num) / factorial(total - num)
    }

    fun factorial(n: Int): Long {
        if (n == 0) return 1
        return fact(n)
    }


    tailrec fun fact(n: Int, accum: Long = 1): Long {
        val soFar = n * accum
        return if (n <= 1) {
            soFar
        } else {
            fact(n - 1, soFar)
        }
    }
}
