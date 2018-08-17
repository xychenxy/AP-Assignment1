package ap;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public abstract class RentalProperty implements RentalAction{
	private String propertyId;
	private String strName;
	private String strNum;
	private String suburb;
	private int numOfBedRoom;
	private String propertyType;
	private boolean propertyStatus = true;
	private RentalRecord rentalRecord = new RentalRecord();
		
	
	
	public RentalProperty(String propertyId,String strName,String strNum,String suburb,int numOfBedRoom,String propertyType) {
		this.propertyId = propertyId;
		this.strName = strName;
		this.strNum = strNum;
		this.suburb = suburb;
		this.numOfBedRoom = numOfBedRoom;
		this.propertyType = propertyType;
	
	}
	public RentalProperty(String propertyId, String strName, String strNum, String suburb, int numOfBedRoom, String propertyType, DateTime maintainDate) {
		this(propertyId, strName, strNum, suburb, numOfBedRoom, propertyType);		
	}
	
	public abstract boolean rentDate(String customer, DateTime rentdate, int numOfRentDay);
	
	public abstract boolean returnDate(DateTime actuRe);
	
	public abstract boolean performMaintenance();
	
	public abstract boolean completeMaintenance(DateTime completionDate);
	
		
	public String calWeekday(String day) { // 0 means something go wrong; 1 means Sunday;
		String[] weekday = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		Date datet = null;
		try {
			datet = f.parse(day);// Sunday is the first day;
			cal.setTime(datet);
		}
		catch (ParseException e) {
			System.out.println("date fromat is wrong //calWeekday");
		}
		return weekday[cal.get(Calendar.DAY_OF_WEEK)];
	}
	
	public int calIntervalDays(String before,String after){ // computer the interval day between before and after;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date bDate = null;
		Date aDate = null;
		try {
			bDate = sdf.parse(before); // 2018-08-01 with 2018-08-02; interval is 1;
			aDate = sdf.parse(after);
		}
		catch (ParseException e) {
			System.out.println("Date Formate is wrong.//calIntervalDays");
		}
		long days = (aDate.getTime()-bDate.getTime())/(1000*3600*24);
		int daysInt = Integer.parseInt(String.valueOf(days));
		return daysInt;
	}
	
	
	public int[] calActAndLate(String actuReDay) { // to computer the actually rental day and delay date
		int[] compareDay = new int[2];
		int temp = calIntervalDays(getRentalRecord().getEstReDate(), getRentalRecord().getActReDate());
		int temp1 = calIntervalDays(getRentalRecord().getRentDate(), getRentalRecord().getEstReDate());
		int temp2 = calIntervalDays(getRentalRecord().getRentDate(), actuReDay);
		if(temp>0) { // delay return
			compareDay[0] = temp1; // update rental fee
			compareDay[1] = temp; //update late fee
		}
		else {
			compareDay[0] = temp2; // update rental fee
			compareDay[1] = 0; //update late fee
		}
		return compareDay;
	}
	
	

	public String keepTwoDotDecimal(double s) {
		DecimalFormat df = new DecimalFormat("#.00");
		if(s==0) return "0.00";
		return df.format(s);
	}
	
	public String getPartofRecrod(String r) {
		String[] s = r.split(":");
		return  String.format("%-30s%s", "Record ID:", s[0])+"\n"+
				String.format("%-30s%s", "Rent Date:", s[1])+"\n"+
				String.format("%-30s%s", "Estimated Return Date:", s[2])+"\n"+
				"----------------------------------------------";
	}
	
	public String getRecordDatails(String r) {// record info
		String[] s = r.split(":");		
		return  String.format("%-30s%s", "Record ID:", s[0])+"\n"+
			String.format("%-30s%s", "Rent Date:", s[1])+"\n"+
			String.format("%-30s%s", "Estimated Return Date:", s[2])+"\n"+
			String.format("%-30s%s", "Actual Return Date:", s[3])+"\n"+
			String.format("%-30s%s", "Rental Fee:", s[4])+"\n"+
			String.format("%-30s%s", "Late Fee:", s[5])+"\n"+
			"----------------------------------------------";
	}
	
	
	public boolean isPropertyStatus() {
		return propertyStatus;
	}
	public void setPropertyStatus(boolean propertyStatus) {
		this.propertyStatus = propertyStatus;
	}
	public String getPropertyId() {
		return propertyId;
	}
	public String getStrName() {
		return strName;
	}
	public String getStrNum() {
		return strNum;
	}
	public String getSuburb() {
		return suburb;
	}
	public int getNumOfBedRoom() {
		return numOfBedRoom;
	}
	public String getPropertyType() {
		return propertyType;
	}
	public RentalRecord getRentalRecord() {
		return rentalRecord;
	} 
}
