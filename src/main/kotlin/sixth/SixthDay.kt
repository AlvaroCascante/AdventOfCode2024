package sixth

import Solution
import utils.FileUtils

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

enum class FieldOptions(val value: Char) {
    GUARDIAN('^'), EMPTY('.'), WALKED('X'), OBSTACLE('#')
}

class SixthDay: Solution {
    /*
    *              Y
    *              |
    *              |
    * _____________|______________X
    *              |
    *              |
    */
    override fun solve(): Any {
        val dataArray = getDataArray()
        val position = getGuardianPosition(dataArray)

        return doWalk(dataArray, position.first, position.second)
    }

    private fun doWalk(dataArray: Array<CharArray>,
                       gY: Int,
                       gX: Int,
                       isSimulating: Boolean = false): Int  {
        val dataCopy = dataArray.map { it.clone() }.toTypedArray()
        var guardianY = gY
        var guardianX = gX
        var cont = 0
        var dir = Direction.UP
        val walkedData = WalkedData(mutableMapOf())

        while (true) {
            println("Cont: $cont - GuardianT: $guardianY - GuardianX: $guardianX")
            when (dir) {
                Direction.UP -> {
                    if (guardianY == 0) {
                        return if (isSimulating) -1 else cont
                    } else {
                        when (dataArray[guardianY - 1][guardianX]) {
                            FieldOptions.EMPTY.value -> {
                                if (!isSimulating && simulateObstacle(
                                        dataArray = dataCopy,
                                        guardianY = gY,
                                        guardianX = gX,
                                        obstacleX = guardianX,
                                        obstacleY = guardianY - 1)
                                ) {
                                    cont ++
                                }
                                moveGuardian(walkedData, dataArray, guardianY, guardianX, guardianY-1, guardianX, Direction.UP)
                                guardianY--
                            }
                            FieldOptions.WALKED.value -> {
                                if(walkedData.data.contains(Pair(guardianY - 1, guardianX))){
                                    if (walkedData.data[Pair(guardianY - 1, guardianX)]!!.contains(Direction.UP)) {
                                        return 1
                                    } else {
                                        addWalkData(walkedData, guardianY - 1, guardianX, Direction.UP)
                                    }
                                }
                                moveGuardian(walkedData, dataArray, guardianY, guardianX, guardianY-1, guardianX, Direction.UP)
                                guardianY--
                            }
                            FieldOptions.GUARDIAN.value -> {
                                moveGuardian(walkedData, dataArray, guardianY, guardianX, guardianY-1, guardianX, Direction.UP)
                                guardianY--
                            }
                            FieldOptions.OBSTACLE.value -> {
                                dir = Direction.RIGHT
                            }
                        }
                    }
                }

                Direction.DOWN -> {
                    if (guardianY == dataArray.size - 1) {
                        return if (isSimulating) -1 else cont
                    } else {
                        when (dataArray[guardianY + 1][guardianX]) {
                            FieldOptions.EMPTY.value -> {
                                if (!isSimulating && simulateObstacle(
                                        dataArray = dataCopy,
                                        guardianY = gY,
                                        guardianX = gX,
                                        obstacleX = guardianX,
                                        obstacleY = guardianY + 1)
                                    ) {
                                    cont ++
                                }
                                moveGuardian(walkedData, dataArray, guardianY, guardianX, guardianY+1, guardianX, Direction.DOWN)
                                guardianY++
                            }
                            FieldOptions.WALKED.value -> {
                                if(walkedData.data.contains(Pair(guardianY + 1, guardianX))){
                                    if (walkedData.data[Pair(guardianY + 1, guardianX)]!!.contains(Direction.DOWN)) {
                                        return 1
                                    } else {
                                        addWalkData(walkedData, guardianY + 1, guardianX, Direction.DOWN)
                                    }
                                }
                                moveGuardian(walkedData, dataArray, guardianY, guardianX, guardianY+1, guardianX, Direction.DOWN)
                                guardianY++
                            }
                            FieldOptions.GUARDIAN.value -> {
                                moveGuardian(walkedData, dataArray, guardianY, guardianX, guardianY-1, guardianX, Direction.DOWN)
                                guardianY++
                            }
                            FieldOptions.OBSTACLE.value -> {
                                dir = Direction.LEFT
                            }
                        }
                    }
                }

                Direction.LEFT -> {
                    if (guardianX == 0) {
                        return if (isSimulating) -1 else cont
                    } else {
                        when (dataArray[guardianY][guardianX - 1]) {
                            FieldOptions.EMPTY.value -> {
                                if (!isSimulating && simulateObstacle(
                                        dataArray = dataCopy,
                                        guardianY = gY,
                                        guardianX = gX,
                                        obstacleX = guardianX - 1,
                                        obstacleY = guardianY)
                                ) {
                                    cont ++
                                }
                                moveGuardian(walkedData, dataArray, guardianY, guardianX, guardianY, guardianX-1, Direction.LEFT)
                                guardianX--
                            }
                            FieldOptions.WALKED.value -> {
                                if (walkedData.data.contains(Pair(guardianY, guardianX - 1))) {
                                    if (walkedData.data[Pair(guardianY, guardianX - 1)]!!.contains(Direction.LEFT)) {
                                        return 1
                                    } else {
                                        addWalkData(walkedData, guardianY, guardianX - 1, Direction.LEFT)
                                    }
                                }
                                moveGuardian(walkedData, dataArray, guardianY, guardianX, guardianY, guardianX-1, Direction.LEFT)
                                guardianX--
                            }
                            FieldOptions.GUARDIAN.value -> {
                                moveGuardian(walkedData, dataArray, guardianY, guardianX, guardianY-1, guardianX, Direction.LEFT)
                                guardianX--
                            }
                            FieldOptions.OBSTACLE.value -> {
                                dir = Direction.UP
                            }
                        }
                    }
                }

                Direction.RIGHT -> {
                    if (guardianX == dataArray.size - 1) {
                        return if (isSimulating) -1 else cont
                    } else {
                        when (dataArray[guardianY][guardianX + 1]) {
                            FieldOptions.EMPTY.value -> {
                                if (!isSimulating && simulateObstacle(
                                        dataArray = dataCopy,
                                        guardianY = gY,
                                        guardianX = gX,
                                        obstacleX = guardianX + 1,
                                        obstacleY = guardianY
                                    )
                                ) {
                                    cont++
                                }
                                moveGuardian(walkedData, dataArray, guardianY, guardianX, guardianY, guardianX+1, Direction.RIGHT)
                                guardianX++
                            }
                            FieldOptions.WALKED.value -> {
                                if (walkedData.data.contains(Pair(guardianY, guardianX + 1))) {
                                    if (walkedData.data[Pair(
                                            guardianY,
                                            guardianX + 1
                                        )]!!.contains(Direction.RIGHT)
                                    ) {
                                        return 1
                                    } else {
                                        addWalkData(walkedData, guardianY, guardianX + 1, Direction.RIGHT)
                                    }
                                }
                                moveGuardian(walkedData, dataArray, guardianY, guardianX, guardianY, guardianX+1, Direction.RIGHT)
                                guardianX++
                            }
                            FieldOptions.GUARDIAN.value -> {
                                moveGuardian(walkedData, dataArray, guardianY, guardianX, guardianY-1, guardianX, Direction.RIGHT)
                                guardianX++
                            }
                            FieldOptions.OBSTACLE.value -> {
                                dir = Direction.DOWN
                            }
                        }
                    }
                }
            }
        }
    }

