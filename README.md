Voice-Controlled Assistant Project  
![image](https://github.com/azarudeen3031/voice_assistant/assets/94823262/47ad1ade-0619-4e21-ac96-6ae04ee13227)


This repository contains the code for a voice-controlled assistant project that enables users to interact with a virtual assistant using voice commands. The project utilizes a combination of HTML, JavaScript, and Java technologies to create a seamless and interactive user experience.

Features
Voice Recognition: The project integrates with the browser's SpeechRecognition API to enable users to speak voice commands.
Voice Responses: The assistant responds to recognized voice commands with spoken responses using the SpeechSynthesis API.
Command Processing: Recognized voice commands are processed to trigger various actions or provide relevant information.
Web-Based Interface: The project offers a web-based interface that users can access through their browsers.
Java Backend: The Java backend processes recognized voice commands and sends back responses to the web interface.
JavaFX Visualization: The JavaFX application provides a graphical user interface for the web-based interface.
Components
HTML and JavaScript Interface: The index.html file includes the web-based interface for the voice assistant. Users can click a microphone button to initiate voice recognition. Recognized text is sent to the Java backend for processing.

JavaFX Application: The VoiceAssistantWithJavaScript.java file contains the JavaFX application code. It loads the HTML and JavaScript interface into a WebView, allowing users to interact with the voice assistant.

Java Backend (NanoHTTPD): The VoiceAssistantHTTPServer.java file implements a lightweight HTTP server using the NanoHTTPD library. It receives recognized text from the web interface, processes the commands, and returns responses or actions.

Usage
Clone this repository to your local machine.
Run the JavaFX application using your preferred IDE.
Access the interface by opening a web browser and navigating to http://localhost:8080.
Click the microphone button to start voice recognition and issue commands.
Customization
To customize the assistant's responses or add new actions, modify the JavaScript code in the HTML interface.
Update placeholders in the code (such as email credentials and application paths) with your actual values.
Contributions
Contributions to this project are welcome! If you'd like to add new features, improve existing functionality, or fix issues, feel free to submit a pull request.
