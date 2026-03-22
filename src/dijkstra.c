#include <stdio.h>
#include <limits.h>
#include "graph.h"

#define MAX 20

char *cities[MAX] = {
    "Delhi","Mumbai","Bengaluru","Hyderabad","Chennai",
    "Kolkata","Jaipur","Ahmedabad","Pune","Bhopal"
};

void printPath(int parent[], int j)
{
    if(parent[j] == -1)
        return;

    printPath(parent, parent[j]);

    printf(" -> %s", cities[j]);
}

void dijkstra(int src, int n)
{
    int dist[MAX];
    int visited[MAX];
    int parent[MAX];

    for(int i = 0; i < n; i++)
    {
        dist[i] = INT_MAX;
        visited[i] = 0;
        parent[i] = -1;
    }

    dist[src] = 0;

    for(int count = 0; count < n - 1; count++)
    {
        int min = INT_MAX;
        int u = -1;

        for(int v = 0; v < n; v++)
        {
            if(!visited[v] && dist[v] <= min)
            {
                min = dist[v];
                u = v;
            }
        }

        if(u == -1)
            break;

        visited[u] = 1;

        for(int v = 0; v < n; v++)
        {
            if(!visited[v] && graph[u][v] &&
               dist[u] != INT_MAX &&
               dist[u] + graph[u][v] < dist[v])
            {
                dist[v] = dist[u] + graph[u][v];
                parent[v] = u;
            }
        }
    }

    printf("\n========== ROUTE RESULTS ==========\n");

    for(int i = 0; i < n; i++)
    {
        printf("\nDestination: %s\n", cities[i]);
        printf("Distance: %d km\n", dist[i]);

        printf("Path: %s", cities[src]);
        printPath(parent, i);

        printf("\n");
    }

    printf("\n===================================\n");
}
