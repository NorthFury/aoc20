package d13

import java.io.File

fun main() {
    val input = File("input/d13.input").readLines()
    val earliestTimestamp = input[0].toInt()
    val busses = input[1].split(',').map(String::toIntOrNull)

    part1(busses, earliestTimestamp)

    // too slow
    // part2TooSlow(busses).let(::println)

    // something is still wrong
    busses.mapIndexedNotNull { index, it -> it?.let { index.toLong() to it.toLong() } }
        .let(::computeChineseRemainder)
        .let(::println)
}

private fun part1(busses: List<Int?>, earliestTimestamp: Int) {
    busses.filterNotNull()
        .map { it to it * (earliestTimestamp / it + 1) }
        .minByOrNull { it.second }
        ?.let { it.first * (it.second - earliestTimestamp) }
        ?.let(::println)
}

private fun part2TooSlow(busses: List<Int?>): Long {
    val indexedBusses = busses
        .mapIndexedNotNull { index, it -> it?.let { index to it } }
        .sortedBy { it.second }
    val referenceBus = busses.filterNotNull().maxOrNull() ?: throw Exception("up")
    val referenceBusIndex = busses.indexOf(referenceBus)

    var iteration = 0
    while (true) {
        val timestamp = iteration * referenceBus.toLong() - referenceBusIndex
        val isCorrect = indexedBusses.all { (i, it) -> (timestamp + i) % it == 0L }
        if (isCorrect) return timestamp
        iteration++
    }
}

// Chinese Remainder Theorem
private fun computeChineseRemainder(values: List<Pair<Long, Long>>): Long =
    values.sortedByDescending { it.second }.reduce { (a1, n1), (a2, n2) ->
        computeForTwo(a1, n1, a2, n2) to n1 * n2
    }.first

private fun computeForTwo(a1: Long, n1: Long, a2: Long, n2: Long): Long {
    var acc = a1
    while (acc % n2 != a2) {
        acc += n1
    }
    return acc
}
