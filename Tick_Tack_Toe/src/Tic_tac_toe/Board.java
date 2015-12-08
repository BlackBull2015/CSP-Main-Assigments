package Tic_tac_toe;

/**
 * Created by g00291410 on 27/10/2015.
 */
public class   Board {

    private char board[] = new char[9];
    private final char X_MARK = 'X', O_MARK = 'O';

    int setMark(int location, char mark){

        board[location] = mark;
        return 1;
    }


    boolean isOver(){
        int endm = 0;
        int rawcol = 0;
        char plX = 'X';
        char plO = 'O';

        for(int i = 0; i < 3; i++)
        {
            if(board[rawcol] == plX &&  board[rawcol + 1] == plX && board[rawcol + 2] == plX )
                endm  = 1;

            else if(board[rawcol ] == plO && board[rawcol + 1] == plO && board[rawcol + 2] == plO )
                endm  = 1;

            rawcol += 3;
        }

        for(int i = 0; i < 3; i++)
        {
            if(board[i] == plX && board[i +3] == plX && board[i +6] == plX )
                endm  = 1;
            else if(board[i + 0] == plO && board[i + 3] == plO && board[i + 6] == plO )
                endm  = 1;
        }

        if(board[0] == plX && board[4] == plX && board[8] == plX )
            endm  = 1;
        else if(board[0] == plO && board[4] == plO && board[8] == plO )
            endm  = 1;

        if(board[2] == plX && board[4] == plX && board[6] == plX )
            endm  = 1;

        else if(board[2] == plO && board[4] == plO && board[6] == plO )
            endm  = 1;

        if(endm  == 1) {
            System.out.println("True");
            return true;
        }
        else {
            System.out.println("False");
            return false;

        }
    }

    boolean isOcupited(int location){
        if ( board[ location ] == X_MARK || board [ location ] == O_MARK )
            return true;
        else
            return false;
    }

    char getMark(int location ){
        return board[location];

    }

     void clear(){
        for(int i = 0; i<9; i++)
            board[i] = ' ';
    }





}
