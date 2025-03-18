//assemblerApplication.java
package assemblerPackage;
import assemblerPackage.HelperFunctions;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

//Author: Sabrina Ferras
//Description: open .asm file, line by line convert to .hack
//Date: 3.17.2025

public class assemblerApplication {

	public static void main(String[] args) {
		HelperFunctions functions = new HelperFunctions();
        Scanner scanner = new Scanner(System.in);
		System.out.print("Enter file path: ");
        String path = scanner.nextLine();
		System.out.print("Enter file name: ");
        String name = scanner.nextLine();

        //String inputFile = "C:\\Users\\Sabrina Ferras\\OneDrive - csumb.edu\\Documents\\CPP\\Spring2025\\CS 5250 - Adv Comp Architecture\\software\\nand2tetris\\nand2tetris\\projects\\6\\rect\\RectL.asm";
        //String outputFile = "C:\\Users\\Sabrina Ferras\\OneDrive - csumb.edu\\Documents\\CPP\\Spring2025\\CS 5250 - Adv Comp Architecture\\software\\nand2tetris\\nand2tetris\\projects\\6\\rect\\RectL1.hack";
        String inputFile = path + "\\" + name + ".asm";
        String outputFile = path + "\\" + name + "1.hack";
        for(int i = 0; i < 2; i++) {
        	
	    try {
	        File myObj = new File(inputFile); 
	        PrintStream fileStream = new PrintStream(new File(outputFile));
	        
	        Scanner myReader = new Scanner(myObj);
	        if(functions.firstPass) functions.init();
	        while (myReader.hasNextLine()) {
	          String data = myReader.nextLine();

	  		  String line = data.trim();
	  		  line = line.split("//")[0];		
	  		  if(line != "") {
	          functions.parser(line);
	  		  
	         // check for comments then skip line
	         // ■ Parse the symbolic command into its underlying fields.
	         // ■ For each field, generate the corresponding bits in the machine language.
	         // ■ Replace all symbolic references (if any) with numeric addresses of memory locations.
	         // ■ Assemble the binary codes into a complete machine instruction.
	          
	          if(!functions.isLabel && !functions.firstPass) System.out.print(functions.arrToString(functions.machineCode) + System.lineSeparator());
	          if(!functions.isLabel && !functions.firstPass) fileStream.println(functions.arrToString(functions.machineCode));
	  		  }
	        }
	        myReader.close();
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
	    functions.firstPass = false;
        }
	   // System.out.println("variable map:");
	   // System.out.print(functions.varMap);
	}

}
