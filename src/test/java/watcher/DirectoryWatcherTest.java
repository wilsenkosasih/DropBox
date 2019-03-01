package watcher;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

public class DirectoryWatcherTest {

	@PrepareForTest(FileList.class)
	@Test
	public void testWatchDirectory() throws IOException, GeneralSecurityException {
		
	}
	
	@Test
	public void testWatchDirectoryIntegration() throws IOException, GeneralSecurityException, InterruptedException {
		String APPLICATION_NAME = "Google Drive API Java Quickstart";
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    	final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, DirectoryWatcher.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        
		MultiThreading object = new MultiThreading();
		object.start();
		
		Thread.sleep(5*1000);
		String absoluteFilePath = "./driveFolder/integrate.txt";
		Path path = Paths.get("integrate.txt");
        java.io.File file = new java.io.File(absoluteFilePath);
        file.createNewFile(); 	
		        
		Thread.sleep(10*1000);
		assertFalse(GoogleDrive.findFileId(GoogleDrive.getAllFiles(service), path).equals(""));

		com.google.common.io.Files.touch(file);
		Thread.sleep(10*1000);
		assertFalse(GoogleDrive.findFileId(GoogleDrive.getAllFiles(service), path).equals(""));

		file.delete();
		Thread.sleep(10*1000);
		assertTrue(GoogleDrive.findFileId(GoogleDrive.getAllFiles(service), path).equals(""));

		object.stop();
	}

}
