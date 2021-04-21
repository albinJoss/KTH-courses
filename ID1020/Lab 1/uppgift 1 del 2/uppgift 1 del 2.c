/*
@Author Albin Jonsson
2020-08-02

This code is a sulotion for the first problem in Fundamental lab in the course ID1020 at KTH

It uses the users input to iteratively reverse a string
*/

#include <stdio.h>

void Iterativ()     //reverses a string with 3 letters by using an iterative function.
{
    char ch[16];        //Initierar en string
    int i = 0;

    for(i; i < 3; ++i)
    {
        ch[i] = getchar();
    }
    for(i; i >= 0; --i)
    {
        putchar(ch[i]);
    }    

}

int main()
{
    Iterativ();

    return 0;
}