//
//  time4io.c
//  Lab3
//
//  Created by Malin Marques on 2020-02-09.
//  Copyright © 2020 Malinc. All rights reserved.
//

#include <stdint.h>
#include <pic32mx.h>
#include "mipslab.h"
#include <string.h>
#include "pitches.h"
//#include "mipslabwork.c"

int getmoresw (void)
{
    int swresult = (PORTD >> 8) & 0x000F;                                       // --shift 8 för börjar på 8 töm resten och returnera--
    return swresult;
}

int getmorebtns (void)
{
    //int Btnbits= (TRISD<<1) & TRISF; -- om ny knappar finns i trisd--
    int btnresult = ((PORTD >> 4) & 0x000e) | ((PORTF >> 1) & 0x1);                                    // --bytte 4 tidigare(5) och 0x000f tidigare(0x0007)--
       return btnresult;
}
