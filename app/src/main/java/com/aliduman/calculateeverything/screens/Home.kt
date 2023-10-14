package com.aliduman.calculateeverything.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliduman.calculateeverything.components.CalculatorButton
import com.aliduman.calculateeverything.ui.theme.DarkRed
import com.aliduman.calculateeverything.ui.theme.MediumGray
import com.aliduman.calculateeverything.ui.theme.PrussianBlue



class Tokenizer(val input: String) {
    // for storing the position of the current character
    var pos = 0

    // for storing the current token
    var token: String? = null

    //Advancing to the next token in the input(tokens are like integers,operators,parenthesis etc)
    fun nextToken() {

        while (pos < input.length && input[pos].isWhitespace()) {
            pos++
        }

        if (pos == input.length) {
            token = null
            return
        }

        // if the current character is a digit or a decimal point, then we parse a number
        if (input[pos].isDigit() || input[pos] == '.') {

            val sb = StringBuilder()
            while (pos < input.length && (input[pos].isDigit() || input[pos] == '.')) {
                sb.append(input[pos])
                pos++
            }
            token = sb.toString()
            return
        }

        if ("+-×÷()".contains(input[pos])) {

            token = input[pos].toString()
            pos++
            return
        }

        throw IllegalArgumentException("Invalid character: ${input[pos]}")
    }
}

fun evaluate(expression: String): Double {

    val tokenizer = Tokenizer(expression)

    tokenizer.nextToken()

    return parseExpression(tokenizer)
}
fun parseExpression(tokenizer: Tokenizer): Double {

    var result = parseTerm(tokenizer)

    while (tokenizer.token in listOf("+", "-")) {

        val op = tokenizer.token!!

        tokenizer.nextToken()

        val term = parseTerm(tokenizer)

        result = when (op) {
            "+" -> result + term
            "-" -> result - term
            else -> throw IllegalStateException("Invalid operator: $op")
        }
    }

    return result
}

fun parseTerm(tokenizer: Tokenizer): Double {

    var result = parseFactor(tokenizer)
    while (tokenizer.token in listOf("×", "÷")) {

        val op = tokenizer.token!!
        tokenizer.nextToken()
        val factor = parseFactor(tokenizer)

        result = when (op) {
            "×" -> result * factor
            "÷" -> result / factor
            else -> throw IllegalStateException("Invalid operator: $op")
        }
    }
    return result

}

fun parseFactor(tokenizer: Tokenizer): Double {

    if (tokenizer.token == null) {
        throw IllegalArgumentException("Missing factor")
    }
    //Checking if the token is a number
    if (tokenizer.token!!.toDoubleOrNull() != null) {

        val value = tokenizer.token!!.toDouble()

        tokenizer.nextToken()

        return value
    }

    //Checking if the token is a unary operator
    if (tokenizer.token in listOf("+", "-")) {

        val op = tokenizer.token!!

        tokenizer.nextToken()

        val factor = parseFactor(tokenizer)

        return when (op) {
            "+" -> +factor
            "-" -> -factor
            else -> throw IllegalStateException("Invalid operator: $op")
        }
    }

    //Handling parenthesis
    if (tokenizer.token == "(" && tokenizer.input.indexOf(")", tokenizer.pos) != -1) { // Added this condition

        tokenizer.nextToken()

        // Parsing the expression inside the parenthesis like (2-6+4) etc
        val value = parseExpression(tokenizer)

        if (tokenizer.token == ")") {

            tokenizer.nextToken()

            return value
        } else {
            throw IllegalArgumentException("Missing closing parenthesis")
        }
    }
    else {
        throw IllegalArgumentException("Invalid factor: ${tokenizer.token}")
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun Home() {
    val buttonSpacing = 8.dp
    val expression = mutableStateOf("")

    fun append(char: String) {
        Log.d("append", "$char Expression Value:${expression.value}")
        if (char in "0123456789") {
            expression.value += char
        }else if(char in "+-×÷") {
            if (expression.value.isNotEmpty()) {
                val lastChar = expression.value.last()

                // if last char is an operator, replace it with the new operator
                if (lastChar in "+-×÷") {
                    expression.value = expression.value.dropLast(1)
                }
            }
            expression.value += char
        }else if(char == ".") {
            if (expression.value.isNotEmpty()) {
                val lastChar = expression.value.last()
                if (lastChar!='.') {
                    // if last char is an operator, and the current char is a dot, add a zero before the dot
                    if (lastChar in "+-×÷") {
                        expression.value += "0"
                    }
                    expression.value += char
                }
            }

        }else if(char =="("){
            if (expression.value.isNotEmpty()) {
                val lastChar = expression.value.last()
                // if last char is not a operator, add a multiplication operator before the parenthesis
                if (lastChar !in "+-×÷") {
                    expression.value += "×"
                }
            }
            expression.value += char
        }else if(char ==")"){
            expression.value += char
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(com.aliduman.calculateeverything.ui.theme.DarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing),
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth(),
                reverseLayout = true
            ) {
                item {
                    Text(
                        text = expression.value,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp, horizontal = 8.dp),
                        fontWeight = FontWeight.Light,
                        fontSize = 80.sp,
                        color = Color.White,
                        maxLines = 1
                    )
                }
            }
            Divider(
                color = MediumGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "AC",
                    color = DarkRed,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            expression.value = ""
                        }
                )

                CalculatorButton(
                    symbol = "(",
                    color = PrussianBlue,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append("(")
                        }
                )
                CalculatorButton(
                    symbol = ")",
                    color = PrussianBlue,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append(")")
                        }
                )

                CalculatorButton(
                    symbol = "÷",
                    color = PrussianBlue,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append("÷")
                        }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "7",
                    color = MediumGray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append("7")
                        }
                )
                CalculatorButton(
                    symbol = "8",
                    color = MediumGray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append("8")
                        }
                )
                CalculatorButton(
                    symbol = "9",
                    color = MediumGray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append("9")
                        }
                )
                CalculatorButton(
                    symbol = "×",
                    color = PrussianBlue,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append("×")
                        }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "4",
                    color = MediumGray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append("4")
                        }
                )
                CalculatorButton(
                    symbol = "5",
                    color = MediumGray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append("5")
                        }
                )
                CalculatorButton(
                    symbol = "6",
                    color = MediumGray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append("6")
                        }
                )
                CalculatorButton(
                    symbol = "-",
                    color = PrussianBlue,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append("-")
                        }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "1",
                    color = MediumGray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append("1")
                        }
                )
                CalculatorButton(
                    symbol = "2",
                    color = MediumGray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append("2")
                        }
                )
                CalculatorButton(
                    symbol = "3",
                    color = MediumGray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append("3")
                        }
                )
                CalculatorButton(
                    symbol = "+",
                    color = PrussianBlue,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append("+")
                        }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp,end = 16.dp,bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "0",
                    color = MediumGray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append("0")
                        }
                )
                CalculatorButton(
                    symbol = ".",
                    color = MediumGray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            append(".")
                        }
                )
                CalculatorButton(
                    symbol = "Del",
                    color = DarkRed,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            if (expression.value.isNotEmpty()) {
                                expression.value = expression.value.dropLast(1)
                            }
                        }
                )
                CalculatorButton(
                    symbol = "=",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            expression.value = try {
                                val result = evaluate(expression.value)
                                result.toString()
                            } catch (e: Exception) {
                                "Error"
                            }
                        }
                )
            }
        }
    }
}