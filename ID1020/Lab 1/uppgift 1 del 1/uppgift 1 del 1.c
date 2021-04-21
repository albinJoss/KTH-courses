/*
@Author Albin Jonsson
2020-08-31

this Code will reverse the input of the user by using a recursive function in C and is a solution to the first problem in lab 1 for the course ID1020 at KTH
It takes input from the user. 
*/


#include <stdio.h>

void Rekursiv()     //REcursively reverses a string of any size.
{
    char ch;        //initierar ch
    ch = getchar();         //Hämtar en karaktär som användaren skriver in

    if(ch == '\n' || ch == '\r')    return;     //basfall

    Rekursiv();        //tillkallar sig själv för att hämta nästa karaktär

    putchar(ch);        //skriver ut den senaste karaktären som inte är en newline.
}

int main()
{
    Rekursiv();

    return 0;
}