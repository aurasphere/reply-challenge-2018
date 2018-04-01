# Reply Code Challenge 2018 Training

This repository contains both the problem statement and my solution to the Reply Code Challenge training problem. The solution is written in Java.

## First approaches

The problem statement required to find the shortest path from point A to point B, so I've started looking into pathfinding algorithms, in particular [Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) since it was the only one I knew at the time.

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

To be valid, a node needed not to be within an obstacle (a 3-tuple of points which defines a triangle) and inside the grid boundary of 10<sup>-6</sup> and 10<sup>6</sup> on both axis.

Unluckily, this quickly proved to be too slow even from the smallest input data set, therefore I switched over to a plain implementation of the [A* algorithm](https://en.wikipedia.org/wiki/A*_search_algorithm). 

The new solution was basically an improved version of the first one: Dijkstra's only took into account the node cost but A* also used an heuristic function to select only the nodes that moved towards the goal. The heuristic function used was the [Manhattan Distance](https://en.wikipedia.org/wiki/Taxicab_geometry) formula because it allows for movement in 8 directions.

Later on, I've also added a new validation to check whether a path between nodes would cross an obstacle. This could have happened in case they were neightbours even if none of them were inside the obstacles themself like in the following example:

<p align="center"><img alt="Nodes crossing obstacle illegally" src="https://github.com/aurasphere/reply-challenge/raw/master/screenshots/nodes_crossing_obstacle.jpg"></p>
