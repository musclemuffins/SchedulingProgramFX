package schedulingApp.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import schedulingApp.MainApp;
import schedulingApp.exceptions.PhoneNumberException;
import schedulingApp.model.Address;
import schedulingApp.model.Customer;

public class CustomerNewController {
    private Stage dialogStage;
    MainApp mainApp = new MainApp();
    ObservableList<String> countryDropDownList = FXCollections.observableArrayList("United States of America", "Germany");
    StringBuilder nullValues = new StringBuilder();


    public CustomerNewController() {
    }

    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField address2Field;
    @FXML
    private TextField cityField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField phoneField;
    @FXML
    private ChoiceBox countryChoiceBox;

    @FXML
    private void initialize() {
        countryChoiceBox.setItems(countryDropDownList);
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleOk() {
        try {
            checkForNulls();
            Customer newCustomer = new Customer();
            newCustomer.setName(nameField.getText());
            newCustomer.setAddress(addressField.getText());
            newCustomer.setAddress2(address2Field.getText());
            newCustomer.setCity(cityField.getText());
            newCustomer.setPostalCode(postalCodeField.getText());
            newCustomer.setPhone(phoneField.getText());
            newCustomer.setCountry(countryChoiceBox.getValue().toString());

            if (!(phoneField.getText().length() == 10)) throw new PhoneNumberException();

            String countryId;
            if (newCustomer.getCountry().equals("United States of America")) {
                countryId = "1";
            } else {
                countryId = "2";
            }

            newCustomer.setCountryId(countryId);
            newCustomer.insertCity();
            newCustomer.insertAddress();
            String newAddressId = Address.getAddressId(newCustomer.getAddress());
            newCustomer.setAddressId(newAddressId);
            newCustomer.insertCustomer();
            dialogStage.close();

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Missing Input");
            alert.setHeaderText("Missing information: ");
            alert.setContentText(nullValues.toString());

            alert.showAndWait();
            nullValues = new StringBuilder();
        } catch (PhoneNumberException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Phone number must contain 10 digits with no dashes.");
            alert.showAndWait();
        }
    }

    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage;}

    public void checkForNulls() {
        boolean returnBoolean = false;
        if (nameField.getText().isEmpty()) {
            nullValues.append("Customer name \n");
            returnBoolean = true;
        }
        if (addressField.getText().isEmpty()) {
            nullValues.append("Address \n");
            returnBoolean = true;
        }
        if (postalCodeField.getText().isEmpty()) {
            nullValues.append("Postal code \n");
            returnBoolean = true;
        }
        if (cityField.getText().isEmpty()) {
            nullValues.append("City \n");
            returnBoolean = true;
        }
        if (phoneField.getText().isEmpty()) {
            nullValues.append("Phone number \n");
            returnBoolean = true;
        }
        if (countryChoiceBox.getValue().equals("")) {
            nullValues.append("Country \n");
            returnBoolean = true;
        }

        if (returnBoolean == true) {
            throw new NullPointerException();
        }
    }
}



