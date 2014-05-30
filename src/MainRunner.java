

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import gridworld.Location;


public class MainRunner {

	static Player human = new Player();
	static InventoryWorld inventory = new InventoryWorld();
	public static GameWorld world;
	
	static HighscoreManager manager = new HighscoreManager();
	
	public static void main(String[] args) 
	{ 
	    Object[] options = { "Start Game", "Instructions", "Leaderboards/Highscores", "Exit Game"};
	   
	    int option =  JOptionPane.showOptionDialog(null, "Click something.", "Warning", JOptionPane.DEFAULT_OPTION, 
	    		JOptionPane.WARNING_MESSAGE, null, options, options[0]);
	    
	   switch (option) 
	   	{
	   		case 0:
	   			startGame();
	   			break;
	   			
	   		case 1:
	   			showInstructions();
	   			break;
	   			
	   		case 2:
	   			showHighscores();
	   			break;
	   			
	   		case 3:
	   			exit();
	   			break;

	   		default:
	   			break;
	   	}
	   
	    System.setProperty("info.gridworld.gui.selection", "hide");
		System.setProperty("info.gridworld.gui.tolltips", "hide");
		System.setProperty("info.gridworld.gui.frametitle", "Zombie Survival Escape");
	}
	
	public static void startGame()
	{
		world = new GameWorld();
		
		world.add(human);
		
		world.show();
		
		int option = JOptionPane.showConfirmDialog(null, "Are you ready? \n*Any zombies next to you will immediately start attacking", "Get Ready!", JOptionPane.YES_NO_OPTION);
		if (option != JOptionPane.YES_OPTION)
		{
			exit();
		}	
	}
	
	public static boolean isRunning()
	{
		return world.isRunning();
	}
	
	public static void showInstructions()
	{
		File htmlFile = new File("src/gridworld/GridWorldHelp.html");
		try {
			Desktop.getDesktop().browse(htmlFile.toURI());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows a JOptionpane of the top 10 highscores
	 */
	public static void showHighscores()
	{
		//JDialog f = new LeaderBoard(new JFrame(), manager.getHighscoreString());
		//f.show();
		JOptionPane.showMessageDialog(null, manager.getHighscoreString(), "Leaderboards/Highscores", 3);
		
		System.exit(0);
	}

	public static void openInventory()
	{	
		if (inventory.getGrid().getOccupiedLocations().size() >0)
		{
			ArrayList<Location> occupied = inventory.getGrid().getOccupiedLocations();
			inventory.dispose(occupied);
			inventory = new InventoryWorld();
		}
			
		ArrayList<Item> temp = human.getInventory();
		for (Item a : temp)
		{
			inventory.add(a);
		}
			
		inventory.show();
	}
	
	public static Player getPlayer()
	{
		return human;
	}
	
	@SuppressWarnings("static-access")
	public static void win()
	{
		inventory.dispose();
		int option = JOptionPane.showConfirmDialog(null, "Congrats! You got all the items and won! Do you want to submit your score?\nScore: " + human.getExp(), "YOU WON!", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION)
		{
			String playerName = JOptionPane.showInputDialog("Please enter your name: ");
			while (playerName.length() < 1)
			{
				playerName = JOptionPane.showInputDialog("Invalid name. Please enter your name: ");
			}
			
			manager.addScore(playerName, human.getExp());
			System.exit(0);
		}
		else
			System.exit(0);
	}
	
	@SuppressWarnings("static-access")
	public static void gameover()
	{
		world.dispose();
		int option = JOptionPane.showConfirmDialog(null, "Oh dear, you died! Thanks for Playing!\nScore: " + human.getExp() + "\nSubmit score?", "GAMEOVER!", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION)
		{
			String playerName = JOptionPane.showInputDialog("Please enter your name: ");
			try
			{
				while (playerName.length() < 1)
				{
					playerName = JOptionPane.showInputDialog("Invalid name. Please enter your name: ");
				}
				
				manager.addScore(playerName, human.getExp());
				showHighscores();
			}
			catch (NullPointerException e)
			{
				System.exit(0);
			}
		}
		else 
		{
			System.exit(0);
		}
	}

	public static HighscoreManager getHighscoreManager()
	{
		return manager;
	}
	/**
	 * Exits out of the program
	 */
	public static void exit()
	{
		System.exit(0);
	}
	
	public static GameWorld getWorld()
	{
		return world;
	}
}
