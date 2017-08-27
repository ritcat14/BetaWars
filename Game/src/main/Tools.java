package main;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public abstract class Tools {
	
	public static int RandomNumber(int min, int max) {
		Random r = new Random(1202348283);
		return r.nextInt((max - min) + 1) + min;
	}
	
	public static int getBaseNum(int mainWidth, int mainHeight, int width, int height) {
		/* Map rules: 
		 * - At least 5 helibases, max 10
		 * - At least 3 Airbases, max 6
		 * - Rest are normal bases, random type
		 * */
		
		int maxBases = (mainWidth / width) * (mainHeight / height);
		
		int helibases = RandomNumber(5, 10);
		int airbases = RandomNumber(3, 6);
		int remainder = maxBases - (helibases + airbases);
		int type1 = RandomNumber(remainder/4, remainder/2);
		int type2 = RandomNumber(remainder/4, remainder/2);		
		
		return helibases + airbases + type1 + type2;
	}
	
	public static BufferedImage[] splitImage(String imageFile, int num, int width) {
		BufferedImage[] images = new BufferedImage[num];
		BufferedImage image = Tools.getImage(imageFile);
		for (int i = 0; i < images.length; i++) {
			images[i] = image.getSubimage(i * width, 0, width, image.getHeight());
        }
		return images;
	}
	
	public static BufferedImage[] splitImage(BufferedImage image, int num, int width) {
		BufferedImage[] images = new BufferedImage[num];
		for (int i = 0; i < images.length; i++) {
			images[i] = image.getSubimage(i * width, 0, width, image.getHeight());
        }
		return images;
	}
	
	public static BufferedImage getImage(String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(Main.class.getResourceAsStream(path + ".png"));
		} catch (Exception e) {
			System.err.println("Cannot read file: " + path);
			System.err.println(Thread.currentThread().getName() + ":");
			e.printStackTrace();
		}
		return image;
	}
	
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
	    File file = new File(Main.class.getResource(path).getFile());
	    List<String> lines = null;
	    try {
	        lines = Files.readAllLines(file.toPath());
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    return lines.toArray(new String[lines.size()]);
	}
	
	public static void writeToFile(String file, String[] data) {
		try(  PrintWriter out = new PrintWriter(file)) {
			for (String s : data) {
				out.println(s);
			}
		} catch (Exception e) { e.printStackTrace(); }
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
			theDir.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean exists(String path) {
		return new File(path).exists();
	}
	
	public static String createSQLFields(String[] data, String sep, String type) {
		String sql = "";
		for (String s : data) {
			sql += s + " " + type + sep + " ";
		}
		return sql;
	}
	
	public static <T> T[] joinArrays(T[] a, T[] b) {
	    int aLen = a.length;
	    int bLen = b.length;

	    @SuppressWarnings("unchecked")
	    T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen+bLen);
	    System.arraycopy(a, 0, c, 0, aLen);
	    System.arraycopy(b, 0, c, aLen, bLen);

	    return c;
	}
	
	public static Color hexToRgb(String colorStr) {
	    return new Color(
	            Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
	            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
	            Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
	}
	
}
