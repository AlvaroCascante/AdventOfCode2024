package utils

object FileUtils {
    fun readFileAsString(fileName: String): String {
        return this::class.java.getResource(fileName)?.readText() ?: ""
    }


    fun readFileAsList(fileName: String): List<String> {
        return this::class.java.getResource(fileName)?.readText()?.lines() ?: emptyList()
    }
}