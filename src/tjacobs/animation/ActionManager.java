package tjacobs.animation;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.AbstractAction;
import javax.swing.Action;

import tjacobs.animation.CollectionUtils;
import tjacobs.animation.PrimaryKey;
/** ActionManager is a class for managing shared resources
 * <p>
 * The different parts are:<ul>
 * <li>ActionManager: High level singleton class that manages registered watchedResources and ManagedActions that use those resources</li>
 * <li>ManagedAction: A subclass of AbstractAction useful for specifying what resources need to be initialized and free for it to run successfully </li>
 * <li>IDedResource: A resource with a declared ID so that it can be captured by a ManagedAction. It is recommended but not required that the ID be of String type
 * <li>DependencyCheckFailed: A resource doesn't exist, wasn't free or wasn't freed in a timely manner
 * </ul>
 * 
 * @deprecated this class has been moved for closer alignment / package reuse
 * @see tjacobs.thread.ActionManager
 * @author tjacobs
 *
 */
public class ActionManager {

	private static ActionManager singleton;
	Map <ManagedAction, actionStatus> availableActions;
	@SuppressWarnings("unchecked")
	Map <Object, IDedResource> watchedResources;
	@SuppressWarnings("unchecked")
	Map<IDedResource, ManagedAction> initializingAction;
	/**
	 * Timeout setting for waiting for a resource to be created / freed
	 */
	public static final long NEVER_TIMEOUT = -1;
	/**
	 * Timeout setting for waiting for a resource to be created / freed
	 */
	public static final long DO_NOT_WAIT = 1;
	/**
	 * Timeout setting for waiting for a resource to be created / freed
	 */
	public static final long DefaultActionWaitTime = 10 * 1000; // 10 seconds
		
	/**
	 * Status of a ManagedAction
	 */
	public enum actionStatus {
		NEVER_RUN, RUNNING, RUN_SUCCEED, RUN_FAIL;
	}
	
	/**
	 * Use status of a resource
	 */
	public enum resourceStatus {
		UNINITIALIZED, AVAILABLE, InUse
	}

	/**
	 * Thrown if the required resources cannot be obtained or created
	 * @author tom
	 *
	 */
	public static class DependencyCheckFailed extends RuntimeException {
		private static final long serialVersionUID = 1L;

		private Object dependency;
		
		public DependencyCheckFailed(Object dependency) {
			super("Failed to initialize: " + dependency);
			this.dependency = dependency; 
		}
		
		public Object getDependency() {
			return dependency;
		}
	}
	
	/**
	 * A watched resource identified by an ID.
	 * @author tom
	 *
	 * @param <ID_TYPE> the type of the ID
	 * @param <VALUE_TYPE> the type of the Resource
	 * @see PrimaryKey
	 */
	
	public static class IDedResource<ID_TYPE, VALUE_TYPE> extends AtomicReference<VALUE_TYPE> {
		private static final long serialVersionUID = 1L;
		ID_TYPE name;
		VALUE_TYPE value;
		resourceStatus status;
		
		public IDedResource(ID_TYPE name) {
			this.name = name;
			status = resourceStatus.UNINITIALIZED;
		}
		
		public IDedResource(ID_TYPE name, VALUE_TYPE val) {
			this.name = name;
			value = val;
			status = resourceStatus.AVAILABLE;
		}
		
		/**
		 * initializes the value of this resource.
		 * Will wake up 1 thread waiting on this resource
		 * @param value
		 */
		public void initialize(VALUE_TYPE value) {
			super.set(value);
			this.value = value;
			status = resourceStatus.AVAILABLE;
			synchronized(this) {
				notify();
			}
		}
		
//		public Object[] getPrimaryKey() {
//			return new Object[] {name};
//		}
		
		@SuppressWarnings("unchecked")
		public boolean equals(Object o) {
			if (! (o instanceof IDedResource)) {
				return false;
			}
			return getID().equals(((IDedResource)o).getID());
			//return PrimaryKey.Impl.equalsImpl(this, o);
			
		}
		
		public int hashCode() {
			return getID().hashCode();
			//PrimaryKey.Impl.hashCode(this);
		}
		
		public ID_TYPE getID() {
			return name;
		}
				
