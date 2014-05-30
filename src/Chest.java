
import gridworld.Rock;
import gridworld.Location;
import java.awt.Color;

public class Chest extends Rock
{
	private int health = 5; 
	private int steps = 0;
	
	public Chest()
	{
		super();
		setColor(null);
		setDirection(Location.SOUTH);
	}
	
	public void act()
	{
		if (steps%50==0)
		{
			if (getColor() == Color.red)
				setColor(null);
			steps++;
		}
		else
		{
			steps++;
		}
	}
	
	public void hurt(double damage)
	{
		health -= (int) damage;
		setColor(Color.red);
		if (health <= 0)
			dead();
	}
	
	public void dead()
	{
		removeSelfFromGrid();
		Player.giveItem();
	}
}
