// First time using Go
// Version: 1.23.4
package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"strconv"
	"strings"
)

const (
	minimumDifference = 1
	maximumDifference = 3
)
// ^ more languages should do this

func assertNoError(err error) {
	if err != nil {
		panic(err)
	}
}

func isSafe(level []int) (bool, string) {
	var increasing bool = false
	var decreasing bool = false

	for i := 1; i < len(level); i++ {
		previous := level[i - 1]
		current := level[i]

		difference := math.Abs(float64(previous - current))

		if !(difference >= minimumDifference && difference <= maximumDifference) {
			return false, fmt.Sprintf("Invalid difference of %v", difference)
		}

		if previous < current {
			increasing = true
		} else if previous > current {
			decreasing = true
		}

		if increasing && decreasing {
			return false, "Both increases and decreases"
		}
	}

	return true, "Safe!"
}

func readReport(reportChannel chan []int) {
	
	inputFile, err := os.Open("./input")
	assertNoError(err)

	scanner := bufio.NewScanner(inputFile)

	for scanner.Scan() {
		line := scanner.Text()
		tokens := strings.Split(line, " ")
		
		var level []int
		for _, token := range tokens {
			number, err := strconv.Atoi(token)
			assertNoError(err)

			level = append(level, number)
		}

		reportChannel <- level
	}

	close(reportChannel)
}

func main()  {
	
	fmt.Println("Hello World")

	// Example data
	// report := [][]int{
	// 	{7, 6, 4, 2, 1},
	// 	{1, 2, 7, 8, 9},
	// 	{9, 7, 6, 2, 1},
	// 	{1, 3, 2, 4, 5},
	// 	{8, 6, 4, 4, 1},
	// 	{1, 3, 6, 7, 9},
	// }

	report := make(chan []int)

	go readReport(report)

	totalSafe := 0
	numberOfLevels := 0
	for {
		level, ok := <-report

		if !ok { // if channel closes
			break
		}

		if level != nil {
			numberOfLevels++
	
			safe, message := isSafe(level)
			fmt.Printf("Level (%v) %v - Safe=%v, %v\n", numberOfLevels, level, safe, message)
			if safe {
				totalSafe++
			}
		}
	}

	fmt.Println("Total number of safe levels:")
	fmt.Println(totalSafe)

}

