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

class EmailSetupPage extends JFrame {
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

    public EmailSetupPage() {
        super("Email Setup Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 600));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBackground(darkGrey);
        File setup = new File("src/main/java/sayItAssistant/EmailSetupInfo.txt");
        String setupInfo[] = new String[7];
        try{
            Scanner myReader = new Scanner(setup);
            int i = 0;
            while (myReader.hasNextLine()) {
                setupInfo[i] = myReader.nextLine();
                i++;
            }
            System.out.println(setupInfo[0]);
            myReader.close();
        }
        catch (FileNotFoundException e) {
            // Handle the exception (e.g., print an error message)
            e.printStackTrace();
        }

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField(20);
        firstNameField.setText(setupInfo[0]);
        panel.add(firstNameLabel);
        panel.add(firstNameField);
        firstNameLabel.setForeground(foregroundColor);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField(20);
        lastNameField.setText(setupInfo[1]);
        lastNameLabel.setForeground(foregroundColor);
        panel.add(lastNameLabel);
        panel.add(lastNameField);

        JLabel displayNameLabel = new JLabel("Display Name:");
        displayNameField = new JTextField(20);
        displayNameField.setText(setupInfo[2]);
        displayNameLabel.setForeground(foregroundColor);
        panel.add(displayNameLabel);
        panel.add(displayNameField);

        JLabel smtpLabel = new JLabel("SMTP Host:");
        smtpField = new JTextField(20);
        smtpField.setText(setupInfo[3]);
        panel.add(smtpLabel);
        panel.add(smtpField);
        smtpLabel.setForeground(foregroundColor);

        JLabel tlsLabel = new JLabel("TLS Port:");
        tlsField = new JTextField(20);
        tlsField.setText(setupInfo[4]);
        panel.add(tlsLabel);
        panel.add(tlsField);
        tlsLabel.setForeground(foregroundColor);

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        emailField.setText(setupInfo[5]);
        panel.add(emailLabel);
        panel.add(emailField);
        emailLabel.setForeground(foregroundColor);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        passwordField.setText(setupInfo[6]);
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
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                try (FileOutputStream fos = new FileOutputStream("src/main/java/sayItAssistant/EmailSetupInfo.txt")) {
                    // Clear the file by writing an empty byte array
                    fos.write(new byte[0]);
                    System.out.println("File contents cleared successfully.");
                } catch (IOException ex) {
                    System.out.println("An error occurred while clearing the file: " + ex.getMessage());
                }

                System.out.println(email);
                System.out.println(password);
                
                JOptionPane.showMessageDialog(EmailSetupPage.this, "Email Information Saved!");

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/sayItAssistant/EmailSetupInfo.txt"))) {
                writer.write(firstName + "\n");
                writer.write(lastName + "\n");
                writer.write(displayName + "\n");
                writer.write(smtp + "\n");
                writer.write(tls + "\n");
                writer.write(email + "\n");
                writer.write(password + "\n");
                writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                AppFrame frame = new AppFrame();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EmailSetupPage();
            }
        });
    }
}
