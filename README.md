# Travelling Salesman Problem
<img src="https://raw.githubusercontent.com/egartley/media/master/screenshots/tsp.png">

## Information

Demo video: https://youtu.be/5mQBiy4HwHI

Application of the [travelling salesman problem](https://en.wikipedia.org/wiki/Travelling_salesman_problem), or TSP. This does not always find the shortest possible route between the points, but is more of a proof-of-concept for being able to attempt it. Currently, this is how it works:

>Given a collection of Cartesian points, starting at the specified "origin" point, iterate through all the possible paths between all of the other points, finding the shortest of those paths, and continue from the corresponding point of the shortest path until the "destination" point is reached.

Here is its actual implementation: [Field.java#L109](https://github.com/egartley/tsp/blob/master/src/Field.java#L109)

[IntelliJ IDEA](https://www.jetbrains.com/idea/) is used for this project

## Computation Time

### 1000 points
<img src="https://raw.githubusercontent.com/egartley/media/master/screenshots/graph1.png">

### 5000 points
<img src="https://raw.githubusercontent.com/egartley/media/master/screenshots/graph2.png">
