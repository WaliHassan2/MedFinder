import LinkedLists.LLOrdered;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import Objects.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Hollan on October 4, 2016
 * CSC 202 Assignment 2
 * Northern Virginia Community College
 */

public class AssignmentTwo extends Application {
    /**
     * The purpose of this program is to allow users to create a profile
     * to be stored in a database with their personal information, and to allow
     * for users to login into the system.
     */


    /**
     * implements number of user instances
     */
    // limits the number of users allowed to sign up
    private static int SIZE = 10;
    // calls a new instance of User class
    //private static ArraySortedList users = new ArraySortedList(SIZE);
    private static LLOrdered users = new LLOrdered();
    // main method


    /**
     * Returns the data as an observable list of Hospitals
     * @return
     */


    public static void main(String[] args) {
        launch(args);
        int i = 0;
        while (i < SIZE) {
            i++;
        }
    }

    File photoFile;
    String inputUserName = "";
    String inputPassword = "";

    @Override
    public void start(Stage mainStage) throws Exception {
        //HospitalFX window = new HospitalFX();
        //loadHospital();
        ImageView imageView = new ImageView();

        BorderPane main = new BorderPane();
        GridPane fieldSetting1 = new GridPane();
        fieldSetting1.setPadding(new Insets(40, 0, 0, 50));
        fieldSetting1.setHgap(5);
        fieldSetting1.setVgap(5);

        main.setCenter(fieldSetting1);

        /**
         * Login/Authentication Page 1 Settings
         */
        VBox fields2 = new VBox();
        Label UserNameLbl = new Label("User Name");
        UserNameLbl.setTextFill(Color.web("#f0f8ff"));
        UserNameLbl.setStyle("-fx-font: bold 10pt \"Helvetica\";");
        Label PasswordLbl = new Label("Password");
        PasswordLbl.setTextFill(Color.web("#f0f8ff"));
        PasswordLbl.setStyle("-fx-font: bold 10pt \"Helvetica\";");
        TextField userNameTxtFld1 = new TextField();
        PasswordField passwordTxtFld1 = new PasswordField();
        Label ErrorLbl = new Label("Invalid Username/Password!");
        ErrorLbl.setTextFill(Color.web("#ff0000"));
        ErrorLbl.setStyle("-fx-font: bold 12pt \"Helvetica\";");
        ErrorLbl.setVisible(false);
        Button loginBtn = new Button("\t\t  Authenticate\t\t\t");
        loginBtn.setStyle("-fx-font: italic 10pt \"Helvetica\";" + "-fx-background-color:  #f8e8cc");


        /**
         * Authentication Page GridPane Placement/Node Position Settings
         */
        fields2.getChildren().add(loginBtn);
        fieldSetting1.add(ErrorLbl, 1, 0);
        fieldSetting1.add(UserNameLbl, 0, 1);
        fieldSetting1.add(PasswordLbl, 0, 2);
        fieldSetting1.add(userNameTxtFld1, 1, 1);
        fieldSetting1.add(passwordTxtFld1, 1, 2);
        fieldSetting1.add(loginBtn, 1, 3);

        /**
         * GridPane CSS Styling/Background Image
         */
        String image1 = this.getClass().getResource("/Images/mountain.jpg").toExternalForm();
        fieldSetting1.setStyle(" -fx-background-image: url('" + image1 + "');  "
                + "-fx-background-position: center center; "
                + "-fx-background-repeat: stretch;");

        /**
         * Authentication Primary Stage Settings
         */
        Scene scene = new Scene(main, 475, 300);
        scene.getStylesheets().add(getClass().getResource("/Stylesheets/JavaFX.css").toExternalForm());
        mainStage.setTitle("\t\t         User Login");
        mainStage.setScene(scene);
        mainStage.show();

        /**
         * 1st Authenticate Button Action
         */
        loginBtn.setOnMouseClicked(e -> {

            try {
                File file = new java.io.File("userDatabase.txt");
                Scanner dataReader = new Scanner(file);
                String[] user = new String[0];
                while (dataReader.hasNextLine()) {
                    for (user = new String[0]; dataReader.hasNextLine(); ) {
                        user = dataReader.nextLine().split(" ");
//                        System.out.println(user[3]);
//                        System.out.println(user[4]);

                        /**
                         * Authentication Page 1 TextField CSS Error Styling
                         */
                        ObservableList<String> styleClass10 = userNameTxtFld1.getStyleClass();
                        ObservableList<String> styleClass11 = passwordTxtFld1.getStyleClass();
                        if (userNameTxtFld1.getText().trim().length() == 0 || !userNameTxtFld1.getText().equals(user[3])) {
                            if (!styleClass10.contains("error")) {
                                styleClass10.add("error");
                                userNameTxtFld1.setOnKeyPressed(e23 -> styleClass10.removeAll(Collections.singleton("error")));
                                userNameTxtFld1.setOnMouseClicked(e25 -> ErrorLbl.setVisible(false));
                            }
                        } else {
                            // removes all occurrences:
                            styleClass10.removeAll(Collections.singleton("error"));
                        }
                        if (passwordTxtFld1.getText().trim().length() == 0 || !passwordTxtFld1.getText().equals(user[4])) {
                            if (!styleClass11.contains("error")) {
                                styleClass11.add("error");
                                passwordTxtFld1.setOnKeyPressed(e24 -> styleClass11.removeAll(Collections.singleton("error")));
                                passwordTxtFld1.setOnMouseClicked(e26 -> ErrorLbl.setVisible(false));
                            }
                        } else {
                            // removes all occurrences:
                            styleClass11.removeAll(Collections.singleton("error"));
                        }

                        //Successful Login Statement
                        if (user[3].equals(userNameTxtFld1.getText()) && user[4].equals(passwordTxtFld1.getText())) {

                            ErrorLbl.setVisible(false);



                            Alert welcome = new Alert(Alert.AlertType.INFORMATION);
                            welcome.setTitle("\t\tLogin Successful");
                            welcome.setHeaderText("Welcome To Hospital Seeker!");
                            welcome.setResizable(true);
                            DialogPane dialogPane2 = welcome.getDialogPane();
                            dialogPane2.setStyle("-fx-background-color: rgba(0, 100, 100, 0.2); -fx-background-radius: 10;");
                            String content2 = "You may now search the database " + userNameTxtFld1.getText();
                            welcome.setContentText(content2);
                            welcome.showAndWait();

                            /**
                             * Launches Hospital GUI
                             */
                            HospitalFX hFX = new HospitalFX();
                            try {
                                hFX.start(HospitalFX.showStage);
                                System.exit(0);
                            } catch (Exception e1) {
                                e1.printStackTrace();}

                            //Invalid Login Statement
                        } else {
                            ErrorLbl.setVisible(true);
                        }
                    }
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        });

        /**
         * Hyperlink to SignUp Page
         */
        Hyperlink link = new Hyperlink();
        link.setText("\n\n\t      CLICK HERE\n\t\t     To\n\t   Create Account");
        fieldSetting1.add(link, 1, 5);
        link.setOnMouseClicked(e1 -> {
            userNameTxtFld1.setText("");
            passwordTxtFld1.setText("");
            ObservableList<String> styleClass12 = userNameTxtFld1.getStyleClass();
            ObservableList<String> styleClass13 = passwordTxtFld1.getStyleClass();
            styleClass12.removeAll(Collections.singleton("error"));
            styleClass13.removeAll(Collections.singleton("error"));
            ErrorLbl.setVisible(false);
            /**
             * SignUp Page Borderpane Settings
             */
            //Borderpane declaration
            BorderPane main2 = new BorderPane();

            /**
             * SignUp Page ScrollPane Settings
             */
            ScrollPane scrollPane = new ScrollPane(main2);
            scrollPane.setFitToHeight(true);
            //scrollPane.set


            //Borderpane style sheet
            String image3 = this.getClass().getResource("/Images/mountain2.jpg").toExternalForm();
            main2.setStyle(" -fx-background-image: url('" + image3 + "');  "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch;");

            /**
             * Borderpane Top Label
             */
            Label BorderTitle = new Label("\t\t\t    Fill in the form below to get instant access!");
            BorderTitle.setStyle("-fx-font: bold italic 10pt \"Helvetica\";" + "-fx-background-color: transparent");
            BorderTitle.setTextFill(Color.web("#f0f8ff"));
            main2.setTop(BorderTitle);


            /**
             * SignUp Page Gridpane Settings
             */
            GridPane fieldSetting = new GridPane();
            fieldSetting.setPadding(new Insets(40, 0, 0, 50));
            fieldSetting.setHgap(20);
            fieldSetting.setVgap(15);
            fieldSetting.setStyle("-fx-background-color: rgba(0, 100, 100, 0.2); -fx-background-radius: 10;");
            main2.setCenter(fieldSetting);

            //Gridpane Label/TextField declarations, and stylings
            Label FirstNameLbl = new Label("First Name");
            Label LastNameLbl = new Label("Last Name");
            LastNameLbl.setStyle("-fx-font: bold 12pt \"Helvetica\";");
            Label EmailLbl = new Label("Email Address ");
            Label PhoneLbl = new Label("Phone Number");
            PhoneLbl.setStyle("-fx-font: bold 12pt \"Helvetica\";");
            Label SocialLbl = new Label("Social Security Number");
            Label dobLbl = new Label("Date of Birth");
            dobLbl.setStyle("-fx-font: bold 12pt \"Helvetica\";");
            Label GenderLbl = new Label("Gender");
            Label PasswdLbl = new Label("Password");
            Label UserLbl = new Label("Username");
            Label CPasswdLbl = new Label("Confirm Password");
            CPasswdLbl.setStyle("-fx-font: bold 12pt \"Helvetica\";");
            CPasswdLbl.setTextFill(Color.web("#f0f8ff"));
            Label PhotoLbl = new Label("Upload Profile Photo");
            PhotoLbl.setTextFill(Color.web("#f0f8ff"));
            PhotoLbl.setStyle("-fx-font: bold 12pt \"Helvetica\";");
            FirstNameLbl.setTextFill(Color.web("#f0f8ff"));
            FirstNameLbl.setStyle("-fx-font: bold 12pt \"Helvetica\";");
            LastNameLbl.setTextFill(Color.web("#f0f8ff"));
            EmailLbl.setTextFill(Color.web("#f0f8ff"));
            EmailLbl.setStyle("-fx-font: bold 12pt \"Helvetica\";");
            PhoneLbl.setTextFill(Color.web("#f0f8ff"));
            SocialLbl.setTextFill(Color.web("#f0f8ff"));
            SocialLbl.setStyle("-fx-font: bold 12pt \"Helvetica\";");
            dobLbl.setTextFill(Color.web("#f0f8ff"));
            GenderLbl.setTextFill(Color.web("#f0f8ff"));
            UserLbl.setTextFill(Color.web("#f0f8ff"));
            UserLbl.setStyle("-fx-font: bold 12pt \"Helvetica\";");
            PasswdLbl.setTextFill(Color.web("#f0f8ff"));
            PasswdLbl.setStyle("-fx-font: bold 12pt \"Helvetica\";");
            TextField FirstNameTxtFld = new TextField();
            FirstNameTxtFld.setPromptText("Enter First Name");
            TextField LastNameTxtFld = new TextField();
            LastNameTxtFld.setPromptText("Enter Last Name");
            TextField EmailTxtFld = new TextField();
            EmailTxtFld.setPromptText("you@example.com ");
            TextField PhoneTxtFld = new TextField();
            PhoneTxtFld.setPromptText("(###) ###-####");
            TextField SocialTxtFld = new TextField();
            SocialTxtFld.setPromptText("###-##-####");
            TextField dobTxtFld = new TextField();
            dobTxtFld.setPromptText("##/##/####");
            TextField UserTxtFld = new TextField();
            UserTxtFld.setPromptText("Enter a Username");
            PasswordField PasswdTxtFld = new PasswordField();
            PasswdTxtFld.setPromptText("Enter a Password");
            PasswordField CPasswdTxtFld = new PasswordField();
            CPasswdTxtFld.setPromptText("Enter Confirmation");
            TextField PhotoTxtFld = new TextField();
            PhotoTxtFld.setPromptText("URL");

            /**
             * SignUp Page VBox settings
             *
             */
            VBox messages = new VBox();
            messages.setStyle("-fx-background-color: rgba(0, 100, 100, 0.2); -fx-background-radius: 10;");
            main2.setBottom(messages);
            messages.setPadding(new Insets(5, 25, 25, 25));
            messages.setSpacing(1);

            /**
             * Labels for SignUp Page VBox
             */
            Label InvalidBlanksLbl = new Label("Red highlighted fields cannot be empty!");
            InvalidBlanksLbl.setTextFill(Color.web("#ff0000"));
            InvalidBlanksLbl.setStyle("-fx-font: bold 12pt \"Helvetica\";");
            messages.getChildren().add(InvalidBlanksLbl);
            InvalidBlanksLbl.setVisible(false);
            Label InvalidEmailLbl = new Label("Email must be in the format of xxxx@xxx.xxx");
            InvalidEmailLbl.setTextFill(Color.web("#ff0000"));
            InvalidEmailLbl.setStyle("-fx-font: bold 12pt \"Helvetica\";");
            messages.getChildren().add(InvalidEmailLbl);
            InvalidEmailLbl.setVisible(false);
            Label InvalidPwLbl = new Label("Password and Confirmation are not the same!");
            InvalidPwLbl.setTextFill(Color.web("#ff0000"));
            InvalidPwLbl.setStyle("-fx-font: bold 12pt \"Helvetica\";");
            messages.getChildren().add(InvalidPwLbl);
            InvalidPwLbl.setVisible(false);
            Label InvalidPw2Lbl = new Label("Password must be at least 8 characters, and case sensitive!" +
                    "\nPassword must also have at least one character from the following categories:" +
                    "\na. Uppercase characters\nb. Lowercase characters \nc. Base 10 digits (0 through 9)" +
                    "\nd. Nonalphanumeric characters:\n ~!@#$%^&*_-+=`|\\(){}[]:;\"\'<>,.?/");
            InvalidPw2Lbl.setTextFill(Color.web("#ff0000"));
            InvalidPw2Lbl.setStyle("-fx-font: bold 12pt \"Helvetica\";");
            messages.getChildren().add(InvalidPw2Lbl);
            InvalidPw2Lbl.setVisible(false);

            /**
             * SignUp Page Toggle Group for gender radio buttons
             */
            ToggleGroup group = new ToggleGroup();
            RadioButton button1 = new RadioButton("Male");
            button1.setToggleGroup(group);
            button1.setSelected(true);
            button1.setTextFill(Color.web("#f0f8ff"));
            button1.setStyle("-fx-font: bold 12pt \"Helvetica\";");
            RadioButton button2 = new RadioButton("Female");
            button2.setToggleGroup(group);
            button2.setTextFill(Color.web("#f0f8ff"));
            button2.setStyle("-fx-font: bold 12pt \"Helvetica\";");
            fieldSetting.add(button1, 1, 6);
            fieldSetting.add(button2, 1, 7);

            /**
             * SignUp Page CheckBox for Terms of Service
             */
            CheckBox terms = new CheckBox("I agree to the Terms of Service");
            terms.setTextFill(Color.web("#f0f8ff"));
            fieldSetting.add(terms, 0, 12);

            /**
             * Buttons for SignUp and Browsing
             */
            //File Browse button
            Button browseBtn = new Button("Browse");
            //Mouse Action Event Controller
            browseBtn.setOnMouseClicked(e2 -> {
                ObservableList<String> styleClass16 = PhotoTxtFld.getStyleClass();
                styleClass16.removeAll(Collections.singleton("error"));
                FileChooser photoChooser = new FileChooser();
                photoChooser.setTitle("Choose a Photo File");
                photoChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("All Images", "*.*"),
                        new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                        new FileChooser.ExtensionFilter("GIF", "*.gif"),
                        new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                        new FileChooser.ExtensionFilter("PNG", "*.png"));

                //Opens File Search Window
                photoFile = photoChooser.showOpenDialog(mainStage);
                PhotoTxtFld.setText(photoFile.getAbsolutePath().toString());
            });

            //SignUp button declaration
            Button signupBtn = new Button("Sign Me Up!");
            signupBtn.setStyle("-fx-font: italic 14pt \"Helvetica\";");

            //SignUp button Action Event Controls
            signupBtn.setOnMouseClicked(e3 -> {
                String inputFirstName = "";
                String inputLastName = "";
                String inputEmail = "";
                String inputConfirmPass = "";
                String inputSocial = "";
                String inputDob = "";
                String inputGender = "";
                String inputPhone = "";
                String inputPhoto = "";
                String statement = "";
                String statement2 = "";
                String pattern = "\\w+@\\w+\\.\\w+";
                String pattern2 = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,64}";

                //Password Authentication Regular Expression Detailed Explanation
                // (?=.*[0-9]) # a digit must occur at least once
                // (?=.*[a-z]) # a lower case letter must occur at least once
                // (?=.*[A-Z]) # an upper case letter must occur at least once
                // (?=.*[@#$%^&+=]) # a special character must occur at least once
                // (?=\\S+$) # no whitespace allowed in the entire string
                // .{8,} # anything, at least eight places though

                /**
                 * SignUp Page TextField Validation Statements and CSS Error Stylings
                 */
                boolean status = false;

                ObservableList<String> styleClass = FirstNameTxtFld.getStyleClass();
                ObservableList<String> styleClass1 = LastNameTxtFld.getStyleClass();
                ObservableList<String> styleClass2 = EmailTxtFld.getStyleClass();
                ObservableList<String> styleClass3 = SocialTxtFld.getStyleClass();
                ObservableList<String> styleClass4 = dobTxtFld.getStyleClass();
                ObservableList<String> styleClass5 = PhoneTxtFld.getStyleClass();
                ObservableList<String> styleClass6 = UserTxtFld.getStyleClass();
                ObservableList<String> styleClass7 = PhotoTxtFld.getStyleClass();
                ObservableList<String> styleClass8 = PasswdTxtFld.getStyleClass();
                ObservableList<String> styleClass9 = CPasswdTxtFld.getStyleClass();
                ObservableList<String> styleClass17 = terms.getStyleClass();

                if ((FirstNameTxtFld.getText().trim().length() == 0) || (LastNameTxtFld.getText().trim().length() == 0)
                        || (EmailTxtFld.getText().trim().length() == 0) || (SocialTxtFld.getText().trim().length() == 0)
                        || (dobTxtFld.getText().trim().length() == 0) || (PhoneTxtFld.getText().trim().length() == 0)
                        || (UserTxtFld.getText().trim().length() == 0) || (PasswdTxtFld.getText().trim().length() == 0)
                        || (PhotoTxtFld.getText().trim().length() == 0) || (CPasswdTxtFld.getText().trim().length() == 0)
                        || (!terms.isSelected())) {
                    InvalidBlanksLbl.setTextFill(Color.RED);
                    InvalidBlanksLbl.setVisible(true);
                    status = true;
                }
                if(!terms.isSelected()){
                    if (!styleClass17.contains("error")) {
                        styleClass17.add("error");
                        terms.setOnMouseClicked(e27 -> styleClass17.removeAll(Collections.singleton("error")));
                    }
                } else {
                    // removes all occurrences:
                    styleClass17.removeAll(Collections.singleton("error"));
                }
                if (FirstNameTxtFld.getText().trim().length() == 0) {
                    if (!styleClass.contains("error")) {
                        styleClass.add("error");
                        FirstNameTxtFld.setOnKeyPressed(e8 -> styleClass.removeAll(Collections.singleton("error")));
                    }
                } else {
                    // removes all occurrences:
                    styleClass.removeAll(Collections.singleton("error"));
                }
                if (LastNameTxtFld.getText().trim().length() == 0) {
                    if (!styleClass1.contains("error")) {
                        styleClass1.add("error");
                        LastNameTxtFld.setOnKeyPressed(e9 -> styleClass1.removeAll(Collections.singleton("error")));
                    }
                } else {
                    // removes all occurrences:
                    styleClass1.removeAll(Collections.singleton("error"));
                }
                if (EmailTxtFld.getText().trim().length() == 0) {
                    if (!styleClass2.contains("error")) {
                        styleClass2.add("error");
                        EmailTxtFld.setOnKeyPressed(e10 -> styleClass2.removeAll(Collections.singleton("error")));
                    }
                } else {
                    // removes all occurrences:
                    styleClass2.removeAll(Collections.singleton("error"));
                }
                if (SocialTxtFld.getText().trim().length() == 0) {
                    if (!styleClass3.contains("error")) {
                        styleClass3.add("error");
                        SocialTxtFld.setOnKeyPressed(e11 -> styleClass3.removeAll(Collections.singleton("error")));
                    }
                } else {
                    // removes all occurrences:
                    styleClass3.removeAll(Collections.singleton("error"));
                }
                if (dobTxtFld.getText().trim().length() == 0) {
                    if (!styleClass4.contains("error")) {
                        styleClass4.add("error");
                        dobTxtFld.setOnKeyPressed(e12 -> styleClass4.removeAll(Collections.singleton("error")));
                    }
                } else {
                    // removes all occurrences:
                    styleClass4.removeAll(Collections.singleton("error"));
                }
                if (PhoneTxtFld.getText().trim().length() == 0) {
                    if (!styleClass5.contains("error")) {
                        styleClass5.add("error");
                        PhoneTxtFld.setOnKeyPressed(e13 -> styleClass5.removeAll(Collections.singleton("error")));
                    }
                } else {
                    // removes all occurrences:
                    styleClass5.removeAll(Collections.singleton("error"));
                }
                if (UserTxtFld.getText().trim().length() == 0) {
                    if (!styleClass6.contains("error")) {
                        styleClass6.add("error");
                        UserTxtFld.setOnKeyPressed(e14 -> styleClass6.removeAll(Collections.singleton("error")));
                    }
                } else {
                    // removes all occurrences:
                    styleClass6.removeAll(Collections.singleton("error"));
                }
                if (PhotoTxtFld.getText().trim().length() == 0) {
                    if (!styleClass7.contains("error")) {
                        styleClass7.add("error");
                        PhotoTxtFld.setOnKeyPressed(e15 -> styleClass7.removeAll(Collections.singleton("error")));
                    }
                } else {
                    // removes all occurrences:
                    styleClass7.removeAll(Collections.singleton("error"));
                }
                if (PasswdTxtFld.getText().trim().length() == 0) {
                    if (!styleClass8.contains("error")) {
                        styleClass8.add("error");
                        PasswdTxtFld.setOnKeyPressed(e16 -> styleClass8.removeAll(Collections.singleton("error")));
                    }
                } else {
                    // removes all occurrences:
                    styleClass8.removeAll(Collections.singleton("error"));
                }
                if (CPasswdTxtFld.getText().trim().length() == 0) {
                    if (!styleClass9.contains("error")) {
                        styleClass9.add("error");
                        CPasswdTxtFld.setOnKeyPressed(e17 -> styleClass9.removeAll(Collections.singleton("error")));
                    }
                } else {
                    // removes all occurrences:
                    styleClass9.removeAll(Collections.singleton("error"));
                }
                fieldSetting.setOnMouseClicked(e18 -> InvalidBlanksLbl.setVisible(false));

                /**
                 * SignUp Page Textfield Variable Assignments
                 */
                inputFirstName = FirstNameTxtFld.getText();
                inputLastName = LastNameTxtFld.getText();
                inputEmail = EmailTxtFld.getText();
                inputUserName = UserTxtFld.getText();
                inputPassword = PasswdTxtFld.getText();
                inputConfirmPass = CPasswdTxtFld.getText();
                inputSocial = SocialTxtFld.getText();
                inputDob = dobTxtFld.getText();
                if (button1.isSelected())
                    inputGender = "male";
                if (button2.isSelected())
                    inputGender = "female";
                inputPhone = PhoneTxtFld.getText();
                if (PhotoTxtFld == null) {
                    inputPhoto = photoFile.getAbsolutePath().toString();
                } else {
                    inputPhoto = PhotoTxtFld.getText();
                }
                statement = EmailTxtFld.getText();
                statement2 = PasswdTxtFld.getText();

                /**
                 * SignUp Page Validation Statements
                 */
                if (!statement2.matches(pattern2)) {
                    InvalidPw2Lbl.setTextFill(Color.RED);
                    InvalidPw2Lbl.setVisible(true);
                    PasswdTxtFld.setOnMouseClicked(e20 -> InvalidPw2Lbl.setVisible(false));
                }
                if (!inputPassword.equals(inputConfirmPass)) {
                    InvalidPwLbl.setTextFill(Color.RED);
                    InvalidPwLbl.setVisible(true);
                    CPasswdTxtFld.setOnMouseClicked(e22 -> InvalidPwLbl.setVisible(false));
                }
                if (!statement.matches(pattern)){
                    InvalidEmailLbl.setTextFill(Color.RED);
                    InvalidEmailLbl.setVisible(true);
                    EmailTxtFld.setOnMouseClicked(e21 -> InvalidEmailLbl.setVisible(false));
                }

                /**
                 * This validation statement creates a User Data File, writes data to the file,
                 * and switches to the Login/Authentication pane
                 */
                if (inputPassword.equals(inputConfirmPass) && statement.matches(pattern)
                        && statement2.matches(pattern2) && status==false) {
                    try {
                        File file = new java.io.File("userDatabase.txt");

                        if (file.exists()) {
                            //reads the entire file and creates a string with all of the values on one line
                            String entireFile = Files.readAllLines(Paths.get("userDatabase.txt")).toString();
//                            Scanner dataReader = new Scanner(entireFile);
//                            String[] user = new String[0];
//                            while (dataReader.hasNextLine()) {
//                                for (user = new String[0]; dataReader.hasNextLine(); ) {
//                                    user = dataReader.nextLine().split(".png");
//                                    for(users.add();user[i];)
//                                    System.out.println(user);
//                                }
//                            }
                            //validates the page ensuring no duplicate account data already exists in the database
                            if (entireFile.contains(UserTxtFld.getText()) || entireFile.contains(EmailTxtFld.getText()) ||
                                    entireFile.contains(SocialTxtFld.getText()) || entireFile.contains(PhoneTxtFld.getText())) {
                                Alert error = new Alert(Alert.AlertType.INFORMATION);
                                error.setTitle("ERROR!");
                                error.setHeaderText("Account Error");
                                error.setResizable(true);
                                DialogPane dialogPane3 = error.getDialogPane();
                                dialogPane3.setStyle("-fx-background-color:  #ff0000; -fx-background-radius: 10;");
                                String content3 = "User Account Info already exists!\n" +
                                        "Log In or Choose Different User Info";
                                error.setContentText(content3);
                                error.showAndWait();
                            } else {
                                //Pop up window pane with user info displayed
                                Alert userInfo = new Alert(Alert.AlertType.INFORMATION);
                                userInfo.setTitle("Information");
                                userInfo.setHeaderText("User Profile");
                                userInfo.setResizable(true);
                                DialogPane dialogPane = userInfo.getDialogPane();
                                dialogPane.setStyle("-fx-background-color: rgba(0, 100, 100, 0.2); -fx-background-radius: 10;");
                                users.add(new User(inputFirstName, inputLastName, inputEmail, inputUserName, inputPassword,
                                        inputSocial, inputDob, inputGender, inputPhone, inputPhoto));
                                String content = "User account created!\n" + "Your First Name Is: " + inputFirstName
                                        + "\t\nLast Name: " + inputLastName + "\t\nEmail: " + inputEmail
                                        + "\t\nUsername: " + inputUserName + "\t\nPassword: ********"
                                        + "\t\nSocial: " + inputSocial + "\t\nD.O.B.:" + inputDob + "\t\nGender: " + inputGender
                                        + "\t\nPhone#: " + inputPhone + "\t\nPhoto Path:" + inputPhoto;
                                //System.out.println(users);
                                try {
                                    BufferedImage bufferedImage = ImageIO.read(photoFile);
                                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                                    imageView.setImage(image);
                                    imageView.setFitHeight(75);
                                    imageView.setFitWidth(75);
                                    imageView.setPreserveRatio(true);
                                } catch (IOException ex) {
                                    Logger.getLogger(AssignmentTwo.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                userInfo.setGraphic(imageView);
                                userInfo.setContentText(content);
                                userInfo.showAndWait();


                                PrintWriter copy = new PrintWriter(new FileWriter(file, true));
                                for (int i = 0; i < users.size(); i++)
                                    copy.append(users + "\n"); //user info inventory
                                copy.close();

                                /**
                                 * Switches the scene from SignUp Page to login/Authentication Page 2
                                 */
                                mainStage.setTitle("            User Login");
                                mainStage.setHeight(300);
                                mainStage.setWidth(475);
                                mainStage.setScene(scene);
                                mainStage.show();
                            }
                            System.out.println("File already exists");
                        } else {

                            Alert userInfo = new Alert(Alert.AlertType.INFORMATION);
                            userInfo.setTitle("Information");
                            userInfo.setHeaderText("User Profile");
                            userInfo.setResizable(true);
                            DialogPane dialogPane = userInfo.getDialogPane();
                            dialogPane.setStyle("-fx-background-color: rgba(0, 100, 100, 0.2); -fx-background-radius: 10;");
                            users.add(new User(inputFirstName, inputLastName, inputEmail, inputUserName, inputPassword, inputSocial, inputDob, inputGender, inputPhone, inputPhoto));
                            String content = "User account created!\n" + "Your First Name Is: " + inputFirstName + "\t\nLast Name: " + inputLastName + "\t\nEmail: " + inputEmail + "\t\nUsername: " + inputUserName + "\t\nPassword: ********" + "\t\nSocial: " + inputSocial + "\t\nD.O.B.:" + inputDob + "\t\nGender: " + inputGender + "\t\nPhone#: " + inputPhone + "\t\nPhoto Path:" + inputPhoto;
                            System.out.println(users);
                            try {
                                BufferedImage bufferedImage = ImageIO.read(photoFile);
                                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                                imageView.setImage(image);
                                imageView.setFitHeight(75);
                                imageView.setFitWidth(75);
                                imageView.setPreserveRatio(true);
                            } catch (IOException ex) {
                                Logger.getLogger(AssignmentTwo.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            userInfo.setGraphic(imageView);
                            userInfo.setContentText(content);
                            userInfo.showAndWait();

                            FileWriter fileWriter = new FileWriter(file);

                            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                            for (int i = 0; i < users.size(); i++)
                                bufferedWriter.write(users + "\n"); //user info inventory

                            bufferedWriter.close();

                            /**
                             * Switches the scene from SignUp Page to login/Authentication Page 3
                             */
                            mainStage.setTitle("            User Login");
                            mainStage.setHeight(300);
                            mainStage.setWidth(475);
                            mainStage.setScene(scene);
                            scene.getStylesheets().add(getClass().getResource("/Stylesheets/JavaFX.css").toExternalForm());
                            mainStage.show();
                        }

                    } catch (IOException ex) {
                        System.out.println("Error writing to file ");
                    }

                    loginBtn.setOnMouseClicked(e4 -> {

                        try {
                            File file = new java.io.File("userDatabase.txt");
                            Scanner dataReader = new Scanner(file);
                            String[] user = new String[0];
                            while (dataReader.hasNextLine()) {
                                for (user = new String[0]; dataReader.hasNextLine(); ) {
                                    user = dataReader.nextLine().split(" ");
//                                    System.out.println(user[3]);
//                                    System.out.println(user[4]);

                                    /**
                                     * Authentication Page 2 TextField CSS Error Styling
                                     */
                                    ObservableList<String> styleClass14 = userNameTxtFld1.getStyleClass();
                                    ObservableList<String> styleClass15 = passwordTxtFld1.getStyleClass();
                                    if (userNameTxtFld1.getText().trim().length() == 0 || !userNameTxtFld1.getText().equals(user[3])) {
                                        if (!styleClass14.contains("error")) {
                                            styleClass14.add("error");
                                            userNameTxtFld1.setOnKeyPressed(e27 -> styleClass14.removeAll(Collections.singleton("error")));
                                            userNameTxtFld1.setOnMouseClicked(e28 -> ErrorLbl.setVisible(false));
                                        }
                                    } else {
                                        // removes all occurrences:
                                        styleClass14.removeAll(Collections.singleton("error"));
                                    }
                                    if (passwordTxtFld1.getText().trim().length() == 0 || !passwordTxtFld1.getText().equals(user[4])) {
                                        if (!styleClass15.contains("error")) {
                                            styleClass15.add("error");
                                            passwordTxtFld1.setOnKeyPressed(e29 -> styleClass15.removeAll(Collections.singleton("error")));
                                            passwordTxtFld1.setOnMouseClicked(e30 -> ErrorLbl.setVisible(false));
                                        }
                                    } else {
                                        // removes all occurrences:
                                        styleClass15.removeAll(Collections.singleton("error"));
                                    }

                                    if (user[3].equals(userNameTxtFld1.getText()) && user[4].equals(passwordTxtFld1.getText())) {
                                        ErrorLbl.setVisible(false);
                                        Alert welcome = new Alert(Alert.AlertType.INFORMATION);
                                        welcome.setTitle("Welcome Back");
                                        welcome.setHeaderText("Login Successful!");
                                        welcome.setResizable(true);
                                        DialogPane dialogPane2 = welcome.getDialogPane();
                                        dialogPane2.setStyle("-fx-background-color: rgba(0, 100, 100, 0.2); -fx-background-radius: 10;");
                                        String content2 = "Let's Get To It " + userNameTxtFld1.getText();
                                        welcome.setContentText(content2);
                                        welcome.showAndWait();
                                        System.exit(0);
                                    } else {
                                        ErrorLbl.setVisible(true);
                                    }
                                }
                            }
                        } catch (IOException e2) {

                            e2.printStackTrace();
                        }
                    });
                }
            });

            /**
             * SignUp Page Gridpane Locations
             */
            fieldSetting.add(FirstNameLbl, 0, 0);
            fieldSetting.add(FirstNameTxtFld, 0, 1);
            fieldSetting.add(LastNameLbl, 1, 0);
            fieldSetting.add(LastNameTxtFld, 1, 1);
            fieldSetting.add(SocialLbl, 0, 2);
            fieldSetting.add(SocialTxtFld, 0, 3);
            fieldSetting.add(dobLbl, 1, 2);
            fieldSetting.add(dobTxtFld, 1, 3);
            fieldSetting.add(PhoneLbl, 0, 4);
            fieldSetting.add(PhoneTxtFld, 0, 5);
            fieldSetting.add(EmailLbl, 1, 4);
            fieldSetting.add(EmailTxtFld, 1, 5);
            fieldSetting.add(UserLbl, 0, 6);
            fieldSetting.add(UserTxtFld, 0, 7);
            fieldSetting.add(PasswdLbl, 0, 8);
            fieldSetting.add(PasswdTxtFld, 0, 9);
            fieldSetting.add(CPasswdLbl, 1, 8);
            fieldSetting.add(CPasswdTxtFld, 1, 9);
            fieldSetting.add(PhotoLbl, 0, 10);
            fieldSetting.add(PhotoTxtFld, 0, 11);
            fieldSetting.add(browseBtn, 1, 11);
            fieldSetting.add(signupBtn, 1, 13);

            /**
             * Hyperlink to Login/Authentication Page 2
             */
            Hyperlink link2 = new Hyperlink();
            link2.setText("Already Have An Account? CLICK HERE");
            link2.setTextFill(Color.web("#f0f8ff"));
            fieldSetting.add(link2, 1, 12);
            link2.setOnMouseClicked(e5 -> {
                mainStage.setTitle("      User Login");
                mainStage.setHeight(300);
                mainStage.setWidth(475);
                mainStage.setScene(scene);
                scene.getStylesheets().add(getClass().getResource("/Stylesheets/JavaFX.css").toExternalForm());
                mainStage.show();
            });

            /**
             * SignUp Page/Primary Stage Settings
             */
            Scene scene2 = new Scene(scrollPane, 600, 800);
            scrollPane.setFitToHeight(true);
            scene2.getStylesheets().add(getClass().getResource("/Stylesheets/JavaFX.css").toExternalForm());
            scene2.getStylesheets().add(getClass().getResource("/Stylesheets/Checkbox.css").toExternalForm());
            mainStage.setScene(scene2);
            mainStage.setTitle("\t\t\t\t   Register Today");
            mainStage.show();
        });
    }
}