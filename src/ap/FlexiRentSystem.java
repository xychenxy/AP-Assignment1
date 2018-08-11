package ap;

// Author: Xiaoyu Chen 
// Date:   11/8/2018

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;



public class FlexiRentSystem {

	private Object[] flexiProperty = new Object[50];
	private String propertyId;	
	private String customerId;
	private RentalProperty existProperty;
	

	public DateTime changeType(String s1) { // change string into datetime
		int[] ch = new int[3];
		String[] s = s1.split("/");
		for(int i=0;i<s.length;i++) {ch[i] = Integer.valueOf(s[i]);}
		DateTime maintain = new DateTime(ch[0], ch[1], ch[2]);
		return maintain;
	}
	
	public boolean checkDate(String s) { // true mean the format is right
		boolean check = true; 
		try {
			SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
			format.setLenient(false);
			Date date = format.parse(s);
		} catch (Exception ex){
//			ex.printStackTrace();
			check = false;
			System.out.println("* * * Date Format is wrong //checkDate* * * ");
		}
		
		return check;
	}
	
	public boolean checkDayIfBefore(String after) {
		boolean check = true;
		DateTime today = new DateTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date aDate = null;
		try {
			 // 2018-08-01 with 2018-08-02; interval is 1;
			aDate = sdf.parse(after);
		}
		catch (ParseException e) {
			// TODO: handle exception
//			e.printStackTrace();
			System.out.println("Date Formate is wrong. //checkDayIfBefore");
			check = false;
		}
		if((aDate.getTime()-today.getTime())/(1000*3600*24)<=0 && (aDate.getTime()-today.getTime())/(1000*3600*24) >=-10) check = true;
		else check = false;
		return check;
	}
	
	
	public void storeProperty(RentalProperty A) {
		for(int i=0;i<flexiProperty.length;i++) {
			if(flexiProperty[i]==null) {
				flexiProperty[i] = A;
				break;
			}		
		}
	}
	
	public boolean checkIfPropertyId(String s) { 
		boolean status = false; // false means it doesn't exsit this property
		for(int i=0;i<flexiProperty.length;i++) {
			if(flexiProperty[i]==null) break;
			else {
				if(flexiProperty[i] instanceof Apartment) {
					if(((Apartment)flexiProperty[i]).getPropertyId().equals(propertyId)) {
						status = true;
						break;
					}
				}
				if(flexiProperty[i] instanceof PremiumSuite) {
					if(((PremiumSuite)flexiProperty[i]).getPropertyId().equals(propertyId)) {
						status = true;
						break;
					}
				}
			}
		}
		return status;
	}
	
	public RentalProperty reProperty(String s) { // use with checkIfPropertyId
		for(int i=0;i<flexiProperty.length;i++) {
			if(flexiProperty[i] instanceof Apartment) {
				if(((Apartment)flexiProperty[i]).getPropertyId().equals(propertyId)) {
					return (Apartment)flexiProperty[i];
				}
			}
			if(flexiProperty[i] instanceof PremiumSuite) {
				if(((PremiumSuite)flexiProperty[i]).getPropertyId().equals(propertyId)) {
					return (PremiumSuite)flexiProperty[i];
				}
			}
		}
		return null;
	}
	
	
	
