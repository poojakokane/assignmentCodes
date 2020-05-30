class PawnBoard():
    """Program will have two people play a board game and return Win, Draw or Unifinished"""

    def get_current_state(self):
        """gives the current state of the game, "DRAW" or "UNIFINISHED" or "X_WON" or "O_WON" """
        return (self.__current_state)

    def print(self):
        print("********* Start Board *********")
        for x in range(4):
            for y in range(4):
                print(self.__board[x][y], end=" ")
            print('\n')
        print("*********  End Board **********")

    def __init__(self):
        """initializing the board and current_state"""
        # board with four rows and columns
        # Initializing the board
        self.__board = [["X", "X", "X", "X"], [" ", " ", " ", " "], [" ", " ", " ", " "], ["O", "O", "O", "O"]]
        self.__current_state = "UNFINISHED"

    def make_move(self, original_row, original_column, new_row, new_column):

        if self.legal_move(original_row, original_column, new_row, new_column):
            self.__board[new_row][new_column] = self.__board[original_row][original_column]
            self.__board[original_row][original_column] = " "

            # Applied the move, now check for completion of game
            if self.__board[new_row][new_column] == "X" and new_row == 3:
                self.__current_state = "X_WON"
            elif self.__board[new_row][new_column] == "O" and new_row == 0:
                self.__current_state = "O_WON"
            elif self.isDraw():
                self.__current_state = "DRAW"

            return True
        else:
            return False
        # if(self.__current_state == 'X_WON' or self.current_state == 'O_WON' or self.current_state == 'DRAW'):
        #     return False
        # if(self.__board == [["X","X","X"]])

    def legal_move(self, original_row, original_column, new_row, new_column):
        # check if the move is legal
        # the columns must be the same and rows increment only by one and the current state must be UNFINISHED
        if (original_column == new_column
                and (new_row == (original_row + 1) or new_row == (original_row - 1))
                and self.__current_state == "UNFINISHED"):
            return True

        # A move can be diagonal only if capturing the opponent's piece
        # The column must be greater by one or less by one and must contain the opponent's piece and the row must increase by 1
        if (((original_row+1 == new_row and (new_column+1 == original_column or new_column-1 == original_column))
             or (original_row-1==new_row and (new_column+1 == original_column or new_column-1 == original_column)))
            and ((self.__board[original_row][original_column] == "X" and self.__board[new_row][new_column] == "O")
                 or (self.__board[original_row][original_column] == "O" and self.__board[new_row][new_column] == "X"))):
            return True

        else:
            return False

    def isDraw(self):
        for i in range(0,4):
            for j in range(0,4):
                if self.__board[i][j] == "X" and (self.__board[i+1][j]==" " or self.isPresentDiagnoally(i,j,"O")):
                        return False
                if self.__board[i][j] == "O" and (self.__board[i-1][j]==" " or self.isPresentDiagnoally(i,j,"X")):
                        return False

        return True

    def isPresentDiagnoally(self, i, j, toCheck):
        if j==0:
            if toCheck=="X" and self.__board[i-1][j+1]=="X":
                return True
            if toCheck=="O" and self.__board[i+1][j+1]=="O":
                return True
        elif j == 3:
            if toCheck=="X" and self.__board[i-1][j-1]=="X":
                return True
            if toCheck=="O" and self.__board[i+1][j-1]=="O":
                return True
        else:
            if toCheck=="X" and (self.__board[i-1][j+1]=="X" or self.__board[i-1][j-1]=="X"):
                return True
            if toCheck=="O" and (self.__board[i+1][j+1]=="O" or self.__board[i+1][j-1]=="O"):
                return True

        return False


# For this assignment this needs to be removed
# This is just a rest for undersatnding the correctness
def main():
    board = PawnBoard()
    board.print()
    board.make_move(0, 0, 1, 0)
    board.print()
    board.make_move(0, 2, 1, 2)
    board.print()
    board.make_move(3, 1, 2, 1)
    board.print()
    board.make_move(1, 2, 2, 1)
    board.print()
    print("Current state: ", board.get_current_state())
    board.make_move(2, 1, 3, 1)
    board.print()
    board.make_move(1, 2, 2, 1)
    board.print()
    print("Current state: ", board.get_current_state())


    board = PawnBoard()
    board.print()
    board.make_move(0, 0, 1, 0)
    board.print()
    board.make_move(0, 2, 1, 2)
    board.print()
    board.make_move(3, 1, 2, 1)
    board.print()
    print("Current state: ", board.get_current_state())
    board.make_move(2, 1, 1, 2)
    board.print()
    board.make_move(1, 2, 0, 3)
    board.print()
    print("Current state: ", board.get_current_state())

    board = PawnBoard()
    board.print()
    board.make_move(0, 0, 1, 0)
    board.print()
    board.make_move(0, 2, 1, 2)
    board.print()
    board.make_move(3, 1, 2, 1)
    board.print()
    print("Current state: ", board.get_current_state())
    board.make_move(2, 1, 1, 1)
    board.print()
    board.make_move(1, 0, 2, 0)
    board.print()
    board.make_move(1, 2, 2, 2)
    board.print()
    board.make_move(0, 3, 1, 3)
    board.print()
    board.make_move(3, 3, 2, 3)
    board.print()
    print("Current state: ", board.get_current_state())
if __name__ == "__main__":
    main()