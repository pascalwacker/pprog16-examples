package hangman;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Hangman {

	public static void main(String[] args) {
		// sentence as input, take a random word from here
		String[] words="Wilkommen zur Hangman Implementation in Java von Pascal Wacker".split(" ");
		// get a random pointer for the words array
		int idx = new Random().nextInt(words.length);
		
		// initialize new Game
		Hangman game = new Hangman(words[idx]);
		// run the game
		game.play();
	}

	// -- fields
	char[] word;
	int wordLength;
	char[] guessed;
	boolean[] used;
	int turn;
	int lives;
	int charFound;
	boolean finished;
	private Scanner in;
	
	// -- constructor
	public Hangman(String word) {
		this.lives = 5;
		this.turn = 0; // would be default
		this.charFound = 0; // would be default
		this.finished = false; // would be default
		
		// get the lenght of the input word
		this.wordLength = word.length();
		
		// initialize word, guessed and used
		this.word = word.toCharArray();
		this.guessed = new char[this.wordLength];
		this.used = new boolean[26];
		Arrays.fill(this.used, false); // no character used yet
		
		// while
		/*int i = 0;
		while (i < this.wordLength) {
			this.word[i] = Character.toUpperCase(this.word[i]);
			this.guessed[i] = '-';
			i++;
		}
		// unset i, if not needed anymore */
		
		// for every character
		for (int i=0; i<this.wordLength; i++) {
			// uppercase all characters
			this.word[i] = Character.toUpperCase(this.word[i]);
			// fill our helper array with `-`
			this.guessed[i] = '-';
		}
		
		// open the Scanner
		this.in = new Scanner(System.in);
	}
	
	
	
	public void play() {
		// print welcome
		System.out.println("Wilkommen zu \"Hangman\", Implementation in Java von Pascal Wacker");
		// print out the initial state
		System.out.println(this.guessed);
		
		// while there are lives left and the game isn't won yet do ...
		while (!this.finished && this.lives > 0) {
			// tell user to input something
			System.out.println((this.turn <= 0 ? "Bitte ersten Buchstaben eingeben:" : "Bitte nächsten Buchstaben eingeben:"));
			
			// read only one character as upper case
			char input = Character.toUpperCase(this.in.next(".").charAt(0)); // there's an exception if it's not okay.
			// int representation of the char (0 for A to 25 for Z)
			int intOfInput = this.charToInt(input);
			
			// is it in the valid input range of A-Z?
			if (intOfInput < 0 || intOfInput > 25) {
				System.out.println("Es können nur Buchstaben von A bis Z verwendet werden, Gross/Kleinschreibung spielt keine Rolle!");
			// if the char is used
			} else if (this.used[intOfInput]) {
				System.out.println("Der Buchstaben \"" + input + "\" wurde bereits benutzt.");
			} else {
				// set this char to used
				this.used[intOfInput] = true;
				
				// check if the char is in our word and if yes at what position
				int positionInWord = this.charInCharArray(input, this.word);
				// if the char is there ...
				if (positionInWord >= 0) {
					// update the guessed array
					this.updateGuessedFromInput(input);
					// update the turn
					this.turn++;

					// check if we're done
					// if (Arrays.equals(this.word, this.guessed)) {
					if (this.charFound == this.wordLength) { // I guess, this should be better, regarding resources, since it hasn't to loop through 2 arrays!
						this.finished = true;
					}
				} else {
					// update turn
					this.turn++;
					// remove a live
					this.lives--;
					System.out.println("Leider kommt \"" + input + "\" nicht im Lösungswort vor." + (this.lives > 0 ? " Es verbleiben noch " + this.lives + " versuche!" : ""));
				}
			}
			
			// print out what we have so far
			System.out.println(this.guessed);
		}
		// close the Scanner
		this.in.close();
		
		// if there aren't any lives left, the user has lost
		if (this.lives <= 0) {
			System.out.println("Leider verloren. Das gesuchte Wort wäre \"" + String.copyValueOf(this.word) + "\" gewesen.");
		} else {
			System.out.println("Herzlichen Glückwunsch. Das gesuchte Wort \"" + String.copyValueOf(this.word) + "\" wurde gefunden in " + this.turn + " versuchen.");
		}
	}
	
	/**
	 * Look for a character in a character array and return the index, default: -1
	 * 
	 * @param char needle
	 * @param char[] charArray
	 * @return int
	 */
	private int charInCharArray(char needle, char[] charArray) {
		int position = -1;
		int i = 0;
		for (char c : charArray) {
		    if (c == needle) {
		    	position = i;
		        break;
		    }
		    i++;
		}
		
		return position;
	}
	
	/**
	 * Update the guessed array with a char input
	 * 
	 * @param char input
	 */
	private void updateGuessedFromInput(char input) {
		for (int i=0; i<this.wordLength; i++) {
			if (this.word[i] == input) {
				this.guessed[i] = input;
				this.charFound++;
			}
		}
	}
	
	/**
	 * Gives the integer of a char starting with 0 for A.
	 * 
	 * @param char input
	 * @return int
	 */
	private int charToInt(char input) {
		return ((int)input - 65);
	}
}
