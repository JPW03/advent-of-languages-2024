
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

lazy val input = scala.io.Source.fromFile("src/input").mkString

object Main extends App {
  println("Hello, World!")

  val wordSearchGrid = multiLineStringTo2dArray(input)

  println("Word search grid:")
  print2dCharArray(wordSearchGrid)

  findXmas(wordSearchGrid)
}

def print2dCharArray(charArray: Array[Array[Char]]): Unit = {
  for (line <- charArray) {
    for (character <- line) {
      print(character)
    }
    println()
  }
}

def multiLineStringTo2dArray(input: String): Array[Array[Char]] = {
  var lines: Array[String] = input.split("\n")
  return (for (line <- lines) yield line.toCharArray()).toArray()
}


def findXmas(wordSearchGrid: Array[Array[Char]]): Unit = {
  
  var totalXmases = 0

  for (i <- 0 until wordSearchGrid.length; j <- 0 until wordSearchGrid(0).length) {
    // totalXmases += checkXmasPart1(wordSearchGrid, i, j)
    totalXmases += checkXmasPart2(wordSearchGrid, i, j)
  }

  println(s"Total 'XMAS': $totalXmases")
}

/// PART 2

def checkXmasPart2(
  wordSearchGrid: Array[Array[Char]],
  i: Int,
  j: Int,
): Int = {
  val searchTerm = "MAS".toCharArray()

  val xmasFits = i+1 < wordSearchGrid.length &&
    i-1 >= 0 &&
    j+1 < wordSearchGrid(0).length &&
    j-1 >= 0

  if (wordSearchGrid(i)(j) == 'A' && xmasFits) {

    var isXmasTopLeftToBottomRight =
      checkForWordInGrid(searchTerm, wordSearchGrid, right + down, i-1, j-1) ||
      checkForWordInGrid(searchTerm, wordSearchGrid, left + up, i+1, j+1)
    
    var isXmasBottomLeftToTopRight =
      checkForWordInGrid(searchTerm, wordSearchGrid, right + up, i+1, j-1) ||
      checkForWordInGrid(searchTerm, wordSearchGrid, left + down, i-1, j+1)

    if (isXmasBottomLeftToTopRight && isXmasTopLeftToBottomRight) return 1
  }
  return 0
}


/// PART 1

def checkXmasPart1(
  wordSearchGrid: Array[Array[Char]],
  i: Int,
  j: Int,
): Int = {
  val searchTerm = "XMAS".toCharArray()
  var xmasesFound = 0

  if (wordSearchGrid(i)(j) == 'X') {

    val xmasFitsRight = j+3 < wordSearchGrid(0).length
    val xmasFitsLeft = j-3 >= 0
    val xmasFitsDown = i+3 < wordSearchGrid.length
    val xmasFitsUp = i-3 >= 0

    val conditionToDirection: Array[(Boolean, Direction)] = Array(
      (xmasFitsRight, right),
      (xmasFitsLeft, left),
      (xmasFitsUp, up),
      (xmasFitsDown, down),
      (xmasFitsUp && xmasFitsRight, up + right),
      (xmasFitsUp && xmasFitsLeft, up + left),
      (xmasFitsDown && xmasFitsRight, down + right),
      (xmasFitsDown && xmasFitsLeft, down + left)
    )

    for ((condition, direction) <- conditionToDirection) {
      if (condition) {
        if (checkForWordInGrid(
          searchTerm,
          wordSearchGrid,
          direction,
          i,
          j
        )) {
          println(s"Found XMAS in direction ${direction.x},${direction.y} at $i,$j")
          xmasesFound += 1
        }
      }
    }
  }
  return xmasesFound
}


/// BOTH PARTS

class Direction(var x: Int, var y: Int) {
  def +(that: Direction) = new Direction(this.x + that.x, this.y + that.y)
}

val right = new Direction(1, 0)
val left = new Direction(-1, 0)
val up = new Direction(0, -1)
val down = new Direction(0, 1)

def checkForWordInGrid(
  searchTerm: Array[Char],
  grid: Array[Array[Char]],
  direction: Direction,
  originY: Int,
  originX: Int
): Boolean = {
  var found = true
  for (n <- 0 until searchTerm.length) {
    found = found && (grid(originY + direction.y*n)(originX + direction.x*n) == searchTerm(n))
  }
  return found;
}