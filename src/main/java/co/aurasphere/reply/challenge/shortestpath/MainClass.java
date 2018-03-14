package co.aurasphere.reply.challenge.shortestpath;

import java.awt.Point;
import java.io.IOException;

import co.aurasphere.reply.challenge.shortestpath.model.Node;
import co.aurasphere.reply.challenge.shortestpath.model.ProblemStatement;

public class MainClass {
	
	private static PathSolver solver = new AStar();

	public static void main(String[] args) throws IOException {
		IOUtils.parseFile("input_1.txt");
		Point startingPoint = ProblemStatement.startingPoint;
		Point endingPoint = ProblemStatement.endingPoint;
		System.out.println(startingPoint);
		System.out.println(endingPoint);
		Node start = new Node((int) startingPoint.getX(), (int) startingPoint.getY());
		Node end = new Node((int) endingPoint.getX(), (int) endingPoint.getY());
		solver.calculateShortestPath(start, end);
		System.out.println(end.getG());
	}
}