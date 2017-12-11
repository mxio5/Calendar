import java.util.ArrayList;
import java.util.TreeMap;

public class SimpleCalendar {
	public static void main(String[] args){
		ArrayList<Event> s = new ArrayList<>();
		ArrayList<Event> s2 = new ArrayList<>();

		TreeMap<String, ArrayList<Event>> tm = new TreeMap<>();
		Event e = new Event("tester", 11, 20, 2017, 1200, 1400);
		s.add(e);
		tm.put("11/20/2017", s);
		CalendarModel calModel = new CalendarModel(tm);
		CalendarView c = new CalendarView(calModel);
		
		calModel.attach(c);

		//c.draw();
	}
}
