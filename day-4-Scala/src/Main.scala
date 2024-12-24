
lazy val exampleInput = """MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX
"""

object Main extends App {
  println("Hello, World!")

  val wordSearchGrid = parseInputString(exampleInput)

  println("Word search grid:")
  for (line <- wordSearchGrid) {
    for (character <- line) {
      print(character)
    }
    println()
  }

  findXmas(wordSearchGrid)
}

def parseInputString(input: String): Array[Array[Char]] = {
  var lines: Array[String] = input.split("\n")
  return (for (line <- lines) yield line.toCharArray().dropRight(1)).toArray()
  // ^ dropRight to remove the end line character
}


def findXmas(wordSearchGrid: Array[Array[Char]]): Unit = {
  
  val lineLength = wordSearchGrid(0).length
  val searchTerm = "XMAS".toCharArray()
  var totalXmases = 0

  for (i <- 0 until wordSearchGrid.length) {
    for (j <- 0 until lineLength) {
  // for (i <- 0 until 1; j <- 0 until lineLength) {
      println(s"Checking $i $j : ${wordSearchGrid(i)(j)}")
      if (wordSearchGrid(i)(j) == 'X') {
        if (j + 3 < lineLength) {
          if (checkForWordInGrid(
            searchTerm,
            wordSearchGrid,
            right,
            i,
            j
          )) totalXmases += 1
        }
      }
      println(s"XMAS's so far: $totalXmases")
    }
  }

  println(s"Total 'XMAS': $totalXmases")
}

class Direction(var x: Int, var y: Int) {
  def +(that: Direction) = new Direction(this.x + that.x, this.y + that.y)
}

val right = new Direction(1, 0)
val left = new Direction(-1, 0)
val up = new Direction(0, 1)
val down = new Direction(0, -1)

def checkForWordInGrid(
  searchTerm: Array[Char],
  grid: Array[Array[Char]],
  direction: Direction,
  originY: Int,
  originX: Int
): Boolean = {
  var found = true
  for (n <- 0 until searchTerm.length) {
    found = grid(originY + direction.y*n)(originX + direction.x*n) == searchTerm(n)
  }
  return found;
}