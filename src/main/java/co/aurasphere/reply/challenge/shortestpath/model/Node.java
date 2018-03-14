package co.aurasphere.reply.challenge.shortestpath.model;

import java.util.ArrayList;
import java.util.List;

public class Node {
	
	private final static int BOUND = 1000000;
	
	private Node previousNode;

	private int x;

	private int y;

	private int g;

	// Lazy init per evitare out of memory.
	private List<Node> adjacentNodes;
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		// Esplicito solo per facilità di lettura.
		this.g = 0;
	}

	public Node(int x, int y, int g) {
		this.x = x;
		this.y = y;
		this.g = g;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public List<Node> getAdjacentNodes() {
		if (adjacentNodes == null) {
			adjacentNodes = new ArrayList<Node>();

			// Genera i nuovi nodi.
			// 1 2 3
			// 4 O 6
			// 7 8 9

			// 1
			if (x != -BOUND && y != BOUND && isValidPoint(x - 1, y + 1)) {
				Node node = new Node(x - 1, y + 1, g + 1);
				adjacentNodes.add(node);
				node.setPreviousNode(this);
			}
			// 2
			if (y != BOUND && isValidPoint(x, y + 1)) {
				Node node = new Node(x, y + 1, g + 1);
				adjacentNodes.add(node);
				node.setPreviousNode(this);
			}
			// 3
			if (x != BOUND && y != BOUND && isValidPoint(x + 1, y + 1)) {
				Node node = new Node(x + 1, y + 1, g + 1);
				adjacentNodes.add(node);
				node.setPreviousNode(this);
			}
			// 4
			if (x != -BOUND && isValidPoint(x - 1, y)) {
				Node node = new Node(x - 1, y, g + 1);
				adjacentNodes.add(node);
				node.setPreviousNode(this);
			}
			// 5 sono io, non lo aggiungo.
			// 6
			if (x != BOUND && isValidPoint(x + 1, y)) {
				Node node = new Node(x + 1, y, g + 1);
				adjacentNodes.add(node);
				node.setPreviousNode(this);
			}
			// 7
			if (x != -BOUND && y != -BOUND && isValidPoint(x - 1, y - 1)) {
				Node node = new Node(x - 1, y - 1, g + 1);
				adjacentNodes.add(node);
				node.setPreviousNode(this);
			}
			// 8
			if (y != -BOUND && isValidPoint(x, y - 1)) {
				Node node = new Node(x, y - 1, g + 1);
				adjacentNodes.add(node);
				node.setPreviousNode(this);
			}
			// 9
			if (x != BOUND && y != -BOUND && isValidPoint(x + 1, y - 1)) {
				Node node = new Node(x + 1, y - 1, g + 1);
				adjacentNodes.add(node);
				node.setPreviousNode(this);
			}
		}
		return adjacentNodes;
	}

	private boolean isValidPoint(int x, int y) {
		for (Triangle t : ProblemStatement.obstacles) {
			if (t.isPointInside(x, y)) {
				return false;
			}
		}
		return true;
	}

	public void setAdjacentNodes(List<Node> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}

	public Node getPreviousNode() {
		return previousNode;
	}

	public void setPreviousNode(Node previousNode) {
		this.previousNode = previousNode;
	}
	
	public boolean equalsNode(Node node) {
		return this.x == node.x && this.y == node.y;
	}
}