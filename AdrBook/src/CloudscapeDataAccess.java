// Fig. 8.36: CloudscapeDataAccess.java
// An implementation of interface AddressBookDataAccess that 
// performs database operations with PreparedStatements.

// Java core packages
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;

public class CloudscapeDataAccess 
   implements AddressBookDataAccess {
   private static final Logger logger = Logger.getLogger("logger");
   // reference to database connection
   private Connection connection;
      
   // reference to prepared statement for locating entry
   private PreparedStatement sqlFind;

   // reference to prepared statement for determining personID
   private PreparedStatement sqlPersonID;

   // references to prepared statements for inserting entry
   private PreparedStatement sqlInsertName;
   private PreparedStatement sqlInsertAddress;
   private PreparedStatement sqlInsertPhone;
   private PreparedStatement sqlInsertEmail;
   
   // references to prepared statements for updating entry
   private PreparedStatement sqlUpdateName;
   private PreparedStatement sqlUpdateAddress;
   private PreparedStatement sqlUpdatePhone;
   private PreparedStatement sqlUpdateEmail;
   
   // references to prepared statements for updating entry
   private PreparedStatement sqlDeleteName;
   private PreparedStatement sqlDeleteAddress;
   private PreparedStatement sqlDeletePhone;
   private PreparedStatement sqlDeleteEmail;

   // set up PreparedStatements to access database
   public CloudscapeDataAccess() throws Exception
   {
      // connect to addressbook database
      connect();

      // locate person
      sqlFind = connection.prepareStatement(
           "SELECT names.personID, firstName, lastName, " +
                   "addressID, address1, city, eircode, " +
                   "county,altaddress, altcity, alteircode, altcounty, phoneID, phoneNumber,mobilePhone,altPhone, emailID, " +
                   "emailAddress,altEmain " +
                   "FROM names, addresses, phonenumbers, emailaddresses " +
                   "WHERE lastName = ? AND " +
                   "names.personID = addresses.personID AND " +
                   "names.personID = phonenumbers.personID AND " +
                   "names.personID = emailaddresses.personID" );
      
      // Obtain personID for last person inserted in database.
      // [This is a Cloudscape-specific database operation.]
      //********************ERROR PLACE**********************
     /*sqlPersonID = connection.prepareStatement(
         "VALUES ConnectionInfo.lastAutoincrementValue( " +
            "'APP', 'NAMES', 'PERSONID')" );*/
      sqlPersonID = connection.prepareStatement("select last_insert_id()");

      // Insert first and last names in table names. 
      // For referential integrity, this must be performed 
      // before sqlInsertAddress, sqlInsertPhone and
      // sqlInsertEmail.
      sqlInsertName = connection.prepareStatement(
         "INSERT INTO names ( firstName, lastName ) " +
         "VALUES ( ? , ? )" );

      // insert address in table addresses
      sqlInsertAddress = connection.prepareStatement(
         "INSERT INTO addresses ( personID, address1, city, eircode, county,altaddress,altcity,alteircode,altcounty ) " +
         "VALUES ( ? ,  ? , ? , ? , ? ,? ,? ,? ,? )" );

      // insert phone number in table phoneNumbers
      sqlInsertPhone = connection.prepareStatement(
         "INSERT INTO phoneNumbers " +
            "( personID, phoneNumber,mobilePhone,altPhone) " +
         "VALUES ( ? , ? ,?,?)" );

      // insert email in table emailAddresses
      sqlInsertEmail = connection.prepareStatement(
         "INSERT INTO emailAddresses " +
            "( personID, emailAddress,altEmain ) " +
         "VALUES ( ? , ?,? )" );

      // update first and last names in table names
      sqlUpdateName = connection.prepareStatement(
         "UPDATE names SET firstName = ?, lastName = ? " +
         "WHERE personID = ?" );

      // update address in table addresses
      sqlUpdateAddress = connection.prepareStatement(
         "UPDATE addresses SET address1 = ?,  " +
            "city = ?, eircode = ?, county = ? , altaddress= ?, altcity = ? , alteircode = ? , altcounty= ?   " +
         "WHERE addressID = ?" );

      // update phone number in table phoneNumbers
      sqlUpdatePhone = connection.prepareStatement(
         "UPDATE phoneNumbers SET phoneNumber = ?, mobilePhone = ?, altPhone = ? " +
         "WHERE phoneID = ?" );

      // update email in table emailAddresses
      sqlUpdateEmail = connection.prepareStatement(
         "UPDATE emailAddresses SET emailAddress = ?, altEmain = ? " +
         "WHERE emailID = ?" );

      // Delete row from table names. This must be executed 
      // after sqlDeleteAddress, sqlDeletePhone and
      // sqlDeleteEmail, because of referential integrity.
      sqlDeleteName = connection.prepareStatement(
         "DELETE FROM names WHERE personID = ?" );

      // delete address from table addresses
      sqlDeleteAddress = connection.prepareStatement(
         "DELETE FROM addresses WHERE personID = ?" );

      // delete phone number from table phoneNumbers
      sqlDeletePhone = connection.prepareStatement(
         "DELETE FROM phoneNumbers WHERE personID = ?" );

      // delete email address from table emailAddresses
      sqlDeleteEmail = connection.prepareStatement(
         "DELETE FROM emailAddresses WHERE personID = ?" );
   }  // end CloudscapeDataAccess constructor
   
   // Obtain a connection to addressbook database. Method may 
   // may throw ClassNotFoundException or SQLException. If so, 
   // exception is passed via this class's constructor back to 
   // the AddressBook application so the application can display
   // an error message and terminate.
   private void connect() throws Exception
   {
      // Cloudscape database driver class name
      String driver = "com.mysql.jdbc.Driver";
     
      // URL to connect to addressbook database
      String url = "jdbc:mysql://localhost:3306/addressbook";
      
      // load database driver class
      Class.forName( driver );

      // connect to database
      connection = DriverManager.getConnection( url,"root","root" );

      // Require manual commit for transactions. This enables 
      // the program to rollback transactions that do not 
      // complete and commit transactions that complete properly.
      connection.setAutoCommit(false);
   }
   
   // Locate specified person. Method returns AddressBookEntry
   // containing information.
   public  ArrayList<AddressBookEntry> findPerson( String lastName )
   {
      ArrayList<AddressBookEntry> People = new ArrayList<>();
      logger.log(Level.INFO, "Find Person");
      try {
         // set query parameter and execute query
         logger.log(Level.INFO,"Searching for person");
         sqlFind.setString( 1, lastName );
         ResultSet resultSet = sqlFind.executeQuery();

         // if no records found, return immediately
//         if ( !resultSet.next() ) {
//            logger.log(Level.INFO,"Did not found anything");
//            return null;
//         }
//         logger.log(Level.INFO,"Got person");

         // create new AddressBookEntry

         while (resultSet.next()) {
            AddressBookEntry person = new AddressBookEntry(
                    resultSet.getInt(1));

            // set AddressBookEntry properties
            person.setFirstName(resultSet.getString(2));
            person.setLastName(resultSet.getString(3));

            person.setAddressID(resultSet.getInt(4));
            person.setAddress1(resultSet.getString(5));
            person.setCity(resultSet.getString(6));
            person.setEircode(resultSet.getString(7));
            person.setCounty(resultSet.getString(8));
            person.setAltaddress(resultSet.getString(9));
            person.setAltcity(resultSet.getString(10));
            person.setAlteircode(resultSet.getString(11));
            person.setAltcounty(resultSet.getString(12));

            person.setPhoneID(resultSet.getInt(13));
            person.setPhoneNumber(resultSet.getString(14));
            person.setHomephone(resultSet.getNString(15));
            person.setAltphone(resultSet.getString(16));

            person.setEmailID(resultSet.getInt(17));
            person.setEmailAddress(resultSet.getString(18));
            person.setAltamail(resultSet.getString(19));
            People.add(person);
         }
         // return AddressBookEntry
         return People;
      }
         
      // catch SQLException
      catch ( SQLException sqlException ) {
         logger.log(Level.INFO,"Thrown exception in person find");
         return null;
      }
   }  // end method findPerson
   
   // Update an entry. Method returns boolean indicating 
   // success or failure.
   public boolean savePerson( AddressBookEntry person )
      throws DataAccessException
   {
      logger.log(Level.INFO, "Save Person");
      // update person in database
      try {
         int result;
         
         // update names table
         sqlUpdateName.setString( 1, person.getFirstName() );
         sqlUpdateName.setString( 2, person.getLastName() );
         sqlUpdateName.setInt(3, person.getPersonID());
         result = sqlUpdateName.executeUpdate();
         logger.log(Level.INFO,"Name updated");

         // if update fails, rollback and discontinue 
         if ( result == 0 ) {
            connection.rollback(); // rollback update
            return false;          // update unsuccessful
         }      
         
         // update addresses table
         logger.log(Level.INFO,"Ready to update addresses");
         sqlUpdateAddress.setString(1, person.getAddress1());
         sqlUpdateAddress.setString( 2, person.getCity() );
         sqlUpdateAddress.setString( 3, person.getEircode() );
         sqlUpdateAddress.setString( 4, person.getCounty() );
         sqlUpdateAddress.setString( 5, person.getAltaddress() );
         sqlUpdateAddress.setString( 6, person.getAltcity() );
         sqlUpdateAddress.setString( 7, person.getAlteircode() );
         sqlUpdateAddress.setString( 8, person.getAltcounty() );
 sqlUpdateAddress.setInt(9, person.getAddressID());
 result = sqlUpdateAddress.executeUpdate();
         logger.log(Level.INFO,"Address updated");

 // if update fails, rollback and discontinue
 if ( result == 0 ) {
 connection.rollback(); // rollback update
 return false;          // update unsuccessful
 }
         logger.log(Level.INFO,"Ready to update phone");
 // update phoneNumbers table
 sqlUpdatePhone.setString( 1, person.getPhoneNumber() );
 sqlUpdatePhone.setString( 2, person.getHomephone());
 sqlUpdatePhone.setString( 3, person.getAltphone());
 sqlUpdatePhone.setInt(4, person.getPhoneID());
 result = sqlUpdatePhone.executeUpdate();
         logger.log(Level.INFO,"Phone updated");

 // if update fails, rollback and discontinue
 if ( result == 0 ) {
 connection.rollback(); // rollback update
 return false;          // update unsuccessful
 }

 // update emailAddresses table
         logger.log(Level.INFO,"Ready to update email");
 sqlUpdateEmail.setString( 1, person.getEmailAddress() );
         sqlUpdateEmail.setString( 2, person.getAltamail() );
 sqlUpdateEmail.setInt(3, person.getEmailID());
 result = sqlUpdateEmail.executeUpdate();
         logger.log(Level.INFO,"Email updated");

 // if update fails, rollback and discontinue
 if ( result == 0 ) {
 connection.rollback(); // rollback update
 return false;          // update unsuccessful
 }
         logger.log(Level.INFO, "Database updated");
                 connection.commit();   // commit update
 return true;           // update successful
 }  // end try

 // detect problems updating database
 catch ( SQLException sqlException ) {

 // rollback transaction
 try {
 connection.rollback(); // rollback update
    logger.log(Level.INFO, "Did roolback ");
 return false;          // update unsuccessful
 }

 // handle exception rolling back transaction
 catch ( SQLException exception ) {
 throw new DataAccessException( exception );
 }
 }
 }  // end method savePerson

 // Insert new entry. Method returns boolean indicating
 // success or failure.
 public boolean newPerson( AddressBookEntry person )
 throws DataAccessException
 {
 logger.log(Level.INFO, "New Person");
 // insert person in database
 try {
 int result;

 // insert first and last name in names table
 sqlInsertName.setString( 1, person.getFirstName() );
 sqlInsertName.setString( 2, person.getLastName() );
 result = sqlInsertName.executeUpdate();

 // if insert fails, rollback and discontinue
 if ( result == 0 ) {
 connection.rollback(); // rollback insert
 logger.log(Level.INFO, "ROLLBACK");
 return false;          // insert unsuccessful

 }

 // determine new personID
 logger.log(Level.INFO, "determine new personID");
 ResultSet resultPersonID = sqlPersonID.executeQuery();
 logger.log(Level.INFO, "Gets person id");
 if ( resultPersonID.next() ) {
 int personID =  resultPersonID.getInt( 1 );
 logger.log(Level.INFO, "Prepering to inster data");
 // insert address in addresses table
 sqlInsertAddress.setInt( 1, personID );
 sqlInsertAddress.setString(2,
 person.getAddress1());
 logger.log(Level.INFO, person.getAddress1());
 sqlInsertAddress.setString(3,
 person.getCity());
 logger.log(Level.INFO, person.getCity());
 sqlInsertAddress.setString(4,
 person.getEircode());
 logger.log(Level.INFO, person.getEircode());
 sqlInsertAddress.setString(5,
 person.getCounty());
 sqlInsertAddress.setString(6,
 person.getAltaddress());
 sqlInsertAddress.setString(7,
 person.getAltcity());
 sqlInsertAddress.setString(8,
 person.getAlteircode());
 sqlInsertAddress.setString(9,
 person.getAltcounty());
 logger.log(Level.INFO, person.getCounty());
 logger.log(Level.INFO, "Data inserted");
 result = sqlInsertAddress.executeUpdate();
 logger.log(Level.INFO, "Updates DATA");
 // if insert fails, rollback and discontinue
 if ( result == 0 ) {
 connection.rollback(); // rollback insert
 logger.log(Level.INFO, "ROLLBACK");
 return false;          // insert unsuccessful
 }
 logger.log(Level.INFO, "Prepared Phone Num");
 // insert phone number in phoneNumbers table
 sqlInsertPhone.setInt( 1, personID );
 sqlInsertPhone.setString(2,person.getPhoneNumber());
 sqlInsertPhone.setString(3,person.getHomephone());
 sqlInsertPhone.setString(4,person.getAltphone());
 logger.log(Level.INFO, "Ready to insert phone num");
 result = sqlInsertPhone.executeUpdate();
 logger.log(Level.INFO, "Phone updated");

 // if insert fails, rollback and discontinue
 if ( result == 0 ) {
 connection.rollback(); // rollback insert
 logger.log(Level.INFO, "ROLLBACK");
 return false;          // insert unsuccessful
 }
 logger.log(Level.INFO, "Prepares email");
 // insert email address in emailAddresses table
 sqlInsertEmail.setInt( 1, personID );
 sqlInsertEmail.setString(2,person.getEmailAddress());
 sqlInsertEmail.setString(3,person.getAltamail());
 result = sqlInsertEmail.executeUpdate();
 logger.log(Level.INFO, "email updated");

 // if insert fails, rollback and discontinue
 if ( result == 0 ) {
 connection.rollback(); // rollback insert
 logger.log(Level.INFO, "ROLLBACK");
 return false;          // insert unsuccessful

 }

 connection.commit();   // commit insert
 logger.log(Level.INFO, "Sucesfull :)");
 return true;           // insert successful
 }

 else {
 logger.log(Level.INFO, "Did not work");
 return false;
 }
 }  // end try

 // detect problems updating database
 catch ( SQLException sqlException ) {
 // rollback transaction
 try {
 connection.rollback(); // rollback update
 logger.log(Level.INFO, "Did rallback outside the main try");
 return false;          // update unsuccessful
 }

 // handle exception rolling back transaction
 catch ( SQLException exception ) {
 throw new DataAccessException( exception );
 }
 }
 }  // end method newPerson

 // Delete an entry. Method returns boolean indicating
 // success or failure.
 public boolean deletePerson( AddressBookEntry person )
 throws DataAccessException
 {System.out.println("Delete person");
 // delete a person from database
 try {
 int result;

 // delete address from addresses table
 sqlDeleteAddress.setInt( 1, person.getPersonID() );
 result = sqlDeleteAddress.executeUpdate();

 // if delete fails, rollback and discontinue
 if ( result == 0 ) {
 connection.rollback(); // rollback delete
 return false;          // delete unsuccessful
 }

 // delete phone number from phoneNumbers table
 sqlDeletePhone.setInt( 1, person.getPersonID() );
 result = sqlDeletePhone.executeUpdate();

 // if delete fails, rollback and discontinue
 if ( result == 0 ) {
 connection.rollback(); // rollback delete
 return false;          // delete unsuccessful
 }

 // delete email address from emailAddresses table
 sqlDeleteEmail.setInt( 1, person.getPersonID() );
 result = sqlDeleteEmail.executeUpdate();

 // if delete fails, rollback and discontinue
 if ( result == 0 ) {
 connection.rollback(); // rollback delete
 return false;          // delete unsuccessful
 }

 // delete name from names table
 sqlDeleteName.setInt( 1, person.getPersonID() );
 result = sqlDeleteName.executeUpdate();

 // if delete fails, rollback and discontinue
 if ( result == 0 ) {
 connection.rollback(); // rollback delete
 return false;          // delete unsuccessful
 }

 connection.commit();   // commit delete
 return true;           // delete successful
 }  // end try

 // detect problems updating database
 catch ( SQLException sqlException ) {
 // rollback transaction
 try {
 connection.rollback(); // rollback update
 return false;          // update unsuccessful
 }

 // handle exception rolling back transaction
 catch ( SQLException exception ) {
 throw new DataAccessException( exception );
 }
 }
 }  // end method deletePerson

 // method to close statements and database connection
 public void close()
 {
 // close database connection
 try {
 sqlFind.close();
 sqlPersonID.close();
 sqlInsertName.close();
 sqlInsertAddress.close();
 sqlInsertPhone.close();
 sqlInsertEmail.close();
 sqlUpdateName.close();
 sqlUpdateAddress.close();
 sqlUpdatePhone.close();
 sqlUpdateEmail.close();
 sqlDeleteName.close();
 sqlDeleteAddress.close();
 sqlDeletePhone.close();
 sqlDeleteEmail.close();
 connection.close();
 }  // end try

 // detect problems closing statements and connection
 catch ( SQLException sqlException ) {
 sqlException.printStackTrace();
 }
 }  // end method close

 // Method to clean up database connection. Provided in case
 // CloudscapeDataAccess object is garbage collected.
 protected void finalize()
 {
 close();
 }
 }  // end class CloudscapeDataAccess


 /**************************************************************************
 * (C) Copyright 2001 by Deitel & Associates, Inc. and Prentice Hall.     *
 * All Rights Reserved.                                                   *
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
