package assemblerPackage;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class HelperFunctions {
	public boolean firstPass = true;
	public int currRomInstruction = 0;
	public int[] machineCode = new int[16];

	private HashMap<String, Pair<Integer, Integer>> compMap = new HashMap<>();
	private ArrayList<String> destMap = new ArrayList<>(8);
	private ArrayList<String> jumpMap = new ArrayList<>(8);
	
		
	private int aIndex = 3;
	private int c1Index = 4;
	private int c2Index = 5;
	private int c3Index = 6;
	private int c4Index = 7;
	private int c5Index = 8;
	private int c6Index = 9;
	
	private int d1Index = 10;
	private int d2Index = 11;
	private int d3Index = 12;
	
	
	private int j1Index = 13;
	private int j2Index = 14;
	private int j3Index = 15;
	
	//for variables
	public boolean isLabel = false;
	private int varLocationCounter = 16;
	public HashMap<String, Integer> varMap = new HashMap<>();
	
	public void init() {
		//when a = 0
		compMap.put("0", new Pair<>(0, 42));
		compMap.put("1", new Pair<>(0, 63));
		compMap.put("-1", new Pair<>(0, 58));
		compMap.put("D", new Pair<>(0, 12));
		compMap.put("A", new Pair<>(0, 48));
		compMap.put("!D", new Pair<>(0, 13));
		compMap.put("!A", new Pair<>(0, 49));
		compMap.put("-D", new Pair<>(0, 15));
		compMap.put("-A", new Pair<>(0, 51));
		compMap.put("D+1", new Pair<>(0, 31));
		compMap.put("A+1", new Pair<>(0, 55));
		compMap.put("D-1", new Pair<>(0, 14));
		compMap.put("A-1", new Pair<>(0, 50));
		compMap.put("D+A", new Pair<>(0, 2));
		compMap.put("D-A", new Pair<>(0, 19));
		compMap.put("A-D", new Pair<>(0, 7));
		compMap.put("D&A", new Pair<>(0, 0));
		compMap.put("D|A", new Pair<>(0, 21));
		
		//when a = 1
		compMap.put("M", new Pair<>(1, 48));
		compMap.put("!M", new Pair<>(1, 49));
		compMap.put("-M", new Pair<>(1, 51));
		compMap.put("M+1", new Pair<>(1, 55));
		compMap.put("M-1", new Pair<>(1, 50));
		compMap.put("D+M", new Pair<>(1, 2));
		compMap.put("D-M", new Pair<>(1, 19));
		compMap.put("M-D", new Pair<>(1, 7));
		compMap.put("D&M", new Pair<>(1, 0));
		compMap.put("D|M", new Pair<>(1, 21));
		
		//dest map init
		destMap.add("null");
		destMap.add("M");
		destMap.add("D");
		destMap.add("MD");
		destMap.add("A");
		destMap.add("AM");
		destMap.add("AD");
		destMap.add("AMD");
		
		
		//jump map init
		jumpMap.add("null");
		jumpMap.add("JGT");
		jumpMap.add("JEQ");
		jumpMap.add("JGE");
		jumpMap.add("JLT");
		jumpMap.add("JNE");
		jumpMap.add("JLE");
		jumpMap.add("JMP");
		
		//add predefined Symbols
		varMap.put("SP", 0);
		varMap.put("LCL", 1);
		varMap.put("ARG", 2);
		varMap.put("THIS", 3);
		varMap.put("THAT", 4);
		varMap.put("R0", 0);
		varMap.put("R1", 1);
		varMap.put("R2", 2);
		varMap.put("R3", 3);
		varMap.put("R4", 4);
		varMap.put("R5", 5);
		varMap.put("R6", 6);
		varMap.put("R7", 7);
		varMap.put("R8", 8);
		varMap.put("R9", 9);
		varMap.put("R10", 10);
		varMap.put("R11", 11);
		varMap.put("R12", 12);
		varMap.put("R13", 13);
		varMap.put("R14", 14);
		varMap.put("R15", 15);
		varMap.put("SCREEN", 16384);
		varMap.put("KBD", 24576);
		
		
	}

	public void parser(String line) {
		isLabel = false;
			if(line.charAt(0) == '@') {
				//A-instruction
				if(!this.firstPass) {
					this.A_COMMAND(line);
					this.machineCode[0] = 0;
				}else {
					this.currRomInstruction++;
				}
			}else if(line.charAt(0) == '('){
				int endIndex = line.indexOf(')');
				String label = line.substring(1, endIndex).trim();
				System.out.println(label);
				isLabel = true;
				//this.L_COMMAND(line);
				if(this.firstPass) {	//adds labels on first pass
					this.varMap.put(label, this.currRomInstruction);
				}
				
			}else {
				//C-instruction
				if(!this.firstPass) {
					this.machineCode[0] = 1;
					this.machineCode[1] = 1;
					this.machineCode[2] = 1;
					this.C_COMMAND(line);
				}else {
					this.currRomInstruction++;
				}
			}	

	}
	
	public void A_COMMAND(String line) {

		int number = 0;
		boolean isVar = false;
		String value = line.split("@")[1];
		try {
			number = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			isVar = true;
		}
		
		int varLoc = this.varLocationCounter;
		if(isVar) {
			if(!this.varMap.containsKey(value)) {
				this.varMap.put(value, this.varLocationCounter);
				this.varLocationCounter++;
			}else {
				varLoc = this.varMap.get(value);
			}
		}
		int[] binaryRep = (!isVar) ? this.numbToBinary(number) : this.numbToBinary(varLoc);

		for(int i = 1; i < this.machineCode.length; i++) {
			this.machineCode[i] = binaryRep[i];
		}

	}
	
	public String C_COMMAND(String line) {
		
		int dest = line.indexOf('='); //if it does not have destination will yield -1
		int jump = line.indexOf(';'); ////if it does not have jump will yield -1
		
		if(dest == -1) {
			this.machineCode[this.d1Index] = 0;
			this.machineCode[this.d2Index] = 0;
			this.machineCode[this.d3Index] = 0;
			this.setCompVals((line.substring(0, jump)).trim());
			this.setJumpVals((line.substring(jump + 1)).trim());
		}
		
		if(jump == -1) {
			this.machineCode[this.j1Index] = 0;
			this.machineCode[this.j2Index] = 0;
			this.machineCode[this.j3Index] = 0;
			this.setDestVals((line.substring(0, dest)).trim());
			this.setCompVals((line.substring(dest + 1)).trim());
		}
		
		
		return "";
	}
	
	public void setCompVals(String comp) {
		//comp
		Pair<Integer, Integer> values = compMap.get(comp);

		int[] cVals = this.numbToBinary(values.getSecond());
		this.machineCode[aIndex] = values.getFirst();
		int shift = 6;
		this.machineCode[c1Index] = cVals[c1Index + shift];
		this.machineCode[c2Index] = cVals[c2Index + shift];
		this.machineCode[c3Index] = cVals[c3Index + shift];
		this.machineCode[c4Index] = cVals[c4Index + shift];
		this.machineCode[c5Index] = cVals[c5Index + shift];
		this.machineCode[c6Index] = cVals[c6Index + shift];
		
	}
	
	public void setDestVals(String dest) {
		//dest
		int numRep = destMap.indexOf(dest);
		int[] dVals = this.numbToBinary(numRep);
		
		int shift = 3;
		this.machineCode[d1Index] = dVals[d1Index + shift];
		this.machineCode[d2Index] = dVals[d2Index + shift];
		this.machineCode[d3Index] = dVals[d3Index + shift];

		
	}
	
	public void setJumpVals(String jump) {
		//jump
		int numRep = jumpMap.indexOf(jump);
		int[] jVals = this.numbToBinary(numRep);
		
		int shift = 0;
		this.machineCode[j1Index] = jVals[j1Index + shift];
		this.machineCode[j2Index] = jVals[j2Index + shift];
		this.machineCode[j3Index] = jVals[j3Index + shift];		
	}
	
	public void outputArr(int[] arr) {
		for(int a: arr) {
			System.out.print(a + " ");
		}
		System.out.println("");
	}
	
	public String arrToString(int[] arr) {
		String result = "";
		for(int a: arr) {
			result = result.concat(String.valueOf(a));
		}
		return result;
	}
	
	public int[] numbToBinary(Integer number) {
		int[] result = new int[16];
		
		int currNumber = number;
		int prevNumber;
		int placeNumber;
		int currIndex = 1;
		
		for(int currPlace = 14; currPlace >= 0; currPlace--) {
			prevNumber = currNumber;
			placeNumber = (int) Math.pow(2, currPlace);
			currNumber = (int) (currNumber / placeNumber);
			
			if(currNumber != 0) {
				result[currIndex] = 1;
				currNumber = (int) (prevNumber % placeNumber);
				
			}else {
				result[currIndex] = 0;
				currNumber = prevNumber;
			}
			
			currIndex++;
		}
		return result;
	}
	
}