		/**
		 * get the resource status
		 * @see resourceStatus
		 * @return
		 */
		public resourceStatus getStatus() {
			return status;
		}
		
		/**
		 * release the resource
		 * this will wake up 1 thread waiting on this resource
		 */
		public synchronized void release() {
			notify();
		}
		
		public String toString() {
			return "Resource: " + getID();
		}
	}
	
	/**
	 * ManagedAction is a facade on an Action 
	 * 
	 * The most critical aspect of ManagedAction is that it contains a list of its dependencies - resources that it
	 * needs in order to complete successfully. 
	 * 
	 * When run, resources will be acquired one by one, then the operation is processed, and finally the resources
	 * are released
	 * 
	 * ManagedAction has a name field so that it can be invoked by name from the ActionManager (if registered)
	 * @author tom
	 *
	 */
	public static class ManagedAction implements Action {

		private static final boolean DEBUG = false;
		@SuppressWarnings("unchecked")
		private static ThreadLocal<Set<IDedResource>> threadLocks;

		private ManagedAction initializer;
		Action main;
		List<Object> dependencies;
		@SuppressWarnings("unchecked")
		Set<IDedResource> heldResources;
		String name;
		private long waitTime = DefaultActionWaitTime;
		private boolean runInitializersIfAvailable = true;		
		
		@SuppressWarnings("unchecked")
		public static boolean hasLock(IDedResource res) {
			if (threadLocks == null) return false;
			return threadLocks.get().contains(res);	
		}
		
		@SuppressWarnings("unchecked")
		public static void addLock(IDedResource res) {
			if (threadLocks == null) {
				threadLocks = new ThreadLocal<Set<IDedResource>>();
			}
			if (threadLocks.get() == null) {
				threadLocks.set(new HashSet<IDedResource>());
			}
			threadLocks.get().add(res);
		}
		
		@SuppressWarnings("unchecked")
		public static boolean removeLock(IDedResource res) {
			if (threadLocks == null) return false;
			return threadLocks.get().remove(res);
		}
		
		public ManagedAction(Action main, String name) {
			this.main = main;
			this.name = name;
		}

		public ManagedAction(Action main, String name, Object... dependencies) {
			this(main, name);
			addDependencies(dependencies);
		}

		public String getName() {
			return name;
		}

		/**
		 * @return time in milliseconds to wait for a required resource to be created / become available
		 */
		public long getWaitTime() {
			return waitTime;
		}

		/**
		 * @param waitTime time in milliseconds to wait for a required resource to be created / become available
		 */
		public void setWaitTime(long waitTime) {
			this.waitTime = waitTime;
		}

		/**
		 * If true, this action will try invoking any registered initializers for resources that are not initialized
		 * @return whether to run the default initializers or just fail
		 */
		public boolean isRunInitializersIfAvailable() {
			return runInitializersIfAvailable;
		}

		/**
		 * If true, this action will try invoking any registered initializers for resources that are not initialized
		 * @param runInitializersIfAvailable whether to run the default initializers or just fail
		 */
		public void setRunInitializersIfAvailable(boolean runInitializersIfAvailable) {
			this.runInitializersIfAvailable = runInitializersIfAvailable;
		}
		
		@SuppressWarnings("unchecked")
		public void addDependencies(Object... res) {
			if (this.dependencies == null) this.dependencies = new ArrayList<Object>(res.length + 5);
			if (heldResources == null) heldResources = new HashSet<IDedResource>(res.length + 5);
			ActionManager manager = ActionManager.getActionManager();
			for (Object dep : res) {
				this.dependencies.add(dep);
				if (manager.getWatchedResource(dep) == null) {
					manager.addWatchedResource(dep);
				}
			}			
		}

		/**
		 * facade for the underlying action
		 */
		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			main.addPropertyChangeListener(listener);
		}

		/**
		 * facade for the underlying action
		 */
		@Override
		public Object getValue(String key) {
			return main.getValue(key);
		}

		/**
		 * facade for the underlying action
		 */
		@Override
		public boolean isEnabled() {
			return main.isEnabled();
		}
		
		/**
		 * facade for the underlying action
		 */
		@Override
		public void putValue(String key, Object value) {
			main.putValue(key, value);
		}
		
