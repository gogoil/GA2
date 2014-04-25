
public class AvailablePos {
	public position pos;
	public int location;
	public int locWidth, locHeight;
	
	public AvailablePos(int location, int locWidth, int locHeight, position pos)
	{
		this.pos = pos;
		this.location = location;
		this.locWidth = locWidth;
		this.locHeight = locHeight;
	}
	
	void print()
	{
		System.out.println(location + ": (" + locWidth + ", " + locHeight + ")" + pos);
	}
}
