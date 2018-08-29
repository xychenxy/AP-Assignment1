package ap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;

public class FlexiRentSystem {
	private RentalProperty[] flexiProperty = new RentalProperty[50];
	
	
	public void runMenu() {
		Scanner console = new Scanner(System.in);
		while(true){
			System.out.println("\n"+"\n"+"**** FLEXIRENT SYSTEM MENU ****");
			System.out.println(String.format("%-30s%s", "Add Property:", 1));
			System.out.println(String.format("%-30s%s", "Rent Property:", 2));
			System.out.println(String.format("%-30s%s", "Return Property:", 3));
			System.out.println(String.format("%-30s%s", "Property Maintenance:", 4));
			System.out.println(String.format("%-30s%s", "Complete Maintenance:", 5));
			System.out.println(String.format("%-30s%s", "Display All Properties:", 6));
			System.out.println(String.format("%-30s%s", "Exit Program:", 7));
			System.out.println("Enter your choice:"+"\n"+"\n");
			
			String menuOption = console.nextLine();
			int levelOne;
			try { levelOne = Integer.parseInt(menuOption);} /// To check input value is valid.
			catch (NumberFormatException e) {	 
				System.out.println("\n"+"*** The input format is error. ***"+"\n");
				continue;
			}
			
			if(levelOne == 7) {
				System.out.println("See you next time");
				console.close();
				break;
			}
			
			switch (levelOne) {
			case 1:
				if(flexiProperty[49]!=null) {
					System.out.println("We have already 50 both Apartment and Premium Suite, please don't add property.");
					break;
				}
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
	
	
	public DateTime changeToDateTime(String s1) { // change string into DateTime format
		int[] ch = new int[3];
		String[] s = s1.split("/");
		for(int i=0;i<s.length;i++) {ch[i] = Integer.valueOf(s[i]);}
		DateTime maintain = new DateTime(ch[0], ch[1], ch[2]);
		return maintain;
	}

	public String[] calIntervalDays(String after) {
		String[] str = new String[3];
		String check = "false";
		DateTime today = new DateTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date aDate = null;
		try {
			aDate = sdf.parse(after);// 2018-08-01 with 2018-08-02; interval is 1;
			check = "true";
			str[0] = String.valueOf((aDate.getTime()-today.getTime())/(1000*3600*24));
		}
		catch (ParseException e) {
			System.out.println("Date Formate is wrong.");
			check = "false";
		}
		str[1] = check;
		return str;
	}
	
	public boolean checkRepeatProperty(String propertyId) {
		boolean check = true;
		for(int i=0;i<flexiProperty.length;i++) {
			if(flexiProperty[i]==null) break;
			else if (flexiProperty[i].getPropertyId().equals(propertyId)) {
				check = false;
				break;
			}
		}
		return check;
	}
	
	public void storeProperty(RentalProperty property) {
		for(int i=0;i<flexiProperty.length;i++) {
			if(flexiProperty[i]==null) {
				flexiProperty[i] = property;
				System.out.println("\n"+"The property has created successful"+"\n");
				break;
			}		
		}
	}
	
	public RentalProperty reProperty(String propertyId) {
		for(int i=0;i<flexiProperty.length;i++) {
			if(flexiProperty[i] == null) break;
			if(flexiProperty[i].getPropertyId().equals(propertyId)) {
				return flexiProperty[i];
			}
		}
		System.out.println("This property ID is not exsit, check your propertyId");
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
	
	public void addApartment(Scanner console, String strNum, String strName, String suburb,String propertyType) {
		while(true) {
			System.out.println("Please enter the Number of bedrooms:  1 or 2 or 3");
			String typeRoom = console.nextLine(); 
			int numOfBedRoom;
			try {numOfBedRoom = Integer.parseInt(typeRoom);} /// To check input value is valid.
			catch (NumberFormatException e) {	        	
				System.out.println("* * *  NumOfBedRooms: The input format is error. * * *");
	    			break;
			}
			if(numOfBedRoom<=0 || numOfBedRoom>=4) {
				System.out.println("* * *  NumOfBedRooms: out of range  * * *");
				break;
			}
			
			String propertyId = createPropertyId("A_",strNum,strName,suburb);
			if(!checkRepeatProperty(propertyId)) {
				System.out.println("this property has already exit");
				break;
			}
			Apartment A = new Apartment(propertyId, strName, strNum, suburb, numOfBedRoom, propertyType);
			storeProperty(A);
			System.out.println(A.getDetails());
			break;
		}
	}
	
	public void addPremiumSuite(Scanner console, String strNum, String strName, String suburb,String propertyType) {
		while(true) {
			System.out.println("Enter LastMaintainDate Like 14/02/2018 (dd/MM/yyyy);  Tip: Maintenance date should before today");
			String maintainDate = console.nextLine();
			
			if(calIntervalDays(maintainDate)[1].equals("false")) break;
			
			if(Integer.valueOf(calIntervalDays(maintainDate)[0])<0 && Integer.valueOf(calIntervalDays(maintainDate)[0])>=-10){				
				String propertyId = createPropertyId("S_",strNum,strName,suburb);
				if(!checkRepeatProperty(propertyId)) {
					System.out.println("this property has already exit");
					break;
				}
				PremiumSuite P = new PremiumSuite(propertyId, strName, strNum, suburb, 3, propertyType, changeToDateTime(maintainDate));
				storeProperty(P);
				System.out.println(P.getDetails());
			}
			else System.out.println("maintenance days should between today and last ten days");
			break;
		}
	}
	
	public void addProperty(Scanner console) {	
		while(true){
			System.out.println("Please enter the Street number:");
			String strNum = console.nextLine();
			
			System.out.println("Please enter the Street name:");
			String strName = console.nextLine();
			
			System.out.println("Please enter the Suburb: ");
			String suburb = console.nextLine();
			
			System.out.println("Property type: If is Apartment, enter 1 or if is Premium Suite, enter 2"); 
			String propertyType = console.nextLine(); // this method can reduce error, more usability, compared with apartment, user pretend to enter 1 or 2			
			if(propertyType.equals("1")) {
				addApartment(console, strNum, strName, suburb, "Apartment");
				break;
			}
			else if (propertyType.equals("2")) {
				addPremiumSuite(console, strNum, strName, suburb, "Premium Suite");
				break;
			}
			else {
				System.out.println("*** The input format is error. ***");
				break;
			}
		}
	}
	
	public void rentProperty(Scanner console) {
		while(true) {
			System.out.println("Pls enter Property ID:   return to menu and select 6 to check your property id");
			String propertyId = console.nextLine();
			RentalProperty existProperty = reProperty(propertyId);
			if(existProperty == null) break;
			
			System.out.println("Pls enter Customer ID:  ");
			String customerId = console.nextLine();
			
			System.out.println("Pls enter Rent date like 08/08/2018(dd/MM/yyyy):");
			String rentDate = console.nextLine();
			DateTime rentDateTime;			
			if(calIntervalDays(rentDate)[1].equals("false")) break;
			if(Integer.valueOf(calIntervalDays(rentDate)[0])>=0) rentDateTime = changeToDateTime(rentDate);
			else {
				System.out.println("normally, the date you enter should be after or equal today.");
				break;
			}
			
			System.out.println("How many days you want to rent? :  Tip: the maximum day for Apartment is 28;");
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
			
			if(existProperty.rentDate(customerId, rentDateTime, rentDayInt)) {
				System.out.println(existProperty.getPropertyType()+" "+existProperty.getPropertyId()+" is now rented by customer "+customerId);
			}
			else {
				System.out.println(existProperty.getPropertyType()+" "+existProperty.getPropertyId()+" could not be rented. ");
			}
			break;
		}
	}
	
	public void returnProperty(Scanner console) {
		while(true) {
			System.out.println("Enter property id: ");
			String propertyId = console.nextLine();
			RentalProperty existProperty = reProperty(propertyId);
			if(existProperty == null) break;
					
			System.out.println("Pls enter Return date (dd/mm/yyyy) like 22/08/2018 : ");
			String returnDayStr = console.nextLine();
			DateTime returnDayTime;
			if(calIntervalDays(returnDayStr)[1].equals("false")) break;
			if(Integer.valueOf(calIntervalDays(returnDayStr)[0])>=0) returnDayTime = changeToDateTime(returnDayStr);
			else {
				System.out.println("normally, the date you enter should be after or equal today.");
				break;
			}
			
			if(existProperty.returnDate(returnDayTime)) {
				System.out.println(existProperty.getPropertyType()+" "+propertyId+" is returned by customer "+existProperty.getRentalRecord().getCustomerID());
				System.out.println(existProperty.getRentalRecord().getDetails());						
			}
			else System.out.println(existProperty.getPropertyType()+" "+propertyId+" could not be returned");
			break;
				
		}
	}
	
	public void performMaintenance(Scanner console) {
		while(true) {
			System.out.println("Enter property id: ");
			String propertyId = console.nextLine();
			RentalProperty existProperty = reProperty(propertyId);
			if(existProperty == null) break;
			
			if(existProperty.performMaintenance()) { // true means can go to maintain
				System.out.println(existProperty.getPropertyType()+" "+propertyId+" is now under maintainance ");
			}
			else {
				System.out.println(existProperty.getPropertyType()+" "+propertyId+" could not be maintainance");
			}
			break;
		}
	}
	
	public void completeMaintenance(Scanner console) {
		while(true) {
			System.out.println("Enter property id: ");
			String propertyId = console.nextLine();
			RentalProperty existProperty = reProperty(propertyId);
			if(existProperty == null)break;
			
			System.out.println("Complete Maintenance date (dd/mm/yyyy) like 12/08/2018: ");
			String compltMainStr = console.nextLine();
			DateTime compltMainTime;
			if(calIntervalDays(compltMainStr)[1].equals("false")) break;
			if(Integer.valueOf(calIntervalDays(compltMainStr)[0])>=0) compltMainTime = changeToDateTime(compltMainStr);
			else {
				System.out.println("normally, the date you enter should be after or equal today.");
				break;
			}
			
			if(existProperty.completeMaintenance(compltMainTime)) { // true means complete maintenance
				System.out.println(existProperty.getPropertyType()+" "+propertyId+" has all maintenance completed and ready for rent ");
			}
			else System.out.println(existProperty.getPropertyType()+" "+propertyId+" could not be maintenance");
			break;
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
	
	
}