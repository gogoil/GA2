public class piece {
	private int pixelRightBorder[];
	private int pixelLeftBorder[];
	private int pixelDownBorder[];
	private int pixelUpBorder[];
	private puzzle p;

	// NumN = the location of the piece in the X axis
	// NumM = the location of the piece in the Y axis
	public piece(puzzle pzl, int NumWidth  /* width */, int NumN) {
		p = pzl;
		this.pixelRightBorder = new int[p.pieceHeight];
		this.pixelDownBorder = new int[p.pieceWidth];
		this.pixelLeftBorder = new int[p.pieceHeight];
		this.pixelUpBorder = new int[p.pieceWidth];
		for (int i = 0; i < p.pieceWidth; i++) {
			pixelDownBorder[i] = p.img.getRGB(p.pieceWidth * NumWidth + i,
					p.pieceHeight * (NumN +1 ) -1);
			pixelUpBorder[i] = p.img.getRGB(p.pieceWidth * NumWidth + i,
					p.pieceHeight * NumN);
		}
		for (int i = 0; i < p.pieceHeight; i++) {
			pixelRightBorder[i] = p.img.getRGB(p.pieceWidth * (NumWidth +1) -1,
					p.pieceHeight * NumN + i);
			pixelLeftBorder[i] = p.img.getRGB(p.pieceWidth * (NumWidth),
					p.pieceHeight * NumN + i);
		}
//		System.out.println(RGB.GetRed(pixelLeftBorder[0]));
	}

	public int[] getPixelRightBorder() {
		return pixelRightBorder;
	}

	public void setPixelRightBorder(int[] pixelRightBorder) {
		this.pixelRightBorder = pixelRightBorder;
	}

	public int[] getPixelLeftBorder() {
		return pixelLeftBorder;
	}

	public void setPixelLeftBorder(int[] pixelLeftBorder) {
		this.pixelLeftBorder = pixelLeftBorder;
	}

	public int[] getPixelDownBorder() {
		return pixelDownBorder;
	}

	public void setPixelDownBorder(int[] pixelDownBorder) {
		this.pixelDownBorder = pixelDownBorder;
	}

	public int[] getPixelUpBorder() {
		return pixelUpBorder;
	}

	public void setPixelUpBorder(int[] pixelUpBorder) {
		this.pixelUpBorder = pixelUpBorder;
	}

}
