package ejik.util.events;

public class Event {
	public String type = "";
	
	public Object sender;
	public boolean active = true;
	
	public Event(String type) {
		this.type = type;
	}

}
