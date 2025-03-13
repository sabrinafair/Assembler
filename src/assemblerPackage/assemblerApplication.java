package assemblerPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class assemblerApplication {

	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
		System.out.print("Enter file path\name.extension: ");

        String file = scanner.nextLine();
		//read in file
	    try {
	        File myObj = new File(file); //test: C:\Users\Sabrina Ferras\OneDrive - csumb.edu\Documents\CPP\Spring2025\CS 5250 - Adv Comp Architecture\software\nand2tetris\nand2tetris\projects\4\mult\Mult.asm
	        Scanner myReader = new Scanner(myObj);
	        while (myReader.hasNextLine()) {
	          String data = myReader.nextLine();
	          System.out.println(data);
	          //check for comments then skip line
	         // ■ Parse the symbolic command into its underlying fields.
	         // ■ For each field, generate the corresponding bits in the machine language.
	         // ■ Replace all symbolic references (if any) with numeric addresses of memory locations.
	         // ■ Assemble the binary codes into a complete machine instruction.
	        }
	        myReader.close();
	      } catch (FileNotFoundException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
	}

}
