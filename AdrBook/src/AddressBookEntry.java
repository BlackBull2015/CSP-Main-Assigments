// Fig. 8.33: AddressBookEntry.java
// JavaBean to represent one address book entry.

public class AddressBookEntry {
   private String firstName = "";
   private String lastName = "";
   private String address1 = "";
   private String city = "";
   private String eircode = "";
   private String county = "";
   private String phoneNumber = "";
   private String emailAddress = "";


   private String altaddress = "";
   private String altcity = "";
   private String alteircode = "";
   private String altcounty = "";
   private String altamail = "";
   private String homephone = "";
   private String altphone = "";

   private int personID;
   private int addressID;
   private int phoneID;
   private int emailID;
   
   // empty constructor
   public AddressBookEntry()
   {
   }

   // set person's id
   public AddressBookEntry( int id )
   {
      personID = id;
   }
   
   // set person's first name
   public void setFirstName( String first )
   {
      firstName = first;
   }
   
   // get person's first name
   public String getFirstName()
   {
      return firstName;
   }
   
   // set person's last name
   public void setLastName( String last )
   {
      lastName = last;
   }
   
   // get person's last name
   public String getLastName()
   {
      return lastName;
   }
   
   // set first line of person's address
   public void setAddress1( String firstLine )
   {
      address1 = firstLine;
   }
   
   // get first line of person's address
   public String getAddress1()
   {
      return address1;
   }

   
   // set city in which person lives
   public void setCity( String personCity )
   {
      city = personCity;
   }
   
   // get city in which person lives
   public String getCity()
   {
      return city;
   }

   // set person's phone number
   public void setPhoneNumber( String number )
   {
      phoneNumber = number;
   }
   
   // get person's phone number 
   public String getPhoneNumber()
   {
      return phoneNumber;
   }

   // set person's email address
   public void setEmailAddress( String email )
   {
      emailAddress = email;
   }
   
   // get person's email address
   public String getEmailAddress()
   {
      return emailAddress;
   }
   
   // get person's ID
   public int getPersonID()
   {
      return personID;
   }
   
   // set person's addressID
   public void setAddressID( int id )
   {
      addressID = id;
   }
   
   // get person's addressID
   public int getAddressID()
   {
      return addressID;
   }
   
   // set person's phoneID
   public void setPhoneID( int id )
   {
      phoneID = id;
   }
   
   // get person's phoneID
   public int getPhoneID()
   {
      return phoneID;
   }
   
   // set person's emailID
   public void setEmailID( int id )
   {
      emailID = id;
   }
   
   // get person's emailID
   public int getEmailID()
   {
      return emailID;
   }

   public String getAltaddress() {
      return altaddress;
   }

   public void setAltaddress(String altaddress) {
      this.altaddress = altaddress;
   }

   public String getAltcity() {
      return altcity;
   }

   public void setAltcity(String altcity) {
      this.altcity = altcity;
   }

   public String getAlteircode() {
      return alteircode;
   }

   public void setAlteircode(String altstate) {
      this.alteircode = altstate;
   }

   public String getAltcounty() {
      return altcounty;
   }

   public void setAltcounty(String altzipcode) {
      this.altcounty = altzipcode;
   }

   public String getAltamail() {
      return altamail;
   }

   public void setAltamail(String altamail) {
      this.altamail = altamail;
   }

   public String getHomephone() {
      return homephone;
   }

   public void setHomephone(String homephone) {
      this.homephone = homephone;
   }

   public String getAltphone() {
      return altphone;
   }

   public void setAltphone(String altphone) {
      this.altphone = altphone;
   }

   public String getCounty() {return county;}

   public void setCounty(String county) {this.county = county;}

   public String getEircode() {return eircode;}

   public void setEircode(String eircode) {this.eircode = eircode;}
}  // end class AddressBookEntry



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
