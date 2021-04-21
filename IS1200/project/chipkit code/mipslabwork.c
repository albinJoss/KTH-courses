/* mipslabwork.c

   This file written 2015 by F Lundevall
   Updated 2017-04-21 by F Lundevall

   This file modified 2017-04-31 by Ture Teknolog 
   This file modified 2019 by Malin Marques & Albin Jonsson

   For copyright and licensing, see file COPYING */

#include <stdio.h>
#include <stdint.h>
#include <pic32mx.h>
#include "mipslab.h"
#include <string.h>
#include "pitches.h"

#define PRESCALER64 (80000000/64)

int tcount;                                                                                   	// RÄKNAR FLAGGOR
int validalarm = 1;                                                                             // AV GÖR OM ETT ALARM ÄR I FUNKTION
int timeforalarm = 0x1201;                                                                      // CLOCKSLAGET SOM TRIGGAR ALARM
int Btnbits;                                                                                  	// EN KOMBINERAD PORT FÖR ALLA KNAPPAR
int snz = 0;
int ringadeAlarm = 0;
char tweet[77];
int null=0;
int* z= &null;											//NULLPOINTER USED IN CALLTIME2STRING

int mytime = 0x1200;                                                                          	// TIDEN PÅ KLOCKAN

int changetime= 0x1200;										// INPUT TIDEN VID TID BYTE

char textstring[] = "text, more text, and even more text!";					// UTNYTTJAS FÖR ATT VISA TIDEN I
char textstring2[] = "text, more text, and even more text!";

void tomtext(void);										// DEKLARERING AV ALLA FUNKTIONER
void flash (int speed, int row, int strow, char str[10][17]);
void changemessage(void);
void alarm(void);
void tid();
void set (int *w, int f);
void menu (void);
void callmenu(void);
void checkalpha (int a, int new);
int calculate_baudrate_divider(int sysclk, int baudrate, int highspeed); 
void init();
void labwork( void );
void raspberry();
void resetalarm (void);
void callIFS (void);
void calltime2string(int i, int p, int* w, int nmbr, int row);
void makeSound (int frequency, int lengthOfNote);



char snzarray[15][69] = {{"Five more minutes..."}, 							// MEDELANDENA SOM SKICKAS
			  {"Ten more minutes..."}, 
			  {"Fifteen more minutes..?"}, 
			  {"Maybe like five minuites late?"},
			  {"... or not..."},
			  {"Could someone share their notes from the first half of the lecture?"}, 
			   {"Wow, someone stayed up late last night!"},
			    {"This is getting ridiculous..."},
			    {"More notes may be needed."},
			    {"Ok, can someone see if everything is allright?"}};

char displaymsg[4][18];
char menutext[5][17] = {{"__MENU__"},									// TEXTEN I MENYN
			{"change the time"},
			{"set an alarm"},
			{"change a message"},
			{"to clock"} };

char alphabet[] = " AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz123456789.,!@_";			// BOKSTAVS ORDNING VID
char *alpha = &alphabet[0];

void tomtext(void)
{
    display_string(0, " ");                                                                 		// TÖMMER DISPLAYEN PÅ TEXT
    display_string(1, " ");
    display_string(2, " ");
    display_string(3, " ");
    display_update();
}
void flash (int speed, int row, int strow, char str[10][17])						// **FUNK FÖR ATT VALD STRING BLINKAR**
{
    
	int i=0;
	while(i<=speed)									
	{
		display_string(row, " ");
		display_update();
		quicksleep(250);
		display_string(row, &str[strow][0]);
		
        display_update();
		quicksleep(250);
		i++;
	}
}


