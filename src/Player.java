
import gridworld.Actor;
import gridworld.Location;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *  The Player class is the Actor that the player actually
 *  controls. Player starts out with 10 health, attack damage of 2,
 *  defense of 0, and 3 bandages.
 *  @author Alex Wang
 */
public class Player extends Zombie
{
	private static int health = 20;
	private int maxHealth = 20;
	private static int ammo = 10;
	private double dmg = 2;
	private double defense = 0; //blocks that percentage
	private static int bandages = 3;
	private static int keyItems = 0;
	private static int experience = 0;
	private static int level = 1; //max level of 3
	private ArrayList<Item> inventory = new ArrayList<Item>();
	public Random tempRandom = new Random();
	int steps = 0;
	
	
	public Player() 
	{
		super();
		
	}
	
	public void act()
	{
		if (steps%50 ==0)
		{
			if (getColor()==Color.red)
			{
				setColor(null);
			}
			checkLevel();
			steps++;
		}
		else
			steps++;
	}

	/**
	 * 
	 */
	public int getKeyItems()
	{
		return keyItems;
	}
	
	/**
	 * sets amount of key items 
	 * @param amt
	 */
	public void setKeyItems(int amt)
	{
		keyItems = amt;
	}
	
	public double getDefense()
	{
		return defense;
	}
	
	
	public int getBandages()
	{
		return bandages;
	}
	
	public static void removeBandage()
	{
		bandages--;
	}
	
	/**
	 * 
	 * @return Current health of player
	 */
	public String getHealth()
	{
		if (health > maxHealth)
		{
			health = maxHealth;
		}
		String playerHealth = "["+health+"/"+maxHealth+"]";
		return playerHealth;
	}
	
	/**
	 * 
	 */
	public static void addHealth(int amount)
	{
		 health += amount;
	}
	
	
	/**
	 * @return Current amt of ammo.
	 */
	public int getAmmo()
	{
		return ammo;
	}
	
	public static void addAmmo(int amt)
	{
		ammo += amt;
	}
	
	
	/**
	 * 
	 * @return amount of experience
	 */
	public static int getExp()
	{
		return experience;
	}
	
	
	/**
	 * 
	 */
	public static void addExp(int amount)
	{
		experience += amount;
		if (experience%100 == 0)
			levelUp();
	}
	
	public static void addKeyItem(int amt)
	{
		keyItems += amt;
	}
	
	
	public static void giveItem()
	{
		Random tempRandom = new Random();
		int temp = tempRandom.nextInt(10); 
		
		switch (temp) 
		{
		case 0: //%30 chance
		case 1:
		case 2:
			ammo += tempRandom.nextInt(5)+1;
			bandages += 1;
			break;
			
		case 3:
			ammo += 10;
			break;
			
		case 4:
			ammo += 5;
			keyItems += 1;
			break;
			
		case 5:
			ammo += 2;
			bandages += 1;
			break;
			
		case 6:
			ammo += tempRandom.nextInt(10)+1;
			break;
		
		case 7:
			bandages += 1;
			ammo += 1;
			break;
			
		case 8:
			bandages += tempRandom.nextInt(3)+1;
			break;
			
		case 9:
			ammo += 2;
			keyItems += 1;
			break;

		default:
			ammo += 1;
			break;
		}
	}
	public static void giveItem(int number)
	{
		switch (number) 
		{
		case 0: 
		case 1:
			break;
			
		case 2:
			ammo += 1;
			break;
			
		case  3:
			bandages += 1;

		default:
			break;
		}
	}
	
	/**
	 * 
	 */
	public int getLevel()
	{
		return level;
	}
	
	/**
	 * 
	 */
	public static void levelUp()
	{
		if (level < 3)
		{
			level++;
		}
	}
	
	
	public void checkLevel()
	{
		switch (level) {
		case 2:
			defense = 25;
			dmg = 3;
			maxHealth = 25;
			break;
			
		case 3:
			defense = 50;
			dmg = 4;
			maxHealth = 30;
			break;

		default:
			break;
		}
	}
	
	/**
	 * 
	 */
	public ArrayList<Item> getInventory()
	{
		if (inventory.size() > 0)
			inventory.clear();
			
		for (int i = 0; i < bandages; i++)
		{
			inventory.add(new Bandage());
		}
		
		for (int i=0; i < keyItems; i++)
		{
			inventory.add(new KeyItem());
		}

		return inventory;
	}
	
	
	/**
	 * 
	 */
	public void hurt(double damage)
	{
		damage = (1-(defense/100)) * damage;
		health = health - (int) damage;
		setColor(Color.red);

		if (health <= 0)
			dead();
	}
	
	
	/**
	 * attack
	 */
	public void attack()
	{
		ArrayList<Actor> actors = getGrid().getNeighbors(getLocation());
		ArrayList<Actor> zombies = new ArrayList<Actor>();
		if (actors.size() > 0)
		{
			for (Actor a : actors)
			{
				if (a instanceof Zombie)
				{
					zombies.add(a);
				}
				else if (a instanceof Chest)
					((Chest) a).hurt(dmg);
			}
			
			if (zombies.size() > 0) //hurts a random zombie
			{
				//Random tempRandom = new Random();
				int randomInt = tempRandom.nextInt(zombies.size());
				((Zombie) zombies.get(randomInt)).hurt(dmg);
			}
		}
	}
	
	/**
	 * shoot
	 */
	public void shoot()
	{
		//stuff to shoot forward/direction facing
		Location front = getLocation().getAdjacentLocation(getDirection());
		
		if (ammo > 0)
		{
			if (getGrid().isValid(front))
			{
				if (getGrid().get(front) instanceof Zombie)
				{
					Zombie temp = (Zombie)(getGrid().get(front));
					temp.hurt(5);
				}
				else
				{
					Bullet bullet = new Bullet();
					bullet.putSelfInGrid(getGrid(),front);
					bullet.setDirection(getDirection());
				}
				ammo--;
			}
		}
			return; //spawn a bullet
	}
	
	
	/**
	 * Removes player if dead
	 */
	public void dead()
	{
		removeSelfFromGrid();
		GameWorld.startGame(false);
		MainRunner.gameover();
	}
	
	public void godmode()
	{
		defense = 100;
	}
	
	public void reset()
	{
		health = 20;
		maxHealth = 20;
		ammo = 10;
		dmg = 2;
		defense = 0;
		bandages = 3;
		keyItems = 0;
		experience = 0;
		level = 1;
	}
}
