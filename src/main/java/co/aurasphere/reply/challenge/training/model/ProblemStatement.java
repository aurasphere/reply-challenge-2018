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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Contains the problem data. For simplicity, all fields are declared public and
 * static.
 * 
 * @author Donato Rimenti
 */
public class ProblemStatement {

	/**
	 * Represents the maximum/minimum (with inverted sign) value for the
	 * coordinates of a node.
	 */
	public static final int BOUND_CONSTRAINT = 1000000;

	/**
	 * The starting node.
	 */
	public static Node startingPoint;

	/**
	 * The ending node.
	 */
	public static Node endingPoint;

	/**
	 * The number of obstacles in the grid.
	 */
	public static int numberOfObstacles;

	/**
	 * Contains all the obstacle perimetrical points.
	 */
	public static final Set<Node> obstaclePoints = new HashSet<Node>();

	/**
	 * The list of obstacles in the grid.
	 */
	public static final List<Obstacle> obstacles = new ArrayList<Obstacle>();

}