
public class Bandage extends Item
{
	public Bandage()
	{
		super();
	}
	
	public void use()
	{
		Player.addHealth(3);
		Player.removeBandage();
		removeSelfFromGrid();
	}
}
