
public class DataRow {

	public DataRow(String[] str) {

		this.target = str[str.length - 1];
		values = new double[str.length - 1];

		for (int i = 0; i < str.length - 1; i++) {
			this.values[i] = Double.parseDouble(str[i]);
		}
	}

	double[] values;
	String target;
}
