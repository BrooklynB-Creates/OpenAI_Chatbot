
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.*;
import java.util.*;

import org.json.JSONObject;
import io.github.cdimascio.dotenv.Dotenv;


public class Chatbot {

    private static OpenAiAssistantEngine assistant;
    private static final File USER_INFO_FILE = new File("user_info.txt");
    private static final File ACU_DATABASE_FILE = new File("acu_database.txt");


    private static final String DB_URL =
    "jdbc:sqlite:" + ACU_DATABASE_FILE.getAbsolutePath();
    
    //questions for the game
    private static final List<String> major_game_questions = List.of(
        "Do you want to hear about Reeves ARCO journey for the 600th time or do you prefer to hear about literally anything else?",
    "Are you a command-line cowboy who prefers to wrangle your code in the terminal or a keyboard-shortcut contortionist who thrives on efficiency?",
    "Do you find yourself debugging with a rubber-duck confessor by your side or searching for answers as a Google search ninja?",
    "Would you rather be caught in a sea of tabs as an open-tab hoarder or writing optimized code like an algorithm optimizer?",
    "Do you want Reeves to yell 'TYPE FASTER' at you or do you prefer to work at your own pace as a semicolon-forgetter?",
    "Are you someone who lives for dark mode as a dark-theme evangelist or someone who feels right at home in light mode as a semicolon-forgetter?",
    "Do you spend your nights as a night-owl coder or an agile story-point calculator, strategizing every sprint?",
    "When you hit a bug, do you consider yourself a keyboard warrior, charging through every line, or a code-comment hater who prefers minimal documentation?",
    "Are you the type to handle your coding errors alone, as a recursive-thinker, or do you rely on Stack Overflow as a stack-overflow scroller?",
    "Do you prefer to keep your desk clean, embracing the lifestyle of a coffee-stained keyboard owner, or thrive on a messy, paper-cluttered desk as a version-control zealot?",
    "Are you someone who loves tweaking algorithms to perfection as an algorithm optimizer or someone who focuses on memory issues like a memory-leak hunter?",
    "Would you rather spend hours on troubleshooting, diving deep as a tunnel-vision multitasker, or jump from task to task, looking for new challenges as an open-tab hoarder?",
    "Do you like to see the big picture as an agile story-point calculator or dive into every detail like a logic-gate guru?",
    "Would you rather stay awake late into the night, fueled by caffeine like a coffee-stained keyboard owner or catch up on much-needed rest as a night-owl coder?",
    "Are you an EOL newline conspiracist who obsesses over code formatting or someone who embraces the beauty of clean code as a curly-braces fanatic?",
    "Would you rather solve complex code problems like a recursive-thinker or optimize performance as an algorithm optimizer?",
    "When you're under pressure, do you tackle problems like a keyboard warrior or a command-line cowboy, riding through the code in the terminal?",
    "Do you feel most at home with a soldering iron in hand, working as a multimeter maestro, or in front of a computer screen, optimizing code as a version-control zealot?"
);



        
    // Check if the user wants to play the game based on their input
    private static boolean promptGame(String userInput) {
                String lc = userInput.toLowerCase();
                return lc.contains("undecided")
                    || lc.contains("not sure")
                    || lc.contains("help picking")
                    || lc.contains("need help choosing")
                    || lc.contains("game");
           }

