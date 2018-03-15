package co.aurasphere.reply.challenge.shortestpath.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Node.
 */
public class Node {
	
	/**
	 * The Constant BOUND.
	 */
	private final static int BOUND = 1000000;
	
	/**
	 * The previous node.
	 */
	private Node previousNode;

	/**
	 * The x.
	 */
	private int x;

	/**
	 * The y.
	 */
	private int y;

	/**
	 * The g.
	 */
	private int g;

	/**
	 * The adjacent nodes.
	 */
	// Lazy init per evitare out of memory.
	private List<Node> adjacentNodes;
	
	/**
	 * Instantiates a new Node.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		// Esplicito solo per facilità di lettura.
		this.g = 0;
	}

	/**
	 * Instantiates a new Node.
	 *
	 * @param x the x
	 * @param y the y
	 * @param g the g
	 */
	public Node(int x, int y, int g) {
		this.x = x;
		this.y = y;
		this.g = g;
	}

	/**
	 * Gets the {@link #x}.
	 *
	 * @return the {@link #x}
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the {@link #x}.
	 *
	 * @param x the new {@link #x}
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the {@link #y}.
	 *
	 * @return the {@link #y}
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the {@link #y}.
	 *
	 * @param y the new {@link #y}
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Gets the {@link #g}.
	 *
	 * @return the {@link #g}
	 */
	public int getG() {
		return g;
	}

	/**
	 * Sets the {@link #g}.
	 *
	 * @param g the new {@link #g}
	 */
	public void setG(int g) {
		this.g = g;
	}

	/**
	 * Gets the {@link #adjacentNodes}.
	 *
	 * @return the {@link #adjacentNodes}
	 */
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

	/**
	 * Gets the {@link #validPoint}.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the {@link #validPoint}
	 */
	private boolean isValidPoint(int x, int y) {
		for (Triangle t : ProblemStatement.obstacles) {
			if (t.isPointInsideWithBaricentricCoords(x, y)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Sets the {@link #adjacentNodes}.
	 *
	 * @param adjacentNodes the new {@link #adjacentNodes}
	 */
	public void setAdjacentNodes(List<Node> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}

	/**
	 * Gets the {@link #previousNode}.
	 *
	 * @return the {@link #previousNode}
	 */
	public Node getPreviousNode() {
		return previousNode;
	}

	/**
	 * Sets the {@link #previousNode}.
	 *
	 * @param previousNode the new {@link #previousNode}
	 */
	public void setPreviousNode(Node previousNode) {
		this.previousNode = previousNode;
	}
	
	/**
	 * Equals node.
	 *
	 * @param node the node
	 * @return true, if successful
	 */
	public boolean equalsNode(Node node) {
		return this.x == node.x && this.y == node.y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "x=" + x + ", y=" + y;
	}

}