package processor
import java.util.Scanner
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    while(true)
        {
            println()
            when (menu()) {
                1 -> addTwoMatrices()
                2 -> multiplyByANumber()
                3 -> multiplyMatrices()
                4 -> transposeMatrix()
                5 -> findDeterminant()
                6 -> inverseMatrix()
                7 -> coFactor()
                else  -> return
            }
        }
}

fun menu(): Int {
    println("1. Add matrices\n2. Multiply matrix to a constant\n3. Multiply matrices\n" +
            "4. Transpose matrix\n5. Calculate a determinant\n6. Inverse matrix\n7. Co-factor\n0. Exit")
    print("Your choice: ")
    val scanner = Scanner(System.`in`)
    return scanner.nextLine().toInt()
}

fun coFactor() {
    val coFact = initializeMatrix()
    coFact.fillMatrix()
    println ("Enter the Row and Column of the item you want the co-factor for: ")
    val scanner = Scanner(System.`in`)
    val row = scanner.nextInt()
    val column = scanner.nextInt()
    scanner.nextLine()
    println(coFact.coFactor(row, column, coFact.matrix.toMutableList()))
}

fun inverseMatrix(){
    val matrix = initializeMatrix()
    matrix.fillMatrix()
    println("The result is:")
    if(matrix.determinant(matrix.rows, matrix.columns, matrix.matrix.toMutableList()) == 0.0 ) {
        println("ERROR no inverse available for this matrix")
        return
    }
    if(!isItSquare(matrix)) {
        println("ERROR")
        return
    }
    val determinant =  1 / matrix.determinant(matrix.rows, matrix.columns, matrix.matrix.toMutableList())
    // println("1/determinate = $determinant")
    matrix.inverseMatrix()
    matrix.setRowsAndColumns()
    /*
    println("\nrow1 ${matrix.row[0].joinToString(" ")} \nrow2 ${matrix.row[1].joinToString(" ")} " +
            "\nrow3 ${matrix.row[2].joinToString(" ")} \n\ncol1 ${matrix.column[0].joinToString(" ")} " +
            "\ncol2 ${matrix.column[1].joinToString(" ")} " +
            "\ncol3 ${matrix.column[2].joinToString(" ")}\n")
    */
    matrix.mainDiagonal()
    matrix.multiplyByConstant(determinant)

    matrix.print()
}

fun findDeterminant(){
    val matrix = initializeMatrix()
    matrix.fillMatrix()
    if (!isItSquare(matrix)) {
        println("ERROR the matrix needs to be square")
        return
    }
    println("The result is:")
    println(matrix.determinant(matrix.rows, matrix.rows, matrix.matrix.toMutableList()))
}

fun initializeMatrix(): Matrix{
    val scanner = Scanner(System.`in`)
    print("Enter matrix size: ")
    val rows = scanner.nextInt()
    val columns = scanner.nextInt()
    scanner.nextLine()
    println("Enter matrix:")
    return Matrix(rows, columns)
}

fun transposeMatrix() {
    print("1. Main diagonal\n2. Side diagonal\n3. Vertical line\n4. Horizontal line\nYour choice: ")
    val scanner = Scanner(System.`in`)
    val choice = scanner.nextLine().toInt()
    val transform = initializeMatrix()
    transform.fillMatrix()
    when (choice) {
        1 -> transform.mainDiagonal()
        2 -> transform.sideDiagonal()
        3 -> transform.verticalLine()
        4 -> transform.horizontalLine()
        else -> return
    }
    println("The result is:")
    transform.print()
}

fun multiplyMatrices() {
    val a = initializeMatrix()
    a.fillMatrix()
    val b = initializeMatrix()
    b.fillMatrix()

    if (!multiplying(a, b)) {
        println("ERROR")
        return
    }

    println("The multiplication result is:")
    val multiplied = multiplyByAMatrix(a, b)
    multiplied.print()
}

fun multiplying(m1: Matrix, m2: Matrix): Boolean {
    return m1.columns == m2.rows
}

fun multiplyByAMatrix(m1: Matrix, m2: Matrix): Matrix {
    val result = Matrix(m1.rows, m2.columns)
    var counter = 0
    for (j in m1.row.indices) {
        for (k in m2.column.indices) {
            result.matrix[counter] = rowByColumn(m1.row[j], m2.column[k])
            counter++
        }
    }
    return result
}

fun rowByColumn(row: DoubleArray, column: DoubleArray): Double {
    var dotProduct = 0.0
    for (i in row.indices) {
        dotProduct += row[i] * column[i]
    }
    return dotProduct
}

fun multiplyByANumber(){
    val multiplied = initializeMatrix()
    multiplied.fillMatrix()

    print("Enter constant to multiply by: ")
    val scanner = Scanner(System.`in`)
    val constant = scanner.next().toDouble()
    scanner.nextLine()

    multiplied.multiplyByConstant(constant)
    println("The multiplication result is:")
    multiplied.print()
}

fun addTwoMatrices() {
    val matrix1 = initializeMatrix()
    matrix1.fillMatrix()

    val matrix2 = initializeMatrix()
    matrix2.fillMatrix()

    println("The multiplication result is:")
    addMatrices(matrix1, matrix2)
}

fun addMatrices(m1: Matrix, m2: Matrix) {
    if(sameSize(m1, m2)) {
        val added = add(m1, m2)
        added.print()
    }
}

fun sameSize(m1: Matrix, m2: Matrix): Boolean {
    if (m1.rows != m2.rows || m1.columns != m2.columns) {
        println("ERROR")
        return false
    }
    return true
}

fun add(m1: Matrix, m2: Matrix) : Matrix {
    val added = Matrix(m1.rows, m1.columns)
    for (i in added.matrix.indices) {
        added.matrix[i] = m1.matrix[i] + m2.matrix[i]
    }
    return added
}

