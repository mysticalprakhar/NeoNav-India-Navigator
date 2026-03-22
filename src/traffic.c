#include <stdio.h>
#include <stdlib.h>
#include <time.h>

void simulateTraffic()
{
    srand(time(NULL));

    int traffic = rand()%3;

    printf("\nTraffic Status:\n");

    if(traffic==0)
        printf("Heavy traffic on Delhi → Jaipur\n");

    else if(traffic==1)
        printf("Moderate traffic on Mumbai → Pune\n");

    else
        printf("All routes clear\n");
}
