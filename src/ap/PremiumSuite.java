package ap;

public class PremiumSuite extends RentalProperty {
	private DateTime maintainDate;
	private DateTime estMaintenanceDate;
	private DateTime nextCompleteDate;
	private boolean maintainStatus = true;
		
	public PremiumSuite(String propertyId, String strName, String strNum, String suburb, int numOfBedRoom, String propertyType, DateTime maintainDate) {
		super(propertyId, strName, strNum, suburb, numOfBedRoom, propertyType);
		this.maintainDate = maintainDate;
		this.nextCompleteDate = new DateTime(maintainDate,10);
	}
	
	public boolean rentDate(String customer, DateTime rentdate, int num) { // return true means it can be booked
		
		DateTime estimatRe = new DateTime(rentdate,num); // to create a estimate return days
		String etf = estimatRe.getFormattedDate();
		String rtf = rentdate.getFormattedDate(); // 3. check the weekday with num
		
		if(!isPropertyStatus()) { // 1. check the status 
			System.out.println("have been rented");
			return false;
		}
		
		 // 2. check the statue of maintain
		if(!maintainStatus)  return false; // !maintainStatus means under maintain
		
		if(!checkRentalDays(rtf)) return false; // rentdate should after current day
			
		int d2 = calIntervalDays(etf, nextCompleteDate.getFormattedDate());				
		if( !(d2>0)) return false; 
		
		// update info
		setPropertyStatus(false);// 5. false means can't be booked
		getRentalRecord().createRecord(getPropertyId(), customer, rtf, etf); //  6. updating record array
		return true;	
	}	
	
	
	public boolean returnDate(DateTime actuRe) { 
		String actuReDay = actuRe.getFormattedDate();
		getRentalRecord().setActReDate(actuReDay); // update actually return date
		
		if(isPropertyStatus()) return false; // true means availabe; check for status, no rent, no return
		
		if(!maintainStatus) {// check for status, true mean under maintain, 
			if(calIntervalDays(actuReDay, estMaintenanceDate.getFormattedDate())<=0) return false;
		}
		else {
			if(calIntervalDays(actuReDay, nextCompleteDate.getFormattedDate())<=0) return false;
		}
		
		if(calIntervalDays(getRentalRecord().getRentDate(), actuReDay)<-1) { //  check for valid of return day
			return false; // returnDate can not before than rentDate, but can the same day
		} 
		
		
		// update the fee
		if(updateFee(actuReDay)[0]==updateFee(actuReDay)[1] && updateFee(actuReDay)[0] == -100) return false; // something go wrong
		getRentalRecord().setRentalFee(feeApartment(updateFee(actuReDay)[0])); // update rental fee
		getRentalRecord().setLateFee(lateFeeApartment(updateFee(actuReDay)[1])); //update late fee

		
		setPropertyStatus(true); // update property status, can be rented again 
		
		getRentalRecord().updateRecord(); // update record
		System.out.println("return success"); // can delete
		return true;
		
	}
	
	public boolean performMaintenance() {  // maintain period, can book, but no intersection
		
		if(!isPropertyStatus()) return false; // return false is rented ; only in available situation can perform
		if(!maintainStatus) return false; // means under maintain, no need to do again
		
		estMaintenanceDate = new DateTime(); // that mean today;
						
		maintainStatus = false; // means under maintain
		return true; // return true mean under maintain
	}
	
	public boolean completeMaintenance(DateTime completionDate) {
		if(!isPropertyStatus()) return false;
		if(maintainStatus) return false; // means a property no under maintain
		//update info
		
		if(calIntervalDays(estMaintenanceDate.getFormattedDate(), completionDate.getFormattedDate())<0) return false; // c should large than m1
		if(calIntervalDays(completionDate.getFormattedDate(), nextCompleteDate.getFormattedDate())<0) return false; // m2 should large than c
		
		maintainStatus = true; // have completed, no maintain	
		maintainDate = completionDate;
		return true;
	}
	
	
	
	public double feeApartment(int n) { // n is num of days
		return 554*n;
	}
	
	public double lateFeeApartment(int n) { // n is num of days
		return 662*n;
	}
	
	public String toString() { // property info
		String s;
		if(isPropertyStatus()) s = "Available";
		else s = "Rented";
		return getPropertyId()+":"+getStrNum()+":"+getStrName()+":"+getSuburb()+":"+getPropertyType()+":"+getNumOfBedRoom()+":"+ s+":"+maintainDate.getFormattedDate();
	}
	
	public String getDetails() {
		String detail = String.format("%-30s%s", "Property ID:", getPropertyId())+"\n"+
				String.format("%-30s%s", "Address:", getStrNum()+" "+getStrName()+" "+getSuburb())+"\n"+
				String.format("%-30s%s", "Type:", getPropertyType())+"\n"+
				String.format("%-30s%s", "Bedroom:", getNumOfBedRoom())+"\n";
                
		if(getRentalRecord().getRentalRecords()[0]==null) { // only check for property without any rental record
			detail = detail +   
					String.format("%-30s%s", "Status:", "Available")+"\n"+
					String.format("%-30s%s", "Last maintenance:", maintainDate.getFormattedDate())+"\n"+
					String.format("%-30s%s", "RENTAL RECORD:", "empty")+"\n";
			return detail;
		}
		int k = 1;
		if(isPropertyStatus()) { // only check for the first one record, true mean can rent
			k = 0;
			detail = detail + 
					String.format("%-30s%s", "Status:", "Available")+"\n"+
					String.format("%-30s%s", "Last maintenance:", maintainDate.getFormattedDate())+"\n"+
					"----------------------------------------------";
				
		}
		else { detail = detail + 
				String.format("%-30s%s", "Status:", "Rented")+"\n"+
				String.format("%-30s%s", "Last maintenance:", maintainDate.getFormattedDate())+"\n"+
				getPartofRecrod(getRentalRecord().getRentalRecords()[0]);} // have beed rented
		
		
		for(int i = k;i<getRentalRecord().getRentalRecords().length;i++) {
			if(getRentalRecord().getActReDate()=="none" && getRentalRecord().getRentalRecords()[1]==null) break;
			if(getRentalRecord().getRentalRecords()[i]==null) break; // if record is empty, over
			detail = detail +"\n" + getRecordDatails(getRentalRecord().getRentalRecords()[i])+"\n"; // handle each record
		}
		
		return detail;
	}

	
}
