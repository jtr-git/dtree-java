import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataReader {

	public static final int noOfCol = 6;
	public Dataset dataset;
	public static Scanner scanner;

	public DataReader(String file) throws FileNotFoundException {
		scanner = new Scanner(new File(file));

		String[] header = getHeader();

		ArrayList<DataRow> rows = new ArrayList<DataRow>();

		while (scanner.hasNextLine()) {

			rows.add(new DataRow(getNextRow()));
		}

		dataset = new Dataset(header, rows);

	}

	public Dataset getDataset() {
		return dataset;
	}

	private static String[] getHeader() {
		return readWords(noOfCol);
	}

	private static String[] getNextRow() {
		return readWords(noOfCol);
	}

	private static String[] readWords(int n) {
		if (scanner.hasNextLine()) {
			return scanner.nextLine().split(",");
		}
		return null;
	}

}
