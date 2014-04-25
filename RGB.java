import java.awt.Color;

public class RGB {
	public static int GetRed(int b) {
		Color c = new Color(b);
		return c.getRed();
	}

	public static int GetGreen(int b) {
		Color c = new Color(b);
		return c.getGreen();
	}

	public static int GetBlue(int b) {
		Color c = new Color(b);
		return c.getBlue();
	}
}
