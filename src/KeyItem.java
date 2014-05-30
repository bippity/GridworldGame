
import java.util.Random;

public class KeyItem extends Item
{
	Random random = new Random();
	private int type = random.nextInt(5);
	
	public KeyItem()
	{
		super();
	}
	
	@SuppressWarnings("static-access")
	public void use()
	{
		/*ArrayList<Item> temp = MainRunner.getPlayer().getInventory();
		ArrayList<Item> remove = new ArrayList<Item>();
		int count = 0;
		for (Item i : temp)
		{
			if (i instanceof KeyItem)
			{
				count++;
				remove.add(i);
			}
		}
		if (count == 5)
		{
			for (Item i : remove)
				i.removeSelfFromGrid();
		}
		*/
		if (MainRunner.getPlayer().getKeyItems() >= 5)
		{
			MainRunner.getPlayer().addExp(MainRunner.getPlayer().getKeyItems() * 100);
			MainRunner.getPlayer().setKeyItems(0);
			MainRunner.win();
		}
	}
	
	public String getImageSuffix()
	{	
		switch (type) 
		{
		case 0:
			return "_1";
		
		case 1:
			return "_2";
			
		case 2:
			return "_3";
			
		case 3:
			return "_4";
			
		case 4:
			return "_5";

		default:
			return "_1";
		}
	}
}
