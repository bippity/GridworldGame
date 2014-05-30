
import gridworld.Actor;
import gridworld.Rock;
import gridworld.Location;

public class Bullet extends Rock
{
	int steps = 0;
	double dmg = 5;
	public Bullet()
	{
		super();
		setColor(null);
	}
	
	public void act()
	{
		Location front = getLocation().getAdjacentLocation(getDirection());
		
		if (getGrid().isValid(front))
		{
			if (getGrid().get(front) instanceof Actor)
			{
				if (getGrid().get(front) instanceof Zombie && !(getGrid().get(front) instanceof Player))
				{
					Zombie temp = (Zombie)(getGrid().get(front));
					temp.hurt(dmg);
					removeSelfFromGrid();
				}
				else 
				{
					removeSelfFromGrid();
				}
			}
			else 
			{
				if (steps < 3)
				{
					moveTo(front);
					steps++;
				}
				else 
				{
					removeSelfFromGrid();
				}
			}
		}
		else
		{
			removeSelfFromGrid();
		}
	}
}
