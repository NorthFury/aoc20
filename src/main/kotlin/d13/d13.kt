package d13

import java.io.File

fun main() {
    val input = File("input/d13.input").readLines()
    val earliestTimestamp = input[0].toInt()
    val busses = input[1].split(',').map(String::toIntOrNull)

    part1(busses, earliestTimestamp)

    // too slow
//    busses.mapIndexedNotNull { index, it -> it?.let { index.toLong() to it.toLong() } }
//        .let(::part2TooSlow)
//        .let(::println)

    // still too slow
//    busses.mapIndexedNotNull { index, it -> it?.let { index.toLong() to it.toLong() } }
//        .map { it.second - it.first to it.second } // to match the CRT the modulo needs to be adjusted
//        .let(::computeChineseRemainder)
//        .let(::println)

    busses.mapIndexedNotNull { index, it -> it?.let { index.toLong() to it.toLong() } }
        .map { it.second - it.first to it.second } // to match the CRT the modulo needs to be adjusted
        .let { pairs ->
            val aArray = pairs.map { it.first }.toLongArray()
            val nArray = pairs.map { it.second }.toLongArray()
            chineseRemainderRosettaCode(nArray, aArray)
        }
        .let(::println)
}

private fun part1(busses: List<Int?>, earliestTimestamp: Int) {
    busses.filterNotNull()
        .map { it to it * (earliestTimestamp / it + 1) }
        .minByOrNull { it.second }
        ?.let { it.first * (it.second - earliestTimestamp) }
        ?.let(::println)
}

private fun part2TooSlow(busses: List<Pair<Long, Long>>): Long {
    val indexedBusses = busses.sortedBy { it.second }
    val (referenceBusIndex, referenceBus) = busses.maxByOrNull { it.second } ?: throw Exception("up")

    var iteration = 0
    while (true) {
        val timestamp = iteration * referenceBus - referenceBusIndex
        val isCorrect = indexedBusses.all { (i, it) -> (timestamp + i) % it == 0L }
        if (isCorrect) return timestamp
        iteration++
    }
}

// Chinese Remainder Theorem sieve approach
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

// yup... totally copy pasted
private fun chineseRemainderRosettaCode(n: LongArray, a: LongArray): Long {
    val prod = n.fold(1L) { acc, i -> acc * i }
    var sum = 0L
    for (i in n.indices) {
        val p = prod / n[i]
        sum += a[i] * multInv(p, n[i]) * p
    }
    return sum % prod
}

private fun multInv(a: Long, b: Long): Long {
    if (b == 1L) return 1L
    var aa = a
    var bb = b
    var x0 = 0L
    var x1 = 1L
    while (aa > 1) {
        val q = aa / bb
        var t = bb
        bb = aa % bb
        aa = t
        t = x0
        x0 = x1 - q * x0
        x1 = t
    }
    if (x1 < 0) x1 += b
    return x1
}
