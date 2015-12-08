// Fig. 18.3: ReadServerFile.java
// Use a JEditorPane to display the contents of a file on a Web server.
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;

public class ReadServerFile extends JFrame {
   private JTextField enterField;
   private JEditorPane contentsArea;

   // set up GUI
   public ReadServerFile()
   {
      super( "Simple Web Browser" );

      Container container = getContentPane();

      // create enterField and register its listener
      enterField = new JTextField( "Enter file URL here" );
      enterField.addActionListener(
         new ActionListener() {

            // get document specified by user
            public void actionPerformed( ActionEvent event )
            {
               getThePage( event.getActionCommand() );
            }

         } // end inner class

      ); // end call to addActionListener

      container.add( enterField, BorderLayout.NORTH );

      // create contentsArea and register HyperlinkEvent listener
      contentsArea = new JEditorPane();
      contentsArea.setEditable( false );
      contentsArea.addHyperlinkListener(
         new HyperlinkListener() {

            // if user clicked hyperlink, go to specified page
            public void hyperlinkUpdate( HyperlinkEvent event )
            {
               if ( event.getEventType() == 
                    HyperlinkEvent.EventType.ACTIVATED )
                  getThePage( event.getURL().toString() );
            }

         } // end inner class

      ); // end call to addHyperlinkListener

      container.add( new JScrollPane( contentsArea ), 
         BorderLayout.CENTER );
      setSize( 400, 300 );
      setVisible( true );

   } // end constructor ReadServerFile

   // load document
   private void getThePage( String location )
   {
      // load document and display location 
      try {
         contentsArea.setPage( location );
         enterField.setText( location );
      }
      catch ( IOException ioException ) {
         JOptionPane.showMessageDialog( this, 
            "Error retrieving specified URL", "Bad URL", 
            JOptionPane.ERROR_MESSAGE );
      }

   } // end method getThePage

   public static void main( String args[] )
   {
      ReadServerFile application = new ReadServerFile();
      application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
   }

} // end class ReadServerFile


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
