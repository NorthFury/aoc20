package d14

import java.io.File

fun main() {
    val lines = File("input/d14.input").readLines().filter(String::isNotBlank)
    val memPattern = Regex("""mem\[(\d+)] = (\d+)""")

    lines.fold(AccPart1(Mask("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"), mutableMapOf())) { acc, line ->
        if (line.startsWith("mask")) {
            acc.mask = Mask(line.drop(7))
        } else {
            val (address, value) = memPattern.matchEntire(line)!!.destructured
            acc.mem[address.toLong()] = acc.mask.apply(value.toLong())
        }
        acc
    }
        .mem.values.sum()
        .let(::println)

    lines.fold(AccPart2(emptyList(), mutableMapOf())) { acc, line ->
        if (line.startsWith("mask")) {
            acc.masks = computeMasks(line.drop(7))
        } else {
            val (address, value) = memPattern.matchEntire(line)!!.destructured
            val addressValue = address.toLong()
            val valueValue = value.toLong()
            acc.masks.forEach { acc.mem[it.apply(addressValue)] = valueValue }
        }
        acc
    }
        .mem.values.sum()
        .let(::println)
}

private fun computeMasks(input: String): List<Mask> = input
    .fold(listOf("")) { acc, c ->
        when (c) {
            '0' -> acc.map { it + 'X' }
            '1' -> acc.map { it + '1' }
            else -> acc.flatMap { listOf(it + '1', it + '0') }
        }
    }
    .map(::Mask)

private data class AccPart1(
    var mask: Mask,
    var mem: MutableMap<Long, Long>
)

private data class AccPart2(
    var masks: List<Mask>,
    var mem: MutableMap<Long, Long>,
)

private class Mask(input: String) {
    private val zeroMask = input.fold(1L) { acc, c -> acc.shl(1) + (if (c == '0') 0 else 1) }
    private val oneMask = input.fold(0L) { acc, c -> acc.shl(1) + (if (c == '1') 1 else 0) }

    fun apply(value: Long): Long = value.and(zeroMask).or(oneMask)
}
