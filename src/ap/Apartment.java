package ap;
public class Apartment extends RentalProperty {
	
	public Apartment(String propertyId,String strName,String strNum,String suburb,int numOfBedRoom,String propertyType) {
		super(propertyId, strName, strNum, suburb, numOfBedRoom, propertyType);
	}
	
	public boolean rentDate(String customer, DateTime rentdate, int num) { // return true means it can be booked
		
		DateTime estimatRe = new DateTime(rentdate,num); // to create a estimate return days
		String etf = estimatRe.getFormattedDate();
		String rtf = rentdate.getFormattedDate(); // 3. check the weekday with num
		
		if(!isPropertyStatus()) { // 1. check the status 
			System.out.println("have been rented");
			return false;
		}
		
		if(!checkRentalDays(rtf)) return false; // rentdate should after current day
		
		if(calWeekday(rtf)==0) return false; // 0 means something go wrong;
		if(calWeekday(rtf)==3 || calWeekday(rtf)==4) {// Friday and Sunday, more than 3 days
			if(num<2) return false; 
		} 
		else { // rest weekday, more than 2 day
			if(num<1) return false;
		}
		if(num>29) { // 4. check the maximun rental days
			System.out.println("the maximum rental days is 28");
			return false;
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
		
		if(calIntervalDays(getRentalRecord().getRentDate(), actuReDay)<-1) { //  check for valid of return day
			return false; // returnDate can not before than rentDate, but can the same day
		} 
		
		if(updateFee(actuReDay)[0]==updateFee(actuReDay)[1] && updateFee(actuReDay)[0] == -100) return false; // something go wrong
		getRentalRecord().setRentalFee(feeApartment(updateFee(actuReDay)[0])); // update rental fee
		getRentalRecord().setLateFee(lateFeeApartment(updateFee(actuReDay)[1])); //update late fee
		
		if(calIntervalDays(getRentalRecord().getRentDate(), actuReDay)>=29) return false;

		
		setPropertyStatus(true); // update property status, can be rented again 
		getRentalRecord().updateRecord(); // update record
		System.out.println("return success"); // can delete
		return true;
	}
	
	public boolean performMaintenance() {
		System.out.println("Apartment do not support maintenance now");
		return false;
	} 
	
	public boolean completeMaintenance(DateTime completionDate) {
		System.out.println("Apartment do not support maintenance now");
		return false;
	} 
	
		
	public double feeApartment(int n) { // n is num of bedrooms
		double p = 0.00;
		if(getNumOfBedRoom()==1) p=143;
		if(getNumOfBedRoom()==2) p=210;
		if(getNumOfBedRoom()==3) p=319;
		return n*p;
	}
	
	public double lateFeeApartment(int n) { // n is num of days
		return feeApartment(n)*1.15;
	}
	
	public String toString() { // property info
		String s;
		if(isPropertyStatus()) s = "Available";
		else s = "Rented";
		return getPropertyId()+":"+getStrNum()+":"+getStrName()+":"+getSuburb()+":"+getPropertyType()+":"+getNumOfBedRoom()+":"+ s;
	}
	
	
	public String getDetails() {
		String detail = String.format("%-30s%s", "Property ID:", getPropertyId())+"\n"+
						String.format("%-30s%s", "Address:", getStrNum()+" "+getStrName()+" "+getSuburb())+"\n"+
						String.format("%-30s%s", "Type:", getPropertyType())+"\n"+
						String.format("%-30s%s", "Bedroom:", getNumOfBedRoom())+"\n";
	
	
		if(getRentalRecord().getRentalRecords()[0]==null) { // only check for property without any rental record
			
			detail = detail +   
					String.format("%-30s%s", "Status:", "Available")+"\n"+
					String.format("%-30s%s", "RENTAL RECORD:", "empty")+"\n";
			return detail;
		}
		
		int k = 1;
		if(isPropertyStatus()) { // only check for the first one record, true mean can rent
			 k=0;
			detail = detail + 
					String.format("%-30s%s", "Status:", "Available")+"\n"+
					"------------------------------------------------------";			
		}
		else { detail = detail+
				String.format("%-30s%s", "Status:", "Rented")+"\n"+
				getPartofRecrod(getRentalRecord().getRentalRecords()[0]);} // have been rented
	
		for(int i = k;i<getRentalRecord().getRentalRecords().length;i++) {
			if(getRentalRecord().getRentalRecords()[i]==null) break; // if record is empty, over
			detail = detail + "\n" +getRecordDatails(getRentalRecord().getRentalRecords()[i])+"\n"; // handle each record
		}
		
		return detail;
	}
	
}
