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
	
	private DateTime maintainDate;  // for premium suite, to store the last maintain date 
	private String estMaintenanceDate; // when calling perform method, create today date and store into it.
	private DateTime nextCompleteDate; // for premium suite, to store the next complete date; 
	private boolean maintenance = true; // this mean this property can be performed maintenance
		
	
	
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
		this.maintainDate = maintainDate;
		this.nextCompleteDate = new DateTime(maintainDate,10);
	}
	
	public abstract boolean rentDate(String customer, DateTime rentdate, int numOfRentDay);
	
	public abstract boolean returnDate(DateTime actuRe);
		
	public abstract boolean completeMaintenance(DateTime completionDate);
	
		
	public boolean performMaintenance() {
		if(!isPropertyStatus()) {
			System.out.println("\n cannot be perform maintenane, as being rented \n");
			return false;
		}
		
		if(!ismaintenance()) {
			System.out.println("\n cannot be perform maintenane, as has been under maintenance \n");
			return false;
		}
		
		DateTime today = new DateTime();
		setEstMaintenanceDate(today.getFormattedDate());
		
		setmaintenance(false);
		return true;
	} 
	
	public int calWeekday(String day) {  // calculate the weekend day.
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
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	public int calIntervalDays(String before,String after){ // return value is (after - before), is used to computer interval days.
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
		
	public String getRecordDatails(String str,int i) {// record info; i is 3 or 6 
		String[] s = str.split(":");	
		String detail = "please attention to value of i";
		String former =  String.format("%-30s%s", "Record ID:", s[0])+"\n"+
						String.format("%-30s%s", "Rent Date:", s[1])+"\n"+
						String.format("%-30s%s", "Estimated Return Date:", s[2])+"\n";
		String later =  String.format("%-30s%s", "Actual Return Date:", s[3])+"\n"+
						String.format("%-30s%s", "Rental Fee:", s[4])+"\n"+
						String.format("%-30s%s", "Late Fee:", s[5])+"\n";
		if(i==3) detail = former;
		if(i==6) detail = former + later;
		return detail + "\n" +
			"----------------------------------------------";
	}
	
	public String getBasicPropertyInfo() {
		String detail = String.format("%-30s%s", "Property ID:", getPropertyId())+"\n"+
				String.format("%-30s%s", "Address:", getStrNum()+" "+getStrName()+" "+getSuburb())+"\n"+
				String.format("%-30s%s", "Type:", getPropertyType())+"\n"+
				String.format("%-30s%s", "Bedroom:", getNumOfBedRoom())+"\n";
		return  detail;
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
	public DateTime getMaintainDate() {
		return maintainDate;
	}
	public void setMaintainDate(DateTime maintainDate) {
		this.maintainDate = maintainDate;
	}
	public String getEstMaintenanceDate() {
		return estMaintenanceDate;
	}
	public void setEstMaintenanceDate(String estMaintenanceDate) {
		this.estMaintenanceDate = estMaintenanceDate;
	}
	public DateTime getNextCompleteDate() {
		return nextCompleteDate;
	}
	public boolean ismaintenance() {
		return maintenance;
	}
	public void setmaintenance(boolean maintenance) {
		this.maintenance = maintenance;
	} 
	
}
