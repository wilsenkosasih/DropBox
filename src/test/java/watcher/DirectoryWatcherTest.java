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

public class DirectoryWatcherTest {

	@PrepareForTest(FileList.class)
	@Test
	public void watchDirectoryUnitTest() throws IOException, GeneralSecurityException {
		/*GoogleDrive googleDriveMock = mock(GoogleDrive.class);
		
		when(googleDriveMock.deleteFile(any(Drive.class), any(Path.class))).thenReturn(true);
		when(googleDriveMock.modifyFile(any(Drive.class), any(Path.class))).thenReturn(true);
		when(googleDriveMock.uploadFile(any(Drive.class), any(Path.class))).thenReturn(true);

		long start = System.currentTimeMillis();
		long end = start + 10*1000; // 60 seconds * 1000 ms/sec
		while (System.currentTimeMillis() < end)
		{
		    DirectoryWatcher.watchDirectory();
		}*/
	}
	
	@Test
	public void watchDirectoryIntegrationTest() throws IOException, GeneralSecurityException {
		/*for(int i = 1; i <= 2; i++) {
			MultiThreading object = new MultiThreading(i);
			object.start();
		}*/
	}

}
