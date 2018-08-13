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
	
	

	public DateTime changeType(String s1) { // change string into DateTime format
		int[] ch = new int[3];
		String[] s = s1.split("/");
		for(int i=0;i<s.length;i++) {ch[i] = Integer.valueOf(s[i]);}
		DateTime maintain = new DateTime(ch[0], ch[1], ch[2]);
		return maintain;
	}
	
	public boolean checkDate(String after) { // true mean the format is right
		boolean check = true; 
		DateTime today = new DateTime();
		Date aDate = null;
		try {
			SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
			format.setLenient(false);
			aDate = format.parse(after);
		} 
		catch (Exception ex){
			check = false;
			System.out.println("* * * Date Format is wrong //checkDate* * * ");
		}
		if((aDate.getTime()-today.getTime())/(1000*3600*24)>=0) check = true;
		else {
			check = false;
			System.out.println("The day you enter should be after Today. And today is :" + today.getFormattedDate());
		}
		return check;
	}
	
	public boolean checkDayForMaintenance(String after) {
		boolean check = true;
		DateTime today = new DateTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date aDate = null;
		try {	 
			aDate = sdf.parse(after);// 2018-08-01 with 2018-08-02; interval is 1;
		}
		catch (ParseException e) {
//			e.printStackTrace();
			System.out.println("Date Formate is wrong. //checkDayIfBefore");
			check = false;
		}
		if((aDate.getTime()-today.getTime())/(1000*3600*24)<=0 && (aDate.getTime()-today.getTime())/(1000*3600*24) >=-10) check = true;
		else {
			System.out.println("The day you enter should be in last ten days. And today is :" + today.getFormattedDate());
			check = false;
		}
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
	
	
	
	public String createPropertyId(String type,String strNum, String strName, String suburb) {
		String id = type + strNum;
		String[] strNameList = strName.split(" ");
		for(int i=0;i<strNameList.length;i++) id = id + strNameList[i].toUpperCase().charAt(0);
		String[] suburbList = suburb.split(" ");
		for(int i=0;i<suburbList.length;i++) id = id + suburbList[i].toUpperCase().charAt(0);
		return id;
	}
	
	
	public void addProperty(Scanner console) {
		while(true){
			if(flexiProperty[49]!=null) {
				System.out.println("We have already 50 both Apartment and Premium Suite, please don't add property.");
				break;
			}
			
			System.out.println("Please enter the Street number:");
			String strNum = console.nextLine();
			try { Integer.parseInt(strNum);} /// To check input value is valid.
			catch (NumberFormatException e) {	        	
					System.out.println("*** The input format is error. ***");
					break;
			}
			
			System.out.println("Please enter the Street name:");
			String strName = console.nextLine();
			
			System.out.println("Please enter the Suburb: ");
			String suburb = console.nextLine();
			
			System.out.println("Property type: If is Apartment, enter 1 or if is Premium Suite, enter 2");
			String propertyType = console.nextLine();
			try {  Integer.parseInt(propertyType);} /// To check input value is valid.
			catch (NumberFormatException e) {	        	
					System.out.println("*** The input format is error. ***");
					break;
			}
			if(propertyType.equals("1")|| propertyType.equals("2") ) {
				if(propertyType.equals("1")) propertyType = "Apartment";
				if(propertyType.equals("2")) propertyType = "Premium Suite";
			}
			else {
				System.out.println("*** The input format is error. ***");
				break;
			}
			
			System.out.println("Please enter the Number of bedrooms:  1 or 2 or 3, Premium Suite has only 3 bedrooms");
			String typeRoom = console.nextLine(); 
			int numOfBedRoom;
			try {numOfBedRoom = Integer.parseInt(typeRoom);} /// To check input value is valid.
			catch (NumberFormatException e) {	        	
	    			System.out.println("* * *  NumOfBedRooms: The input format is error. * * *");
	    			break;
			}
			if(propertyType.equals("Apartment")) {
				if(numOfBedRoom<=0 || numOfBedRoom>=4) {
					System.out.println("* * *  NumOfBedRooms: out of range  * * *");
					break;
				}
				
				propertyId = createPropertyId("A_",strNum,strName,suburb);
				Apartment A = new Apartment(propertyId, strName, strNum, suburb, numOfBedRoom, propertyType);
				storeProperty(A);
				System.out.println("The Apartment property has created successful");
				break;
			}
			if(propertyType.equals("Premium Suite")) {
				if(numOfBedRoom!=3) {
					System.out.println("* * *  NumOfBedRooms: Premium Suite has only 3 bedrooms  * * *");
					break;
				}
				
				System.out.println("Enter Premium Suite LastMaintainDate Like 14/02/2018 (dd/MM/yyyy); "+"\n"+
								"Tip: Maintenance date should before today");
				String maintainDate = console.nextLine();
				if(checkDayForMaintenance(maintainDate)){
					propertyId = createPropertyId("S_",strNum,strName,suburb);
					DateTime lastMaintainDate = changeType(maintainDate);
					PremiumSuite P = new PremiumSuite(propertyId, strName, strNum, suburb, numOfBedRoom, propertyType, lastMaintainDate);
					storeProperty(P);
					System.out.println("The Premium Suite property has created successful");
					break;
				}
				else break;
			}
		}
	}
	
	public void rentProperty(Scanner console) {
		while(true) {
			System.out.println("Pls enter Property ID: ");
			propertyId = console.nextLine();
			if(!checkIfPropertyId(propertyId)) { // check if exist this property, if exist and return this object, else continuous
				System.out.println("* * *  Rent a property: The property is not exist. * * *");
				break;
			}
			
			System.out.println("Pls enter Customer ID:  ");
			customerId = console.nextLine();
			
			System.out.println("Pls enter Rent date like 08/08/2018(dd/MM/yyyy):");
			String rentDate = console.nextLine();
			DateTime rentDateTime;
			
			if(checkDate(rentDate)) rentDateTime = changeType(rentDate);
			else break;
			
			
			System.out.println("How many days? :  Tip: the maximum day for Apartment is 28;");
			String rentDayStr = console.nextLine();
			int rentDayInt;
			try { rentDayInt = Integer.parseInt(rentDayStr);} /// To check input value is valid.
			catch (NumberFormatException e) {	        	
	    			System.out.println("* * *  Rent a property: The input format is error. * * *");
	    			break;
			}
			if(rentDayInt<=0) {
				System.out.println("The number must be large than 0");
				break;
			}
						
			existProperty = reProperty(propertyId);
			if(existProperty.rentDate(customerId, rentDateTime, rentDayInt)) {
				System.out.println(existProperty.getPropertyType()+" "+existProperty.getPropertyId()+" is now rented by customer "+customerId);
				break;
			}
			else {
				System.out.println(existProperty.getPropertyType()+existProperty.getPropertyId()+" could not be rented");
				break;
			}
		}
	}
	
	public void returnProperty(Scanner console) {
		while(true) {
			System.out.println("Enter property id: ");
			propertyId = console.nextLine();
			// check if exist this property, if exist and return this object, else continuous
			if(!checkIfPropertyId(propertyId)) {
				System.out.println("* * *  Return a property: The property is not exist. * * *");
				break;
			}
			
			System.out.println("Pls enter Return date (dd/mm/yyyy) like 22/08/2018 : ");
			String returnDayStr = console.nextLine();
			DateTime returnDayTime;
			
			if(checkDate(returnDayStr)) returnDayTime = changeType(returnDayStr);
			else break;
			
			existProperty = reProperty(propertyId); // find that property
			if(existProperty.returnDate(returnDayTime)) { // true means return success
				if(existProperty instanceof Apartment) {
					System.out.println(existProperty.getPropertyType()+" "+propertyId+" is returned by customer "+((Apartment)existProperty).getRentalRecord().getCustomerID());
					System.out.println(((Apartment)existProperty).getRentalRecord().getDetails());						
					break;
				}
				if(existProperty instanceof PremiumSuite) {
					System.out.println(existProperty.getPropertyType()+" "+propertyId+" is returned by customer "+((PremiumSuite)existProperty).getRentalRecord().getCustomerID());
					System.out.println(((PremiumSuite)existProperty).getRentalRecord().getDetails());						
					break;
				}
			}
			else {
				System.out.println(existProperty.getPropertyType()+" "+propertyId+" could not be returned");
				break;
			}
		}
	}
	
	public void performMaintenance(Scanner console) {
		while(true) {
			System.out.println("Enter property id: ");
			propertyId = console.nextLine();
			
			// check if exist this property, if exist and return this object, else continuous
			if(!checkIfPropertyId(propertyId)) {
				System.out.println("* * *  Return a property: The property is not exist. * * *");
				break;
			}
			else {
				existProperty = reProperty(propertyId); // find that property
				if(existProperty instanceof Apartment) {
					System.out.println("Apartment do not have maintenance function.");
					break;
				}
				if(existProperty.performMaintenance()) { // true means can go to maintain
					System.out.println(existProperty.getPropertyType()+" "+propertyId+" is now under maintainance ");
					break;
				}
				else {
					System.out.println(existProperty.getPropertyType()+" "+propertyId+" could not be maintainance");
					break;
				}
			}
		}
	}
	
	public void completeMaintenance(Scanner console) {
		while(true) {
			System.out.println("Enter property id: ");
			propertyId = console.nextLine();
			if(!checkIfPropertyId(propertyId)) { // check if exist this property
				System.out.println("* * *  Complete Maintenance: The property is not exist. * * *");
				break;
			}
			
			existProperty = reProperty(propertyId); // find that property
			if(existProperty instanceof Apartment) {
				System.out.println("Apartment do not have maintenance function.");
				break;
			}
			
			System.out.println("Complete Maintenance date (dd/mm/yyyy) like 12/08/2018: ");
			String compltMainStr = console.nextLine();
			DateTime compltMainTime;
			if(checkDate(compltMainStr)) compltMainTime = changeType(compltMainStr);
			else break;
			

			if(existProperty.completeMaintenance(compltMainTime)) { // true means complete maintenance
				System.out.println(existProperty.getPropertyType()+" "+propertyId+" has all maintenance completed and ready for rent ");
				break;
			}
			else {
				System.out.println(existProperty.getPropertyType()+" "+propertyId+" could not be maintenance");
				break;
			}
		}
	}
	
	public void displayProperty(Scanner console) {
		for(int i=0;i<flexiProperty.length;i++) {
			if(flexiProperty[0]==null) {
				System.out.println("There is no property now");
				break;
			}
			if(flexiProperty[i]==null) {
				System.out.println("All properties has been shown above");
				break;
			}
			System.out.println(flexiProperty[i]);
			if(flexiProperty[i] instanceof Apartment) System.out.println(((Apartment)flexiProperty[i]).getDetails());
			if(flexiProperty[i] instanceof PremiumSuite) System.out.println(((PremiumSuite)flexiProperty[i]).getDetails());
		}
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
				System.out.println("\n"+"*** The input format is error. ***"+"\n");
				continue;
			}
			
			
			if(firstIntChoice == 7) {
				System.out.println("See you next time");
				console.close();
				break;
			}
			
			switch (firstIntChoice) {
			case 1:
				addProperty(console);
				break;
			case 2:
				rentProperty(console);
				break;
			case 3:
				returnProperty(console);
				break;
			case 4:
				performMaintenance(console);
				break;
			case 5:
				completeMaintenance(console);
				break;
			case 6:
				displayProperty(console);
				break;
			default:
				System.out.println("*** The input number is error. Optional range is from 1 to 7; ***");
				break;
			}	
		}
	}
}