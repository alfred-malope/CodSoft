import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {

    private static final int TIMER_DELAY = 30; // in seconds

    private List<Question> questions;
    private int currentQuestionIndex;
    private int score;
    private Timer timer;
    private int correctAnswers;
    private int incorrectAnswers;

    private Stage primaryStage;
    private VBox vbox;
    private Label questionLabel;
    private List<RadioButton> optionButtons;
    private Button submitButton;
    private ToggleGroup optionsGroup;
    private Label timerLabel;
    private boolean timerExpired = false;

    public Main() {
        this.questions = new ArrayList<>();
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.timer = null;
        this.correctAnswers = 0;
        this.incorrectAnswers = 0;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        Scene scene = new Scene(vbox, 600, 400);

        questionLabel = new Label();
        optionButtons = new ArrayList<>();
        optionsGroup = new ToggleGroup();

        for (int i = 0; i < 4; i++) {
            RadioButton button = new RadioButton();
            button.setToggleGroup(optionsGroup);
            optionButtons.add(button);
        }

        submitButton = new Button("Submit");
        submitButton.setOnAction(e -> handleSubmission());

        timerLabel = new Label("Time remaining: " + TIMER_DELAY + " seconds");

        vbox.getChildren().addAll(questionLabel);
        optionButtons.forEach(button -> vbox.getChildren().add(button));
        vbox.getChildren().addAll(submitButton, timerLabel);

        primaryStage.setTitle("Java Quiz Game");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.show();

        // Add questions
        addQuestion(new Question("Which statement is true about the switch statement?", Arrays.asList(
                "It must contain the default section.",
                "The break statement, at the end of each case block, is optional.",
                "Its case label literals can be changed at runtime.",
                "Its expression must evaluate to a collection of values."
        ), "2"));

        addQuestion(new Question("What is the name of the Java concept that uses access modifiers to protect variables and hide them within a class?", Arrays.asList(
                "Encapsulation",
                "Inheritance",
                "Instantiation",
                "Polymorphism"
        ), "1"));

        addQuestion(new Question("Which statement is true about Java byte code?", Arrays.asList(
                "It can run on any platform.",
                "It can run on any platform only if it was compiled for that platform.",
                "It can run on any platform that has the Java Runtime Environment.",
                "It can run on any platform that has a Java compiler."
        ), "3"));

        addQuestion(new Question("Which statement is true about the switch statement?", Arrays.asList(
                "It must contain the default section.",
                "The break statement, at the end of each case block, is mandatory.",
                "Its case label literals can be changed at runtime.",
                "Its expression must evaluate to a single value."
        ), "4"));

        startQuiz();
    }

    private void addQuestion(Question question) {
        this.questions.add(question);
    }

    private void startQuiz() {
        if (questions.isEmpty()) {
            showAlert("No questions available.");
            Platform.exit();
        } else {
            displayQuestion(questions.get(currentQuestionIndex));
            startTimer();
        }
    }

    private void displayQuestion(Question question) {
        resetOptions();

        questionLabel.setText("Question " + (currentQuestionIndex + 1) + ": " + question.getText());

        List<String> options = question.getOptions();
        for (int i = 0; i < options.size(); i++) {
            optionButtons.get(i).setText("(" + (i + 1) + ") " + options.get(i));
        }
    }

    private void handleTimeUp() {
        if (!timerExpired) {
            timerExpired = true;

            Platform.runLater(() -> timerLabel.setText("Time is up!"));

            incorrectAnswers++;

            moveToNextQuestion();
        }
    }


    private void moveToNextQuestion() {
        currentQuestionIndex++;

        if (currentQuestionIndex < questions.size()) {
            displayQuestion(questions.get(currentQuestionIndex));
            startTimer();
        } else {
            displayResultScreen();
        }
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            private int timeRemaining = TIMER_DELAY;

            @Override
            public void run() {
                if (timeRemaining > 0) {
                    Platform.runLater(() -> timerLabel.setText("Time remaining: " + timeRemaining + " seconds"));
                    timeRemaining--;
                } else {
                    Platform.runLater(() -> {
                        timerLabel.setText("Time is up!");
                        handleTimeUp(); // Automatically submit when time is up
                    });
                    stopTimer();
                }
            }
        }, 0, 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private boolean isCorrectAnswer(Question question, String answer) {
        return answer.equalsIgnoreCase(question.getCorrectAnswer());
    }

    private void displayResultScreen() {
        String resultMessage = "Your final score is: " + score + " out of 4 questions \n" +
                "You answered " + correctAnswers + " questions correctly.\n" +
                "You answered " + incorrectAnswers + " questions incorrectly.";

        showAlert(resultMessage);

        primaryStage.close();
    }

    private void handleSubmission() {
        RadioButton selectedButton = (RadioButton) optionsGroup.getSelectedToggle();

        if (selectedButton != null) {
            int answerIndex = optionButtons.indexOf(selectedButton);
            String answer = String.valueOf(answerIndex + 1);

            Question question = questions.get(currentQuestionIndex);

            if (isCorrectAnswer(question, answer)) {
                score++;
                correctAnswers++;
            } else {
                incorrectAnswers++;
            }

            // Stop the timer for the current question
            stopTimer();

            currentQuestionIndex++;

            if (currentQuestionIndex < questions.size()) {
                displayQuestion(questions.get(currentQuestionIndex));
                startTimer();
            } else {
                displayResultScreen();
            }
        }
    }

    private void resetOptions() {
        optionsGroup.selectToggle(null);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class Question {
    private String text;
    private List<String> options;
    private String correctAnswer;

    public Question(String text, List<String> options, String correctAnswer) {
        this.text = text;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
