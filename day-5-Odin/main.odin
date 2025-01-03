package main

import "core:fmt"
import "core:strings"
import "core:strconv"
import "core:math"

exampleInput :: `47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47
`

input :: #load("./input", string)

Rule :: struct {
    before: int,
    after: int
}

Update :: [dynamic]int

main :: proc() {
    fmt.println("Hello World!")
    
    rules, updates := parseInput(input)

    total := 0

    for update in updates {
        fmt.println(update)
        valid := is_update_valid(update, rules)
        fmt.println(valid)

        if valid {
            total += get_middle_number(update)
        }
    }
    fmt.print("Total = ")
    fmt.println(total)
}

parseInput :: proc(input: string) -> (rules: [dynamic]Rule, updates: [dynamic]Update) {
    lines := strings.split_lines(input)
    lines = lines[:len(lines) - 1] // Trim empty line at the end
    
    rule_update_separator_index : int
    count := 0
    for line in lines[:len(lines) - 1] {
        if line == "" {
            rule_update_separator_index = count
        }
        count += 1
    }

    rule_strings := lines[:rule_update_separator_index]
    for rule_string in rule_strings {
        number_strings := strings.split(rule_string, "|")
        
        first_number, second_number: int
        ok: bool
        first_number, ok = strconv.parse_int(number_strings[0])
        second_number, ok = strconv.parse_int(number_strings[1])
        
        if !ok {
            panic("At least one of the rules has an invalid string")
        }
        
        rule := Rule{first_number, second_number}
        append(&rules, rule)
    }
    
    update_strings := lines[rule_update_separator_index + 1:]
    for update_string in update_strings {
        number_strings := strings.split(update_string, ",")

        update: Update
        for number_string in number_strings {
            number, ok := strconv.parse_int(number_string)
            if !ok {
                panic("At least one of the updates has an invalid string")
            }
            
            append(&update, number)
        }

        append(&updates, update)
    }

    return
}

is_update_valid :: proc(update: Update, rules: [dynamic]Rule) -> bool {

    relevant_rules := find_relevant_rules(update, rules)
    fmt.println(relevant_rules)

    for page in update {
        // fmt.println(page)
        rules_to_remove : [dynamic]int
        for rule, i in relevant_rules {
            // fmt.println(rule)
            if rule.after == page {
                // fmt.println("Broken")
                return false
            }

            if rule.before == page {
                // fmt.println("Removing")
                append(&rules_to_remove, i)
            }
        }

        for i, count in rules_to_remove {
            ordered_remove(&relevant_rules, i - count)
        }
        // fmt.println(relevant_rules)

        (len(relevant_rules) > 0) or_break
    }

    return true
}

find_relevant_rules :: proc(update: Update, rules: [dynamic]Rule) -> (relevant_rules: [dynamic]Rule) {
    for rule, i in rules {
        found_after, found_before: bool
        for page in update {
            if rule.after == page {
                found_after = true
            }
            else if rule.before == page {
                found_before = true
            }
        }
        if found_after && found_before {
            append(&relevant_rules, rule)
        }
    }
    return
}

get_middle_number :: proc(update: Update) -> int {
    return update[cast(u32)math.floor_f16(cast(f16)(len(update) / 2))]
}
