package ejik.util.scheduling.event;

import ejik.util.events.Event;
import ejik.util.scheduling.IScheduledObject;

public class ScheduledEvent extends Event {
	
	public static final String SCHEDULE_TRIGGERED = "scheduleTriggered";
	
	public IScheduledObject object = null;

	public ScheduledEvent(IScheduledObject object) {
		super(SCHEDULE_TRIGGERED);
		this.object = object;
	}

}
