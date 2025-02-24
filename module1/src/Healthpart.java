import java.io.*;
import java.util.Scanner;

public class Healthpart {
    // Fields to store health data
    private int stepsGoal; // Daily steps goal
    private int waterGlasses; // Daily water intake (in glasses)
    private int hoursSleeping; // Daily hours of sleep
    private int fats; // Daily fat intake (in grams)
    private int proteins; // Daily protein intake (in grams)
    private int carbohydrates; // Daily carbohydrate intake (in grams)
    private String[] wrongHabits; // List of habits to stop
    private static final int toStopHabits = 30; // Days required to drop a habit

    // Reference to the UserInfo object (passed via constructor)
    UserInfo mainUser;

    // Constructor to initialize the Healthpart with a UserInfo object
    public Healthpart(UserInfo mainUserInfo) {
        mainUser = mainUserInfo;
    }

    // Method to display health data
    public void displayHealthData() {
        System.out.println("\n______🏥 Health Data ______");
        System.out.println("Steps Goal: " + stepsGoal);
        System.out.println("Water Intake (Glasses): " + waterGlasses);
        System.out.println("Hours of Sleep: " + hoursSleeping);
        System.out.println("Calorie Intake:");
        System.out.println("  Fats: " + fats + "g");
        System.out.println("  Proteins: " + proteins + "g");
        System.out.println("  Carbohydrates: " + carbohydrates + "g");
        System.out.println("Habits to Stop:");
        if (wrongHabits != null) {
            for (String habit : wrongHabits) {
                System.out.println("  - " + habit);
            }
        }
        System.out.println("Days to Drop a Habit: " + toStopHabits);
        System.out.println("___________________________\n");
    }

    // Method to set health goals
    public void setHealthGoals(Scanner scanner) {
        System.out.print("Enter your daily steps goal: ");
        stepsGoal = scanner.nextInt();
        System.out.print("Enter your daily water intake (in glasses): ");
        waterGlasses = scanner.nextInt();
        System.out.print("Enter your daily hours of sleep: ");
        hoursSleeping = scanner.nextInt();
        System.out.print("Enter your daily fat intake (in grams): ");
        fats = scanner.nextInt();
        System.out.print("Enter your daily protein intake (in grams): ");
        proteins = scanner.nextInt();
        System.out.print("Enter your daily carbohydrate intake (in grams): ");
        carbohydrates = scanner.nextInt();
    }

    // Method to set habits to stop
    public void setWrongHabits(Scanner scanner) {
        int numHabits = 0;
        boolean validInput = false;

        // Keep asking until a valid integer is entered
        while (!validInput) {
            System.out.print("Enter the number of habits you want to stop: ");
            String input = scanner.nextLine();

            try {
                numHabits = Integer.parseInt(input);
                validInput = true; // Exit the loop if parsing succeeds
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        wrongHabits = new String[numHabits]; // Initialize the array

        for (int i = 0; i < numHabits; i++) {
            System.out.print("Enter habit " + (i + 1) + ": ");
            wrongHabits[i] = scanner.nextLine();
        }
    }

    // Method to calculate progress in dropping habits
    public void calculateHabitProgress(int daysCompleted) {
        if (wrongHabits == null || wrongHabits.length == 0) {
            System.out.println("No habits to track!");
            return;
        }

        double progress = ((double) daysCompleted / toStopHabits) * 100;
        System.out.println("\nHabit Progress:");
        System.out.println("Days Completed: " + daysCompleted);
        System.out.println("Total Days Required: " + toStopHabits);
        System.out.printf("Progress: %.2f%%\n", progress);

        if (daysCompleted >= toStopHabits) {
            System.out.println("Congratulations! You've successfully dropped your habits.");
        } else {
            System.out.println("Keep going! You're on the right track.");
        }
    }

    // Method to save health data to a file
    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Steps Goal: " + stepsGoal + "\n");
            writer.write("Water Intake (Glasses): " + waterGlasses + "\n");
            writer.write("Hours of Sleep: " + hoursSleeping + "\n");
            writer.write("Fats: " + fats + "g\n");
            writer.write("Proteins: " + proteins + "g\n");
            writer.write("Carbohydrates: " + carbohydrates + "g\n");
            if (wrongHabits != null) {
                writer.write("Habits to Stop:\n");
                for (String habit : wrongHabits) {
                    writer.write("  - " + habit + "\n");
                }
            }
            System.out.println("Health data saved successfully to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving health data: " + e.getMessage());
        }
    }

    // Method to load health data from a file
    public void loadFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Steps Goal: ")) {
                    stepsGoal = Integer.parseInt(line.substring("Steps Goal: ".length()));
                } else if (line.startsWith("Water Intake (Glasses): ")) {
                    waterGlasses = Integer.parseInt(line.substring("Water Intake (Glasses): ".length()));
                } else if (line.startsWith("Hours of Sleep: ")) {
                    hoursSleeping = Integer.parseInt(line.substring("Hours of Sleep: ".length()));
                } else if (line.startsWith("Fats: ")) {
                    fats = Integer.parseInt(line.substring("Fats: ".length(), line.length() - 1));
                } else if (line.startsWith("Proteins: ")) {
                    proteins = Integer.parseInt(line.substring("Proteins: ".length(), line.length() - 1));
                } else if (line.startsWith("Carbohydrates: ")) {
                    carbohydrates = Integer.parseInt(line.substring("Carbohydrates: ".length(), line.length() - 1));
                } else if (line.startsWith("Habits to Stop:")) {
                    int numHabits = 0;
                    while ((line = reader.readLine()) != null && line.startsWith("  - ")) {
                        numHabits++;
                    }
                    wrongHabits = new String[numHabits];
                    for (int i = 0; i < numHabits; i++) {
                        wrongHabits[i] = line.substring(4); // Remove "  - " from the habit name
                        line = reader.readLine();
                    }
                }
            }
            System.out.println("Health data loaded successfully from " + filename);
        } catch (IOException e) {
            System.err.println("Error loading health data: " + e.getMessage());
        }
    }

    // Menu method to allow user to choose actions
    public void mainHealth() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n________Health Menu________");
            System.out.println("1. Set Health Goals");
            System.out.println("2. Set Habits to Stop");
            System.out.println("3. Display Health Data");
            System.out.println("4. Save Health Data to File");
            System.out.println("5. Load Health Data from File");
            System.out.println("6. Calculate Habit Progress");
            System.out.println("7. Exit");
            System.out.print("Enter your choice (1-7): ");

            int choice = Main.ValidChoice(1 , 7);
            switch (choice) {
                case 1:
                    setHealthGoals(scanner);
                    break;
                case 2:
                    setWrongHabits(scanner);
                    break;
                case 3:
                    displayHealthData();
                    break;
                case 4:
                    System.out.print("Enter file name to save health data: ");
                    saveToFile(scanner.nextLine().trim());
                    break;
                case 5:
                    System.out.print("Enter file name to load health data: ");
                    loadFromFile(scanner.nextLine().trim());
                    break;
                case 6:
                    System.out.print("Enter the number of days completed for habit tracking: ");
                    int daysCompleted = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    calculateHabitProgress(daysCompleted);
                    break;
                case 7:
                    exit = true;
                    System.out.println("Exiting Health Menu. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
    }
}