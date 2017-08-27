
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public abstract class Tools {
	
	public static String[] loadFromFile(String path) {
	    File file = new File(path);
	    List<String> lines = null;
	    try {
	        lines = Files.readAllLines(file.toPath());
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    return lines.toArray(new String[lines.size()]);
	}
	
	public static String[] loadFromResource(String path) {
	    File file = new File(DatabaseManager.class.getResource(path).getFile());
	    List<String> lines = null;
	    try {
	        lines = Files.readAllLines(file.toPath());
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    return lines.toArray(new String[lines.size()]);
	}
	
	public static int[] StringToIntArray(String[] stringArray) {
		int[] intArray = new int[stringArray.length];
		for (int i = 0; i < stringArray.length; i++) {
			intArray[i] = Integer.parseInt(stringArray[i]);
		}
		return intArray;
	}
	
	public static String StringArrayToString(String[] strings, String sep) {
		String s = "";
		if (strings.length == 1) return strings[0];
		for (int i = 0; i < strings.length - 1; i++) {
			s += (strings[i] + sep);
		}
		s += strings[strings.length - 1];
		return s;
	}
	
	public static void createFolder(String path) {
		File theDir = new File(path);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
		    System.out.println("creating directory: " + theDir.getName());
		    boolean result = false;

		    try{
		        theDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        //handle it
		    }        
		    if(result) {    
		        System.out.println("DIR created");  
		    }
		}
	}
	
	public static void createFile(String path) {
		File theDir = new File(path);
		theDir.getParentFile().mkdirs();
		try {
			new FileOutputStream(path, true).close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean exists(String path) {
		return new File(path).exists();
	}
	
	public static String createSQLFields2(String[] data, String sep, String type) {
		String sql = "";
		for (int i = 0; i < data.length - 1; i++) {
			String s = data[i];
			sql += s + " " + type + sep + " ";
		}
		sql += data[data.length - 1] + " " + type;
		return sql;
	}
	
	public static String createSQLFields(String[] data, String sep, String type) {
		String sql = "";
		for (String s : data) {
			sql += s + " " + type + sep + " ";
		}
		return sql;
	}
	
}
