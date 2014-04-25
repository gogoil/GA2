
public class RunThread  implements Runnable {
	private int from;
	private int to;
	private int threadNum;
	private generation gOld;
	private generation gNew;

	  public RunThread(int from, int to, generation gOld, generation gNew, int threadNum) {
		super();
		this.from = from;
		this.to = to;
		this.gOld = gOld;
		this.gNew = gNew;
		this.threadNum = threadNum;
	}

	public void run() {

	for (int i = from; i < to; i ++) {
		if (i % 10 == 0 ) 
			System.out.println("[ thread# " + threadNum + "] > "+ i);


		// Selection operator
		int first = gOld.selectChromosome(Math.random());
		int second = gOld.selectChromosome(Math.random());

		// Crossover
		
		crossover cro = new crossover(gOld.getC()[first],gOld.getC()[second]);
		gNew.c[i] = cro.generateSon();
		gNew.c[i].evaluate();

	}
}
}
