#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <termios.h>
#include <string.h>





int main(int argc,char** argv)
{
        struct termios tio;
        struct termios stdio;
        int tty_fd;
        fd_set rdset;

        unsigned char c[1000];
        unsigned char temp = 'a';
        unsigned char quit;
        int i = 0, n = 0;

        //printf("Please start with %s /dev/ttyS2 (for example)\n",argv[0]);
        memset(&stdio,0,sizeof(stdio));
        stdio.c_iflag=0;
        stdio.c_oflag=0;
        stdio.c_cflag=0;
        stdio.c_lflag=0;
        stdio.c_cc[VMIN]=1;
        stdio.c_cc[VTIME]=0;
        tcsetattr(STDOUT_FILENO,TCSANOW,&stdio);
        tcsetattr(STDOUT_FILENO,TCSAFLUSH,&stdio);
        fcntl(STDIN_FILENO, F_SETFL, O_NONBLOCK);       // make the reads non-blocking

        memset(&tio,0,sizeof(tio));
        tio.c_iflag=0;
        tio.c_oflag=0;
        tio.c_cflag=CS8|CREAD|CLOCAL;           // 8n1, see termios.h for more information
        tio.c_lflag=0;
        tio.c_cc[VMIN]=1;
        tio.c_cc[VTIME]=5;

        tty_fd = open("/dev/ttyS2", O_RDWR | O_NONBLOCK);        // O_NONBLOCK might override VMIN and VTIME, so read() may return immediately.
        cfsetospeed(&tio,B115200);            // 115200 baud
        cfsetispeed(&tio,B115200);            // 115200 baud
        tcsetattr(tty_fd, TCSANOW,&tio);
        write(tty_fd, &temp, 1);
        while(quit != 'q')
        {
        while (read(tty_fd, &c[i], 1)>0 && i < 1000)
        {
              
               
                  
                   write(STDOUT_FILENO, &c[i], 1);
                   ++i;
                  
                  // write(tty_fd, &temp, 1);
               
               if(c[i - 1] == '\0')
               {
                   i = 0;
                   break;
               }
               
        }
        /*while(c[n] != '\0')
        {
            write(STDOUT_FILENO, &c[n], 1);
            ++n;
        */i = 0;
        }
        close(tty_fd);
        exit(0);
}