import java.util.Scanner;
public class UserInfo {
    private String firstName;
    private int age;
    private String gender;
    private String password;

    public Scanner input = new Scanner(System.in);

    public UserInfo() {
    }

    //Validate {first , nick} Names
    public static boolean ValidName(String str) {
        for (char c : str.toCharArray()) { //Converts the string to a character array using toCharArray().
            if (!Character.isLetter(c)) { //we use method Character.isLetter(c) to check each character in the string is an alpha
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
                System.out.println("Age must be between 0 and 100. Try again.");
                return false; // Return false for invalid ages
            }
            this.age = age;
            return true; // Return true for valid ages
        } catch (NumberFormatException e) {
            System.out.println("Age must be a valid integer. Try again.");
            return false; // Return false for non-integer input
        }
    }

    //Validate Choices
    public static int ValidChoice(int min, int max) {
        Scanner scanner = new Scanner(System.in);
        while (true) { // Loop until valid input is provided
            System.out.print("Enter your choice (" + min + "-" + max + "): ");
            String userInput = scanner.nextLine().trim(); // Read the user's choice as a string

            try {
                int choice = Integer.parseInt(userInput); // Try to parse the choice as an integer
                if (choice >= min && choice <= max) {
                    return choice; // Return the valid choice
                } else {
                    System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPassword() {
        boolean strong = false;
        do {
            System.out.print("Enter your password: ");
            String password = input.nextLine().trim();
            if (password.length() > 6) {
                strong = true;
            }
            else{
                System.out.println("Password must contain at least 6 characters.");
            }
        } while (!strong);// Continue until a valid password is entered

        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    // Main Information Program
    public void mainUserInfo() {
        // Validate First Name
        while (true) {
            System.out.println("Enter your name LETTERS ONLY ;)");
            String firstName = input.nextLine();
            if (ValidName(firstName)) {
                this.firstName = firstName;
                break;
            } else {
                System.out.println("Invalid name. Please use letters only.");
            }
        }

        // Validate Age
        while (true) {
            System.out.println("Enter your age");
            String age_ = input.nextLine();
            if (ValidAge(age_)) {
                break; // Exit the loop if age is valid
            }
        }

        // Validate Gender Choice
        System.out.println("What is your gender? \n 1- Male\n 2- Female\n 3- Prefer not to say");
        int genderChoice = ValidChoice(1, 3);
        if (genderChoice == 1) {
            gender = "Male";
        } else if (genderChoice == 2) {
            gender = "Female";
        } else {
            gender = "Prefer not to say";
        }

        // Set Password
        setPassword();
    }
}