class FiveBoard:
    """
    This class represents one round of the FiveBoard game. The game is like tic-tac-toe
    but with two changes:
    1. The board is 15x15
    2. A player wins if the corresponding token covers 5 consecutive cells

    __l : This is the 2D array that represents the current board
    __current_state: This is a string representing the current state of the game
    """

    __l = None
    __current_state = None

    def __init__(self):
        """
        This constructs a game in initial state, i.e. the 15x15 board is empty and the game
        state is unfinished
        """
        self.__current_state = "UNFINISHED"
        self.__l = []
        for i in range(15):
            temp = []
            for j in range(15):
                temp.append(" ")
            self.__l.append(temp)

    def get_current_state(self):
        """
        Return the current state of game
        :return: current state
        """
        return self.__current_state

    def make_move(self, row, col, value):
        """
        Try to make the move. And if the game finished, set the current state appropriately
        :param row: the row for move
        :param col: the col for move
        :param value: the token that we want to place at (row, col)
        :return: if the move is illegal, return False, True otherwise
        """
        if self.__l[row][col] != " ":
            return False

        elif self.get_current_state() in ["X_WON", "O_WON", "DRAW"]:  # return false if state is in any elements of list
            return False

        else:
            self.__l[row][col] = value  # change the value of board at given location
            self.setGameStateIfPossible()
            return True

    def print_board(self):
        """
        Prints the current board
        :return: None
        """
        for i in self.__l:
            print("|", end='')
            for j in i:
                print(j, end="|")
            print()
        print()

    def setGameStateIfPossible(self):
        """
        Check if the game is finished, if yes, set the game state appropriately
        :return: None
        """
        if self.checkHorizontalWin("x") or self.checkVerticalWin("x") \
                or self.checkForwardDiagonalWin("x") or self.checkReversedDiagonalWin("x"):
            self.__current_state = "X_WON"

        elif self.checkHorizontalWin("o") or self.checkVerticalWin("o") \
                or self.checkForwardDiagonalWin("o") or self.checkReversedDiagonalWin("o"):
            self.__current_state = "O_WON"

        elif self.isDraw():
            self.__current_state = "DRAW"

    def isDraw(self):
        """
        Check if the game is Draw
        :return: True if there are empty cells available, False otherwise
        """
        for row in self.__l:
            for element in row:
                if element == " ":
                    return False

        return True

    def checkHorizontalWin(self, param):
        """
        Check if there exists a horizontal row of 5 consecutive cells where all are param
        :param param: the token to check for
        :return: True if there exists such a row, False otherwise
        """
        for row in self.__l:
            for i in range(15 - 5):
                if row[i:i + 5] == [param] * 5:
                    return True

        return False

    def checkVerticalWin(self, param):
        """
        Check if there exists a vertical column of 5 consecutive cells where all are param
        :param param: the token to check for
        :return: True if there exists such a column, False otherwise
        """
        transposeBoard = [[row[i] for row in self.__l] for i in range(len(self.__l[0]))]

        for row in transposeBoard:
            for i in range(15 - 5):
                if row[i:i + 5] == [param] * 5:
                    return True

        return False

    def checkForwardDiagonalWin(self, param):
        """
        Check for presence of 5 consecutive params in the forward diagonal, i.e. \
        :param param: The token to check
        :return: True if there exists such a diagonal, False otherwise
        """
        for i in range(15 - 5):
            for j in range(15 - 5):
                if self.ifForwardDiagonalMatch(i, j, [param] * 5):
                    return True
        return False

    def checkReversedDiagonalWin(self, param):
        """
        Check for presence of 5 consecutive params in the reverse diagonal, i.e. /
        :param param: The token to check
        :return: True if there exists such a diagonal, False otherwise
        """
        for i in range(15 - 5):
            for j in range(4, 15):
                if self.ifReverseDiagonalMatch(i, j, [param] * 5):
                    return True
        return False

    def ifForwardDiagonalMatch(self, i, j, param):
        """
        Check for presence of 5 consecutive params in the forward diagonal, i.e. \ that is
        starting at i,j
        :param i: The row
        :param j: The column
        :param param: The token to check
        :return: True if there exists such a diagonal, False otherwise
        """
        diag = []
        for x in range(5):
            diag.append(self.__l[i + x][j + x])
        if diag == param:
            return True
        return False

    def ifReverseDiagonalMatch(self, i, j, param):
        """
        Check for presence of 5 consecutive params in the reverse diagonal, i.e. / that is
        starting at i,j
        :param i: The row
        :param j: The column
        :param param: The token to check
        :return: True if there exists such a diagonal, False otherwise
        """
        diag = []
        for x in range(5):
            diag.append(self.__l[i + x][j - x])
        if diag == param:
            return True
        return False


# Print help information
help(FiveBoard)

# Game 1 - This is checking the linear win
board = FiveBoard()
board.make_move(0, 0, 'o')
board.make_move(6, 5, 'x')
board.make_move(2, 1, 'x')
board.make_move(3, 2, 'x')

print(board.get_current_state())
board.print_board()  # print the board

board.make_move(4, 3, 'x')
print(board.get_current_state())
board.print_board()  # print the board

board.make_move(5, 4, 'o')
print(board.get_current_state())
board.print_board()  # print the board

board.make_move(0, 1, 'o')
print(board.get_current_state())
board.print_board()  # print the board

board.make_move(0, 2, 'o')
print(board.get_current_state())
board.print_board()  # print the board

board.make_move(0, 3, 'o')
print(board.get_current_state())
board.print_board()  # print the board

board.make_move(0, 4, 'o')
print(board.get_current_state())
board.print_board()  # print the board

board.make_move(0, 5, 'o')
print(board.get_current_state())
board.print_board()  # print the board

# Game 2 - This is checking the diagonal win case
board = FiveBoard()
board.make_move(0, 0, 'o')
board.make_move(6, 5, 'x')
board.make_move(2, 1, 'x')
board.make_move(3, 2, 'x')

print(board.get_current_state())
board.print_board()  # print the board

board.make_move(4, 3, 'x')
print(board.get_current_state())
board.print_board()  # print the board

board.make_move(5, 4, 'x')
print(board.get_current_state())
board.print_board()  # print the board

board.make_move(0, 1, 'o')
print(board.get_current_state())
board.print_board()  # print the board
