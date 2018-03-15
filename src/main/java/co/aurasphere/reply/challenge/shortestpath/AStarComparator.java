package co.aurasphere.reply.challenge.shortestpath;

import java.util.Comparator;
import java.util.List;

import co.aurasphere.reply.challenge.shortestpath.model.Node;

public class AStarComparator implements Comparator<Node> {

	private Node end;

	public AStarComparator(Node end) {
		this.end = end;
	}

	public int compare(Node o1, Node o2) {
		int f1 = f(o1);
		int f2 = f(o2);
		if (f1 == f2) {
			return 0;
		}

		// Priorità più alta per chi ha f più basso.
		if (f1 > f2) {
			// 1 = primo nodo ha priorità più alta.
			return 1;
		}
		return -1;
	}

	public int f(Node n) {
		return g(n) + h(n);
	}

	/**
	 * Euristica costo dal nodo o1 alla destinazione. Usata distanza diagonale.
	 * 
	 * @param n
	 * @return
	 */
	public int h(Node n) {
		return Math.max(Math.abs(n.getX() - end.getX()), Math.abs(n.getY() - end.getY()));
		// return (int) Math.sqrt(Math.pow((n.getX() - end.getX()), 2) +
		// Math.pow((n.getY() - end.getY()), 2));
	}

	/**
	 * Costo dal nodo iniziale a quello successivo.
	 * 
	 * @param n
	 * @return
	 */
	public int g(Node n) {
		return -n.getG();
	}

}
