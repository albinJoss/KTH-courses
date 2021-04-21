/* mipslabwork.c

   This file written 2015 by F Lundevall
   Updated 2017-04-21 by F Lundevall

   This file should be changed by YOU! So you must
   add comment(s) here with your name(s) and date(s):
	ALBIN JONSSON 2020-02-13
   This file modified 2017-04-31 by Ture Teknolog 

   For copyright and licensing, see file COPYING */

#include <stdint.h>   /* Declarations of uint_32 and the like */
#include <pic32mx.h>  /* Declarations of system-specific addresses etc */
#include "mipslab.h"  /* Declatations for these labs */

#define TMR2PERIOD ((80000000 / 256) / 10) /* 100ms */
#if TMR2PERIOD > 0xffff
#error "TimerPeriodIsTooBig"
#endif


int mytime = 0x5957;
int timeoutcount = 0;

char textstring[] = "text, more text, and even more text!";

/* Interrupt Service Routine */
void user_isr( void )
{
  return;
}

/* Lab-specific initialization goes here */
void labinit( void )
{
	volatile int * trise = (volatile int *) 0xbf886100;
	trise[1] = 0xff; 
	TRISD = TRISD | 0x7f0;
	
	T2CONSET = 0x70; //1:256 prescaling
	PR2 = TMR2PERIOD;
	TMR2 = 0;
	T2CONSET = 0x8000;
	return;
}

/* This function is called repetitively from the main program */
void labwork( void )
{
	volatile int * porte = (volatile int *) 0xbf886110;
	*porte = 0x0;
	int BTN;
	while(1)
	{
		if(BTN >= 1)
		{
			int sw = getsw();
			if(BTN == 4 || BTN == 5 || BTN == 6 || BTN == 7)
			{
				mytime = (mytime & 0x0fff) | (sw << 12);
			}
			if(BTN == 2 || BTN == 3 || BTN == 6 || BTN == 7)
			{
				mytime = (mytime & 0xf0ff) | (sw << 8);
			}
			if(BTN == 1 || BTN == 3 || BTN == 5 || BTN == 7)
			{
				mytime = (mytime & 0xff0f) | (sw << 4);
			}
			
		}
		//delay( 1000 );
		if(IFS(0) & 0X100)
		{
			IFS(0) = 0;
			timeoutcount++;
			
			if(timeoutcount == 10);
			{
				time2string( textstring, mytime );
				display_string( 3, textstring );
				display_update();
				tick( &mytime );
				timeoutcount = 0;
			}
		}
		BTN = getbtns();
		
		display_image(96, icon);
		*porte += 0x1;
	}
}
