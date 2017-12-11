/**
 * Fall 2017 CS151 Programming Assignment 4
 * My First Calendar 
 * 11/20/2017
 * Author: Marissa Xiong
 */

public class Event implements Comparable<Event> {
	private String event;

	String d;
	String t;
	int day;
	int month;
	int year;
	MONTHS month1;
	int start;
	int end;
	
	public Event(){
		
	}

	public Event(String title, int month, int day, int year, int st, int et) {
		t = title;
		this.month = month;
		this.month = day;
		this.year = year;
		start = st;
		end = et;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public String getStartString() {
		String st = String.valueOf(start);
		if (st.length() < 4){
			st = "0"+st;
		}
		if (st.length() == 4) {
			String f = st.substring(0, 2);
			String e = st.substring(2, 4);
			return f + ":" + e;
		} else
			return st;
	}

	public String getEndString() {
		String st = String.valueOf(end);
		if (st.length() < 4){
			st = "0"+st;
		}
		if (st.length() == 4) {
			String f = st.substring(0, 2);
			String e = st.substring(2, 4);
			return " - " + f + ":" + e;
		} else
			return "";
	}

	public String getTitle() {
		return t;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	public int getDay() {
		return day;
	}

	public String getDate() {
		return d;
	}

	public String toString() {
		return event;
	}

	boolean equals(Event e) {
		if (this.getMonth() == e.getMonth()) {
			if (this.getDay() == e.getDay()) {
				if (this.getYear() == e.getYear()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int compareTo(Event e) {
		int thisStart = this.getStart();
		int thisEnd = this.getEnd();
		int eStart = e.getStart();
		int eEnd = e.getEnd();

		int compare = 0;

		if (thisStart < eStart) {
			compare = -1;
		}
		if (thisStart > eStart) {
			compare = 1;
		}
		if (thisEnd > eStart) {
			compare = 0;
		} 
		else if (thisEnd < eEnd) {
			compare = -1;
		} else if (thisEnd > eEnd) {
			compare = 1;
		}
		return compare;
	}
}
