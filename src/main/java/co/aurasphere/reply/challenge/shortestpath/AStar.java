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
			
			for (Node successor : q.getAdjacentNodes()) {
				Node topOpenList = openList.peek();
				Node topCloseList = closeList.peek();
				
				// check fine raggiunta
				if (end.equalsNode(successor)) {
					end.setPreviousNode(successor);
					end.setG(successor.getG() + 1);
					return end;
				}
				
				// Se questo è il miglior candidato, lo aggiungo alla lista da esplorare.
				int f = comp.f(successor);
				int lowestOpenListF = topOpenList == null ? Integer.MAX_VALUE : comp.f(topOpenList);
				int lowestCloseListF = topCloseList == null ? Integer.MAX_VALUE : comp.f(topCloseList);
				if (f <= lowestOpenListF &&  f <= lowestCloseListF) {
					openList.add(successor);
				}
			
			}
			// Questo nodo è stato esplorato.
			closeList.add(q);
		}
		
		// Nessun percorso trovato.
		return null;
	}

}
