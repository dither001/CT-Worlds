
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class GenSubsector {
   // fields
   private static Date myDate = new Date();
   private static PrintWriter myWriter = null;
   
//   private static DiceRoller dice = new DiceRoller();
//   private static GenWorld[] worlds = new GenWorld[80];
   private static int counter = 0;
   
   private static String[] header = {
      "hex",
      "size", "atmo", "temp", "hydro", "pop", "gov", "law",
      "travel", "tech", "port", "fee", 
      "Ag", "As", "Ba", "De", "Fl", "Ga", "Hi", "Ht", "IC",
      "In", "Lo", "Lt", "NA", "NI", "Po", "Ri", "Va", "Wa",
      "Dominant Gov't",
      "temp2", "hydro2", "atmo2",
      "fac1S", "fac1T", "fac2S", "fac2T",
      "fac3S", "fac3T", "fac4S", "fac4T",
      "giant",
      "navy", "scout", "lab", "consul", "TAS", "pirate"
   };
   
   // methods
   public static void main(String[] args) { //throws IOException {
//       try {
//          myWriter = new PrintWriter(myDate.getTime() + ".csv");
//       } catch (IOException error) {
//          error.printStackTrace();
//       }
      
      // fills array with worlds -or- null
//       for (int i = 0; i < worlds.length; ++i) {
//          worlds[i] = (dice.deeRoll(1,6) > 3) ? new GenWorld() : null;
//       }
      
      //printSubsector();
      
      
//       StarSystem mySystem = new StarSystem();
//       while (! (mySystem.isInteresting()) ) {
//          mySystem = new StarSystem();
//       }
//       System.out.println(mySystem.toString());
      
      
      
      
      
      for (int i = 0; i < 1; ++i) {
         StarSystem mySystem = new StarSystem();
         while (! (mySystem.hasMainWorld())) {
            mySystem = new StarSystem();
         }
         System.out.println(mySystem.toString());
         System.out.println();
      }
      
   }
   
   public static void printSubsector() {
      for (int i = 0; i < header.length; ++i) {
         myWriter.printf("%s,", header[i]);
      }
      myWriter.printf("%n");
      
      for (int i = 1; i <= 8; ++i) {
         for (int j = 1; j <= 10; ++j) {
            if (j < 10)
               myWriter.printf("'0%d0%d,", i, j);
            else
               myWriter.printf("'0%d%d,", i, j);
            // rest of world
//            if (worlds[counter] != null)
//               printWorld();
            
            myWriter.printf("%n");
            ++counter;
         }
      }
      
      myWriter.close();
   }   
   
//   public static void printWorld() {
//      // scores
//      for (int i = 0; i < worlds[counter].scores.length; ++i) {
//         myWriter.printf("%d,", worlds[counter].scores[i]);
//      }
//      
//      // travel code, tech level, port class, port fee
//      myWriter.printf("%s,", worlds[counter].travelCode);
//      myWriter.printf("%d,", worlds[counter].techLevel);
//      myWriter.printf("%c,", worlds[counter].portClass);
//      myWriter.printf("(%d),", worlds[counter].portFee);
//      
//      // trade codes
//      for (int i = 0; i < worlds[counter].tradeCodes.length; ++i) {
//         myWriter.printf("%s,", worlds[counter].tradeCodes[i]);
//      }
//      
//      // world types, government, and factions
//      myWriter.printf("%s,", worlds[counter].govType);
//      myWriter.printf("%s,", worlds[counter].tempType);
//      myWriter.printf("%s,", worlds[counter].hydroType);
//      myWriter.printf("%s,", worlds[counter].atmoType);
//      
//      for (int i = 0; i < worlds[counter].factionGov.length; ++i) {
//         myWriter.printf("%s,", worlds[counter].factionStr[i]);
//         myWriter.printf("%s,", worlds[counter].factionDescrip[i]);
//      }
//      
//      // gas giant and bases
//      myWriter.printf("%s,", worlds[counter].gasGiant);
//      for (int i = 0; i < worlds[counter].bases.length; ++i) {
//         myWriter.printf("%s,", worlds[counter].bases[i]);
//      }
//      
//      // end of world
//   }
}