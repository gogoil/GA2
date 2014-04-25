import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

public class chromosome {
	private int[][] matrix;
	private long fitness;
	private double normalizedFitness;// ייצג את value:Gvalue (שבר :ערך לחלק לערך
	private double accomulatedNormalFitness;// ייצג את מיקום הכרומוזום על ציר
											// הכרומוזימים

	public chromosome() {
		this.matrix = new int[main.p.puzzleWidth][main.p.puzzleHeight];	
	}
	
	public void initAsRandom()
	{
		randomize();
	}
	
	public void initAsBest()
	{
		DontRandomize();
		this.fitness = evaluate();
	}

	public chromosome(int[][] board) {
		this.matrix = new int[main.p.puzzleWidth][main.p.puzzleHeight];
		int tempI = 0, tempJ = 0;
		for (int i = 0; i < board.length; i++) {
			tempJ = 0;
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != -1) {
					matrix[tempI][tempJ] = board[i][j];
					tempJ++;
				}

			}
			if (tempJ != 0)
				tempI++;
		}
		this.fitness = evaluate();// to recheck. too
	}

	public int getAdjacentPos(int location, position pos) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == location) {
					if (pos == position.right) {
						if (i == matrix.length-1)
							return -1;
						else
							return matrix[i + 1][j];
					}
					if (pos == position.left) {
						return (i == 0) ? -1 : matrix[i - 1][j];
					}
					if (pos == position.down) {
						return (j == matrix[i].length-1) ? -1 : matrix[i][j + 1];
					}
					if (pos == position.up) {
						return (j == 0) ? -1 : matrix[i][j - 1];
					}
				}

			}

		}
		return -1;
	}

	public void DontRandomize() {
		ArrayList<Integer> cards = new ArrayList<Integer>();
		for (int i = 0; i < main.p.puzzleHeight * main.p.puzzleWidth; i++) {
			cards.add(i);
		}
		//Collections.shuffle(cards, main.generator);

		for (int i = 0; i < main.p.puzzleWidth; i++) {
			for (int j = 0; j < main.p.puzzleHeight; j++) {
				matrix[i][j] = cards.get(i + j * main.p.puzzleWidth);

			}
		}
	}
	
	public void randomize() {
		ArrayList<Integer> cards = new ArrayList<Integer>();
		for (int i = 0; i < main.p.puzzleHeight * main.p.puzzleWidth; i++) {
			cards.add(i);
		}
		Collections.shuffle(cards, main.generator);

		for (int i = 0; i < main.p.puzzleWidth; i++) {
			for (int j = 0; j < main.p.puzzleHeight; j++) {
				matrix[i][j] = cards.get(i * main.p.puzzleHeight + j);

			}
		}
	}

	public long evaluate() {
		long count = 0;
		for (int i = 0; i < main.p.puzzleWidth; i++) {
			for (int j = 0; j < main.p.puzzleHeight; j++) {
				if (j != main.p.puzzleHeight - 1) {
					count += main.p.dissimilarityInt(
							main.p.getPiece(matrix[i][j]),
							main.p.getPiece(matrix[i][j + 1]), position.down);
				}
				if (i != main.p.puzzleWidth - 1) {
					count += main.p.dissimilarityInt(
							main.p.getPiece(matrix[i][j]),
							main.p.getPiece(matrix[i + 1][j]), position.right);
				}
			}
		}
		return Long.MAX_VALUE /count;
	}

	public long getFitness() {
		return fitness;
	}

	public void setFitness(long fitness) {
		this.fitness = fitness;
	}

	public double getNormalizedFitness() {
		return normalizedFitness;
	}

	public double getAccomulatedNormalFitness() {
		return accomulatedNormalFitness;
	}

	public void setAccomulatedNormalFitness(double position) {
		this.accomulatedNormalFitness = position;
	}

	public void setNormalizedFitness(double normalizedFitness) {
		this.normalizedFitness = normalizedFitness;
	}

	public void print(String filename)
	{
		BufferedImage imgOut = new BufferedImage(main.p.width, main.p.height, BufferedImage.TYPE_INT_RGB);
		File outputfile = new File(filename);

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				copyPieceIntoNewImage(imgOut, matrix[i][j], i, j);
			}
			
		}
			try {
				ImageIO.write(imgOut, "jpg", outputfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return;
	}
	
	private void copyPieceIntoNewImage(BufferedImage dst, int srcPieceNum, int row, int col /* width */) 
	{
	    int srcDX = main.p.pieceWidth * (srcPieceNum % main.p.puzzleWidth);
	    int srcDY = main.p.pieceHeight * (srcPieceNum / main.p.puzzleWidth);
	    int dstDX = main.p.pieceWidth * row;
	    int dstDY  = main.p.pieceHeight * col;
	    
        for (int x = 0; x < main.p.pieceWidth; x++) {
            for (int y = 0; y < main.p.pieceHeight; y++) {
                dst.setRGB( dstDX + x, dstDY + y, main.p.img.getRGB(srcDX + x, srcDY + y) );

            }
        }
	}
}