int coparemsg (int msgnow, int compmsg)                         //funktion för att kolla om det fortfarande är samma medelande som valts
{
    if (msgnow == compmsg)
    {
        return 0;                                               //ingenskillnad => 0
    }
    return 1;
}
void changemessage(void)										// **FUNK FÖR ATT ÄNDRA MEDDELANDE**					
{
	int msg = 0;							                    //vilket medelande är valt
	char *letter;
	letter  = &snzarray[0][0];					                //vilken bokstavs är vald
	int n=0;							                        //har en ny bokstav valts
	int a=0;							                        //vart är alpha i alphabetet?
    
	int lettercountr = 0;						                //är letter inom medelandet?
	int BTN;							                        //vilka knappar trycks?
    int SWT;                                                    //Vilket medelande har valts
	
    int compmsg;                                                //jämförs med medelate string
    
    
		if(tcount % 10 > 5)
		{
			display_string(1, " ");
			display_update();
		}
		else
		{
			display_string(1, &menutext[3][0]);
			display_update();
		}
    
    display_update();
	tomtext();							                          //instruktioner för usern
	display_string (0, "to change message use switches");
    display_update();
    quicksleep(2000000);
    
	tomtext();
    display_update();
	display_string(0, "step forward message");
    display_update();
    quicksleep(2000000);
    
    display_update();
	display_string(1, "step backwards message");
    display_update();
    quicksleep(2000000);
    
    display_update();
	display_string(2, "forward in alphabet");
    display_update();
    quicksleep(2000000);
    
    display_update();
	display_string (0, "backwards in alphabet");
    display_update();
    quicksleep(2000000);
    
    tomtext();
    display_string(1, snzarray[0]);
    display_update();
	while (1)
    {
        
        compmsg = msg;                                                  //sätter compmrg till msg
        quicksleep(1000000);
		BTN = getmorebtns();                                            //kollar knappar och switches
        SWT = getmoresw();
        msg = SWT;                                                      //sätter msg till switches
        
        
        if (coparemsg(msg, compmsg))                                    //om medelandet som valts ändras så
        {
            letter = &snzarray[msg][0];                                 //sätter letter pointern till första bokstaven i nya medelandet
            quicksleep(10000);
            tomtext();
            lettercountr = 0;                                           //startar om bokstavs räknaren
        }
        
        if (BTN == 1)
        {
            if (lettercountr < 68)                                      //kollar att medelandet inte är för långt
            {
                
                letter = &snzarray[msg][++lettercountr];                //väljer en ny bokstav i medelandet (framåt)
                                                                    
                n=1;                                                    //visar att ny bokstav valts
            }
            else                                                        //om man vill hoppa för långt, börjar den om från början
            {
                letter = &snzarray[msg][0];
            }
            display_string(1, snzarray[msg]);                           //updatera displayen
            display_update();
        }
        if(BTN == 2)
        {
            
            if (lettercountr > 0)                                       //kollar att medelandet inte är för långt
                {
                    letter = &snzarray[msg][--lettercountr];            //väljer en ny bokstav i medelandet (bakåt)
                                                                    
                    n = 1;                                              //markerar att en ny bokstav valts
                }
                else
                {
                    letter = &snzarray[msg][68];                        //om man backar för långt, börjar den från sista bokstaven igen
                    lettercountr = 68;
                }
                display_string(1, snzarray[msg]);                       //updatera displayen
                display_update();
            }
        if (BTN == 4) {
            alpha++;                                                    //hoppar fram i alphabets listan
            a++;                                                        //alpha räknaren updateras
            checkalpha(a, n);                                           //kallar en kollare
            *letter = *alpha;                                           //bokstaven i letter positionen byt ut med bokstaven som valts i alphabetet
            n=0;                                                        //visat att ingen ny position valts
            display_string(1, snzarray[msg]);                           //updatera diplayen
            display_update();
        }
        if (BTN == 8)
        {
            alpha--;                                                    //hoppar bak i lphabets listan
            a--;                                                        //alphabets räknaren updateras
            checkalpha(a, n);                                           //kallar kollare
            *letter = *alpha;                                           //bokstaven i letter positionen byt ut med bokstaven som valts i alphabetet
            n = 0;                                                      //visat att ingen ny position valts
            display_string(1, snzarray[msg]);                           //updatera diplayen
            display_update();
        }
       
		if (BTN == 12)                                                  //skickar tillbaka till menyn
		{
			display_string(1, "to menu");
			display_update();
		
			break;
		}
	}
	
		callmenu();						                                //återvänder till menyn
}

