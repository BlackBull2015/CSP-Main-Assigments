package Tic_tac_toe;

// Fig. 18.9: TicTacToeClient.java
// Client that let a user play Tic-Tac-Toe with another across a network.
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class   TicTacToeClient extends JApplet implements Runnable {
   private JTextField idField;
   private JTextArea displayArea;
   private JButton jcomp1;
   private JPanel boardPanel, panel2;
   private Square board[][], currentSquare;
   private Socket connection;
   private DataInputStream input;
   private DataOutputStream output;
   private char myMark;
   private boolean myTurn;
   private final char X_MARK = 'X', O_MARK = 'O';
   private boolean isOver = false;
   private static final Logger logger = Logger.getLogger("logger");

   // Set up user-interface and board
   public void init()
   {
      logger.entering(getClass().getName(), "Init started");
      Container container = getContentPane();
 
      // set up JTextArea to display messages to user
      displayArea = new JTextArea( 4, 30 );
      jcomp1 = new JButton("Reset");
      jcomp1.setBounds (225, 160, 140, 20);
      add (jcomp1);

      displayArea.setEditable( false );
      container.add( new JScrollPane( displayArea ), BorderLayout.SOUTH );

      // set up panel for squares in board
      boardPanel = new JPanel();
      boardPanel.setLayout( new GridLayout( 3, 3, 0, 0 ) );

      // create board
      board = new Square[ 3 ][ 3 ];

      // When creating a Square, the location argument to the constructor 
      // is a value from 0 to 8 indicating the position of the Square on 
      // the board. Values 0, 1, and 2 are the first row, values 3, 4, 
      // and 5 are the second row. Values 6, 7, and 8 are the third row.
      for ( int row = 0; row < board.length; row++ ) {

         for ( int column = 0; column < board[ row ].length; column++ ) {

            // create Square
            board[ row ][ column ] = new Square( ' ', row * 3 + column );
            boardPanel.add( board[ row ][ column ] );        
         }
      }

      // textfield to display player's mark
      idField = new JTextField();
      idField.setEditable( false );
      container.add( idField, BorderLayout.NORTH );
      
      // set up panel to contain boardPanel (for layout purposes)
      panel2 = new JPanel();
      panel2.add( boardPanel, BorderLayout.CENTER );
      container.add( panel2, BorderLayout.CENTER );

      logger.log(Level.INFO, "Jpanel created");
     //Sets inactive
      jcomp1.setVisible(false);

      jcomp1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if(isOver){

               for ( int row = 0; row < board.length; row++ ) {

                  for (int column = 0; column < board[row].length; column++) {

                     // create Square
                     setMark(  board[ row ][ column ],' ' );
                     logger.log(Level.INFO, "Board Reseted");
                  }
               }
               try {
                  output.writeUTF("reset");
               } catch (IOException e1) {
                  e1.printStackTrace();
               }
            //   myMark = X_MARK;
           //    idField.setText( "You are player \"" + myMark + "\"" );
               jcomp1.setVisible(false);
               logger.log(Level.INFO, "Game reseted");
            }


         }
      });

   } // end method init

   // Make connection to server and get associated streams.
   // Start separate thread to allow this applet to
   // continually update its output in textarea display.
   public void start()
   {
      // connect to server, get streams and start outputThread
      try {
         
         // make connection
         connection = new Socket( getCodeBase().getHost(), 12345 );

         // get streams
         input = new DataInputStream( connection.getInputStream() );
         output = new DataOutputStream( connection.getOutputStream() );
         logger.log(Level.INFO, "Connection established");
      }


      // catch problems setting up connection and streams
      catch ( IOException ioException ) {
         ioException.printStackTrace();         
      }

      // create and start output thread
      Thread outputThread = new Thread( this );
      outputThread.start();

   } // end method start

   // control thread that allows continuous update of displayArea
   public void run()
   {
      logger.log(Level.INFO, "Inside Run");
      // get player's mark (X or O)
      try {
         myMark = input.readChar();

         // display player ID in event-dispatch thread
         SwingUtilities.invokeLater( 
            new Runnable() {         
               public void run()
               {
                  idField.setText( "You are player \"" + myMark + "\"" );
                  logger.log(Level.INFO, "Gives you player mark");
               }
            }
         ); 
         
         myTurn = ( myMark == X_MARK ? true : false );

         // receive messages sent to client and output them
         while ( true ) {
            processMessage( input.readUTF() );

         }

      } // end try

      // process problems communicating with server
      catch ( IOException ioException ) {
         ioException.printStackTrace();         
      }

   }  // end method run

   // process messages received by client
   private void processMessage( String message ) throws IOException {
      logger.log(Level.INFO, "Processing messages");
      System.out.println(message);
      // invalid move occurred
      if(message.equals( "PlayerX has won"  ) && myMark == 'X')
      {
         displayMessage("you have won\n");
         jcomp1.setVisible(true);
         isOver = true;
      }
      else if (message.equals("PlayerO has won" ) && myMark == 'X')
      {
         displayMessage("you have lost\n");
         jcomp1.setVisible(true);
         isOver = true;
      }

      else if(message.equals( "PlayerX has won" ) && myMark == 'O')
      {
         displayMessage("you have lost\n");
         jcomp1.setVisible(true);
         isOver = true;
      }
      else if(message.equals( "PlayerO has won"  ) && myMark == 'O')
      {
         displayMessage("you have won\n");
         jcomp1.setVisible(true);
         isOver = true;

      }
      // valid move occurred
      else if ( message.equals( "Valid move." ) ) {
         displayMessage( "Valid move, please wait.\n" );
         setMark( currentSquare, myMark );
      }

      else if ( message.equals( "Invalid move, try again" ) ) {
         displayMessage( message + "\n" );
         myTurn = true;
      }
      else if ( message.equals("ChangeSignO") ) {
         displayMessage( message + "\n" );
         myMark = O_MARK;
         idField.setText("You are player \"" + myMark + "\"");
         myTurn = false;
      }
      else if ( message.equals( "ChangeSignX")) {
         displayMessage( message + "\n" );
         myMark = X_MARK;
         idField.setText( "You are player \"" + myMark + "\"" );
         myTurn = true;
      }

      else if(message.equalsIgnoreCase("nextRound")){
         displayMessage("i start next turn");
         myTurn = true;
      }
      // opponent moved
      else if ( message.equals( "Opponent moved" ) ) {

         // get move location and update board
         try {
            int location = input.readInt();
            int row = location / 3;
            int column = location % 3;

            setMark(  board[ row ][ column ], 
               ( myMark == X_MARK ? O_MARK : X_MARK ) );                  
            displayMessage( "Opponent moved. Your turn.\n" );
            myTurn = true;

         }// end try

         // process problems communicating with server
         catch ( IOException ioException ) {
            ioException.printStackTrace();         
         }

      } // end else if
      else if ( message.equals( "reset" ) ) {
         for ( int row = 0; row < board.length; row++ ) {

            for (int column = 0; column < board[row].length; column++) {

               // create Square
               setMark(  board[ row ][ column ],' ' );
            }
         }
         displayMessage( "Game reseted" + "\n" );
         jcomp1.setVisible(false);

      }
      // simply display message
      else if ( message.equals( "You have lost" ) ) {
         int location = input.readInt();
         int row = location / 3;
         int column = location % 3;

         setMark(  board[ row ][ column ],
                 ( myMark == X_MARK ? O_MARK : X_MARK ) );
         displayMessage( message + "\n" );
     //    jcomp1.setVisible(true);
     //    myTurn = true;
      }
      else
         displayMessage( message + "\n" );

   } // end method processMessage

   // utility method called from other threads to manipulate 
   // outputArea in the event-dispatch thread
   private void displayMessage( final String messageToDisplay )
   {
      logger.log(Level.INFO, "Displaying messages");
      // display message from event-dispatch thread of execution
      SwingUtilities.invokeLater(
         new Runnable() {  // inner class to ensure GUI updates properly

            public void run() // updates displayArea
            {
               displayArea.append( messageToDisplay );
               displayArea.setCaretPosition( 
                  displayArea.getText().length() );
            }

         }  // end inner class

      ); // end call to SwingUtilities.invokeLater
   }

   // utility method to set mark on board in event-dispatch thread
   private void setMark( final Square squareToMark, final char mark )
   {
      logger.log(Level.INFO, "Set a Mark");
      SwingUtilities.invokeLater(
         new Runnable() {
            public void run()
            {
               squareToMark.setMark( mark );
            }
         }
      ); 
   }

   // send message to server indicating clicked square
   public void sendClickedSquare( int location )
   {
      logger.log(Level.INFO, "Send clicked square");
      if ( myTurn ) {

         // send location to server
         try {
            output.writeInt( location );
            myTurn = false;
         }

         // process problems communicating with server
         catch ( IOException ioException ) {
            ioException.printStackTrace();
         }
      }
   }

   // set current Square
   public void setCurrentSquare( Square square )
   {
      currentSquare = square;
   }

   // private inner class for the squares on the board
   private class Square extends JPanel {
      private char mark;
      private int location;
   
      public Square( char squareMark, int squareLocation )
      {
         mark = squareMark;
         location = squareLocation;

         addMouseListener(
                 new MouseAdapter() {
                    public void mouseReleased( MouseEvent e )
               {
                  setCurrentSquare( Square.this );
                  sendClickedSquare( getSquareLocation() );
                  logger.log(Level.INFO, "Box clicked");
               }
            }  
         );

      } // end Square constructor

      // return preferred size of Square
      public Dimension getPreferredSize() 
      { 
         return new Dimension( 30, 30 );
      }

      // return minimum size of Square
      public Dimension getMinimumSize() {
         return getPreferredSize();
      }

      // set mark for Square
      public void setMark( char newMark ) 
      { 
         mark = newMark; 
         repaint();
         logger.log(Level.INFO, "mark set");
      }
   
      // return Square location
      public int getSquareLocation() 
      {
         return location; 
      }
   
      // draw Square
      public void paintComponent( Graphics g )
      {
         super.paintComponent( g );

         g.drawRect( 0, 0, 29, 29 );
         g.drawString( String.valueOf( mark ), 11, 20 );   
      }

   } // end inner-class Square
 
} // end class TicTacToeClient

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
