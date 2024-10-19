//Pablo Mendoza
//10-17-2024
//CPSC-39-12112
//Chosen Improvement #1: Added word points for word length 
//Chosen Improvement #2: Allowed players to chose how many characters they recieve (from 4 - 9)

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class scrabble{

	static class Word{
		//simple Word class, with a constructor, accessor, and a toString method

		private String word;

		public Word(String word){
			this.word = word;
		}

		public String getWord() {
			return word;
		}

		public String toString(){
			return word; 
		}

	}

	public static void main(String[] args){
		//try method to read file and convert words into "Word" class instances 
		ArrayList<Word> words = new ArrayList<>();
		ArrayList<String> letters = new ArrayList<>(); //stores
		ArrayList<String> letterPoints = new ArrayList<>();
		try {
			//take the file, and use a scanner to read each line. I also removed the first two lines of the .txt file to make this simpler
			File file = new File("CollinsScrabbleWords_2019.txt");
			Scanner fileScanner = new Scanner(file);
			//
			while(fileScanner.hasNextLine()){
				//scanner scans each line, then uses the string value to create word class object
				String line = fileScanner.nextLine();
				Word word = new Word(line);
				words.add(word);
			}
			fileScanner.close();

			//second scanner for other txt file containg the letter-point conversion - - - - - - - - - - - - - - - - - - - - - - 
			File filePoints = new File("scrabbleLettersPoints.txt");
			Scanner filePointsScanner = new Scanner(filePoints);
			String wholeRow = filePointsScanner.nextLine();
			String[] dividedRow = wholeRow.split(" ");
			for(String chars : dividedRow){
				letters.add(chars);
			}
			String numValues = filePointsScanner.nextLine();
			String[] dividedValues = numValues.split(" ");
			for(String point : dividedValues){
                letterPoints.add(point);
            }
			filePointsScanner.close();

		} catch (FileNotFoundException e) {
			//error exception handeling 
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

		//create arraylist of words, in which we will store all our word class objects later
		ArrayList<String> characters = new ArrayList<>();
		Scanner scanner = new Scanner(System.in);

        //use a loop to add all 26 letters from 'A' to 'Z'
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            characters.add(String.valueOf(letter));
        }
        
        int characterCount = 0;

        //loops until a valid number is entered (from 4 - 9)
        while(characterCount < 4 || characterCount > 9) {
            System.out.println("Enter the number of characters to play with (4-9): ");
            characterCount = Integer.parseInt(scanner.nextLine());

            if(characterCount < 4 || characterCount > 9){
                System.out.println("Invalid input. Please enter a number between 4 and 9.");
            }
        }

		ArrayList<String> chosenWordChars = new ArrayList<>();


		//void function that determines four random, different letters for the player
        retrievePlayerLetters(characterCount, characters, chosenWordChars);
        System.out.println("Create a word using these charcters!");
        //intilaizing player's input
        String inputString = "";

        //while function keeps going until the player chooses a word within the parameter of their allotted letters
        boolean stillgoing = true;
        while(stillgoing){
			inputString = scanner.nextLine().toUpperCase();
        	if(checkPlayerWord(inputString, chosenWordChars)){
        		stillgoing = false; 
        	}else{
        		System.out.println("Invalid characters detected, try again");
        	}
        }

       	//binary search that returns boolean value. player wins if true
        if(wordBinarySearch(words, inputString)){
        	System.out.println("Your word is correct and has a score of " + wordToPointConversion(inputString, letters, letterPoints) + "!");
        }else{
        	System.out.println("Your word is incorrect!");
        }
	}



	//function that takes letter count, array of alphabet to select and modify the chosenWordChars 
	//array with selected characters. 
	public static void retrievePlayerLetters(int num, ArrayList<String> characters, ArrayList<String> chosenWordChars){
		Random random = new Random(); 
		for(int i = 0; i < num; i++){
			//random class produces a random integer index, character is added to chosenWordChars, removed from characters
			int randomIndex = random.nextInt(characters.size());
			chosenWordChars.add(characters.get(randomIndex));
			characters.remove(randomIndex);
		}

		//printing out characters here
		System.out.println("Your given characters are - ");
		for(String letter : chosenWordChars){
			System.out.print("'" + letter + "' , ");
		}
	}

	public static boolean checkPlayerWord(String inputString, ArrayList<String> chosenWordChars){
		//converts string into character array, looks at each character in the input string, and checks to see if 
		//any of the characters dont match the chosenWordChars array
		for (char c : inputString.toCharArray()) {
            //check if the character is not in allowed characters
            if (!chosenWordChars.contains(String.valueOf(c))) {
                return false; //invalid character found
            }
        }
        return true; //input word is valid
	}


	public static boolean wordBinarySearch(ArrayList<Word> words, String targetWord){
		//simple binary search. since the array is sorted by Lexicographically, we compare lexicographic value
		//of the input word to the middle word of .txt file, and sort our way from there.
		int startPoint = 0, endpoint = words.size() -1; 
		while (startPoint <= endpoint){
			int mid = startPoint + (endpoint - startPoint)/2; 
			Word searchedWord = words.get(mid);
			String wordString = searchedWord.getWord();

			int wordCompareValue = wordString.compareTo(targetWord); 

			if(wordCompareValue < 0){
				startPoint = mid + 1; 
			}else if(wordCompareValue > 0){
				endpoint = mid - 1; 
			}else{
				return true;
			}
		}
		return false;
	}

	public static int wordToPointConversion(String inputString, ArrayList<String> letters, ArrayList<String> letterPoints){
		//function that converts word to point value. searches for index of character, then uses the index to find value 
		int stringValue = 0;
		for(char character : inputString.toCharArray()){
			String charStr = String.valueOf(character);
			for(int i = 0; i < letters.size(); i++){
				if(charStr.equals(letters.get(i))){
					stringValue += Integer.valueOf(letterPoints.get(i));
					break;
				}
			}
		}
		return stringValue; 
	}
}