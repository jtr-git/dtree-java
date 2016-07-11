
public class TreeNode {
	int attribute;
	Dataset sample;
	double threshold;
	double entropy;
	TreeNode belowThreshold;
	TreeNode aboveThreshold;

	public TreeNode(Dataset sample) {
		this.sample = sample;
	}
}
