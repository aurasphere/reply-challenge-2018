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
package co.aurasphere.reply.challenge.training.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import co.aurasphere.reply.challenge.training.AStarAlgorithm;

/**
 * Single node used by this algorithm. The whole data structure is made up by a
 * node element with no {@link #parent} (the root) connected with others through
 * the {@link #adjacentNodes} field.
 * 
 * @author Donato Rimenti
 */
public class Node extends Point {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The parent node of this one. If this is null, this is the root node.
	 */
	private Node parent;

	/**
	 * G score of this node, which represents the cost from the starting node to
	 * this one.
	 */
	private double g;

	/**
	 * The nodes reachable from this one. To avoid out of memory errors, they
	 * are lazy initialized.
	 */
	private List<Node> adjacentNodes;

	/**
	 * Instantiates a new Node.
	 *
	 * @param x
	 *            {@link Point#x}
	 * @param y
	 *            {@link Point#y}
	 */
	public Node(int x, int y) {
		super(x, y);
	}

	/**
	 * Instantiates a new Node.
	 *
	 * @param x
	 *            {@link Point#x}
	 * @param y
	 *            {@link Point#y}
	 * @param parent
	 *            the {@link #parent}
	 */
	public Node(int x, int y, Node parent) {
		super(x, y);
		this.g = parent.g + (parent.x != x && parent.y != y ? AStarAlgorithm.DIAGONAL_COST : 1);
		this.parent = parent;
	}

	/**
	 * Gets the {@link #g}.
	 *
	 * @return the {@link #g}
	 */
	public double getG() {
		return g;
	}

	/**
	 * Sets the {@link #g}.
	 *
	 * @param g
	 *            the new {@link #g}
	 */
	public void setG(double g) {
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

			// Lazy init of new nodes to avoid out of memory errors. For
			// reference, here's a visual representation of the neighbour nodes
			// of a node X:
			// 1 2 3
			// 4 X 6
			// 7 8 9

			// 1
			addIfValid(new Node(x - 1, y + 1, this));
			// 2
			addIfValid(new Node(x, y + 1, this));
			// 3
			addIfValid(new Node(x + 1, y + 1, this));
			// 4
			addIfValid(new Node(x - 1, y, this));
			// 6
			addIfValid(new Node(x + 1, y, this));
			// 7
			addIfValid(new Node(x - 1, y - 1, this));
			// 8
			addIfValid(new Node(x, y - 1, this));
			// 9
			addIfValid(new Node(x + 1, y - 1, this));
		}
		return adjacentNodes;
	}

	/**
	 * Checks that a pair of coordinates are not inside an obstacle and within
	 * the grid boundary.
	 *
	 * @param x
	 *            the x of the point to check
	 * @param y
	 *            the y of the point to check
	 * @return true if the point is not inside an obstacle, false otherwise
	 */
	public boolean addIfValid(Node n) {
		// Checks that there's no obstacle obstructing the path.
		if (ProblemStatement.obstaclePoints.contains(n)) {
			return false;
		}

		// Checks that the point is within the boundary.
		if (n.x < -ProblemStatement.BOUND_CONSTRAINT || n.x > ProblemStatement.BOUND_CONSTRAINT
				|| n.y < -ProblemStatement.BOUND_CONSTRAINT || n.y > ProblemStatement.BOUND_CONSTRAINT) {
			return false;
		}

		// This point is valid.
		adjacentNodes.add(n);
		return true;
	}

	/**
	 * Gets the {@link #parent}.
	 *
	 * @return the {@link #parent}
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * Sets the {@link #parent}.
	 *
	 * @param parent
	 *            the new {@link #parent}
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.geom.Point2D#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Point#equals(java.lang.Object)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Point#toString()
	 */
	@Override
	public String toString() {
		return x + " " + y;
	}

}