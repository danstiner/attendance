package comment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import time.Date;
import time.Time;

/**
 * 
 * @author Yifei Zhu, Todd Wegter
 * 
 */
@Entity
public class Message implements Comparable<Message> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// creates id for entry
	private Long id;

	private String senderNetID;
	private String recipientNetID;
	private String contents;
	private String time;
	private String[] readers;

	public Message(String from, String to, String contents) {
		this.senderNetID = from;
		this.recipientNetID = to;
		this.contents = contents;
		this.readers = new String[2];
		readers[0] = senderNetID;
		this.time = new Time().toString(24);
	}

	public String getSender() {
		return senderNetID;
	}

	public void setSender(String netID) {
		this.senderNetID = netID;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Time getTime() {
		return new Time(time);
	}

	public void setTime(Time time) {
		this.time = time.toString(24);
	}

	public String getRecipientNetID() {
		return recipientNetID;
	}

	public String getSenderNetID() {
		return senderNetID;
	}
	
	public boolean readBy(String netID) {
		return (readers[0].equals(netID) || readers[1].equals("netID"));
	}
	
	@Override
	public int compareTo(Message o) {
		if(o==null)
		{
			throw new NullPointerException("date is null");
		}
		return this.getTime().compareTo(o.getTime());
	}

}
