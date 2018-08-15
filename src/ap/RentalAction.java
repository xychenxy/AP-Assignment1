package ap;

public interface RentalAction {

	
	public abstract boolean rentDate(String customer, DateTime rentdate, int num);
	
	public abstract boolean returnDate(DateTime actuRe); 
	
	public abstract boolean performMaintenance(); 
	
	public abstract boolean completeMaintenance(DateTime completionDate);
	
}
