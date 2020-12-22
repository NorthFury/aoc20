package d22

import java.io.File
import java.util.*

fun main() {
    val (deck1, deck2) = File("input/d22.input").readText()
        .split("\n\n").filter(String::isNotBlank)
        .map { playerDeckInput ->
            playerDeckInput.reader().readLines().filter(String::isNotBlank)
                .drop(1).mapNotNull(String::toIntOrNull)
        }

    playNormalCombat(deck1, deck2).second.let(::println)

    playRecursiveCombat(deck1, deck2).second.let(::println)
}

private fun playNormalCombat(p1Deck: List<Int>, p2Deck: List<Int>): Pair<Int, Int> {
    val deck1 = LinkedList(p1Deck)
    val deck2 = LinkedList(p2Deck)
    while (deck1.isNotEmpty() && deck2.isNotEmpty()) {
        val c1 = deck1.remove()
        val c2 = deck2.remove()
        if (c1 > c2) {
            deck1.add(c1)
            deck1.add(c2)
        } else {
            deck2.add(c2)
            deck2.add(c1)
        }
    }

    if (deck2.isEmpty()) return 1 to computeDeckScore(deck1)
    return 2 to computeDeckScore(deck2)
}

private fun playRecursiveCombat(p1Deck: List<Int>, p2Deck: List<Int>): Pair<Int, Int> {
    val previousStates = mutableSetOf<Pair<List<Int>, List<Int>>>()

    val deck1 = LinkedList(p1Deck)
    val deck2 = LinkedList(p2Deck)
    while (deck1.isNotEmpty() && deck2.isNotEmpty()) {
        val state = deck1.toList() to deck1.toList()
        if (previousStates.contains(state)) {
            return 1 to computeDeckScore(state.first)
        }
        previousStates.add(state)

        val c1 = deck1.remove()
        val c2 = deck2.remove()
        val winner = when {
            deck1.size >= c1 && deck2.size >= c2 ->
                playRecursiveCombat(deck1.take(c1), deck2.take(c2)).first
            else -> if (c1 > c2) 1 else 2
        }
        when (winner) {
            1 -> {
                deck1.add(c1)
                deck1.add(c2)
            }
            2 -> {
                deck2.add(c2)
                deck2.add(c1)
            }
        }
    }

    if (deck2.isEmpty()) return 1 to computeDeckScore(deck1)
    return 2 to computeDeckScore(deck2)
}

private fun computeDeckScore(deck: List<Int>) = deck.reversed().mapIndexed { i, it -> (i + 1) * it }.sum()
