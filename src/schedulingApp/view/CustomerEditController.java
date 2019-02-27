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
import schedulingApp.model.Customer;

public class CustomerEditController {
    private Stage dialogStage;
    private Customer customer;
    private String customerOldName;
    StringBuilder nullValues = new StringBuilder();
    MainApp mainApp = new MainApp();

    public CustomerEditController() {
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
    ObservableList<String> countryDropDownList = FXCollections.observableArrayList("United States of America", "Germany");

    @FXML
    private void initialize() {
        countryChoiceBox.setItems(countryDropDownList);
        this.customerOldName = nameField.getText();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleOk() {
        try {
            checkForNulls();
            customer.setName(nameField.getText());
            customer.setAddress(addressField.getText());
            customer.setAddress2(address2Field.getText());
            customer.setCity(cityField.getText());
            customer.setPostalCode(postalCodeField.getText());
            customer.setPhone(phoneField.getText());
            customer.setCountry(countryChoiceBox.getValue().toString());

            if (!(phoneField.getText().length() == 10)) throw new PhoneNumberException();

            String countryId;
            if (customer.getCountry().equals("United States of America")) {
                countryId = "1";
            } else {
                countryId = "2";
            }
            customer.setCountryId(countryId);
            customer.editCity();
            customer.editAddress();
            customer.editCustomer(customer.getCustomerId());
            dialogStage.close();
        }catch (NullPointerException e) {
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

    public void setCustomer(Customer customer) {
        this.customer = customer;
        nameField.setText(customer.getName());
        addressField.setText(customer.getAddress());
        address2Field.setText(customer.getAddress2());
        cityField.setText(customer.getCity());
        postalCodeField.setText(customer.getPostalCode());
        phoneField.setText(customer.getPhone());
        countryChoiceBox.setValue(customer.getCountry());
        customer.setCustomerId(Customer.getCustomerId(customer.getName()));
        customer.setAddressId(Customer.getAddressId(customer.getName()));
    }

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
