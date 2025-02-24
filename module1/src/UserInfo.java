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


    public void setPassword() {
        boolean strong = false;
        String tempPassword;  // Store temporary password
        do {
            System.out.print("Enter your password: ");
            tempPassword = input.nextLine().trim();
            if (tempPassword.length() > 6) {
                strong = true;
            } else {
                System.out.println("Password must contain at least 6 characters.");
            }
        } while (!strong);

        this.password = tempPassword;
    }


    public String getFirstName() {
        return firstName;
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
        int genderChoice = Main.ValidChoice(1, 3);
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