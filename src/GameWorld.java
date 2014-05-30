

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gridworld.BoundedGrid;
import gridworld.Location;
import gridworld.ActorWorld;
import gridworld.Actor;
import gridworld.WorldFrame;


/**
 * @author Alex Wang
 * Remake of ActorWorld
 */
public class GameWorld extends ActorWorld
{
	private Player human = MainRunner.getPlayer();
	private JFrame frame;
	private static boolean running = false;
	
	/**
	 * Constructs game world with default grid.
	 */
	public GameWorld()
	{
		super(new BoundedGrid<Actor>(10, 20));
		
		update();
		refresh(); 
	}
	
	
	public void show()
	{
		if (frame == null)
		{
			frame = new WorldFrame<Actor>(this);
			frame.setVisible(true);
			frame.setSize(1030,660);
		}
	}


	@SuppressWarnings("static-access")
	public void update()
	{
		setMessage("Health: "+human.getHealth() + "\tAmmo: "+human.getAmmo() + "\tExperience: "+human.getExp() + "\tDefense: "+ (int)human.getDefense()+"%"
				+ "\nLevel: "+human.getLevel() + "\tBandages: "+human.getBandages() + "\tKey Items: "+" ["+human.getKeyItems() + "/5]");
	}
	
	/**
	 * 
	 * @return current human player
	 */
	public Player getPlayer()
	{
		return human;
	}
	


	/**
	 * Disables methods when clicking inside grid
	 */
	public boolean locationClicked(Location loc)
	{
		if (getGrid().isValid(loc))
			return true;
		else
			return false;
	}
	
	
	@SuppressWarnings("static-access")
	public boolean keyPressed(String description, Location loc)
	{
		/**
		 * Movement input
		 */
			try 
			{	
				update();
				
				if (description.equals("UP") && running)
				{
					if (!getGrid().isValid(human.getLocation().getAdjacentLocation(Location.NORTH)))
					{
						Location temp = new Location(getGrid().getNumRows()-1, human.getLocation().getCol());
						human.moveTo(temp);
						refresh();
					}
					else 
					{
						if (getGrid().get(human.getLocation().getAdjacentLocation(Location.NORTH))==null)
						human.moveTo(human.getLocation().getAdjacentLocation(Location.NORTH));
					}
					return true;
				}
				if (description.equals("DOWN") && running)
				{
					if (!getGrid().isValid(human.getLocation().getAdjacentLocation(Location.SOUTH)))
					{
						Location temp = new Location(0, human.getLocation().getCol());
						human.moveTo(temp);
						refresh();
					}
					else 
					{
						if (getGrid().get(human.getLocation().getAdjacentLocation(Location.SOUTH))==null)
						human.moveTo(human.getLocation().getAdjacentLocation(Location.SOUTH));
					}
					return true;
				}
				if (description.equals("LEFT") && running)
				{
					if (!getGrid().isValid(human.getLocation().getAdjacentLocation(Location.WEST)))
					{
						Location temp = new Location(human.getLocation().getRow(), getGrid().getNumCols()-1);
						human.moveTo(temp);
						refresh();
					}
					else 
					{
						if (getGrid().get(human.getLocation().getAdjacentLocation(Location.WEST))==null)
						human.moveTo(human.getLocation().getAdjacentLocation(Location.WEST));
					}
					return true;
				}
				if (description.equals("RIGHT") && running)
				{
					if (!getGrid().isValid(human.getLocation().getAdjacentLocation(Location.EAST)))
					{
						Location temp = new Location(human.getLocation().getRow(), 0);
						human.moveTo(temp);
						refresh();
					}
					else 
					{
						if (getGrid().get(human.getLocation().getAdjacentLocation(Location.EAST))==null)
						human.moveTo(human.getLocation().getAdjacentLocation(Location.EAST));
					}
					return true;
				}
				
				/**
				 * Aiming input
				 */
				if (description.equals("W")&& running)
				{
					human.setDirection(Location.NORTH);
					return true;
				}
				if (description.equals("A")&& running)
				{
					human.setDirection(Location.WEST);
					return true;
				}
				if (description.equals("S")&& running)
				{
					human.setDirection(Location.SOUTH);
					return true;
				}
				if (description.equals("D")&& running)
				{
					human.setDirection(Location.EAST);
					return true;
				}
				
				
				/**
				 * Melee attack
				 */
				if (description.equals("SPACE")&& running)
				{
					human.attack();
					return true;
				}
				
				/**
				 * Shoot
				 */
				if (description.equals("F")&& running)
				{
					human.shoot();
					update();
					return true;
				}
				
				
				/**
				 * Opens up inventory
				 */
				if (description.equals("I")&& running)
				{
					MainRunner.openInventory();
					return true;
				}
				
				/**
				 * Uses a bandage without opening up Inventory
				 */
				if (description.equals("H")&& running)
				{
					if (human.getBandages() > 0)
					{
						Player.addHealth(3);
						Player.removeBandage();
						update();
					}
				}
				
				/**
				 * Opens command prompt thing. For debug/testing.
				 */
				if (description.equals("SLASH"))
				{
					String command = JOptionPane.showInputDialog("Enter command");
					
					if (command.equalsIgnoreCase("help"))
					{
						JOptionPane.showMessageDialog(null, "help, heal, ammo, exp, keyitem, godmode");
					}
					
					if (command.equalsIgnoreCase("heal"))
					{
						String amt = JOptionPane.showInputDialog("Enter heal amount");
						human.addHealth(Integer.parseInt(amt));
					}
					
					if (command.equalsIgnoreCase("ammo"))
					{
						String amt = JOptionPane.showInputDialog("Enter ammo amount");
						human.addAmmo(Integer.parseInt(amt));
					}
					
					if (command.equalsIgnoreCase("exp"))
					{
						String amt = JOptionPane.showInputDialog("Enter exp amount");
						human.addExp(Integer.parseInt(amt));
					}
					
					if (command.equalsIgnoreCase("keyitem"))
					{
						String amt = JOptionPane.showInputDialog("Enter keyItem amount");
						human.addKeyItem(Integer.parseInt(amt));
					}
					
					if (command.equalsIgnoreCase("godmode"))
					{
						human.godmode();
					}
				}
				
				return true;
			}
			catch (Exception e)
			{
				setMessage("Gameover.");
				System.out.println(e.toString()); //prints exception
				return false;
			}
	}
	
	public static void startGame(boolean status)
	{
		running = status;
	}
	
	/**
	 * Clears any existing actors except Player and reloads all things into a "new" world
	 */
	public void refresh()
	{
		ArrayList<Location> temp = getGrid().getOccupiedLocations();
		ArrayList<Actor> toClear = new ArrayList<Actor>();
		
		for(Location tempLocation : temp)
		{
			toClear.add(getGrid().get(tempLocation));
		}
		
		for (Actor i : toClear)
		{
			if (!(i instanceof Player))
			{
				i.removeSelfFromGrid();
			}
		}
		
		for (int i = 0; i < 10; i++)
		{
			add(new Zombie());
		}
		
		Random rand = new Random();
		int genChest = rand.nextInt(3);
		if (genChest == 2)
		{
			add(new Chest());
			add(new Chest());
		}
		else if (genChest == 1)
		{
			add(new Chest());
		}
		else
		{
			return;
		}
	}
	
	public void dispose()
	{
		frame.dispose();
	}
	
	public boolean isRunning()
	{
		return running;
	}
}
