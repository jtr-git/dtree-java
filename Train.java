import java.io.FileNotFoundException;

public class Train {

	public static final String file = "DT-datasetHW#3.csv";
	public static final double splitRatio = 0.8;
	public static final double entropyThreshold = 0.8;

	public static void main(String[] args) throws FileNotFoundException {

		DataReader reader = new DataReader(file);
		Algorithm algorithm = new Algorithm(splitRatio, reader.dataset, entropyThreshold);
		// train a model
		DecisionTree model = algorithm.train();
		model.writeToFile("model");
	}

}