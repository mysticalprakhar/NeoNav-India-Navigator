#include "graph.h"

int graph[MAX][MAX];

void initGraph(int n)
{
    for(int i=0;i<n;i++)
    {
        for(int j=0;j<n;j++)
        {
            graph[i][j]=0;
        }
    }
}

void addEdge(int a,int b,int weight)
{
    graph[a][b]=weight;
    graph[b][a]=weight;
}