           //launch major game
    // This method handles the game logic and user interaction
    private static void playGame(BufferedReader reader) throws IOException, SQLException {

        Map<String,Integer> scores = new HashMap<>();//stores the scores for each major

        File gameDatabaseFile = new File("acu_database1.txt"); //connect to the game database
        String gameDbUrl = "jdbc:sqlite:" + gameDatabaseFile.getAbsolutePath();
        
        //print the game title
        System.out.println("""                                                    
 _____ _____ _____    _____ _       _   _       _   
|  _  |     |  |  |  |     | |_ ___| |_| |_ ___| |_ 
|     |   --|  |  |  |   --|   | .'|  _| . | . |  _|
|__|__|_____|_____|  |_____|_|_|__,|_| |___|___|_|  

                                              
 _____       _            _____               
|     |___  |_|___ ___   |   __|___ _____ ___ 
| | | | .'| | | . |  _|  |  |  | .'|     | -_|
|_|_|_|__,|_| |___|_|    |_____|__,|_|_|_|___|
          |___|                               
                                                    
        """);
        
        
        System.out.println("Welcome to the \"Pick Your Major\" game!");
        System.out.println("Type 'stop' at any time to end the game.\n\n");

        //loop through the questions and get the answers from the user
        try (Connection conn = DriverManager.getConnection(gameDbUrl)){
            for (String q : major_game_questions) {
                System.out.println("\nGame: " + q);
                String answer = reader.readLine().trim().toLowerCase();
                if (answer.equalsIgnoreCase("stop")) {
                    //allow the user to stop the game at any time
                    System.out.println("Game: You chose to stop the game early. Goodbye!");
                    return;
                }

                //tokenize the answer and check for keywords in the database
                // This regex splits the answer into tokens based on non-word characters
                for (String token : answer.split("\\W+")) {
                    if (token.isBlank()) continue;

                    String sql = "SELECT majorID from major_keywords WHERE lower(keyword) LIKE ?";

                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, "%" + token + "%");
                        ResultSet rs = pstmt.executeQuery();

                        //for each token, check if it matches a keyword in the database
                        // If it does, increment the score for that major
                        while (rs.next()) {
                            String major = rs.getString("majorID");
                            scores.put(major, scores.getOrDefault(major, 0) + 1);
                        }
                    } catch (SQLException e) {
                        System.out.println("Error executing SQL query: " + e.getMessage());
                    }
                }
            }
        }

        //determine the major with the highest score
        String highestMatch = scores.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);

            //display the results to the user
        if (highestMatch != null) {
            System.out.println("Game: Based on your answers, you might be interested in the " + highestMatch + " major!\n\n");
        } else {
            System.out.println("Game: Sorry, we couldn't find a match for your answers.");
        }

        //end game message
        System.out.println("""
      ╔══════════════════════════════╗
      ║      Thanks for Playing!     ║
      ╚══════════════════════════════╝
      """);

        System.out.println("Game: Thanks for playing! Remember, this is just a fun way to explore your options. Good luck with your decision!\n\n");
    }

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String APIKEY = dotenv.get("MY_API_KEY");
       // System.out.println("API Key: " + APIKEY);


        assistant = new OpenAiAssistantEngine(APIKEY);
        System.out.println("-------------------------");
        System.out.println("Setting up AI Academic Advisor...");

        String assistantId = setupAssistant();
        if (assistantId == null) {
            return;
        }

        startInteractiveChat(assistantId);
    }

    private static String setupAssistant() {
        String assistantId = assistant.createAssistant(
                "gpt-3.5-turbo",
                "Personal AI Academic Advisor",
                null, // i dont think this is really needed
                "You are a real-time chat AI Academic Advisor for Abilene Christian University. Address the student by their first and last name based on the user info provided in the user_info.txt file. Provide information about the student's academic journey, courses, and other academic-related topics.",
                null, //not supported by this specific model
                List.of("file_search"),
                null, // we will add this later with the vector store
                0.5,
                0.5,
                null // we will add these later
        );

        if (assistantId == null) {
            System.out.println("Failed to create assistant");
            return null;
        }

        String userInfoFileID = assistant.uploadFile(USER_INFO_FILE, "assistants");
        String acuDatabaseFileID = assistant.uploadFile(ACU_DATABASE_FILE, "assistants");

        if (userInfoFileID == null || acuDatabaseFileID == null) {
            System.out.println("Failed to upload one or more files");
            return null;
        }

        Map<String, String> fileMetadata = new HashMap<>();
        fileMetadata.put(userInfoFileID, "This fileID (user_info.txt) is associated with the user info");
        fileMetadata.put(acuDatabaseFileID, "This fileID (acu_database.txt) is associated with the ACU database");

        String vectorStoreId = assistant.createVectorStore(
                "User Files",
                Arrays.asList(userInfoFileID, acuDatabaseFileID),
                null,
                null,
                fileMetadata
        );

        if (vectorStoreId == null) {
            System.out.println("Failed to create vector store");
            return null;
        }

        Map<String, Object> toolResources = new HashMap<>();
        Map<String, List<String>> fileSearch = new HashMap<>();
        fileSearch.put("vector_store_ids", List.of(vectorStoreId));
        toolResources.put("file_search", fileSearch);

        boolean updateSuccess = assistant.modifyAssistant(
                assistantId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                toolResources,
                null,
                null
        );

        if (!updateSuccess) {
            System.out.println("Failed to update assistant with vector store");
            return null;
        }

        System.out.println("Assistant setup successfully with ID: " + assistantId);
        return assistantId;
    }

    private static void startInteractiveChat(String assistantId) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String threadId = null;

        System.out.println("\n=== ACU AI Academic Advisor Chat ===");
        System.out.println("If you don't know your major or are unsure which on to pick, type 'game' to play a the \"Pick your Major\" game!");
        System.out.println("Type 'exit' to end the conversation");

        try {
            String userInput;
            while (true) {
                System.out.print("\nYou: ");
                userInput = reader.readLine().trim();

                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }
                if (userInput.isEmpty()) {
                    continue;
                }

                if (promptGame(userInput)) {
                    try {
                        playGame(reader);
                    } catch (SQLException | IOException e) {
                        System.out.println("Game error: " + e.getMessage());
                    }
                    continue;
                }

                try {
                    if (threadId == null) {
                        List<JSONObject> messages = List.of(
                            new JSONObject().put("role", "user").put("content", userInput)
                        );
                        threadId = assistant.createThread(messages, null, null);
                        if (threadId == null) {
                            System.out.println("Failed to create thread. Please try again.");
                            continue;
                        }
                    } else {
                        String messageId = assistant.addMessageToThread(threadId, userInput);
                        if (messageId == null) {
                            System.out.println("Failed to send message. Please try again.");
                            continue;
                        }
                    }

                    String runId = assistant.createRun(
                        threadId, assistantId,
                        null, null, null, null,
                        null, null, null, null,
                        null, null, null, null,
                        null, null, null, null
                    );

                    if (runId == null) {
                        System.out.println("Failed to create run. Please try again.");
                        continue;
                    }

                    boolean completed = assistant.waitForRunCompletion(threadId, runId, 60, 1000);
                    if (!completed) {
                        System.out.println("The assistant encountered an issue. Please try again.");
                        continue;
                    }

                    List<String> retrievedMessages = assistant.listMessages(threadId, runId);
                    if (retrievedMessages != null && !retrievedMessages.isEmpty()) {
                        System.out.println("\nAdvisor: " + retrievedMessages.get(0));
                    } else {
                        System.out.println("No response received. Please try again.");
                    }
                } catch (Exception e) {
                    System.out.println("Chat error: " + e.getMessage());
                }
            }

            if (threadId != null) {
                assistant.deleteResource("threads", threadId);
            }
        } catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        }
    }
}
