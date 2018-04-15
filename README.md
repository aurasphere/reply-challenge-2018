# Reply Code Challenge 2018 Training

This repository contains both the problem statement and my solution to the Reply Code Challenge training problem. The solution is written in Java.

<s> **Note:** since the method which prints the final solution uses recursion to print the visited nodes by traversing back the last one, you may get a StackOverflowError. To avoid that, you should increase your stack size using the <code>-Xss</code> argument when starting the JVM. I've run this code with <code>-Xss515m</code> and it worked fine.</s>

## Log

The problem statement required to find the shortest path from point A to point B, so I've started looking into pathfinding algorithms, in particular [Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) since it was the only one I knew at the time.

### Dijkstra

It usually requires a graph to run Dijkstra but I couldn't afford to build the whole grid because of its huge size, hence, I've used a discovery method that, at each iteration, would lazily compute and add the valid neighbour nodes of the current one to the "to visit" list:

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
            if (isValid(x - 1, y + 1)) {
                new Node(x - 1, y + 1, this);
            }
            // 2
            if (isValid(x, y + 1)) {
                new Node(x, y + 1, this);
            }
            // 3
            if (isValid(x + 1, y + 1)) {
                new Node(x + 1, y + 1, this);
            }
            // 4
            if (isValid(x - 1, y)) {
                new Node(x - 1, y, this);
            }
            // 6
            if (isValid(x + 1, y)) {
                new Node(x + 1, y, this);
            }
            // 7
            if (isValid(x - 1, y - 1)) {
                new Node(x - 1, y - 1, this);
            }
            // 8
            if (isValid(x, y - 1)) {
                new Node(x, y - 1, this);
            }
            // 9
            if (isValid(x + 1, y - 1)) {
                new Node(x + 1, y - 1, this);
            }
        }
        return adjacentNodes;
    }

Here's a visual representation of Dijkstra's method:

<p align="center"><img alt="Dijkstra 1" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/dijkstra_1.png" width="300px" height="300px" align="middle">&nbsp;&nbsp;&nbsp;->&nbsp;&nbsp;&nbsp;<img alt="Dijkstra 2" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/dijkstra_2.png" width="300px" height="300px" align="middle"></p>

### Nodes Validations

