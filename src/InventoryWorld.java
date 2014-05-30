
import gridworld.Location;
import gridworld.ActorWorld;
import gridworld.Actor;
import gridworld.WorldFrame;

import java.util.ArrayList;

import javax.swing.JFrame;


/**
 * 
 * @author Alex Wang
 *
 */
public class InventoryWorld extends ActorWorld
{
	private JFrame frame;
	public InventoryWorld()
	{
		super();
		setMessage("This is your inventory.");
		
	}
	
	public void show()
	{
		if (frame == null)
		{
			frame = new WorldFrame<Actor>(this);
			frame.setVisible(true);
		}
	}
	
	/**
	 * Disables methods when clicking inside grid unless it's an Item
	 */
	public boolean locationClicked(Location loc)
	{
		if (getGrid().get(loc) == null)
			return true;
		else
			return false;
	}
	
	public void dispose(ArrayList<Location> occupied)
	{
		frame.dispose();

		for (Location i : occupied)
		{
			remove(i);
		}
	}
	
	public void dispose()
	{
		frame.dispose();
	}
	
}
