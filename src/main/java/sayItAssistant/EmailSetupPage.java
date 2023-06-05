// package src.main.java.sayItAssistant;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.model.UpdateOptions;
import org.bson.json.JsonWriterSettings;
import org.json.JSONObject;

class EmailSetupPage extends JFrame {
    String emailOfUser;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField displayNameField;
    private JTextField smtpField;
    private JTextField tlsField;
    private JTextField emailField;
    private JPasswordField passwordField;
    Color red = new Color(255, 0, 0);
    Color darkRed = new Color (200, 0, 0);
    Color darkGrey = new Color (50,50,50);
    Color foregroundColor = new Color(200, 200, 200);
    Color lightGrey = new Color(100, 100, 100);
    public final String db_uri = "mongodb+srv://xicoreyes513:gtejvn59@gettingstarted.pr6de6a.mongodb.net/?retryWrites=true&w=majority";

    public EmailSetupPage(String email) {
        super("Email Setup Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 600));

        this.emailOfUser = email;

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBackground(darkGrey);

        //get firstName, lastName, displayName, smtp host, tls port, email, password from database
        String firstName = "";
        String lastName = "";
        String displayName = "";
        String smtpHost = "";
        String tlsPort = "";
        String emailEmail = "";
        String emailPassword = "";
        try (MongoClient mongoClient = MongoClients.create(db_uri)) {
            MongoDatabase sayItAssistant = mongoClient.getDatabase("say_it_assistant");
            MongoCollection<Document> users = sayItAssistant.getCollection("users");
      
      
            Document doc = users.find(eq("email", email)).first();
      
            String jsonString = doc.toJson().toString();
            JSONObject jsonObject = new JSONObject(jsonString);
            firstName = jsonObject.getString("firstName");
            lastName = jsonObject.getString("lastName");
            displayName = jsonObject.getString("displayName");
            smtpHost = jsonObject.getString("smtp");
            tlsPort = jsonObject.getString("tls");
            emailEmail = jsonObject.getString("emailEmail");
            emailPassword = jsonObject.getString("emailPassword");
        }


        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField(20);
        firstNameField.setText(firstName);
        panel.add(firstNameLabel);
        panel.add(firstNameField);
        firstNameLabel.setForeground(foregroundColor);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField(20);
        lastNameField.setText(lastName);
        lastNameLabel.setForeground(foregroundColor);
        panel.add(lastNameLabel);
        panel.add(lastNameField);

        JLabel displayNameLabel = new JLabel("Display Name:");
        displayNameField = new JTextField(20);
        displayNameField.setText(displayName);
        displayNameLabel.setForeground(foregroundColor);
        panel.add(displayNameLabel);
        panel.add(displayNameField);

        JLabel smtpLabel = new JLabel("SMTP Host:");
        smtpField = new JTextField(20);
        smtpField.setText(smtpHost);
        panel.add(smtpLabel);
        panel.add(smtpField);
        smtpLabel.setForeground(foregroundColor);

        JLabel tlsLabel = new JLabel("TLS Port:");
        tlsField = new JTextField(20);
        tlsField.setText(tlsPort);
        panel.add(tlsLabel);
        panel.add(tlsField);
        tlsLabel.setForeground(foregroundColor);

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        emailField.setText(emailEmail);
        panel.add(emailLabel);
        panel.add(emailField);
        emailLabel.setForeground(foregroundColor);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        passwordField.setText(emailPassword);
        passwordLabel.setForeground(foregroundColor);
        panel.add(passwordLabel);
        panel.add(passwordField);

        JButton saveButton = new JButton("Save");
        saveButton.setAlignmentX(CENTER_ALIGNMENT);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle create email button click event
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String displayName = displayNameField.getText();
                String smtp = smtpField.getText();
                String tls = tlsField.getText();
                String emailEmail = emailField.getText();
                String emailPassword = new String(passwordField.getPassword());

                boolean successful = uploadEmailDetails(email, firstName, lastName, displayName, smtp, tls, emailEmail, emailPassword);
                if (successful) {
                    JOptionPane.showMessageDialog(EmailSetupPage.this, "Email Information Saved!");
                }
                else {
                    JOptionPane.showMessageDialog(EmailSetupPage.this, "Email Information Not Saved.");
                }
                
             }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // if (cancelButton.isSelected()) {
                JOptionPane.showMessageDialog(EmailSetupPage.this, "Setup Canceled!");
                // close the window
                dispose();
            }
        });
        panel.add(saveButton);
        panel.add(cancelButton, BorderLayout.CENTER);

        add(panel);
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    Boolean uploadEmailDetails(String email, String firstName, String lastName, String displayName,
        String smtp, String tls, String emailEmail, String emailPassword) {
        try (MongoClient mongoClient = MongoClients.create(db_uri)) {
            MongoDatabase sayItAssistant = mongoClient.getDatabase("say_it_assistant");
            MongoCollection<Document> users = sayItAssistant.getCollection("users");
      
      
            // Create the filter
            Document filter = new Document("email", email);
      
            // Create the update for password
            Document update = new Document("$set", new Document("firstName", firstName));
            users.updateOne(filter, update, new UpdateOptions().upsert(true));

            update = new Document("$set", new Document("lastName", lastName));
            users.updateOne(filter, update, new UpdateOptions().upsert(true));

            update = new Document("$set", new Document("displayName", displayName));
            users.updateOne(filter, update, new UpdateOptions().upsert(true));

            update = new Document("$set", new Document("smtp", smtp));
            users.updateOne(filter, update, new UpdateOptions().upsert(true));

            update = new Document("$set", new Document("tls", tls));
            users.updateOne(filter, update, new UpdateOptions().upsert(true));

            update = new Document("$set", new Document("emailEmail", emailEmail));
            users.updateOne(filter, update, new UpdateOptions().upsert(true));

            update = new Document("$set", new Document("emailPassword", emailPassword));
            users.updateOne(filter, update, new UpdateOptions().upsert(true));

            String response = "";
            Document doc = users.find(eq("email", email)).first();
            if (doc != null) {
                response = doc.toJson();
                return true;
            } else {
                response = "No matching documents found.";
                return false;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EmailSetupPage("");
            }
        });
    }
}
