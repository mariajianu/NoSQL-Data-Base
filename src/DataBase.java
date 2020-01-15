
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
/**
 * @author Maria Jianu 321CB
 * Aceasta este clada DataBase care contine nodurile si toate info din noduri
 * 
 *
 */
public class DataBase {
	//vectorul de noduri
	private ArrayList<Node> nodes; 
	private String dataBaseName;
	private int nrOfNodes, capacityOfNode;
	private int occupiedNodes = 0;
	// toate entitatile facute prin CREATE
	private ArrayList<Entity> entities;
	
	/**
	 * @param name numele bazei de date
	 * @param nr numarul de elemente
	 * @param max capacitatea unui nod
	 */
	public DataBase(String name, int nr, int max) {
		nodes = new ArrayList<Node>(nr); 
		dataBaseName = name;
		nrOfNodes = nr;
		capacityOfNode = max;
		entities = new ArrayList<Entity>(20);
	}

	/**
	 * @param name numele entitatii
	 * @param nameA numele atributului
	 * @param intAtr valoarea int a atributului
	 * @param floatAtr valoarea float a atributului
	 * @param stringAtr valorea string a atributului
	 * @param ok flagu-ul pentru setarea cheii -> daca e 1, cheia trebuie setata
	 */
	public void updateInstanceInfo(String name, String nameA, int intAtr, float floatAtr, String stringAtr, int ok) {
		int nrAtr;
		int count;
		for (Entity en : entities) {
			//gaseste entitatea cu numele name
			if (en.getEntityName().equals(name)) {
				nrAtr = en.getTotalAtr();
				count = nrAtr;
				for (Instance in : en.getAtribute()) {
					if (count == nrAtr && ok == 1)
						//seteaza cheia 
						setKey(en, in, intAtr, floatAtr, stringAtr);
					//seteaza valoarea atributului in functie de tipul ei
					if (in.getAtributeType().equals("Integer") && in.getAtributeName().equals(nameA))
						in.setIntValue(intAtr);
					else if (in.getAtributeType().equals("Float") && in.getAtributeName().equals(nameA))
						in.setFloatValue(floatAtr);
					else if (in.getAtributeType().equals("String") && in.getAtributeName().equals(nameA))
						in.setStringValue(stringAtr);
					nrAtr--;
				}
			}
		}
	}

	/**
	 * @param en entitatea careia trebuie sa-i setam cheia
	 * @param in atributul care este, de fapt, cheia
	 * @param intKey valoarea int a cheii
	 * @param floatKey valorea float a cheii
	 * @param stringKey valoarea string a cheii
	 */
	public void setKey(Entity en, Instance in, int intKey, float floatKey, String stringKey) {
		//verifica tipul atributului si seteaza cheia corespunzator
		if (en.getKeyType().equals("Integer")) {
			in.setKeyType("Integer");
			in.setIntKey(intKey);
		}
		if (en.getKeyType().equals("Float")) {
			in.setKeyType("Float");
			in.setFloatKey(floatKey);
		}
		if (en.getKeyType().equals("String")) {
			in.setKeyType("String");
			in.setStringKey(stringKey);
		}
	}

	/**
	 * @param in ArrayList-ul care trebuie clonat
	 * @return clona ArrayList-ului in
	 */
	public ArrayList<Instance> copy(ArrayList<Instance> in) {
		//cloneaza ArrayList-ul in, si intoarce clona lui
		ArrayList<Instance> copy = new ArrayList<Instance>(in.size());
		for (Instance i : in) {
			copy.add(new Instance(i));
		}
		return copy;
	}

