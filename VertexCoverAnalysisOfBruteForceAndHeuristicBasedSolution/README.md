# Vertex Cover Problem
```bash
PS E:\assignmentCodes\VertexCoverAnalysisOfBruteForceAndHeuristicBasedSolution\src\main\java> cd .\org\example\
PS E:\assignmentCodes\VertexCoverAnalysisOfBruteForceAndHeuristicBasedSolution\src\main\java\org\example> javac *.java
PS E:\assignmentCodes\VertexCoverAnalysisOfBruteForceAndHeuristicBasedSolution\src\main\java\org\example> cd ../..
PS E:\assignmentCodes\VertexCoverAnalysisOfBruteForceAndHeuristicBasedSolution\src\main\java> ls


    Directory: E:\assignmentCodes\VertexCoverAnalysisOfBruteForceAndHeuristicBasedSolution\src\main\java


Mode                LastWriteTime         Length Name
----                -------------         ------ ----
d-----        5/10/2020  11:26 AM                org
-a----        5/10/2020   4:21 PM            212 input.txt


PS E:\assignmentCodes\VertexCoverAnalysisOfBruteForceAndHeuristicBasedSolution\src\main\java> java org.example.Main input.txt
**************************
USING BRUTE-FORCE APPROACH
**************************

1 - 2
2 - 3
3 - 4
4 - 1
1 - 3
2 - 4
4 - 5
Time taken = 1282400 ns
For the above graph, there ISN'T a vertex cover of size 1
Time taken = 641000 ns
For the above graph, there ISN'T a vertex cover of size 2
Time taken = 186900 ns
For the above graph, there IS a vertex cover of size 3, VertexCover = [1, 2, 4]
Time taken = 230300 ns
For the above graph, there IS a vertex cover of size 4, VertexCover = [1, 2, 3, 4]
Time taken = 143200 ns
For the above graph, there IS a vertex cover of size 5, VertexCover = [1, 2, 3, 4, 5]

1 - 2
2 - 3
3 - 1
Time taken = 224900 ns
For the above graph, there ISN'T a vertex cover of size 1
Time taken = 130300 ns
For the above graph, there IS a vertex cover of size 2, VertexCover = [1, 2]
Time taken = 178100 ns
For the above graph, there IS a vertex cover of size 3, VertexCover = [1, 2, 3]

1 - 3
1 - 4
1 - 2
1 - 5
3 - 5
Time taken = 337000 ns
For the above graph, there ISN'T a vertex cover of size 1
Time taken = 294200 ns
For the above graph, there IS a vertex cover of size 2, VertexCover = [1, 3]
Time taken = 488500 ns
For the above graph, there IS a vertex cover of size 3, VertexCover = [1, 2, 3]
Time taken = 688800 ns
For the above graph, there IS a vertex cover of size 4, VertexCover = [1, 2, 3, 4]
Time taken = 142400 ns
For the above graph, there IS a vertex cover of size 5, VertexCover = [1, 2, 3, 4, 5]


Aggregated statistics
=======================
Number of Vertices      |Number Of Edges        |Average time taken in NanoSeconds
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
5                       |5                      |390180
5                       |7                      |496760
3                       |3                      |177766




******************************
USING HEURISTIC-BASED APPROACH
******************************

1 - 2
2 - 3
3 - 4
4 - 1
1 - 3
2 - 4
4 - 5
Heuristically found min vertex cover = [4, 1, 2]
Time taken = 852900 ns
For the above graph, there ISN'T a vertex cover of size 1
Time taken = 400 ns
For the above graph, there ISN'T a vertex cover of size 2
Time taken = 400 ns
For the above graph, there IS a vertex cover of size 3
Time taken = 300 ns
For the above graph, there IS a vertex cover of size 4
Time taken = 300 ns
For the above graph, there IS a vertex cover of size 5

1 - 2
2 - 3
3 - 1
Heuristically found min vertex cover = [1, 2]
Time taken = 882000 ns
For the above graph, there ISN'T a vertex cover of size 1
Time taken = 600 ns
For the above graph, there IS a vertex cover of size 2
Time taken = 400 ns
For the above graph, there IS a vertex cover of size 3

1 - 3
1 - 4
1 - 2
1 - 5
3 - 5
Heuristically found min vertex cover = [1, 3]
Time taken = 228100 ns
For the above graph, there ISN'T a vertex cover of size 1
Time taken = 400 ns
For the above graph, there IS a vertex cover of size 2
Time taken = 300 ns
For the above graph, there IS a vertex cover of size 3
Time taken = 300 ns
For the above graph, there IS a vertex cover of size 4
Time taken = 300 ns
For the above graph, there IS a vertex cover of size 5


Aggregated statistics
=======================
Number of Vertices      |Number Of Edges        |Average time taken in NanoSeconds
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
5                       |5                      |45880
5                       |7                      |170860
3                       |3                      |294333

PS E:\assignmentCodes\VertexCoverAnalysisOfBruteForceAndHeuristicBasedSolution\src\main\java>
```