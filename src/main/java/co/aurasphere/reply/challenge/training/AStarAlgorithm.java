/*
 * MIT License
 *
 * Copyright (c) 2018 Donato Rimenti
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package co.aurasphere.reply.challenge.training;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import co.aurasphere.reply.challenge.training.model.Node;

/**
 * A* pathfinding algorithm implementation.
 * 
 * @author Donato Rimenti
 *
 */
public class AStarAlgorithm {

	/**
	 * Cost of a diagonal step.
	 */
	public final static double DIAGONAL_COST = Math.sqrt(2);

	/**
	 * Final destination to reach with this algorithm.
	 */
	private Node target;

	/**
	 * Static weight of the heuristic function.
	 */
	private double staticWeight = 1.5;

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
		Queue<Node> openList = new PriorityQueue<Node>((n1, n2) -> Double.compare(f(n1), f(n2)));
		Map<Node, Node> openMap = new HashMap<Node, Node>();
		// Explored nodes.
		Set<Node> closedList = new HashSet<Node>();

		// Initializes the first node by forcing g to 0.
		start.setG(0);
		openList.offer(start);

		while (!openList.isEmpty()) {
			Node q = openList.poll();

			// Uncomment to enable logging.
			// System.out.println(q);

			// Main loop of the algorithm.
			for (Node successor : q.getAdjacentNodes()) {

				// If we already explored that node or we already added to the
				// "to explore" list, we just skip it.
				if (closedList.contains(successor)) {
					continue;
				}

				// Stop if we reached the goal.
				if (successor.equals(goal)) {
					return successor;
				}

				// Add the node to the list to explore if not already there.
				Node oldSuccessor = openMap.get(successor);
				if (oldSuccessor == null) {
					openList.offer(successor);
					openMap.put(successor, successor);
					continue;
				}

				// The distance from start to a neighbor.
				double tentativeGScore = g(q) + distanceBetween(q, successor);

				// This is not a better path.
				if (tentativeGScore >= g(oldSuccessor)) {
					continue;
				}

				// This path is the best until now.
				oldSuccessor.setParent(q);
				oldSuccessor.setG(tentativeGScore);
				openList.remove(oldSuccessor);
				openList.add(oldSuccessor);
			}

			// This node has been fully explored.
			closedList.add(q);
		}

		// No path has been found.
		return null;
	}

	/**
	 * Returns the distance between two nodes. The distance is defined ad
	 * {@link #DIAGONAL_COST} if the move is diagonal or 1 otherwise.
	 * 
	 * @param start the starting node
	 * @param end the ending node
	 * @return the distance between the nodes
	 */
	private double distanceBetween(Node start, Node end) {
		// Nodes are diagonal.
		if (start.x != end.x && start.y != end.y) {
			return DIAGONAL_COST;
		}
		// Nodes are not diagonal.
		return 1;
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
	private double g(Node n) {
		return n.getG();
	}

}