package ejik.util.events;

public interface IEventDispatcher {
	
	public void addEventListener(String eventType, IEventListener listener);
	public void removeEventListener(String eventType, IEventListener listener);
	public void dispatchEvent(Event event);
	public boolean hasEventListener(String eventType);

}
