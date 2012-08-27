package ejik.util.events;

import java.lang.ref.WeakReference;
import java.util.Vector;

public class EventDispatcher implements IEventDispatcher {
	
	class TypeListenerPair {
		public WeakReference<String> eventType;
		public WeakReference<IEventListener> listener;
		
		public TypeListenerPair(WeakReference<String> eventType, WeakReference<IEventListener> listener) {
			this.eventType = eventType;
			this.listener = listener;
		}
		
	}
	
	public Object sender = null;
	
	Vector<TypeListenerPair> listeners = new Vector<TypeListenerPair>();

	public EventDispatcher() {
		this.sender = null;
	}
	
	public EventDispatcher(Object sender) {
		this.sender = sender;
	}
	
	private Vector<TypeListenerPair> getListeners() {
		Vector<TypeListenerPair> res = new Vector<TypeListenerPair>();
		synchronized (this.listeners) {
				res = (Vector<TypeListenerPair>)this.listeners.clone();
		}
		return res;
	}
	
	public void addEventListener(String type, IEventListener listener) throws IllegalArgumentException {
		if (type.equals("") || listener == null) {
			throw new IllegalArgumentException();
		}
		synchronized (this.listeners) {
			this.listeners.add(new TypeListenerPair(new WeakReference<String>(type), new WeakReference<IEventListener>(listener)));
		}
	}

	public void removeEventListener(String eventType, IEventListener listener) {
		synchronized (this.listeners) {
			for (TypeListenerPair pair: this.listeners) {
				if (pair.listener.get() == listener && pair.eventType.get().equals(eventType)) {
					this.listeners.remove(pair);
					break;
				}
			}
		}
	}

	public void dispatchEvent(Event event) throws IllegalArgumentException {
		if (event == null) {
			throw new IllegalArgumentException();
		}
		Object newSender = null;
		if (this.sender != null) {
			newSender = this.sender;
		} else {
			newSender = this;
		}
		if (event.sender == null) {
			event.sender = newSender;
		}
		IEventListener listener = null;
		Vector<TypeListenerPair> cListeners = this.getListeners();
		for (TypeListenerPair pair: cListeners) {
			if (event.active) {
				listener = pair.listener.get(); 
				if (listener != null) {
					if (pair.eventType.get().equals(event.type)) {
						//synchronized (listener) {
							listener.receiveEvent(event);
						//}
					}
				} else {
					// TODO: clear old weak references
				}
			} else {
				break;
			}
		}
	}

}
