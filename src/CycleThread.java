import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CycleThread extends Thread {

	private final int iterations;
	private int randomNum;
	private int position;
	private CycleThread[] cycleThreads;
	private boolean checkedFromLeft = false;
	private boolean checkedFromRight = false;
	private CyclicBarrier iterationLock;
	private final Object checkLock = new Object();
	private int[] values;



	public CycleThread(int randomNum, int position, int iterations, CycleThread[] cycleThreads, CyclicBarrier iterationLock, int[] values) {
		this.randomNum = randomNum;
		this.iterations = iterations;
		this.position = position;
		this.cycleThreads = cycleThreads;
		this.iterationLock = iterationLock;
		this.values = values;
	}

	public int getNumber() {
		return randomNum;

	}


	@Override
	public void run() {

		for (int i = 0; i < iterations; i++) {
			CycleThread leftThread = cycleThreads[(position - 1 + cycleThreads.length) % cycleThreads.length];
			CycleThread rightThread = cycleThreads[(position + 1 + cycleThreads.length) % cycleThreads.length];
			int myLeftNeighbourNumber = leftThread.getNumber();
			int myRightNeighbourNumber = rightThread.getNumber();
			synchronized (rightThread.checkLock) {
				rightThread.checkedFromLeft = true;
				rightThread.checkLock.notify();
			}
			synchronized (leftThread.checkLock) {
				leftThread.checkedFromRight = true;
				leftThread.checkLock.notify();
			}

			while (!checkedFromLeft || !checkedFromRight) {
				synchronized (checkLock) {
					try {
						checkLock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if (randomNum < myLeftNeighbourNumber && randomNum < myRightNeighbourNumber) {
				randomNum = randomNum + 1;
			} else if (randomNum > myLeftNeighbourNumber && randomNum > myRightNeighbourNumber) {
				randomNum = randomNum - 1;
			} // else -- if between: do nothing

			values[position] = randomNum;
			try {
				iterationLock.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public String toString() {
		return "(t" + position+")";
	}
}