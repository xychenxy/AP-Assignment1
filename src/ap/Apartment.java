package ap;
public class Apartment extends RentalProperty{
	
	public Apartment(String propertyId,String strName,String strNum,String suburb,int numOfBedRoom,String propertyType) {
		super(propertyId, strName, strNum, suburb, numOfBedRoom, propertyType);
	}
	
	public boolean rentDate(String customer, DateTime rentdate, int numOfRentDay) { // return true means it can be booked
		
		DateTime estimatRe = new DateTime(rentdate,numOfRentDay); // to create a estimate return days
		String etf = estimatRe.getFormattedDate();
		String rtf = rentdate.getFormattedDate(); // 3. check the weekday with num
		
		if(!isPropertyStatus()) { // 1. check the status 
			System.out.println("Sorry, this apartment has been rented.");
			return false;
		}
		
		if(numOfRentDay>=29) { // 4. check the maximun rental days
			System.out.println("the maximum rental days is 28");
			return false;
		}
		
		if(calWeekday(rtf)==4 || calWeekday(rtf)==5) {// Friday and Sunday, more than 3 days
			if(numOfRentDay<=2) {
				System.out.println("From Friday to Saturday, the minimum rental day is 3 days.");
				return false; 
			}
		} 
		else { // rest weekday, more than 2 day
			if(numOfRentDay<=1) {
				System.out.println("From Sunday to Thursday the minimum rental day is 2 days.");
				return false;
			}
		}
		
		
		setPropertyStatus(false); // 5. false means can't be booked
		getRentalRecord().createRecord(getPropertyId(), customer, rtf, etf); //  6. updating record array
		return true;	
	}
	
	
	
	public boolean returnDate(DateTime actuRe) { 
		if(isPropertyStatus()) {
			System.out.println("can not return, as it is available");
			return false;
		}
		String actuReDay = actuRe.getFormattedDate();
		getRentalRecord().setActReDate(actuReDay); // update actually return date
		
		if(calIntervalDays(getRentalRecord().getRentDate(), actuReDay)<-1 || calIntervalDays(getRentalRecord().getRentDate(), actuReDay)>=29) { //  check for valid of return day
			System.out.println("Your return day cannot before than rental day, and maximum rental day is 28. And your rental day is: "+ getRentalRecord().getRentDate());
			return false; // returnDate can not before than rentDate, but can be the same day
		} 
	
		feeApartment(calActAndLate(actuReDay)[0],calActAndLate(actuReDay)[1]); // update the fee
		setPropertyStatus(true); // update property status, can be rented again 
		getRentalRecord().updateRecord(); // update record
		return true;
	}
	
	
	public boolean completeMaintenance(DateTime completionDate) {
		if(!isPropertyStatus()) {
			System.out.println("Apartment is being rented, no allow to perform maintenance");
			return false;
		}
		
		if(ismaintenance()) {
			System.out.println("sorry, Apartment haven't performed maintenance, no allow complete maintenance");
			return false;
		}
		
		setmaintenance(true);
		return true;
	} 
	
	public void feeApartment(int n1, int n2) { // update the actually rental fee and delay return fee
		double p = 0.00;
		if(getNumOfBedRoom()==1) p=143;
		if(getNumOfBedRoom()==2) p=210;
		if(getNumOfBedRoom()==3) p=319;
		getRentalRecord().setRentalFee(keepTwoDotDecimal(n1*p));
		getRentalRecord().setLateFee(keepTwoDotDecimal(n2*p));
	}
	
	
	public String toString() { // property info
		String s;
		if(isPropertyStatus()) s = "Available";
		else s = "Rented";
		return getPropertyId()+":"+getStrNum()+":"+getStrName()+":"+getSuburb()+":"+getPropertyType()+":"+getNumOfBedRoom()+":"+ s;
	}
	
	public String getDetails() {
		String status;
		if(isPropertyStatus()) status = "Available";
		else status = "Rented";
		
		String detail = getBasicPropertyInfo();
	
		if(getRentalRecord().getRentalRecords()[0]==null) { // only check for property without any rental record
			detail = detail +   
					String.format("%-30s%s", "Status:", status)+"\n"+
					String.format("%-30s%s", "RENTAL RECORD:", "Empty")+"\n";
			return detail;
		}
		
		int k = 1;
		if(isPropertyStatus()) { // only check for the first one record, true mean can rent
			 k=0;
			detail = detail + 
					String.format("%-30s%s", "Status:", status)+"\n"+
					"------------------------------------------------------";			
		}
		else { detail = detail+
				String.format("%-30s%s", "Status:", status)+"\n"+
				getRecordDatails(getRentalRecord().getRentalRecords()[0],3); // have been rented
		} 
		
		for(int i = k;i<getRentalRecord().getRentalRecords().length;i++) {
			if(getRentalRecord().getRentalRecords()[i]==null) break; // if record is empty, over
			detail = detail + "\n" +getRecordDatails(getRentalRecord().getRentalRecords()[i],6)+"\n"; // handle each record
		}
		return detail;
	}
	
}
