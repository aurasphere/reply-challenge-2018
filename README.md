# Reply Code Challenge 2018 Training

This repository contains both the problem statement and my solution to the Reply Code Challenge training problem. The solution is written in Java.

**Note:** since the method which prints the final solution uses recursion to print the visited nodes by traversing back the last one, you may get a StackOverflowError. To avoid that, add the <code>-Xss515m</code> argument when starting your JVM. 

## Log

The problem statement required to find the shortest path from point A to point B, so I've started looking into pathfinding algorithms, in particular [Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) since it was the only one I knew at the time.

### Dijkstra

It usually requires a graph to run Dijkstra but I couldn't afford to build the whole grid because of its huge size, hence, I've used a discovery method that, at each iteration, would lazily compute and add the valid neightbours nodes of the current one to the "to visit" list:

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

To be valid, a node needed not to be within an obstacle, a 3-tuple of points which defines a triangle, and inside the grid boundary of 10<sup>-6</sup> and 10<sup>6</sup> on both axis. Certainly the latter validation was trivial to implement but the former was not that difficult either when considering that I was working with triangles and taking advantage of the [baricentric coordinates](https://stackoverflow.com/a/2049712/4921205):

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

Later on, I've also added a new validation to check whether a path between nodes would cross an obstacle. This could have happened in case they were neightbours even if none of them were inside the obstacles themself like in the following example:

<p align="center"><img alt="Nodes crossing obstacle illegally" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/nodes_crossing_obstacle.jpg"></p>

How naive of me was to forgot such a check! Here's the code that does that by using the Java 2D API and intersections:

    public boolean isPathObstructed(Point origin, Point destination) {
        Line2D pathToCheck = new Line2D.Float(origin, destination);

        return firstSegment.intersectsLine(pathToCheck) || secondSegment.intersectsLine(pathToCheck)
                || thirdSegment.intersectsLine(pathToCheck);
    }
    
All of this checks were performed by iterating all the obstacles each time we needed to explore a node. Altought I didn't think that would work both due to performance and memory limitations, I resisted the urge to perform any premature optimization and I gave it a shot instead. It actually worked, to my surprise, and it was fast enough so I never really looked into a way to improve this:

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

The new solution was basically an improved version of the first one: Dijkstra's fitness function only took into account the node cost *g()* but A* also used an heuristic function *h()* to select only the nodes that moved towards the goal. The heuristic function used was the [Chebyshev distance](https://en.wikipedia.org/wiki/Chebyshev_distance) formula because it allows movement in 8 directions:

    private int h(Node n) {
        return Math.max(Math.abs(n.x - target.x), Math.abs(n.y - target.y));
    }

The whole A* algorithm was the following:

    public Node calculateShortestPath(Node start, Node goal) {
        this.target = goal;

        // Unexplored nodes.
        Queue<Node> openList = new PriorityQueue<Node>(this);
        // Explored nodes.
        List<Node> closedList = new ArrayList<Node>();

        // Initializes the first node by forcing g to 0.
        start.setG(0);
        openList.offer(start);

        int currentStep = 0;
        while (!openList.isEmpty() && currentStep < maxNumberOfSteps) {
            Node q = openList.poll();

            // Logging.
            if (debug) {
                System.out.println(q);
            }
            // Main loop of the algorithm.
            mainLoop: for (Node successor : q.getAdjacentNodes()) {

                // If we already explored that node or we already added to the
                // "to explore" list, we just skip it.
                if (closedList.contains(successor)) {
                    continue;
                }

                // Stop if we reached the goal.
                if (successor.equals(goal)) {
                    return successor;
                }

                // If we have already found this node we will keep the best
                // between the two.
                if (openList.contains(successor)) {
                    Iterator<Node> iterator = openList.iterator();
                    while (iterator.hasNext()) {
                        Node oldNode = iterator.next();
                        if (oldNode.equals(successor)) {
                            // Skip this node if we have a better one.
                            if (g(successor) >= g(oldNode)) {
                                continue mainLoop;
                            }

                            // Otherwise, this is a better node let's remove the
                            // other one.
                            iterator.remove();
                        }
                    }

                }

                // Add the node to the list to explore.
                openList.offer(successor);

            }
            // This node has been fully explored.
            closedList.add(q);
            currentStep++;
        }

        // No path has been found.
        return null;
    }

Here's a visual representation of how the A* algorithm moved towards the goal:

<p align="center"><img alt="A* 1" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/astar_1.png" width="300px" height="300px" align="middle">&nbsp;&nbsp;&nbsp;->&nbsp;&nbsp;&nbsp;<img alt="A* 2" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/astar_2.png" width="300px" height="300px" align="middle"></p>
<p align="center"><img alt="A* 3" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/astar_3.png" width="300px" height="300px" align="middle">&nbsp;&nbsp;&nbsp;->&nbsp;&nbsp;&nbsp;<img alt="A* 4" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/astar_4.png" width="300px" height="300px" align="middle"></p>
<p align="center"><img alt="A* 5" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/astar_5.png" width="300px" height="300px" align="middle"></p>

### Bounded Relaxation

Although the A* proved to be much quicker and efficient than Dijkstra, it still took a very long time to find the best path between the two nodes and I was still testing with the smallest dataset. At this point, I just decided than finding a solution would have been better than trying to find the best solution and this ultimately led me to accept a tradeoff between optimality and performances.

With this in mind, I've added the concept of [bounded relaxation](https://en.wikipedia.org/wiki/Relaxation_(approximation)) inside the algorithm by adding a static weight *w()* to the heuristic *h()* when computing the fitness function *f()*:

    private double f(Node n) {
        return g(n) + w(n) * h(n);
    }

Here's a depiction of the improved algorithm with a weight value of 10.0. The last picture shows how a path would be traced when a solution would have been found to highlight the performed tradeoff (in this dataset the final node is actually obstructed so that is not a correct solution):

<p align="center"><img alt="A* with bounded relaxation 1" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/bounded_relaxation_10_1.png" width="300px" height="300px" align="middle">&nbsp;&nbsp;&nbsp;->&nbsp;&nbsp;&nbsp;<img alt="A* with bounded relaxation 2" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/bounded_relaxation_10_2.png" width="300px" height="300px" align="middle"></p>
<p align="center"><img alt="A* with bounded relaxation 3" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/bounded_relaxation_10_final.png" width="300px" height="300px" align="middle"></p>

### Limitations

When working with the first dataset, I've stumbled upon the situation in which the goal node was obstructed. The way I coped with that was by limiting the maximum number of nodes that could be visited. Altough this prevented the algorithm to go on forever if the goal was unreacheable, it also meant that the algorithm would yield a negative result for a feasible path that required to explore more nodes than this threshold.

Of course, there are plenty of better and cleaner solutions like running the algorithm in a different thread with a timeout or checking this limit relative to a node, but this was the cheapest to implement so I just went for it. 

To give a little more insight, here is a visual representation of both the problem I wanted to avoid and the algorithm dealing with it by going on exploring until the threshold is reached after 170.000 nodes explored:

<p align="center"><img alt="Obstructed node" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/obstructed_node.jpg" width="300px" height="300px" align="middle"> <img alt="Obstructed node stalemate" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/obstructed_node_stalemate.png" width="300px" height="300px" align="middle"></p>
