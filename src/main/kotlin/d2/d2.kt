package d2

import java.io.File

fun main() {
    val regex = Regex("""(\d+)-(\d+) ([a-z]): ([a-z]+)""")
    val entries = File("input/d2.input").readLines()
        .filter { it.isNotBlank() }
        .mapNotNull(regex::find)
        .map {
            val (min, max, char, password) = it.destructured
            Entry(min.toInt(), max.toInt(), char.first(), password)
        }

    println(entries.count(Entry::isValid))

    println(entries.count(Entry::isValidSecond))
}

data class Entry(
    val min: Int,
    val max: Int,
    val char: Char,
    val password: String,
) {
    fun isValid(): Boolean {
        val count = password.count { it == char }
        return count in min..max
    }

    fun isValidSecond(): Boolean {
        val first = password.length >= min && password[min - 1] == char
        val second = password.length >= max && password[max - 1] == char
        return first xor second
    }
}