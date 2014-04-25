public class generation {
	public chromosome[] c;
	private double Gfitness;// סכום ערכי הכרומוזמים בכל הדור
	public static double MutationRate = 0.0001;
	public static int GenerationSize = 500;
	public static int ElitismRate = 1;
	public static int numOfThreads = 4;

	public generation() {
		this.c = new chromosome[GenerationSize];
		for (int i = 0; i < GenerationSize; i++) {
			this.c[i] = new chromosome();
			this.c[i].initAsRandom();
		}
		initialize();
	}

	// Calculate fitness and normalized fitness for the entire generation
	public void initialize() {
		for (int i = 0; i < GenerationSize; i++) {
			this.c[i].setFitness(this.c[i].evaluate());
			this.Gfitness += c[i].getFitness();
		}
		setNormalizedFitness();
		setAccomulatedNormalFitness();
	}

	// Construct this generation from the previous generation
	public generation(generation g) throws InterruptedException {

		this.c = new chromosome[GenerationSize];
		for (int i = 0; i < ElitismRate; i++) {
			c[i] = g.getBestChromosome();
		}
		
		Thread thArr[] = new Thread[numOfThreads];
	//	RunThread r[] = new RunThread[numOfThreads];
		
		int from = ElitismRate;
		int chromosomesPerThread = GenerationSize/numOfThreads;
		for (int j=0;j<numOfThreads; j++)
		{
			Thread t = new Thread(new RunThread(from, from + chromosomesPerThread, g, this, j));
			from= chromosomesPerThread*(j+1);
			thArr[j] = t;
			t.start();
		}
		for (int j=0;j<numOfThreads; j++) {
			thArr[j].join();
		}
	// Calculate fitness for the new generation
	initialize();

}
		
			
			
//				
//		}
//
//		RunThread r1 = new RunThread(ElitismRate, GenerationSize / 4, g);
//		RunThread r2 = new RunThread(GenerationSize / 4, GenerationSize / 2, g);
//		RunThread r3 = new RunThread(GenerationSize / 2,
//				GenerationSize / 4 * 3, g);
//		RunThread r4 = new RunThread(GenerationSize / 4 * 3, GenerationSize, g);
//
//		Thread t1 = new Thread(r1);
//		t1.start();
//		Thread t2 = new Thread(r2);
//		t2.start();
//		Thread t3 = new Thread(r3);
//		t3.start();
//		Thread t4 = new Thread(r4);
//		t4.start();
//		t1.join();
//		t2.join();
//		t3.join();
//		t4.join();
		
		// for (int i = ElitismRate; i < GenerationSize; i ++) {
		// if (i % 10 == 0 ) System.out.println("> "+i);

		// Selection operator
		// int first = g.selectChromosome(Math.random());
		// int second = g.selectChromosome(Math.random());

		// Crossover

		// crossover cro = new crossover(g.getC()[first],g.getC()[second]);
		// c[i] = cro.generateSon();
		// c[i].evaluate();

		// }


	public void setNormalizedFitness() {
		for (int i = 0; i < GenerationSize; i++) {
			double temp = (double) this.c[i].getFitness() / this.Gfitness;
			this.c[i].setNormalizedFitness(temp);
		}
	}

	public void setAccomulatedNormalFitness() {// ייצירת ציר כרומוזומים בו כל
												// כרומוזום לוקח מקום
		// לפי ערכו.
		double position = 0;
		for (int i = 0; i < GenerationSize; i++) {
			position += c[i].getNormalizedFitness();
			c[i].setAccomulatedNormalFitness(position);
		}
	}

	public int selectChromosome(double a)// ימצא את הכרומוזום לפי מיקומו על הציר
	{
		int count = 0;
		while (a > c[count].getAccomulatedNormalFitness()) {
			count++;
		}
		return count;
	}

	public chromosome[] getC() {
		return this.c;
	}

	public void setC(chromosome[] c) {
		this.c = c;
	}

	public String toString() {
		String s = new String();
		for (int i = 0; i < GenerationSize; i++)
			// printing
			s += c[i] + " fitness:" + c[i].getFitness()
					+ "  normalizedFitness:" + c[i].getNormalizedFitness()
					+ "  accNormalFitness:"
					+ c[i].getAccomulatedNormalFitness() + "\n";
		return s;
	}

	public float avgFitness() {
		float t = 0;
		for (int i = 0; i < GenerationSize; i++)
			// printing
			t += c[i].getFitness();
		return t / GenerationSize;

	}

	public chromosome getBestChromosome() {
		int bestFit = -1;
		long bestFitness = 0;
		for (int i = 0; i < GenerationSize; i++) {
			if (c[i].getFitness() > bestFitness) {
				bestFit = i;
				bestFitness = c[bestFit].getFitness();
			}
		}
		return c[bestFit];
	}
}
