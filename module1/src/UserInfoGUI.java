import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserInfoGUI extends JFrame {
    // Fields for user input
    private JTextField firstNameField;
    private JTextField ageField;
    private JComboBox<String> genderComboBox;
    private JPasswordField passwordField;
    private JTextField fontTypeField;
    private JTextField fontSizeField;
    private JTextArea journalTextArea;
    private JTextField dateTimeField;
    private JLabel photoLabel; // Label to display the selected photo

    // Constructor
    public UserInfoGUI() {
        // Set up the main window
        setTitle("User Information and Journal Form");
        setSize(800, 600); // Larger size for better layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Create a split pane to separate photo and text areas
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300); // Set initial divider position

        // Left panel for photo
        JPanel photoPanel = new JPanel(new BorderLayout());
        photoPanel.setBorder(BorderFactory.createTitledBorder("Photo Preview")); // Add a border
        photoLabel = new JLabel();
        photoLabel.setHorizontalAlignment(JLabel.CENTER); // Center the photo
        photoPanel.add(photoLabel, BorderLayout.CENTER);

        // Right panel for journal text
        JPanel journalPanel = new JPanel(new BorderLayout());
        journalPanel.setBorder(BorderFactory.createTitledBorder("Journal Text")); // Add a border
        journalTextArea = new JTextArea();
        journalTextArea.setLineWrap(true); // Enable line wrapping
        journalTextArea.setWrapStyleWord(true); // Wrap at word boundaries
        JScrollPane scrollPane = new JScrollPane(journalTextArea); // Add scroll bars
        journalPanel.add(scrollPane, BorderLayout.CENTER);

        // Add photo and journal panels to the split pane
        splitPane.setLeftComponent(photoPanel);
        splitPane.setRightComponent(journalPanel);

        // Create a form panel for user information
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10)); // 7 rows, 2 columns, with spacing
        formPanel.setBorder(BorderFactory.createTitledBorder("User Information")); // Add a border

        // Add components to the form panel
        formPanel.add(new JLabel("First Name (Letters Only):"));
        firstNameField = new JTextField();
        formPanel.add(firstNameField);

        formPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        formPanel.add(ageField);

        formPanel.add(new JLabel("Gender:"));
        String[] genders = {"Male", "Female", "Prefer not to say"};
        genderComboBox = new JComboBox<>(genders);
        formPanel.add(genderComboBox);

        formPanel.add(new JLabel("Password (Min 6 Characters):"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Font Type:"));
        fontTypeField = new JTextField();
        formPanel.add(fontTypeField);

        formPanel.add(new JLabel("Font Size:"));
        fontSizeField = new JTextField();
        formPanel.add(fontSizeField);

        formPanel.add(new JLabel("Date and Time:"));
        dateTimeField = new JTextField();
        dateTimeField.setEditable(false); // Make the field read-only
        dateTimeField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))); // Set current date and time
        formPanel.add(dateTimeField);

        // Create a button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Center-aligned buttons
        JButton browseButton = new JButton("Browse Photo");
        JButton submitButton = new JButton("Submit");
        JButton saveButton = new JButton("Save to File");
        JButton loadButton = new JButton("Load from File");

        // Add buttons to the button panel
        buttonPanel.add(browseButton);
        buttonPanel.add(submitButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        // Add action listeners to buttons
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browsePhoto();
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateAndSubmit();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToFile();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFromFile();
            }
        });

        // Add components to the main panel
        mainPanel.add(formPanel, BorderLayout.NORTH); // Form at the top
        mainPanel.add(splitPane, BorderLayout.CENTER); // Split pane in the center
        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // Buttons at the bottom

        // Add the main panel to the frame
        add(mainPanel);
    }

    // Method to browse and select a photo
    private void browsePhoto() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            // Display the selected photo in the photoLabel
            ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
            Image image = imageIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH); // Resize the image
            photoLabel.setIcon(new ImageIcon(image));
        }
    }

    // Method to validate and submit user information
    private void validateAndSubmit() {
        // Validate First Name
        String firstName = firstNameField.getText().trim();
        if (!ValidName(firstName)) {
            JOptionPane.showMessageDialog(this, "Invalid name. Please use letters only.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate Age
        String ageStr = ageField.getText().trim();
        if (!ValidAge(ageStr)) {
            JOptionPane.showMessageDialog(this, "Invalid age. Please enter a number between 0 and 100.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate Password
        String password = new String(passwordField.getPassword()).trim();
        if (password.length() <= 6) {
            JOptionPane.showMessageDialog(this, "Password must contain at least 6 characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate Font Size
        String fontSizeStr = fontSizeField.getText().trim();
        if (!ValidFontSize(fontSizeStr)) {
            JOptionPane.showMessageDialog(this, "Invalid font size. Please enter a positive integer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get Gender
        String gender = (String) genderComboBox.getSelectedItem();

        // Get Font Type
        String fontType = fontTypeField.getText().trim();

        // Get Font Size
        int fontSize = Integer.parseInt(fontSizeStr);

        // Get Journal Text
        String journalText = journalTextArea.getText().trim();

        // Get Date and Time
        String dateTime = dateTimeField.getText().trim();

        // Display Success Message
        String message = "User Information:\n" +
                "First Name: " + firstName + "\n" +
                "Age: " + ageStr + "\n" +
                "Gender: " + gender + "\n" +
                "Password: " + password + "\n" +
                "Font Type: " + fontType + "\n" +
                "Font Size: " + fontSize + "\n" +
                "Journal Text: " + journalText + "\n" +
                "Date and Time: " + dateTime;
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to save user information and journal data to a file
    private void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Write user information and journal data to the file
                writer.write("First Name: " + firstNameField.getText().trim() + "\n");
                writer.write("Age: " + ageField.getText().trim() + "\n");
                writer.write("Gender: " + genderComboBox.getSelectedItem() + "\n");
                writer.write("Password: " + new String(passwordField.getPassword()).trim() + "\n");
                writer.write("Font Type: " + fontTypeField.getText().trim() + "\n");
                writer.write("Font Size: " + fontSizeField.getText().trim() + "\n");
                writer.write("Journal Text: " + journalTextArea.getText().trim() + "\n");
                writer.write("Date and Time: " + dateTimeField.getText().trim() + "\n");
                JOptionPane.showMessageDialog(this, "User information and journal data saved to file: " + file.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving user information: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to load user information and journal data from a file
    private void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("First Name: ")) {
                        firstNameField.setText(line.substring("First Name: ".length()));
                    } else if (line.startsWith("Age: ")) {
                        ageField.setText(line.substring("Age: ".length()));
                    } else if (line.startsWith("Gender: ")) {
                        genderComboBox.setSelectedItem(line.substring("Gender: ".length()));
                    } else if (line.startsWith("Password: ")) {
                        passwordField.setText(line.substring("Password: ".length()));
                    } else if (line.startsWith("Font Type: ")) {
                        fontTypeField.setText(line.substring("Font Type: ".length()));
                    } else if (line.startsWith("Font Size: ")) {
                        fontSizeField.setText(line.substring("Font Size: ".length()));
                    } else if (line.startsWith("Journal Text: ")) {
                        journalTextArea.setText(line.substring("Journal Text: ".length()));
                    } else if (line.startsWith("Date and Time: ")) {
                        dateTimeField.setText(line.substring("Date and Time: ".length()));
                    }
                }
                JOptionPane.showMessageDialog(this, "User information and journal data loaded from file: " + file.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading user information: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Validate {first, nick} Names
    public static boolean ValidName(String str) {
        if (str == null || str.isEmpty()) {
            return false; // Handle null or empty input
        }
        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    // Validate Age
    public boolean ValidAge(String ageStr) {
        try {
            int age = Integer.parseInt(ageStr);
            if (age < 0 || age > 100) {
                return false; // Return false for invalid ages
            }
            return true; // Return true for valid ages
        } catch (NumberFormatException e) {
            return false; // Return false for non-integer input
        }
    }

    // Validate Font Size
    public boolean ValidFontSize(String fontSizeStr) {
        try {
            int fontSize = Integer.parseInt(fontSizeStr);
            if (fontSize <= 0) {
                return false; // Return false for invalid font sizes
            }
            return true; // Return true for valid font sizes
        } catch (NumberFormatException e) {
            return false; // Return false for non-integer input
        }
    }

    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UserInfoGUI gui = new UserInfoGUI();
                gui.setVisible(true); // Make the window visible
            }
        });
    }
}