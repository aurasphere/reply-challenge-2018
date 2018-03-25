package co.aurasphere.reply.challenge.training.model;

import java.util.List;

/**
 * Contains the problem data. For simplicity, all fields are declared public and
 * static.
 * 
 * @author Donato Rimenti
 */
public class ProblemStatement {

	/**
	 * Represents the maximum/minimum (with inverted sign) value for the coordinates of a node.
	 */
	public final static int BOUND_CONSTRAINT = 1000000;

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
	 * The list of obstacles in the grid.
	 */
	public static List<Obstacle> obstacles;

}
