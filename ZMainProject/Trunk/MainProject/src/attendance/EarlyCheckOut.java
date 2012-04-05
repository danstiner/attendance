package attendance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import time.Time;
/**
 * 
 * @author Yifei Zhu, Todd Wegter
 *
 */

//get and set messages with key field with corresponding times and type

@Entity
public class EarlyCheckOut{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  //creates id for entry
	private Long id;
	
	private String checkOutTime;
	private boolean approved;
	private String type;
	
	public EarlyCheckOut(Time checkOutTime){
		this.checkOutTime = checkOutTime.toString(24);
		this.approved = false;
	}

	public String toString()
	{      
		//Should be in the form "year-month-day hour:minute:second type isApproved"
		return getTime().getDate().toString() + " " + getTime().get24Format() + " " + type + " " + approved;
	}
	
	public Time getTime() {
		return new Time(checkOutTime);
	}

	public void setTime(Time time) {
		this.checkOutTime = time.toString(24);
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		if(type.equalsIgnoreCase("performance"))
			this.type = "performance";
		else
			this.type = "rehearsal";
	}
	
	public boolean isRehearsal(){
		return type.equalsIgnoreCase("rehearsal");
	}
	
	public boolean isPerformance(){
		return type.equalsIgnoreCase("performance");
	}

	public void setID(long id){
		this.id = id;
	}
	
	public long getID(){
		return id;
	}
}
