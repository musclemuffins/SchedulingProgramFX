package schedulingApp.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import schedulingApp.MainApp;
import schedulingApp.model.Appointment;
import schedulingApp.model.DataSource;
import javafx.scene.control.Label;
import schedulingApp.model.Output;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.control.Button;

public class LoginController {

    private MainApp mainApp;

    @FXML
    private Label signInLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Label timeZoneLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signInButton;

    public LoginController() {
    }

    private void handleSceneChange() {
        Parent main = null;
        try {
            main = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(main);
            Stage stage = MainApp.getStage();
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void Login() throws SQLException {
        DataSource.open();
        String userName = usernameField.getText();
        String password = passwordField.getText();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM user WHERE userName = ? AND password = ?";
        try {
            preparedStatement = DataSource.getConnection().prepareStatement(query);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                
                                ResourceBundle resourceBundle = ResourceBundle.getBundle("schedulingApp.resources.Scheduling", Locale.getDefault());
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(resourceBundle.getString("loginSuccess"));
                alert.setHeaderText(resourceBundle.getString("loginSuccessDescription"));
                alert.showAndWait();
                
                DataSource.setUserLoggedIn(userName);
                Output.recordLogin();
                MainController.loadCustomers();
                MainController.loadAppointments();
                

                
                
                handleSceneChange();

            } else {
                ResourceBundle resourceBundle = ResourceBundle.getBundle("schedulingApp.resources.Scheduling", Locale.getDefault());
                errorLabel.setText(resourceBundle.getString("invalidCredentials"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
    }

    @FXML
    private void initialize() {
        Locale.setDefault(new Locale("ge", "DE"));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("schedulingApp.resources.Scheduling", Locale.getDefault());
        timeZoneLabel.setText(resourceBundle.getString("intro"));
        usernameField.setPromptText(resourceBundle.getString("userName"));
        passwordField.setPromptText(resourceBundle.getString("password"));
        signInLabel.setText(resourceBundle.getString("signIn"));
        signInButton.setText(resourceBundle.getString("signInButton"));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

}