	/**
	 * @param en entitatea pe care o vom clona si replica in noduri
	 * @param time timeStampul dupa care vom decide ulterior pozitia atributelor
	 */
	public void updateNode(Entity en, int time) {
		Entity en2 = null;
		Node n2 = null;
		int count = 0;
		ArrayList<Instance> in;
		//clona ArrayList-ului din entitatea en
		in = copy(en.getAtribute());
		int rf = en.getRF();
		if (occupiedNodes == 0) {
			//daca baza de date e goala
			while (rf != 0) {
				//construiesc rf noduri
				n2 = new Node(capacityOfNode);
				//entitatea din nod
				en2 = new Entity(en.getEntityName(), en.getRF(), en.getTotalAtr());
				//setez vectorul de atribute al entitatii
				en2.setAtribute(in);
				en2.setTimeStamp(time);
				en2.setKeyType(en.getKeyType());
				//adaug entitatea in arraylist ul nodului
				n2.getInstance().add(0, en2);
				n2.setElemInNode(n2.getElemInNode() + 1);
				//adaug nodul in arraylist ul de noduri
				nodes.add(n2);
				occupiedNodes++;
				rf--;
			}
		} else {
			//daca baza de date nu era goala
			for (Node n : nodes) {
				count++;
				//adaug doar in nodurile in care mai este loc si sunt deja initializate
				if (rf != 0 && n.getElemInNode() < n.getMaxCapacity()) {
					en2 = new Entity(en.getEntityName(), en.getRF(), en.getTotalAtr());
					en2.setKeyType(en.getKeyType());
					en2.setAtribute(in);
					en2.setTimeStamp(time);
					n.setElemInNode(n.getElemInNode() + 1);
					n.getInstance().add(0, en2);
					rf--;
				}
			}
			if (rf != 0) {
				//daca rf era mai mare decat numarul de noduri initializate
				//initializam alte noduri si adaugam in ele pana ajungem la rf
				int nr = rf;
				while (nr != 0) {
					n2 = new Node(capacityOfNode);
					en2 = new Entity(en.getEntityName(), en.getRF(), en.getTotalAtr());
					en2.setAtribute(in);
					en2.setTimeStamp(time);
					en2.setKeyType(en.getKeyType());
					n2.getInstance().add(0, en2);
					n2.setElemInNode(n2.getElemInNode() + 1);
					nodes.add(n2);
					occupiedNodes++;
					nr--;
				}
			}
		}
	}

