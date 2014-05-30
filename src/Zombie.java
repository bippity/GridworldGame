
import gridworld.Actor;
import gridworld.Critter;
import gridworld.Grid;
import gridworld.Location;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;


public class Zombie extends Critter
{
	private Grid<Actor> grid;
	private Location location;
	private int direction;
	private boolean chased = false;
	private boolean attacked = false;
	private int health = 10;
	private int dmg = 4;
	private Color color = null;
	private int steps = 0;
	
	public Zombie()
	{
		super();
		direction = Location.SOUTH;
		//grid = null;
		//location = null;
		setColor(color);
	}
	
	public void act()
	{
		GameWorld.startGame(true);
		
		if (getGrid() == null)
			return;
		if (MainRunner.isRunning())
		{
			if (steps%50 == 0)
			{
				ArrayList<Actor> actors = getActors();
				processActors(actors);
	
				if (getColor() == Color.red) 
				{
					setColor(null);
				}
	
				if (!chased) 
				{
					attack();
					if (!attacked) { //if it didn't hit the player, move around
						ArrayList<Location> moveLocs = getMoveLocations();
						Location loc = selectMoveLocation(moveLocs);
						makeMove(loc);
					}
					attacked = false;
				}
				else 
				{
					chased = false;
				}
				steps++;
			}
			else
			{
				steps++;
			}
		}
	}
	
	public void processActors(ArrayList<Actor> actors)
	{
		boolean found = false;
		Player human = new Player();
		for (Actor a : actors)
		{
			if (a instanceof Player)
			{
				human = (Player) a;
				found = true;
				break;
			}
			else
			{
				chased = false;
			}
		}
		
		if (found)
		{
			Location towardLocation = getLocation().getAdjacentLocation(getLocation().getDirectionToward(human.getLocation()));
			
			if (getGrid().get(towardLocation) instanceof Actor)
			{
				return;
			}
			else 
			{
				moveTo(towardLocation);
				chased = true;
			}
		}
	}
	

	
	/**
	 * Gets current direction of actor/zombie.
	 * @return direction of this actor/zombie, angle between 0 and 359 degrees.
	 */
	public int getDirection()
	{
		return direction;
	}
	
	/**
	 * Sets current direction
	 * @param newDirection the new direction. Angle between 0 and 359 degrees
	 */
	public void setDirection(int newDirection)
	{
		direction = newDirection % Location.FULL_CIRCLE;
		if (direction < 0)
			direction += Location.FULL_CIRCLE;
	}
	
	/**
	 * 
	 */
	public Grid<Actor> getGrid()
	{
		return grid;
	}
	
	/**
	 * 
	 */
	public Location getLocation()
	{
		return location;
	}
	
	/**
	 * 
	 * @return
	 */
	public void putSelfInGrid(Grid<Actor> gr, Location loc)
    {
        if (grid != null)
            throw new IllegalStateException(
                    "This actor is already contained in a grid.");

        Actor actor = gr.get(loc);
        if (actor != null)
            actor.removeSelfFromGrid();
        gr.put(loc, this);
        grid = gr;
        location = loc;
    }
	
	/**
	 * 
	 * @return
	 */
	public void removeSelfFromGrid()
	{
		if (grid == null)
			throw new IllegalStateException("This actor is not contained in a grid.");
		if (grid.get(location) != this)
			throw new IllegalStateException("The grid contains a different actor at location " + location + ".");
		
		grid.remove(location);
		grid = null;
		location = null;
	}
	
	/**
	 * 
	 * @return
	 */
	public void moveTo(Location newLocation)
    {
        if (grid == null)
            throw new IllegalStateException("This actor is not in a grid.");
        if (grid.get(location) != this)
            throw new IllegalStateException(
                    "The grid contains a different actor at location "
                            + location + ".");
        if (!grid.isValid(newLocation))
            throw new IllegalArgumentException("Location " + newLocation
                    + " is not valid.");

        if (newLocation.equals(location))
            return;
        grid.remove(location);
        Actor other = grid.get(newLocation);
        if (other != null)
            other.removeSelfFromGrid();
        location = newLocation;
        grid.put(location, this);
    }
	
	
	/**
	 * 
	 * @return Amount of damage sent
	 */
	public int getDmg()
	{
		return dmg;
	}
	
	/**
	 * 
	 * @return All actors in its surrounding 2 blocks
	 */
	public ArrayList<Actor> getActors()
	{	
		ArrayList<Actor> temp = new ArrayList<Actor>();
		
		for (int i=getLocation().getRow()-2; i<=getLocation().getRow()+2; i++)
		{
			for (int j=getLocation().getCol()-2; j <= getLocation().getCol()+2; j++)
			{
				if (getGrid().isValid(new Location(i, j)) && getGrid().get(new Location(i, j)) != null)
				{
					temp.add(getGrid().get(new Location(i, j)));
				}
			}
		}
		return temp;
	}
	
	/**
	 * 
	 */
	public void attack()
	{
		//attacks player if in neighbors
		ArrayList<Actor> actors = getGrid().getNeighbors(getLocation());//getActors();
		
		for (Actor a : actors)
		{
			if (a instanceof Player)
			{
				((Player) a).hurt(dmg);
				attacked = true; //Succesfully hurt player
			}
		}
	}
	
	/**
	 * 
	 */
	public void hurt(double damage)
	{
		health -= (int) damage;
		setColor(Color.red);
		//setColor(color);
		if (health <= 0)
			dead();
	}
	
	/**
	 * Removes the zombie if it's dead.
	 */
	public void dead()
	{
		Random random = new Random();
		int item = random.nextInt(4);
		removeSelfFromGrid();
		Player.addExp(10);
		Player.giveItem(item);
	}
}
