#include <bits/stdc++.h>
#include <zconf.h>
#include <sys/wait.h>
#include <sys/stat.h>

#define YOUR_STUDENT_ID "cssc9999"

using namespace std;

void printCurrentShell();

bool runNextCommand();

void tokenize(std::string const &str, const char *delim, std::vector<std::string> &out);

void forkSingleCommand(vector<string> commandAndArgs, int pipes[][2], int commandId, int totalPipes);

void closeAllPipes(int pipes[][2], int numPipes, int skipId);

bool dummyFunc();

int main() {
    do {
        printCurrentShell();
    } while (runNextCommand());
}

bool runNextCommand() {
    string commandLine;
    getline(cin, commandLine);

    vector<std::string> commandsAcrossPipes;
    tokenize(commandLine, "|", commandsAcrossPipes);

    if(commandsAcrossPipes.size() == 0){
        return true;
    }

    if(commandsAcrossPipes.size() == 1){
        vector<string> commandAndArgs;
        tokenize(commandsAcrossPipes[0], " ", commandAndArgs);
        if(commandAndArgs.size() == 1 && commandAndArgs[0] == "exit"){
            return false;
        }

        forkSingleCommand(commandAndArgs, nullptr, 0, 0);
        wait(0);
        return true;
    }

    // we need n-1 pipes
    int pipes[commandsAcrossPipes.size()-1][2];
    // setup all pipes
    for(int i=0; i<commandsAcrossPipes.size()-1; i++){
        pipe(pipes[i]);
    }

    for(int i=0; i<commandsAcrossPipes.size(); i++){
        vector<string> commandAndArgs;
        tokenize(commandsAcrossPipes[i], " ", commandAndArgs);
        if(commandAndArgs.size() == 1 && commandAndArgs[0] == "exit"){
            return false;
        }

        forkSingleCommand(commandAndArgs, pipes, i, commandsAcrossPipes.size()-1);
    }

    closeAllPipes(pipes, commandsAcrossPipes.size() - 1, -1);
    for(int i=0; i<commandsAcrossPipes.size(); i++){
        wait(0);
    }

    return true;
}

void forkSingleCommand(vector<string> commandAndArgs, int pipes[][2], int commandId, int totalPipes) {
    string command = commandAndArgs[0];
    char **args = (char**)malloc(commandAndArgs.size() * sizeof(char*));

    for(int i=0; i<commandAndArgs.size(); i++){
        args[i] = (char *)(commandAndArgs[i].c_str());
    }
    args[commandAndArgs.size()] = NULL;

    // Check if command is executable
//    struct stat sb;
//    if (!(stat(command.c_str(), &sb) == 0 && sb.st_mode & S_IXUSR)){
//        cout << "The command '" << command << "' is not executable" << endl;
//        return;
//    }

    if(fork() == 0){
//        while(dummyFunc()){
//            sleep(10);
//        }
        if(pipes != NULL) {
            if (commandId == 0) {
                close(STDOUT_FILENO);
                dup2(pipes[commandId][1], STDOUT_FILENO);

                closeAllPipes(pipes, totalPipes, -1);
            } else if (commandId == totalPipes){
                close(STDIN_FILENO);
                dup2(pipes[commandId-1][0], STDIN_FILENO);

                closeAllPipes(pipes, totalPipes, -1);
            }
            else{
                close(STDOUT_FILENO);
                close(STDIN_FILENO);
                dup2(pipes[commandId][1], STDOUT_FILENO);
                dup2(pipes[commandId-1][0], STDIN_FILENO);

                closeAllPipes(pipes, totalPipes, -1);
            }
        }

        if (execvp(command.c_str(), args) < 0){
            cout << "The command '" << command << "' is not executable" << endl;
            exit(0);
        }
    }
    else {
        return;
    }
}

bool dummyFunc() {
    int test = 5;
    if(test == 5)
        return true;

    return false;
}

void closeAllPipes(int pipes[][2], int numPipes, int skipId) {
    for(int i=0; i<numPipes; i++){
        if(i == skipId) {
            continue;
        }
        close(pipes[i][0]);
        close(pipes[i][1]);
    }
}

void tokenize(std::string const &str, const char *delim,
              std::vector<std::string> &out) {
    size_t start;
    size_t end = 0;

    while ((start = str.find_first_not_of(delim, end)) != std::string::npos) {
        end = str.find(delim, start);
        out.push_back(str.substr(start, end - start));
    }
}

void printCurrentShell() {
    cout << YOUR_STUDENT_ID << "% ";
}
