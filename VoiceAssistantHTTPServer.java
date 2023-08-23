import fi.iki.elonen.NanoHTTPD;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class VoiceAssistantHTTPServer extends NanoHTTPD {
    private static final Logger logger = Logger.getLogger(VoiceAssistantHTTPServer.class.getName());

    public VoiceAssistantHTTPServer() {
        super(8080);
    }

    public static void main(String[] args) {
        try {
            VoiceAssistantHTTPServer server = new VoiceAssistantHTTPServer();
            server.start();
            System.out.println("Server started on port 8080");// Start the HTTP server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response serve(IHTTPSession session) {
        Response response = newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "OK");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");

        if (session.getMethod() == Method.OPTIONS) {
            // Handle preflight OPTIONS request
            return response;
        }

        if (session.getMethod() == Method.POST) {
            try {
                Map<String, String> parms = new HashMap<>();
                session.parseBody(parms);

                String recognizedText = parms.get("text");
                String responseText = getJavaResponse(recognizedText);

                return newFixedLengthResponse(responseText);
            } catch (IOException | ResponseException e) {
                e.printStackTrace();
                return newFixedLengthResponse("Error processing request: " + e.getMessage());
            }
        } else {
            return newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED, MIME_PLAINTEXT, "Method not allowed");
        }
    }

    private String getJavaResponse(String recognizedText) {
        if (recognizedText.contains("open website")) {
            openWebsite(recognizedText);
            return "Opening website...";
        } else if (recognizedText.contains("send email")) {
            sendEmail();
            return "Sending email...";
        } else if (recognizedText.contains("shutdown")) {
            shutdownPC();
            return "Shutting down...";
        } else if (recognizedText.contains("search web")) {
            searchWeb(recognizedText);
            return "Searching the web...";
        } else if (recognizedText.contains("open application")) {
            openApplication(recognizedText);
            return "Opening application...";
        } else {
            speakText(recognizedText);
            return "Speaking text...";
        }
    }

    private void openWebsite(String command) {
        String[] words = command.split(" ");
        String website = words[words.length - 1];

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("https://" + website));
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error opening website: " + website, e);
            }
        }
    }
    private void sendEmail() {
        final String fromEmail = "yourmailid@gmail.com";
        final String password = "yourpassword";
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter recipient's email: ");
        String toEmail = scanner.nextLine();
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            String subject = "Hello from Voice Assistant";
            String body = "This is an automated email sent by your voice assistant.";

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            logger.log(Level.WARNING, "Error sending email", e);
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

    private void shutdownPC() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder processBuilder = new ProcessBuilder();

            if (os.contains("win")) {
                // For Windows
                processBuilder.command("shutdown", "/s", "/t", "0");
            } else if (os.contains("mac") || os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                // For macOS and Linux
                processBuilder.command("shutdown", "-h", "now");
            } else {
                System.out.println("Unsupported operating system: " + os);
                return;
            }
            Process process = processBuilder.start();
            process.waitFor();

            System.out.println("Shutting down...");
        } catch (IOException | InterruptedException e) {
            logger.log(Level.WARNING, "Error while shutting down", e);
            System.out.println("Error while shutting down: " + e.getMessage());
        }

    }

    private void searchWeb(String command) {
        String[] words = command.split(" ");
        StringBuilder searchQuery = new StringBuilder();

        // Start building the search query by skipping the first two words
        for (int i = 2; i < words.length; i++) {
            searchQuery.append(words[i]);
            if (i < words.length - 1) {
                searchQuery.append(" ");
            }
        }

        String searchUrl = "https://www.google.com/search?q=" + searchQuery;

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(searchUrl));
            } catch (Exception e) {
                logger.log(Level.SEVERE, "An error occurred while searching the web", e);
            }
        }
    }
    private static String getApplicationPath(String applicationKeyword) {
        // Map application keywords to their corresponding executable paths
        // Replace these with actual paths on your system
        HashMap<String, String> applicationPaths = new HashMap<>();
        applicationPaths.put("calculator", "C:\\Windows\\System32\\calc.exe");
        applicationPaths.put("notepad", "C:\\Windows\\System32\\notepad.exe");
        // Add more application keywords and paths as needed

        return applicationPaths.get(applicationKeyword);
    }

    private void openApplication(String command) {
        String[] words = command.split(" ");

        if (words.length >= 3) { // Check if there are enough words
            String applicationKeyword = words[2].toLowerCase(); // Assuming the application keyword is at index 2

            String applicationPath = getApplicationPath(applicationKeyword);

            if (applicationPath != null) {
                try {
                    Desktop.getDesktop().open(new File(applicationPath));
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error opening application: " + applicationKeyword, e);
                }
            } else {
                System.out.println("Application not found: " + applicationKeyword);
            }
        }
    }

    private void speakText(String text) {
        String googleTranslateBaseUrl = "https://translate.google.com/translate_tts?ie=UTF-8&client=tw-ob&tl=en&q=";
        String encodedText = text.replace(" ", "+");

        try {
            URI uri = new URI(googleTranslateBaseUrl + encodedText);
            Desktop.getDesktop().browse(uri);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error while speaking text", e);
            System.out.println("Error while speaking text: " + e.getMessage());
        }

    }
}
