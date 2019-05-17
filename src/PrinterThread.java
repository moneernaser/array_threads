public class PrinterThread implements Runnable {

	private final int[] values;
	private int round;

	PrinterThread(int[] values) {
		this.values = values;
		this.round = 0;
	}

	@Override
	public void run() {
		System.out.println();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("round " + ++round);
		for (int i = 0; i < values.length; i++) {
			System.out.print(values[i] + ", ");
		}
	}
}
