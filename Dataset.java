import java.util.ArrayList;

public class Dataset {

	String[] header;
	ArrayList<DataRow> rows;

	int splitAttribute;
	String threshold;
	double entropy;

	public Dataset(String[] header, ArrayList<DataRow> rows) {
		// TODO Auto-generated constructor stub
		this.header = header;
		this.rows = rows;
	}

	public int getRowCount() {
		return rows.size();
	}

	public int getColumnCount() {
		return header.length;
	}
}