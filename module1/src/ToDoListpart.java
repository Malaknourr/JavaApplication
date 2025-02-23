import java.io.*;
import java.util.*;

public class ToDoListpart {
    private final List<String> tasks = new ArrayList<>(); // Stores tasks
    private final List<String> importanceLevels = new ArrayList<>(); // Stores task importance
    private final List<Boolean> completed = new ArrayList<>(); // Stores task completion status
    private String date = ""; // Stores the date of the list

    UserInfo mainUser;

    public ToDoListpart(UserInfo userInfo) {
        mainUser = userInfo;
    }

    // Getters and Setters
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public List<String> getTasks() { return tasks; }
    public List<String> getImportanceLevels() { return importanceLevels; }
    public List<Boolean> getCompleted() { return completed; }

    // Display the tasks sorted by importance (Hard -> Medium -> Easy)
    public void displaySortedTasks() {
        System.out.println("\nTo-Do List (Sorted by Importance)");
        System.out.println("--------------------------------------");
        List<String> sortedTasks = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            String status = completed.get(i) ? "Completed" : "Pending";
            sortedTasks.add(importanceLevels.get(i) + ": " + tasks.get(i) + " (" + status + ")");
        }
        sortedTasks.sort((a, b) -> {
            if (a.startsWith("Hard") && !b.startsWith("Hard")) return -1;
            if (b.startsWith("Hard") && !a.startsWith("Hard")) return 1;
            if (a.startsWith("Medium") && b.startsWith("Easy")) return -1;
            if (b.startsWith("Medium") && a.startsWith("Easy")) return 1;
            return 0;
        });
        for (String task : sortedTasks) {
            System.out.println(task);
        }
        System.out.println("--------------------------------------\n");
    }

    // Save the to-do list to a file
    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Date: " + date + "\n");
            for (int i = 0; i < tasks.size(); i++) {
                String status = completed.get(i) ? "Completed" : "Pending";
                writer.write(importanceLevels.get(i) + ": " + tasks.get(i) + " (" + status + ")\n");
            }
            System.out.println("\nTo-Do List saved successfully to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

    // Main To-Do List Menu
    public void mainToDo() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n______📋 To-Do List Menu ______");
            System.out.println("1️⃣ Create a new To-Do List");
            System.out.println("2️⃣ Load and Edit an existing To-Do List");
            System.out.println("3️⃣ Display To-Do List");
            System.out.println("4️⃣ Exit");

            int choice = UserInfo.ValidChoice(1, 4);
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
        date = scanner.nextLine();
        tasks.clear();
        importanceLevels.clear();
        completed.clear();
        System.out.print("Enter the number of tasks: ");
        int numTasks = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        for (int i = 0; i < numTasks; i++) {
            System.out.print("Enter Task " + (i + 1) + ": ");
            tasks.add(scanner.nextLine());
            System.out.print("Enter Importance Level (1- Hard / 2- Medium / 3- Easy): ");
            int inputChoice = UserInfo.ValidChoice(1, 3);
            if (inputChoice == 1) {
                importanceLevels.add("Hard");
            } else if (inputChoice == 2) {
                importanceLevels.add("Medium");
            } else {
                importanceLevels.add("Easy");
            }
            completed.add(false); // Default task status is incomplete
        }
        displaySortedTasks();
        System.out.print("Do you want to save this list? (yes/no): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            System.out.print("Enter file name to save: ");
            saveToFile(scanner.nextLine().trim());
        }
    }

    // Helper method to load and edit an existing To-Do List
    private void loadAndEditList(Scanner scanner) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available! Create a list first.");
            return;
        }
        displaySortedTasks();
        System.out.println("1) Edit a Task\n2) Add a New Task\n3) Mark a Task as Complete");
        System.out.print("Choose an option: ");
        int editChoice = UserInfo.ValidChoice(1, 3);

        if (editChoice == 1) { // Edit a task
            System.out.print("Enter task number to edit (1 to " + tasks.size() + "): ");
            int taskIndex = scanner.nextInt() - 1;
            scanner.nextLine();
            if (taskIndex >= 0 && taskIndex < tasks.size()) {
                System.out.print("Enter new task: ");
                tasks.set(taskIndex, scanner.nextLine());
                System.out.print("Enter new importance level (1- Hard / 2- Medium / 3- Easy): ");
                int inputChoice = UserInfo.ValidChoice(1, 3);
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
            tasks.add(scanner.nextLine());
            System.out.print("Enter importance level (1- Hard / 2- Medium / 3- Easy): ");
            int inputChoice = UserInfo.ValidChoice(1, 3);
            if (inputChoice == 1) {
                importanceLevels.add("Hard");
            } else if (inputChoice == 2) {
                importanceLevels.add("Medium");
            } else {
                importanceLevels.add("Easy");
            }
            completed.add(false);
        } else if (editChoice == 3) { // Mark task as complete
            System.out.print("Enter task number to mark as complete (1 to " + tasks.size() + "): ");
            int taskIndex = scanner.nextInt() - 1;
            if (taskIndex >= 0 && taskIndex < tasks.size()) {
                completed.set(taskIndex, true);
            } else {
                System.out.println("Invalid task number!");
            }
        }
        displaySortedTasks();
    }
}