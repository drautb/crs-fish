package io.github.drautb.crs.fish;

public class FishStore {

  private static String currentFishColor = "blue";

  public static void changeColor(String newColor) {
    currentFishColor = newColor;
  }

  public static String getColor() {
    return currentFishColor;
  }

}
