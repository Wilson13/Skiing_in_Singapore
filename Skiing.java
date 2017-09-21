import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Skiing {

  private static final String FILENAME = "map.txt";

  public static void main(String[] args) {

    // Obtain 2d array digital map
    int[][] map = getMap2dArray(FILENAME);

    // If input was valid
    if (map != null) {
      String path = "";
      String longestPath = "";
      String currentPath = "";
      int curLen = 0;
      int longestLen = 0;
      // Loop through every single element and seek the longest path it can obtain
      for (int i = 0; i < map.length; i++) {
        for (int j = 0; j < map[0].length; j++) {
          currentPath = recursiveSeek(map, i, j, path);

          curLen = currentPath.split("->").length;
          longestLen = longestPath.split("->").length;
          // If returned path from this element is longer than
          // the current stored path, save it as the lonest path.
          if (curLen > longestLen)
              longestPath = currentPath;

          // If the returned path from this element is the same length as
          // the current stored path, save the one with the more vertical drop.
          else if (curLen == longestLen && currentPath.length() > 0 && longestPath.length() > 0)
            longestPath = compareDrop(currentPath, longestPath);
        }
      }
      String[] longestPathChar = longestPath.split("->");
      int longestPathDrop = Integer.parseInt(longestPathChar[0]) - Integer.parseInt(longestPathChar[longestPathChar.length-1]);
      System.out.println("Longest and largest vertical drop path: " + longestPath);
      System.out.println("Length: " + longestPathChar.length);
      System.out.println("Drop: " + longestPathDrop);
    } else {
        System.out.println("Input out of range!");
    }
  }

  private static int[][] getMap2dArray(String fileName) {

    BufferedReader br = null;
    FileReader fr = null;
    int numLine = 0; // current number of line
    try {
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
          numLine++;
        }
        // If the number of rows is not the same height stated, it is not a valid input. (no. of rows = total lines - 1)
        if (numLine-1 != elevationArray.length) {
          System.out.println("Invalid number of rows");
          return null;
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

    // This recursiveSeek is a recursive DFS
    String[] fourPaths = {"", "", "", ""};
    if (path.length() > 0)
        path += "->";
    path += map[i][j];

    // If west is viable
    if (j-1 >= 0 && map[i][j-1] < map[i][j]) {
      fourPaths[0] = recursiveSeek(map, i, j-1, path);
    }
    // If north is viable
    if (i-1 >= 0 && map[i-1][j] < map[i][j]) {
      fourPaths[1] = recursiveSeek(map, i-1, j, path);
    }
    // If east is viable
    if (j+1 < map[i].length && map[i][j+1] < map[i][j]) {

      fourPaths[2] = recursiveSeek(map, i, j+1, path);
    }
    // If south is viable
    if (i+1 < map.length && map[i+1][j] < map[i][j]) {
      fourPaths[3] = recursiveSeek(map, i+1, j, path);
    }

    // Compare and return the longest and largest vertical drop path.
    for (int k = 0; k < fourPaths.length; k++) {

        int fourPathsLen = fourPaths[k].split("->").length;
        int pathLen = path.split("->").length;
        // If returned path from this element is longer than
        // the current stored path, save it as the lonest path.
        if (fourPathsLen > pathLen)
            path = fourPaths[k];

        // If the returned path from this element is the same length as
        // the current stored path, save the one with the more vertical drop.
        else if (fourPathsLen == pathLen && fourPaths[k].length() > 0 && path.length() > 0) {
          path = compareDrop(path, fourPaths[k]);
        }
    }
    return path;
  }

  private static String compareDrop(String firstPath, String secondPath) {
    String[] firstPathChar = firstPath.split("->");
    String[] secondPathChar = secondPath.split("->");
    int firstPathDrop = Integer.parseInt(firstPathChar[0]) - Integer.parseInt(firstPathChar[firstPathChar.length-1]);
    int secondPathDrop = Integer.parseInt(secondPathChar[0]) - Integer.parseInt(secondPathChar[secondPathChar.length-1]);
      if (firstPathDrop > secondPathDrop)
        return firstPath;
      else
        return secondPath;
  }
}
