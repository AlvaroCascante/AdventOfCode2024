package third

import Solution

/*
* The goal of the program is just to multiply some numbers.
* It does that with instructions like mul(X,Y), where X and Y are
* each 1-3 digit numbers. For instance, mul(44,46) multiplies 44 by 46
* to get a result of 2024. Similarly, mul(123,4) would multiply 123 by 4.
*
* What do you get if you add up all of the results of the multiplications?
*
* SECOND PART
* There are two new instructions you'll need to handle:
*
*  - The do() instruction enables future mul instructions.
*  - The don't() instruction disables future mul instructions.
*  Only the most recent do() or don't() instruction applies.
*  At the beginning of the program, mul instructions are enabled.
 */
class ThirdDay: Solution {

    override fun solve(): Any {
        // FIRST PART -- Read the file
        //val data = readFile("/second_input.txt")

        // SECOND PART -- Read the file but remove the don'ts
        val data = removeDonts()

        // Pattern to find the multiplication instructions
        val pattern = Regex("""mul\(\d{1,3},\d{1,3}\)""")

        var result = 0L

        val matches = pattern.findAll(data)
        for (match in matches) {
            val multipliers = match.value
                .replace("mul(","")
                .replace(")","")
                .split(",")

            // Obtain values from the instruction and do the math
            val m1 = multipliers[0].toInt()
            val m2 = multipliers[1].toInt()
            result += m1 * m2
        }

        return result
    }

    // Remove the don't() from the file until found a do()
    private fun removeDonts(): String {
        val data = readFileAsString("/third_input.txt")
        var dontIndex = -1
        var doIndex = -1
        var modifiedString = data
        while(true) {
            dontIndex = modifiedString.indexOf("don't()")
            doIndex = modifiedString.indexOf("do()",  dontIndex + 7)
            if (dontIndex == -1)
                break
            modifiedString = modifiedString.removeRange(dontIndex, doIndex)
        }
        return modifiedString
    }

    override fun readFile(fileName: String): List<String> {
        return this::class.java.getResource(fileName)?.readText()?.lines() ?: emptyList()
    }

    // Read the file as one string, not by lines
    private fun readFileAsString(fileName: String): String {
        return this::class.java.getResource(fileName)?.readText() ?: ""
    }
}