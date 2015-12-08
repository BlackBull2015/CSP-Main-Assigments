package Tic_tac_toe;



// Fig. 18.8: TicTacToeServer.java
// This class maintains a game of Tic-Tac-Toe for two client applets.
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import javax.swing.*;
import java.util.logging.*;


public class      TicTacToeServer extends JFrame {
   private char[] board;           
   private JTextArea outputArea;
   private Player[] players;
   private ServerSocket server;
   private int currentPlayer;
   private final int PLAYER_X = 0, PLAYER_O = 1;
   private final char X_MARK = 'X', O_MARK = 'O';
   int endm = 0;
   int s =0;
   int resetFlag= 0;
   boolean RoundNotOver = true;
   int Winner;
   int Looser;
  // public static final Logger global;
 //  global = new Logger("name","two");

   ExecutorService Executes = Executors.newFixedThreadPool(2);

   private static final Logger logger = Logger.getLogger("logger");


   Board b1 = new Board();

   // set up tic-tac-toe server and GUI that displays messages
   public TicTacToeServer()
   {
      super("Tic-Tac-Toe Server");

      logger.entering(getClass().getName(), "Server has started");

      board = new char[ 9 ];
      players = new Player[ 2 ];
      currentPlayer = PLAYER_X;
      logger.log(Level.INFO, "Variables Set");
      // set up ServerSocket
      try {
         server = new ServerSocket( 12345, 2 );
         logger.log(Level.INFO, "Created sever socket");
      }

      // process problems creating ServerSocket
      catch( IOException ioException ) {
         ioException.printStackTrace();
         System.exit( 1 );
         logger.log(Level.FINE, "Error in socket");
      }

      // set up JTextArea to display messages during execution
      outputArea = new JTextArea();
      getContentPane().add( outputArea, BorderLayout.CENTER );
      outputArea.setText( "Server awaiting connections\n" );

      setSize( 300, 300 );
      setVisible(true);
      logger.log(Level.INFO, "Cretes panel");

   } // end TicTacToeServer constructor

   // wait for two connections so game can be played
   public void execute()
   {
      logger.entering(getClass().getName(), "Executor has started");

      Executes.execute(new Runnable() {
         @Override
         public void run() {
            // wait for each client to connect
            for ( int i = 0; i < players.length; i++ ) {

               // wait for connection, create Player, start thread
               try {
                  players[ i ] = new Player( server.accept(), i );
                  players[ i ].start();
                  logger.log(Level.INFO, "Player set");
               }


               // process problems receiving connection from client
               catch( IOException ioException ) {
                  ioException.printStackTrace();
                  System.exit( 1 );
               }
            }
            logger.log(Level.INFO, "Players Set");
            // Player X is suspended until Player O connects.
            // Resume player X now.
            synchronized ( players[ PLAYER_X ] ) {
               players[ PLAYER_X ].setSuspended( false );
               players[ PLAYER_X ].notify();
            }
            logger.log(Level.INFO, "Started");
         }
      });

  
   }  // end method execute
   
   // utility method called from other threads to manipulate 
   // outputArea in the event-dispatch thread
   private void displayMessage( final String messageToDisplay )
   {
      logger.entering(getClass().getName(), "Inside message");
      // display message from event-dispatch thread of execution
      SwingUtilities.invokeLater(
              new Runnable() {  // inner class to ensure GUI updates properly

                 public void run() // updates outputArea
                 {
                    outputArea.append(messageToDisplay);
                    outputArea.setCaretPosition(
                            outputArea.getText().length());
                    logger.log(Level.INFO, "Message Displayed");
                 }

              }  // end inner class

      ); // end call to SwingUtilities.invokeLater
   }

