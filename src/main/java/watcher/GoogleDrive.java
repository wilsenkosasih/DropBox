package watcher;

import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GoogleDrive {
	public static boolean deleteFile(Drive service, Path filename) throws GeneralSecurityException, IOException {
		List<File> files = getAllFiles(service);

		if (files == null || files.isEmpty()) {
			System.out.println("No files found.");
			return false;
		}

		String fileId = findFileId(files, filename);
		if (fileId != "") {
			service.files().delete(fileId).execute();
			return true;
		}
		return false;
	}

	public static boolean uploadFile(Drive service, Path filename) throws IOException, GeneralSecurityException {
		File fileMetadata = new File();
		fileMetadata.setName(filename.toString());

		java.io.File filePath = new java.io.File("./driveFolder/" + filename.toString());
		FileContent mediaContent = new FileContent("*/*", filePath);
		File file = service.files().create(fileMetadata, mediaContent).setFields("id").execute();

		System.out.println("File ID: " + file.getId());

		return true;
	}

	public static boolean modifyFile(Drive service, Path filename) throws IOException, GeneralSecurityException {
		List<File> files = getAllFiles(service);
		String fileId = findFileId(files, filename);
		if (fileId == "")
			return false;

		File newfile = new File();
		newfile.setName(filename.toString());

		java.io.File filePath = new java.io.File("./driveFolder/" + filename);
		FileContent mediaContent = new FileContent("*/*", filePath);
		File file = service.files().update(fileId, newfile, mediaContent).execute();

		System.out.println("File ID: " + file.getId());

		return true;

	}

	public static String findFileId(List<File> files, Path filename) {
		String fileId = "";
		for (File file : files) {
			if (filename.toString().equals(file.getName())) {
				fileId = file.getId();
				break;
			}
		}
		return fileId;
	}

	public static List<File> getAllFiles(Drive service) throws IOException {
		FileList result = service.files().list().execute();
		List<File> files = result.getFiles();

		return files;
	}
}
