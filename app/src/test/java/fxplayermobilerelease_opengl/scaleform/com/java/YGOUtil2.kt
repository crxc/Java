package fxplayermobilerelease_opengl.scaleform.com.java

import org.junit.Test
import java.lang.Exception

import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class YGOUtil2 {

    data class Card(val name: String, val type: String)

    private val deckList = arrayListOf<Card>()
    private val handCards = arrayListOf<Card>()
    private val fieldCards = arrayListOf<Card>()
    private var num = 0//凡人意志数量
    private var turn = 1
    private var successNum = 0
    private var allTurn = 0
    private var sucNum = 0

    @Test
    fun main() {
        initList()
        (1..10000).forEach {
            try {
                startSimulate()
            } catch (e: Exception) {
                checkIsSuc()
            }

        }
        print(allTurn)
        print("average turn${allTurn / 10000f}")
        print("成功次数${sucNum}")
    }

    private fun startSimulate() {
        reset()
        initHandCard()
        startYourFirstTurn()
        startTurn()
    }

    private fun reset() {
        num = 0
        turn = 1
        deckList.clear()
        initList()
        handCards.clear()
        fieldCards.clear()
    }


    private fun startTurn() {
        val drawCard2 = drawCard2()
        if (drawCard2.name == "凡人的意志" && num < 2) {
            playACard(handCards.indexOf(drawCard2))
        }
        if (num == 0 && drawCard2.name == "上膛") {
            reload()
            (1..2).forEach {
                val haveCard = haveCard(Card("凡人的意志", "spell"), handCards)
                if (haveCard >= 0) {
                    playACard(haveCard)
                }
            }
        }
        if (checkIsSuc()) {
            print("我方回合数$turn")
            print("牌组还有${deckList.size}张卡")
            print("手牌有${handCards.size}张卡")
        } else {
            if (turn == 2) {
                val haveCard = haveCard(Card("normal", "normal"), handCards)
                if (haveCard >= 0) {
                    playACard(haveCard)
                }
            }
            endYourTurn()
            startTurn()
        }

    }

    private fun checkIsSuc(): Boolean {
        var normals = 0
        var b1 = false
        var b2 = false
        handCards.forEach {
            when {
                it.type == "normal" -> normals++
                it.name == "半封" -> b1 = true
                it.name == "融合" -> b2 = true
            }
        }
        if (b1 && b2 && normals >= 10) {
            print("success\n")
            successNum++
            allTurn += turn
            if (turn <= 3) {
                sucNum++
            }
            successNum++
            return true
        }
        return false
    }

    private fun startYourFirstTurn() {
        (1..2).forEach {
            val haveCard = haveCard(Card("凡人的意志", "spell"), handCards)
            if (haveCard >= 0) {
                playACard(haveCard)
            }
        }
        val haveCard1 = haveCard(Card("上膛", "spell"), handCards)
        if (num == 0 && (haveCard1 >= 0)) {
            handCards.removeAt(haveCard1)
            reload()
            (1..2).forEach {
                val haveCard = haveCard(Card("凡人的意志", "spell"), handCards)
                if (haveCard >= 0) {
                    playACard(haveCard)
                }
            }
        }
        if (haveCard(Card("凡人的意志", "spell"), fieldCards) >= 0) {
            val haveCard = haveCard(Card("上膛", "quick_spell"), handCards)
            if (haveCard >= 0) {
                playACard(haveCard)
            }
        }
        endYourTurn()
    }

    private fun endYourTurn() {
        print("第${turn}回合结束\n")
        turn += 1
    }

    private fun playACard(haveCard: Int) {
        fieldCards.add(handCards[haveCard])
        print("放置${handCards[haveCard].name}到场上\n")
        if (handCards[haveCard].name == "凡人的意志") {
            num++
        }
        handCards.removeAt(haveCard)
    }

    private fun haveCard(
        card: Card,
        handCards: ArrayList<Card>
    ): Int {
        handCards.forEachIndexed { index, card2 ->
            if (card.name == card2.name) return index
        }
        return -1
    }

    private fun initHandCard() {
        (1..4).forEach {
            val drawCard = drawCard()
        }
    }

    //非第一回合抽卡
    private fun drawCard2(): Card {
        if (num == 0) {
            return drawCard()
        } else {
            val random = Random()
            val size = deckList.size
            val nextInt = random.nextInt(size)
            val card = deckList[nextInt]
            handCards.add(card)
            deckList.removeAt(nextInt)
            print("draw $card\n")
            if (card.type == "normal" && num > 0) {
                if (num == 1) {
                    print("发动凡人的意志抽卡\n")
                    drawCard2()
                }
                if (num == 2) {
                    print("发动凡人的意志抽卡两次\n")
                    drawCard()
                    drawCard2()
                }
            } else if (card.name == "上膛") {
                handCards.remove(card)
                reload()
            } else if (haveCard(Card("上膛", "quick_spell"), fieldCards) >= 0) {
                fieldCards.removeAt(haveCard(Card("上膛", "quick_spell"), fieldCards))
                reload()
            }
            return card
        }
    }

    private fun reload() {
        print("发动上膛\n")
        deckList.addAll(handCards)
        val size = handCards.size
        handCards.clear()
        (1..size).forEach {
            drawCard()
        }
        if (haveCard(Card("normal", "normal"), handCards) >= 0) {
            if (num == 0) {
                return
            } else if (num == 1) {
                print("发动凡人的意志抽卡\n")
                drawCard2()
            } else if (num == 2) {
                print("发动凡人的意志抽卡\n")
                drawCard()
                drawCard2()
            }
        }
    }

    private fun drawCard(): Card {
        val random = Random()
        val nextInt = random.nextInt(deckList.size)
        val card = deckList[nextInt]
        handCards.add(card)
        print("draw $card\n")
        deckList.removeAt(nextInt)
        return card
    }

    private fun initList() {
        (1..22).forEach {
            deckList.add(Card("normal", "normal"))
        }
        (1..2).forEach {
            deckList.add(Card("凡人的意志", "spell"))
        }
        (1..3).forEach {
            deckList.add(Card("上膛", "quick_spell"))
        }
        deckList.add(Card("半封", "quick_spell"))
        deckList.add(Card("融合", "spell"))
//        deckList.add(Card("集中电流", "spell"))
    }


}
