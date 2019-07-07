package biz.orgin.minecraft.hothgenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import org.bukkit.Bukkit;

public class HothTaskManager
{
	public static final String TASKFILE = "plugins/HothGenerator/hoth_tasklist.dat";
	
	private int taskId;
	
	private HothGeneratorPlugin plugin;
	private Vector<HothRunnable> taskList;
	
	public HothTaskManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
		this.taskList = new Vector<HothRunnable>();
		this.resume();
	}
	
	public void addTask(HothRunnable task, boolean prioritized)
	{
		task.setPrioritized(prioritized);
		this.taskList.add(task);
	}

	public void pause()
	{
		if(this.taskId!=-1)
		{
			Bukkit.getServer().getScheduler().cancelTask(this.taskId);
		}

		// remove all tasks from storage
		File file = new File(HothTaskManager.TASKFILE);
		file.delete();
		
		try
		{
			for(int i=0;i<this.taskList.size();i++)
			{
				taskList.elementAt(i).serialize();
				this.plugin.debugMessage("Serializing task: " + taskList.elementAt(i).getName());
			}
			
			FileOutputStream fout = new FileOutputStream(HothTaskManager.TASKFILE);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(this.taskList);
			oos.close();
			fout.close();
			this.plugin.debugMessage("Stored " + this.taskList.size() + " tasks.");

		}
		catch(Exception e)
		{
			// Could not store the object. Just ignore.
			this.plugin.debugMessage("Failed to store task list to storage");
		}
		
		// Clear the taskList
		this.taskList.setSize(0);
	}
	
	@SuppressWarnings("unchecked")
	public void resume()
	{
	
		// Load all tasks and remove from storage
		try
		{
			FileInputStream fin = new FileInputStream(HothTaskManager.TASKFILE);
			ObjectInputStream ois = new ObjectInputStream(fin);
			this.taskList  = (Vector<HothRunnable>) ois.readObject();
			this.plugin.debugMessage("Loaded " + this.taskList.size() + " tasks.");
			ois.close();
			fin.close();
			
			// No point in deserializing the tasks here since no worlds are loaded at this point anyway.
			// Objects are deserialized once the task manager starts. With a counter that removes
			// the task from the list after too many retries. 

		}
		catch(Exception e)
		{
			this.taskList = new Vector<HothRunnable>();
			this.plugin.debugMessage("Failed to read task list from storage");
		}
		
		File file = new File(HothTaskManager.TASKFILE);
		file.delete();
		
		try
		{
			this.taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TaskExecutor(this.plugin), 10, 5);
		}
		catch(Exception e)
		{
			plugin.logMessage("WARNING! Exception while trying to register TaskExecutor repeating task. You probably need to restart the server.", true);
		}
	}
	
	private class TaskExecutor implements Runnable
	{
		private HothGeneratorPlugin plugin;
		
		public TaskExecutor(HothGeneratorPlugin plugin)
		{
			this.plugin = plugin;
		}

		@Override
		public void run()
		{
			
			if(taskList.size()>0)
			{
				boolean isEmpty = false;
				boolean runNormalTask = true;
				while(!isEmpty)
				{
					boolean stop = false;
					for(int i=0;!stop && i<taskList.size();i++)
					{
						HothRunnable task = taskList.get(i);
						if(task.isPrioritized())
						{
							this.executeTask(task);
							stop = true;
							runNormalTask = false;
						}
					}
					isEmpty = stop == false;
				}
				
				if(runNormalTask && taskList.size()>0)
				{
					HothRunnable task = taskList.get(0);
					this.executeTask(task);
				}
			}
		}
		
		private void executeTask(HothRunnable task)
		{
			task.setPlugin(this.plugin);
			if(task.getWorld()!=null)
			{
				try
				{
					if(!task.isPrioritized())
					{
						this.plugin.debugMessage("Executing task: " + task.getName() + " with parameters: " + task.getParameterString());
					}
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, task);
					taskList.remove(task);
				}
				catch(Exception e)
				{
					plugin.logMessage("WARNING! Exception while trying to execute task in queue " + task.getName() + ". You probably need to restart the server.", true);
				}
			}
			else
			{
				// try to deserialize the task again, perhaps the world was not loaded?
				task.deserialize();
				if(task.isStale())
				{
					taskList.remove(task);
					plugin.debugMessage("Removed task " + task.getName() + " from task list, could not deserialize.");
				}
			}
		}
	}
	
}
