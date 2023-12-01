import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CalorieCounting {
    public static void main(String[] args) throws Exception {
        Boolean isTest = false;
        List<String> inputStrings = getInputString(isTest);
        Map<String, List<Integer>> caloriesToElvesMap = mapCalorieItemsToElf(inputStrings);
        
        Map<String, Integer> elfWithMostCalories = getElfCarryingMostCalories(caloriesToElvesMap);
        System.out.println("PART 1 | Elf with most calories: " + elfWithMostCalories);

        Map<String, Integer> topThreeElvesWithMostCalories = getTopThreeElvesCarryingMostCalories(caloriesToElvesMap);
        Integer sumOfTopThreeElvesCalories = getSumOfTopThreeCalories(topThreeElvesWithMostCalories);
        System.out.println("PART 2 | Sum of top three elves with most calories: " + sumOfTopThreeElvesCalories);
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

    private static Map<String, List<Integer>> mapCalorieItemsToElf(List<String> inputStrings) {
        Map<String, List<Integer>> caloriesToElvesMap = new HashMap<>();

        Integer iterations = 1;

        List<Integer> caloriesList = new ArrayList<>();

        for (String s : inputStrings) {
            String nameOfElf = "Elf No. " + String.valueOf(iterations);
            if (!s.isEmpty()) {
                caloriesList.add(Integer.parseInt(s));
            } else {
                iterations++;
                caloriesList = new ArrayList<>();
                continue;
            }
            caloriesToElvesMap.put(nameOfElf, caloriesList);
        }

        return caloriesToElvesMap;
    }

    private static Map<String, Integer> getElfCarryingMostCalories(Map<String, List<Integer>> caloriesToElvesMap) {
        Map<String, Integer> elfWithMostCalories = new HashMap<>();
        Integer highestSum = 0;
        String elfNameMostKcal = new String();

        for (Map.Entry<String, List<Integer>> entry : caloriesToElvesMap.entrySet()) {
            Integer sumOfCalories = 0;
            String elfName = entry.getKey();
            List<Integer> elfCalories = entry.getValue();

            for (Integer calories : elfCalories) {
                sumOfCalories += calories;
            }

            if (sumOfCalories > highestSum) {
                highestSum = sumOfCalories;
                elfNameMostKcal = elfName;
            }
        }

        elfWithMostCalories.put(elfNameMostKcal, highestSum);

        return elfWithMostCalories;
    }

    private static Map<String, Integer> getTopThreeElvesCarryingMostCalories(Map<String, List<Integer>> caloriesToElvesMap){
        Map<String, Integer> topThreeElvesKcal = new HashMap<>();
        Map<String, Integer> elvesWithSumOfCalories = new HashMap<>();

        for (Map.Entry<String, List<Integer>> entry : caloriesToElvesMap.entrySet()) {
            Integer sumOfCalories = 0;
            String elfName = entry.getKey();
            List<Integer> elfCalories = entry.getValue();

            for (Integer calories : elfCalories) {
                sumOfCalories += calories;
            }

            elvesWithSumOfCalories.put(elfName, sumOfCalories);
        }
        
        topThreeElvesKcal = sortByValue(elvesWithSumOfCalories, Comparator.reverseOrder());

        return topThreeElvesKcal;
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, Comparator<? super V> comparator) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(comparator))
                .limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private static Integer getSumOfTopThreeCalories(Map<String, Integer> topThreeElvesWithMostCalories){
        Integer totalSumOfCalories = 0;

        for (Map.Entry<String, Integer> entry : topThreeElvesWithMostCalories.entrySet()) {
            totalSumOfCalories += entry.getValue();
        }

        return totalSumOfCalories;
    }
}