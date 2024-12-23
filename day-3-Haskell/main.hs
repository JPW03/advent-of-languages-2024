{-
    ghc version: 9.2.8

    Run the code:
    `>ghci`
    `>:add main.hs`
    `>processInput`
-}
module Day3 where
    import Data.List (stripPrefix)
    import Data.Char (isNumber)
    import Text.Read (readMaybe)

    isPairNumbers :: String -> Bool
    isPairNumbers str
        | ',' `elem` str =
            all isNumber firstString && not (null firstString) &&
            all isNumber secondString && not (null secondString)
        | otherwise = False
        where
            (firstString, ',':secondString) = break (==',') str

    getNumbersIfValidStringPair :: String -> Maybe (Int, Int)
    getNumbersIfValidStringPair str
        | isPairNumbers str = Just (read firstString :: Int, read secondString :: Int)
        | otherwise = Nothing
        where
            (firstString, ',':secondString) = break (==',') str

    areNumbersInRange :: (Int, Int) -> Bool
    areNumbersInRange (x, y) = inRange x && inRange y where inRange = (<1000)

    resolveIfMulStatement :: String -> Int
    resolveIfMulStatement statement =
        case stripPrefix "mul(" statement of
            Just restOfStatement ->
                case getNumbersIfValidStringPair bracketContents of
                    Just (x, y) ->
                        if areNumbersInRange (x, y) then
                            x * y
                        else
                            0
                    Nothing -> 0
                where
                    bracketContents = takeWhile (/=')') restOfStatement
            Nothing -> 0

    -- ENTRY POINT
    resolveInput :: String -> Int
    resolveInput input = recurse (" " ++ input) 0 True
        where
            recurse ('d':'o':'n':'\'':'t':'(':')':input) total _ =
                recurse (')':input) total False
            recurse ('d':'o':'(':')':input) total _ =
                recurse (')':input) total True

            recurse (_:input) total True =
                recurse input (total + resolveIfMulStatement input) True
            recurse (_:input) total False = recurse input total False

            recurse "" total _ = total

    exampleInput :: String
    exampleInput = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"

    exampleInputPart2 :: String
    exampleInputPart2 = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"

    processInputFile :: IO ()
    processInputFile = do
        input <- readFile "input"
        putStrLn "Total:"
        print (resolveInput input)
        where
            processLines [] = 0
            processLines (line:rest) = resolveInput line + processLines rest
