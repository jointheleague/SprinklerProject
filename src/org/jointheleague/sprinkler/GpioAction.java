package org.jointheleague.sprinkler;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class GpioAction implements Comparable<GpioAction>{

	private final Set<Integer> headsOn;
	private final Set<Integer> headsOff;
	private final Calendar time;
	/**
	 * Constructor
	 * @param headsOn an int array of the indices of the heads to turn on
	 * @param headsOff an int array of the indices of the heads to turn off
	 * @param time the time of the action
	 */
	public GpioAction(Set<Integer> headsOn, Set<Integer> headsOff, Calendar time) {
		this.headsOn = headsOn;
		this.headsOff = headsOff;
		this.time = time;
	}

	@Override
	public int compareTo(GpioAction o) {
		return time.compareTo(o.time);
	}
 
	/**
	 * Get the array of indices of the heads to turn on.
	 * @return the set of heads to turn on.
	 */
	public Set<Integer> getHeadsOn() {
		return headsOn;
	}
	/**
	 * Get the array of indices of the heads to turn off.
	 * @return the set of heads to turn off.
	 */
	public Set<Integer> getHeadsOff() {
		return headsOff;
	}
	/**
	 * Get the time of the action
	 * @return the time of the action
	 */
	public long getTimeOfAction() {
		return time.getTimeInMillis();
	}
	
	/**
	 * Adds seven days to the time of this action.
	 */
	public void addWeek() {
		time.add(Calendar.DAY_OF_MONTH, 7);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[off = [");
		boolean first = true;
		for(Integer i: headsOff) {
			if(!first){
				sb.append(", ");
			} else {
				first = false;
			}
			sb.append(i.toString());
		}
		sb.append("], on = [");
		first = true;
		for(Integer i: headsOn) {
			if(!first){
				sb.append(", ");
			} else {
				first = false;
			}
			sb.append(i.toString());
		}
		sb.append("], time = ");
		sb.append(new Date(getTimeOfAction()));
		sb.append("]");
		return sb.toString();
		
	}
}