void alarm(void)                                                                           			//** FUNK VID ALARM**
{
    
    int BTN;
        
    while (1)
    {
       
        BTN = getmorebtns();
        makeSound(0, 1);
        makeSound(NOTE_E1, 1);
        makeSound(NOTE_FS1, 1);
        makeSound(NOTE_A2, 1);
        makeSound(NOTE_B2, 1);
        makeSound(NOTE_D3, 1);
        makeSound(NOTE_E3, 1);
        makeSound(NOTE_D3, 1);
        makeSound(NOTE_B2, 1);
        makeSound(NOTE_A2, 1);
        makeSound(NOTE_FS1, 1);
        makeSound(NOTE_E1, 1);                                                                        
      		
        if (BTN == 4)                                                  // då knapp 3 trycks
        {
        
			makeSound(0, 1);
            
            int i;
            ringadeAlarm = 0;
            timeforalarm = mytime;
            for(i = 0; i < 5; i++)
                tick(&timeforalarm);
			
            raspberry();
			snz++;                                                                   
            break;
        }
          else if(BTN == 8)						//om knapp 4 trycks återställs alarmet
        {
			makeSound(0, 1);
            tomtext();
            ringadeAlarm = 0;
			snz = 0;
            resetalarm();
            break;
        }
        
		
    }    
	menu();
}

void callIFS (void)
{
	 if(IFS(0) & 0x100)															//KODUPPREPNING!!!!!!!!
                {
                    tcount++;                                            //tcount ökar för varje gång
                    IFSCLR(0) = 0X100;                                   //tömmer på flaggor
                }
}

void tid()												//**FUNKT FÖR KLOCKA**
{
   
            	callIFS();
               
                if(tcount == 10)
                {
                    tick( &mytime );
                    tcount=0;                                         //set tcount t noll
                }

                if (ringadeAlarm != 1 && validalarm==1) 
                {                                                               // --kollar när alarmet ska gå--
                        if(mytime == timeforalarm)
                        {
							tomtext();
        					display_string (0, "WAKE UP!");
        					display_string (1, "Button 3: snooze");                                    //-- hur länge ska texten visas??-- ***
        					display_string (2, "Button 4: stop");
        					display_update(); 
                            ringadeAlarm = 1;
							alarm();                                                          // -- triggar alarmet--
                        }
                } 
                if(ringadeAlarm == 1)   alarm();   
                return;
}

void set (int *w, int f)                                                                       //**FUNK FÖR AT SET TID/ALARM**
{
   
    
    if(f == 1)
    {
    validalarm = 1;
    timeforalarm = mytime;
    tomtext();
    }                                              		    //ge instruktioner
    display_string(0, "write the binary number on the switches");
    display_string(1, "press the button that corresponds with");
    display_string(2, "the position of the number you want to change");
    tomtext();
	display_string(0, "to go back to the menu");
	display_string(1, "press buttons 3 & 4 simultaneously");
    display_update();
    quicksleep(30);
	time2string(textstring, mytime);
    display_string(3, textstring);
    display_update();
    
    int BTN;
    int SW;
    while(1)
    {
       
        BTN = getmorebtns();					   //set BTN=knapparna
        if (BTN == 8)      
        {
            SW = getmoresw();
            *w = (*w & 0x0fff) | (SW << 12);			  //kolla viss knapp						
            
                time2string( textstring2, *w );
                display_string(3, textstring2);
        		display_update();
        }
        
        if (BTN == 4)
        {
            SW = getmoresw();
            *w = (*w & 0xf0ff) | (SW << 8);
            
            time2string( textstring2, *w );
            display_string(3, textstring2);
        	display_update();
        }
        
        if (BTN == 2)
        {
            SW = getmoresw();
            *w = (*w & 0xff0f) | (SW << 4);
            
            time2string( textstring2, *w );
            display_string(3, textstring2);
       		display_update();
        }
        if (BTN == 1)         
        {
            SW = getmoresw();
            *w = ((*w & 0xfff0) | SW );
        
        time2string( textstring2, *w );
            display_string(3, textstring2);
            display_update();
            
        }
        
        /*if (BTN != 0x8 && BTN != 4 && BTN != 2 && BTN != 1 && BTN != 0 && BTN != 12)
        {
        validalarm=0;
        }*/
    
                if( (*w & 0x0000ff00) >= 0x00002400 ) *w += 0x0000dc00;
                if( (*w & 0x00000f00) >= 0x00000a00 ) *w += 0x00000600;
                if( (*w & 0x000000f0) >= 0x00000060 ) *w += 0x000000a0;
                if( (*w & 0x0000000f) >= 0x0000000a ) *w += 0x00000006;
            
            //calltime2string(2, 1, w, 0, 3);
            
            
            time2string( textstring2, *w );
            display_string(3, textstring2);
        	display_update();
    	if (BTN == 12)
        {
            //callmenu();
			break;
        }   
    }
}
void buzzerdelay (int tempo)
{
  //This function will stop all other operations except for the tcount. This will hold a note for 0.8s if the tempo is 8. Because of the PWM operating seperate from the other things in the program the sound will continue till it is set to something else.
  
    int BTN;
    int i = 0;
    while(1)
    {
        BTN = getmorebtns();
        if(IFS(0) & 0x100)
	    {   // if flag of Timer 2 is on (every 1/10th second) tcount is incremented
            ++i;
            ++tcount;
            IFSCLR(0) = 0x100;  // clears flag
        }
    
        if(i == tempo)
         break;
        
        if(tcount == 10) 
         tcount = 0;    //keeps counting up so the clock is still synched

	    if(BTN == 4 || BTN == 8)
		 break;     //breaks in case that either btn4 or btn3 (snooze or turn off the alarm) is pressed.
  }
}
void makeSound (int frequency, int lengthOfNote)
{
	int period = PRESCALER64 / frequency;
	int tempo = 8;
	// initialise and set PWM
    OC1CON = 0b01110;    // set output compare 1 module to PMW mode
    T3CONSET = 0x8000;      // start the timer 15th bit
    OC1CON |= 0x8000;    // turn on output compare 1 module 
    OC1RS = PR3 / 2;         // set duty cycle // change this
    
   	// initialise and set timer 3
	if (frequency == 0){
	 	PR3 = 0;
		OC1CON = 0x000E;    // turn on output compare 1 module 
	}

	
    PR3 = period;    
    OC1RS = (PR3 / 2);         // set duty cycle to change volume
    buzzerdelay(tempo / lengthOfNote);          //decides how many 1/10th of a second the note will be playing at. Has a set BPM of 75 if length of note is 1 it's a whole note, if it's 2 it's a half note, if it's 4 it's a quater note and if it's 8 it's an eigth note.
    }

