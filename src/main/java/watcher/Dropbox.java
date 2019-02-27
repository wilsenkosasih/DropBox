package watcher;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Dropbox {
	public static void main( String[] args ) throws IOException, GeneralSecurityException
    {    	
    	DirectoryWatcher directoryWatcher = new DirectoryWatcher();
    	directoryWatcher.watchDirectory();   	    	
    }
		
}
