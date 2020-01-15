import java.util.Comparator;
/**
 * @author Maria Jianu 321CB
 *
 */
public class CompareObj implements Comparator<Entity> {

	public int compare(Entity en1, Entity en2) {
		//compara doua obiecte Entity dupa timeStampul lor
		//folosesc aceasta metoda cu functia de sortare deja imprementata sort
		return en2.getTimeStamp() - en1.getTimeStamp();
	}
}
