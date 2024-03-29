//package src.main.java.sayItAssistant;
import org.json.JSONObject;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.*;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.net.*;



class CreateAccountPage extends JFrame {
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JPasswordField verifyPasswordField;
    public String URL = "http://localhost:8100/";
    Color red = new Color(255, 0, 0);
    Color darkRed = new Color (200, 0, 0);
    Color darkGrey = new Color (50,50,50);
    Color foregroundColor = new Color(200, 200, 200);
    Color lightGrey = new Color(100, 100, 100);

    public CreateAccountPage() {
        super("Create Account Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 600));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBackground(darkGrey);

        JLabel emailLabel = new JLabel("Email:");
        emailTextField = new JTextField(20);
        panel.add(emailLabel);
        panel.add(emailTextField);
        emailLabel.setForeground(foregroundColor);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        passwordLabel.setForeground(foregroundColor);
        panel.add(passwordLabel);
        panel.add(passwordField);

        JLabel verifyPasswordLabel = new JLabel("Verify Password:");
        verifyPasswordField = new JPasswordField(20);
        verifyPasswordLabel.setForeground(foregroundColor);
        panel.add(verifyPasswordLabel);
        panel.add(verifyPasswordField);

        JToggleButton autoLogin = new JToggleButton();
        autoLogin.setText("Auto Login");
        autoLogin.setFocusPainted(false);
        autoLogin.setContentAreaFilled(false);
        autoLogin.setOpaque(true);
        autoLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = autoLogin.isSelected();
                if (selected) {
                    autoLogin.setBackground(Color.GREEN);
                } else {
                    autoLogin.setBackground(Color.RED);
                }
            }
        });


        JButton createButton = new JButton("Create Account");
        createButton.setAlignmentX(CENTER_ALIGNMENT);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle login button click event
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());
                String verifyPassword = new String(verifyPasswordField.getPassword());

                // System.out.println(email);
                // System.out.println(password);
                // System.out.println(verifyPassword);

                if (checkAccountDetails(email, password, verifyPassword)) {
                    JOptionPane.showMessageDialog(CreateAccountPage.this, "Login successful!");

                    if (autoLogin.isSelected()) {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/sayItAssistant/LoginInfo.txt"))) {
                        writer.write(email + "\n");
                        writer.write(password + "\n");
                        writer.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    else {
                        try (FileOutputStream fos = new FileOutputStream("src/main/java/sayItAssistant/LoginInfo.txt")) {
                            // Clear the file by writing an empty byte array
                            fos.write(new byte[0]);
                            System.out.println("File contents cleared successfully.");
                        } catch (IOException ex) {
                            System.out.println("An error occurred while clearing the file: " + ex.getMessage());
                        }
                    }

                    // This creates account on database and gives commandhandler on server this users email
                    createAccount(email, password);


                    
                } else {
                    JOptionPane.showMessageDialog(CreateAccountPage.this, "Passwords do not match!");
                }
            }
        });
        panel.add(autoLogin);
        panel.add(createButton, BorderLayout.CENTER);

        add(panel);
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    public boolean createAccount(String email, String password) {
        boolean result = false;
        String query = "false~" + email + "~" + password;
        try {
            URL url = new URL(URL + "?=" + query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader response = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String jsonString = response.readLine();
            System.out.println("From Create Account: " + jsonString);

            // Since we are creating account, we will always succeed
            AppFrame app = new AppFrame();
            // close Create Account page
            this.dispose();
            app.list.update(jsonString);
            result = true;
          } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: No Connection to Server");
        } 
        return result;
    }

    public static boolean checkAccountDetails(String email, String password, String verifyPassword) {
        boolean result = false;
        if (password.equals(verifyPassword)) {
            result = true;
        }
        if (email == null || password == null || verifyPassword == null) {
            result = false;
        }
        return result;
    }
}


class LoginPage extends JFrame {
    JTextField emailTextField;
    JPasswordField passwordField;
    JToggleButton autoLogin;
    Color red = new Color(255, 0, 0);
    Color darkRed = new Color (200, 0, 0);
    Color darkGrey = new Color (50,50,50);
    Color foregroundColor = new Color(200, 200, 200);
    Color lightGrey = new Color(100, 100, 100);
    public final String URL = "http://localhost:8100/";

