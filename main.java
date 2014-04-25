import java.util.Random;

public class main {
	public static Random generator = new Random(System.currentTimeMillis());
	public static int NoOfGenerations = 100;
	public static puzzle p;
	/** 
	 * @param args
	 */
	public static void main(String[] args) {

		p = new puzzle ("file:///C:/Users/YARONGO/Downloads/GA v2/IMG_3202.jpg", 30, 30);
	
//		p = new puzzle ("file:///C:/Users/yarongo/Downloads/GA v2/2013-08-05 08.24.13.jpg", 25, 25);
//		p = new puzzle ("file:///C:/Users/yarongo/Downloads/GA v2/2013-07-31 11.14.24.jpg", 20, 20);

	

		chromosome winner = new chromosome();
		winner.initAsBest();
		String fileName = String.format("original picture");
		winner.print("pictures/" + fileName + ".jpg");
		
		
		generation a = new generation();
		
		for (int i = 0; i < NoOfGenerations; i++) {
			chromosome bestFit = a.getBestChromosome();
			String fileName1 = String.format("best-of-generation %d. Fitness=%.3f", i, bestFit.getFitness()/((double)winner.getFitness()));
			System.out.println(fileName1);			
			bestFit.print("pictures/" + fileName1 + ".jpg");
			
			if (bestFit.getFitness() == winner.getFitness())
				exit(0);
			
			try {
			a = new generation(a);
			} catch(InterruptedException e )
			{
				System.out.println(e.getMessage());
			}
		}

	}
	private static void exit(int i) {
		
	}
}
