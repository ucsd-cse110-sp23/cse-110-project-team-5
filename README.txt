SayItAssistant is an app that can take questions in the form of voice inputs to answer questions.

Download and Setup: ens

1. Navigate to https://github.com/ucsd-cse110-sp23/cse-110-project-team-5
2. Switch branches by clicking on "main", then click on the branch named "MS1-Demo".
3. Click on the green "< > Code" button and "Download Zip" to dowload the folder to local computer.
4. In your terminal, change directories as necessary to where the folder is located. 
5. Run the following commands to setup the server
  a. javac src/main/java/sayItAssistant/MyServer.java
  b. java src/main/java/sayItAssistant/MyServer
  
6. If you are using MacOS/Unix based systems, run these commands to run the application:
  a. javac -cp lib/json-20230227.jar:. src/main/java/sayItAssistant/GUI.java
  b. java -cp lib/json-20230227.jar:. src/main/java/sayItAssistant/GUI 

7. If you are on Windows, run these commands to run the application:
  a. javac -cp lib/json-20230227.jar;. src/main/java/sayItAssistant/GUI.java
  b. java -cp lib/json-20230227.jar;. src/main/java/sayItAssistant/GUI 
  
 
App Instructions:

Ask a question to get an answer:
1. Press and hold the "Ask Question" button to start recording and give the voice prompt. After you are done with saying your question, stop pressing the button.
2. The app will load your question transcript on the left panel on the screen. Note this might take a few seconds.
3. The answer to that question will be displayed on the right panel. The size of the app frame might need to be changed based on the length of the answer, to make it fully visible. 
4. You can horizontally scroll to see the full answer, depending on the length of the answer.
5. You can select, highlight and copy parts of, or the full answer.

See question history:
1. The panel on the left shows all the questions asked in your current history and this is saved and loaded everytime the app is closed and opened.
2. You can scroll vertically to see all questions.
3. This list is updated as more questions are asked.

See answer of past question:
1. Select the "Expand" button to see the answer of a past question on the right panel.

Delete a question:
1. Press the "Delete" button (on the right) on a particular question to delete it.
2. Once deleted, the question cannot be recovered.


Clear all question history
1. Press the "Clear All" button at the top of the App window to clear current browsing history permanently. 


  
