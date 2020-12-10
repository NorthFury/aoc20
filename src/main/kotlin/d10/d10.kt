package d10

import java.io.File

fun main() {
    val adapters = File("input/d10.input").readLines()
        .filter(String::isNotBlank)
        .mapNotNull(String::toIntOrNull)

    val builtInAdapter = adapters.maxOrNull()?.let { it + 3 } ?: return
    val chargingOutlet = 0

    val diffs = (adapters + builtInAdapter + chargingOutlet)
        .sorted()
        .windowed(2) { (a, b) -> b - a }

    val jolts = diffs.groupingBy { it }.eachCount()
    println((jolts[1] ?: 0) * (jolts[3] ?: 0))

    getGroupsOfOne(diffs)
        .map { List(it) { 1 } }
        .map(::countCustomPermutationsSlow)
        .reduce { acc, it -> acc * it }
        .let(::println)

    // too slow... not worth running over the full set, regardless which recursive version is used
    // countCustomPermutations(diffs).let(::println)
}

private fun getGroupsOfOne(diffs: List<Int>): List<Int> {
    val result = mutableListOf<Int>()
    var count = 0
    diffs.forEach {
        when (it) {
            1 -> count++
            else -> {
                if (count > 1) result.add(count)
                count = 0
            }
        }
    }
    if (count > 1) result.add(count)

    return result
}

private fun countCustomPermutationsSlow(diffs: List<Int>): Long {
    if (diffs.size < 2) return 1

    val diff = diffs[0] + diffs[1]
    val remaining = diffs.drop(2)
    val untouched = listOf(diffs[1]) + remaining
    val countUntouched = countCustomPermutationsSlow(untouched)
    return when {
        diff > 3 -> countUntouched
        else -> countUntouched + countCustomPermutationsSlow(listOf(diff) + remaining)
    }
}

private fun countCustomPermutations(diffs: List<Int>, i: Int = 0, prepend: Int = 0): Long {
    if (prepend == 0) {
        if (diffs.size - i < 2) return 1L

        val diff = diffs[i] + diffs[i + 1]
        val countUntouched = countCustomPermutations(diffs, i + 1)
        return when {
            diff > 3 -> countUntouched
            else -> countUntouched + countCustomPermutations(diffs, i + 2, diff)
        }
    }

    if (diffs.size - i < 1) return 1L

    val diff = prepend + diffs[i]
    val countUntouched = countCustomPermutations(diffs, i)
    return when {
        diff > 3 -> countUntouched
        else -> countUntouched + countCustomPermutations(diffs, i + 1, diff)
    }
}
