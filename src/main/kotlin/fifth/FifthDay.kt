package fifth

import Solution
import utils.FileUtils

class FifthDay: Solution {

    override fun solve(): Any {
        val data = getAllData()
        val result = printReports(data)

        val result2 = printInvalidReports(result.invalidReports, data.rules)
        println("Valid reports: ${result2.validReports}")
        return result2.validReports.sum()
    }

    data class InputData(
        val rules: Map<Int, List<Int>>,
        val reports: List<List<Int>>
    )

    data class ResultData(
        val validReports: List<Int> = mutableListOf(),
        val invalidReports: List<List<Int>> = mutableListOf()
    )

    private fun printInvalidReports(data: List<List<Int>>, rules: Map<Int, List<Int>>): ResultData {
        val validReports = mutableListOf<Int>()
        val invalidReports = mutableListOf<List<Int>>()

        for (report in data) {  // Iterate over reports
            val newReport = report.toMutableList()
            for (page in (report.indices)) { // Iterate pages
                for (index in (0)..< page) {
                    if (rules.contains(newReport[page])) {
                        if (rules[newReport[page]]!!.contains(newReport[index])) {
                            val tem = newReport[page]
                            newReport.removeAt(page)
                            newReport.add(index, tem)
                            break
                        }
                    }
                }
            }
            if (validateReport(newReport, rules)) {
                validReports.add(getMiddlePage(newReport))
            } else {
                invalidReports.add(newReport)
            }
        }
        return ResultData(validReports, invalidReports)
    }

    private fun printReports(data: InputData): ResultData {
        val validReports = mutableListOf<Int>()
        val invalidReports = mutableListOf<List<Int>>()

        for (report in data.reports) {  // Iterate over reports
            if (validateReport(report, data.rules)) {
                validReports.add(getMiddlePage(report))
            } else {
                invalidReports.add(report)
            }
        }
        return ResultData(validReports, invalidReports)
    }

    private fun validateReport(report: List<Int>, rules: Map<Int, List<Int>>): Boolean {
        var isValid = true
        for (index in report.indices) { // Iterate over each page in the report
            if (index == 0) { // First page no need validation
                continue
            } else {
                for (i in (index - 1) downTo 0) { // Validate all previous pages
                    if (!isValidReport(
                            pageToValidate = report[i],
                            currentPage = report[index],
                            rules = rules)) {
                        isValid = false
                        break
                    }
                }
            }
        }
        return isValid
    }

    private fun isValidReport(pageToValidate: Int, currentPage: Int, rules: Map<Int, List<Int>>): Boolean {
        if (rules.containsKey(currentPage)) {
            if (rules[currentPage]!!.contains(pageToValidate)) {
                return false
            }
        }
        return true
    }

    private fun getMiddlePage(report: List<Int>): Int {
        val middle = report.size / 2
        return report[middle]
    }

    private fun getAllData(): InputData {
        val fileData = FileUtils.readFileAsList("/fifth_input.txt")

        val pattern = Regex("""\d{2}\|\d{2}""")
        val rules = mutableMapOf<Int, List<Int>>()
        val data = mutableListOf<List<Int>>()

        for (item in fileData) {    //Iterate the file data
            val match = pattern.find(item)  // Check if is a rule
            if (match != null) {
                val pages = match.value.split("|")
                val page1 = pages[0].toInt()
                val page2 = pages[1].toInt()
                if (rules.contains(page1)) {
                    (rules[page1] as MutableList).add(page2)
                } else {
                    rules[page1] = mutableListOf(page2)
                }
            } else {
                if (item.isEmpty()) continue else // Blank line
                data.add(item.split(",").map { it.toInt() }) // Reports
            }
        }
        return InputData(rules, data)
    }
}