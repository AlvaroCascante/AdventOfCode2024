package first

import Solution
import utils.FileUtils
import kotlin.math.abs

/*
 * Pair up the numbers and measure how far apart they are. Pair up the
 * smallest number in the left list with the smallest number in the right list,
 * then the second-smallest left number with the second-smallest
 * right number, and so on.
 *
 * Within each pair, figure out how far apart the two numbers are;
 * you'll need to add up all of those distances. For example, if you pair
 * up a 3 from the left list with a 7 from the right list,
 * the distance apart is 4; if you pair up a 9 with a 3, the distance apart is 6.
 *
 * SECOND PART
 * This time, you'll need to figure out exactly how often each number
 * from the left list appears in the right list. Calculate a total similarity
 * score by adding up each number in the left list after multiplying it by
 * the number of times that number appears in the right list.
 */
class FirstDay: Solution {

    override fun solve(): Any {
        val data = FileUtils.readFileAsList("/first_input.txt")

        val list1 = mutableListOf<Long>()
        val list2 = mutableListOf<Long>()

        // Obtain the two lists independently
        data.forEach { item ->
            val part = item.split("   ")
            try {
                list1.add(part[0].toLong())
                list2.add(part[1].toLong())
            } catch (e: NumberFormatException) {
                println("Error: $e")
            }
        }

        // Sort the lists
        list1.sort()
        list2.sort()

        // Get the distance between the items in the lists
        var result = 0L
        for (i in list1.indices) {
            val line1 = list1[i]
            val line2 = list2[i]
            result += abs(line1 - line2) // Get absolute value
        }

        println("First Result: $result")

        //Using a map to store the digits, this way no re counting would be needed
        val resultMap: MutableMap<Long, Long> = mutableMapOf()
        var result2 = 0L
        for (i in list1.indices) {
            if (resultMap.contains(list1[i])) {
                result2 += resultMap[list1[i]]!!
            } else {
                val count = list2.count { it == list1[i] }
                resultMap[list1[i]] = list1[i] * count
                result2 += list1[i] * count
            }
        }

        println("Second Result: $result")
        return result + result2
    }
}