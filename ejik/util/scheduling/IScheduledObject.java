package ejik.util.scheduling;

import ejik.util.events.IEventDispatcher;

public interface IScheduledObject extends IEventDispatcher {
	
	public void setId(long id);
	public long getId();
	
	public void initTimer();
	
	public void tick();
	
	public void setScheduler(IScheduler scheduler);
	public IScheduler getScheduler();
	
	public boolean isReady();
	
	public boolean isInterval();
	
	public void setIsInterval(boolean value);
	public void setTimeout(long timeout);
	public long getTimeout();
	public void setTimeoutSeconds(long timeout);
	
	public long getTimeToTrigger();
	public long getElapsedTime();
	
	public long getTriggerCount();
	
	public String getType();
	
	public void setData(Object o);
	public Object getData();
	
	public void pause();
	public boolean isPaused();
	public void unpause();

}
