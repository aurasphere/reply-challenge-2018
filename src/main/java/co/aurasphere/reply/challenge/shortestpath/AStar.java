package co.aurasphere.reply.challenge.shortestpath;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.commons.collections.comparators.ReverseComparator;

import co.aurasphere.reply.challenge.shortestpath.model.Node;

public class AStar implements PathSolver {
	
	private Queue<Node> openList;
	
	private Queue<Node> closeList;

	public Node calculateShortestPath(Node start, Node end) {
		AStarComparator comp = new AStarComparator(end);
		// Lista nodi da esplorare.
		openList = new PriorityQueue<Node>(comp);
		// Lista nodi esplorati.
		closeList = new PriorityQueue<Node>(comp);

		start.setG(0);
		openList.add(start);
		
		// TODO: qui serve un limite per non andare in out of memory.
		while (!openList.isEmpty()) {
			Node q = openList.poll();
			System.out.println(q.getX() + " " + q.getY());
			
			for (Node successor : q.getAdjacentNodes()) {
				Node topOpenList = openList.peek();
				Node topCloseList = closeList.peek();
				successor.setG(q.getG() + 1);
				
				if (closeList.contains(successor)){
					continue;
				}
				
				// check fine raggiunta
				if (end.equals(successor)) {
					end.setPreviousNode(successor);
					end.setG(successor.getG() + 1);
					return end;
				}
				
				// Se questo è il miglior candidato, lo aggiungo alla lista da esplorare.
//				int g = comp.g(successor);
//				int lowestOpenListG = topOpenList == null ? Integer.MAX_VALUE : comp.g(topOpenList);
//				int lowestCloseListG = topCloseList == null ? Integer.MAX_VALUE : comp.g(topCloseList);
//				if (g <= lowestOpenListG) {
					openList.add(successor);
//				}
			
			}
			// Questo nodo è stato esplorato.
			closeList.add(q);
		}
		
		// Nessun percorso trovato.
		return null;
	}

}
