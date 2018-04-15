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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import co.aurasphere.reply.challenge.training.model.Node;
import co.aurasphere.reply.challenge.training.model.Obstacle;
import co.aurasphere.reply.challenge.training.model.ProblemStatement;

/**
 * Class for specific optimizations on a path.
 * 
 * @author Donato Rimenti
 *
 */
public class PathOptimizer {

	/**
	 * Private constructor for utility class.
	 */
	private PathOptimizer() {
	}

	/**
	 * Reduces a path to the minimum number of points needed. Each of this
	 * points represents an extrema of a segment on the path.
	 * 
	 * @param points
	 *            the path to compress
	 * @return the compressed path
	 */
	public static List<Node> compressPath(List<Node> points) {
		List<Node> compressedPath = new ArrayList<Node>();

		Iterator<Node> pointIterator = points.iterator();
		Node previous = pointIterator.next();
		int currentXDirection = 0;
		int currentYDirection = 0;
		while (pointIterator.hasNext()) {
			Node current = pointIterator.next();
			// Checks for any direction changes by looking at the coordinates of
			// the current and previous node.
			int tmpXDirection = Double.compare(previous.x, current.x);
			int tmpYDirection = Double.compare(previous.y, current.y);

			// If the direction changes, the previous is a new point of the
			// compression.
			if (tmpXDirection != currentXDirection || tmpYDirection != currentYDirection) {
				compressedPath.add(previous);
				currentXDirection = tmpXDirection;
				currentYDirection = tmpYDirection;
			}

			// Moves to the next point.
			previous = current;
		}
		compressedPath.add(previous);
		return compressedPath;
	}

	/**
	 * Reduces a path length by removing unnecessary nodes. A node is defined
	 * unnecessary if there is a non obstructed path between a previous node and
	 * a successor node.
	 * 
	 * @param points
	 *            the path to reduce
	 * @return a reduced path
	 */
	public static List<Node> reduce(List<Node> points) {
		List<Node> reducedPath = new ArrayList<Node>();

		// Iterates all the points from the first.
		for (int i = 0; i < points.size(); i++) {
			Node fromStart = points.get(i);

			// Iterates all the points from the last.
			middle: for (int j = points.size() - 1; j > i; j--) {
				Node fromEnd = points.get(j);

				// If there's an obstacle between the two points, there's no
				// path between the points. Let's consider the next one from the
				// end.
				for (Obstacle o : ProblemStatement.obstacles) {
					if (o.isPathObstructed(fromStart, fromEnd)) {
						continue middle;
					}
				}
				// No obstacles if here.
				reducedPath.add(fromStart);
				reducedPath.add(fromEnd);
				// Let's jump to the next point. At the next iteration i will
				// increase again so we go to the previous point.
				i = j - 1;

			}
		}
		return reducedPath;
	}

	/**
	 * Checks if there's a clear path available to a node. If not, tries to make
	 * one by converting a node representation from 4C to 8C.
	 * 
	 * @param terminalNode
	 *            the node whose path needs to be clear
	 * @return false if this problem doesn't have any solution, true if it may
	 *         be solvable (not guaranteed though)
	 */
	public static boolean clearPath(Node terminalNode) {
		// First of all we check that the point is not within any obstacle. If
		// it is, the problem doesn't have any solution.
		for (Obstacle obstacle : ProblemStatement.obstacles) {
			if (obstacle.isPointInside(terminalNode.x, terminalNode.y)) {
				// This problem doesn't have any solution.
				return false;
			}
		}

		// We add the first node to the next nodes to visit.
		Set<Node> visitedNodes = new HashSet<Node>();
		LinkedList<Node> nextNodes = new LinkedList<Node>();
		nextNodes.add(terminalNode);

		// If there's more than just one node, the path to the node is open.
		while (nextNodes.size() == 1) {
			Node currentNode = nextNodes.poll();

			// This node is not within any obstacles.
			ProblemStatement.obstaclePoints.remove(currentNode);

			// Current point neighbors.
			List<Node> currentNeighbors = Arrays.asList(new Node(currentNode.x - 1, currentNode.y - 1),
					new Node(currentNode.x, currentNode.y - 1), new Node(currentNode.x + 1, currentNode.y - 1),
					new Node(currentNode.x - 1, currentNode.y), new Node(currentNode.x + 1, currentNode.y),
					new Node(currentNode.x - 1, currentNode.y + 1), new Node(currentNode.x, currentNode.y + 1),
					new Node(currentNode.x + 1, currentNode.y + 1));

			// Adds all the neighbors nodes not already visited to the "to
			// visit" list.
			currentNeighbors.forEach(node -> {
				if (!visitedNodes.contains(node)) {
					nextNodes.add(node);
				}
			});

			// If any of the current node's neighbors is obstructed by
			// an obstacle is removed from the "to visit" list.
			for (Obstacle obstacle : ProblemStatement.obstacles) {
				currentNeighbors.forEach(node -> {
					if (obstacle.isPathObstructed(node, currentNode)) {
						ProblemStatement.obstaclePoints.add(node);
						nextNodes.remove(node);
					}
				});

			}
			// This node has been fully explored.
			visitedNodes.add(currentNode);
		}

		// An optimization may have been happened.
		return true;
	}

}
