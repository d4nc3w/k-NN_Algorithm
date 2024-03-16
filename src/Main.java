import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private int k;
    private List<double[]> trainingSet;

    public Main(int k) {
        this.k = k;
        trainingSet = new ArrayList<>();
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
                trainingSet.add(values);
            }
        } catch (IOException e) {
            System.out.println("Problem reading " + filename + " file");
        }
    }

    public String classify(double[] testData) {
        Map<String, Integer> map = new HashMap<>();
        List<double []> distances = new ArrayList<>();

        for(double[] trainVec : trainingSet) {
            double dist = euclideanDistance(testData, trainVec);
            distances.add(new double[]{
                    dist, trainVec[trainVec.length - 1]
            });
        }
        distances.sort(Comparator.comparingDouble(a -> a[0]));

        for(int i = 0; i < k; i++) {
            double[] nearest = distances.get(i);
            String label = Double.toString(nearest[1]);
            map.put(label, map.getOrDefault(label, 0) + 1);
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

    public static double euclideanDistance(double[] x, double[] y) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow(x[i] - y[i], 2);
        }
        return Math.sqrt(sum);
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
                if(result.equals(trueClass)) {
                    correct++;
                }
                total++;
        }
    }
        catch (IOException e) {
            System.out.println("Problem reading " + testSetFile + " file");
        }
        return (double) correct / total;
    }
}