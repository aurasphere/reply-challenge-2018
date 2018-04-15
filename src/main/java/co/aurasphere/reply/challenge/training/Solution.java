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

import java.awt.Point;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import co.aurasphere.reply.challenge.training.model.Node;
import co.aurasphere.reply.challenge.training.model.Obstacle;
import co.aurasphere.reply.challenge.training.model.ProblemStatement;

/**
 * Main solution class for the Reply code challenge 2018 training problem.
 * 
 * @author Donato Rimenti
 *
 */
public class Solution {

	/**
	 * Solution and debugging output file.
	 */
	private static String OUTPUT_FILE = "C:\\Users\\donato\\Desktop\\output_4.txt";

	/**
	 * The main method of this class. Reads an input file into a
	 * {@link ProblemStatement}, solves it using {@link AStarAlgorithm} and
	 * prints the solution.
	 *
	 * @param args
	 *            null
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Loads the problem statement.
		parseFile("input_4.txt");

		// Redirects the system output to a file if present.
		if (OUTPUT_FILE != null) {
			System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(OUTPUT_FILE)), true,
					StandardCharsets.UTF_8.name()));
		}

		// If a terminal point of the problem is in the obstacles set, checks if
		// this is due to the 4C representation. If so, tryes to create a path
		// by converting the obstacles from 8C to 4C in the first segment. This
		// problem is visible in the ending point of the first input dataset.
		if (ProblemStatement.obstaclePoints.contains(ProblemStatement.startingPoint)) {
			if (!PathOptimizer.clearPath(ProblemStatement.startingPoint)) {
				System.out.print("IMPOSSIBLE");
				return;
			}
		}
		if (ProblemStatement.obstaclePoints.contains(ProblemStatement.endingPoint)) {
			if (!PathOptimizer.clearPath(ProblemStatement.endingPoint)) {
				System.out.print("IMPOSSIBLE");
				return;
			}
		}

		// Solves the problem.
		AStarAlgorithm solver = new AStarAlgorithm();
		Node target = solver.calculateShortestPath(ProblemStatement.startingPoint, ProblemStatement.endingPoint);

		// Clears the output from debugging results before printing the actual
		// solution.
		if (OUTPUT_FILE != null) {
			System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(OUTPUT_FILE)), true,
					StandardCharsets.UTF_8.name()));
		}

		// Prints the solution.
		if (target == null) {
			// In this case the score is 0.
			System.out.print("IMPOSSIBLE");
		} else {
			// In this case the score is 1 / numberOfNodes * 1_000_000 if the
			// solution is correct.

			// Cleans up the path applying some optimizations.
			List<Node> formattedPath = formatPathAsList(target);

			// Prints the solution.
			System.out.println(formattedPath.size());
			for (Point n : formattedPath) {
				System.out.println(n);
			}

		}
	}

	/**
	 * Converts the path into a list and performs some optimizations.
	 * 
	 * @param node
	 *            the last node of the path
	 */
	private static List<Node> formatPathAsList(Node node) {
		List<Node> formattedPath = new ArrayList<Node>();

		// Traverses back the whole path, adding each node to a list.
		Node current = node;
		while (current.getParent() != null) {
			formattedPath.add(current);
			current = current.getParent();
		}
		// Adds the last node.
		formattedPath.add(current);

		// Reverse ordering since we traversed the collection backwards.
		Collections.reverse(formattedPath);

		// Compresses the path by leaving only the turning points.
		formattedPath = PathOptimizer.compressPath(formattedPath);

		// Reduces the path by removing unnecessary points.
		formattedPath = PathOptimizer.reduce(formattedPath);

		return formattedPath;
	}

	/**
	 * Loads an input file from classpath into the {@link ProblemStatement}
	 * class.
	 * 
	 * @param fileName
	 *            the input file resource name
	 * @throws Exception
	 */
	public static void parseFile(String fileName) throws Exception {
		// Reads all the file lines.
		URI filePath = Solution.class.getClassLoader().getResource(fileName).toURI();
		List<String> fileLines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);

		// Processes each line.
		Iterator<String> it = fileLines.iterator();
		String currentLine = it.next();

		// Reads the starting and ending point.
		String[] coords = currentLine.split(" ");
		ProblemStatement.startingPoint = new Node(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
		ProblemStatement.endingPoint = new Node(Integer.parseInt(coords[2]), Integer.parseInt(coords[3]));

		// Reads the number of obstacles.
		String secondLine = it.next();
		ProblemStatement.numberOfObstacles = Integer.parseInt(secondLine);

		// Reads the obstacles. We store it both as points and whole obstacles
		// for later optimizations.
		while (it.hasNext()) {
			currentLine = it.next();
			coords = currentLine.split(" ");
			int x1 = Integer.parseInt(coords[0]);
			int y1 = Integer.parseInt(coords[1]);
			int x2 = Integer.parseInt(coords[2]);
			int y2 = Integer.parseInt(coords[3]);
			int x3 = Integer.parseInt(coords[4]);
			int y3 = Integer.parseInt(coords[5]);

			ProblemStatement.obstaclePoints.addAll(getLinePoints(x1, y1, x2, y2));
			ProblemStatement.obstaclePoints.addAll(getLinePoints(x2, y2, x3, y3));
			ProblemStatement.obstaclePoints.addAll(getLinePoints(x3, y3, x1, y1));

			Obstacle obstacle = new Obstacle(parseLineAsPoints(currentLine));
			ProblemStatement.obstacles.add(obstacle);
		}
	}

	/**
	 * Simple Bresenham's line algorithm implementation to find all the points
	 * in a line between two points and write them in 8-Connected notation. Not
	 * written by me.
	 * 
	 * @param x0
	 *            the first point x coordinate
	 * @param y0
	 *            the first point y coordinate
	 * @param x1
	 *            the second point x coordinate
	 * @param y1
	 *            the second point y coordinate
	 * @return a collection of points between the two passed as argument
	 */
	public static List<Node> getLinePoints(int x0, int y0, int x1, int y1) {
		List<Node> line = new ArrayList<Node>();

		final int dx = Math.abs(x1 - x0);
		final int dy = Math.abs(y1 - y0);
		final int totalD = dx + dy;

		final int ix = x0 < x1 ? 1 : x0 > x1 ? -1 : 0;
		final int iy = y0 < y1 ? 1 : y0 > y1 ? -1 : 0;

		int e = 0;
		int xD = x0;
		int yD = y0;
		int e1, e2;

		for (int i = 0; i < totalD; i++) {
			line.add(new Node(xD, yD));
			e1 = e + dy;
			e2 = e - dx;
			if (Math.abs(e1) < Math.abs(e2)) {
				xD += ix;
				e = e1;
			} else {
				yD += iy;
				e = e2;
			}
		}
		return line;
	}

	/**
	 * Parses a string as an unknown size collection of 2D points coordinates.
	 * The string format is assumed to be an even number of integers separed by
	 * a whitespace character.
	 * 
	 * @param line
	 *            a string representing a collection of 2D points coordinates
	 * @return a list of {@link Point}
	 */
	private static List<Point> parseLineAsPoints(String line) {
		String[] coords = line.split(" ");
		// We roughly estimate the size of the array to improve performances a
		// little.
		List<Point> points = new ArrayList<Point>(60000);
		// Step by 2 since each iteration makes a point out of 2 array elements.
		for (int i = 0; i < coords.length; i += 2) {
			points.add(new Point(Integer.parseInt(coords[i]), Integer.parseInt(coords[i + 1])));
		}
		return points;
	}
}