void menu (void)											// **FUNK FÖR MENY**
{
    time2string(textstring, mytime);
    display_string(3, textstring);
    display_update();
	tomtext();
	int BTN;
	static int i = 0;
	
	
	while (1)
	{
        quicksleep(1000000);
		tid();
        time2string(textstring, mytime);
        display_string(3, textstring);
        BTN = getmorebtns();
		if(tcount % 10 > 5)
		{
		display_string(1, " ");
		display_update();
		}
		else
		{
			display_string(1, &menutext[i][0]);
			display_update();
		}
								
		if (i>1)
		{
			display_string(0, &menutext[i-1][0]);		//skroll liknande funktion
		}
		if (i>2)
		{
			display_string(2, &menutext[i+1][0]);
            display_update();
		}
		if (BTN == 2)						//vid knapp 2 tryck 
		{
            quicksleep(1000000);
			switch(i){					//switch funktion för att välja alla funktioner
				case 1:
					tomtext();
					flash(5, 0, 1, menutext);	//via vad som valts
					
					quicksleep(30);                                                                         
					
					set(&changetime, 0);
					time2string(textstring2, changetime);
					display_string(1, textstring2);
					display_update();
					mytime = (mytime & 0x0000) | changetime;
					time2string(textstring, mytime);
					display_string(3, textstring);
					display_update();
					break;
				case 2:
					tomtext();
					
					flash(5, 0,2, menutext);	//visa vad som valts
					
					quicksleep(30);                                                                         
					tomtext();			//ge instruktioner
					
					display_string(0, "to reset an alarm:");                                              
					display_string(1, "press all buttons at once");
                    display_update();

					quicksleep(30);                                                                         

					tomtext();
					set(&timeforalarm, 1);		
					break;
				case 3:
					changemessage();		//kalla change menu
					break;
				case 4:
					labwork();			//kalla menyn
					break;
			}
			break;
		}
		if(i<5 && BTN ==1)					//visar vad som vill väljas
		{
			++i;
		}
		if(i==5) 						//börjar om från början då man skrollat hela vägen ner
		{
			i=1;
		}
        
	}
}

void callmenu(void)										//**FUNK FÖR ATT KALLA MENY**
{
    
	tomtext();
	display_string (0, menutext[0]);	//info
	tomtext();
	menu();								//kallar meny
}



