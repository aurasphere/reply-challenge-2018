package co.aurasphere.reply.challenge.shortestpath;

import java.util.List;

import co.aurasphere.reply.challenge.shortestpath.model.Node;

public interface PathSolver {
	
	public Node calculateShortestPath(Node start, Node end);

}
