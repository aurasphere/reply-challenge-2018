package co.aurasphere.reply.challenge.training;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
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
	 * Static weight of the heuristic function.
	 */
	private double staticWeight = 10.0;

	/**
	 * If true, each visited node coordinates are logged.
	 */
	private boolean debug = true;

	/**
	 * Max number of iteration the algorithm is allowed to perform before
	 * returning a negative response.
	 */
	private int maxNumberOfSteps = 170_000;

	/**
	 * Finds the shortest path from start to end. If a path has been found, the
	 * end node is returned. You can get the path between the two nodes by
	 * traversing them back using the {@link Node#getParent()} method.
	 * 
	 * @param start
	 *            the starting node
	 * @param goal
	 *            the ending node
	 * @return the end node if a path has been found, null otherwise
	 */
	public Node calculateShortestPath(Node start, Node goal) {
		this.target = goal;

		// Unexplored nodes.
		Queue<Node> openList = new PriorityQueue<Node>(this);
		// Explored nodes.
		List<Node> closedList = new ArrayList<Node>();

		// Initializes the first node by forcing g to 0.
		start.setG(0);
		openList.offer(start);

		int currentStep = 0;
		while (!openList.isEmpty() && currentStep < maxNumberOfSteps) {
			Node q = openList.poll();

			// Logging.
			if (debug) {
				System.out.println(q);
			}
			// Main loop of the algorithm.
			mainLoop: for (Node successor : q.getAdjacentNodes()) {

				// If we already explored that node or we already added to the
				// "to explore" list, we just skip it.
				if (closedList.contains(successor)) {
					continue;
				}

				// Stop if we reached the goal.
				if (successor.equals(goal)) {
					return successor;
				}

				// If we have already found this node we will keep the best
				// between the two.
				if (openList.contains(successor)) {
					Iterator<Node> iterator = openList.iterator();
					while (iterator.hasNext()) {
						Node oldNode = iterator.next();
						if (oldNode.equals(successor)) {
							// Skip this node if we have a better one.
							if (g(successor) >= g(oldNode)) {
								continue mainLoop;
							}

							// Otherwise, this is a better node let's remove the
							// other one.
							iterator.remove();
						}
					}

				}

				// Add the node to the list to explore.
				openList.offer(successor);

			}
			// This node has been fully explored.
			closedList.add(q);
			currentStep++;
		}

		// No path has been found.
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
		// Static weight
		return staticWeight;
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
		// We use Chebyshev distance (or diagonal distance) as our heuristic
		// since we can move in 8 different directions.
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