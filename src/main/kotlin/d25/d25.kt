package d25

import java.io.File

fun main() {
    val (cardPublicKey, doorPublicKey) = File("input/d25.input")
        .readLines().filter(String::isNotBlank)
        .mapNotNull(String::toLongOrNull)

    val cardLoopSize = findLoopSize(cardPublicKey, 7)
    val doorLoopSize = findLoopSize(doorPublicKey, 7)

    transform(cardPublicKey, doorLoopSize).let(::println)
    transform(doorPublicKey, cardLoopSize).let(::println)
}

private fun findLoopSize(key: Long, subject: Long): Int {
    var loops = 0
    var acc = 1L
    while (acc != key) {
        acc = acc * subject % 20201227L
        loops++
    }
    return loops
}

private fun transform(subject: Long, loopSize: Int): Long {
    var acc = 1L
    var loops = 0
    while (loops++ < loopSize) {
        acc = acc * subject % 20201227L
    }
    return acc
}
