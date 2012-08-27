package ejik.util.scheduling;

import java.lang.ref.WeakReference;

import ejik.util.events.Event;
import ejik.util.events.EventDispatcher;
import ejik.util.events.IEventListener;
import ejik.util.scheduling.event.ScheduledObjectEvent;

public class ScheduledObject implements IScheduledObject {

	private long id = 0;
	private boolean ready = false;
	private boolean isInterval = false;
	private long startTime = 0;
	private long destinationTime = 0;
	private long timeout = 0;
	private long triggerCount = 0;
	private Object data = null;
	
	private boolean paused = false;
	private long timeAfterPause = 0;
	private long oldTimeout = 0;
	
	private EventDispatcher internalDispatcher = new EventDispatcher(this);
	
	private WeakReference<IScheduler> scheduler = new WeakReference<IScheduler>(null);
	
	
	private String type = "";
	
	public ScheduledObject(String type, long timeout) {
		this.setTimeout(timeout);
		this.type = type;
	}
	
	public ScheduledObject(String type, long timeout, boolean isInterval) {
		this(type, timeout);
		this.setIsInterval(isInterval);
	}
	
	public String getType() {
		return this.type;
	}
	
	@Override
	public long getId() {
		return this.id;
	}

	@Override
	public void initTimer() {
		this.startTime = System.currentTimeMillis();
		this.setDestination();
		this.ready = false;
	}

	@Override
	public boolean isReady() {
		return this.ready;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public void tick() {
		if (this.paused) {
			return;
		}
		long currentTime = System.currentTimeMillis();
		if (currentTime >= this.destinationTime) {
			this.ready = true;
			this.triggerCount++;
		}
	}

	@Override
	public boolean isInterval() {
		return this.isInterval;
	}

	@Override
	public void setIsInterval(boolean value) {
		this.isInterval = value;
	}

	@Override
	public void setTimeout(long timeout) {
		this.timeout = timeout;
		this.initTimer();
	}
	
	private void setDestination() {
		this.destinationTime = this.startTime + this.timeout;
	}

	@Override
	public void setTimeoutSeconds(long timeout) {
		timeout *= 1000;
		this.setTimeout(timeout);
	}

	@Override
	public long getTimeToTrigger() {
		if (paused) {
			return this.timeAfterPause;
		}
		long currentTime = System.currentTimeMillis();
		long res = this.destinationTime - currentTime;
		if (res < 0) res = 0;
		return res;
	}

	@Override
	public IScheduler getScheduler() {
		return this.scheduler.get();
	}

	@Override
	public void setScheduler(IScheduler scheduler) {
		this.scheduler = new WeakReference<IScheduler>(scheduler);
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public void setData(Object o) {
		this.data = o;
	}

	@Override
	public long getTimeout() {
		return this.timeout;
	}

	@Override
	public long getTriggerCount() {
		return this.triggerCount;
	}

	@Override
	public void pause() {
		if (paused) return;
		this.timeAfterPause = this.getTimeToTrigger();
		this.oldTimeout = this.timeout;
		this.dispatchEvent(new ScheduledObjectEvent(ScheduledObjectEvent.PAUSED));
		this.paused = true;
	}
	
	@Override
	public boolean isPaused() {
		return this.paused;
	}

	@Override
	public void unpause() {
		if (!paused) return;
		this.paused = false;
		this.setTimeout(this.timeAfterPause);
		this.timeAfterPause = 0;
		this.timeout = this.oldTimeout;
		this.dispatchEvent(new ScheduledObjectEvent(ScheduledObjectEvent.UNPAUSED));
	}

	@Override
	public void addEventListener(String eventType, IEventListener listener) {
		this.internalDispatcher.addEventListener(eventType, listener);
	}

	@Override
	public void dispatchEvent(Event event) {
		this.internalDispatcher.dispatchEvent(event);
	}

	@Override
	public void removeEventListener(String eventType, IEventListener listener) {
		this.internalDispatcher.removeEventListener(eventType, listener);
	}

	@Override
	public long getElapsedTime() {
		long res = this.getTimeout() - this.getTimeToTrigger();
		if (res < 0) res = 0;
		return res;
	}

}

