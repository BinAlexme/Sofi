import java.util.Scanner;

/**
 * @author Alex on 24/6/2025
 */
public class Main {
    public static void main(String[] args) {
        // Initialize Sofi instance and scanner for user input
        Sofi sofi = new Sofi();
        Scanner scanner = new Scanner(System.in);

        // Prompt user for input and handle messages until exit command is received
        String input = scanner.nextLine();

        while (!input.equals(Sofi.getExit())) {

            sofi.handeMessage(input);

            input = scanner.nextLine();
        }

        // Send a farewell message when the user exits the chat session
        System.out.println("Thank you for chatting with Sofi. Goodbye!");

    }
}