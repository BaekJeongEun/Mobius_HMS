#include <wiringPi.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
 
#define GPIOIN         26
 
void read_motion()
{
    pinMode( GPIOIN, INPUT );
    printf("Sensor start\n");
    
    /* detect change and read data */
    while (1){
		if(!digitalRead(GPIOIN)){
			printf("No Detected\n");
		}
		else{
			printf("Detected\n");
		}
        delay(500);
    }
}
 
int main( void )
{
    //wiringPiSetup :: initialize wiringPi
    if ( wiringPiSetupGpio() == -1 )
        exit( 1 );
 
    read_motion();
    return(0);
}
