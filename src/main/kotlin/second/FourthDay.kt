package second

import Solution
import utils.FileUtils

/*
* Find one word: XMAS.
* This word search allows words to be horizontal, vertical, diagonal,
* written backwards, or even overlapping other words
*
* SECOND PART, find two MAS in the shape of an X, each MAS can be written forwards or backwards
*   M.S
*   .A.
*   M.S
 */
class FourthDay: Solution  {

    override fun solve(): Any {
        val data = getAllData()

        val pattern = Regex("""XMAS""")
        val patternBack = Regex("""SAMX""")

        var result = 0L

        data.data.forEach { item ->
            val matches = pattern.findAll(item).toList()
            val matchesBack = patternBack.findAll(item).toList()
            result += (matches.size + matchesBack.size)
        }

        data.dataInverted.forEach { item ->
            val matches = pattern.findAll(item).toList()
            val matchesBack = patternBack.findAll(item).toList()
            result += matches.size + matchesBack.size
        }

        data.dataDiagonalsRL.forEach { item ->
            val matches = pattern.findAll(item).toList()
            val matchesBack = patternBack.findAll(item).toList()
            result += matches.size + matchesBack.size
        }

        data.dataDiagonalsLR.forEach { item ->
            val matches = pattern.findAll(item).toList()
            val matchesBack = patternBack.findAll(item).toList()
            result += matches.size + matchesBack.size
        }

        println("Response1: $result")

        val result2 = secondPart(data.data)

        return result2
    }

    private fun convertListToMatrix(data: List<String>): Array<Array<Char>> {
        return data.map { it.toCharArray().toTypedArray() }.toTypedArray()
    }

    private fun getAllData(): AllData {
        val data = FileUtils.readFileAsList("/fourth_input.txt")

        return AllData(
            data = data,
            dataInverted = getInvertedData(data),
            dataDiagonalsLR = getDiagonalDataLR(data),
            dataDiagonalsRL = getDiagonalDataRL(data)
        )
    }

    private fun getInvertedData(data: List<String>): List<String> {
        val arrayData = convertListToMatrix(data)
        val invertedArray: Array<Array<Char>> = Array(arrayData[0].size) { Array(arrayData.size) { ' ' } }
        for ((i, array) in arrayData.withIndex()) {
            for ((j, barray) in array.withIndex()) {
                invertedArray[j][i] = barray
            }
        }
        return invertedArray.map { it.joinToString("") }
    }

    private fun getDiagonalDataLR(data: List<String>): List<String> {
        /*
        *              Y
        *              |
        *              |
        * _____________|______________X
        *              |
        *
        */

        val arrayData = convertListToMatrix(data)

        val sizeY = arrayData.size
        val sizeX = arrayData[0].size
        val lowerSize = if (sizeY < sizeX) sizeY else sizeX
        val diagonalsArray: Array<Array<Char>> =
            Array(arrayData.size + arrayData[0].size - 1) {
                Array(lowerSize) { ' ' }
            }

        var indexi = 0
        var indexj = 0

        for (j in (0..< sizeX)) { // Moving Horizontally
            indexj = 0
            val limit = if (sizeX - j > sizeY) sizeY else sizeX - j
            for (k in (0..< limit)) {
                diagonalsArray[indexi][indexj] = arrayData[k][k+j]
                indexj++

            }
            indexi++
        }
        for (j in (1..< sizeY)) {
            indexj = 0
            val limit = if (sizeY - j > sizeX) sizeX else sizeY - j
            for (k in (0..< limit)) {
                diagonalsArray[indexi][indexj] = arrayData[k+j][k]
                indexj++
            }
            indexi++
        }
        return diagonalsArray.map { it.joinToString("") }
    }

    private fun getDiagonalDataRL(data: List<String>): List<String> {
        /*
        *              Y
        *              |
        *              |
        * _____________|______________X
        *              |
        *
        */

        val arrayData = convertListToMatrix(data)

        val sizeY = arrayData.size
        val sizeX = arrayData[0].size
        val lowerSize = if (sizeY < sizeX) sizeY else sizeX
        val diagonalsArray: Array<Array<Char>> =
            Array(arrayData.size + arrayData[0].size - 1) {
                Array(lowerSize) { ' ' }
            }

        var indexi = 0
        var indexj = 0

        for (j in (sizeX - 1) downTo 0) { // Moving Horizontally
            indexj = 0
            val limit = if (j > sizeY - 1) sizeY else j + 1
            for (k in (0..< limit)) {
                diagonalsArray[indexi][indexj] = arrayData[k][j-k]
                indexj++

            }
            indexi++
        }
        for (j in (1..<sizeY)) {
            indexj = 0
            val limit = if (j > sizeY - 1) sizeY else sizeX - (sizeY - j)
            for (k in (sizeX -1 ) downTo limit) {
                diagonalsArray[indexi][indexj] = arrayData[j + (sizeX-k-1)][k]
                indexj++
            }
            indexi++
        }
        return diagonalsArray.map { it.joinToString("") }
    }

    private fun secondPart(data: List<String>): Int {
        var result = 0

        val pattern = Regex("A")        // Search for the letter S
        for (i in (1..<data.size - 1)) {     // Discard first and last line
            val matches = pattern.findAll(data[i]).toList()
            for (match in matches) {            // Iterate over the matches
                val index = match.range.first - 1
                if (index >= 0 && index < data[i].length - 2) { // Check if the index is valid, no on the edges
                    val prior = data[i - 1].substring(index, index + 3).removeRange(1..1) // Get the prior and post letters of the upper row
                    val post = data[i + 1].substring(index, index + 3).removeRange(1..1)  // Get the prior and post letters of the lower row
                    if (validateXMAS(prior, post)) { // Validate the XMAS pattern
                        result++
                    }
                }
            }
        }
        return result
    }

    private fun validateXMAS(prior: String, post: String):Boolean {
        if (prior == "MM" && post == "SS") {
            return true
        }
        if (prior == "SS" && post == "MM") {
            return true
        }
        if (prior == "MS" && post == "MS") {
            return true
        }
        if (prior == "SM" && post == "SM") {
            return true
        }
        return false
    }

    data class AllData (
        val data: List<String>,
        val dataInverted: List<String>,
        val dataDiagonalsLR: List<String>,
        val dataDiagonalsRL: List<String>
    )
}

// 1854 TO LOW