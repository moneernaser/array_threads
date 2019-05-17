import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

	public static final int MAX_RANDOM_NUM = 100;
	public static final int DEFAULT_ROUNDS = 20;
	public static final int DEFAULT_THREADS = 20;

	public static void main(String[] args) {
		int nThreads = DEFAULT_THREADS, rounds=DEFAULT_ROUNDS;
		if (args.length > 0) {
			nThreads = Integer.parseInt(args[0]);
			rounds = Integer.parseInt(args[1]);
		}
    	int[] values = new int[nThreads];
    	CyclicBarrier iterationLock = new CyclicBarrier(nThreads, new PrinterThread(values));
		CycleThread[] cycleThreads = new CycleThread[nThreads];
		for (int i = 0; i < nThreads; i++) {
			int randomNum = ThreadLocalRandom.current().nextInt(1, MAX_RANDOM_NUM + 1);
			cycleThreads[i] = new CycleThread(randomNum, i, rounds, cycleThreads, iterationLock, values);
		}
		for (Thread thread : cycleThreads) {
			thread.start();
		}
    }


}
