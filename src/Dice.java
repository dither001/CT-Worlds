/********************************************************
* Author: Nick Foster
*
* Last edited: 10/14/2017
********************************************************/

import java.util.Random;

public class Dice {
   // fields
   private static final Random rand = new Random();
   
   // methods
   public static int deeRoll(int num, int sides) {
      int n = 0;
      for (int i = 1; i <= num; ++i) {
         n += deeFace(sides);
      }
      return n;
   }
   
   public static int deeFace(int sides) {
      return rand.nextInt(sides) + 1;
   }
}