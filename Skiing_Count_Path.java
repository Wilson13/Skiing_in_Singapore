import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Skiing_Count_Path {

  private static final String FILENAME = "map.txt";

  public static void main(String[] args) {

    int[][] matrix = getMap2dArray(FILENAME);

    if (matrix.length == 0) System.out.println("longest: " + 0);

        String longestPath = "";
        String path = "";
        System.out.println("Matrix size: " + matrix.length +  "x" + matrix[0].length);

        // Loop through every single element and seek the longest path it can obtain
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                String retPath = dfs(matrix, i, j, path);
                if (longestPath.split("-").length < retPath.split("-").length)
                  longestPath = retPath;
            }
        }
        System.out.println("longestPath: " + longestPath);
        System.out.println("longest: " + longestPath.split("-").length);
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

  private static String dfs(int[][] matrix, int i, int j, String path) {

      String longestPath = "";
      if (path.length() > 0)
        path += "-";
      path += matrix[i][j];
      longestPath = path;
        // If west is viable
        if (j-1 >= 0 && matrix[i][j-1] < matrix[i][j]) {
            String retPath = dfs(matrix, i, j-1, path);
            if (longestPath.split("-").length < retPath.split("-").length)
              longestPath = retPath;
        }
        // If north is viable
        if (i-1 >= 0 && matrix[i-1][j] < matrix[i][j]) {
          String retPath = dfs(matrix, i-1, j, path);
          if (longestPath.split("-").length < retPath.split("-").length)
            longestPath = retPath;
        }
        // If east is viable
        if (j+1 < matrix[i].length && matrix[i][j+1] < matrix[i][j]) {
          String retPath = dfs(matrix, i, j+1, path);
          if (longestPath.split("-").length < retPath.split("-").length)
            longestPath = retPath;
        }
        // If south is viable
        if (i+1 < matrix.length && matrix[i+1][j] < matrix[i][j]) {
          String retPath = dfs(matrix, i+1, j, path);
          if (longestPath.split("-").length < retPath.split("-").length)
            longestPath = retPath;
        }

        return longestPath;
    }
}
