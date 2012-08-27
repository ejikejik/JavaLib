package ejik.util.scheduling.event;

import ejik.util.events.Event;

public class ScheduledObjectEvent extends Event {
	
	public static final String PAUSED = "paused";
	public static final String UNPAUSED = "unpaused";

	public ScheduledObjectEvent(String type) {
		super(type);
	}

}
