import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class Proj2 {
    public static void main(String[] args) throws IOException {
        // Use command line arguments to specify the input file
        if (args.length != 2) {
            System.err.println("Usage: java TestAvl <input file> <number of lines>");
            System.exit(1);
        }

        String inputFileName = args[0];
        int numLines = Integer.parseInt(args[1]);

        // For file input
        FileInputStream inputFileNameStream = null;
        Scanner inputFileNameScanner = null;

        // Open the input file
        inputFileNameStream = new FileInputStream(inputFileName);
        inputFileNameScanner = new Scanner(inputFileNameStream);

        // ignore first line
        inputFileNameScanner.nextLine();

        /* Read up to numLines from the file (after header) into dataSet */
        ArrayList<String> dataSet = new ArrayList<>(numLines);
        while (inputFileNameScanner.hasNextLine() && dataSet.size() < numLines) {
            String line = inputFileNameScanner.nextLine().trim();
            if (!line.isEmpty()) {
                dataSet.add(line);
            }
        }
        inputFileNameScanner.close();
        inputFileNameStream.close();

        /* Make array list for sorted and randomized order */
        ArrayList<String> sorted = new ArrayList<>(dataSet);
        Collections.sort(sorted);

        ArrayList<String> randomized = new ArrayList<>(dataSet);
        Collections.shuffle(randomized);

        /* Build four trees BST, AVL for sorted/randomized */
        BST<String> bstSort = new BST<>();
        BST<String> bstRan = new BST<>();
        AvlTree<String> avlSort = new AvlTree<>();
        AvlTree<String> avlRan = new AvlTree<>();

        long start;
        long end;

        /* Insert Time */
        start = System.nanoTime();
        for (int i = 0; i < numLines; i++) {
            bstSort.insert(sorted.get(i));
        }
        end = System.nanoTime();
        long bstSortInsert = end - start;

        start = System.nanoTime();
        for (int i = 0; i < numLines; i++) {
            bstRan.insert(randomized.get(i));
        }
        end = System.nanoTime();
        long bstRanInsert = end - start;

        start = System.nanoTime();
        for (int i = 0; i < numLines; i++) {
            avlSort.insert(sorted.get(i));
        }
        end = System.nanoTime();
        long avlSortInsert = end - start;

        start = System.nanoTime();
        for (int i = 0; i < numLines; i++) {
            avlRan.insert(randomized.get(i));
        }
        end = System.nanoTime();
        long avlRanInsert = end - start;

        /* Search Time */
        start = System.nanoTime();
        for (int i = 0; i < numLines; i++) {
            bstSort.search(dataSet.get(i));
        }
        end = System.nanoTime();
        long bstSortSearch = end - start;

        start = System.nanoTime();
        for (int i = 0; i < numLines; i++) {
            bstRan.search(dataSet.get(i));
        }
        end = System.nanoTime();
        long bstRanSearch = end - start;

        start = System.nanoTime();
        for (int i = 0; i < numLines; i++) {
            avlSort.contains(dataSet.get(i));
        }
        end = System.nanoTime();
        long avlSortSearch = end - start;

        start = System.nanoTime();
        for (int i = 0; i < numLines; i++) {
            avlRan.contains(dataSet.get(i));
        }
        end = System.nanoTime();
        long avlRanSearch = end - start;

        /* Convert nanoseconds → seconds */
        double bstSortInsertSecond = bstSortInsert / 1_000_000_000.0;
        double bstRanInsertSecond = bstRanInsert / 1_000_000_000.0;
        double avlSortInsertSecond = avlSortInsert / 1_000_000_000.0;
        double avlRanInsertSecond = avlRanInsert / 1_000_000_000.0;
        double bstSortSearchSecond = bstSortSearch / 1_000_000_000.0;
        double bstRanSearchSecond = bstRanSearch / 1_000_000_000.0;
        double avlSortSearchSecond = avlSortSearch / 1_000_000_000.0;
        double avlRanSearchSecond = avlRanSearch / 1_000_000_000.0;

        /* Print out results on screen */
        System.out.println("===========Insertion Time===========");
        System.out.printf("Number of lines: %d\n", numLines);
        System.out.printf("Sorted BST: %.6f seconds\n", bstSortInsertSecond);
        System.out.printf("Randomized BST: %.6f seconds\n", bstRanInsertSecond);
        System.out.printf("Sorted AVL: %.6f seconds\n", avlSortInsertSecond);
        System.out.printf("Randomized AVL: %.6f seconds\n", avlRanInsertSecond);
        System.out.println("===========Search Time===========");
        System.out.printf("Number of lines: %d\n", numLines);
        System.out.printf("Sorted BST: %.6f seconds\n", bstSortSearchSecond);
        System.out.printf("Randomized BST: %.6f seconds\n", bstRanSearchSecond);
        System.out.printf("Sorted AVL: %.6f seconds\n", avlSortSearchSecond);
        System.out.printf("Randomized AVL: %.6f seconds\n", avlRanSearchSecond);

        /* Make CSV style output file */
        File out = new File("output.txt");
        boolean writeHeader = !out.exists();

        String header = String.join(",", "N", "bstSortInsertSecond", "bstRanInsertSecond", "avlSortInsertSecond", "avlRanInsertSecond"
        , "bstSortSearchSecond", "bstRanSearchSecond", "avlSortSearchSecond", "avlRanSearchSecond", "bstSortInsertRate", "bstRanInsertRate"
        , "avlSortInsertRate", "avlRanInsertRate");
        String output = String.format("%d,%.9f,%.9f,%.9f,%.9f,%.9f,%.9f,%.9f,%.9f,%.9e,%.9e,%.9e,%.9e,%.9e,%.9e,%.9e,%.9e", numLines
        ,bstSortInsertSecond, bstRanInsertSecond, avlSortInsertSecond, avlRanInsertSecond, bstSortSearchSecond, bstRanSearchSecond
        , avlSortSearchSecond, avlRanSearchSecond, bstSortInsertSecond/numLines, bstRanInsertSecond/numLines, avlSortInsertSecond/numLines,
                avlRanInsertSecond/numLines, bstSortSearchSecond/numLines, bstRanSearchSecond/numLines, avlSortSearchSecond/numLines, avlRanSearchSecond/numLines);

        try (FileWriter writer = new FileWriter("output.txt", true)) {
            if (writeHeader) writer.write(header + "\n");
            writer.write(output + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}