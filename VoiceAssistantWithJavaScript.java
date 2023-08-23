import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class VoiceAssistantWithJavaScript extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a WebView and a WebEngine
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Load HTML content into the WebEngine
        webEngine.loadContent(getHtmlContent());

        // Create a Scene with the WebView
        Scene scene = new Scene(webView, 800, 600);

        // Set the Scene and Stage properties
        primaryStage.setScene(scene);
        primaryStage.setTitle("Voice Assistant");
        primaryStage.show();
    }

    // Generate the HTML content for the WebView
    private String getHtmlContent() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Voice Assistant</title>\n" +
                "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h3 align=\"center\">Voice Assistant</h3>\n" +
                "    <div id=\"result\"></div>\n" +
                "    <button onclick=\"startConverting();\"><i class=\"fa fa-microphone btn btn-danger\" aria-hidden=\"true\"></i></button>\n" +
                "    <script>\n" +
                "        function sendTextToJava(text) {\n" +
                "            fetch('http://localhost:8080/processText', {\n" +
                "                method: 'POST',\n" +
                "                headers: {\n" +
                "                    'Content-Type': 'application/json'\n" +
                "                },\n" +
                "                body: JSON.stringify({ text: text })\n" +
                "            })\n" +
                "            .then(response => {\n" +
                "                if (response.ok) {\n" +
                "                    console.log('Text sent to Java backend successfully');\n" +
                "                } else {\n" +
                "                    console.error('Failed to send text to Java backend');\n" +
                "                }\n" +
                "            })\n" +
                "            .catch(error => {\n" +
                "                console.error('Error sending text to Java backend:', error);\n" +
                "            });\n" +
                "        }\n" +
                "\n" +
                "        function startConverting() {\n" +
                "            var recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition || window.mozSpeechRecognition || window.msSpeechRecognition)();\n" +
                "            recognition.continuous = true;\n" +
                "            recognition.interimResults = true;\n" +
                "            recognition.lang = 'en-US';\n" +
                "            recognition.start();\n" +
                "\n" +
                "            var finalTranscripts = '';\n" +
                "            var result = document.getElementById('result');\n" +
                "\n" +
                "            recognition.onresult = function(event) {\n" +
                "                var interimTranscripts = '';\n" +
                "                for (var i = event.resultIndex; i < event.results.length; i++) {\n" +
                "                    var transcript = event.results[i][0].transcript;\n" +
                "                    transcript = transcript.replace(\"\\n\", \"<br>\");\n" +
                "                    if (event.results[i].isFinal) {\n" +
                "                        finalTranscripts += transcript;\n" +
                "                    } else {\n" +
                "                        interimTranscripts += transcript;\n" +
                "                    }\n" +
                "                }\n" +
                "\n" +
                "                result.innerHTML = finalTranscripts + '<span style=\"color: #999\">' + interimTranscripts + '</span>';\n" +
                "                var recognizedText = finalTranscripts + interimTranscripts;\n" +
                "\n" +
                "                // Send the recognized text to Java backend\n" +
                "                sendTextToJava(recognizedText);\n" +
                "            };\n" +
                "\n" +
                "            // Event triggered when recognition is started\n" +
                "            recognition.onstart = function() {\n" +
                "                console.log('Speech recognition started');\n" +
                "            };\n" +
                "\n" +
                "            // Event triggered when recognition is ended\n" +
                "            recognition.onend = function() {\n" +
                "                console.log('Speech recognition ended');\n" +
                "            };\n" +
                "\n" +
                "            recognition.onerror = function(event) {\n" +
                "                console.error('Speech recognition error:', event.error);\n" +
                "            };\n" +
                "        }\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }
}
