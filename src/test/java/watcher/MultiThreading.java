package watcher;

public class MultiThreading extends Thread {

	public void run() {
		try {
			DirectoryWatcher.watchDirectory();
		} catch (Exception e) {
			System.out.println("Loop Stopped");
			System.out.println("Integration Test Done");
		}
	}
}
