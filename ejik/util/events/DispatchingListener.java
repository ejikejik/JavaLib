package ejik.util.events;

public class DispatchingListener implements IDispatchingListener {
	
	private EventDispatcher dispatcher = null;
	
	public DispatchingListener() {
		this.dispatcher = new EventDispatcher(this);
	}

	@Override
	public void addEventListener(String eventType, IEventListener listener) {
		this.dispatcher.addEventListener(eventType, listener);

	}

	@Override
	public void dispatchEvent(Event event) {
		this.dispatcher.dispatchEvent(event);

	}

	@Override
	public void removeEventListener(String eventType, IEventListener listener) {
		this.dispatcher.removeEventListener(eventType, listener);
	}

	@Override
	public void receiveEvent(Event event) {

	}

}
