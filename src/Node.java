import java.util.ArrayList;
/**
 * @author Maria Jianu 321CB
 *
 */
public class Node {
	private int maxCapacity;
	private int elemInNode = 0;
	private int occupied = 0;
	private ArrayList<Entity> instance;

	public Node(int n) {
		maxCapacity = n;
		instance = new ArrayList<Entity>(20);
	}

	public int getElemInNode() {
		return elemInNode;
	}

	public void setElemInNode(int elemInNode) {
		this.elemInNode = elemInNode;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public int getOccupied() {
		return occupied;
	}

	public void setOccupied(int occupied) {
		this.occupied = occupied;
	}

	public ArrayList<Entity> getInstance() {
		return instance;
	}

	public void setInstance(ArrayList<Entity> instance) {
		this.instance = instance;
	}

}
