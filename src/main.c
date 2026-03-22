#include <stdio.h>
#include "graph.h"
#include "dijkstra.h"

void showCities()
{
    printf("\nIndian Cities Network:\n");
    printf("0 Delhi\n1 Mumbai\n2 Bengaluru\n3 Hyderabad\n4 Chennai\n");
    printf("5 Kolkata\n6 Jaipur\n7 Ahmedabad\n8 Pune\n9 Bhopal\n");
}

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

    int choice;

    while(1)
    {
        printf("\n========== NEO NAV SYSTEM ==========\n");
        printf("1. Show City Network\n");
        printf("2. Find Shortest Route\n");
        printf("3. Traffic Simulation\n");
        printf("4. Drone Delivery Route\n");
        printf("5. Exit\n");

        printf("Enter choice: ");
        scanf("%d",&choice);

        if(choice==1)
        {
            showCities();
        }

        else if(choice==2)
        {
            int source;
            printf("Enter source city index: ");
            scanf("%d",&source);
            dijkstra(source,n);
        }

        else if(choice==3)
        {
            printf("\nTraffic Update: Some routes congested ⚠\n");
        }

        else if(choice==4)
        {
            printf("\nDrone Route Simulation\n");
            dijkstra(0,n);
        }

        else
        {
            break;
        }
    }

    return 0;
}
