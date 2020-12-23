package d23

import java.io.File

fun main() {
    val inputValues = File("input/d23.input").readText().trim().map { (it - '0') }
    val part1Node = Node.fromList(inputValues)

    val inputMin = inputValues.minOrNull() ?: throw Exception("up")
    val inputMax = inputValues.maxOrNull() ?: throw Exception("up")

    shuffle(part1Node, 100, inputMin, inputMax)
    part1Node.find(1)?.toList()?.drop(1)?.joinToString("")?.let(::println)


    val part2Values = inputValues + (inputMax + 1).rangeTo(1_000_000).toList()
    val part2Node = Node.fromList(part2Values)
    shuffle(part2Node, 10_000_000, inputMin, 1_000_000)
    part2Node.find(1)
        ?.let { it.next().value.toLong() * it.next().next().value.toLong() }
        ?.let(::println)
}

private fun shuffle(start: Node, n: Int, min: Int, max: Int) {
    val referenceMap = start.getReferenceMap()
    var i = 0
    var current = start
    while (i < n) {
        i++
        val slice = current.sliceOutNext(3)
        val sliceValues = slice.toList()

        var insertPositionValue = current.value
        do {
            insertPositionValue = if (insertPositionValue == min) max else insertPositionValue - 1
        } while (insertPositionValue in sliceValues)
        val insertPosition = referenceMap[insertPositionValue] ?: throw Exception("up")
        insertPosition.insertSlice(slice)
        current = current.next()
    }
}

private class Node(val value: Int) {
    private var next: Node = this

    companion object {
        fun fromList(values: List<Int>): Node {
            val head = Node(values.first())
            values.drop(1).fold(head) { acc, it ->
                acc.add(it)
                acc.next
            }
            return head
        }
    }

    fun next(): Node = next

    fun add(value: Int) {
        val currentNext = next
        next = Node(value)
        next.next = currentNext
    }

    fun find(value: Int): Node? {
        var current = this
        do {
            if (current.value == value) return current
            current = current.next
        } while (current != this)
        return null
    }

    fun sliceOutNext(n: Int): Node {
        // this assumes this circular list contains at least n elements
        val result = next
        var sliceEnd = next
        var i = n
        while (i > 1) {
            sliceEnd = sliceEnd.next
            i--
        }
        next = sliceEnd.next
        sliceEnd.next = result
        return result
    }

    fun insertSlice(slice: Node) {
        var sliceEnd = slice
        while (sliceEnd.next != slice) {
            sliceEnd = sliceEnd.next
        }

        sliceEnd.next = next
        next = slice
    }

    fun toList(): List<Int> {
        val result = mutableListOf(value)
        var current = this
        while (current.next != this) {
            current = current.next
            result.add(current.value)
        }
        return result
    }

    fun getReferenceMap(): Map<Int, Node> {
        val result = mutableMapOf(value to this)
        var current = this
        while (current.next != this) {
            current = current.next
            result[current.value] = current
        }
        return result
    }
}
