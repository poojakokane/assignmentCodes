#include <stdio.h>
#include <stdlib.h>

int countStars(char **map, int rows, int cols);

void findNextStar(char **map, int rows, int cols, int *i, int *j);

void removeStarFromMap(char **map, int rows, int cols, int i, int j);

int main() {

    char *ipFile = "input.txt";

    FILE *f = fopen(ipFile, "r");
    int testcase = 1;

    // Run for each testcase
    while(!feof(f)){

        // Read the rows and columns in the current testcase
        int rows, cols;
        fscanf(f, "%d %d", &rows, &cols);

        // Create the map that stores will store the "sky" characters
        char **map = (char**) malloc(sizeof(char*) * rows);
        for(int i=0; i< rows; i++){
            map[i] = (char*) malloc(sizeof(char) * cols);

            // Read the lines for the SKY
            fscanf(f, "%s", map[i]);
        }

        // Print the output for the current testcase
        printf("Case %d: %d\n", testcase++, countStars(map, rows, cols));

        for(int i=0; i<rows; i++){
            free(map[i]);
        }
        free(map);
    }



    return 0;
}

int countStars(char **map, int rows, int cols) {
    // initialize the number of starts with 0
    int stars = 0;

    // In a loop we find one star and then mark all the connected characters
    // Keep this for all the stars found and keep count of the stars marked
    int i=-1, j=-1;
    // find the next star if present
    findNextStar(map, rows, cols, &i, &j);
    while(i != -1 && j != -1){
        // increment the count for stars
        stars++;

        // mark all the characters corresponding to that star
        removeStarFromMap(map, rows, cols, i, j);
        i = -1;
        j = -1;

        // find the next star
        findNextStar(map, rows, cols, &i, &j);
    }

    return stars;
}

void removeStarFromMap(char **map, int rows, int cols, int i, int j) {
    // Mark the current cell as 'x' denoting that this cell is already visited
    // This is done to avoid double counting
    map[i][j] = 'x';

    // For all the connected cells (up, down, left and right)
    // if that is a  cell with '-' recurse to mark that cell
    if(i>0 && map[i-1][j] == '-'){
        // Up
        removeStarFromMap(map, rows, cols, i-1, j);
    }
    if(i<rows-1 && map[i+1][j] == '-'){
        // Down
        removeStarFromMap(map, rows, cols, i+1, j);
    }
    if(j>0 && map[i][j-1] == '-'){
        // Left
        removeStarFromMap(map, rows, cols, i, j-1);
    }
    if(j<cols-1 && map[i][j+1] == '-'){
        // Right
        removeStarFromMap(map, rows, cols, i, j+1);
    }
}

void findNextStar(char **map, int rows, int cols, int *i, int *j) {

    // Just find the first character that is '-' which signifies presence of a star
    for(int r=0; r<rows; r++){
        for(int c=0; c<cols; c++){
            if(map[r][c] == '-'){
                *i = r;
                *j = c;
                return;
            }
        }
    }
}
