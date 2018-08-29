package ap;

public class PremiumSuite extends RentalProperty{
	
	public PremiumSuite(String propertyId, String strName, String strNum, String suburb, int numOfBedRoom, String propertyType, DateTime maintainDate) {
		super(propertyId, strName, strNum, suburb, numOfBedRoom, propertyType,maintainDate);
	}
	
	
	public boolean rentDate(String customer, DateTime rentdate, int numOfRentDay) { // return true means it can be booked
		DateTime estimatRe = new DateTime(rentdate,numOfRentDay); // to create a estimate return days
		
		if(!isPropertyStatus()) { // 1. check the status 
			System.out.println("Sorry, this premium suite has been rented.");
			return false;
		}
		
		if(!ismaintenance()) {
			System.out.println("The Premium Suite is under maintenance.");
			return false; // !maintenance means under maintain
		}
						
		if( !(calIntervalDays(estimatRe.getFormattedDate(), getNextCompleteDate().getFormattedDate())>0)) {
			System.out.println("Sorry, you cannot rent the premium suite over the next completely maintenance day.");
			return false; 
		}
		
		setPropertyStatus(false);  // update info
		getRentalRecord().createRecord(getPropertyId(), customer, rentdate.getFormattedDate(), estimatRe.getFormattedDate()); 
		return true;	
	}	
	
	
	public boolean returnDate(DateTime actuRe) { 
		if(isPropertyStatus()) {
			System.out.println("You don't need to return the premium suite, because you haven't rented it before.");
			return false; 
		}
		
		String actuReturnDay = actuRe.getFormattedDate();
		getRentalRecord().setActReDate(actuReturnDay); 
		
		if(!ismaintenance()) {// below handle under maintain, 
			if(calIntervalDays(actuReturnDay, getEstMaintenanceDate())<=0) {
				System.out.println("Sorry, you cannot return the premium suite over the maintenance day.");
				return false;
			}
		}
		else { // below handle out of under maintain,
			if(calIntervalDays(actuReturnDay, getNextCompleteDate().getFormattedDate())<=0) {
				System.out.println("Sorry, you cannot return the premium suite over the next completely maintenance day.");
				return false;
			}
		}
		
		if(calIntervalDays(getRentalRecord().getRentDate(), actuReturnDay)<-1) {
			System.out.println("Your return day cannot before your rental day. And your rental day is: " + getRentalRecord().getRentDate());
			return false; 
		} 
		
		feePremiumSuite(calActAndLate(actuReturnDay)[0],calActAndLate(actuReturnDay)[1]); // update the fee
		setPropertyStatus(true); 
		getRentalRecord().updateRecord(); 
		return true;
	}
	
	
	public boolean completeMaintenance(DateTime completionDate) {
		int intervalDay1;
		int intervalDay2;
		if(!isPropertyStatus()) {
			System.out.println("The Premium Suite is being rented, no allow to complete maintenance.");
			return false;
		}
		if(ismaintenance()) {
			System.out.println("The Premium Suite is no under maintenance. pls perform maintenance at first.");
			return false; 
		}
		
		intervalDay1 = calIntervalDays(getEstMaintenanceDate(), completionDate.getFormattedDate());
		intervalDay2 = calIntervalDays(completionDate.getFormattedDate(), getNextCompleteDate().getFormattedDate());
		if(intervalDay1<0 || intervalDay2<0){
			System.out.println("complete maintenance day should between perform maintenance day and the next complete maintenance day.");
			return false; 
		}
		
		setmaintenance(true);
		setMaintainDate(completionDate);
		return true;
	}
	
	
	public void feePremiumSuite(int n1, int n2) {
		getRentalRecord().setRentalFee(keepTwoDotDecimal(n1*554));
		getRentalRecord().setLateFee(keepTwoDotDecimal(n2*662));		
	}

	
	public String toString() {
		String status;
		if(isPropertyStatus()) status = "Available";
		else status = "Rented";
		if(!ismaintenance()) status = "Under Maintenance";
		return getPropertyId()+":"+getStrNum()+":"+getStrName()+":"+getSuburb()+":"+getPropertyType()+":"+getNumOfBedRoom()+":"+status+":"+getMaintainDate().getFormattedDate();
	}
	
	public String getDetails() {
		int k = 1;
		String status;
		if(isPropertyStatus()) {
			status = "Available";
			k = 0;
		}
		else status = "Rented";
		if(!ismaintenance()) status = "Under Maintenance";
		String detail = getBasicPropertyInfo();
                
		if(getRentalRecord().getRentalRecords()[0]==null) { // only check for property without any rental record
			detail = detail +   
					String.format("%-30s%s", "Status:", "Available")+"\n"+
					String.format("%-30s%s", "Last maintenance:", getMaintainDate().getFormattedDate())+"\n"+
					String.format("%-30s%s", "RENTAL RECORD:", "Empty")+"\n";
			return detail;
		}
		
		if(isPropertyStatus()) { // only check for the first one record, true mean can rent
			detail = detail + 
					String.format("%-30s%s", "Status:", status)+"\n"+
					String.format("%-30s%s", "Last maintenance:", getMaintainDate().getFormattedDate())+"\n"+
					"----------------------------------------------";
		}
		else { detail = detail + 
				String.format("%-30s%s", "Status:", status)+"\n"+
				String.format("%-30s%s", "Last maintenance:", getMaintainDate().getFormattedDate())+"\n"+
				getRecordDatails(getRentalRecord().getRentalRecords()[0],3);} // have been rented
		
		for(int i = k;i<getRentalRecord().getRentalRecords().length;i++) {
			if(getRentalRecord().getActReDate()=="none" && getRentalRecord().getRentalRecords()[1]==null) break;
			if(getRentalRecord().getRentalRecords()[i]==null) break; // if record is empty, over
			detail = detail +"\n" + getRecordDatails(getRentalRecord().getRentalRecords()[i],6)+"\n"; // handle each record
		}
		return detail;
	}

	
}
