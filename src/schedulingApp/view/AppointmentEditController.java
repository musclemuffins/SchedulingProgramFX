package schedulingApp.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import schedulingApp.MainApp;
import schedulingApp.model.Appointment;
import schedulingApp.model.Customer;

import java.time.LocalDateTime;

public class AppointmentEditController {
    private Stage dialogStage;
    Appointment appointment;
    MainApp mainApp = new MainApp();
    ObservableList<String> startTimeList = FXCollections.observableArrayList("08:00", "08:30", "09:00", "09:30",
            "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00");
    ObservableList<String> endTimeList = FXCollections.observableArrayList("08:30", "09:00", "09:30", "10:00",
            "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30");
    ObservableList<String> appointmentTitleList = FXCollections.observableArrayList("Routine Checkup", "Teeth Cleaning",
            "Tooth Filling", "Root Canal", "Dental Crown");
    ObservableList<String> locationList = FXCollections.observableArrayList("Room 1", "Room 2",
            "Room 3", "Room 4", "Room 5");
    ObservableList<Customer> customerNameList = FXCollections.observableArrayList(Customer.getAllCustomerNames());
    StringBuilder nullValues = new StringBuilder();


    public AppointmentEditController() {
    }
    @FXML
    private ChoiceBox customerNameBox;
    @FXML
    private ChoiceBox titleChoiceBox;
    @FXML
    private ChoiceBox locationChoiceBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox startTimeBox;
    @FXML
    private ChoiceBox endTimeBox;

    @FXML
    private void initialize() {
        customerNameBox.setItems(customerNameList);
        titleChoiceBox.setItems(appointmentTitleList);
        locationChoiceBox.setItems(locationList);
        startTimeBox.setItems(startTimeList);
        endTimeBox.setItems(endTimeList);
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleOk() {
        try {
            checkForNulls();
            appointment.setCustomerName(customerNameBox.getSelectionModel().getSelectedItem().toString());
            appointment.setCustomerId(Customer.getCustomerId(customerNameBox.getSelectionModel().getSelectedItem().toString()));
            appointment.setTitle(titleChoiceBox.getSelectionModel().getSelectedItem().toString());
            appointment.setLocation(locationChoiceBox.getSelectionModel().getSelectedItem().toString());
            appointment.setStart(Appointment.localDateTimetoString((Appointment.getLDTFromDateAndTimeFields(datePicker, startTimeBox))));
            appointment.setEnd(Appointment.localDateTimetoString((Appointment.getLDTFromDateAndTimeFields(datePicker, endTimeBox))));
            
            if (!Appointment.checkAppointmentOverlap(appointment)) {
                appointment.editAppointment();
                dialogStage.close();
                MainController.loadAppointments();
            }
        }catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Missing Input");
            alert.setHeaderText("Missing information: ");
            alert.setContentText(nullValues.toString());

            alert.showAndWait();
            nullValues = new StringBuilder();
        }
    }

    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage;}

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
        customerNameBox.setValue(appointment.getCustomerName());
        titleChoiceBox.setValue(appointment.getTitle());
        locationChoiceBox.setValue(appointment.getLocation());
    }
    public void checkForNulls() {
        boolean returnBoolean = false;
        if (customerNameBox.getSelectionModel().isEmpty()) {
            nullValues.append("Customer name \n");
            returnBoolean = true;
        }
        if (datePicker.getValue()==null) {
            nullValues.append("Date \n");
            returnBoolean = true;
        }
        if (titleChoiceBox.getSelectionModel().isEmpty()) {
            nullValues.append("Title \n");
            returnBoolean = true;
        }
        if (locationChoiceBox.getSelectionModel().isEmpty()) {
            nullValues.append("Location \n");
            returnBoolean = true;
        }
        if (startTimeBox.getSelectionModel().isEmpty()) {
            nullValues.append("Start time \n");
            returnBoolean = true;
        }
        if (endTimeBox.getSelectionModel().isEmpty()) {
            nullValues.append("End time \n");
            returnBoolean = true;
        }

        if (returnBoolean == true) {
            throw new NullPointerException();
        }
    }

}
