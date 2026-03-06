#include <stdio.h>
#include <stdlib.h>

void aiSuggestRoute()
{
    int traffic = rand()%3;

    printf("\nAI Route Recommendation:\n");

    if(traffic==0)
        printf("AI Suggests: Delhi → Jaipur → Ahmedabad\n");

    else if(traffic==1)
        printf("AI Suggests: Delhi → Lucknow → Patna\n");

    else
        printf("AI Suggests: Delhi → Mumbai → Pune\n");
}
