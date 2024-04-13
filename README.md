# k-NN_Algorithm
Simple k-NN algorithm, that learns from a given set of data and predicts the outcome for new data.

MiniProject: k-NN (Nearest Neighbours)

public Main(int k) {
* This is the constructor of our Main class that takes args[0] and assign k variable to it
* It also creates our training set as an ArrayList<>
}

public static void main(String[] args) {
* This is our main method that is our entry of the program 
* It takes k, train_set, test_set from args
* It asks user if he want to check his own vector or just check accuracy for test file 
    * If user wants to insert his own vector: 
        * handleNewVector() method is initiated 
    * If user don’t want to check his own vector: 
        * Method readData() is called
        * We create List of testVectors and List of trueClasses 
        * Method readTestSet method is called
        * It calls accuracy() method passing testVectors, trueClasses filled by readTestSet method
        * It prints accuracy of the model.
}

static class Entry{
* This class represents an entry in the dataset
* It contains attributes (double array) and the class Name (String)
}

class ProcessedEntry {
* This class represents processed Entry during classification 
* It holds the distance (double) from a test instance and the class Name (String)
}

public static double euclideanDistance(double[] x, double[] y) {
* This method calculates the Euclidean distance between two data points represented by double arrays.
}

public void readData(String filename) {
* This method reads data from a test_Set file and 
* Populates the training set with newly created Entry instances. 
}

public String classify(double[] testData){
* This method performs k-nearest neighbor classification.
* For each test instance, it calculates distances to all training instances, sorts them, selects the k-nearest neighbors, and predicts the class based on majority voting.

* Step by step breakdown: 
    * Hashmap map store class Names and their counts.
    * List<ProcessedEntry> distances store distances between the test instance and training instances.
    * Then it iterates through each entry in the trainingSet: 
        * Calculate the Euclidean distance between the testData and each training instance's attributes.
        * Create a ProcessedEntry object containing the distance and the class label of the training instance.
        * Add the ProcessedEntry object to the distances list.
    * Sort the distances list based on the distances in ascending order using a comparator.
    * Iterate through the distances list up to k nearest neighbours: 
        * Get the class label of each nearest neighbor and increment its count in the map.
    * Find the class label with the highest count (mode) in the map.
        * Assign this class label to the prediction variable.
    * Return the predicted class label.
}

public double accuracy(String testSetFile){
* Initialize variables correct and total to keep track of the number of correctly classified instances and the total number of instances in the test set, respectively.
* correct is initialized to 0.
* total is assigned the size of the testVectors list, which represents the total number of test instances.
* Iterate over each test instance using a for loop.
    * Retrieve the test vector (double[]) at the current index from the testVectors list.
    * Retrieve the true class (String) at the current index from the trueClasses list.
    * Call the classify method to predict the class of the test vector.
    * Print the predicted class and the true class to the console.
    * Compare predicted to true class and increment correct if it is equal
* Return the accuracy as a double value.
}

private static void handleNewVector(int k, String train_Set){
* It reads vector from the user (format: x.x,y.y,z.z,w.w)
* It splits the vector and put each double separately into double array
* The method creates a new instance of the Main class with the specified value of k.
* It calls the readData method of the Main instance to read the training data from the specified file (train_Set).
* It calls the classify method of the Main instance, passing the newly created vector (newVector) as an argument.
* The result of the classification is stored in the result variable.
* Result is printed on the console 
}

private static void readTestSet(test_Set, TestVectors, trueClasses) {
Overall, the readTestSet method reads the test set data from the specified file, parses each line to extract the test vector components and true class, and adds them to the corresponding lists (testVectors and trueClasses).

* Method reads each line from the given file and splits its wherever comma is (,)
* Then it initializes a new double array (testVec) with a length equal to the length of data minus one (to exclude the true class).
    * For each element, it parses the string representation of the component as a double using Double.parseDouble() and stores the result in the corresponding index of testVec.
* After parsing the components of the line, the method adds the resulting testVec array to the testVectors list.
* It also adds the last element of data (the true class) to the trueClasses list.
}
