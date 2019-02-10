package main.com.comparator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.toMap;

class FileHandler {

    private Map<String, List<String>> fileContent;
    private String[] filesList;
    private Map<String[], Integer> matchResult = new HashMap<>();
    private Map<String[], Integer> sortedResult = new HashMap<>();
    private String documentsPath = System.getProperty("user.dir") + File.separator + File.separator + "src" +
            File.separator + "main" + File.separator + "testFiles" + File.separator;

    private List<String> readFileRows(String fileName) throws IOException {
        List<String> readFileContent = new ArrayList<>();
        File file = new File(documentsPath + fileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        while ((line = bufferedReader.readLine()) != null){
            readFileContent.add(line);
        }
        bufferedReader.close();
        return readFileContent;
    }

    private int evaluateFilesDifference(Map.Entry<String, List<String>> firstFile, Map.Entry<String, List<String>> secondFile) {
        List<String> firstFileContent = firstFile.getValue();
        List<String> secondFileContent = secondFile.getValue();

        Collection<String> similar = new HashSet<>(firstFileContent);
        similar.retainAll(secondFileContent);

        int equalItemsQty = similar.size();

        int biggestFileSize;
        if (firstFileContent.size() > secondFileContent.size()) {
            biggestFileSize = firstFileContent.size();
        } else {
            biggestFileSize = secondFileContent.size();
        }
        return equalItemsQty * 100 / biggestFileSize;
    }

    void listFilesInFolder() {
        File directory = new File(this.documentsPath);
        this.filesList = directory.list();
    }

    private void sortResultsDesc() {
        this.sortedResult = this.matchResult
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
    }

    void parseFilesContent() throws IOException {
        this.fileContent = new HashMap<>();

        for (String fileName : this.filesList){
            this.fileContent.put(fileName, this.readFileRows(fileName));
        }
    }

    /**
     * Preform sorting all file combinations in desc. order and take first number of file names from the list.
     * Quantity of files to be found is custom and can be changed in parameter.
     * **/
    void findMostSimilarFiles(int quantityToFind) {
        this.sortResultsDesc();

        List<String> mostSimilarFiles =new ArrayList<>();

        System.out.printf("Top %d most similar files:\n", quantityToFind);
        int count = 0;
        for (Map.Entry<String[], Integer> pairStatistic : this.sortedResult.entrySet()) {
            if (count >= quantityToFind) break;
            String[] pairFiles = pairStatistic.getKey();
            mostSimilarFiles.addAll(Arrays.asList(pairFiles));
            count++;
        }

        Set<String> uniqueFileNames = new LinkedHashSet<>(mostSimilarFiles);
        int i = 0;
        for (String name : uniqueFileNames) {
            if (i < quantityToFind) {
                System.out.println(name);
            } else {
                break;
            }
            i++;
        }
    }

    void evaluateFoundFilesSimilarity() {
        for (Map.Entry<String, List<String>> firstFile : this.fileContent.entrySet()) {
            for (Map.Entry<String, List<String>> secondFile : this.fileContent.entrySet()) {
                String firstName = firstFile.getKey();
                String secondName = secondFile.getKey();

                if (!firstName.equals(secondName)) {
                    boolean allowAddingFilesPair = false;
                    int matchPercent = this.evaluateFilesDifference(firstFile, secondFile);
                    String[] filesPair = {firstName, secondName};

                    if (this.matchResult.size() == 0) {
                        allowAddingFilesPair = true;
                    } else {
                        for (String[] keySet : this.matchResult.keySet()) {
                            Collection<String> filesSet = new HashSet<>(Arrays.asList(filesPair));
                            filesSet.retainAll(Arrays.asList(keySet));
                            if (filesSet.size() == filesPair.length) {
                                allowAddingFilesPair = false;
                                break;
                            } else {
                                allowAddingFilesPair = true;
                            }
                        }
                    }
                    if (allowAddingFilesPair) {
                        this.matchResult.put(filesPair, matchPercent);
                    }
                }
            }
        }
    }

    /**
     * This method prints the list of all compared files combinations.
     * @param isSorted allows to choose printing sorted by percentage in decs. order list or not.
     * **/
    void printFullComparisonStatistic(boolean isSorted) {
        System.out.println("Full comparison statistic:");
        Map<String[], Integer> result;
        if (isSorted) {
            result = this.sortedResult;
        } else {
            result = this.matchResult;
        }
        for (Map.Entry<String[], Integer> comparisonResult : result.entrySet())
            System.out.printf("%s: %d%%\n", Arrays.toString(comparisonResult.getKey()), comparisonResult.getValue());
    }
}