fun isItSquare(m: Matrix): Boolean {
    return m.rows == m.columns
}

// Matrix Class ************************************************
class Matrix (val rows: Int, val columns: Int){
    private val size = rows * columns
    var matrix = DoubleArray(size) {0.0}
    val row = mutableListOf<DoubleArray>()
    val column = mutableListOf<DoubleArray>()

    fun mainDiagonal(){
        if(rows != columns) {
            println("ERROR")
            return
        }
        var counter = 0
        val array = DoubleArray(size) {0.0}
        repeat(rows) {
            for (element in column[it]) {
                array[counter] = element
                counter++
            }
        }
        for (i in matrix.indices) {
            matrix[i] = array[i]
        }
    }

    fun sideDiagonal(){
        if(rows != columns) {
            println("ERROR")
            return
        }
        var counter = 0
        val array = DoubleArray(size) {0.0}
        for (i in columns - 1 downTo 0) {
            for (j in column[i].size - 1 downTo 0) {
                array[counter] = column[i][j]
                counter++
            }
        }
        for (i in matrix.indices) {
            matrix[i] = array[i]
        }
    }

    fun verticalLine(){
        var counter = 0
        val array = DoubleArray(size) {0.0}
        for (i in row.indices) {
            for (j in row[i].size -1 downTo 0) {
                array[counter] = row[i][j]
                counter++
            }
        }
        for (i in matrix.indices) {
            matrix[i] = array[i]
        }
    }

    fun horizontalLine(){
        var counter = 0
        val array = DoubleArray(size) {0.0}
        for (i in rows - 1 downTo 0) {
            for (element in row[i]){
                array[counter] = element
                counter++
            }
        }
        for (i in matrix.indices) {
            matrix[i] = array[i]
        }
    }

    fun multiplyByConstant(constant: Double) {
        for (i in matrix.indices) {
            matrix[i] = matrix[i] * constant
        }
    }

    fun print() {
        var count = 0
        repeat(rows) {
            repeat(columns) {
                print(String.format("%.2f", matrix[count]))
                print(' ')
                count++
            }
            println()
        }
    }

    fun fillMatrix() {
        var counter = 0
        repeat(rows) {
            val input = getARow(columns)
            row.add(it, input)
            for (element in input) {
                matrix[counter] = element
                counter++
            }
        }
        repeat(columns) {
            val array = DoubleArray(rows){0.0}
            for (i in row.indices) {
                array[i] = row[i][it]
            }
            column.add(it, array)
        }
    }

    private fun getARow(columns: Int): DoubleArray {
        val scanner = Scanner(System.`in`)
        val input = scanner.nextLine().split(' ')
        val row = DoubleArray(columns){0.0}
        for (i in row.indices) {
            row[i] = input[i].toDouble()
        }
        return row
    }

    fun determinant(rows: Int, columns: Int, list: MutableList<Double>): Double {
        var det = 0.0
        if (rows == 2 && columns == 2 && list.size == 4){
            det = list[0] * list[3] - list[1] * list[2]
        } else {
            val tempList = list.drop(columns)
            val tempListList = tempList.chunked(columns)
            for ((colCounter) in (0 until columns).withIndex()){
                val num = list[colCounter]
                //println()
                //println("colCounter $colCounter")
                val sign = (-1.0).pow(1.0 + colCounter + 1)
                val reciprocal = MutableList(0){0.0}
                for ((rowCount) in (tempListList.indices).withIndex()){
                    //println("col $colCounter rowCount $rowCount")
                    for(itemCount in (tempListList[0].indices)){
                        if (itemCount != colCounter){
                             // println("col $colCounter row $rowCount item $itemCount")
                            reciprocal.add(tempListList[rowCount][itemCount])
                        }
                    }
                }
                // println(reciprocal.joinToString(","))
                det += num * (sign * determinant(rows - 1, columns - 1, reciprocal))
            }
        }
        return det
    }

    fun coFactor(row: Int, column: Int, list: MutableList<Double>): Double {
        var cf = 4.2
        val rows = sqrt(list.size.toDouble()).toInt()
        val columns = rows
        val sign = (-1.0).pow(row.toDouble() + column)


        if (rows == 2 && columns == 2) {

            cf = if(row == 1 && column == 1) {
                sign * list[3]
            } else if (row == 1 && column == 2){
                sign * list[2]
            } else if (row == 2 && column == 1){
                sign * list[1]
            } else {
                sign * list[0]
            }
        } else {
            val tempListList = list.chunked(columns)
            for (c in (0 until columns).withIndex()){
                val reciprocal = MutableList(0){0.0}
                for ((rowCount) in (tempListList.indices).withIndex()){
                    if(rowCount != row - 1){
                        for(itemCount in (tempListList[0].indices)){
                            if (itemCount != column - 1){
                                reciprocal.add(tempListList[rowCount][itemCount])
                            }
                        }
                    }
                }
                cf =  sign * determinant(rows - 1, columns - 1, reciprocal)
            }
        }
        return cf
    }

    fun inverseMatrix(){
        var counter = 0
        val array = DoubleArray(size){0.0}
        for (i in 1..rows){
            for(j in 1..columns){
                array[counter] = coFactor(i, j, matrix.toMutableList())
                counter++
            }
        }
        for (i in matrix.indices) {
            matrix[i] = array[i]
        }
    }

    fun setRowsAndColumns(){
        val list = matrix.toList().chunked(columns)
        for(i in list.indices) {
            row[i] = list[i].toDoubleArray()
        }

        repeat(columns) {
            val array = DoubleArray(rows){0.0}
            for (i in row.indices) {
                array[i] = row[i][it]
            }
            column.add(it, array)
        }
    }
}


