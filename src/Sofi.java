import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Alex on 24/6/2025
 */
public class Sofi {

    private static final String PREFIX = "Sofi";
    private static final String EXIT = "exit";
    private static final Random rand = new Random();

    // Map to store word swaps for pronoun transformation
    private final Map<String, String> swapMap
            = Map.of(
            "am", "are",
            "was", "were",
            "i", "you",
            "my", "your",
            "are", "am",
            "your", "my",
            "yours", "mine",
            "you", "me",
            "me", "you"
    );

    //Map to store response patterns and corresponding replies
    private final Map<String, List<String>> responseMap
            = Map.of(
            "CAN YOU" , List.of("DON'T YOU BELIEVE THAT I CAN*",
                    "PERHAPS YOU WOULD LIKE TO BE ABLE TO*",
                    "YOU WANT ME TO BE ABLE TO*"),
            "CAN I" , List.of(
                    "DO YOU THINK YOU CAN*",
                    "WOULD YOU LIKE TO BE ABLE TO*",
                    "DO YOU BELIEVE YOU SHOULD BE ABLE TO*"),
            "YOU ARE" , List.of(
                    "WHY DO YOU THINK I AM*",
                    "DOES IT MATTER TO YOU WHETHER I AM*",
                    "WOULD IT CHANGE ANYTHING IF I WERE*",
                    "HOW DO YOU FEEL ABOUT ME BEING*"),
            "YOURE" , List.of(
                    "WHY DO YOU THINK I AM*",
                    "DOES IT MATTER TO YOU WHETHER I AM*",
                    "WOULD IT CHANGE ANYTHING IF I WERE*",
                    "HOW DO YOU FEEL ABOUT ME BEING*"),
            "I DONT" , List.of(
                    "WHY DON'T YOU*",
                    "DO YOU WISH THAT YOU DID*",
                    "DO YOU FEEL THAT YOU SHOULD*",
                    "DO YOU BELIEVE THAT I DON'T*"),
            "I FEEL" , List.of(
                    "WHY DO YOU FEEL*",
                    "DO YOU OFTEN FEEL*",
                    "DO YOU ENJOY FEELING*",
                    "DO YOU BELIEVE THAT I FEEL*"),
            "WHY DONT YOU" , List.of(
                    "DO YOU BELIEVE I DON'T*",
                    "PERHAPS I WILL*",
                    "DO YOU WANT ME TO*"),
            "I WANT" , List.of(
                    "WHY DO YOU WANT*"),
            "ARE YOU" , List.of(
                    "WHY ARE YOU INTERESTED IN WHETHER I AM* OR NOT*",
                    "WOULD YOU PREFER IF I WERE NOT*",
                    "PERHAPS IN YOUR FANTASIES I AM*"),
            "I CANT" , List.of(
                    "HOW DO YOU KNOW YOU CAN'T*?",
                    "PERHAPS YOU CAN*?",
                    "WHAT WOULD IT MEAN IF YOU COULD*?")
    );

    //Default responses for unrecognized inputs
    private final List<String> baseResponse
            = List.of(
            "Interesting, tell me more about that.",
            "Hmm, I didn't expect that. Could you explain further?",
            "I'm curious. What prompted you to say that?"
    );

    /**
     * Constructor for the Sofi class. Initializes the chat session.
     */
    public Sofi(){
        printFirstMessage();
    }

    /**
     * Prints the introductory message for the Sofi chat session.
     */
    private void printFirstMessage()
    {
        System.out.println("Hi, I'm Sofi");
        System.out.printf("Say '%s' to exit.\n", EXIT);
        System.out.println("WHAT'S YOUR PROBLEM?");
    }

    /**
     * Sends a message to the chat interface with the Sofi prefix.
     * @param message The message to be sent.
     */
    private void sendMessage(String message)
    {
        System.out.println(PREFIX + ": " + message);
    }

    /**
     * Handles the incoming message, processes it, and generates a response.
     * @param message The incoming message from the user.
     */
    public void handeMessage(String message)
    {
        AtomicBoolean found = new AtomicBoolean(false);

        // Iterate through response patterns and generate appropriate replies
        responseMap.forEach((key, value) -> {
            Pattern pattern = Pattern.compile(key, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(message);

            if (matcher.find()) {
                found.set(true);
                List<String> responses = responseMap.get(key);
                String semiResponse = responses.get(rand.nextInt(responses.size()));

                StringBuilder builder = new StringBuilder(message);
                builder.replace(matcher.start(), matcher.end() , "");

                String response = semiResponse.replace("*", swapMarks(swapPronouns(builder.toString())));

                sendMessage(response);
            }
        });

        // If no specific pattern matches, provide a generic response
        if (!found.get()) {
            sendMessage(baseResponse.get(rand.nextInt(baseResponse.size())));
        }
    }

    /**
     * Swaps ending punctuation marks to fit the context of a question.
     * @param message The input message to be processed.
     * @return The modified message with punctuation replaced.
     */
    private String swapMarks(String message)
    {
        StringBuilder builder = new StringBuilder(message);
        Pattern pattern = Pattern.compile("\\.(?=[^.]*$)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            builder.replace(matcher.start(), matcher.end() , "?");
        }

        return builder.toString();
    }

    /**
     * Swaps pronouns in the input message according to predefined mappings.
     * @param message The input message to be processed.
     * @return The message with pronouns swapped.
     */
    private String swapPronouns(String message)
    {
        StringBuilder builder = new StringBuilder(message);

        swapMap.forEach((key, value) -> {;
            Pattern pattern = Pattern.compile("\\b"+ key + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(message);

            while (matcher.find()) {
                builder.replace(matcher.start() , matcher.end(), value.toUpperCase());
            }
        });

        return builder.toString();
    }

    /**
     * Gets the exit command for terminating the chat session.
     * @return The exit command.
     */
    public String getExit() {
        return EXIT;
    }
}