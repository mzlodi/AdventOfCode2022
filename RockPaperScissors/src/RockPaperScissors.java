import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RockPaperScissors {
    enum Moves {
        ROCK,
        PAPER,
        SCISSORS
    }

    enum Outcome {
        LOSS,
        DRAW,
        WIN
    }

    public static void main(String[] args) throws Exception {
        Boolean isTest = false;
        Boolean isFirstPartOfChallenge = false;
        List<String> inputStrings = getInputString(isTest);
        Integer totalScore = calculatePoints(inputStrings, isFirstPartOfChallenge);

        System.out.println("Total score is " + totalScore);
    }

    private static List<String> getInputString(Boolean isTest) {
        String filePath = isTest == true ? "testInput.txt" : "input.txt";

        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            List<String> inputStrings = new ArrayList<>();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                inputStrings.add(line);
            }

            bufferedReader.close();
            fileReader.close();

            return inputStrings;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read input file: " + e.getMessage());
        }
    }

    private static Map<String, String> mapCharToMove() {
        Map<String, String> charsToGameMove = Map.ofEntries(
                Map.entry("A", Moves.ROCK.toString()),
                Map.entry("B", Moves.PAPER.toString()),
                Map.entry("C", Moves.SCISSORS.toString()),
                Map.entry("X", Moves.ROCK.toString()),
                Map.entry("Y", Moves.PAPER.toString()),
                Map.entry("Z", Moves.SCISSORS.toString()));

        return charsToGameMove;
    }

    private static Map<String, String> mapCharToMoveAndStrat() {
        Map<String, String> charsToGameMove = Map.ofEntries(
                Map.entry("A", Moves.ROCK.toString()),
                Map.entry("B", Moves.PAPER.toString()),
                Map.entry("C", Moves.SCISSORS.toString()),
                Map.entry("X", Outcome.LOSS.toString()),
                Map.entry("Y", Outcome.DRAW.toString()),
                Map.entry("Z", Outcome.WIN.toString()));

        return charsToGameMove;
    }

    private static Integer calculatePoints(List<String> inputStrings, Boolean isFirstPartOfChallenge) {
        Integer pointsTotal = 0;
        Map<String, Integer> pointsToMoves = mapPointsToMoves();
        Map<String, String> charsToGameMove = isFirstPartOfChallenge ? mapCharToMove() : mapCharToMoveAndStrat();

        for (String s : inputStrings) {
            String opponentMove = charsToGameMove.get(String.valueOf(s.charAt(0)));
            String playerMove = charsToGameMove.get(String.valueOf(s.charAt(s.length() - 1)));

            if (isFirstPartOfChallenge) {
                if (isWin(playerMove, opponentMove)) {
                    pointsTotal += pointsToMoves.get(playerMove) + 6;
                } else if (playerMove == opponentMove) {
                    pointsTotal += pointsToMoves.get(playerMove) + 3;
                } else if (isLoss(playerMove, opponentMove)) {
                    pointsTotal += pointsToMoves.get(playerMove);
                }
            } else {
                if (playerMove == Outcome.LOSS.toString()) {
                    if (opponentMove == Moves.ROCK.toString()) {
                        pointsTotal += pointsToMoves.get(Moves.SCISSORS.toString());
                    } else if (opponentMove == Moves.PAPER.toString()) {
                        pointsTotal += pointsToMoves.get(Moves.ROCK.toString());
                    } else if (opponentMove == Moves.SCISSORS.toString()) {
                        pointsTotal += pointsToMoves.get(Moves.PAPER.toString());
                    }
                } else if (playerMove == Outcome.DRAW.toString()) {
                    pointsTotal += pointsToMoves.get(opponentMove) + 3;
                } else if (playerMove == Outcome.WIN.toString()) {
                    if (opponentMove == Moves.ROCK.toString()) {
                        pointsTotal += pointsToMoves.get(Moves.PAPER.toString()) + 6;
                    } else if (opponentMove == Moves.PAPER.toString()) {
                        pointsTotal += pointsToMoves.get(Moves.SCISSORS.toString()) + 6;
                    } else if (opponentMove == Moves.SCISSORS.toString()) {
                        pointsTotal += pointsToMoves.get(Moves.ROCK.toString()) + 6;
                    }
                }
            }
        }

        return pointsTotal;
    }

    private static Map<String, Integer> mapPointsToMoves() {
        Map<String, Integer> pointsToMovesMap = Map.ofEntries(
                Map.entry(Moves.ROCK.toString(), 1),
                Map.entry(Moves.PAPER.toString(), 2),
                Map.entry(Moves.SCISSORS.toString(), 3));

        return pointsToMovesMap;
    }

    private static Boolean isWin(String playerMove, String opponentMove) {
        return (playerMove.equals(Moves.ROCK.toString()) && opponentMove.equals(Moves.SCISSORS.toString())) ||
                (playerMove.equals(Moves.PAPER.toString()) && opponentMove.equals(Moves.ROCK.toString())) ||
                (playerMove.equals(Moves.SCISSORS.toString()) && opponentMove.equals(Moves.PAPER.toString()));
    }

    private static Boolean isLoss(String playerMove, String opponentMove) {
        return (playerMove.equals(Moves.ROCK.toString()) && opponentMove.equals(Moves.PAPER.toString())) ||
                (playerMove.equals(Moves.PAPER.toString()) && opponentMove.equals(Moves.SCISSORS.toString())) ||
                (playerMove.equals(Moves.SCISSORS.toString()) && opponentMove.equals(Moves.ROCK.toString()));
    }
}