package ejik.util.scheduling;

import java.util.Vector;


import ejik.util.events.Event;
import ejik.util.events.EventDispatcher;
import ejik.util.events.IEventListener;
import ejik.util.scheduling.event.ScheduledEvent;
import ejik.util.scheduling.event.ScheduledObjectEvent;
import ejik.util.uid.UIDGenerator;

public class Scheduler extends EventDispatcher implements IScheduler, IEventListener, Runnable {

	/**
	 * unique id for task counter
	 */
	private static long lastId = 1;
	
	/**
	 * schedules container. lazy creation
	 */
	private Vector<IScheduledObject> schedules = null;
	
	/**
	 * minimal time to wait before check
	 */
	private long waitTime = Long.MAX_VALUE;
	
	/**
	 * @return
	 */
	private Vector<IScheduledObject> getSchedules() {
		if (schedules == null) {
			schedules = new Vector<IScheduledObject>();
		}
		return schedules;
	}
	
	/**
	 * @return
	 */
	private Vector<IScheduledObject> getSchedulesClone() {
		Vector<IScheduledObject> res = this.getSchedules();
		res = (Vector<IScheduledObject>)res.clone();
		return res;
	}
	
	private Thread thread = null;
	
	public Scheduler() {
		this.init();
	}
	
	public Scheduler(IScheduledObject object) {
		this();
		if (object != null) {
			this.addTask(object);
		}
	}
	
	private void init() {
		Thread thread = new Thread(this, "Scheduler_" +UIDGenerator.getUid(4) + " by " + Thread.currentThread().getName());
		this.thread = thread;
		thread.setDaemon(true);
		//Tracer.trace("Init start");
		this.started = true;
		thread.start();
	}
	
	private volatile boolean started = false;
	
	/**
	 * implements Runnable
	 */
	@Override
	public void run() {
		try {
			started = true;
			while (started) {
				//System.out.println("0");
				workingCycle();
				//System.out.println("10");
			}
			//Tracer.trace("Cycle over");
		} catch (Exception e) {
			if (!(e instanceof InterruptedException)) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * main cycle which determines if some event triggered 
	 */
	private void workingCycle() throws Exception {
		Vector<IScheduledObject> scheduleList = this.getSchedulesClone();
		//System.out.println("1");
		//Tracer.trace("Working cycle");
		synchronized (scheduleList) {
			//System.out.println("2");
			for (IScheduledObject object: scheduleList) {
				object.tick();
				//System.out.println("3");
				if (object.isReady()) {
					//System.out.println("4");
					if (!object.getType().equals("everySecond")) {
						System.out.println("triggered timer id=" + object.getId() + " (" + object.getType() + ")");
					}
					this.dispatchEvent(new ScheduledEvent(object));
					if (!object.isInterval()) {
						//System.out.println("4.5");
						this.removeTask(object.getId());
						// structure of list is changed, prefer to start cycle again
						return;
					} else {
						object.initTimer();
					}
				}
			}
			this.waitTime = this.getMinimalWaitTime();
		}
		//System.out.println("Wait time: " + this.waitTime);
		if (this.waitTime != 0) {
			synchronized (this) {
				this.wait(this.waitTime);
				//Tracer.trace("Waiting " + this.waitTime);
			}
		} else {
			started = false;
			//Tracer.trace("Stopped 127");
		}
		
	}
	
	/**
	 * add task to scheduler
	 */
	@Override
	public long addTask(IScheduledObject object) {
		long id = lastId++;
		Vector<IScheduledObject> scheduleList = this.getSchedules();
		object.setId(id);
		object.addEventListener(ScheduledObjectEvent.PAUSED, this);
		object.addEventListener(ScheduledObjectEvent.UNPAUSED, this);
		System.out.println("Added timer " + id + " (" + object.getType() + ", " + object.getTimeout() + ")");
		synchronized (scheduleList) {
			scheduleList.add(object);
			object.setScheduler(this);
			object.initTimer();
		}
		synchronized (this) {
			this.notify();
		}
		this.waitTime = this.getMinimalWaitTime();
		if (!this.started) {
			
			try {
				this.init();
				this.started = true;
				//Tracer.trace("Starting 155");
			} catch (Exception e) {
				//Tracer.trace("Exception");
			}
			
			
		}
		return id;
	}
	
	private long getMinimalWaitTime() {
		long res = Long.MAX_VALUE;
		Vector<IScheduledObject> scheduleList = this.getSchedulesClone();
		if (scheduleList.size() == 0) {
			return 0;
		}
		res = scheduleList.get(0).getTimeToTrigger();
		long time = 0;
		boolean found = false;
		synchronized (scheduleList) {
			for (IScheduledObject object: scheduleList) {
				if (object.isPaused()) {
					continue;
				}
				found = true;
				time = object.getTimeToTrigger();
				if (time < res) {
					res = time;
				}
			}
		}
		if (!found) {
			return 0;
		}
		if (res < 1) res = 1;
		return res;
	}

	@Override
	public IScheduledObject getTask(long taskId) {
		Vector<IScheduledObject> scheduleList = this.getSchedules();
		synchronized (scheduleList) {
			for (IScheduledObject object: scheduleList) {
				if (object.getId() == taskId){
					return object;
				}
			}
		}
		return null;
	}

	@Override
	public boolean removeTask(long taskId) {
		Vector<IScheduledObject> scheduleList = this.getSchedules();
		synchronized (scheduleList) {
			for (IScheduledObject object: scheduleList) {
				if (object.getId() == taskId){
					System.out.println("Removing task " + taskId + " (" + object.getType() + ")");
					scheduleList.remove(object);
					object.removeEventListener(ScheduledObjectEvent.PAUSED, this);
					object.removeEventListener(ScheduledObjectEvent.UNPAUSED, this);
					synchronized (this) {
						this.notify();
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void receiveEvent(Event event) {
		if (event.type.equals(ScheduledObjectEvent.PAUSED)) {
			
		}
		if (event.type.equals(ScheduledObjectEvent.UNPAUSED)) {
			synchronized (this) {
				this.notify();
			}
			return;
		}
	}

	@Override
	public void stopAll() {
		if (this.thread != null) {
			if (!this.thread.isInterrupted()) {
				synchronized (this) {
					this.notifyAll();
					this.thread.interrupt();
				}
			}
		}
	}

	public void pauseAll() {
		Vector<IScheduledObject> scheduleList = this.getSchedules();
		synchronized (scheduleList) {
			for (IScheduledObject object: scheduleList) {
				object.pause();
			}
		}
		
	}
	
	public void unpauseAll() {
		Vector<IScheduledObject> scheduleList = this.getSchedules();
		synchronized (scheduleList) {
			for (IScheduledObject object: scheduleList) {
				object.unpause();
			}
		}
		
	}

}
