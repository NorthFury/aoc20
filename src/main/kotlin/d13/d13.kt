package d13

import java.io.File

fun main() {
    val input = File("input/d13.input").readLines()
    val earliestTimestamp = input[0].toInt()
    val busses = input[1].split(',').map(String::toIntOrNull)

    part1(busses, earliestTimestamp)

    part2TooSlow(busses).let(::println)
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

    var timestampPrint = 1000000000000L
    var iteration = 155365474341L // 176360808711 checkpoint
    while (true) {
        val timestamp = iteration * referenceBus.toLong() - referenceBusIndex
        val isCorrect = indexedBusses.all { (i, it) -> (timestamp + i) % it == 0L }
        if (isCorrect) return timestamp
        iteration++
        if (timestampPrint < timestamp) {
            timestampPrint += 100000000000L
            println("$timestampPrint $iteration")
        }
    }
}
