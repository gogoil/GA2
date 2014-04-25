import java.awt.Color;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class puzzle {
	public BufferedImage img;
	public int puzzleWidth, puzzleHeight; // Size of puzzle in pieces
	public int height;
	public int width;
	public int pieceWidth, pieceHeight;
	public piece pieces[][];

	public puzzle(String fileName, int n, int m) {
		puzzleWidth = n;
		puzzleHeight = m;
		init(fileName);
		height = img.getHeight();
		width =  img.getWidth();
		pieceWidth = width / puzzleWidth;
		pieceHeight = height / puzzleHeight;
		pieces = new piece[puzzleWidth][puzzleHeight];
		for (int i = 0; i < this.puzzleWidth; i++) {
			for (int k = 0; k < this.puzzleHeight; k++) {
				pieces[i][k] = new piece(this, i, k);
			}
		}
	}

	public piece getPiece(int location) {
		// System.out.println("loc=" + location);
		return pieces[location % puzzleWidth][location / puzzleWidth];
	}

	public void init(String fileName) {
		try {
			URL url = new URL(fileName);
			img = ImageIO.read(url);
		} catch (IOException e) {
			System.out.println("problem reading file: " + fileName);
		}

	}

	public int GetRed(int b) {
		Color c = new Color(b);
		return c.getRed();
	}
/*
	public double dissimilarity(piece a, piece b, position pos) {
		if (pos == position.left) {
			return dissimilarity(a.getPixelLeftBorder(), b.getPixelRightBorder());
		}
		if (pos == position.down) {
			return dissimilarity(a.getPixelDownBorder(), b.getPixelUpBorder());
		}
		if (pos == position.up) {
			return dissimilarity(a.getPixelUpBorder(), b.getPixelDownBorder());
		}
		if (pos == position.right) {
			return dissimilarity(a.getPixelRightBorder(), b.getPixelLeftBorder());
		}
		return 0;
	}

	private double dissimilarity(int pixelBorder1[], int pixelBorder2[]) {
		long sum = 0;
		for (int i = 0; i < pixelBorder1.length; i++) {
			sum += Math.pow(
					RGB.GetRed(pixelBorder1[i]) - RGB.GetRed(pixelBorder2[i]),
					2.0);
			sum += Math.pow(
					RGB.GetGreen(pixelBorder1[i])
							- RGB.GetGreen(pixelBorder2[i]), 2.0);
			sum += Math
					.pow(RGB.GetBlue(pixelBorder1[i])
							- RGB.GetBlue(pixelBorder2[i]), 2.0);
		}
		return Math.sqrt(sum);
	}
*/
	// Trying to optimize for speed. For BestBuddy all we need is dissimilraty comparison which can be done with integers
	public long dissimilarityInt(piece a, piece b, position pos) {
		if (pos == position.left) {
			return dissimilarityInt(a.getPixelLeftBorder(), b.getPixelRightBorder());
		}
		if (pos == position.down) {
			return dissimilarityInt(a.getPixelDownBorder(), b.getPixelUpBorder());
		}
		if (pos == position.up) {
			return dissimilarityInt(a.getPixelUpBorder(), b.getPixelDownBorder());
		}
		if (pos == position.right) {
			return dissimilarityInt(a.getPixelRightBorder(), b.getPixelLeftBorder());
		}
		return 0;
	}

	private long dissimilarityInt(int pixelBorder1[], int pixelBorder2[]) {
		long sum = 0;
		for (int i = 0; i < pixelBorder1.length; i++) {
			int temp = RGB.GetRed(pixelBorder1[i]) - RGB.GetRed(pixelBorder2[i]);
			sum += temp*temp;
			temp = RGB.GetGreen(pixelBorder1[i]) - RGB.GetGreen(pixelBorder2[i]);
			sum += temp*temp;
			temp = RGB.GetBlue(pixelBorder1[i])	- RGB.GetBlue(pixelBorder2[i]);
			sum += temp*temp;
		}
		return sum; // Giving up the square root for the sake of speed. Dissimilarity would be just the same.
	}

}