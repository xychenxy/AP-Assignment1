package ap;

public class PremiumSuite extends RentalProperty {
	private DateTime maintainDate;
	private String estMaintenanceDate;
	private DateTime nextCompleteDate;
	private boolean maintainStatus = true;
		
	public PremiumSuite(String propertyId, String strName, String strNum, String suburb, int numOfBedRoom, String propertyType, DateTime maintainDate) {
		super(propertyId, strName, strNum, suburb, numOfBedRoom, propertyType);
		this.maintainDate = maintainDate;
		this.nextCompleteDate = new DateTime(maintainDate,10);
	}
	
	
	public boolean rentDate(String customer, DateTime rentdate, int numOfRentDay) { // return true means it can be booked
		DateTime estimatRe = new DateTime(rentdate,numOfRentDay); // to create a estimate return days
		
		if(!isPropertyStatus()) { // 1. check the status 
			System.out.println("Sorry, this premium suite has been rented.");
			return false;
		}
		
		if(!maintainStatus) {
			System.out.println("The Premium Suite is under maintenance.");
			return false; // !maintainStatus means under maintain
		}
						
		if( !(calIntervalDays(estimatRe.getFormattedDate(), nextCompleteDate.getFormattedDate())>0)) {
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
		
		if(!maintainStatus) {// below handle under maintain, 
			if(calIntervalDays(actuReturnDay, estMaintenanceDate)<=0) {
				System.out.println("Sorry, you cannot return the premium suite over the maintenance day.");
				return false;
			}
		}
		else { // below handle out of under maintain,
			if(calIntervalDays(actuReturnDay, nextCompleteDate.getFormattedDate())<=0) {
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
		System.out.println("return success"); 
		return true;
	}
	
	public boolean performMaintenance() {  
		if(!isPropertyStatus()) {
			System.out.println("The Premium Suite is being rented, no allow to perform maintenance.");
			return false; 
		}
		if(!maintainStatus) {
			System.out.println("The Premium Suite is under maintenance.");
			return false; 
		}
		DateTime today = new DateTime();
		estMaintenanceDate = today.getFormattedDate();
		maintainStatus = false; 
		return true; // return true mean under maintain
	}
	
	public boolean completeMaintenance(DateTime completionDate) {
		if(!isPropertyStatus()) {
			System.out.println("The Premium Suite is being rented, no allow to complete maintenance.");
			return false;
		}
		if(maintainStatus) {
			System.out.println("The Premium Suite is no under maintenance. pls perform maintenance at first.");
			return false; 
		}
		
		if(calIntervalDays(estMaintenanceDate, completionDate.getFormattedDate())<0 || calIntervalDays(completionDate.getFormattedDate(), nextCompleteDate.getFormattedDate())<0){
			System.out.println("complete maintenance day should between perform maintenance day and the next complete maintenance day.");
			return false; 
		}
		
		maintainStatus = true; 
		maintainDate = completionDate;
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
		if(!maintainStatus) status = "Under Maintenance";
		return getPropertyId()+":"+getStrNum()+":"+getStrName()+":"+getSuburb()+":"+getPropertyType()+":"+getNumOfBedRoom()+":"+status+":"+maintainDate.getFormattedDate();
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
				getPartofRecrod(getRentalRecord().getRentalRecords()[0]);} // have been rented
		
		
		for(int i = k;i<getRentalRecord().getRentalRecords().length;i++) {
			if(getRentalRecord().getActReDate()=="none" && getRentalRecord().getRentalRecords()[1]==null) break;
			if(getRentalRecord().getRentalRecords()[i]==null) break; // if record is empty, over
			detail = detail +"\n" + getRecordDatails(getRentalRecord().getRentalRecords()[i])+"\n"; // handle each record
		}
		
		return detail;
	}

	
}
