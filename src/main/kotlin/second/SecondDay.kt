package second

import Solution

/*
* Report only counts as safe if both of the following are true:
*    - The levels are either all increasing or all decreasing.
*    - Any two adjacent levels differ by at least one and at most three.
* How many reports are safe?
*
*  SECOND PART
* Now, the same rules apply as before, except if removing a single level
* from an unsafe report would make it safe,
* the report instead counts as safe.
 */
class SecondDay: Solution {

    override fun solve(): Any {
        val data = readFile("/second_input.txt")

        var count = 0L
        data.forEach { item ->
            val list = mutableListOf<Long>()

            // Get the report as a list
            item.split(" ").forEach { itit ->
                list.add(itit.toLong())
            }

            // Run the validation to check if is valid
            if(runValidation(list)) {
                count++
            } else {
                // For the SECOND PART, check tolerance for invalid reports
                println("Item: $item - INVALID")
                if (validateTolerance(list)) {
                    count++
                }
            }
        }
        return count
    }

    // Return true if the report is valid
    private fun validateTolerance(list: MutableList<Long>): Boolean {
        // Remove one item at a time and check if in that way the report is valid
        for (i in list.indices) {
            val newList = list.filterIndexed { index, _ -> index != i}
            if (runValidation(newList)) {
                println("Valid: $newList")
                return true
            }
        }
        return false
    }

    // Return true if the report is valid
    private fun runValidation(list: List<Long>) : Boolean {
        var isUp = false
        var valid: Boolean

        try {
            //Check if the list is increasing or decreasing
            if(list[0] < list[1]) {
                isUp = true
                valid = true
            } else if (list[0] > list[1]) {
                isUp = false
                valid = true
            } else {
                // If the first two items are the same, the report is invalid
                valid = false
            }
        }  catch (e: Exception) {
            valid = false
        }

        var isFirst = true
        var n1 = 0L
        var n2: Long
        // Move through the list and validate the difference between the numbers
        for (listItem in list) {
            if(isFirst) {
                n1 = listItem
                isFirst = false
            } else {
                n2 = n1
                n1 = listItem
                if(!validate(n1, n2, isUp)) {
                    valid = false
                    break
                }
            }
        }
        return valid
    }

    // Return true if the difference between the two numbers is between 1 and 3
    private fun validate(n1: Long, n2: Long, isUp: Boolean): Boolean {
        return if(isUp) {
            (n1 - n2) in 1..3
        } else {
            (n2 -  n1) in 1..3
        }
    }

    override fun readFile(fileName: String): List<String> {
        return this::class.java.getResource(fileName)?.readText()?.lines() ?: emptyList()
    }
}