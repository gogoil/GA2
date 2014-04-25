import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.soap.Node;

public class crossover {
	private int board[][];
	private ArrayList<AvailablePos> l;
	private chromosome a, b;
	private boolean isPlaced[];

	public crossover(chromosome a, chromosome b) {
		this.board = new int[main.p.puzzleWidth * 2 + 1][main.p.puzzleHeight * 2 + 1];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = -1;
			}
		}
		this.isPlaced = new boolean[main.p.puzzleHeight * main.p.puzzleWidth];
		this.a = a;
		this.b = b;
		l = new ArrayList<AvailablePos>();
	}

	public chromosome generateSon() {

		// put the first piece randomly
		int firstLoc = (int) (main.generator.nextFloat() * (main.p.puzzleHeight * main.p.puzzleWidth));
		board[main.p.puzzleWidth][main.p.puzzleHeight] = firstLoc;
		isPlaced[firstLoc] = true;
		generateAvailablePos();
		for (int i = 0; i < main.p.puzzleHeight * main.p.puzzleWidth - 1; i++) {

			boolean success = false;

			// algorithm level1. Need both parents to agree on the same adjacent
			// piece

			for (Iterator<AvailablePos> iterator = l.iterator(); !success
					&& iterator.hasNext();) {
				AvailablePos avPos = (AvailablePos) iterator.next();

				double r = Math.random();
				if (r < generation.MutationRate) {

					int loc = -1;
					int j = (int) (Math.random() * isPlaced.length);
					while (loc == -1) {
						if (isPlaced[j] == false) {
							loc = j;
						}
						j = (j == isPlaced.length - 1) ? 0 : j + 1;
					}
					success = true;
					isPlaced[loc] = true;
					place(loc, avPos);
				} else {
					int aLoc = a.getAdjacentPos(avPos.location, avPos.pos);
					int bLoc = b.getAdjacentPos(avPos.location, avPos.pos);
					if (aLoc != -1 && bLoc != -1 && aLoc == bLoc
							&& !isPlaced[aLoc]) {
						// Condition 1 in algorithm is met -- both parents have
						// the
						// same adjacent position
						success = true;
						isPlaced[aLoc] = true;
						place(aLoc, avPos);
					}
				}

			}

			// second phase
			// Alogrithm level 2. Its enough for one of the parents to have an
			// adjacent best buddy

			for (Iterator iterator = l.iterator(); !success
					&& iterator.hasNext();) {
				AvailablePos avPos = (AvailablePos) iterator.next();
				int aLoc = a.getAdjacentPos(avPos.location, avPos.pos);

				if (aLoc != -1 && !success && !isPlaced[aLoc]) {
					if (bestBuddy(avPos.location, aLoc, avPos.pos)) {
						success = true;
						isPlaced[aLoc] = true;
						place(aLoc, avPos);
					}
				}
				int bLoc = b.getAdjacentPos(avPos.location, avPos.pos);
				if (bLoc != -1 && !success && !isPlaced[bLoc]) {
					if (bestBuddy(avPos.location, bLoc, avPos.pos)) {
						success = true;
						isPlaced[bLoc] = true;
						place(bLoc, avPos);
					}
				}

			}

			// ELSE algorithm level 3
			// Choose best buddy without considering parents
			for (Iterator iterator = l.iterator(); !success
					&& iterator.hasNext();) {
				AvailablePos avPos = (AvailablePos) iterator.next();

				int loc = -1;
				double r = Math.random();
				if (r < generation.MutationRate) {

					int j = (int) (Math.random() * isPlaced.length);
					while (loc == -1) {
						if (isPlaced[j] == false) {
							loc = j;
						}
						j = (j == isPlaced.length - 1) ? 0 : j + 1;
					}

				} else {
					loc = getAnyBestBuddy(avPos.location, avPos.pos);
					if (loc == -1) {
						System.out.println("we have a problem");
						loc = getAnyBestBuddy(avPos.location, avPos.pos);
					}
				}
				success = true;
				isPlaced[loc] = true;
				place(loc, avPos);
			}

			if (!success) {
				System.out
						.println("algorithm could not find how to continue. Abort");
				System.exit(1);
			}

		}
		/*
		 * //to remove later for (int j = 0; j < main.p.puzzleHeight *
		 * main.p.puzzleWidth; j++) { if (!isPlaced[j]) {
		 * System.out.println("missing piece on board. Location=" + j); }
		 * 
		 * }
		 */
		chromosome son = new chromosome(board);
		return son;
	}

	private boolean bestBuddy(int aLoc, int bLoc, position pos) {
		long temp = main.p.dissimilarityInt(main.p.getPiece(aLoc),
				main.p.getPiece(bLoc), pos);
		for (int i = 0; i < main.p.puzzleHeight * main.p.puzzleWidth - 1; i++) {
			if (!isPlaced[i])
				if (main.p.dissimilarityInt(main.p.getPiece(aLoc),
						main.p.getPiece(i), pos) < temp)
					return false;
		}
		position oppositePosition = getOppositePosition(pos);

		for (int i = 0; i < main.p.puzzleHeight * main.p.puzzleWidth - 1; i++) {
			if (!isPlaced[i])
				if (main.p.dissimilarityInt(main.p.getPiece(bLoc),
						main.p.getPiece(i), oppositePosition) < temp)
					return false;
		}
		return true;
	}

	private int getAnyBestBuddy(int loc, position pos) {
		int theBest = -1;
		long theBestValue = Long.MAX_VALUE; 
		for (int i = 0; i <= main.p.puzzleHeight * main.p.puzzleWidth - 1; i++) {
			if (!isPlaced[i] && i != loc) {
				long val = main.p.dissimilarityInt(main.p.getPiece(loc),
						main.p.getPiece(i), pos);
				if (val <= theBestValue) {
					theBest = i;
					theBestValue = val;
				}
			}

		}
		return theBest;
	}

	position getOppositePosition(position pos) {
		position oppositePosition = null;
		switch (pos) {
		case right:
			oppositePosition = position.left;
			break;
		case left:
			oppositePosition = position.right;
			break;
		case up:
			oppositePosition = position.down;
			break;
		case down:
			oppositePosition = position.up;
			break;
		default:
			break;
		}
		return oppositePosition;

	}

	public void place(int loc, AvailablePos avPos) {

		int newPosWidth = 0, newPosHeight = 0;

		if (avPos.pos == position.right) {
			newPosWidth = avPos.locWidth + 1;
			newPosHeight = avPos.locHeight;
		}
		if (avPos.pos == position.left) {
			newPosWidth = avPos.locWidth - 1;
			newPosHeight = avPos.locHeight;
		}
		if (avPos.pos == position.down) {
			newPosWidth = avPos.locWidth;
			newPosHeight = avPos.locHeight + 1;
		}
		if (avPos.pos == position.up) {
			newPosWidth = avPos.locWidth;
			newPosHeight = avPos.locHeight - 1;
		}

		isPlaced[loc] = true;
		if (board[newPosWidth][newPosHeight] != -1) {
			System.out.println("overwrite on board problem");
			System.exit(1);
		}

		board[newPosWidth][newPosHeight] = loc;

		// update. correcting the Available position structure
		// First we need to understand whether there are restrictions in height
		// or width on available positions.
		// To do that, first find the lowest and highest places where elements
		// exists.
		int firstFilledWidth = -1;
		int firstFilledHeight = -1;
		int lastFilledWidth = -1;
		int lastFilledHeight = -1;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != -1) {
					if (firstFilledWidth == -1 || firstFilledWidth != -1
							&& i < firstFilledWidth)
						firstFilledWidth = i;
					if (firstFilledHeight == -1 || firstFilledHeight != -1
							&& j < firstFilledHeight)
						firstFilledHeight = j;

					if (lastFilledWidth == -1 || lastFilledWidth != -1
							&& i > lastFilledWidth)
						lastFilledWidth = i;
					if (lastFilledHeight == -1 || lastFilledHeight != -1
							&& j > lastFilledHeight)
						lastFilledHeight = j;

				}
			}
		}

		// If we reached the maximum width or height -- do not generate
		// positions in these directions.

		boolean widthRestriction = (lastFilledWidth - firstFilledWidth + 1 >= main.p.puzzleWidth);
		boolean heightRestriction = (lastFilledHeight - firstFilledHeight + 1 >= main.p.puzzleHeight);

		// System.out.println("new location: " + loc);
		// printBoard();
		// printAvailablePos();

		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			AvailablePos avPos1 = (AvailablePos) iterator.next();
			if (widthRestriction) {
				if (avPos1.locWidth == lastFilledWidth
						&& avPos1.pos == position.right)
					iterator.remove();
				if (avPos1.locWidth == firstFilledWidth
						&& avPos1.pos == position.left)
					iterator.remove();
			}
			if (heightRestriction) {
				if (avPos1.locHeight == lastFilledHeight
						&& avPos1.pos == position.down)
					iterator.remove();
				if (avPos1.locHeight == firstFilledHeight
						&& avPos1.pos == position.up)
					iterator.remove();
			}
			if (avPos1.pos == position.right
					&& avPos1.locHeight == newPosHeight
					&& avPos1.locWidth == newPosWidth - 1) {
				iterator.remove();
			}
			if (avPos1.pos == position.left && avPos1.locHeight == newPosHeight
					&& avPos1.locWidth == newPosWidth + 1) {
				iterator.remove();
			}
			if (avPos1.pos == position.up
					&& avPos1.locHeight == newPosHeight + 1
					&& avPos1.locWidth == newPosWidth) {
				iterator.remove();
			}
			if (avPos1.pos == position.down
					&& avPos1.locHeight == newPosHeight - 1
					&& avPos1.locWidth == newPosWidth) {
				iterator.remove();
			}
		}

		if (board[newPosWidth + 1][newPosHeight] == -1
				&& !(widthRestriction && newPosWidth + 1 > lastFilledWidth))
			l.add(new AvailablePos(loc, newPosWidth, newPosHeight,
					position.right));
		if (board[newPosWidth][newPosHeight + 1] == -1
				&& !(heightRestriction && newPosHeight + 1 > lastFilledHeight))
			l.add(new AvailablePos(loc, newPosWidth, newPosHeight,
					position.down));
		if (board[newPosWidth - 1][newPosHeight] == -1
				&& !(widthRestriction && newPosWidth - 1 < firstFilledWidth))
			l.add(new AvailablePos(loc, newPosWidth, newPosHeight,
					position.left));
		if (board[newPosWidth][newPosHeight - 1] == -1
				&& !(heightRestriction && newPosHeight - 1 < firstFilledHeight))
			l.add(new AvailablePos(loc, newPosWidth, newPosHeight, position.up));

		// printAvailablePos();
		// System.out.println("debug point here");

	}

	// Generate all adjacent areas to current board assignments
	private void generateAvailablePos() {
		l.clear();

		// First we need to understand whether there are restrictions in height
		// or width on available positions.
		// To do that, first find the lowest and highest places where elements
		// exists.
		int firstFilledWidth = -1;
		int firstFilledHeight = -1;
		int lastFilledWidth = -1;
		int lastFilledHeight = -1;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != -1) {
					if (firstFilledWidth == -1 || firstFilledWidth != -1
							&& i < firstFilledWidth)
						firstFilledWidth = i;
					if (firstFilledHeight == -1 || firstFilledHeight != -1
							&& j < firstFilledHeight)
						firstFilledHeight = j;

					if (lastFilledWidth == -1 || lastFilledWidth != -1
							&& i > lastFilledWidth)
						lastFilledWidth = i;
					if (lastFilledHeight == -1 || lastFilledHeight != -1
							&& j > lastFilledHeight)
						lastFilledHeight = j;

				}
			}
		}

		// If we reached the maximum width or height -- do not generate
		// positions in these directions.

		boolean widthRestriction = (lastFilledWidth - firstFilledWidth + 1 >= main.p.puzzleWidth);
		boolean heightRestriction = (lastFilledHeight - firstFilledHeight + 1 >= main.p.puzzleHeight);

		// now generate all allowed positions

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != -1) {

					if (i != board.length - 1)
						if (board[i + 1][j] == -1
								&& !(widthRestriction && i + 1 > lastFilledWidth))
							l.add(new AvailablePos(board[i][j], i, j,
									position.right));
					if (i != 0)
						if (board[i - 1][j] == -1
								&& !(widthRestriction && i - 1 < firstFilledWidth))
							l.add(new AvailablePos(board[i][j], i, j,
									position.left));

					if (j != board[i].length - 1)
						if (board[i][j + 1] == -1
								&& !(heightRestriction && j + 1 > lastFilledHeight))
							l.add(new AvailablePos(board[i][j], i, j,
									position.down));
					if (j != 0)
						if (board[i][j - 1] == -1
								&& !(heightRestriction && j - 1 < firstFilledHeight))
							l.add(new AvailablePos(board[i][j], i, j,
									position.up));

				}
			}
		}
	}

	void printAvailablePos() {
		System.out.println("available Positions:");

		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			AvailablePos avPos = (AvailablePos) iterator.next();
			avPos.print();
		}
		System.out.println("-----------------");
	}

	void printBoard() {
		System.out.println("Board:");

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[j][i] + " ");
			}
			System.out.println(" ");
		}

		System.out.println("-----------------");
	}
}