	public void runMenu() {
		
		Scanner console = new Scanner(System.in);
		
		while(true){
			System.out.println("**** FLEXIRENT SYSTEM MENU ****");
			System.out.println(String.format("%-30s%s", "Add Property:", 1));
			System.out.println(String.format("%-30s%s", "Rent Property:", 2));
			System.out.println(String.format("%-30s%s", "Return Property:", 3));
			System.out.println(String.format("%-30s%s", "Property Maintenance:", 4));
			System.out.println(String.format("%-30s%s", "Complete Maintenance:", 5));
			System.out.println(String.format("%-30s%s", "Display All Properties:", 6));
			System.out.println(String.format("%-30s%s", "Exit Program:", 7));
			System.out.println("Enter your choice:");
			
			String firstStrChoice = console.nextLine();
			int firstIntChoice;
			try { firstIntChoice = Integer.parseInt(firstStrChoice);} /// To check input value is valid.
			catch (NumberFormatException e) {	        	
					System.out.println("*** The input format is error. ***");
					continue;
			}
			
			if(firstIntChoice<=0 || firstIntChoice>=8) {
				System.out.println("*** The input number is error. Please enter the correct row number ***");
				System.out.println();
				continue;
			}
			
			
			if(firstIntChoice == 1) { // if there is any errors, show err msg, go back to main memu
				if(flexiProperty[49]!=null) {
					System.out.println("We have already 50 both Apartment and Premium Suite, please don't add property.");
					continue;
				}
				
				System.out.println("Please enter the Street number:");
				String strNum = console.nextLine();
				try { int strNumInt = Integer.parseInt(strNum);} /// To check input value is valid.
				catch (NumberFormatException e) {	        	
						System.out.println("*** The input format is error. ***");
						continue;
				}
				
				
				
				System.out.println("Please enter the Street name:");
				String strName = console.nextLine();
				
				
				System.out.println("Please enter the Suburb:");
				String suburb = console.nextLine();
				
				System.out.println("Property type: if Apartment, enter 1 or if Premium Suite, enter 2");
				String propertyType = console.nextLine();
				int propertyTypeInt;
				try { propertyTypeInt = Integer.parseInt(propertyType);} /// To check input value is valid.
				catch (NumberFormatException e) {	        	
						System.out.println("*** The input format is error. ***");
						continue;
				}
				if(propertyTypeInt == 1 || propertyTypeInt == 2) {
					if(propertyTypeInt == 1) propertyType = "Apartment";
					if(propertyTypeInt == 2) propertyType = "Premium Suite";
				}
				else {
					System.out.println("*** The input format is error. ***");
					continue;
				}
				
				System.out.println("Please enter the Number of bedrooms:  1 or 2 or 3, Premium Suite has only 3 bedrooms");
				String typeRoom = console.nextLine(); 
				int numOfBedRoom;
				try {numOfBedRoom = Integer.parseInt(typeRoom);} /// To check input value is valid.
				catch (NumberFormatException e) {	        	
		    			System.out.println("* * *  NumOfBedRooms: The input format is error. * * *");
		    			continue;
				}
				if(propertyType.equals("Apartment")) {
					if(numOfBedRoom<=0 || numOfBedRoom>=4) {
						System.out.println("* * *  NumOfBedRooms: out of range  * * *");
						continue;
					}
					//creat apartment
					propertyId = "A_" + strNum + strName + suburb;
					Apartment A = new Apartment(propertyId, strName, strNum, suburb, numOfBedRoom, propertyType);
					storeProperty(A);
					System.out.println("The Apartment property has created successful");
					continue;
				}
				if(propertyType.equals("Premium Suite")) {
					if(numOfBedRoom!=3) {
						System.out.println("* * *  NumOfBedRooms: Premium Suite has only 3 bedrooms  * * *");
						continue;
					}
					
					System.out.println("Enter Premium Suite LastMaintainDate Like 14/02/2018 (dd/MM/yyyy); maintenance date should before today");
					String maintainDate = console.nextLine();
					if(checkDate(maintainDate)){
						// create the property
						if(!checkDayIfBefore(maintainDate)) {
							System.out.println("maintenance date should before today");
							continue;
						}
						propertyId = "S_" + strNum + strName + suburb;
						DateTime lastMaintainDate = changeType(maintainDate);
						PremiumSuite P = new PremiumSuite(propertyId, strName, strNum, suburb, numOfBedRoom, propertyType, lastMaintainDate);
						storeProperty(P);
						System.out.println("The Premium Suite property has created successful");
						continue;
					}
					else {
						System.out.println("* * *  MaintainDate: The input format is error. * * *");
						continue;
					}
				}			
			}
			
			if(firstIntChoice == 2) { 
				System.out.println("Enter property id: ");
				propertyId = console.nextLine();
				if(!checkIfPropertyId(propertyId)) { // check if exist this property, if exist and return this object, else continuous
					System.out.println("* * *  Rent a property: The property is not exist. * * *");
					continue;
				}
				
				System.out.println("Customer id:  ");
				customerId = console.nextLine();
				
				System.out.println("Rent date (dd/mm/yyyy) : ");
				String rentDate = console.nextLine();
				DateTime rentDateTime;
				if(checkDate(rentDate)){
					rentDateTime = changeType(rentDate);
				}
				else {
					System.out.println("* * *  Rent a property: The input format of rentDate is error. * * *");
					continue;
				}
				
				
				System.out.println("How many days? :");
				String rentDayStr = console.nextLine();
				int rentDayInt;
				try { rentDayInt = Integer.parseInt(rentDayStr);} /// To check input value is valid.
				catch (NumberFormatException e) {	        	
		    			System.out.println("* * *  Rent a property: The input format is error. * * *");
		    			continue;
				}
				if(rentDayInt<=0) {
					System.out.println("The number must be large than 0");
					continue;
				}
							
				existProperty = reProperty(propertyId);
				if(existProperty.rentDate(customerId, rentDateTime, rentDayInt)) {
					System.out.println(existProperty.getPropertyType()+" "+existProperty.getPropertyId()+" is now rented by customer "+customerId);
					continue;
				}
				else {
					System.out.println(existProperty.getPropertyType()+existProperty.getPropertyId()+" could not be rented");
					continue;
				}
				
			}
			
			if(firstIntChoice == 3) {
				System.out.println("Enter property id: ");
				propertyId = console.nextLine();
				// check if exist this property, if exist and return this object, else continuous
				if(!checkIfPropertyId(propertyId)) {
					System.out.println("* * *  Return a property: The property is not exist. * * *");
					continue;
				}
				
				System.out.println("Pls enter Return date (dd/mm/yyyy) like 22/08/2018 : ");
				String returnDayStr = console.nextLine();
				DateTime returnDayTime;
				if(checkDate(returnDayStr)){
					returnDayTime = changeType(returnDayStr);
				}
				else {
					System.out.println("* * *  Return a property: The input format of rentDate is error. * * *");
					continue;
				}
				
				existProperty = reProperty(propertyId); // find that property
				if(existProperty.returnDate(returnDayTime)) { // true means return sccess
					if(existProperty instanceof Apartment) {
						System.out.println(existProperty.getPropertyType()+" "+propertyId+" is returned by customer "+((Apartment)existProperty).getRentalRecord().getCustomerID());
						System.out.println(((Apartment)existProperty).getRentalRecord().getDetails());						
						continue;
					}
					if(existProperty instanceof PremiumSuite) {
						System.out.println(existProperty.getPropertyType()+" "+propertyId+" is returned by customer "+((PremiumSuite)existProperty).getRentalRecord().getCustomerID());
						System.out.println(((PremiumSuite)existProperty).getRentalRecord().getDetails());						
						continue;
					}
				}
				else {
					System.out.println(existProperty.getPropertyType()+" "+propertyId+" could not be returned");
					continue;
				}				
			}
			
			if(firstIntChoice == 4) {
				System.out.println("Enter property id: ");
				propertyId = console.nextLine();
				
				// check if exist this property, if exist and return this object, else continuous
				if(!checkIfPropertyId(propertyId)) {
					System.out.println("* * *  Return a property: The property is not exist. * * *");
					continue;
				}
				else {
					existProperty = reProperty(propertyId); // find that property
					if(existProperty instanceof Apartment) {
						System.out.println("Apartment do not have maintenance function.");
						continue;
					}
					if(existProperty.performMaintenance()) { // true means can go to maintain
						System.out.println(existProperty.getPropertyType()+" "+propertyId+" is now under maintainance ");
						continue;
					}
					else {
						System.out.println(existProperty.getPropertyType()+" "+propertyId+" could not be maintainance");
						continue;
					}
				}
				
			}
			
			if(firstIntChoice == 5) {
				System.out.println("Enter property id: ");
				propertyId = console.nextLine();
				if(!checkIfPropertyId(propertyId)) { // check if exist this property
					System.out.println("* * *  Complete Maintenance: The property is not exist. * * *");
					continue;
				}
				
				existProperty = reProperty(propertyId); // find that property
				if(existProperty instanceof Apartment) {
					System.out.println("Apartment do not have maintenance function.");
					continue;
				}
				
				System.out.println("Complete Maintenance date (dd/mm/yyyy) like 12/08/2018: ");
				String compltMainStr = console.nextLine();
				DateTime compltMainTime;
				if(checkDate(compltMainStr)){
					compltMainTime = changeType(compltMainStr);
				}
				else {
					System.out.println("* * *  Complete Maintenance: The input format of rentDate is error. * * *");
					continue;
				}
				

				if(existProperty.completeMaintenance(compltMainTime)) { // true means complete maintenance
					System.out.println(existProperty.getPropertyType()+" "+propertyId+" has all maintenance completed and ready for rent ");
					continue;
				}
				else {
					System.out.println(existProperty.getPropertyType()+" "+propertyId+" could not be maintenance");
					continue;
				}				
			}
			
			if(firstIntChoice == 6) {
				for(int i=0;i<flexiProperty.length;i++) {
					if(flexiProperty[0]==null) {
						System.out.println("There is no property now");
						break;
					}
					if(flexiProperty[i]==null) {
						System.out.println("All properties has been shown above");
						System.out.println("########## End ##########");
						break;
					}
					System.out.println(flexiProperty[i]);
					if(flexiProperty[i] instanceof Apartment) System.out.println(((Apartment)flexiProperty[i]).getDetails());
					if(flexiProperty[i] instanceof PremiumSuite) System.out.println(((PremiumSuite)flexiProperty[i]).getDetails());
				}
			}
			
			if(firstIntChoice == 7) {
				System.out.println("See you next time");
				break;
			}
				
		}
	}
}