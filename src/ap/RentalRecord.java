package ap;
import java.text.DecimalFormat;

public class RentalRecord {

	private String[] rentalRecords;
	private String recordId = "none";
	private String rentDate = "none"; 
	private String estReDate = "none";
	private String actReDate = "none";
	private double rentalFee = 0.00;
	private double lateFee = 0.00;
	
	private String customerID;
	
	
	public RentalRecord() {
		this.rentalRecords = new String[10];
	}
	
	
	public void createRecord(String propertyId,String customer, String rentaldate, String estimatedate) { 
		customerID = customer;
		rentDate = rentaldate; // dd/MM/yyyy
		estReDate = estimatedate; // dd/MM/yyyy
		String[] tempList = rentDate.split("/"); // deal with the format
		String tempStr = tempList[0]+tempList[1]+tempList[2];
		recordId = propertyId+"_"+customer+"_"+tempStr;
		for(int i=9;i>0;i--) { rentalRecords[i] = rentalRecords[i-1];} // change position and then update the first one record
		rentalRecords[0] = recordId +":"+ rentDate +":"+ estReDate +":"+actReDate+":"+rentalFee+":"+lateFee;	
	}
	
	public void updateRecord() {
		rentalRecords[0] = recordId +":"+ rentDate +":"+ estReDate +":"+actReDate+":"+rentalFee+":"+lateFee;
	}
	
	public String toString() {
	if(actReDate.equals("none")) { 
		return getRecordId()+":"+getRentDate()+":"+getEstReDate()+":"+"none"+":"+"none"+":"+"none";
	}
	return getRecordId()+":"+getRentDate()+":"+getEstReDate()+":"+getActReDate()+":"+getRentalFee()+":"+getLateFee();
	}

	public String getDetails() {
		if(actReDate.equals("none")) { 
			return  String.format("%-30s%s", "Record ID:", recordId)+"\n"+
					String.format("%-30s%s", "Rent Date:", rentDate)+"\n"+
					String.format("%-30s%s", "Estimated Return Date:", estReDate);
		}
		else {
			return 	String.format("%-30s%s", "Record ID:", recordId)+"\n"+
					String.format("%-30s%s", "Rent Date:", rentDate)+"\n"+
					String.format("%-30s%s", "Estimated Return Date:", estReDate)+"\n"+
					String.format("%-30s%s", "Actual Return Date:", actReDate)+"\n"+
					String.format("%-30s%s", "Rental Fee:", twoDotDecimalDouble(rentalFee))+"\n"+
					String.format("%-30s%s", "Late Fee:", twoDotDecimalDouble(lateFee));
		}
	}
	

	public String twoDotDecimalDouble(double s) {
		DecimalFormat df = new DecimalFormat("#.00");
		if(s==0) return "0.00";
		return df.format(s);
	}
	
	
	
	
	public String[] getRentalRecords() {
		return rentalRecords;
	}

	public String getRecordId() {
		return recordId;
	}
	
	public String getRentDate() {
		return rentDate;
	}

	public String getEstReDate() {
		return estReDate;
	}

	public String getActReDate() {
		return actReDate;
	}

	public double getRentalFee() {
		return rentalFee;
	}

	public void setRentalFee(double rentalFee) {
		this.rentalFee = rentalFee;
	}

	public double getLateFee() {
		return lateFee;
	}

	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}

	public void setActReDate(String actReDate) {
		this.actReDate = actReDate;
	}

	public String getCustomerID() {
		return customerID;
	}
		
}
