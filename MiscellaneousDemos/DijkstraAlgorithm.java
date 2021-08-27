package MiscellaneousDemos;

import java.util.Scanner;

public class DijkstraAlgorithm {
    public static void main(String[] args) {
        int i, j, k, n, min_dist, min_dist_vertex;
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the Number of Nodes: ");
        n = input.nextInt();
        int[][] adj_mat = new int[n][n];
        //Taking Edges Matrix as Input
        System.out.println("Enter the Edges Matrix:");
        for(i = 0 ; i < n ; i++)
            for(j = 0 ; j < n ; j++)
                adj_mat[i][j] = input.nextInt();

        System.out.println();

        System.out.print("Enter the Source Vertex: ");
        int sourceVertex = input.nextInt();
        int[] shortestPath = new int[n], visited = new int[n];
        min_dist_vertex = sourceVertex;

        // Initializations
        for(i = 0 ; i < n ; i++){
            if(adj_mat[sourceVertex][i] != 0)
                shortestPath[i] = adj_mat[sourceVertex][i];
            else
                shortestPath[i] = Integer.MAX_VALUE;
            visited[i] = 0;
        }
        shortestPath[sourceVertex] = 0;

        // Main Code
        for(i = 0 ; i < n ; i++){
            min_dist = Integer.MAX_VALUE;
            for(k = 0 ; k < n ; k++){
                // Select the vertices that are unvisited and are nearer to sourceVertex
                if(visited[k] == 0 && min_dist >= shortestPath[k]){
                    min_dist = shortestPath[k];
                    min_dist_vertex = k;
                }
            }
            // min_dist_vertex is at minimum distance to sourceVertex. So put it in solution
            visited[min_dist_vertex] = 1;

            for (j = 0; j < n; j++){
                // Select unvisited vertices, adjacent to min_dist_vertex and
                // distance from source to this vertex is not infinity
                // Also apply the relaxation procedure
                if(visited[j] == 0 && adj_mat[min_dist_vertex][j] != 0 && shortestPath[min_dist_vertex] != Integer.MAX_VALUE &&
                        shortestPath[j] > shortestPath[min_dist_vertex] + adj_mat[min_dist_vertex][j]){
                    shortestPath[j] = shortestPath[min_dist_vertex] + adj_mat[min_dist_vertex][j];
                }
            }

        }

        // Printing the shortest paths
        System.out.println("Shortest Paths from the Source Vertex " + sourceVertex);
        for(i = 0; i < n; i++){
            System.out.printf("%d -> %d: %d\n", sourceVertex, i, shortestPath[i]);
        }
    }
}
