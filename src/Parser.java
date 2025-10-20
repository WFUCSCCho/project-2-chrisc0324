/**
 * @file: Parser.java
 * @description: This class defines the Parser class that reads the input file, process the inputs, and writes result
 * to an output file called "result.txt"
 * @author: Chris Cha {@literal <chah22@wfu.edu>}
 * @date: September 17, 2025 (Modified September 24, 2025)
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class Parser {

    //Create a BST tree of Integer type
    private BST<F1> mybst = new BST<>();
    private String dataname;

    /* processes the given input file */
    public Parser(String filename, String dataname) throws FileNotFoundException {
        this.dataname = dataname;
        process(new File(filename));
    }

    /* reads each input line and cleans up spaces */
    public void process(File input) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(input)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String editedLine = line.trim().replaceAll("\\s+", " ");
                if (editedLine.isEmpty()) {
                    continue;
                }
                String[] command = editedLine.split(" ");
                operate_BST(command);
            }
        }
    }

    /* Searches the dataset file for a driver by name and constructs an F1 object containing driver's information. */
    public F1 findDriver(String driver1) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(this.dataname))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] command = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                String driver = command[0].trim();
                if (!driver.equalsIgnoreCase(driver1)) {
                    continue;
                }
                String nationality = command[1].trim();
                String seasons = command[2].trim();
                if (seasons.startsWith("\"") && seasons.endsWith("\"")) {
                    seasons = seasons.substring(1, seasons.length() - 1);
                }
                seasons = seasons.trim();
                if (seasons.startsWith("[") && seasons.endsWith("]")) {
                    seasons = seasons.substring(1, seasons.length() - 1);
                }
                String[] seasonsArray = seasons.split("\\s*,\\s*");
                int championships = Integer.parseInt(command[3].trim());
                int raceWins = Integer.parseInt(command[4].trim());
                int podiums = Integer.parseInt(command[5].trim());
                double points = Double.parseDouble(command[6].trim());

                return new F1(driver, nationality, seasonsArray, championships, raceWins, podiums, points);
            }
        }
        return null;
    }

    /* Determines the command and calls the corresponding operations */
    public void operate_BST(String[] command) throws FileNotFoundException {
        switch (command[0]) {
            case "insert" -> {
                if (command.length < 2) {
                    writeToFile("Invalid Command", "./result.txt");
                    return;
                }
                try {
                    String name = String.join(" ", java.util.Arrays.copyOfRange(command, 1, command.length)).trim();
                    F1 match = findDriver(name);
                    if (match == null) {
                        writeToFile("insert failed", "./result.txt");
                    } else {
                        mybst.insert(match);
                        writeToFile("insert " + name, "./result.txt");
                    }
                }
                catch (NumberFormatException e) {
                    writeToFile("Invalid Command", "./result.txt");
                }
            }
            case "search" -> {
                if (command.length < 2) {
                    writeToFile("Invalid Command", "./result.txt");
                    return;
                }
                try {
                    String driver = String.join(" ", java.util.Arrays.copyOfRange(command, 1, command.length)).trim();
                    F1 match = null;
                    for (Iterator<F1> iterator = mybst.iterator(); iterator.hasNext();) {
                        F1 current = iterator.next();
                        if (current.getDriver() != null && current.getDriver().equalsIgnoreCase(driver)) {
                            match = current;
                            break;
                        }
                    }
                    if (match == null) {
                        writeToFile("search failed", "./result.txt");
                    }
                    else {
                        writeToFile("found " + driver, "./result.txt");
                    }
                }
                catch (NumberFormatException e) {
                    writeToFile("Invalid Command", "./result.txt");
                }
            }
            case "remove" -> {
                if (command.length < 2) {
                    writeToFile("Invalid Command", "./result.txt");
                    return;
                }
                try {
                    String driver = String.join(" ", java.util.Arrays.copyOfRange(command, 1, command.length)).trim();
                    F1 match = null;
                    for (Iterator<F1> iterator = mybst.iterator(); iterator.hasNext();) {
                        F1 current = iterator.next();
                        if (current.getDriver() != null && current.getDriver().equalsIgnoreCase(driver)) {
                            match = current;
                            break;
                        }
                    }
                    if (match == null) {
                        writeToFile("remove failed", "./result.txt");
                        return;
                    }
                    Node<F1> removed = mybst.remove(match);
                    if (removed == null) {
                        writeToFile("remove failed", "./result.txt");
                    }
                    else {
                        writeToFile("removed " + driver, "./result.txt");
                    }
                }
                catch (NumberFormatException e) {
                    writeToFile("Invalid Command", "./result.txt");
                }
            }
            case "print" -> {
                if (command.length != 1) {
                    writeToFile("Invalid Command", "./result.txt");
                    return;
                }
                try {
                    StringBuilder output = new StringBuilder();
                    boolean first = true;
                    for (F1 f1 : mybst) {
                        if (!first) {
                            output.append(" ");
                        }
                        output.append(f1.getDriver());
                        first = false;
                    }
                    writeToFile(output.toString(), "./result.txt");
                }
                catch (Exception e) {
                    writeToFile("Invalid Command", "./result.txt");
                }
            }
            default -> writeToFile("Invalid Command", "./result.txt");
        }
    }

    /* writes results to output file */
    public void writeToFile(String content, String filePath) {
        try (FileWriter writer = new FileWriter(filePath,true)) {
            writer.write(content + "\n");
        }
        catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}