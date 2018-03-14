package co.aurasphere.reply.challenge.shortestpath;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.aurasphere.reply.challenge.shortestpath.model.ProblemStatement;
import co.aurasphere.reply.challenge.shortestpath.model.Triangle;

public class IOUtils {
	
	
	
	public static List<String> readWholeFile(String file) throws IOException {
		List<String> fileContent = new ArrayList<String>(20000);
		String filePath = IOUtils.class.getClassLoader().getResource(file).getPath();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

		while (bufferedReader.ready()) {
			fileContent.add(bufferedReader.readLine());
		}
		bufferedReader.close();	
		return fileContent;
	}
	
	public static ProblemStatement parseFile(String fileName) throws IOException {
		List<String> fileLines = readWholeFile(fileName);
		Iterator<String> it = fileLines.iterator();
		String firstLine = it.next();
		
		List<Point> firstLinePoints = parseLineAsPoints(firstLine);
		Point startingPoint = firstLinePoints.get(0);
		Point endingPoint = firstLinePoints.get(1);
		ProblemStatement.startingPoint = startingPoint;
		ProblemStatement.endingPoint = endingPoint;
		
		String secondLine = it.next();
		ProblemStatement.numberOfObstacles = Integer.parseInt(secondLine);
		
		List<Triangle> obstacles = new ArrayList<Triangle>();
		ProblemStatement.obstacles = obstacles;
		
		while (it.hasNext()) {
			String nextLine = it.next();
			Triangle triangle = new Triangle(parseLineAsPoints(nextLine));
			obstacles.add(triangle);
		}
		
//		int[][] matrix = new int[1000000][1000000];
//		for (int i = 0; i < 1000000; i++) {
//			for (int j = 0; j < 1000000; j++) {
//				matrix[i][j] = 1;
//			}
//		}
		return null;
	}

	private static List<Point> parseLineAsPoints(String line) {
		String[] coords = line.split(" ");
		List<Point> points = new ArrayList<Point>(60000);
		for (int i = 0; i < coords.length; i += 2) {
			points.add(new Point(Integer.parseInt(coords[i]),Integer.parseInt(coords[i+1])));
		}
		return points;
	}
	
}
