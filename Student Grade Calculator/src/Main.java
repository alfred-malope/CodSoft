import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Take marks obtained in each subject.
        try (Scanner scanner = new Scanner(System.in)) {
            int numberOfSubjects = 0;

            boolean validInput = false;
            while (!validInput) {
                try {
                    System.out.print("Enter the number of subjects: ");
                    numberOfSubjects = scanner.nextInt();

                    if (numberOfSubjects <= 0) {
                        throw new IllegalArgumentException("Number of subjects must be greater than 0.");
                    }
                    validInput = true; // If no exception, exit the loop
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer for the number of subjects.");
                    scanner.next(); // Clear the buffer
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            int[] marks = new int[numberOfSubjects];
            for (int i = 0; i < numberOfSubjects; i++) {
                int subjectMark;
                do {
                    try {
                        System.out.print("Enter marks obtained in subject " + (i + 1) + " (out of 100): ");
                        subjectMark = scanner.nextInt();

                        if (subjectMark < 0 || subjectMark > 100) {
                            System.out.println("Invalid input. Please enter a mark between 0 and 100.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid integer for the marks.");
                        scanner.next(); // Clear the buffer
                        subjectMark =- 1; // Set an invalid value to trigger the do-while loop
                    }
                } while (subjectMark < 0 || subjectMark > 100);

                marks[i] = subjectMark;
            }
            // Calculate the total marks.
            int totalMarks = 0;
            for (int mark : marks) {
                totalMarks += mark;
            }

            // Calculate the average percentage.
            double averagePercentage = (double) totalMarks / numberOfSubjects;

            // Create a DecimalFormat object with the pattern "0.00" for two decimal places
            DecimalFormat df = new DecimalFormat("0.00");

            // Format the averagePercentage using the DecimalFormat
            String formattedPercentage = df.format(averagePercentage);

            // Calculate the grade.
            String grade;
            if (averagePercentage >= 90) {
                grade = "A+";
            } else if (averagePercentage >= 80) {
                grade = "A";
            } else if (averagePercentage >= 70) {
                grade = "B";
            } else if (averagePercentage >= 60) {
                grade = "C";
            } else if (averagePercentage >= 50) {
                grade = "D";
            } else {
                grade = "F";
            }

            // Display the results to the user.
            System.out.println("\nResults:");
            System.out.println("Total Marks: " + totalMarks);
            System.out.println("Average Percentage: " + formattedPercentage + "%");
            System.out.println("Grade: " + grade);
        }
    }
}