
#include <stdio.h>
#include "graph.h"
#include "dijkstra.h"

int main()
{
    int n = 10;

    initGraph(n);

    addEdge(0,1,280);
    addEdge(0,2,550);
    addEdge(1,3,660);
    addEdge(2,4,585);
    addEdge(3,5,270);
    addEdge(5,6,150);
    addEdge(6,7,840);
    addEdge(7,8,350);
    addEdge(8,9,630);

    int source;

    printf("NeoNav Smart City Navigator\n");

    printf("Enter Source City Index (0-9): ");
    scanf("%d",&source);

    dijkstra(source,n);

    return 0;
}
