package fxplayermobilerelease_opengl.scaleform.com.java

import org.junit.Test

import java.math.BigDecimal
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class YGOUtil {
    @Test
    fun main() {
//        print(BigDecimal("9") / BigDecimal("3"))
        print(fetchProbability(9, 4, 20))
//        print(fetchProbability(5, 5, 25))
//        print(fetchProbability(3, 4, 20))
//        draw(4)
    }

    /**
     * num:卡牌数量
     * handsNum:起手手牌数量
     * total:卡组卡牌数量
     */
    fun fetchProbability(num: Int, handsNum: Int = 4, total: Int = 20) {
        val i = Math.min(num, handsNum)
        print("假设牌组总共${total}张牌，起手摸${handsNum}张牌，某牌A有${num}张，则：\n")
        var totalP = BigDecimal("0")
        (1..i).forEach {
            totalP = totalP.add(fetchPossibility(num, handsNum, it, total))
        }
        print("总计$totalP\n")
    }

    private fun fetchPossibility(num: Int, handsNum: Int, it: Int, total: Int): BigDecimal? {
        val p =
            (combinationNum(handsNum - it, total - num) * combinationNum(it, num)).divide(
                combinationNum(handsNum, total),
                6,
                BigDecimal.ROUND_HALF_UP
            )
        print("起手${handsNum}张牌，有${it}张所选牌的概率为$p\n")
        return p
    }

    fun combinationNum(num: Int, total: Int): BigDecimal {
        return factorial(total) / factorial(num) / factorial(total - num)
    }

    fun factorial(n: Int): BigDecimal {
        if (n == 0) return BigDecimal("1")
        return fact(n)
    }


    tailrec fun fact(n: Int, accum: BigDecimal = BigDecimal("1")): BigDecimal {
        val soFar = BigDecimal(n.toString()) * accum
        return if (n <= 1) {
            soFar
        } else {
            fact(n - 1, soFar)
        }
    }

    private val deckList = arrayListOf<YGOUtil2.Card>()

    init {
        deckList.add(YGOUtil2.Card("Bluster Blader", "1"))
        deckList.add(YGOUtil2.Card("Bluster Blader", "1"))
//        deckList.add(YGOUtil2.Card("Bluster Blader", "1"))
        deckList.add(YGOUtil2.Card("龙破之证", "2"))
//        deckList.add(YGOUtil2.Card("龙破之证", "2"))
        deckList.add(YGOUtil2.Card("破坏剑士的伴龙", "1"))
        deckList.add(YGOUtil2.Card("破坏剑士的伴龙", "1"))
        deckList.add(YGOUtil2.Card("破坏剑士的伴龙", "1"))
        deckList.add(YGOUtil2.Card("破坏剑士融合", "2"))
//        deckList.add(YGOUtil2.Card("破坏剑士融合", "2"))
//        deckList.add(YGOUtil2.Card("破坏剑士融合", "2"))
        deckList.add(YGOUtil2.Card("DNA", "3"))
        deckList.add(YGOUtil2.Card("DNA", "3"))
        deckList.add(YGOUtil2.Card("DNA", "3"))
        deckList.add(YGOUtil2.Card("风筝", "1"))
        deckList.add(YGOUtil2.Card("风筝", "1"))
        deckList.add(YGOUtil2.Card("召唤魔术", "2"))
        deckList.add(YGOUtil2.Card("召唤师", "1"))
        deckList.add(YGOUtil2.Card("召唤师", "1"))
        deckList.add(YGOUtil2.Card("魔神王", "1"))
        deckList.add(YGOUtil2.Card("魔晶龙", "1"))
        deckList.add(YGOUtil2.Card("宇宙风", "1"))
        deckList.add(YGOUtil2.Card("宇宙风", "1"))
        deckList.add(YGOUtil2.Card("宇宙风", "1"))
    }

    private val handCards = arrayListOf<YGOUtil2.Card>()

    fun draw(num: Int) {
        for (i in 0 until num) {
            draw()
        }

    }

    fun draw(): Unit {
        val random = Random()
        val size = deckList.size
        val nextInt = random.nextInt(size)
        val card = deckList[nextInt]
        handCards.add(card)
        deckList.removeAt(nextInt)
        print("draw ${card.name}\n")
    }

}
// 5 0 10 5 5 10 0 5 10 10 0