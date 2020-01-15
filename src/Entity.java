import java.util.ArrayList;
/**
 * @author Maria Jianu 321CB
 *
 */

public class Entity {
	private String entityName;
	private int RF;
	private int totalAtr;
	private String keyType;
	private int nrInt = 0;
	private int nrFloat = 0;
	private int nrString = 0;
	private int intKey;
	private float floatKey;
	private ArrayList<Instance> atribute;
	private int timeStamp;
	
	public Entity(String name, int RF, int totalAtr) {
		entityName = name;
		this.RF = RF;
		this.totalAtr = totalAtr;
		atribute = new ArrayList<Instance>(totalAtr);
	}

	/**
	 * @param name numele atributului
	 * @param type tipul atributului
	 * @param keyType tipul cheii
	 */
	public void addAtribute(String name, String type, String keyType) {
		//adauga un atribut in ArrayList-ul de atribute
		Instance s = new Instance(name, type, keyType);
		atribute.add(s);
	}

	public int getIntKey() {
		return intKey;
	}
	public int getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}
	public void setIntKey(int intKey) {
		this.intKey = intKey;
	}

	public float getFloatKey() {
		return floatKey;
	}

	public void setFloatKey(float floatKey) {
		this.floatKey = floatKey;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getEntityName() {
		return entityName;
	}

	public int getNrInt() {
		return nrInt;
	}

	public void setNrInt(int nrInt) {
		this.nrInt = nrInt;
	}

	public int getNrFloat() {
		return nrFloat;
	}

	public void setNrFloat(int nrFloat) {
		this.nrFloat = nrFloat;
	}

	public int getNrString() {
		return nrString;
	}

	public void setNrString(int nrString) {
		this.nrString = nrString;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public int getRF() {
		return RF;
	}

	public void setRF(int rF) {
		RF = rF;
	}

	public int getTotalAtr() {
		return totalAtr;
	}

	public void setTotalAtr(int totalAtr) {
		this.totalAtr = totalAtr;
	}

	public ArrayList<Instance> getAtribute() {
		return atribute;
	}

	public void setAtribute(ArrayList<Instance> atribute) {
		this.atribute = atribute;
	}

}
