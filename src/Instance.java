/**
 * @author Maria Jianu 321CB
 *
 */
public class Instance {
	private String atributeName;
	private String atributeType;
	private int intValue = 0;
	private float floatValue = 0;
	private String stringValue = null;
	private String keyType = null;
	private int intKey = 0;
	private float floatKey = 0;
	private String stringKey = null;

	public Instance(String name, String type, String key) {
		atributeName = name;
		atributeType = type;
		keyType = key;
	}

	/**
	 * @param in instanta care trebuie clonata
	 */
	public Instance(Instance in) {
		//constructor care cloneaza un obiect deja existent
		atributeName = in.atributeName;
		atributeType = in.atributeType;
		intValue = in.intValue;
		floatValue = in.floatValue;
		stringValue = in.stringValue;
		if (in.keyType.equals("Integer")) {
			intKey = in.intKey;
		}
		if (in.keyType.equals("Float")) {
			floatKey = in.floatKey;
		}
		if (in.keyType.equals("String")) {
			stringKey = in.stringKey;
		}
		keyType = in.keyType;
	}

	public String getAtributeName() {
		return atributeName;
	}

	public void setAtributeName(String atributeName) {
		this.atributeName = atributeName;
	}

	public String getAtributeType() {
		return atributeType;
	}

	public void setAtributeType(String atributeType) {
		this.atributeType = atributeType;
	}

	public int getIntKey() {
		return intKey;
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

	public String getStringKey() {
		return stringKey;
	}

	public void setStringKey(String stringKey) {
		this.stringKey = stringKey;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public float getFloatValue() {
		return floatValue;
	}

	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

}
