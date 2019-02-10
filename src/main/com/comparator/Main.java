package main.com.comparator;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        FileHandler handler = new FileHandler();

        handler.listFilesInFolder();
        handler.parseFilesContent();
        handler.evaluateFoundFilesSimilarity();
        handler.findMostSimilarFiles(3);
        handler.printFullComparisonStatistic(true);
    }
}
