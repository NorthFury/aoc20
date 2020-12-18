package d18

import java.io.File

fun main() {
    val expressions = File("input/d18.input").readLines().filter(String::isNotBlank)

    expressions.map(::evaluateExpressionPart1).sum().let(::println)

    expressions.map(::evaluateExpressionPart2).sum().let(::println)
}

private fun evaluateExpressionPart1(expression: String): Long {
    var i = 0
    var acc = 0L
    var operation = '+'
    while (i < expression.length) {
        when (val c = expression[i]) {
            '+' -> operation = '+'
            '*' -> operation = '*'
            in '0'..'9' -> when (operation) {
                '+' -> acc += c - '0'
                '*' -> acc *= c - '0'
            }
            '(' -> {
                val subExpression = extractSubExpression(expression, i)
                val subExpressionValue = evaluateExpressionPart1(subExpression)
                when (operation) {
                    '+' -> acc += subExpressionValue
                    '*' -> acc *= subExpressionValue
                }
                i += subExpression.length
            }
        }
        i++
    }
    return acc
}

private fun evaluateExpressionPart2(expression: String): Long {
    var i = 0
    val acc = mutableListOf<Long>(0)
    var operation = '+'
    while (i < expression.length) {
        when (val c = expression[i]) {
            '+' -> operation = '+'
            '*' -> operation = '*'
            in '0'..'9' -> when (operation) {
                '+' -> acc.add((c - '0').toLong() + acc.removeLast())
                '*' -> acc.add((c - '0').toLong())
            }
            '(' -> {
                val subExpression = extractSubExpression(expression, i)
                val subExpressionValue = evaluateExpressionPart2(subExpression)
                when (operation) {
                    '+' -> acc.add(subExpressionValue + acc.removeLast())
                    '*' -> acc.add(subExpressionValue)
                }
                i += subExpression.length
            }
        }
        i++
    }
    return acc.reduce { a, b -> a * b }
}

private fun extractSubExpression(expression: String, start: Int): String {
    var open = 1
    var i = start + 1
    while (open > 0) {
        when (expression[i]) {
            '(' -> open++
            ')' -> open--
        }
        i++
    }
    return expression.substring(start + 1, i - 1)
}
