import java.io.*;
import java.util.*;

public class Main {
    private int k;
    private List<Entry> trainingSet;

    public Main(int k) {
        this.k = k;
        trainingSet = new ArrayList<>();
    }

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        String train_Set = args[1];
        String test_Set = args[2];

        System.out.print("Do you wish to insert your vector to predict its class? (y/n): ");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            handleNewVector(k, train_Set);

        } else if (choice.equalsIgnoreCase("n")) {
            System.out.println("Continuing...");
            Main main = new Main(k);
            main.readData(train_Set);

            List<double[]> testVectors = new ArrayList<>();
            List<String> trueClasses = new ArrayList<>();
            readTestSet(test_Set, testVectors, trueClasses);

                double accuracy = main.accuracy(testVectors, trueClasses);
            System.out.println("Accuracy of this model is equal to: " + accuracy + "\n");
        } else {
            System.out.println("Invalid input");
        }
    }

    static class Entry {
        double[] attributes;
        String name;

        public Entry(double[] attributes, String name) {
            this.attributes = attributes;
            this.name = name;
        }
    }

    class ProcessedEntry {
        private double distance;
        private String name;

        public ProcessedEntry(double dist, String name) {
            this.distance = dist;
            this.name = name;
        }
        public double getDistance() {
            return distance;
        }

        public String getName() {
            return name;
        }
    }

    public static double euclideanDistance(double[] x, double[] y) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow(x[i] - y[i], 2);
        }
        return Math.sqrt(sum);
    }

    public void readData(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                double[] values = new double[data.length - 1];
                for (int i = 0; i < data.length - 1; i++) {
                    values[i] = Double.parseDouble(data[i]);
                }
                trainingSet.add(new Entry(values, data[data.length - 1]));
            }
        } catch (IOException e) {
            System.out.println("Problem reading " + filename + " file");
        }
    }

    public String classify(double[] testData) {
        Map<String, Integer> map = new HashMap<>();
        List<ProcessedEntry> distances = new ArrayList<>();

        for (Entry trainEntry : trainingSet) {
            double dist = euclideanDistance(testData, trainEntry.attributes);
            distances.add(new ProcessedEntry(dist, trainEntry.name));
        }
        distances.sort(Comparator.comparingDouble(ProcessedEntry::getDistance));

        for (int i = 0; i < k; i++) {
            String name = distances.get(i).getName();
            map.put(name, map.getOrDefault(name, 0) + 1);
        }

        String prediction = null;
        int max = -1;

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                prediction = entry.getKey();
            }
        }
        return prediction;
    }

    public double accuracy(List<double[]> testVectors, List<String> trueClasses) {
        int correct = 0;
        int total = testVectors.size();

        for (int i = 0; i < total; i++) {
            double[] testVec = testVectors.get(i);
            String trueClass = trueClasses.get(i);
            String result = classify(testVec);

            System.out.println("Predicted class: " + result);
            System.out.println("True class: " + trueClass);

            if (result.equals(trueClass)) {
                correct++;
            }
        }
        return (double) correct / total;
    }

    private static void handleNewVector(int k, String train_Set) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the vector (format: x.x,y.y,z.z,w.w): ");
        String vector = scanner.nextLine();

        String[] vectorParts = vector.split(",");
        double[] newVector = new double[vectorParts.length];

        for (int i = 0; i < vectorParts.length; i++) {
            newVector[i] = Double.parseDouble(vectorParts[i]);
        }

        Main main = new Main(k);
        main.readData(train_Set);
        String result = main.classify(newVector);
        System.out.println("Predicted class: " + result);
    }

    private static void readTestSet(String test_Set, List<double[]> testVectors, List<String> trueClasses) {
        try (BufferedReader reader = new BufferedReader(new FileReader(test_Set))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                double[] testVec = new double[data.length - 1];
                for (int i = 0; i < testVec.length; i++) {
                    testVec[i] = Double.parseDouble(data[i]);
                }
                testVectors.add(testVec);
                trueClasses.add(data[data.length - 1]);
            }
        } catch (IOException e) {
            System.out.println("Problem reading " + test_Set + " file");
        }
    }

    //I used this method to extract data for every k to draw a graph in Excel (it's no longer valid)
    /*private static void writeResultsToFile(String trainSet, String testSet) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(trainSet + "_result.tsv"))) {
            writer.write("k\taccuracy\n");

            for (int i = 1; i <= 100; i++) {
                Main m = new Main(i);
                m.readData(trainSet);
                double acc = m.accuracy(testSet);
                writer.write(i + "\t" + String.format("%.2f", acc) + "\n");
            }

        } catch (IOException e) {
            System.out.println("Problem writing to file: " + e.getMessage());
        }
    }*/
}