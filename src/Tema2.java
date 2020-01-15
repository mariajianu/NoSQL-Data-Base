import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Scanner;

/**
 * @author Maria Jianu 321CB In aceasta clasa o sa citesc comenzile din fisierul
 *         de input iar apoi apelez metodele care se ocupa de ele din celelalte
 *         clase.
 *
 */
public class Tema2 {

	public static void main(String args[]) {
		// golesc fisirul de output
		emptyFile(args[0] + "_out");
		Scanner reader = null;
		String command;
		DataBase b = null;
		Entity s = null;
		int intAtr = 0;
		float floatAtr = 0;
		String stringAtr = null;
		int timeStamp = 0;
		try {
			reader = new Scanner(new File(args[0]));
			while (reader.hasNext() == true) {
				timeStamp++;
				command = reader.next();
				if (command.equals("CREATEDB")) {
					// numele bazei de date
					String name = reader.next();
					int nrNoduri = Integer.parseInt(reader.next());
					// capacitatea maxima a unui nod
					int max = Integer.parseInt(reader.next());
					b = new DataBase(name, nrNoduri, max);
				}
				if (command.equals("CREATE")) {
					// numele entitatii
					String name = reader.next();
					int RF = Integer.parseInt(reader.next());
					int nrAtr = Integer.parseInt(reader.next());
					int count = nrAtr;
					String keyType = null;
					// creez un obict de tip entitate pe care il adaug in ArrayList
					s = new Entity(name, RF, nrAtr);
					while (nrAtr != 0) {
						String nameAtr = reader.next(); // numele atributului
						String type = reader.next();
						// daca este primul atribut, setez cheia
						if (count == nrAtr) {
							keyType = type;
							s.setKeyType(keyType);
						}
						// adaug atributul in ArrayList-ul de atribute
						s.addAtribute(nameAtr, type, keyType);
						nrAtr--;
					}
					b.addEntity(s); // vector de entitati din baza de date
				}

				if (command.equals("INSERT")) {
					// numele entitatii
					String name = reader.next();
					for (Entity en : b.getEntities()) {
						int nrAt = en.getTotalAtr();
						int count = nrAt;
						// caut entitatea
						if (en.getEntityName().equals(name)) {
							for (Instance in : en.getAtribute()) {
								// citesc valorile in functie de tipul lor
								String nameAtr = in.getAtributeName();
								if (in.getAtributeType().equals("Integer"))
									intAtr = Integer.parseInt(reader.next());
								else if (in.getAtributeType().equals("Float"))
									floatAtr = Float.parseFloat(reader.next());
								else if (in.getAtributeType().equals("String"))
									stringAtr = reader.next();
								if (nrAt == count)
									// apelez cu ok = 1 -> flagul pentru setarea cheii
									b.updateInstanceInfo(name, nameAtr, intAtr, floatAtr, stringAtr, 1);
								else
									b.updateInstanceInfo(name, nameAtr, intAtr, floatAtr, stringAtr, 0);
								nrAt--;
								count = -3;
							}
							// adaug entitatea en cu noile informatii in baza de date
							b.updateNode(en, timeStamp);
						}
					}
				}

				if (command.equals("SNAPSHOTDB")) {
					String out_file = args[0] + "_out";
					b.printDataBase(out_file);
				}
				
				if (command.equals("GET")) {
					String out_file = args[0] + "_out";
					String entName = reader.next();
					String key = reader.next();
					String type = null;
					for (Entity en : b.getEntities()) {
						if (en.getEntityName().equals(entName)) {
							type = en.getKeyType();
							break;
						}
					}
					// cauta in entitati atributul cu respectiva cheie
					b.findInstance(entName, type, key, out_file);
				}
				
				if (command.equals("DELETE")) {
					String entName = reader.next();
					String key = reader.next();
					String out_file = args[0] + "_out";
					// sterge din baza de date entitatea cu atributul cerut
					b.deleteInstance(entName, key, out_file);
				}
				
				if (command.equals("UPDATE")) {
					String entName = reader.next();
					int count = 0;
					int intKey = 0;
					float floatKey = 0;
					String atrName = null;
					String stringKey = null;
					for (Entity en : b.getEntities()) {
						//cauta entitatea
						if (en.getEntityName().equals(entName)) {
							int nrAtr = en.getTotalAtr();
							count = nrAtr;
							for (Instance in : en.getAtribute()) {
								//daca e primul atribut citeste cheia dupa care caut
								if (count == nrAtr) {
									if (in.getKeyType().equals("Integer"))
										intKey = Integer.parseInt(reader.next());
									else if (in.getKeyType().equals("Float"))
										floatKey = Float.parseFloat(reader.next());
									else if (in.getKeyType().equals("String"))
										stringKey = reader.next();
									//numele atributului
									atrName = reader.next();
									//daca nu e primul atribut citeste noua valoare
								} else if (in.getAtributeName().equals(atrName)) {
									if (in.getAtributeType().equals("Integer")) {
										int intVal = Integer.parseInt(reader.next());
										//metoda care are valoarea int
										b.changeInstance(en, atrName, intKey, floatKey, stringKey, intVal, timeStamp);
									}
									if (in.getAtributeType().equals("Float")) {
										float floatVal = Float.parseFloat(reader.next());
										//metoda care are valoare float
										b.changeInstance(en, atrName, intKey, floatKey, stringKey, floatVal, timeStamp);
									}
									if (in.getAtributeType().equals("String")) {
										String stringVal = reader.next();
										//metoda care are valoarea string
										b.changeInstance(en, atrName, intKey, floatKey, stringKey, stringVal,
												timeStamp);
									}
								}
								nrAtr--;

							}

						}
					}
					//sorteaza atrobutele in nod dupa timeStamp
					for (Node n : b.getNodes()) {
						Collections.sort(n.getInstance(), new CompareObj());
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	/**
	 * @param file fisierul care trebuie golit, cel de output
	 *
	 */
	public static void emptyFile(String file) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(file));
			writer.print("");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
