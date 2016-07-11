import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Algorithm {

	public static double splitRatio;
	static Dataset data;
	static Dataset train;
	static Dataset test;
	public static double entropyThreshold;

	public Algorithm(double splitRatio, Dataset data, double entropyThreshold) {
		Algorithm.splitRatio = splitRatio;
		Algorithm.data = data;
		Algorithm.entropyThreshold = entropyThreshold;
		splitData();
	}

	private static void splitData() {
		int trainRowCount = (int) Math.floor(splitRatio * data.getRowCount());

		ArrayList<DataRow> trainrows = new ArrayList<DataRow>();
		ArrayList<DataRow> testrows = new ArrayList<DataRow>();

		for (int i = 0; i < data.rows.size(); i++) {
			if (i < trainRowCount) {
				trainrows.add(data.rows.get(i));
			} else {
				testrows.add(data.rows.get(i));
			}
		}

		train = new Dataset(data.header, trainrows);
		test = new Dataset(data.header, testrows);

	}

	public DecisionTree train() {

		DecisionTree tree = new DecisionTree(train);
		generateNode(tree.root);
		return tree;

	}

	private static void generateNode(TreeNode node) {

		node.attribute = getGoodAttribute(node.sample);

		// find threshold

		node.threshold = findThreshold(node.sample, node.attribute);
		node.entropy = getEntropy(node.sample);

		Dataset sampleBelow = getFilteredArray(node.sample, node.attribute, node.threshold);
		Dataset sampleAbove = getFilteredArray(node.sample, node.attribute, node.threshold);
		node.belowThreshold = new TreeNode(sampleBelow);
		node.aboveThreshold = new TreeNode(sampleAbove);

		if (getEntropy(node.aboveThreshold.sample) > entropyThreshold) {
			generateNode(node.aboveThreshold);
		}
		if (getEntropy(node.belowThreshold.sample) > entropyThreshold) {
			generateNode(node.belowThreshold);
		}
	}

	private static double findThreshold(Dataset sample, int attribute) {

		// get values of attribute

		HashMap<Double, Integer> frequency = getFrequencyMap(sample, attribute);
		// assuming all input values will be Numeric
		Set<Double> valueSet = frequency.keySet();

		// find threshold by looping across values and calculating entropies

		double minEntropy = 1;
		double a = 1, b = 1, c = 1;
		double threshold = 0;
		for (Double value : valueSet) {
			Dataset aboveThreshold = getFilteredArray(sample, attribute, value);
			Dataset belowThreshold = getFilteredArray(sample, attribute, value);
			a = getEntropy(aboveThreshold);
			b = getEntropy(belowThreshold);
			c = a > b ? b : a;

			if (c < minEntropy) {
				minEntropy = c;
				threshold = value;
			}
		}

		return threshold;
	}

	private static int getGoodAttribute(Dataset sample) {
		double minEntropy = 1;
		double entropy = 0;

		int goodAttribute = 0;

		// find attribute with lowest entropy

		for (int i = 0; i < sample.rows.size(); i++) {
			entropy = getAttributeEntropy(sample, i);
			System.out.println("Attribute : " + i + ", Entropy : " + entropy);
			if (entropy < minEntropy && entropy != 0) {
				minEntropy = entropy;
				goodAttribute = i;
			}
		}

		System.out.println(goodAttribute + " " + minEntropy);

		return goodAttribute;
	}

	private static double getAttributeEntropy(Dataset dataset, int colNum) {

		Dataset filteredData;
		double entropy = 0;
		double noOfRows = dataset.rows.size();

		HashMap<Double, Integer> freqMap = getFrequencyMap(dataset, colNum);

		for (Double value : freqMap.keySet()) {
			filteredData = getFilteredArray(dataset, colNum, value);
			// System.out.println("getAttributeEntropy : " + value + "=>" +
			// entropy);
			entropy += ((double) filteredData.rows.size() / (double) noOfRows) * getEntropy(filteredData);
			// System.out.println("getAttributeEntropy final entropy =>" +
			// entropy);
		}

		return entropy;
	}

	private static double getEntropy(Dataset sample) {

		ArrayList<DataRow> rows = sample.rows;

		ArrayList<String> targetValues = new ArrayList<String>();

		for (DataRow row : rows) {
			targetValues.add(row.target);
		}

		String[] values = (String[]) targetValues.toArray();

		HashMap<String, Integer> frequency = new HashMap<String, Integer>();

		int value = 0;

		for (int i = 0; i < values.length; i++) {
			if (frequency.containsKey(values[i])) {
				value = frequency.get(values[i]);
				frequency.replace(values[i], value, value + 1);
			} else {
				frequency.put(values[i], 1);
			}
		}

		double entropy = 0;
		double pi = 0;
		for (String key : frequency.keySet()) {
			pi = ((double) frequency.get(key).intValue()) / ((double) values.length);
			entropy = entropy - pi * Math.log(pi);
			// System.out.println("getEntropy : " + key + " => " + entropy);
		}
		// System.out.println("getEntropy return => " + entropy);
		return entropy;
	}

	private static Dataset getFilteredArray(Dataset dataset, int attributeIndex, double value) {

		ArrayList<DataRow> filteredRows = new ArrayList<DataRow>();

		for (DataRow row : dataset.rows) {
			if (row.values[attributeIndex] == value) {
				filteredRows.add(row);
			}
		}

		Dataset filteredData = new Dataset(dataset.header, filteredRows);
		return filteredData;
	}

	private static HashMap<Double, Integer> getFrequencyMap(Dataset dataset, int attribute) {
		HashMap<Double, Integer> freqMap = new HashMap<Double, Integer>();

		double value = 0;
		int count = 0;

		for (int i = 1; i < dataset.rows.size(); i++) {
			value = dataset.rows.get(i).values[attribute];

			if (freqMap.containsKey(value)) {
				count = freqMap.get(value);
				freqMap.replace(value, count, count + 1);
			}

			else {
				freqMap.put(value, 1);
			}
		}
		return freqMap;
	}

}
