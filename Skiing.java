import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Skiing {

	private static final String FILENAME = "Example_Input_2.txt";

	public static void main(String[] args) {

		int[][] map = getMap2dArray(FILENAME);

		// If input was valid
		if (map != null) {
			String path = "";
			String longestPath = "";
			String currentPath = "";
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[0].length; j++) {
					// Loop through every single element and seek the path
					currentPath = recursiveSeek(map, i, j, path);
					// If returned path from this element is longer than
					// the current stored path, save it as the lonest path.
					if (currentPath.length() > longestPath.length())
						longestPath = currentPath;
					// If the returned path from this element is the same length as
					// the current stored path, save the one with the more vertical drop.
					else if (currentPath.length() == longestPath.length()) {
						int currentPathDrop = Character.getNumericValue(currentPath.charAt(0)) - Character.getNumericValue(currentPath.charAt(currentPath.length()-1));
						int longestPathDrop = Character.getNumericValue(longestPath.charAt(0)) - Character.getNumericValue(longestPath.charAt(longestPath.length()-1));
						if (currentPathDrop > longestPathDrop) {
							longestPath = currentPath;
						}
					}
					//System.out.println("Current longest and largest vertical drop path: " + longestPath);
				}
			}
			System.out.println("Longest and largest vertical drop path: " + longestPath);
		} else {
			System.out.println("Input out of range!");
		}
		//Scanner reader = new Scanner(System.in);  // Reading from System.in
		//int input = reader.nextInt();
	}

	private static int[][] getMap2dArray(String fileName) {

		BufferedReader br = null;
		FileReader fr = null;
		int numLine = 0; // current number of line
		try {
			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String sCurrentLine;
			int[][] elevationArray = new int[10][10];
			while ((sCurrentLine = br.readLine()) != null) {
				// Get map size
				if (numLine == 0) {
					String[] size = sCurrentLine.split(" ");
					System.out.println("Map size: " + size[0] + "x" + size[1]);
					int numRow = Integer.parseInt(size[0]);
					int numCol = Integer.parseInt(size[1]);

					// Make sure the program is not fed with something out of the range
					if (numRow > 0 && numRow <= 1000 && numCol> 0 && numCol <= 1000)
						elevationArray = new int[numRow][numCol];
					else {
						return null;
					}
					//System.out.print("elevationArray[0].length: " + elevationArray[0].length);
					//System.out.println("elevationArray.length: " + elevationArray.length);
				} else {
					String[] splitLine = sCurrentLine.split(" ");

					// If there is one row that is not having the same width stated, it is not a valid input.
					if (splitLine.length != elevationArray[0].length) {
						System.out.println("Invalid number of columns");
						return null;
					}
					// Save elevation into 2-d array
					for (int i = 0; i < elevationArray[0].length; i++) {
						elevationArray[numLine-1][i] = Integer.parseInt(splitLine[i]);
					}
				}
				//System.out.println(sCurrentLine);
				numLine++;
			}
			// If the number of rows is not the same height stated, it is not a valid input. (no. of rows = total lines - 1)
			if (numLine-1 != elevationArray.length) {
				System.out.println("Invalid number of rows");
				return null;
			}

			for (int i = 0; i < elevationArray.length; i++) {
				for (int j = 0; j < elevationArray[0].length; j++) {
					//System.out.println("elevationArray["+i+"]["+j+"]: " + elevationArray[i][j]);
				}
			}
			return elevationArray;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
				try {

					if (br != null)
						br.close();

					if (fr != null)
						fr.close();

				} catch (IOException ex) {
					ex.printStackTrace();
				}
		}
		return null;
	}

	private static String recursiveSeek(int[][] map, int i, int j, String path) {

		//System.out.println("recursiveSeek i: "+i+"j: "+j);
		if (path.length() > 0)
			path += "->";
		path += map[i][j];
		String[] fourPaths = {"", "", "", ""};
		// West
		if (i-1 > 0) {
			//System.out.println("i-1 > 0");
			if (map[i-1][j] < map[i][j]) {
				fourPaths[0] = recursiveSeek(map, i-1, j, path);
			}
		}
		// North
		if (j-1 > 0) {
			//System.out.println("j-1 > 0");
			if (map[i][j-1] < map[i][j]) {
				fourPaths[1] = recursiveSeek(map, i, j-1, path);
			}
		}
		// East
		if (j+1 < map[i].length) {
			//System.out.println("j+1 < map[i].length");
			if (map[i][j+1] < map[i][j]) {
				fourPaths[2] = recursiveSeek(map, i, j+1, path);
			}
		}
		// South
		if (i+1 < map.length) {
			//System.out.println("i+1 < map[i].length");
			if (map[i+1][j] < map[i][j]) {
				fourPaths[3] = recursiveSeek(map, i+1, j, path);
			}
		}
		String returnPath = path;
		// Compare and return the longest and largest vertical drop path.
		for (int k = 0; k < fourPaths.length; k++) {
			//System.out.println("fourPaths["+k+"]:" + fourPaths[k].length());
			// If returned path from this element is longer than
			// the current stored path, save it as the lonest path.
			if (fourPaths[k].length() > returnPath.length())
				returnPath = fourPaths[k];
			else if (fourPaths[k].length() == returnPath.length() && returnPath.length() > 1) {
				// If the returned path from this element is the same length as
				// the current stored path, save the one with the more vertical drop.
				//System.out.println("fourPaths[k]: " + fourPaths[k] + " length: " + fourPaths[k].length());
				//System.out.println("returnPath: " + returnPath);
				int fourPathsDrop = Character.getNumericValue(fourPaths[k].charAt(0)) - Character.getNumericValue(fourPaths[k].charAt(fourPaths[k].length()-1));
				int returnPathDrop = Character.getNumericValue(returnPath.charAt(0)) - Character.getNumericValue(returnPath.charAt(returnPath.length()-1));
				if (fourPathsDrop > returnPathDrop) {
					returnPath = fourPaths[k];
				}
			}
		}
		//System.out.println("Return path: " + returnPath);
		return returnPath;
	}
}
