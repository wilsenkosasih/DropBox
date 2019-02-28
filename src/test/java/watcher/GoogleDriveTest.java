package watcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import org.powermock.modules.junit4.PowerMockRunner;
//import org.powermock.api.easymock.PowerMock;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "javax.net.ssl.*", "javax.security.*" })

public class GoogleDriveTest {
	@PrepareForTest(File.class)
	@Test
	public void uploadFileUnitTest() throws IOException, GeneralSecurityException {
    	
		Drive servicemock = mock(Drive.class);
		Drive.Files mock2 = mock(Drive.Files.class);
		Drive.Files.Create mock3 = mock(Drive.Files.Create.class);
		Drive.Files.Create mock4 = mock(Drive.Files.Create.class);		
		File mock5 = mock(File.class);
		
		when(servicemock.files()).thenReturn(mock2);
		when(mock2.create(any(File.class),any(FileContent.class))).thenReturn(mock3);
		when(mock3.setFields(eq("id"))).thenReturn(mock4);
		when(mock4.execute()).thenReturn(mock5);
		
		Path path = Paths.get("a.txt");
		
		assertTrue(GoogleDrive.uploadFile(servicemock, path));
		verify(servicemock, times(1)).files();
	}
		
	@PrepareForTest(FileList.class)
	@Test
	public void deleteFileUnitTest() throws IOException, GeneralSecurityException {
		
		Drive servicemock = mock(Drive.class);
		Drive.Files mock2 = mock(Drive.Files.class);
		Drive.Files.List mock3 = mock(Drive.Files.List.class);
		FileList mock4 = mock(FileList.class);

		when(servicemock.files()).thenReturn(mock2);
		when(mock2.list()).thenReturn(mock3);
		when(mock3.execute()).thenReturn(mock4);
		
		Path path = Paths.get("a.txt");
		assertFalse(GoogleDrive.deleteFile(servicemock, path));
		verify(servicemock, times(1)).files();		
	}
	
	@PrepareForTest(FileList.class)
	@Test
	public void modifyFileUnitTest() throws IOException, GeneralSecurityException {
		/*
		Drive servicemock = mock(Drive.class);
		Drive.Files mock2 = mock(Drive.Files.class);
		Drive.Files.List mock3 = mock(Drive.Files.List.class);
		FileList mock4 = mock(FileList.class);

		when(servicemock.files()).thenReturn(mock2);
		when(mock2.list()).thenReturn(mock3);
		when(mock3.execute()).thenReturn(mock4);
		
		Path path = Paths.get("a.txt");
		assertFalse(GoogleDrive.deleteFile(servicemock, path));
		verify(servicemock, times(1)).files();	
		*/	
	}
	
	@Test
	public void findFileIdTest()  {		
		java.util.List<File> files = new ArrayList<File>();
				
		Path path = Paths.get("a.txt");
		assertEquals(GoogleDrive.findFileId(files, path), "");		
	}
	
	@PrepareForTest(FileList.class)
	@Test
	public void getAllFilesTest() throws IOException {
		Drive servicemock = mock(Drive.class);
		Drive.Files mock2 = mock(Drive.Files.class);
		Drive.Files.List mock3 = mock(Drive.Files.List.class);
		FileList mock4 = mock(FileList.class);

		when(servicemock.files()).thenReturn(mock2);
		when(mock2.list()).thenReturn(mock3);
		when(mock3.execute()).thenReturn(mock4);
		
		assertEquals(GoogleDrive.getAllFiles(servicemock), Collections.emptyList());

	}
	
	@Test
	public void uploadFileIntegrationTest() throws IOException, GeneralSecurityException {
		String absoluteFilePath = "./driveFolder/upload.txt";
        java.io.File file = new java.io.File(absoluteFilePath);
        file.createNewFile();    	

    	String APPLICATION_NAME = "Google Drive API Java Quickstart";
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    	final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, DirectoryWatcher.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    	
		Path path = Paths.get("upload.txt");
		assertTrue(GoogleDrive.uploadFile(service, path));
		assertFalse(GoogleDrive.findFileId(GoogleDrive.getAllFiles(service), path).equals(""));
		
		file.delete();
		GoogleDrive.deleteFile(service, path);
	}
	
	@Test (expected = FileNotFoundException.class)
	public void uploadFileFailIntegrationTest() throws IOException, GeneralSecurityException {	
    	String APPLICATION_NAME = "Google Drive API Java Quickstart";
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    	final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, DirectoryWatcher.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    	
		Path path = Paths.get("nonexistent.txt");
		assertFalse(GoogleDrive.uploadFile(service, path));
		assertEquals(GoogleDrive.findFileId(GoogleDrive.getAllFiles(service), path),"");
	}
	
	@Test 
	public void deleteFileIntegrationTest() throws GeneralSecurityException, IOException  {
		String APPLICATION_NAME = "Google Drive API Java Quickstart";
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, DirectoryWatcher.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        
        String absoluteFilePath = "./driveFolder/delete.txt";
        java.io.File file = new java.io.File(absoluteFilePath);
        file.createNewFile();  
        Path path = Paths.get("delete.txt");
		GoogleDrive.uploadFile(service, path);
		
		assertTrue(GoogleDrive.deleteFile(service, path));
		assertEquals(GoogleDrive.findFileId(GoogleDrive.getAllFiles(service), path),"");

		file.delete();
        
	}
	
	@Test
	public void deleteFileFailIntegrationTest() throws GeneralSecurityException, IOException  {
		String APPLICATION_NAME = "Google Drive API Java Quickstart";
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, DirectoryWatcher.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        
        Path path = Paths.get("nonexistent.txt");
		
		assertFalse(GoogleDrive.deleteFile(service, path));        
	}
	
	@Test
	public void modifyFileIntegrationTest() throws GeneralSecurityException, IOException  {
		String APPLICATION_NAME = "Google Drive API Java Quickstart";
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, DirectoryWatcher.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        
        String absoluteFilePath = "./driveFolder/modify.txt";
        java.io.File file = new java.io.File(absoluteFilePath);
        file.createNewFile();  
        Path path = Paths.get("modify.txt");
		GoogleDrive.uploadFile(service, path);
		
		assertTrue(GoogleDrive.modifyFile(service, path));
		assertFalse(GoogleDrive.findFileId(GoogleDrive.getAllFiles(service), path).equals(""));
		
		file.delete();
		GoogleDrive.deleteFile(service, path);   
	}
	
	@Test
	public void modifyFileFailIntegrationTest() throws GeneralSecurityException, IOException  {
		String APPLICATION_NAME = "Google Drive API Java Quickstart";
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, DirectoryWatcher.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        
        Path path = Paths.get("nonexistent.txt");
		
		assertFalse(GoogleDrive.modifyFile(service, path));
		assertEquals(GoogleDrive.findFileId(GoogleDrive.getAllFiles(service), path),"");
	}
}