void checkalpha (int a, int n)                            //**FUNK FÖR ATT KOLLA OM ALPHABETSTRINGEN MST BÖRJA OM**
{
    if (a==66 || n==1)                            //kollar om a är innuti alphabetet
    {
        alpha = &alphabet[0];
        a=0;
    }
    //return 0;
}

int calculate_baudrate_divider(int sysclk, int baudrate, int highspeed)         		//**FUNK FÖR BAUD RATE BERÄKNING**
{												//heavily inspired by the example project
	int pbclk, uxbrg, divmult;						//initiera variabel
	unsigned int pbdiv;
	
	divmult = (highspeed) ? 4 : 16;						//
	/* Periphial Bus Clock is divided by PBDIV in OSCCON */
	pbdiv = (OSCCON & 0x180000) >> 19;					//
	pbclk = sysclk >> pbdiv;
	
	/* Multiply by two, this way we can round the divider up if needed */
	uxbrg = ((pbclk * 2) / (divmult * baudrate)) - 2;			//
	/* We'll get closer if we round up */
	if (uxbrg & 1)
		uxbrg >>= 1, uxbrg++;
	else
		uxbrg >>= 1;
	return uxbrg;
}

void init()										//**FUNK FÖR ATT INITIERA OSCCON
{         //heavily inspired by the example project
	/* On Uno32, we're assuming we're running with sysclk == 80 MHz */
	/* Periphial bust can run at a maximum of 40 MHz, setting PBDIV to 1 divides sysclk with 2 */
	OSCCON &= ~0x180000;				//and & or osccon					
	OSCCON |= 0x080000;
} 

void raspberry()									//**FUNK FÖR ATT SKICKA MEDELANDENA**
{
    unsigned char tmp[101];
    strcpy(tmp, snzarray[snz]);
	int i = 0;
	display_string(1, "0");
	display_update();
	
	ODCE = 0;
	TRISECLR = 0xFF;					

	init();
	
	
	/* Configure UART1 for 115200 baud, no interrupts */
	U1BRG = calculate_baudrate_divider(80000000, 115200, 0);
	U1STA = 0;
	/* 8-bit data, no parity, 1 stop bit */
	U1MODE = 0x8000;
	/* Enable transmit and recieve */
	U1STASET = 0x1400;	
	for(i; i < 100;)
    {
		
			while(U1STA & (1 << 9)); //make sure the write buffer is not full 
			U1TXREG = tmp[i];
			PORTE = tmp[i];
			display_string(1, "2");
			display_update();
		
		++i;
		if(tmp[i] == '\0') 
		{
			
			i = 0;
			break;
		}
	}
	
	
}

void resetalarm (void)                                                                          //**FUNK FÖR VALID ALARM=0**
{
    
        validalarm = 0;
        labwork();                                                                        
}


void labinit( void )										//**FUNK FÖR INITIERING AV PORTAR**
{
    volatile int *trise = (volatile int *) 0xbf886100;             //volatile pointer för port e
    trise[1]=0xff;
    
    
    TRISFSET= 0x2;                                                 //BTN1 är från port f i  bit 1
    
    TRISDSET = 0x7f0;                                              //resten av knappar från port d
    
    Btnbits= (TRISD<<1) & TRISF;                                   //kombon av vad som finns i båda portarna
    
	
    PR2= ((80000000/256)/10);                                      //perioden är (Hz/t2con)/10
    T2CON = 0x70;
    TMR2=0;
    T2CONSET=0x8000;


     T3CON = 0x60;           // prescale tmer 3 to 64:1
    PR3 =  0; //(80000000 / 64) / 440;               //
    TMR3 = 0;      
}

void labwork(void)										//**FUNK SOM ÄR HUVUDSAKEN**
{
    
    volatile int *porte = (volatile int *) 0xbf886110;
    *porte = 0x0;
    int BTN;
    static int nyssStartad = 1;
    while (1) {
    BTN= getmorebtns();
    if(nyssStartad == 1)				//skickar till meny vid start
    {
	callmenu();
        nyssStartad = 0;
    }
	if (BTN == 8)					//kallar till meny via knapp tryckning
	{
		callmenu();
	}
    time2string( textstring, mytime);
    display_string( 3, textstring );
    display_update();
   
   

                    tid();
    *porte = *porte + 0x1;
    }    
}
