#include <stdint.h>
#include <pic32mx.h>
#include "mipslab.h" 

int getsw( void )
{
	int switchBits = 0x00000000;
	switchBits = (PORTD >> 8) & 0x000f;
	return switchBits;
}

int getbtns(void)
{
	int buttonBits = (PORTD >> 5) & 0x0007;
	return buttonBits;
}