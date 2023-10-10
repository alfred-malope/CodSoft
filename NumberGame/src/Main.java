
import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int randomNumber = new Random().nextInt(100) + 1;

        System.out.println("Welcome to Number Game");

        int userGuess = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("Guess a number between 1 and 100:");
                userGuess = scanner.nextInt();

                // Check if userGuess is within the valid range (1 to 100)
                if (userGuess >= 1 && userGuess <= 100) {
                    validInput = true;
                } else {
                    System.out.println("Please enter a number between 1 and 100.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear the buffer
            }
        }

        int maxAttempts = 10;
        int attempts = 0;

        while (userGuess != randomNumber && attempts < maxAttempts) {
            if (userGuess < randomNumber) {
                System.out.print("Your guess is too low. Try again:");
            } else {
                System.out.print("Your guess is too high. Try again:");
            }

            validInput = false;

            while (!validInput) {
                try {
                    userGuess = scanner.nextInt();

                    if (userGuess >= 1 && userGuess <= 100) {
                        validInput = true;
                    } else {
                        System.out.println("Please enter a number between 1 and 100.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // Clear the buffer
                }
            }

            attempts++;
        }

        if (attempts < maxAttempts) {
            System.out.println("Congratulations! You guessed the correct number!");
            double percentage = ((maxAttempts - attempts) / (double) maxAttempts) * 100;
            System.out.println("You scored: " + percentage + "%");
        } else {
            System.out.println("You ran out of attempts. The correct number was " + randomNumber);
            double percentage = ((maxAttempts - attempts) / (double) maxAttempts) * 100;
            System.out.println("You scored: " + percentage + "%");
        }

        System.out.print("Do you want to play again? (y/n) ");
        String playAgain = scanner.next();

        if (playAgain.equalsIgnoreCase("y")) {
            main(args);
        } else {
            System.out.print("Thank you for playing!");
        }
    }
}
