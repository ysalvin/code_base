// Student Name and no: Hong Yuk Sing 3035927257
// Platform: CS server workbench2

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>
#include <ctype.h>
#include <signal.h>

const int MAX_STRING = 30;
const int MAX_CHAR_LENGTH = 1024;
pid_t shell_pid;

// signal handler for SIGINT and SIGUSR1
void signal_handler(int sig_num)
{
    if (sig_num == 2) // SIGINT
    {
        signal(SIGINT, signal_handler);

        printf("\n## JCshell [%d] ## ", (int)getpid());
        fflush(stdout);
    }

    //  signal handler fo SIGUSR1
    else if (sig_num == SIGUSR1)
    {
        signal(SIGUSR1, signal_handler);
        printf("SIGUSR1\n");
    }
}

int input_line(char *myargs[MAX_STRING], char cmdstr[MAX_CHAR_LENGTH])
{
    char line[MAX_CHAR_LENGTH];
    int myargs_length = 0;

    // read line from standard input
    fgets(line, MAX_CHAR_LENGTH, stdin);

    // use input empty string, ask user to input again
    if (strlen(line) == 0)
    {
        return -1;
    }
    strcpy(cmdstr, line);

    // formating
    if ((strlen(line) > 0) && (line[strlen(line) - 1] == '\n'))
    {
        line[strlen(line) - 1] = '\0';
    }

    char *token = strtok(line, " ");
    while (token != NULL)
    {
        myargs[myargs_length] = token;
        myargs_length++;
        token = strtok(NULL, " ");
    }

    return myargs_length;
}