	/**
	 * @param out_file fisierul in care scriu
	 * metoda care imi printeaza in fisier baza de date
	 */
	public void printDataBase(String out_file) {
		PrintWriter writer = null;
		int count = 1;
		int nrAt;
		String pattern = "#.##";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		try {
			writer = new PrintWriter(new FileWriter(out_file, true));
			if (occupiedNodes == 0)
				writer.println("EMPTY DB");
			else {
				for (Node n : nodes) {
					writer.print("Nod");
					writer.println(count);
					for (Entity en : n.getInstance()) {
						writer.print(en.getEntityName() + " ");
						nrAt = en.getTotalAtr();
						for (Instance in : en.getAtribute()) {
							nrAt--;
							writer.print(in.getAtributeName() + ":");
							if (in.getAtributeType().equals("Integer"))
								writer.print(in.getIntValue());
							else if (in.getAtributeType().equals("Float")) {
								String format = decimalFormat.format(in.getFloatValue());
								writer.print(format);
							} else if (in.getAtributeType().equals("String"))
								writer.print(in.getStringValue());
							if (nrAt != 0)
								writer.print(" ");
						}
						writer.println();
					}
					count++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.close();
	}

	public static boolean isNumeric(String str) {
		//metoda care verifica daca un string este numeric 
		try {
			int in = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/**
	 * @param enName numele entitatii
	 * @param keyType tipul cheii
	 * @param key valoarea cheii
	 * @param out_file fisierul de output
	 */
	public void findInstance(String enName, String keyType, String key, String out_file) {
		//metoda care se ocupa de comanda GET
		int countNode = 1;
		int intKey = 0;
		float floatKey = 0;
		int nrAtr = 0;
		int ok = 0;
		int ok2 = 0;
		int stop = 0;
		String stringKey = null;
		PrintWriter writer = null;
		String pattern = "#.##";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		if (keyType.equals("Integer")) {
			//verifica daca stringul e numeric si daca e il schimba in int
			if (isNumeric(key) == true)
				intKey = Integer.parseInt(key);
			else
				stop = 1;
		}
		if (keyType.equals("Float")) {
			//schimba stringul key in float
			floatKey = Float.parseFloat(key);
		}
		if (keyType.equals("String")) {
			stringKey = key;
		}
		try {
			writer = new PrintWriter(new FileWriter(out_file, true));
			for (Node n : nodes) {
				for (Entity en : n.getInstance()) {
					if (en.getEntityName().equals(enName)) {
						for (Instance in : en.getAtribute()) {
							// o sa mergem doar prin prima instanta
							// verifica cheia dupa tip si valoare
							if (in.getKeyType().equals("Integer") && in.getIntKey() == intKey && stop == 0) {
								writer.print("Nod" + countNode + " ");
								ok2 = 1;
							} else if (in.getKeyType().equals("Float") && in.getFloatKey() == floatKey) {
								writer.print("Nod" + countNode + " ");
								ok2 = 1;
							} else if (in.getKeyType().equals("String") && stringKey.equals(in.getStringKey())) {
								writer.print("Nod" + countNode + " ");
								ok2 = 1;
							}
						}
						if (ok2 == 1)
							nrAtr = en.getTotalAtr();
					}
				}
				countNode++;
			}
			if (ok2 == 1 && stop == 0) {
				writer.print(enName + " ");
				bigloop: 
				for (Node n : nodes) {
					for (Entity en : n.getInstance()) {
						//cauta entitatea
						if (en.getEntityName().equals(enName)) {
							for (Instance in : en.getAtribute()) {
								if (in.getKeyType().equals("Integer") && in.getIntKey() == intKey) {
									ok = 1;
								} else if (in.getKeyType().equals("Float") && in.getFloatKey() == floatKey) {
									ok = 1;
								} else if (in.getKeyType().equals("String") && stringKey.equals(in.getStringKey())) {
									ok = 1;
								}
								//daca am gasit atributul cu cheia ceruta il printam in fisier
								if (ok == 1) {
									nrAtr--;
									writer.print(in.getAtributeName() + ":");
									if (in.getAtributeType().equals("Integer"))
										writer.print(in.getIntValue());
									else if (in.getAtributeType().equals("Float")) {
										String format = decimalFormat.format(in.getFloatValue());
										writer.print(format);
									} else if (in.getAtributeType().equals("String"))
										writer.print(in.getStringValue());
									if (nrAtr != 0)
										writer.print(" ");
								}
							}
							if (ok == 1) {
								writer.println();
								break bigloop;
							}

						}
					}
				}
			} else
				writer.println("NO INSTANCE FOUND");
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.close();
	}

	/**
	 * @param entName numele entitatii
	 * @param key cheia
	 * @param out_file fisierul de output
	 */
	/**
	 * @param entName
	 * @param key
	 * @param out_file
	 */
	public void deleteInstance(String entName, String key, String out_file) {
		//metoda care se ocupa de comanda DELETE
		String keyType = null;
		int intKey = 0;
		float floatKey = 0;
		int ok = 0;
		int ok2 = 0;
		String stringKey = null;
		Entity item = null;
		PrintWriter writer = null;
		for (Entity en : entities) {
			if (en.getEntityName().equals(entName)) {
				keyType = en.getKeyType();
				break;
			}
		}
		//convertim stringul cheii la valoarea lui
		if (keyType.equals("Integer")) {
			if (isNumeric(key) == true)
				intKey = Integer.parseInt(key);
		}
		if (keyType.equals("Float")) {
			floatKey = Float.parseFloat(key);
		}
		if (keyType.equals("String")) {
			stringKey = key;
		}
		for (Node n : nodes) {
			for (Entity en : n.getInstance()) {
				if (en.getEntityName().equals(entName)) {
					for (Instance in : en.getAtribute()) {
						if (in.getKeyType().equals("Integer") && in.getIntKey() == intKey) {
							ok = 1;
						} else if (in.getKeyType().equals("Float") && in.getFloatKey() == floatKey) {
							ok = 1;
						} else if (in.getKeyType().equals("String") && stringKey.equals(in.getStringKey())) {
							ok = 1;
						}
					}
					if (ok == 1) {
						item = en;
					}
				}
				if (ok == 1)
					break;
			}
			//daca a fost gasit elementul cu cheia ceruta il stergem din nod
			if (ok == 1) {
				n.getInstance().remove(item);
				n.setElemInNode(n.getElemInNode() - 1);
				ok2 = 1;
				ok = 0;
			}
		}
		//daca nu am gasit elementul afisam mesajul de eroare
		if (ok2 == 0) {
			try {
				writer = new PrintWriter(new FileWriter(out_file, true));
				writer.println("NO INSTANCE TO DELETE");
			} catch (IOException e) {
				e.printStackTrace();
			}
			writer.close();
		}
	}

	/**
	 * @param entity entitatea in care modificam
	 * @param name numele entitatii
	 * @param intKey valoarea int a cheii
	 * @param floatKey valoarea float a cheii
	 * @param stringKey valoarea string a cheii
	 * @param val valorea int care trebuie updatata in nod
	 * @param time timeStampul dupa care vom sorta
	 */
	public void changeInstance(Entity entity, String name, int intKey, float floatKey, String stringKey, int val,
			int time) {
		int count = 0, ok = 0;
		for (Node n : nodes) {
			for (Entity en : n.getInstance()) {
				if (en.getEntityName().equals(entity.getEntityName())) {
					for (Instance in : en.getAtribute()) {
						count++;
						if (in.getKeyType().equals("Integer") && in.getIntKey() == intKey)
							ok = 1;
						else if (in.getKeyType().equals("Float") && in.getFloatKey() == floatKey)
							ok = 1;
						else if (in.getKeyType().equals("String") && stringKey.equals(in.getStringKey()))
							ok = 1;
						if (count > 1 && ok == 1 && name.equals(in.getAtributeName())) {
							if (in.getAtributeType().equals("Integer")) {
								/*
								 * daca am gasit elementul cerut
								 * ii schimbam valoarea cu noua valoare 
								 * si modificam timeStampul pentru sortare
								 */
								in.setIntValue(val);
								en.setTimeStamp(time);
							}
						}
					}
					ok = 0;
				}
			}
		}
	}
	/**
	 * @param entity entitatea in care modificam
	 * @param name numele entitatii
	 * @param intKey valoarea int a cheii
	 * @param floatKey valoarea float a cheii
	 * @param stringKey valoarea string a cheii
	 * @param val valorea float care trebuie updatata in nod
	 * @param time timeStampul dupa care vom sorta
	 */
	public void changeInstance(Entity entity, String name, int intKey, float floatKey, String stringKey, float val,
			int time) {
		int count = 0, ok = 0;
		for (Node n : nodes) {
			for (Entity en : n.getInstance()) {
				if (en.getEntityName().equals(entity.getEntityName())) {
					for (Instance in : en.getAtribute()) {
						count++;
						if (in.getKeyType().equals("Integer") && in.getIntKey() == intKey)
							ok = 1;
						else if (in.getKeyType().equals("Float") && in.getFloatKey() == floatKey)
							ok = 1;
						else if (in.getKeyType().equals("String") && stringKey.equals(in.getStringKey()))
							ok = 1;
						if (count > 1 && ok == 1 && name.equals(in.getAtributeName())) {
							if (in.getAtributeType().equals("Float")) {
								/*
								 * daca am gasit elementul cerut
								 * ii schimbam valoarea cu noua valoare 
								 * si modificam timeStampul pentru sortare
								 */
								in.setFloatValue(val);
								en.setTimeStamp(time);
							}
						}
					}
					ok = 0;
				}
			}
		}
	}
	/**
	 * @param entity entitatea in care modificam
	 * @param name numele entitatii
	 * @param intKey valoarea int a cheii
	 * @param floatKey valoarea float a cheii
	 * @param stringKey valoarea string a cheii
	 * @param val valorea string care trebuie updatata in nod
	 * @param time timeStampul dupa care vom sorta
	 */
	public void changeInstance(Entity entity, String name, int intKey, float floatKey, String stringKey, String val,
			int time) {
		int ok = 0;
		int count = 0;
		for (Node n : nodes) {
			for (Entity en : n.getInstance()) {
				if (en.getEntityName().equals(entity.getEntityName())) {
					for (Instance in : en.getAtribute()) {
						count++;
						if (in.getKeyType().equals("Integer") && in.getIntKey() == intKey)
							ok = 1;
						else if (in.getKeyType().equals("Float") && in.getFloatKey() == floatKey)
							ok = 1;
						else if (in.getKeyType().equals("String") && stringKey.equals(in.getStringKey()))
							ok = 1;
						if (count > 1 && ok == 1 && name.equals(in.getAtributeName())) {
							if (in.getAtributeType().equals("String")) {
								/*
								 * daca am gasit elementul cerut
								 * ii schimbam valoarea cu noua valoare 
								 * si modificam timeStampul pentru sortare
								 */
								in.setStringValue(val);
								en.setTimeStamp(time);
							}
						}
					}
					ok = 0;
				}
			}
		}
	}

	public int getOccupiedNodes() {
		return occupiedNodes;
	}

	public void setOccupiedNodes(int occupiedNodes) {
		this.occupiedNodes = occupiedNodes;
	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public String getDataBaseName() {
		return dataBaseName;
	}

	public void setDataBaseName(String dataBaseName) {
		this.dataBaseName = dataBaseName;
	}

	public int getNrOfNodes() {
		return nrOfNodes;
	}

	public void setNrOfNodes(int nrOfNodes) {
		this.nrOfNodes = nrOfNodes;
	}

	public int getCapacity() {
		return capacityOfNode;
	}

	public void setCapacity(int capacity) {
		this.capacityOfNode = capacity;
	}

}