   // Determine if a move is valid. This method is synchronized because 
   // only one move can be made at a time.
   public synchronized boolean validateAndMove( int location, int player )
   {
      logger.entering(getClass().getName(), "Validation");
      boolean moveDone = false;

      // while not current player, must wait for turn
      while ( player != currentPlayer ) {

         // wait for turn
         try {
            wait();
            logger.log(Level.INFO, "Waiting");
         }

         // catch wait interruptions
         catch( InterruptedException interruptedException ) {
            interruptedException.printStackTrace();
         }
      }

      // if location not occupied, make move
      if ( !b1.isOcupited(location) ) {
         logger.log(Level.INFO, "Place not ocupied");
         // set move in board array
         b1.setMark(location, (currentPlayer == PLAYER_X ? X_MARK : O_MARK));
         //board[ location ] = currentPlayer == PLAYER_X ? X_MARK : O_MARK;
         logger.log(Level.INFO, "Mark placed");

         // change current player
         currentPlayer = ( currentPlayer + 1 ) % 2;
         logger.log(Level.INFO, "Player changed");
         // let new current player know that move occurred
         if(!b1.isOver()) {
            players[currentPlayer].otherPlayerMoved(location, currentPlayer);
            logger.log(Level.INFO, "Came continues not over");
            notify(); // tell waiting player to continue
         }
         else{
            players[currentPlayer].gameOver(location);
            Looser = currentPlayer%2;
            Winner = (currentPlayer +1 )%2;
            logger.log(Level.INFO, "game over starting new game");

         }

         // tell player that made move that the move was valid
         return true;
      }

      // tell player that made move that the move was not valid
      else
         return false;

   } // end method validateAndMove