    public LoginPage() {
        super("Login Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(darkGrey);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBackground(darkGrey);
        panel.setForeground(foregroundColor);

        JLabel emailLabel = new JLabel("Email:");
        emailTextField = new JTextField(20);
        panel.add(emailLabel);
        panel.add(emailTextField);
        emailLabel.setForeground(foregroundColor);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(foregroundColor);
        passwordField = new JPasswordField(20);
        panel.add(passwordLabel);
        panel.add(passwordField);

        autoLogin = new JToggleButton();
        autoLogin.setText("Auto Login");
        autoLogin.setFocusPainted(false);
        autoLogin.setContentAreaFilled(false);
        autoLogin.setOpaque(true);
        autoLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autoLoginSelect();
            }
        });

        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(CENTER_ALIGNMENT);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle login button click event
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());
                // System.out.println(email);
                // System.out.println(password);

                if (autoLogin.isSelected()) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/sayItAssistant/LoginInfo.txt"))) {
                    writer.write(email + "\n");
                    writer.write(password + "\n");
                    writer.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    try (FileOutputStream fos = new FileOutputStream("src/main/java/sayItAssistant/LoginInfo.txt")) {
                        // Clear the file by writing an empty byte array
                        fos.write(new byte[0]);
                        System.out.println("File contents cleared successfully.");
                    } catch (IOException ex) {
                        System.out.println("An error occurred while clearing the file: " + ex.getMessage());
                    }
                }

                // Before you create, try to get this account from the database
                boolean loggedIn = logIn(email, password);
                // JOptionPane.showMessageDialog(null, "Error: Account not found");
                // verify account
            }
        });
        panel.add(autoLogin);
        panel.add(loginButton);

        add(panel);
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    public boolean logIn(String email, String password) {
        boolean result = false;
        String query = "true~" + email + "~" + password;
        try {
            URL url = new URL(URL + "?=" + query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            /////////////////////////////////////////
            BufferedReader response = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String jsonString = response.readLine();
            System.out.println("From LogIn(): " + jsonString);

            if (jsonString.equals("No matching documents found.")) {
                result = false;
                JOptionPane.showMessageDialog(null, "Error: Account does not exist");
            }
            else {
                AppFrame app = new AppFrame();
                this.dispose();
                app.list.update(jsonString);
                result = true;
            }

            } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: No Connection to Server");
        } 
        return result;
    }

    public static String autoLoad() {
        try {
            File myObj = new File("src/main/java/sayItAssistant/LoginInfo.txt");
            Scanner myReader = new Scanner(myObj);

            String email = "";
            String password = "";
            if (myObj.length() == 0) {
                myReader.close();
                return null;
            }
            email = myReader.nextLine();
            password = myReader.nextLine();
  
            myReader.close();

            return email + " " + password;
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
          }
    }

    public void autoLoginSelect() {
        boolean selected = autoLogin.isSelected();
            if (selected) {
                autoLogin.setBackground(Color.GREEN);
            } else {
                autoLogin.setBackground(Color.RED);
            }
    }
    
}



public class StartupPage extends JFrame {
  Color red = new Color(255, 0, 0);
  Color darkRed = new Color (200, 0, 0);
  Color darkGrey = new Color (50,50,50);
  Color foregroundColor = new Color(200, 200, 200);
  Color lightGrey = new Color(100, 100, 100);

    public StartupPage() {
        super("SayItAssistant");
        this.setPreferredSize(new Dimension(800, 600)); // 1200 width and 1000 height

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));
        panel.setBackground(darkGrey);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBackground(lightGrey);
        // createAccountButton.setForeground(foregroundColor);
        UIManager.put("Button.select", lightGrey);

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle "Create Account" button click event
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        CreateAccountPage page = new CreateAccountPage();
                        page.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    }
                });
            }
        });
        panel.add(createAccountButton);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(lightGrey);
        // loginButton.setForeground(foregroundColor);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle "Login" button click event
                SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                LoginPage page = new LoginPage();
                                page.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                                String result = page.autoLoad();
                                String email = result.substring(0, result.indexOf(" "));
                                String password = result.substring(result.indexOf(" ") + 1);
                                System.out.println("email: " + email);
                                System.out.println("password: " + password);
                                page.emailTextField.setText(email);
                                page.passwordField.setText(password);
                                if (result != null) {
                                    page.autoLogin.setSelected(true);
                                    page.autoLoginSelect();
                                }
                                
                            }
                        });
            }
        });
        panel.add(loginButton);

        add(panel);
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
        revalidate();
    }

    public static void main(String[] args) {
        StartupPage sp = new StartupPage();
        String result = LoginPage.autoLoad();
        if (result != null) {
            String email = result.substring(0, result.indexOf(" "));
            String password = result.substring(result.indexOf(" ") + 1);
            System.out.println("email: " + email);
            System.out.println("password: " + password);
            LoginPage lp = new LoginPage();
            lp.emailTextField.setText(email);
            lp.passwordField.setText(password);
            lp.logIn(email, password);
        }
    }
}