    private fun moveGuardian(walkedData: WalkedData, dataArray: Array<CharArray>, guardianY: Int, guardianX: Int, newGuardianY: Int, newGuardianX: Int, direction: Direction) {
        dataArray[guardianY][guardianX] = FieldOptions.WALKED.value
        addWalkData(walkedData, guardianY, guardianX, direction)
        dataArray[newGuardianY][newGuardianX] = FieldOptions.GUARDIAN.value
    }

    private fun simulateObstacle(
        dataArray: Array<CharArray>,
        guardianY: Int,
        guardianX: Int,
        obstacleY: Int,
        obstacleX: Int
    ): Boolean {
        val checkArray = dataArray.map { it.clone() }.toTypedArray()
        checkArray[obstacleY][obstacleX] = FieldOptions.OBSTACLE.value
        val value = doWalk(checkArray, guardianY, guardianX, true)
        return value > 0
    }

    private fun addWalkData(walkData: WalkedData, guardianY: Int, guardianX: Int, direction: Direction) {
        if(walkData.data[Pair(guardianY, guardianX)] == null){
            walkData.data[Pair(guardianY, guardianX)] = mutableListOf()
        }
        walkData.data[Pair(guardianY, guardianX)]?.add(direction)
    }

    private fun getDataArray(): Array<CharArray> {
        val data = FileUtils.readFileAsList("/sixth_input.txt")
        val sizeX = data[0].length
        val sizeY = data.size
        val dataArray = Array(sizeX) { CharArray(sizeY)}
        for (i in data.indices) {
            for (j in data[i].indices) {
                dataArray[i][j] = data[i][j]
            }
        }
        return dataArray
    }

    private fun getGuardianPosition(data: Array<CharArray>): Pair<Int, Int> {
        for (i in data.indices) {
            for (j in data[i].indices) {
                if (data[i][j] == FieldOptions.GUARDIAN.value) {
                    return Pair(i, j)
                }
            }
        }
        return Pair(-1, -1)
    }

    data class WalkedData(
        val data: MutableMap<Pair<Int,Int>, MutableList<Direction>>
    )
}

//1752 To LOW