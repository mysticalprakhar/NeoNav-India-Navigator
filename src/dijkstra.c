#include <stdio.h>
#include <limits.h>
#include "graph.h"

void dijkstra(int src,int n)
{
    int dist[MAX];
    int visited[MAX];

    for(int i=0;i<n;i++)
    {
        dist[i]=INT_MAX;
        visited[i]=0;
    }

    dist[src]=0;

    for(int count=0;count<n-1;count++)
    {
        int min=INT_MAX;
        int u=0;

        for(int v=0;v<n;v++)
        {
            if(!visited[v] && dist[v]<=min)
            {
                min=dist[v];
                u=v;
            }
        }

        visited[u]=1;

        for(int v=0;v<n;v++)
        {
            if(!visited[v] && graph[u][v] &&
               dist[u]!=INT_MAX &&
               dist[u]+graph[u][v]<dist[v])
            {
                dist[v]=dist[u]+graph[u][v];
            }
        }
    }

    printf("\nShortest distance from source:\n");

    for(int i=0;i<n;i++)
    {
        printf("City %d → %d km\n",i,dist[i]);
    }
}