		/**
		 * facade for the underlying action
		 */
		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			main.removePropertyChangeListener(listener);
		}
		
		/**
		 * facade for the underlying action
		 */
		@Override
		public void setEnabled(boolean b) {
			main.setEnabled(b);
		}

//		public void run() {
//			initializer.actionPerformed(null);											
//		}
		
		@SuppressWarnings("unchecked")
		public void actionPerformed(final ActionEvent e) {
			if (DEBUG)
				System.out.println("run: " + getName());
			ActionManager man = ActionManager.getActionManager();
			if (dependencies != null) {
				for (Object nm : dependencies) {
					IDedResource nr = man.getWatchedResource(nm);
					//will throw a NPE if the resource somehow isn't being watched
					if (hasLock(nr)) continue;
					synchronized(nr) {
						if (DEBUG)
							System.out.println(nr.getStatus());
						if (nr.getStatus() != resourceStatus.AVAILABLE) {
							if (nr.getStatus() == resourceStatus.UNINITIALIZED && isRunInitializersIfAvailable()) { 
								//no one else is loading it right now and we should try to load ourselves
								if (DEBUG)
									System.out.println("Initialize");
								nr.status = resourceStatus.InUse;
								initializer = man.getInitializingAction(nr);
								if (initializer != null) {
									//new Thread(this).start();
									initializer.actionPerformed(null);
								}
								//hopefully, the resource should be available now.
								if (nr.getStatus() == resourceStatus.UNINITIALIZED) throw new DependencyCheckFailed("Could not initialize dependency: " + nr.getID());
							}
							if (nr.getStatus() != resourceStatus.AVAILABLE) {
								if (getWaitTime() == ActionManager.DO_NOT_WAIT) {
									man.fail(this);
									return;
								}
								if (DEBUG)
									System.out.println("Wait");
								try {
									if (getWaitTime() == ActionManager.NEVER_TIMEOUT) {
										nr.wait();
										// it is unlikely for the below condition to occur
										if (nr.getStatus() != resourceStatus.AVAILABLE) {
											man.fail(this);
											return;
										}
									}
									else {
										nr.wait(getWaitTime());
										if (nr.getStatus() != resourceStatus.AVAILABLE) {
											man.fail(this);
											return;
										}
									}
									heldResources.add(nr);
									addLock(nr);
								}
								catch (InterruptedException ex) {
									man.fail(this);
									throw new DependencyCheckFailed(nm);
								}
							}
							else {
								heldResources.add(nr);
								addLock(nr);
							}
						}
						else {
							heldResources.add(nr);
							addLock(nr);
						}
					}
				}
			}
			try {
				man.running(this);
				main.actionPerformed(e);
				man.succeed(this);
			}
			catch (RuntimeException ex) {
				man.fail(this);
				throw ex;
			}
			catch (Error ex) {
				man.fail(this);
				throw ex;
			}
		}
		

		@SuppressWarnings("unchecked")
		Set<IDedResource> getHeldResources() {
			return heldResources;
		}
		
		@SuppressWarnings("unchecked")
		void clearHeldResources() {
			for (IDedResource nr : heldResources) {
				removeLock(nr);
			}
			heldResources.clear();
		}

		public String toString() {
			return "Managed Action: " + getName();
		}
		
	}
	
	/**
	 * called at the start of a ManagedAction to tell the ActionManager that the action has started
	 * @param action
	 */
	@SuppressWarnings("unchecked")
	public void running(ManagedAction action) {
		availableActions.put(action, actionStatus.RUNNING);
		Set<IDedResource> res = action.getHeldResources();
		if (res == null) return;
		for (IDedResource nr : res) {
			//NamedResource nr = getWatchedResource(s);
			nr.status = resourceStatus.InUse;
		}
	}
	
	/**
	 * Called by ManagedAction when the action failed. This can mean either it couldn't acquire the necessary resources
	 * or that the underlying action threw a Throwable
	 * @param action
	 */
	@SuppressWarnings("unchecked")
	public void fail(ManagedAction action) {
		availableActions.put(action, actionStatus.RUN_FAIL);
		Set<IDedResource> res = action.getHeldResources();
		if (res == null) return;
		for (IDedResource nr : res) {
			synchronized(nr) {
				nr.status = nr.value != null ? resourceStatus.AVAILABLE : resourceStatus.UNINITIALIZED;
				nr.notify();
			}
		}
		action.clearHeldResources();
	}
	
	/**
	 * Called by ManagedAction after all resource dependencies have been acquired and the underlying action
	 * has been successfully executed.
	 * @param action
	 */
	@SuppressWarnings("unchecked")
	public void succeed(ManagedAction action) {
		availableActions.put(action, actionStatus.RUN_SUCCEED);		
		Set<IDedResource> res = action.getHeldResources();
		if (res == null) return;
		for (IDedResource nr : res) {
			synchronized(nr) {
				nr.status = nr.value != null ? resourceStatus.AVAILABLE : resourceStatus.UNINITIALIZED;
				nr.notify();
			}
		}
		action.clearHeldResources();
	}
	
	/**
	 * Get a registered NameResource by its name
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IDedResource getWatchedResource(Object name) {
		return watchedResources.get(name); 
	}
	
//	@SuppressWarnings("unchecked")
//	public void initializeWatchedSource(String name, Object val) {
//		getWatchedResource(name).initialize(val);
//	}
	
	/**
	 * Waits indefinitely for a WatchedResource to be created or freed
	 * @throws NullPointException if parameter is not the name of a watched resource
	 * @param nr
	 */
	public void waitForWatchedResource(String nm) throws InterruptedException {
		waitForWatchedResource(getWatchedResource(nm));
	}
	
	/**
	 * Waits indefinitely for a WatchedResource to be created or freed
	 * @throws NullPointException if parameter is null
	 * @param nr
	 */
	@SuppressWarnings("unchecked")
	public void waitForWatchedResource(IDedResource nr) throws InterruptedException {
		if (nr.getStatus() != resourceStatus.AVAILABLE) {
			synchronized(nr) {
				nr.wait();
			}
		}
	}

	/**
	 * Create a new generic NamedResource in the ActionManager
	 * @param name
	 */
	@SuppressWarnings("unchecked")
	public synchronized void addWatchedResource(Object id) {
		if (getWatchedResource(id) == null) return;
		IDedResource nr = new IDedResource(id);
		addWatchedResource(nr);
	}
	
	/**
	 * Add a NamedResource that has already been created to the ActionManager
	 * @param wr
	 */
	@SuppressWarnings("unchecked")
	public synchronized void addWatchedResource(IDedResource wr) {
		if (wr == null) return;
		if (watchedResources == null) watchedResources = new HashMap<Object, IDedResource>();
		watchedResources.put(wr.getID(), wr);
	}

	/**
	 * Remove a namedResource from the ActionManager's list of watched resources
	 * @param s
	 */
	public void removeWatchedResource(Object id) {
		if (watchedResources == null) return;
		watchedResources.put(id, null);
	}
	
	private ActionManager() {}
	
	public static synchronized ActionManager getActionManager() {
		if (singleton == null) {
			singleton = new ActionManager();
		}
		return singleton;
	}

	/**
	 * Gets the initializingAction, if any, for <i>resource</i>
	 * @param resource
	 * @return null or a ManagedAction that initializes resource
	 */
	@SuppressWarnings("unchecked")
	public ManagedAction getInitializingAction(IDedResource resource) {
		if (initializingAction == null) return null;
		return initializingAction.get(resource);
	}
	
	/**
	 * Set the default initializer action on a resource
	 * @param resource
	 * @param action
	 */
	@SuppressWarnings("unchecked")
	public synchronized void setIntializingAction(IDedResource resource, ManagedAction action) {
		if (initializingAction == null) initializingAction = new HashMap<IDedResource, ManagedAction>();
		initializingAction.put(resource, action);
	}

	/**
	 * Run the ManagedAction with the name <i>actionName</i>
	 * @param actionName
	 */
	public void runNamedAction(String actionName) {
		runNamedAction(actionName, null);
	}
	
	/**
	 * Run the ManagedAction with the name <i>actionName</i>
	 * @param actionName
	 */
	public void runNamedAction(String actionName, ActionEvent ae) {
		Iterator<ManagedAction> _it = availableActions.keySet().iterator();
		while (_it.hasNext()) {
			ManagedAction act = _it.next();
			if (act.name.equals(actionName)) {
				act.actionPerformed(ae);
				break;
			}
		}
	}

	/**
	 * Run the ManagedAction with the name <i>actionName</i>
	 * Wait if the required resources cannot be obtained
	 * @param actionName
	 * @deprecated
	 */
	public void runNamedActionWaitForResources(String actionName) {
		runNamedActionWaitForResources(actionName, null);
	}
	
	/**
	 * Run the ManagedAction with the name <i>actionName</i>
	 * Wait if the required resources cannot be obtained
	 * @param actionName
	 * @deprecated
	 */
	public void runNamedActionWaitForResources(String actionName, ActionEvent ae) {
		runNamedActionWaitForResources(actionName, ae, DefaultActionWaitTime);
	}
	
	/**
	 * Run the ManagedAction with the name <i>actionName</i>
	 * Wait if the required resources cannot be obtained
	 * @param actionName
	 */
	public void runNamedActionWaitForResources(String actionName, ActionEvent ae, long waitTime) {
		Iterator<ManagedAction> _it = availableActions.keySet().iterator();
		while (_it.hasNext()) {
			ManagedAction act = _it.next();
			act.setWaitTime(waitTime);
			if (act.name.equals(actionName)) act.actionPerformed(ae);
		}
	}

	/**
	 * Create a ManagedAction and add it to the list of availableActions
	 * @param action
	 * @param name
	 * @return
	 */
	public synchronized ManagedAction addAvailableAction(Action action, String name) {
		ManagedAction a = action instanceof ManagedAction ? (ManagedAction) action : new ManagedAction(action, name == null ? "Unnamed" : name);
		if (availableActions == null) availableActions = new HashMap<ManagedAction, actionStatus>();
		if (!availableActions.containsKey(a)) {
			availableActions.put(a, actionStatus.NEVER_RUN);
		}
		return a;
	}
	
	public void removeAvailableAction(Action a) {
		availableActions.remove(a);
	}

	/*
	public void useResource(Object o, Action user) throws InterruptedException {
		//watchedResources.put(key, value)
		if (watchedResources == null) {
			synchronized(this) {
				if (watchedResources == null) {
					usedResources = new HashMap<Object, Action>();
				}
			}
		}
		Action usr = usedResources.get(o);
		synchronized(o) {
			while (usr != null) {
				o.wait();
			}
		}
		usedResources.put(o, usr);
	}
	
	public void freeResource(Object obj) {
		synchronized(obj) {
			usedResources.remove(obj);
			obj.notify();
		}
	}
	*/
	
	
	/*
	 * Just for testing
	 */
	@SuppressWarnings({ "serial" })
	public static void main(String args[]) {
		final ActionManager man = ActionManager.getActionManager();
		final IDedResource<String, HashMap<String, String>> data = new IDedResource<String, HashMap<String, String>>("my data");
		man.addWatchedResource(data);
		Action printData = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				System.out.println("start print");
				CollectionUtils.printCollection(data.get());
				System.out.println("end print");
			}
		};
		Action addValues = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				System.out.println("start add");
				data.get().put("c", "d");
				System.out.println("end add");
			}
		};
		final ManagedAction ma = new ManagedAction(printData, "print data", data.getID());
		ManagedAction ma3 = new ManagedAction(addValues, "change data", data.getID());
		man.addAvailableAction(ma, null);
		man.addAvailableAction(ma3, null);
		ma3.setWaitTime(10000);
		//man.addAvailableAction(printData, "print data");
		Action init = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				try {
					Thread.sleep(5000);
				}
				catch (InterruptedException ex) {
					return;
				}
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("a", "b");
				map.put("1", "2");
				map.put("@", "#");
				data.initialize(map);
			}
		};
		ManagedAction ma2 = new ManagedAction(init, "load hashmap");
		man.setIntializingAction(data, ma2);
		Runnable r = new Runnable() {
			public void run() {
				man.runNamedAction("change data");
			}
		};
		Runnable r2 = new Runnable() {
			public void run() {
				man.runNamedAction(ma.getName());
			}
		};
		new Thread(r2).start();
		new Thread(r).start();
		man.runNamedAction(ma.getName());
		//debugging
		CollectionUtils.printCollection(man.availableActions);
		CollectionUtils.printCollection(man.initializingAction);
		CollectionUtils.printCollection(man.watchedResources);
	}	
}