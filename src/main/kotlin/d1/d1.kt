package d1

import java.io.File

fun main() {
    val values = File("input/d1.input").readLines()
        .filter { it.isNotBlank() }
        .mapNotNull { it.toIntOrNull() }
        .sorted()

    for (i in values.indices) {
        for (j in (i + 1)..values.lastIndex) {
            for (k in (j + 1)..values.lastIndex) {
                val a = values[i]
                val b = values[j]
                val c = values[k]
                if (a + b + c == 2020) {
                    println(a * b * c)
                    return
                }
            }
        }
    }
}