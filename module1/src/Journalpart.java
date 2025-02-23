import java.io.*; // For file input/output operations
import java.time.LocalDateTime; // For date and time handling
import java.time.format.DateTimeFormatter; // For formatting date and time
import java.util.Objects;
import java.util.Scanner; // For user input handling

public class Journalpart {
    private String journalText = ""; // Stores journal text
    private LocalDateTime dateTime = LocalDateTime.now(); // Stores current date and time
    private String photoPath = ""; // Stores photo file name or path
    
    //Personalize the application
    UserInfo mainUser;

    static {
        new UserInfo();
    }

    public Journalpart(UserInfo mainUserInfo) {
        mainUser = mainUserInfo;
    }
    
    // Getters and Setters
    public String getJournalText() {
        return journalText;
    }

    public void setJournalText(String journalText) {
        this.journalText = journalText;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    // Save journal data to file (supports both file name and full path)
    public void saveToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Journal Text: " + journalText + "\n");
            writer.write("Date and Time: " + dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\n");
            writer.write("Photo: " + (photoPath.isEmpty() ? "None" : photoPath) + "\n");
            System.out.println("\nJournal saved successfully in: " + filePath);
        } catch (IOException e) {
            System.err.println("\nError saving journal: " + e.getMessage());
        }
    }

    // Load journal data from file
    public void loadFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Journal Text: ")) {
                    journalText = line.substring("Journal Text: ".length());
                } else if (line.startsWith("Date and Time: ")) {
                    dateTime = LocalDateTime.parse(line.substring("Date and Time: ".length()), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                } else if (line.startsWith("Photo: ")) {
                    photoPath = line.substring("Photo: ".length());
                    if (photoPath.equals("None")) photoPath = "";
                }
            }
            System.out.println("\nJournal loaded successfully from: " + filePath);
        } catch (IOException e) {
            System.err.println("\nError loading journal: " + e.getMessage());
        }
    }

    // Display journal details
    public void displayJournalDetails() {
        System.out.println("\n📖 Journal Details:");
        System.out.println("-----------------------------");
        System.out.println("📝 Text: " + journalText);
        System.out.println("📅 Date: " + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("🖼️ Photo: " + (photoPath.isEmpty() ? "None" : photoPath));
        System.out.println("-----------------------------\n");
    }

    // Main journaling menu
    public void mainJournal() {
        Scanner scanner = new Scanner(System.in);
        Journalpart journal = new Journalpart(mainUser);

        // Step 1: Ask about mood and rate the day
        System.out.print("How do you feel today? ");
        scanner.nextLine();
        System.out.print("On a scale of 1 to 10, how was your day? ");
        int rating = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Provide encouragement based on rating
        if (rating <= 6) {
            System.out.println("\n🌟 Stay strong! Tomorrow is a new day with new opportunities! Keep going!");
        } else {
            System.out.println("\nThat’s great! May joy and positivity continue to fill your days!🎉");
        }

        // Step 2: Display the menu
        while (true) {
            System.out.println("-------📜 Journal Menu-------");
            System.out.println("1️) Write a new journal entry");
            System.out.println("2) Load an existing journal");
            System.out.println("3️) Display current journal");
            System.out.println("4️) Exit");
            int choice = UserInfo.ValidChoice(1 , 4);
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // User writes a journal entry
                    System.out.print("Enter your journal text: ");
                    journal.setJournalText(scanner.nextLine());

                    // Optional: Attach a photo (file name or full path)
                    System.out.print("Enter photo file name or path (or leave empty if none): ");
                    journal.setPhotoPath(scanner.nextLine().trim());

                    // Save journal entry
                    System.out.print("Enter file name or path to save journal (e.g., journal.txt): ");
                    journal.saveToFile(scanner.nextLine().trim());
                    break;

                case 2:
                    // User loads an existing journal entry
                    System.out.print("Enter file name or path to load journal: ");
                    journal.loadFromFile(scanner.nextLine().trim());
                    break;

                case 3:
                    System.out.println("Enter your password for security");
                    String password = scanner.nextLine();
                    if (Objects.equals(password, mainUser.getPassword())) {
                        // Display the current journal details
                        journal.displayJournalDetails();
                    }
                    else{
                        System.out.println("Password does not match!");
                    }
                    break;

                case 4:
                    // Exit the program
                    System.out.println("\nThank you for journaling!"+ mainUser.getFirstName() +":)");
                    return;

                default:
                    System.out.println("\nInvalid choice! Please select a valid option.");
            }
        }
    }
}