   public static void main( String args[] )
   {
      logger.entering("main", "Main has startes");
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            TicTacToeServer application = new TicTacToeServer();
            application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            application.execute();
         }
      });

   }



   // private inner class Player manages each Player as a thread
   private class Player extends Thread {

      private Socket connection;
      private DataInputStream input;
      private DataOutputStream output;
      private int playerNumber;
      private char mark;
      protected boolean suspended = true;




      // set up Player thread
      public Player( Socket socket, int number )
      {
         logger.entering(getClass().getName(), "Thread constructior started");
         playerNumber = number;

         // specify player's mark
         mark = ( playerNumber == PLAYER_X ? X_MARK : O_MARK );
         logger.log(Level.INFO, "Getting a Player mark ");
         connection = socket;
         
         // obtain streams from Socket
         try {
            input = new DataInputStream( connection.getInputStream() );
            output = new DataOutputStream( connection.getOutputStream() );
            logger.log(Level.INFO, "Setting IO streams");
         }

         // process problems getting streams
         catch( IOException ioException ) {
            ioException.printStackTrace();
            System.exit( 1 );
         }

      } // end Player constructor

      // send message that other player moved

      public void SendReset() throws IOException {

         output.writeUTF("reset");
         logger.log(Level.INFO, "Game resets");
         currentPlayer = 0;
      }

      public void otherPlayerMoved( int location , int player)
      {
            System.out.println(location + " and player is " + player);

            // send message indicating move
            try {


               output.writeUTF("Opponent moved");
               output.writeInt(location);

            }

            // process problems sending message
            catch (IOException ioException) {
               ioException.printStackTrace();
            }


      }

      public void gameOver(int location){
         try {
            output.writeUTF("You have lost");
            output.writeInt(location);
            logger.log(Level.INFO, "You have lost message");
         } catch (IOException e) {
            e.printStackTrace();
         }
         RoundNotOver = false;
      }

      public void ChangeToO() throws IOException{
         output.writeUTF("ChangeSignO");
         logger.log(Level.INFO, "Changing sign");
      }
      public void ChangeToX() throws IOException{
         output.writeUTF("ChangeSignX");
         logger.log(Level.INFO, "Changing sign");
      }
      public void WakeUp() throws IOException {
         output.writeUTF("nextRound");
         logger.log(Level.INFO, "Starting next round");
      }
      // control thread's execution

              //.execute(new Runnable() {

      public void run()
      {
         // send client message indicating its mark (X or O),
         // process messages from client
         try {
            displayMessage( "Player " + ( playerNumber ==
               PLAYER_X ? X_MARK : O_MARK ) + " connected\n" );
            logger.log(Level.INFO, "Gets players connected");

            output.writeChar( mark ); // send player's mark

            // send message indicating connection
            output.writeUTF( "Player " + ( playerNumber == PLAYER_X ?
               "X connected\n" : "O connected, please wait\n" ) );
            logger.log(Level.INFO, "Give player a number");

            // if player X, wait for another player to arrive
            if ( mark == X_MARK ) {
               output.writeUTF( "Waiting for another player" );

               // wait for player O
               try {
                  synchronized( this ) {
                     while ( suspended )
                        wait();
                     logger.log(Level.INFO, "Waits for another player");
                  }
               }

               // process interruptions while waiting
               catch ( InterruptedException exception ) {
                  exception.printStackTrace();
               }

               // send message that other player connected and
               // player X can make a move
               output.writeUTF( "Other player connected. Your move." );
               logger.log(Level.INFO, "Game starts");
            }


            //while to reset
            boolean reset =true;
            while(reset) {


               // while game not over
                 while(RoundNotOver){
                    logger.log(Level.INFO, "in Game");
                    resetFlag = 0;
//                    for(int z = 0; z < 9; z++){
//                       System.out.print(b1.getMark(z));
//                       System.out.println("");
//                    }
                  System.out.println("in "+endm);

                  // get move location from client
                  int location = input.readInt();
                    logger.log(Level.INFO, "Read move");
                  // check for valid move
                  if (validateAndMove(location, playerNumber)) {
                     displayMessage("\nlocation: " + location);
                     output.writeUTF("Valid move.");
                     logger.log(Level.INFO, "Move validated");
                  } else
                     output.writeUTF("Invalid move, try again");
               }
               logger.log(Level.INFO, "Game over");
               System.out.println(playerNumber);
               if (s == 0) {
                  if (playerNumber == 0)
                     output.writeUTF("PlayerX has won");
                  else if (playerNumber == 1)
                     output.writeUTF("PlayerO has won");
                  logger.log(Level.INFO, "INSIDE FIRST LOOP OF GAME OVER");
               } else {
                  if (playerNumber == 1)
                     output.writeUTF("PlayerX has won");
                  else if (playerNumber == 0)
                     output.writeUTF("PlayerO has won");
                  logger.log(Level.INFO, "INSIDE SECOND LOOP OF GAME OVER");
               }
               logger.log(Level.INFO, "Player info sent");
               s++;
               System.out.println("It is over");

               System.out.println("out"+endm);

               if(input.readUTF().equalsIgnoreCase("reset")){
                  resetFlag = 1;
                  logger.log(Level.INFO, "game reset flag connected");

               }


               if(resetFlag == 1){
                  System.out.println("inside if");
                  b1.clear();
                  players[0].SendReset();
                  players[1].SendReset();
                  RoundNotOver = true;
                  players[Looser].ChangeToO();
                  players[Winner].ChangeToX();
                  players[Winner].WakeUp();
                  //players[0].WakeUp();
                  //output.writeUTF("reset");
                  currentPlayer = Winner;
                  endm = 0;
                  s = 0;
                  resetFlag = 0;
                  logger.log(Level.INFO, "GAME HAS BEEN RESETED");
               }
             //  System.out.println(board.toString());
               System.out.println();

            }


            connection.close(); // close connection to client
            logger.log(Level.INFO, "Connection closed");

         } // end try

         // process problems communicating with client
         catch( IOException ioException ) {
            ioException.printStackTrace();
            System.exit( 1 );
         }

      } // end method run

      // set whether or not thread is suspended
      public void setSuspended( boolean status )
      {
         suspended = status;
      }

   } // end class Player

} // end class TicTacToeServer


/**************************************************************************
 * (C) Copyright 1992-2003 by Deitel & Associates, Inc. and               *
 * Prentice Hall. All Rights Reserved.                                    *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/
