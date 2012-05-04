package edu.iastate.music.marching.attendance.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.datanucleus.sco.backed.LinkedList;

import edu.iastate.music.marching.attendance.App;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.User;

public class MobileDataController {
	
	private DataTrain train;
	
	MobileDataController(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	public String getClassList() {
		
		//TODO
		//ensure proper delimitation
		
		List<User> users = train.getUsersController().get(User.Type.Student, User.Type.TA);
		
		StringBuilder sb = new StringBuilder();
		
		for (User next : users) {
			if (next.getType() == User.Type.Director) {
				//do nothing
			} else if (next.getType() == User.Type.TA){
				sb.append("TA" + "&split&" + next.getNetID() + "&split&" + next.getFirstName() + "&split&" + next.getLastName() + "&split&" + App.getHashedMobilePassword() + "&split&" + next.getRank());
			} else if (next.getType() == User.Type.Student) {
				sb.append("Student" + "&split&" + next.getNetID() + "&split&" + next.getFirstName() + "&split&" + next.getLastName() + "&split&" + next.getUniversityID() + "&split&" + next.getRank());
			}
		}
		return sb.toString();
	}
	
	public boolean pushMobileData(String data) {

		String[] fullLines = data.split("&newline&");

		ArrayList<String> eventLines = new ArrayList<String>();
		ArrayList<String> otherLines = new ArrayList<String>();
		
		//for each line, we want to create an object of the appropriate type using the controllers
		for (String s : fullLines) {
			if (s.contains("storedPerformance") || s.contains("storedRehearsal")) {
				eventLines.add(s);
			} else {
				otherLines.add(s);
			}
		}
		
		EventController ec = this.train.getEventsController();
		AbsenceController ac = this.train.getAbscencesController();
		UserController uc = this.train.getUsersController();
		
		//List<Event> localEvents = new LinkedList<Event>();
		
		//TODO do we get the data in the same format as we push it? i.e., "&split&" delimited?
		for (String s : eventLines) {
			//TODO, this is all really bullshit to mock it up. 
			String[] event = s.split("&split&");
			String type = event[0];
			String startTime = event[1];
			String startDate = event[2];
			String endTime = event[3];
			String endDate = event[4];
			Date start = new Date();
			Date end = new Date();
			
			Event.Type type2 = null;
			Event newEvent = ec.createOrUpdate(type2, start, end);
			
			if (newEvent == null) {
				//do something TODO
			}
		}
		
		for (String s : otherLines) {
			if (s.contains("tardy")) {
				Date time;
				ac.createOrUpdateTardy(time);
			} else if (s.contains("absent")) {
				
				String netid;
				
				Date start;
				Date end;
				
				User student = uc.get(netid);
				
				ac.createOrUpdateAbsence(student, start, end);
			} else if (s.toLowerCase().contains("earlycheckout")) {
				Date time;
				
				ac.createOrUpdateEarlyCheckout(time);
			} else {
				//WE HAVE SOMETHING INCORRECT HERE, JIM.
			}
		}
		
		
		return true;
	}
	
//	private static class Parser {
//		private static final String absentPrependPerformance = "absentStudentPerformance";
//		private static final String absentPrependRehearsal = "absentStudentRehearsal";
//		private static final String rehearsalPrepend = "storedRehearsal";
//		private static final String performancePrepend = "storedPerformance";
//		private static final String tardyPrepend = "tardyStudent";
//		private static final String studentPrepend = "studentRecord";
//
//		//Remember the people absent and if they were tardy then just remove the absence.
//		public static String splat(String add) {
//			// Splats the massive string into an array of strings for each person
//			String[] people = add.split(",");
//			
//			List<AbsenceEntry> absences = new LinkedList<AbsenceEntry>();
//			List<TardyEntry> tardies = new LinkedList<TardyEntry>();
//			List<Event> events = new LinkedList<Event>();
//			
//			int breakspot = 0;
//
//			try
//			{
//				for (String e : people) {				
//					
//					// The person splat is in the form
//					// prepend, firstName, lastName, netID, date, startTime, endTime,
//					// rank
//					String[] personalInfo = e.split("&split&");
//					breakspot=1;
//					String prepend = personalInfo[0];
//					breakspot=2;
//					if (prepend.equalsIgnoreCase(studentPrepend))
//						continue;
//					else if (prepend.equalsIgnoreCase(rehearsalPrepend) || prepend.equalsIgnoreCase(performancePrepend))
//					{
//						//Store the Event
//						String date = personalInfo[4];
//						breakspot=3;
//						String startTime = personalInfo[5];
//						breakspot=4;
//						String endTime = personalInfo[6];
//						breakspot=5;
//						Date useDate = parseDate(date);
//						breakspot=6;
//						Time start = parseTime(startTime, useDate);
//						breakspot=7;
//						Time end = parseTime(endTime, useDate);
//						breakspot=8;
//						Event newEvent = new Event(start, end, prepend);
//						breakspot=9;
//						events.add(newEvent);
//						breakspot=10;
//						DatabaseUtil.addEvent(newEvent);
//						breakspot=11;
//					}
//					else if (prepend.equalsIgnoreCase(absentPrependPerformance) || prepend.equalsIgnoreCase(absentPrependRehearsal)
//							|| prepend.equalsIgnoreCase(tardyPrepend))
//					{
//						//Store the absence or tardy
//						String netID = personalInfo[3];
//						breakspot=12;
//						String date = personalInfo[4];
//						breakspot=13;
//						String startTime = personalInfo[5];
//						breakspot=14;
//						String endTime = personalInfo[6];
//						breakspot=15;
//						
//						User person = DatabaseUtil.getUser(netID);
//						breakspot=16;
//						Date useDate = parseDate(date);
//						breakspot=17;
//						Time start = parseTime(startTime, useDate);
//						breakspot=18;
//						Time end = parseTime(endTime, useDate);
//						breakspot=19;
//						updateLists(person, prepend, useDate, start, end, absences, tardies);
//						breakspot=20;
//					}
//				}
//				updateALLTheThings(absences, tardies, events);
//				breakspot=21;
//				return "success";
//			}
//			catch (Exception exception)
//			{
//				//exception.printStackTrace();
//				return "fail " + breakspot + " " + exception;
//			}
//		}
//			
//
//		private static Date parseDate(String date) {
//			// Dates in the form year-month-day
//			Scanner parser = new Scanner(date).useDelimiter("-");
//			return new Date(Integer.parseInt(parser.next()),
//					Integer.parseInt(parser.next()),
//					Integer.parseInt(parser.next()));
//		}
//
//		private static Time parseTime(String time, Date date) {
//			if (time.length() >= 4)
//			{
//				int hour = Integer.parseInt(time.substring(0, 2));
//				int minute = Integer.parseInt(time.substring(2, 4));
//				return new Time(hour, minute, date);
//			}
//			else //Return an empty date. This should only happen for Tardy endtimes cause it's just a |
//				return new Time(0,0, date);
//		}
//
//		private static void updateLists(User guy, String prepend, Date eventDate, Time start, Time end, List<AbsenceEntry> absences, List<TardyEntry> tardies) {
//			if ( guy!= null && (guy.getType().equalsIgnoreCase("Student")))
//			{
//				//Adding performance absences
//				if (prepend.equalsIgnoreCase(absentPrependPerformance)) 
//				{
//					Absence toAdd = new Absence(guy.getNetID(), start, end, "Performance");
//					absences.add(new AbsenceEntry(toAdd, guy));
//				} 
//				//Adding rehearsal absences
//				else if (prepend.equalsIgnoreCase(absentPrependRehearsal)) 
//				{
//					
//					Absence toAdd = new Absence(guy.getNetID(), start, end, "Rehearsal");
//					absences.add(new AbsenceEntry(toAdd, guy));
//				}
//				//Adding tardies -at this point we don't know what type it is-
//				else if (prepend.equalsIgnoreCase(tardyPrepend)) 
//				{
//					//guy.addTardy(new Tardy(start, "unknown"));
//					Absence toAdd = new Absence(guy.getNetID(), start, "unknown");
//					tardies.add(new TardyEntry(toAdd, guy));
//				}
//			}
//		}
//		
//		//Returns true if second time is later  than first time
//		private static boolean timeLaterThan(Time firstTime, Time secondTime)
//		{
//			return secondTime.compareTo(firstTime) > 0;
//		}
//		
//		private static void updateALLTheThings(List<AbsenceEntry> absences, List<TardyEntry> tardies, List<Event> events)
//		{
//			//Update the tardies to what type of event they are: rehearsal, performance
//			List<TardyEntry> tardsToRemove = new LinkedList<TardyEntry>();
//			List<AbsenceEntry> absToRemove = new LinkedList<AbsenceEntry>();
//
//			//Go thru all the tardies and try to add the type
//			for (TardyEntry te: tardies)
//			{
//				for (int i = 0; i < events.size(); i++)
//				{
//					Event e = events.get(i);
//					if (te.isDuringEvent(e))
//					{
//						te.value.setType(e.getType());
//						te.setMyEvent(e);
//					}
//				}
//			}
//			
//			//Remove the absences that correspond to the same event and student as a tardy
//			for (TardyEntry te: tardies)
//			{
//				for (AbsenceEntry ae: absences)
//				{
//					if (te.isDuringAbsence(ae))
//					{
//						absToRemove.add(ae);
//					}
//				}
//			}
//			
//			//Go thru all the tardies and change it to an absence if it is later than half an hour from the start of the event.
//			for (TardyEntry te: tardies)
//			{
//				if (te.isLaterThanHalfHourForMyEvent())
//				{
//					//Remove the tardy and add it as an absence
//					Absence abs = new Absence(te.key.getNetID(), te.myEvent.getStartTime(), te.myEvent.getEndTime(), te.value.getType());
//					absences.add(new AbsenceEntry(abs, te.key));
//					tardsToRemove.add(te);
//				}	
//			}
//			
//			//Check forms
//			for (TardyEntry te: tardies)
//			{
//				List<Form> forms = DatabaseUtil.getForms(te.key.getNetID());
//				for (int i = 0; i < forms.size(); i++)
//				{
//					if (te.checkFormSatisfies(forms.get(i)))
//					{
//						tardsToRemove.add(te);
//					}
//				}
//			}
//			for (AbsenceEntry ae: absences)
//			{
//				List<Form> forms = DatabaseUtil.getForms(ae.key.getNetID());
//				for (int i = 0; i < forms.size(); i++)
//				{
//					if (ae.checkFormSatisfies(forms.get(i)))
//					{
//						absToRemove.add(ae);
//					}
//				}
//			}
//			
//			for (int i = 0; i < tardsToRemove.size(); i++)
//				tardies.remove(tardsToRemove.get(i));
//			for (int i = 0; i < absToRemove.size(); i++)
//				absences.remove(absToRemove.get(i));
//			
//			for (AbsenceEntry ae: absences)
//			{
//				List<Absence> myAbsences = DatabaseUtil.getAbsences(ae.key.getNetID());
//				//Remove the old ones
//				for (int i = 0; i < myAbsences.size(); i++)
//				{
//					if (myAbsences.get(i).equals(ae.value))
//					{
//						DatabaseUtil.removeAbsence(myAbsences.get(i));
//					}
//				}
//				DatabaseUtil.addAbsence(ae.value);
//			}
//			
//			for (TardyEntry te: tardies)
//			{
//				List<Tardy> myTardies = DatabaseUtil.getTardies(te.key.getNetID());
//				for (int i = 0; i < myTardies.size(); i++)
//				{
//					if (myTardies.get(i).equals(te.value))
//					{
//						DatabaseUtil.removeTardy(myTardies.get(i));
//					}
//				}
//				DatabaseUtil.addTardy(te.value);
//			}
//			
//		}
//		
//		private static boolean canGetToStartBySeven(Date currentDate, Date startDate)
//		{
//			int monthDays[][] = {{0}, {31}, {28, 29}, {31}, {30}, {31}, {30}, {31}, {31}, {30}, {31}, {30}, {31}};
//			int febDays = (currentDate.isLeapYear()) ? 1 : 0;
//			int curMonth = currentDate.getMonth();
//			int curDay = currentDate.getDay();
//			Date temp = new Date(currentDate.getYear(), curMonth, curDay);
//			while (temp.compareTo(startDate) > 0)
//			{
//				curDay -= 7;
//				//If we need to go back a month
//				if (curDay <= 0)
//				{
//					curDay = monthDays[curMonth - 1][(curMonth == 2) ? febDays : 0] + curDay;
//					curMonth--;
//					temp.setMonth(curMonth);
//				}
//				temp.setDay(curDay);
//			}
//			if (temp.compareTo(startDate) == 0)
//				return true;
//			return false;
//			
//		}
//		
//		public static class AbsenceEntry
//		{
//			public Absence value;
//			public User key;
//			public Event myEvent;
//			
//			public AbsenceEntry(Absence v, User k)
//			{
//				value = v;
//				key = k;
//			}
//			
//			public boolean equals(Object o)
//			{
//				if (o == null || o.getClass() != this.getClass()) return false;
//				AbsenceEntry ae = (AbsenceEntry) o;
//				return ae.value.equals(value) && ae.key.equals(key);
//			}
//			
////			public void setMyEvent(Event e)
////			{
////				myEvent = e;
////			}
//			
//			public String toString()
//			{
//				return key.getNetID() + " "+ value.toString();
//			}
//			
//			public boolean isDuringEvent(Event e)
//			{
//				return e.getStartTime().compareTo(value.getStartTime()) <= 0 
//						&& e.getEndTime().compareTo(value.getStartTime()) >= 0;
//			}
//			
//			public boolean checkFormSatisfies(Form f)
//			{
//				if (f.getType().equalsIgnoreCase("formA"))
//				{
//					//Form A only works for performances
//					if (value.getType().equalsIgnoreCase("Performance"))
//					{
//						//If it is an approved form
//						if (f.getStatus().equalsIgnoreCase("Approved"))
//						{
//							//If it is the same day
//							if (value.getStartTime().getDate().equals(f.getStartTime().getDate()))
//							{
//								return true;
//							}
//						}
//					}
//					return false;
//				}
//				else if (f.getType().equalsIgnoreCase("formB"))
//				{
//					if (f.getStatus().equalsIgnoreCase("Approved"))
//					{
//						if (timeLaterThan(f.getStartTime(), value.getStartTime()) && timeLaterThan(value.getEndTime(), f.getEndTime()))
//						{
//							String duration = f.durationToString();
//							//dur[0] is either until, Starting, or Completely
//							String[] dur = duration.split(" ");
//							if (dur[0].equalsIgnoreCase("until"))
//							{
//								//If the hours aren't the same
//								if (f.getEndTime().getHour() - value.getStartTime().getHour() == 0)
//								{
//									//TODO make this not just 15 minutes
//									if (f.getEndTime().getMinute() + 15 > value.getStartTime().getMinute())
//									{
//										return true;
//									}
//								}
//								//We are looking at a time when where endTime is like 7:45 and check in at 8
//								//When this is the case the endtime minute should be less than the check in minute
//								if (f.getEndTime().getHour() - value.getStartTime().getHour() == -1)
//								{
//									if (f.getEndTime().getMinute() + 15 > value.getStartTime().getMinute())
//									{
//										return false;
//									}
//									return true;
//								}
//							}
//							else if (dur[0].equalsIgnoreCase("Starting"))
//							{
//								//TODO Early check out don't need to handle
//								
//							}
//							else if (dur[0].equalsIgnoreCase("Completely"))
//							{
//								if (canGetToStartBySeven(value.getStartTime().getDate(), f.getStartTime().getDate()))
//									return true;
//								return false;
//							}
//							else
//							{
//								System.err.println("Bad things happened, couldn't figure out what type of form B it was");
//							}
//						}
//					}
//					return false;
//				}
//				
//				else if (f.getType().equalsIgnoreCase("formC"))
//				{
//					//Form C only works for performances
//					if (value.getType().equalsIgnoreCase("Rehearsal"))
//					{
//						//If it is an approved form
//						if (f.getStatus().equalsIgnoreCase("Approved"))
//						{
//							//If it is the same day
//							if (value.getStartTime().getDate().equals(f.getStartTime().getDate()))
//							{
//								return true;
//							}
//						}
//					}
//					return false;
//				}
//				else
//				{
//					System.err.println("Error occurred.. didn't know when what type of Form B it was");
//					return false;
//				}
//			}
//				
//		
//		}
//		public static class TardyEntry
//		{
//			public Absence value;
//			public User key;
//			public Event myEvent;
//			
//			public TardyEntry(Absence v, User k)
//			{
//				value = v;
//				key = k;
//			}
//			
//			public boolean equals(Object o)
//			{
//				if (o == null || o.getClass() != this.getClass()) return false;
//				TardyEntry ae = (TardyEntry) o;
//				return ae.value.equals(value) && ae.key.equals(key);
//			}
//			
//			public String toString()
//			{
//				return key.getNetID() + " " + value.toString();
//			}
//			
//			public boolean isDuringEvent(Event e)
//			{
//				return e.getStartTime().compareTo(value.getTime()) <= 0 
//						&& e.getEndTime().compareTo(value.getTime()) >= 0;
//			}
//			
//			public void setMyEvent(Event e)
//			{
//				myEvent = e;
//			}
//			
//			public boolean isLaterThanHalfHourForMyEvent()
//			{
//				if (myEvent == null)
//					return false;
//				Time myTime = value.getTime();
//				if (myTime.getDate().compareTo(myEvent.getDate()) >= 1)
//				{
//					return true;
//				}
//				else if (myTime.getHour() - myEvent.getStartTime().getHour() >= 1)
//				{
//					return true;
//				}
//				else if (myTime.getMinute() - myEvent.getStartTime().getMinute() >= 30)
//				{
//					return true;
//				}
//				return false;
//			}
//			
//			//Method used to check when we need to remove an Absence because a Tardy corresponds to the same event
//			public boolean isDuringAbsence(AbsenceEntry a)
//			{
//				//Check that the absence is during this event
//				if (myEvent == null)
//					return false;
//				if (a.isDuringEvent(myEvent))
//					return key.getNetID().equals(a.key.getNetID());
//				return false;
//			}
//			
//			public boolean checkFormSatisfies(Form f)
//			{
//				if (f.getType().equalsIgnoreCase("formA"))
//				{
//					//Form A only works for performances
//					if (value.getType().equalsIgnoreCase("Performance"))
//					{
//						//If it is an approved form
//						if (f.getStatus().equalsIgnoreCase("Approved"))
//						{
//							//If it is the same day
//							if (value.getTime().getDate().equals(f.getStartTime().getDate()))
//							{
//								return true;
//							}
//						}
//					}
//					return false;
//				}
//				else if (f.getType().equalsIgnoreCase("formB"))
//				{
//					if (f.getStatus().equalsIgnoreCase("Approved"))
//					{
//						if (timeLaterThan(value.getTime(), f.getStartTime()) && timeLaterThan(f.getEndTime(), value.getTime()))
//						{
//							String duration = f.durationToString();
//							//dur[0] is either until, Starting, or Completely
//							String[] dur = duration.split(" ");
//							if (dur[0].equalsIgnoreCase("until"))
//							{
//								//If the hours aren't the same
//								if (f.getEndTime().getHour() - value.getTime().getHour() == 0)
//								{
//									//TODO make this not just 15 minutes
//									if (f.getEndTime().getMinute() + 15 > value.getTime().getMinute())
//									{
//										return true;
//									}
//								}
//								//We are looking at a time when where endTime is like 7:45 and check in at 8
//								//When this is the case the endtime minute should be less than the check in minute
//								if (f.getEndTime().getHour() - value.getTime().getHour() == -1)
//								{
//									if (f.getEndTime().getMinute() + 15 > value.getTime().getMinute())
//									{
//										return false;
//									}
//									return true;
//								}
//							}
//							else if (dur[0].equalsIgnoreCase("Starting"))
//							{
//								//TODO Early check out don't need to handle
//								
//							}
//							else if (dur[0].equalsIgnoreCase("Completely"))
//							{
//								//TODO
//								if (canGetToStartBySeven(value.getTime().getDate(), f.getStartTime().getDate()))
//									return true;
//								return false;
//							}
//							else
//							{
//								System.err.println("Bad things happened, couldn't figure out what type of form B it was");
//							}
//						}
//					}
//					return false;
//				}
//				
//				else if (f.getType().equalsIgnoreCase("formC"))
//				{
//					//Form C only works for performances
//					if (value.getType().equalsIgnoreCase("Rehearsal"))
//					{
//						//If it is an approved form
//						if (f.getStatus().equalsIgnoreCase("Approved"))
//						{
//							//If it is the same day
//							if (value.getTime().getDate().equals(f.getStartTime().getDate()))
//							{
//								return true;
//							}
//						}
//					}
//					return false;
//				}
//				else
//				{
//					System.err.println("Error occurred.. didn't know when what type of Form B it was");
//					return false;
//				}
//			}
//				
//		}
//
//	}
//
}
