import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CalendarModel {
	//ArrayList<Double> data;
	TreeMap<String, ArrayList<Event>> data;
	//ArrayList<Event> data;
	ArrayList<ChangeListener> listeners;

	/**
	 * Constructs a DataModel object
	 * 
	 * @param d
	 *            the data to model
	 */
	public CalendarModel(TreeMap<String, ArrayList<Event>> d) {
		data = d;
		listeners = new ArrayList<ChangeListener>();
	}

	
	/**
	 * Constructs a DataModel object
	 * 
	 * @return the data in an ArrayList
	 */
	public TreeMap<String, ArrayList<Event>> getData() {

		return (TreeMap<String, ArrayList<Event>>) (data.clone());
	}

	/**
	 * Attach a listener to the Model
	 * 
	 * @param c
	 *            the listener
	 */
	public void attach(ChangeListener c) {
		listeners.add(c);
	}

	/**
	 * Change the data in the model at a particular location
	 * 
	 * @param location
	 *            the index of the field to change
	 * @param value
	 *            the new value
	 */
	public void update(String date, ArrayList<Event> value) {
		//data.set(date, value);
		data.put(date, value);
		System.out.println("size of lsiteners: " + listeners.size());

	      for (ChangeListener l : listeners)
	      {
	         l.stateChanged(new ChangeEvent(this));
	      }
	}

}
