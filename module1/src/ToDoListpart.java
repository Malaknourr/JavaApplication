import java.io.*;
import java.util.*;

public class ToDoListpart {
    // Lists to store tasks, their importance levels, and completion status
    private final List<String> tasks = new ArrayList<>(); // Stores task descriptions
    private final List<String> importanceLevels = new ArrayList<>(); // Stores task importance (Hard, Medium, Easy)
    private final List<Boolean> completed = new ArrayList<>(); // Stores task completion status (true = completed, false = pending)
    private String date = ""; // Stores the date of the To-Do list

    // Reference to the UserInfo object (passed via constructor)
    UserInfo mainUser;

    // Constructor to initialize the ToDoListpart with a UserInfo object
    public ToDoListpart(UserInfo userInfo) {
        mainUser = userInfo;
    }

    // Method to display tasks sorted by importance (Hard -> Medium -> Easy) with task numbers
    public void displaySortedTasks() {
        System.out.println("\nTo-Do List (Sorted by Importance)");
        System.out.println("--------------------------------------");

        // Create a list to store formatted task strings with numbers
        List<String> sortedTasks = new ArrayList<>();

        // Loop through all tasks and format them with their index, importance, description, and status
        for (int i = 0; i < tasks.size(); i++) {
            String status = completed.get(i) ? "Completed" : "Pending"; // Check if task is completed
            sortedTasks.add((i + 1) + ". " + importanceLevels.get(i) + ": " + tasks.get(i) + " (" + status + ")");
        }

        // Sort tasks by importance (Hard -> Medium -> Easy)
        sortedTasks.sort((a, b) -> {
            if (a.contains("Hard") && !b.contains("Hard")) return -1; // Hard tasks come first
            if (b.contains("Hard") && !a.contains("Hard")) return 1;
            if (a.contains("Medium") && b.contains("Easy")) return -1; // Medium tasks come before Easy
            if (b.contains("Medium") && a.contains("Easy")) return 1;
            return 0; // If importance levels are the same, maintain original order
        });

        // Display the sorted tasks
        for (String task : sortedTasks) {
            System.out.println(task);
        }
        System.out.println("--------------------------------------\n");
    }

    // Method to save the To-Do list to a file
    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Date: " + date + "\n"); // Write the date of the list
            for (int i = 0; i < tasks.size(); i++) {
                String status = completed.get(i) ? "Completed" : "Pending"; // Check task status
                writer.write(importanceLevels.get(i) + ": " + tasks.get(i) + " (" + status + ")\n"); // Write task details
            }
            System.out.println("\nTo-Do List saved successfully to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage()); // Handle file saving errors
        }
    }

    // Main To-Do List Menu
    public void mainToDo() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // Display the main menu options
            System.out.println("\n______📋 To-Do List Menu ______");
            System.out.println("1️⃣ Create a new To-Do List");
            System.out.println("2️⃣ Load and Edit an existing To-Do List");
            System.out.println("3️⃣ Display To-Do List");
            System.out.println("4️⃣ Exit");

            // Get user choice (validated using UserInfo.ValidChoice)
            int choice = Main.ValidChoice(1, 4);
            switch (choice) {
                case 1: // Create a new list
                    createNewList(scanner);
                    break;
                case 2: // Load and Edit an existing list
                    loadAndEditList(scanner);
                    break;
                case 3: // Display list
                    displaySortedTasks();
                    break;
                case 4: // Exit
                    System.out.println("Exiting To-Do List. Have a productive day!");
                    return;
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
        }
    }

    // Helper method to create a new To-Do List
    private void createNewList(Scanner scanner) {
        System.out.print("Enter the date for this To-Do List: ");
        date = scanner.nextLine(); // Get the date for the list

        // Clear existing tasks, importance levels, and completion status
        tasks.clear();
        importanceLevels.clear();
        completed.clear();

        // Get the number of tasks from the user
        System.out.print("Enter the number of tasks: ");
        int numTasks = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character after reading the integer

        // Loop to add tasks
        for (int i = 0; i < numTasks; i++) {
            System.out.print("Enter Task " + (i + 1) + ": ");
            tasks.add(scanner.nextLine()); // Add task description

            // Get importance level from the user
            System.out.print("Enter Importance Level (1- Hard / 2- Medium / 3- Easy): ");
            int inputChoice = Main.ValidChoice(1, 3); // Validate user input
            if (inputChoice == 1) {
                importanceLevels.add("Hard");
            } else if (inputChoice == 2) {
                importanceLevels.add("Medium");
            } else {
                importanceLevels.add("Easy");
            }

            completed.add(false); // Default task status is incomplete
        }

        // Display the newly created list
        displaySortedTasks();

        // Ask the user if they want to save the list
        System.out.print("Do you want to save this list? (yes/no): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            System.out.print("Enter file name to save: ");
            saveToFile(scanner.nextLine().trim()); // Save the list to a file
        }
    }

    // Helper method to load and edit an existing To-Do List
    private void loadAndEditList(Scanner scanner) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available! Create a list first.");
            return;
        }

        // Display the current list
        displaySortedTasks();

        // Display edit options
        System.out.println("1) Edit a Task\n2) Add a New Task\n3) Mark a Task as Complete");
        System.out.print("Choose an option: ");
        int editChoice = Main.ValidChoice(1, 3); // Validate user input

        if (editChoice == 1) { // Edit a task
            System.out.print("Enter task number to edit (1 to " + tasks.size() + "): ");
            int taskIndex = scanner.nextInt() - 1; // Convert to zero-based index
            scanner.nextLine(); // Consume the newline character
            if (taskIndex >= 0 && taskIndex < tasks.size()) {
                System.out.print("Enter new task: ");
                tasks.set(taskIndex, scanner.nextLine()); // Update task description

                // Update importance level
                System.out.print("Enter new importance level (1- Hard / 2- Medium / 3- Easy): ");
                int inputChoice = Main.ValidChoice(1, 3); // Validate user input
                if (inputChoice == 1) {
                    importanceLevels.set(taskIndex, "Hard");
                } else if (inputChoice == 2) {
                    importanceLevels.set(taskIndex, "Medium");
                } else {
                    importanceLevels.set(taskIndex, "Easy");
                }
            } else {
                System.out.println("Invalid task number!");
            }
        } else if (editChoice == 2) { // Add a new task
            System.out.print("Enter new task: ");
            tasks.add(scanner.nextLine()); // Add new task description

            // Set importance level for the new task
            System.out.print("Enter importance level (1- Hard / 2- Medium / 3- Easy): ");
            int inputChoice = Main.ValidChoice(1, 3); // Validate user input
            if (inputChoice == 1) {
                importanceLevels.add("Hard");
            } else if (inputChoice == 2) {
                importanceLevels.add("Medium");
            } else {
                importanceLevels.add("Easy");
            }

            completed.add(false); // Default status is incomplete
        } else if (editChoice == 3) { // Mark a task as complete
            System.out.print("Enter task number to mark as complete (1 to " + tasks.size() + "): ");
            int taskIndex = scanner.nextInt() - 1; // Convert to zero-based index
            if (taskIndex >= 0 && taskIndex < tasks.size()) {
                completed.set(taskIndex, true); // Mark task as complete
            } else {
                System.out.println("Invalid task number!");
            }
        }

        // Display the updated list
        displaySortedTasks();
    }
}