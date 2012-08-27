package ejik.util.scheduling;

import ejik.util.events.IEventDispatcher;

public interface IScheduler extends IEventDispatcher {
	public long addTask(IScheduledObject object);
	public boolean removeTask(long taskId);
	public IScheduledObject getTask(long taskId);
	public void stopAll();
}
