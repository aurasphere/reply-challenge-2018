package co.aurasphere.reply.challenge.training;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import co.aurasphere.reply.challenge.training.model.Node;

/**
 * A* pathfinding algorithm implementation.
 * 
 * @author Donato Rimenti
 *
 */
public class AStarAlgorithm implements Comparator<Node> {

	/**
	 * Final destination to reach with this algorithm.
	 */
	private Node target;

	/**
	 * Epsilon value used for dynamic heuristic function's weight computation.
	 */
	private Double epsilon;

	/**
	 * Estimated max depth value used for dynamic heuristic function's weight
	 * computation.
	 */
	private Integer estimatedMaxDepth;

	/**
	 * Static weight of the heuristic function.
	 */
	private Double staticWeight;

	/**
	 * Instantiates a new AStarAlgorithm with weight 1 for the heuristic
	 * function.
	 */
	public AStarAlgorithm() {
	}

	/**
	 * Instantiates a new AStarAlgorithm with weight the value passed as
	 * argument for the heuristic function.
	 *
	 * @param staticWeight
	 *            the {@link #staticWeight}
	 */
	public AStarAlgorithm(double staticWeight) {
		this.staticWeight = staticWeight;
	}

	/**
	 * Instantiates a new AStarAlgorithm with dynamic weight computed with the
	 * values passed as arguments for the heuristic function.
	 *
	 * @param epsilon
	 *            the {@link #epsilon}
	 * @param estimatedMaxDepth
	 *            the {@link #estimatedMaxDepth}
	 */
	public AStarAlgorithm(double epsilon, int estimatedMaxDepth) {
		this.epsilon = epsilon;
		this.estimatedMaxDepth = estimatedMaxDepth;
	}

	/**
	 * Finds the shortest path from start to end. If a path has been found, the
	 * end node is returned. You can get the path between the two nodes by
	 * traversing them back using the {@link Node#getParent()} method.
	 * 
	 * @param start
	 *            the starting node
	 * @param end
	 *            the ending node
	 * @return the end node if a path has been found, null otherwise
	 */
	public Node calculateShortestPath(Node start, Node end) {
		this.target = end;

		// Unexplored nodes.
		Queue<Node> openList = new PriorityQueue<Node>(this);
		// Explored nodes.
		List<Node> closedList = new ArrayList<Node>();

		// Initializes the first node by forcing g to 0.
		start.setG(0);
		openList.offer(start);

		// XXX: This may cause out of memory errors but this kind of error
		// handling doesn't really fit the purpose of this project.
		while (!openList.isEmpty()) {
			Node q = openList.poll();
			System.out.println(q + " " + f(q) + " " + g(q));

			for (Node successor : q.getAdjacentNodes()) {

				// If we already explored that node or we already added to the
				// "to explore" list, we just skip it.
				if (closedList.contains(successor)) {
					continue;
				}

				// Stop if we reached the end.
				if (successor.equals(end)) {
					end.setParent(successor);
					end.setG(successor.getG() + 1);
					return end;
				}

				Node sameNode = getSameNode(openList, successor);
				// Skip this node if we have a better one.
				if (sameNode != null) {
					if (successor.getG() >= sameNode.getG()) {
						continue;
					}

					// Otherwise, this is a better node let's remove the other
					// one.
					openList.remove(sameNode);
				}

				openList.offer(successor);

			}
			// This node has been fully explored.
			closedList.add(q);
		}

		// No path has been found.
		return null;
	}

	private Node getSameNode(Queue<Node> openList, Node sameNode) {
		for (Node node : openList) {
			if (node.equals(sameNode)) {
				return sameNode;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Node o1, Node o2) {
		// Use f() function and delegation.
		return Double.compare(f(o1), f(o2));
	}

	/**
	 * Returns the fitness for a node n. The lowest the fitness, the best is the
	 * node.
	 * 
	 * @param n
	 *            the node whose fitness value needs to be computed
	 * @return the fitness score for the node n
	 */
	private double f(Node n) {
		return g(n) + w(n) * h(n);
	}

	/**
	 * Returns the weight of the heuristic function for a node.
	 * 
	 * @param n
	 *            the node whose heuristic weight needs to be computed
	 * @return the weight of the heuristic function for a node
	 */
	private double w(Node n) {
		// Dynamic weight
		if (epsilon != null && estimatedMaxDepth != null)
			return 1 + epsilon - (epsilon * d(n) / estimatedMaxDepth);
		// Static weight
		if (staticWeight != null)
			return staticWeight;
		// No weight
		return 1;
	}

	/**
	 * Returns the depth of the current search.
	 * 
	 * @param n
	 *            the node whose depth needs to be computed
	 * @return the depth of the current search
	 */
	private int d(Node n) {
		// We use g(n) as a depth estimator.
		return g(n);
	}

	/**
	 * Returns the estimated cost to reach the ending node from the node n using
	 * an heuristic.
	 * 
	 * @param n
	 *            the node whose cost to reach the end needs to be estimated
	 * @return the estimated cost to reach the end from the node n
	 */
	private int h(Node n) {
		// We use diagonal distance as our heuristic since we can move in 8
		// different directions.
		return Math.max(Math.abs(n.x - target.x), Math.abs(n.y - target.y));
	}

	/**
	 * Returns the cost from the starting node to the node n.
	 * 
	 * @param n
	 *            the node up to where the distance from start needs to be
	 *            computed
	 * @return the distance between the starting node and the node n
	 */
	private int g(Node n) {
		return n.getG();
	}

}
