#include <stdio.h>
#include <ctype.h>

//Written by Malin J. A. Marques & Albin Jonsson
//file created 23-09-2020, edited last 27-09-2020
//The file cleans a text of non-alphabetic symbols (with the exceptions of \n and blank (" ")) and replaces them with a blank(" ").
//No code has been borrowed.
unsigned char getch( char filename[], int pos)
{
    FILE *textfile;                                                         //create a pointer that points to where the file is
    textfile= fopen(filename, "r");                                  //points pointer to textfile in use and enables permission to read the file

    if(textfile==NULL) {
        perror("Couldn't open file");                                       //if an error occurs (pointer put to null) print out error message
        return 0;
    }
    char c[1];                                                              //create a char
    fseek(textfile, pos, SEEK_SET);                                         //move pointer in file to requested position

    fread(c,1,1,textfile);                                     //read from the file
    fclose(textfile);                                                      //close text file to not override open file limit
    return c[0];
}
int fileln ( char filename[] )
{

    FILE *textfile;                                                         //create a pointer that points to where the file is
    textfile= fopen(filename, "r");                                  //points pointer to textfile in use

    if(textfile==NULL) {
        perror("Couldn't open file");                                       //if an error occurs (pointer put to null) print out error message
        return 0;
    }
    int counter=0;                                                        //initieate counter
    char c=fgetc(textfile);                                               //retrieve char from textfile
    while (c!=EOF)                                                        //check if end of file (1 if not end of file)
    {
        counter++;                                                        //looks for what char exists at a given point (counter), seel-set=start of file (points to first byte in file)
        c=fgetc(textfile);
    }                                                                     //too look through file "index"= seekset+counter


    fclose(textfile);                                                     //close text file
    return counter;                                                       //returns how many characters in file
}

int charcheck(unsigned char i)
{
    if(isalpha(i) == 0 && i != '\n' && i != ' ' )                          //checks if character is equal to newline or blank when the char isn't a letter
    {
        return 0;                                                          //returns 0 if char isn't a letter or newline or blank
    }

    return 1;                                                              //returns 1 otherwise
}


int main()
{

    int ln = fileln("F:\\Algoritmer och datastrukturer\\lab3\\src\\com\\company\\TheText.txt");                               //check length of text file using func fileln
    unsigned char array[ln];                                                                                  //create a char array of length of text to modify

    for(int i=0; i<ln; i++)
    {
        if(charcheck(getch("F:\\Algoritmer och datastrukturer\\lab3\\src\\com\\company\\TheText.txt", i))) {                //copies letter if check finds that char is a letter
            array[i] = getch("F:\\Algoritmer och datastrukturer\\lab3\\src\\com\\company\\TheText.txt", i);
        }
        else                                                                                                  //replaces symbol if check finds that the char isn't a letter etc.
            array[i]= ' ';
    }
    FILE *textfile;                                                                                           //create a pointer that points to where the file is
    textfile= fopen("F:\\Algoritmer och datastrukturer\\lab3\\src\\com\\company\\TheText2.txt", "w+");                    //points pointer to textfile in use and allow the program to write to the file

    if(textfile==NULL) {
        printf("textfilenull");
       perror("Couldn't open file");                                                                         //if an error occurs (pointer put to null) print out error message
        return 0;
    }

        printf("new text: %s", array);                                                                        //print out altered text

    fwrite(array,1, ln,textfile);                                                                        //write to the textfile
    fflush(textfile);                                                                                         //empty outstream
    fclose(textfile);                                                                                         //close access to text file
}
