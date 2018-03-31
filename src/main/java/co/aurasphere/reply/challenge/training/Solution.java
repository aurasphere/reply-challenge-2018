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
	private static String OUTPUT_FILE;
	
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
			printSolution(target, 0);
		}
	}

	/**
	 * Prints the solution by traversing back the node hierarchy.
	 * 
	 * @param node
	 *            the node to traverse back
	 * @param nodeCounter
	 *            used to print the number of nodes
	 * @return the number of nodes
	 */
	private static int printSolution(Node node, int nodeCounter) {
		if (node.getParent() != null) {
			printSolution(node.getParent(), ++nodeCounter);
			System.out.println();
		} else {
			// Prints the first line (the number of nodes).
			System.out.println(nodeCounter);
		}
		System.out.print(node);

		// Returns the number of nodes.
		return nodeCounter;
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
		String firstLine = it.next();

		// Reads the starting and ending point.
		List<Point> firstLinePoints = parseLineAsPoints(firstLine);
		ProblemStatement.startingPoint = new Node(firstLinePoints.get(0));
		ProblemStatement.endingPoint = new Node(firstLinePoints.get(1));

		// Reads the number of obstacles.
		String secondLine = it.next();
		ProblemStatement.numberOfObstacles = Integer.parseInt(secondLine);

		// Reads the obstacles.
		List<Obstacle> obstacles = new ArrayList<Obstacle>();
		ProblemStatement.obstacles = obstacles;
		while (it.hasNext()) {
			String nextLine = it.next();
			Obstacle obstacle = new Obstacle(parseLineAsPoints(nextLine));
			obstacles.add(obstacle);
		}
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