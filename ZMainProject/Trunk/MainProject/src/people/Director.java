package people;

import javax.persistence.*;


/**
 * 
 * @author yzhu
 *
 */
@Entity(name="Director")
@DiscriminatorValue("Director")
public class Director extends Person{

	public Director(String netID, String password, String firstName,
			String lastName, String univID) 
	{
		super(netID, password, firstName, lastName, univID);
	}

	
	

}
