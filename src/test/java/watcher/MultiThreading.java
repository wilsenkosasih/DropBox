package watcher;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

public class MultiThreading extends Thread{
	private int threadid;
	
	public MultiThreading(int i) {
		threadid = i;
	}
	
	public void run () {
		if(this.threadid == 1) {
				try {
					System.out.println("got here");
					long start = System.currentTimeMillis();
					long end = start + 10*1000; // 10 seconds * 1000 ms/sec
					/*while (System.currentTimeMillis() < end)
					{*/
						System.out.println("test");
						DirectoryWatcher.watchDirectory();
						System.out.println("test2");
					//}
					
					System.out.println("got here already");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GeneralSecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		} else if (this.threadid == 2) {
			
			System.out.println("got here 2");
			String absoluteFilePath = "./driveFolder/modify.txt";
	        java.io.File file = new java.io.File(absoluteFilePath);
	        try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			System.out.println("got here 3");

			//Create file
			//Delete file
			//Modify file
		} else {
			System.out.println("dont get here");
		}
	}
	
}