To be valid, a node needed not to be within an obstacle, a 3-tuple of points which defines a triangle, and inside the grid boundary of 10<sup>-6</sup> and 10<sup>6</sup> on both axis. Certainly, the latter validation was trivial to implement but the former was not that difficult either when considering that I was working with triangles and taking advantage of the [baricentric coordinates](https://stackoverflow.com/a/2049712/4921205):

    public boolean isPointInside(long x, long y) {
        long s = a.y * c.x - a.x * c.y + (c.y - a.y) * x + (a.x - c.x) * y;
        long t = a.x * b.y - a.y * b.x + (a.y - b.y) * x + (b.x - a.x) * y;
    
        if ((s < 0) != (t < 0)) {
            return false;
        }
    
        long A = -b.y * c.x + a.y * (c.x - b.x) + a.x * (b.y - c.y) + b.x * c.y;
        if (A < 0.0) {
            s = -s;
            t = -t;
            A = -A;
        }
        return s > 0 && t > 0 && (s + t) <= A;
    }

Later on, I've also added a new validation to check whether a path between nodes would cross an obstacle. This could have happened in case they were neighbours even if none of them was inside the obstacles themselves like in the following example. It was easy to fix this using the Java 2D API and intersections. Here are the code and two examples showing after and before the check:

    public boolean isPathObstructed(Point origin, Point destination) {
        Line2D pathToCheck = new Line2D.Float(origin, destination);

        return firstSegment.intersectsLine(pathToCheck) || secondSegment.intersectsLine(pathToCheck)
                || thirdSegment.intersectsLine(pathToCheck);
    }

<p align="center"><img alt="Before" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/illegal_crossing_1.png" width="300px" height="300px" align="middle">&nbsp;&nbsp;&nbsp;->&nbsp;&nbsp;&nbsp;<img alt="After" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/illegal_crossing_2.png" width="300px" height="300px" align="middle"></p>

All of this checks were performed by iterating all the obstacles each time we needed to explore a node. Although I didn't think that would work both due to performance and memory limitations, I resisted the urge to perform any premature optimization and I gave it a shot instead. It actually worked, to my surprise, and it was fast enough so I never really looked into a way to improve this:

    public boolean isValid(int x, int y) {
        // Checks that the point is within the boundary.
        if (x < -ProblemStatement.BOUND_CONSTRAINT || x > ProblemStatement.BOUND_CONSTRAINT
                || y < -ProblemStatement.BOUND_CONSTRAINT || y > ProblemStatement.BOUND_CONSTRAINT) {
            return false;
        }

        for (Obstacle t : ProblemStatement.obstacles) {
            // Checks that the point is not within an obstacle.
            if (t.isPointInside(x, y)) {
                return false;
            }
            
            // Checks that there's no obstacle obstructing the path.
            if (t.isPathObstructed(this, new Point(x, y))){
                return false;
            }
        }
        
        // This point is valid.
        return true;
    }

### A*

Unluckily, Dijkstra method quickly proved to be too slow even for the smallest input dataset, therefore I switched over to a plain implementation of the [A* algorithm](https://en.wikipedia.org/wiki/A*_search_algorithm). 

The new solution was basically an improved version of the first one: Dijkstra's fitness function only took into account the node cost *g()* but A* also used a heuristic function *h()* to select only the nodes that moved towards the goal. The heuristic function used was the [Chebyshev distance](https://en.wikipedia.org/wiki/Chebyshev_distance) formula because it allows movement in 8 directions:

    private int h(Node n) {
        return Math.max(Math.abs(n.x - target.x), Math.abs(n.y - target.y));
    }

The whole A* algorithm was the following:

	public Node calculateShortestPath(Node start, Node goal) {
		this.target = goal;

		// Unexplored nodes.
		Queue<Node> openList = new PriorityQueue<Node>((n1, n2) -> Double.compare(f(n1), f(n2)));
		Map<Node, Node> openMap = new HashMap<Node, Node>();
		// Explored nodes.
		Set<Node> closedList = new HashSet<Node>();

		// Initializes the first node by forcing g to 0.
		start.setG(0);
		openList.offer(start);

		while (!openList.isEmpty()) {
			Node q = openList.poll();

			// Uncomment to enable logging.
			// System.out.println(q);

			// Main loop of the algorithm.
			for (Node successor : q.getAdjacentNodes()) {

				// If we already explored that node or we already added to the
				// "to explore" list, we just skip it.
				if (closedList.contains(successor)) {
					continue;
				}

				// Stop if we reached the goal.
				if (successor.equals(goal)) {
					return successor;
				}

				// Add the node to the list to explore if not already there.
				Node oldSuccessor = openMap.get(successor);
				if (oldSuccessor == null) {
					openList.offer(successor);
					openMap.put(successor, successor);
					continue;
				}

				// The distance from start to a neighbor.
				double tentativeGScore = g(q) + distanceBetween(q, successor);

				// This is not a better path.
				if (tentativeGScore >= g(oldSuccessor)) {
					continue;
				}

				// This path is the best until now.
				oldSuccessor.setParent(q);
				oldSuccessor.setG(tentativeGScore);
				openList.remove(oldSuccessor);
				openList.add(oldSuccessor);
			}

			// This node has been fully explored.
			closedList.add(q);
		}

		// No path has been found.
		return null;
	}

Here's a visual representation of how the A* algorithm moved towards the goal:

<p align="center"><img alt="A* 1" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/astar_1.png" width="300px" height="300px" align="middle">&nbsp;&nbsp;&nbsp;->&nbsp;&nbsp;&nbsp;<img alt="A* 2" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/astar_2.png" width="300px" height="300px" align="middle"></p>
<p align="center"><img alt="A* 3" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/astar_3.png" width="300px" height="300px" align="middle">&nbsp;&nbsp;&nbsp;->&nbsp;&nbsp;&nbsp;<img alt="A* 4" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/astar_4.png" width="300px" height="300px" align="middle"></p>
<p align="center"><img alt="A* 5" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/astar_5.png" width="300px" height="300px" align="middle"></p>

### Bounded Relaxation

Although the A* proved to be much quicker and efficient than Dijkstra, it still took a very long time to find the best path between the two nodes and I was still testing with the smallest dataset. At this point, I just decided that finding a solution would have been better than trying to find the best solution and this ultimately led me to accept a tradeoff between optimality and performances.

With this in mind, I've added the concept of [bounded relaxation](https://en.wikipedia.org/wiki/Relaxation_(approximation)) inside the algorithm by adding a static weight *w()* to the heuristic *h()* when computing the fitness function *f()*:

    private double f(Node n) {
        return g(n) + w(n) * h(n);
    }

Here's a depiction of the improved algorithm with a weight value of 10.0. The last picture shows how a path would be traced when a solution would have been found to highlight the performed tradeoff (in this dataset the final node is actually obstructed so that is not a correct solution):

<p align="center"><img alt="A* with bounded relaxation 1" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/bounded_relaxation_10_1.png" width="300px" height="300px" align="middle">&nbsp;&nbsp;&nbsp;->&nbsp;&nbsp;&nbsp;<img alt="A* with bounded relaxation 2" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/bounded_relaxation_10_2.png" width="300px" height="300px" align="middle"></p>
<p align="center"><img alt="A* with bounded relaxation 3" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/bounded_relaxation_10_final.png" width="300px" height="300px" align="middle"></p>

### Limitations

When working with the first dataset, I've stumbled upon the situation in which the goal node was obstructed. The way I coped with that was by limiting the maximum number of nodes that could be visited. Although this prevented the algorithm to go on forever if the goal was unreachable, it also meant that the algorithm would yield a negative result for a feasible path that required to explore more nodes than this threshold.

Of course, there are plenty of better and cleaner solutions like running the algorithm in a different thread with a timeout or checking this limit relative to a node, but this was the cheapest to implement so I just went for it. 

To give a little more insight, here is a visual representation of both the problem I wanted to avoid and the algorithm dealing with it by going on exploring until the threshold is reached after 170.000 nodes explored:

<p align="center"><img alt="Obstructed node" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/obstructed_node.jpg" width="300px" height="300px" align="middle"> <img alt="Obstructed node stalemate" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/obstructed_node_stalemate.png" width="300px" height="300px" align="middle"></p>

### (Almost) Total Rework

After some time, I gave this problem another shoot and improved the solution a whole lot with some optimizations.

First of all, I made the A* algorithm implementation much lighter by caching all the obstacles perimetral points instead of checking each time with a mathematical formula. This sacrified some memory but I could afford that and so I started the program with a heap size of 10 GB (<code>-Xmx10g</code>). This was the new validity check (after a bit of refactoring):

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

To add the nodes inside the set, I used a custom version of the [Bresenham's lines Algorithm](https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm) which extracted each perimetral point of the obstacles [in a 4-connected representation instead of the classic 8-connected](https://en.wikipedia.org/wiki/Pixel_connectivity), in order to prevent the nodes cutting through obstacle's problem:

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

After some testing, I've noticed that altough not stated anywhere, the solution score was not based on the number of nodes but on the real distance. Initially, the distance between any nodes and its neihbors was always 1 but since we were now considering real distances, I've added a check to set the distance equal to <code>sqrt(2)</code> if the nodes were diagonally aligned, to improve the approximation:

	public static double distanceBetween(Node start, Node end) {
		// Nodes are diagonal.
		if (start.x != end.x && start.y != end.y) {
			return DIAGONAL_COST;
		}
		// Nodes are not diagonal.
		return 1;
	}

Another thing I've noticed (also not stated) was that the nodes didn't need to be continuos but could "jump". To improve readability, I've started pruning unnecessary nodes (this was also needed to keep the number of nodes below the maximum threshold):

    public static List<Node> compressPath(List<Node> points) {
		List<Node> compressedPath = new ArrayList<Node>();

		Iterator<Node> pointIterator = points.iterator();
		Node previous = pointIterator.next();
		int currentXDirection = 0;
		int currentYDirection = 0;
		while (pointIterator.hasNext()) {
			Node current = pointIterator.next();
			// Checks for any direction changes by looking at the coordinates of
			// the current and previous node.
			int tmpXDirection = Double.compare(previous.x, current.x);
			int tmpYDirection = Double.compare(previous.y, current.y);

			// If the direction changes, the previous is a new point of the
			// compression.
			if (tmpXDirection != currentXDirection || tmpYDirection != currentYDirection) {
				compressedPath.add(previous);
				currentXDirection = tmpXDirection;
				currentYDirection = tmpYDirection;
			}

			// Moves to the next point.
			previous = current;
		}
		compressedPath.add(previous);
		return compressedPath;
	}

After this compression, I saw more clearly that some nodes were extending the path for no real reasons, so I pruned them as well with this code:

	public static List<Node> reduce(List<Node> points) {
		List<Node> reducedPath = new ArrayList<Node>();

		// Iterates all the points from the first.
		for (int i = 0; i < points.size(); i++) {
			Node fromStart = points.get(i);

			// Iterates all the points from the last.
			middle: for (int j = points.size() - 1; j > i; j--) {
				Node fromEnd = points.get(j);

				// If there's an obstacle between the two points, there's no
				// path between the points. Let's consider the next one from the
				// end.
				for (Obstacle o : ProblemStatement.obstacles) {
					if (o.isPathObstructed(fromStart, fromEnd)) {
						continue middle;
					}
				}
				// No obstacles if here.
				reducedPath.add(fromStart);
				reducedPath.add(fromEnd);
				// Let's jump to the next point. At the next iteration i will
				// increase again so we go to the previous point.
				i = j - 1;

			}
		}
		return reducedPath;
	}

Last but not least, I've noticed (after definely too much time) that the first input set is not impossible as I originally thought but it actually has a solution. This was a really difficult problem to solve because the 4C representation of the obstacles cut that solution away, adding the goal node and its neighbours to the obstacle points set. 

To solve this, I've added a new check before actually running the algorithm on the start and end point. If any of the point is in the obstacle points set, I try to make a path to that specific node by converting the surrounding obstacles back to their 8C representation. If after this there's no such path to that node, I declare that problem impossible without even running the algorithm:

    public static boolean clearPath(Node terminalNode) {
		// First of all we check that the point is not within any obstacle. If
		// it is, the problem doesn't have any solution.
		for (Obstacle obstacle : ProblemStatement.obstacles) {
			if (obstacle.isPointInside(terminalNode.x, terminalNode.y)) {
				// This problem doesn't have any solution.
				return false;
			}
		}

		// We add the first node to the next nodes to visit.
		Set<Node> visitedNodes = new HashSet<Node>();
		LinkedList<Node> nextNodes = new LinkedList<Node>();
		nextNodes.add(terminalNode);

		// If there's more than just one node, the path to the node is open.
		while (nextNodes.size() == 1) {
			Node currentNode = nextNodes.poll();

			// This node is not within any obstacles.
			ProblemStatement.obstaclePoints.remove(currentNode);

			// Current point neighbors.
			List<Node> currentNeighbors = Arrays.asList(new Node(currentNode.x - 1, currentNode.y - 1),
					new Node(currentNode.x, currentNode.y - 1), new Node(currentNode.x + 1, currentNode.y - 1),
					new Node(currentNode.x - 1, currentNode.y), new Node(currentNode.x + 1, currentNode.y),
					new Node(currentNode.x - 1, currentNode.y + 1), new Node(currentNode.x, currentNode.y + 1),
					new Node(currentNode.x + 1, currentNode.y + 1));

			// Adds all the neighbors nodes not already visited to the "to
			// visit" list.
			currentNeighbors.forEach(node -> {
				if (!visitedNodes.contains(node)) {
					nextNodes.add(node);
				}
			});

			// If any of the current node's neighbors is obstructed by
			// an obstacle is removed from the "to visit" list.
			for (Obstacle obstacle : ProblemStatement.obstacles) {
				currentNeighbors.forEach(node -> {
					if (obstacle.isPathObstructed(node, currentNode)) {
						ProblemStatement.obstaclePoints.add(node);
						nextNodes.remove(node);
					}
				});

			}
			// This node has been fully explored.
			visitedNodes.add(currentNode);
		}

		// An optimization may have been happened.
		return true;
	}


## Scoring

### Formula

The scoring method is the following:

 - if the solution is IMPOSSIBLE, the score is 0.
 - if the solution is not correct or incomplete, the score is -100
 - otherwise the score is 1 / (total_path_length) * 1.000.000
 
### Partial Scores

Ordered by dataset input:

 - *220 points*: the path found is 18 nodes long
    <p align="center"><img alt="Solution 1" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/input_1_solution.png" width="300px" height="300px" align="middle"> 
 
 - *0 points*: both nodes are within an obstacle, therefore the result is again IMPOSSIBLE
    <p align="center"><img alt="Solution 2" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/input_2_solution.png" width="300px" height="300px" align="middle"> 
 
 - *161 points*: the path found is 64 nodes long
    <p align="center"><img alt="Solution 3" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/input_3_solution.png" width="300px" height="300px" align="middle"> 
 
 - *66 points*: here, the path found is 142 nodes long
    <p align="center"><img alt="Solution 4" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/input_4_solution.png" width="300px" height="300px" align="middle"> 
 
### Total Score

*447 points*

 <p align="center"><img alt="Total Score" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/final_submission.png" align="middle"> 
 
## Final Considerations

This project was really fun and interesting. I've surely learned a lot on pathfinding by working on it.

My solution is not the best for sure and can be further improved. In particular, a more efficient approach would probably be the [Jump Point algorithm](https://users.cecs.anu.edu.au/~dharabor/data/papers/harabor-grastien-aaai11.pdf), which I may study in the future if I get any spare time.

## Acknowledgements

Special thanks to @enricoaleandri for building the visualizer used to show the paths.

## Discussions

If you want to say something, feel free to open an issue on this project. I don't guarantee I'll make any fixes on this project but I'll read and reply for sure.

If instead you want to get in touch directly, just send me an email.

<sub>Copyright (c) 2018 Donato Rimenti</sub>
