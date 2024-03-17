import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private int k;
    private List<Entry> trainingSet;

    public Main(int k) {
        this.k = k;
        trainingSet = new ArrayList<>();
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

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        String train_Set = args[1];
        String test_Set = args[2];

        Main main = new Main(k);
        main.readData(train_Set);
        double accuracy = main.accuracy(test_Set);
        System.out.println("Accuracy of this model is equal to: " + accuracy);

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

        for(int i = 0; i < k; i++) {
            String name = distances.get(i).getName();
            map.put(name, map.getOrDefault(name, 0) + 1);
        }

        String prediction = null;
        int max = -1;

        for(Map.Entry<String, Integer> entry : map.entrySet()) {
            if(entry.getValue() > max) {
                max = entry.getValue();
                prediction = entry.getKey();
            }
        }
        return prediction;
    }

    public double accuracy(String testSetFile) {
        int correct = 0;
        int total = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(testSetFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                double[] testVec = new double[data.length - 1];
                for (int i = 0; i < testVec.length; i++) {
                    testVec[i] = Double.parseDouble(data[i]);
                }

                String trueClass = data[data.length - 1];
                String result = classify(testVec);

                System.out.println("Predicted class: " + result);
                System.out.println("True class: " + trueClass);

                if(result.equals(trueClass)) {
                    correct++;
                }
                total++;
            }
        } catch (IOException e) {
            System.out.println("Problem reading " + testSetFile + " file");
        }
        return (double) correct / total;
    }
}