void new_process(char *command[5], int command_length)
{
    // array of pid to store the child process
    pid_t pid_list[command_length];
    // pipline
    int pipefd[command_length - 1][2];

    int fd_in = 0;
    // store the running statistics
    char *stat_output[command_length];
    int stat_index = 0;
    
    // set up the pipline for the child process
    for (int i = 0; i < command_length - 1; i++)
    {
        if (pipe(pipefd[i]) < 0)
        {
            perror("pipe");
            exit(1);
        }
    }

    // set up every child in a for loop
    for (int i = 0; i < command_length; i++)
    {
        // signal handler for SIGUSR1
        signal(SIGUSR1, signal_handler);

        // fork() the current process
        pid_list[i] = fork();

        // handle fork() error
        if (pid_list[i] < 0)
        {
            perror("fork");
            exit(1);
        }

        // child process
        else if (pid_list[i] == 0)
        {
            // set the pipeline output
            if (i < command_length - 1)
            {
                dup2(pipefd[i][1], 1);
            }

            //set the pipeline input
            if (i != 0)
            {
                dup2(pipefd[i - 1][0], 0);
            }

            // close all pipline input output
            for (int j = 0; j < command_length - 1; j++)
            {
                close(pipefd[j][0]);
                close(pipefd[j][1]);
            }

            // Set up the SIGUSR1 signal handler
            struct sigaction sa;
            sa.sa_flags = SA_SIGINFO;
            sa.sa_handler = signal_handler;

            // format the command to pass to the execvp() function
            char arg[strlen(command[i] + 1)];
            strcpy(arg, command[i]);

            char *token = strtok(arg, " ");
            char *arg_list[30];
            int arg_length = 0;
            while (token != NULL)
            {
                arg_list[arg_length] = token;
                arg_length++;
                token = strtok(NULL, " ");
            }
            arg_list[arg_length] = NULL;

            // sigaction(SIGUSR1, &sa, NULL);
            sigset_t set;
            int sig;
            sigemptyset(&set);

            /* Add signals to the set */
            sigaddset(&set, SIGUSR1);

            signal(SIGINT, SIG_DFL);

            // use signal wait to wait for the SIGUSR1 signal from the parent process before runs
            int result = sigwait(&set, &sig);

            // error handling for error command
            if (execvp(arg_list[0], arg_list) == -1)
            {
                char l[MAX_CHAR_LENGTH];
                // handle the error when exec the command
                sprintf(l, "JCShell: '%s'", arg_list[0]);
                perror(l);
            }
            exit(1);
        }
    }

    // parent process
    siginfo_t info;
    int status[command_length];
    sleep(1);
    // when every child is setted up, send the SIGUSR1 to every child using the kill() function 
    for (int j = 0; j < command_length; j++)
    {
        kill(pid_list[j], SIGUSR1);
    }
    //close all the pipeline input output
    for (int j = 0; j < command_length - 1; j++)
    {
        close(pipefd[j][1]);
        close(pipefd[j][0]);
    }

    // using a for loop to collect all the process stat
    for (int i = 0; i < command_length; i++)
    {   
        // wait until a child is ended
        int ret = waitid(P_ALL, 0, &info, WNOWAIT | WEXITED);

        if (!ret)
        {

            char CMD[100];
            char STATE;
            int EXCODE, PPID, PID, VCTX, NVCTX;
            unsigned long USER, SYS;

            char str[100];
            FILE *file;
            int foo_int;
            unsigned long foo_lu;
            unsigned foo_u;

            // read the /proc/pid/stat file
            sprintf(str, "/proc/%d/stat", (int)(info.si_pid));
            file = fopen(str, "r");
            if (file == NULL)
            {
                printf("Error in open my proc file\n");
                exit(0);
            }  
            // read the stat file according to the format and store them in variable 
            fscanf(file, "%d %s %c %d %d %d %d %d %u %lu %lu %lu %lu %lu %lu", &PID, CMD, &STATE, &PPID, &foo_int, &foo_int, &foo_int, &foo_int,
                   &foo_u, &foo_lu, &foo_lu, &foo_lu, &foo_lu, &USER, &SYS);
            fclose(file);

            // format the CMD name, removing the ()
            char CMD2[strlen(CMD) - 2];
            strncpy(CMD2, CMD + 1, strlen(CMD) - 2);
            CMD2[strlen(CMD) - 2] = '\0';

            // read the /proc/pid/stat file
            sprintf(str, "/proc/%d/status", (int)(info.si_pid));
            file = fopen(str, "r");
            if (file == NULL)
            {
                printf("Error in open my proc file\n");
                exit(0);
            }
            char key[100];
            char line_1[100];
            char line_2[100];

            // the info about the ctxt switches is contained in the last 2 lines
            while (fgets(key, 100, file))
            {
                strncpy(line_2, line_1, 100);
                strncpy(line_1, key, 100);
            }
            fclose(file);
            sscanf(key, "nonvoluntary_ctxt_switches: %d", &NVCTX);
            sscanf(line_2, "voluntary_ctxt_switches: %d", &VCTX);

            // end the zombie process
            waitpid(info.si_pid, &status[i], 0);

            if (WIFEXITED(status[i]) && PID != shell_pid)
            {
                // printf("Child process %d's resource has exited with status %d", (int)info.si_pid, WEXITSTATUS(status));
                stat_output[stat_index] = malloc(1024 * sizeof(char));
                sprintf(stat_output[stat_index], "(PID)%d (CMD)%s (STATE)%c (EXCODE)%d (PPID)%d (USER)%.2lf (SYS)%.2lf (VCTX)%d  (NVCTX)%d \n", PID, CMD2, STATE, WEXITSTATUS(status[i]), PPID, USER * 1.0f / sysconf(_SC_CLK_TCK), SYS * 1.0f / sysconf(_SC_CLK_TCK), VCTX, NVCTX);
                // printf("\n(PID)%d (CMD)%s (STATE)%c (EXCODE)%d (PPID)%d (USER)%lf (SYS)%lf (VCTX)%d  (NVCTX)%d \n\n", PID, CMD2, STATE, WEXITSTATUS(status), PPID, USER * 1.0f / sysconf(_SC_CLK_TCK), SYS * 1.0f / sysconf(_SC_CLK_TCK), VCTX, NVCTX);
                stat_index++;
            }
            if (WIFSIGNALED(status[i]) && PID != shell_pid)
            { // check if is killed
                // printf("Child process %d has been killed by signal %d", (int)info.si_pid, WTERMSIG(status));
                stat_output[stat_index] = malloc(1024 * sizeof(char));
                sprintf(stat_output[stat_index], "(PID)%d (CMD)%s (STATE)%c (EXSIG)%s (PPID)%d (USER)%.2lf (SYS)%.2lf (VCTX)%d  (NVCTX)%d \n", PID, CMD2, STATE, strsignal(WTERMSIG(status[i])), PPID, USER * 1.0f / sysconf(_SC_CLK_TCK), SYS * 1.0f / sysconf(_SC_CLK_TCK), VCTX, NVCTX);
                // printf("\n(PID)%d (CMD)%s (STATE)%c (EXSIG)%s (PPID)%d (USER)%lf (SYS)%lf (VCTX)%d  (NVCTX)%d \n\n", PID, CMD2, STATE, strsignal(WTERMSIG(status)), PPID, USER * 1.0f / sysconf(_SC_CLK_TCK), SYS * 1.0f / sysconf(_SC_CLK_TCK), VCTX, NVCTX);
                stat_index++;
            }
        }
        else
        {
            perror("waitid");
        }
    }

    // print all the statistics when all child is terminated 
    printf ("\n");
    for (int i = 0; i < stat_index; i++)
    {
        printf("%s", stat_output[i]);
    }
}

