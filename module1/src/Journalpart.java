// Import necessary classes for file handling, date/time, and user input
import java.io.*; // For file input/output operations
import java.nio.file.Files; // For copying files (used in addPhoto method)
import java.nio.file.StandardCopyOption; // For file copy options
import java.time.LocalDateTime; // For representing date and time
import java.time.format.DateTimeFormatter; // For formatting date and time
import java.util.Scanner; // For reading user input

public class Journalpart {
    // Fields to store journal details
    private String fontType; // Stores the font type (e.g., Arial, Times New Roman)
    private int fontSize; // Stores the font size (e.g., 12, 14)
    private boolean addPhoto; // Indicates whether a photo is added (true/false)
    private String journalText; // Stores the journal text
    private String photoPath; // Stores the path to the photo (if added)
    private LocalDateTime dateTime; // Stores the date and time of the journal entry

    // Constructor to initialize default values
    public Journalpart() {
        this.fontType = "Arial"; // Default font type
        this.fontSize = 12; // Default font size
        this.addPhoto = false; // Default: no photo
        this.journalText = ""; // Initialize journal text as empty
        this.photoPath = ""; // Initialize photo path as empty
        this.dateTime = LocalDateTime.now(); // Set the current date and time
    }

    // Getters and Setters for each field
    public String getFontType() {
        return fontType;
    }

    public void setFontType(String fontType) {
        this.fontType = fontType;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isAddPhoto() {
        return addPhoto;
    }

    public void setAddPhoto(boolean addPhoto) {
        this.addPhoto = addPhoto;
    }

    public String getJournalText() {
        return journalText;
    }

    public void setJournalText(String journalText) {
        this.journalText = journalText;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    // Method to save journal data to a file
    public void saveToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write each field to the file in a structured format
            writer.write("Font Type: " + fontType + "\n"); // Write font type
            writer.write("Font Size: " + fontSize + "\n"); // Write font size
            writer.write("Add Photo: " + addPhoto + "\n"); // Write whether a photo is added
            writer.write("Journal Text: " + journalText + "\n"); // Write journal text
            writer.write("Photo Path: " + photoPath + "\n"); // Write photo path
            writer.write("Date and Time: " + dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\n"); // Write date and time
            System.out.println("Journal data saved to file: " + filePath);
        } catch (IOException e) {
            // Handle errors during file writing
            System.err.println("Error saving journal data to file: " + e.getMessage());
        }
    }

    // Method to load journal data from a file
    public void loadFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                // Check each line and update the corresponding field
                if (line.startsWith("Font Type: ")) {
                    fontType = line.substring("Font Type: ".length()); // Extract font type
                } else if (line.startsWith("Font Size: ")) {
                    fontSize = Integer.parseInt(line.substring("Font Size: ".length())); // Extract font size
                } else if (line.startsWith("Add Photo: ")) {
                    addPhoto = Boolean.parseBoolean(line.substring("Add Photo: ".length())); // Extract addPhoto
                } else if (line.startsWith("Journal Text: ")) {
                    journalText = line.substring("Journal Text: ".length()); // Extract journal text
                } else if (line.startsWith("Photo Path: ")) {
                    photoPath = line.substring("Photo Path: ".length()); // Extract photo path
                } else if (line.startsWith("Date and Time: ")) {
                    dateTime = LocalDateTime.parse(line.substring("Date and Time: ".length()), DateTimeFormatter.ISO_LOCAL_DATE_TIME); // Extract date and time
                }
            }
            System.out.println("Journal data loaded from file: " + filePath);
        } catch (IOException e) {
            // Handle errors during file reading
            System.err.println("Error loading journal data from file: " + e.getMessage());
        }
    }

    // Method to copy a photo to a specified directory
    public void addPhoto(String sourcePhotoPath, String destinationDirectory) {
        File sourceFile = new File(sourcePhotoPath); // Create a File object for the source photo
        File destinationFile = new File(destinationDirectory, sourceFile.getName()); // Create a File object for the destination

        try {
            // Copy the photo from the source path to the destination directory
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            this.photoPath = destinationFile.getAbsolutePath(); // Update the photo path field
            System.out.println("Photo copied to: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            // Handle errors during photo copying
            System.err.println("Error copying photo: " + e.getMessage());
        }
    }

    // Method to display journal details
    public void displayJournalDetails() {
        System.out.println("Font Type: " + fontType); // Print font type
        System.out.println("Font Size: " + fontSize); // Print font size
        System.out.println("Add Photo: " + addPhoto); // Print whether a photo is added
        System.out.println("Journal Text: " + journalText); // Print journal text
        System.out.println("Photo Path: " + photoPath); // Print photo path
        System.out.println("Date and Time: " + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))); // Print formatted date and time
    }

    // Main method for testing
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Create a Scanner object for user input
        Journalpart journal = new Journalpart(); // Create a Journalpart object

        // Set journal details based on user input
        System.out.print("Enter font type: ");
        journal.setFontType(scanner.nextLine()); // Set font type

        System.out.print("Enter font size: ");
        journal.setFontSize(Integer.parseInt(scanner.nextLine())); // Set font size

        System.out.print("Add a photo? (true/false): ");
        journal.setAddPhoto(Boolean.parseBoolean(scanner.nextLine())); // Set addPhoto

        if (journal.isAddPhoto()) {
            // If the user wants to add a photo, ask for the source path and destination directory
            System.out.print("Enter the path to the photo: ");
            String sourcePhotoPath = scanner.nextLine();
            System.out.print("Enter the destination directory to save the photo: ");
            String destinationDirectory = scanner.nextLine();
            journal.addPhoto(sourcePhotoPath, destinationDirectory); // Copy the photo
        }

        System.out.print("Enter journal text: ");
        journal.setJournalText(scanner.nextLine()); // Set journal text

        // Save journal data to a file
        System.out.print("Enter the file path to save journal data: ");
        String saveFilePath = scanner.nextLine();
        journal.saveToFile(saveFilePath); // Save data to the specified file

        // Display journal details
        System.out.println("\nJournal Details:");
        journal.displayJournalDetails(); // Print journal details

        // Load journal data from a file
        System.out.print("\nEnter the file path to load journal data: ");
        String loadFilePath = scanner.nextLine();
        journal.loadFromFile(loadFilePath); // Load data from the specified file

        // Display loaded journal details
        System.out.println("\nLoaded Journal Details:");
        journal.displayJournalDetails(); // Print loaded journal details
    }
}