import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

enum MONTHS {
	January, February, March, April, May, June, July, August, September, October, November, December;

}

enum DAYS {
	Su, Mo, Tu, We, Th, Fr, Sa;
}

public class CalendarView extends JFrame implements ChangeListener {
	ArrayList<Event> data = new ArrayList<Event>();
	private CalendarModel calModel;

	TreeMap<String, ArrayList<Event>> eventsList;
	ArrayList<Event> events = new ArrayList<>();
	ArrayList<String> details = new ArrayList<>();
	private GregorianCalendar c = new GregorianCalendar(); // capture today
	int actualDay = c.get(Calendar.DATE);
	String currentDate;

	public CalendarView(CalendarModel m) {
		calModel = m;
		eventsList = calModel.getData();
		int curMonth = c.get(Calendar.MONTH) + 1;
		int curYear = c.get(Calendar.YEAR);
		
		currentDate = curMonth + "/"+ actualDay + "/"+curYear;
		System.out.println("first: " +currentDate);
		draw();
	}
	
	
	/**
	 * compiles all events into a string
	 * @return a string of all events
	 */
	public String getEventList() {
		String list = "";
		Set<String> set = eventsList.keySet();
		if (set.isEmpty()){
			System.out.println("No Events");
		}
		for (String s: set){
			ArrayList<String> arrayliststring = getEvent(s);
			for (String sr : arrayliststring)
			list += "\n" + sr;
		}
		return list;
	}

	public void draw() {
		
		setLayout(new BorderLayout());
		JFrame frame = this;

		JButton left = new JButton("<");
		JButton right = new JButton(">");
		JButton create = new JButton("Create Event");
		JButton quit = new JButton("Quit");
		JPanel top = new JPanel();
		JPanel calendarPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		JTextArea text = new JTextArea(20, 30);


		// CLOSE ACTIONLISTENER
		ActionListener a = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String liste = getEventList();
				try {
			         FileOutputStream fileOut = new FileOutputStream("events.txt");
			         ObjectOutputStream out = new ObjectOutputStream(fileOut);
			         out.writeObject(liste);
			         out.close();
			         fileOut.close();
			         System.out.print("Serialized data is saved in events.txt");
			      }catch(IOException i) {
			         System.out.println("Error: First run.");
			      }
				dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		};
		
		
		actualDay = Integer.parseInt(currentDate.substring(3, 5));
		calendarPanel.add(drawCalendar());