void trim(char *s)
{
    // this function is used to remove leading or tailing whitespace
    char *p = s;
    int l = strlen(p);

    while (isspace(p[l - 1]))
    {
        p[--l] = 0;
    }
    while (*p && isspace(*p))
    {
        ++p, --l;
    }

    memmove(s, p, l + 1);
}

void read_line(char *myargs[MAX_STRING], int myargs_length, char cmdstr[MAX_CHAR_LENGTH])
{
    // detect || pipline format error
    if (strstr(cmdstr, "||") != NULL)
    {
        printf("JCshell: should not have two | symbols without in-between command\n");
        return;
    }
    // pointer array to store string, each string is a single command
    char *command[5];
    int command_index = 0;
    char cmd_copy[MAX_CHAR_LENGTH];
    strncpy(cmd_copy, cmdstr, MAX_CHAR_LENGTH);

    // use strtok() function tp divide the string of user input to seperate command
    char *token;
    token = strtok(cmd_copy, "|");
    while (token != NULL)
    {
        // allocate memory to store string
        char *p = malloc(strlen(token) + 1);
        strcpy(p, token);
        trim(p);
        if (strlen(p) == 0 && command_index != 0)
        {
            printf("JCshell: should not have two | symbols without in-between command\n");
            // Free allocated memory and return
            free(p);
            for (int i = 0; i < command_index; i++)
                free(command[i]);
            return;
        }

        command[command_index] = p;
        command_index++;
        token = strtok(NULL, "|");
    }
    new_process(command, command_index);
}

int main(int argc, char *argv[])
{
    // define signal handler for SIGINT
    signal(SIGINT, signal_handler);
    // array of string that each element store a words
    char *myargs[MAX_STRING];
    // store all the command in one string
    char cmdstr[MAX_CHAR_LENGTH];
    int myargs_length;
    shell_pid = getpid();

    while (1)
    {
        // ask user to input command
        printf("## JCshell [%d] ## ", shell_pid);
        myargs_length = input_line(myargs, cmdstr);

        if (myargs_length != -1)
        {
            // handle user "exit"
            if (strcmp(myargs[0], "exit") == 0 && myargs_length == 1)
            {
                printf("JCshell: Terminated\n");
                exit(1);
            }
            // handle incorrect format of exit
            else if (strcmp(myargs[0], "exit") == 0 && myargs_length != 1)
            {
                printf("JCshell: \"exit\" with other arguments!!!\n");
            }
            // readline 
            else
            {
                read_line(myargs, myargs_length, cmdstr);
            }
        }
    }
    return 0;
}