		// RIGHT ONE DAY ACTIONLISTENER
		ActionListener r = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewByDay(currentDate, "n");
				System.out.println("drawing");
				draw();
				System.out.println("drew");
			}
		};
		right.addActionListener(r);

		// LEFT ONE DAY ACTIONLISTENER
		ActionListener l = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewByDay(currentDate, "p");
				System.out.println("left_drawing");
				draw();
				System.out.println("left_drew");
			}
		};
		left.addActionListener(l);

		// CREATE BUTTON ACTIONLISTENER
		ActionListener c = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// current date in mm/dd/yyyy form

				JPanel createPanel = new JPanel();

				JPanel panel = new JPanel(new FlowLayout());
				JPanel panel2 = new JPanel(new FlowLayout());
				createPanel.setLayout(new GridBagLayout());

				JLabel label = new JLabel("Event: ");
				JButton save = new JButton("Save");
				JTextField writeField = new JTextField(20);
				writeField.setEditable(true);

				JTextField date = new JTextField(currentDate);
				System.out.println("CURRENTDATE_2: " + currentDate);
				JLabel time = new JLabel("Time: ");
				JTextField start = new JTextField(4);
				JLabel to = new JLabel(" to ");
				JTextField end = new JTextField(4);

				panel.add(label);
				panel.add(writeField);

				panel2.add(date);
				panel2.add(time);
				panel2.add(start);
				panel2.add(to);
				panel2.add(end);

				createPanel.add(panel);
				createPanel.add(panel2);
				
				
				// CREATE AN EVENT
				int optionType = JOptionPane.DEFAULT_OPTION;
				int messageType = JOptionPane.PLAIN_MESSAGE;
				Icon icon = null;
				String[] options = { "Save" };
				int result = JOptionPane.showOptionDialog(null, createPanel, "Create", optionType, messageType, icon,
						options, options[0]);
				if (result == JOptionPane.OK_OPTION) {
					String dateString = date.getText();
					String w = writeField.getText();
					String s = start.getText();
					String evString = end.getText();
					String etall = "0";

					String year = dateString.substring(6, 10);
					String day = dateString.substring(3, 5);
					String month = dateString.substring(0, 2);
					String stbeg = s.substring(0, 2);
					String stend = s.substring(3, 5);
					String stall = stbeg + stend;
					if (evString.length() > 0) {
						String etbeg = evString.substring(0, 2);
						String etend = evString.substring(3, 5);
						etall = etbeg + etend;
					}
					int y = Integer.parseInt(year);
					int d = Integer.parseInt(day);
					int m = Integer.parseInt(month) - 1;
					int st = Integer.parseInt(stall);
					int et = Integer.parseInt(etall);
					if (et != 0 && st > et) {
						System.out.println("Event ends before it starts. Cannot add.");
					} else {
						Event ev = new Event(w, m, d, y, st, et);
						if (eventsList.containsKey(dateString)) {
							ArrayList<Event> existingEvent = eventsList.get(dateString);
							int size = existingEvent.size();
							for (int i = 0; i < size; i++) {
								int compare = ev.compareTo(existingEvent.get(i));
								if (compare > 0)
									existingEvent.add(ev);
								else if (compare < 0) {
									existingEvent.add(i, ev);
									size--;
								} else {
									System.out.println("Conflicting Times. Cannot add.");
									//JOptionPane.showMessageDialog(, "Eggs are not supposed to be green.");
									JOptionPane.showMessageDialog(null, "Conflicting Times. Cannot add.", "OOPS", JOptionPane.ERROR_MESSAGE);
									
								}
							}
							eventsList.put(dateString, existingEvent);
							calModel.update(dateString, existingEvent);
							// frame.dispose();

						} else {
							ArrayList<Event> newEvent = new ArrayList<>();
							newEvent.add(ev);
							eventsList.put(dateString, newEvent);
							System.out.println("" + w + ", " + m + ", " + d + ", " + y + ", " + st + ", " + et);
							ActionListener a = new ActionListener() {
								public void actionPerformed(ActionEvent event) {
									calModel.update(dateString, newEvent);
								}
							};
							System.out.println("done");
						}
					}
				}
			}
		};

		// ADD EVENTS TO EVENT TEXTAREA
		ArrayList<String> strings = getEvent(currentDate);
		//System.out.println(currentDate);
		String list = "";
		for (String s : strings) {
			list = list + "\n" + s;
			text.setText(list);
		}

		quit.addActionListener(a);
		create.addActionListener(c);

		top.add(create);
		top.add(left);
		top.add(right);
		top.add(quit);

		rightPanel.add(text);

		add(top, BorderLayout.NORTH);
		add(calendarPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}


	public JPanel drawCalendar() {
		// GUI CALENDAR
		MONTHS[] arrayOfMonths = MONTHS.values();
		DAYS[] days = DAYS.values();
		
		
		int curMonth = c.get(Calendar.MONTH)+1;
		int curYear = c.get(Calendar.YEAR);
		String header = arrayOfMonths[curMonth-1] + "   " + curYear;
		System.out.println("curMonth: "+curMonth+", curYear"+curYear);
		
		JPanel calPanel = new JPanel();
		calPanel.setLayout(new BorderLayout());
		JLabel month = new JLabel(header);
		DefaultTableModel tableModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableModel.setRowCount(7);
		tableModel.setColumnCount(7);
		JTable daysTable = new JTable(tableModel);
		TableColumn column = null;
		for (int i = 0; i < days.length; i++) {
			tableModel.setValueAt(days[i], 0, i);
			column = daysTable.getColumnModel().getColumn(i);
			column.setPreferredWidth(50);
		}

		daysTable.setRowSelectionAllowed(true);
		daysTable.setColumnSelectionAllowed(true);

		ArrayList<String> daysOfMonth = new ArrayList<>();

		int currentj = 0;
		int currentk = 0;
		int rows = 6;
		int columns = 7;
		int count = 0;
		String blank = "";

		boolean f = true;
		c.set(Calendar.DAY_OF_MONTH, 1);
		int firstDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		int current = 1;
		for (int i = 1; i <= rows; i++) {
			for (int j = 1; j <= columns; j++) {
				if (i == 1 && f == true) {

					if (firstDayOfWeek == 1) {
						count++;
						f = false;
					}
					// blank until first day of week (mon, tue, etc) is reached
					else if (j < firstDayOfWeek) {
						blank += " ";
						daysOfMonth.add(blank);
						count++;
					}
				} else {
					if (count % 7 == 0) {
					}
					int tempMonth = curMonth + 1;
					String monthSt = String.valueOf(tempMonth);
					String dateSt = String.valueOf(current);
					if (monthSt.length() != 2) {
						monthSt = "0" + monthSt;
					}
					if (dateSt.length() != 2) {
						dateSt = "0" + dateSt;
					}
					//String aDay = monthSt + "/" + dateSt + "/" + curYear;
					// insert rest of days of month
					if (current <= daysInMonth) {
						actualDay = Integer.parseInt(currentDate.substring(3, 5));
						if (current == actualDay) {
							System.out.println("actualDay: " +actualDay);
							daysOfMonth.add(current+ "  ");
						} else
							daysOfMonth.add(current + "");
					}
					current++;
					count++;
					if (f == false) {
						count--;
					}
					f = true;
				}
			}
		}
		int i = 0;
		for (int j = 1; j < 6; j++) { // rows
			for (int k = 0; k < 7; k++) { // columns
				tableModel.setValueAt(daysOfMonth.get(i), j, k);
				if (daysOfMonth.get(i).endsWith("  ")) {
					currentj = j;
					currentk = k;
				}
				i++;
				if (i > daysOfMonth.size() - 1)
					break;
			}
		}
		daysTable.setRowSelectionInterval(currentj, currentj);
		daysTable.setColumnSelectionInterval(currentk, currentk);

		daysTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JTable target = (JTable) e.getSource();
				int row = target.getSelectedRow();
				int column = target.getSelectedColumn();
				String cMonth = String.valueOf(curMonth);
				String clickedCell = (String) daysTable.getModel().getValueAt(row, column);
				if (cMonth.length() != 2) {
					cMonth = "0" + cMonth;
				}
				if (clickedCell.length() != 2) {
					if (clickedCell.length() < 2)
					clickedCell = "0" + clickedCell;
				}
				if (clickedCell.endsWith(" ")) {
					clickedCell = clickedCell.substring(0, 2);
				}
				currentDate = cMonth + "/" + clickedCell + "/" + curYear;
				daysTable.setRowSelectionInterval(row, row);
				daysTable.setColumnSelectionInterval(column, column);
				System.out.println("CURRENT DATE: " + currentDate);
				draw();
			}
		});

		calPanel.add(month, BorderLayout.NORTH);
		calPanel.add(daysTable);
		return calPanel;

	}

	public void addEvent() {
		JPanel eventPanel = new JPanel();
		eventPanel.setLayout(new BorderLayout());
		JTextArea text = new JTextArea();
		text.setEditable(false);
		eventPanel.add(text);
	}


	/**
	 * displays day view of calendar and events, corresponding to day can
	 * display either previous or next day
	 * 
	 * @param movCal
	 *            Calendar to keep track of time (months, days) because of user
	 *            input
	 * @param v
	 *            either "p" for previous or "n" for next
	 */
	public void viewByDay(String date, String v) {
		Calendar movCal = c;
		int changeMonth = 0;
		String year = date.substring(6, 10);
		String day = date.substring(3, 5);
		String month = date.substring(0, 2);
		int y = Integer.parseInt(year);
		int d = Integer.parseInt(day);
		int m = Integer.parseInt(month) - 1;
		movCal.set(y, m, d);
		//System.out.println("year: " + y+", month: "+ m +", day: "+d);
		if (v.equals("p")) {
			movCal.add(Calendar.DAY_OF_WEEK, -1);
			changeMonth = -1;
		}
		if (v.equals("n")) {
			movCal.add(Calendar.DAY_OF_WEEK, 1);
			changeMonth =1;
		}
		d = movCal.get(Calendar.DAY_OF_WEEK);
		int newMonth = movCal.get(Calendar.MONTH);
		y = movCal.get(Calendar.YEAR);
		int date1 = movCal.get(Calendar.DATE);
		//System.out.println("date: " + date1);
		newMonth = newMonth + 1;
		String monthSt = String.valueOf(newMonth);
		String dateSt = String.valueOf(date1);
		if (monthSt.length() < 2) {
			monthSt = "0" + monthSt;
		}
		if (dateSt.length() < 2) {
			dateSt = "0" + dateSt;
		}
		if (month.compareTo(monthSt) != 0){
			System.out.println("months changed");
			//c.add(Calendar.MONTH, -1);
			//System.out.println("new Mpnthe: " + c.get(Calendar.MONTH));
			draw();
		}
		else{
			drawCalendar();
		}
		currentDate = monthSt + "/" + dateSt + "/" + year;
		System.out.println("viewbyday_currentDate: " + currentDate);
	}

	/**
	 * get the specified event in ArrayList<String> format
	 * 
	 * @return the specified event, with date and time
	 */
	public ArrayList<String> getEvent(String date) { // display event
		ArrayList<String> outputs = new ArrayList<>();
		String output = "";
		String year = date.substring(6, 10);
		String day = date.substring(3, 5);
		String month = date.substring(0, 2);
		int y = Integer.parseInt(year);
		int d = Integer.parseInt(day);
		int m = Integer.parseInt(month) - 1;
		Calendar newCal = new GregorianCalendar(y, m, d);
		MONTHS[] arrayOfMonths = MONTHS.values();
		DAYS[] arrayOfDays = DAYS.values();
		DAYS dayOfWeek = arrayOfDays[newCal.get(Calendar.DAY_OF_WEEK) - 1];
		MONTHS monthOfYear = arrayOfMonths[m];
		//System.out.println(year + ", " + day + ", " + month + ", ints: " + y + d + m + "\n" + eventsList.size());

		if (eventsList.containsKey(date)) {
			output += y + "\n";
			ArrayList<Event> niagra = eventsList.get(date);
			for (Event e : niagra) {
				String st = e.getStartString();
				String en = e.getEndString();
				String ti = e.getTitle();
				// output += dayOfWeek + " " + monthOfYear + " " + day + " " +
				// st + en + " " + ti + "\n";
				output = st + en + " " + ti;
				outputs.add(output);
				System.out.println(y + "\n" + dayOfWeek + " " + monthOfYear + " " + day + " " + st + en + " " + ti);
			}
		} else {
			outputs.add("No event(s) on that date");
		}
		return outputs;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// data = calModel.getData();
		eventsList = calModel.getData();
		draw();
	}

}
