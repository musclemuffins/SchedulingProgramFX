package schedulingApp.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import schedulingApp.MainApp;
import schedulingApp.model.Appointment;
import schedulingApp.model.Customer;
import schedulingApp.model.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;


public class MainController {

    private static ObservableList<Customer> customers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    ObservableList<String> monthList = FXCollections.observableArrayList(Month.JANUARY.toString(), Month.FEBRUARY.toString(),
            Month.MARCH.toString(), Month.APRIL.toString(), Month.MAY.toString(), Month.JUNE.toString(), Month.JULY.toString(),
            Month.AUGUST.toString(), Month.SEPTEMBER.toString(), Month.OCTOBER.toString(), Month.NOVEMBER.toString(),
            Month.DECEMBER.toString(), "");


    MainApp mainApp = new MainApp();

    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> columnName;
    @FXML
    private TableColumn<Customer, String> columnAddress;
    @FXML
    private TableColumn<Customer, String> columnAddress2;
    @FXML
    private TableColumn<Customer, String> columnCity;
    @FXML
    private TableColumn<Customer, String> columnPostalCode;
    @FXML
    private TableColumn<Customer, String> columnCountry;
    @FXML
    private TableColumn<Customer, String> columnPhone;
    @FXML
    private TableColumn<Customer, String> columnConsultant;

    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> appointmentName;
    @FXML
    private TableColumn<Appointment, String> appointmentTitle;
    @FXML
    private TableColumn<Appointment, String> appointmentLocation;
    @FXML
    private TableColumn<Appointment, String> appointmentStartDate;
    @FXML
    private TableColumn<Appointment, String> appointmentEndDate;
    @FXML
    private TableColumn<Appointment, String> appointmentConsultant;
    @FXML
    private ChoiceBox monthChoiceBox;

    @FXML
    private javafx.scene.control.TextField weekNumberField;


    public MainController() {
        ArrayList<Appointment> upcomingAppointments = MainController.getUpcomingAppointments();
        if (upcomingAppointments.size() > 0) {
            String appointmentList = "";
            for (Appointment appointment: upcomingAppointments){
                appointmentList += ("Customer: " + appointment.getCustomerName() + " Title: " + appointment.getTitle() + "Date: " + appointment.getStart() + "\n");
            }
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.getDialogPane().setMinWidth(550);
            alert.setTitle("Appointment Reminders");
            alert.setContentText(appointmentList);
            alert.showAndWait();
        }
    }

    @FXML
    private void initialize() {
        monthChoiceBox.setItems(monthList);

        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        columnAddress2.setCellValueFactory(new PropertyValueFactory<>("address2"));
        columnPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        columnConsultant.setCellValueFactory(new PropertyValueFactory<>("consultant"));
        columnCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        columnCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        customerTable.setItems(customers);

        appointmentName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentStartDate.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndDate.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentConsultant.setCellValueFactory(new PropertyValueFactory<>("consultant"));
        appointmentTable.setItems(appointments);
        }

    public static void loadCustomers () {

        try {
            ResultSet rs = DataSource.getConnection().createStatement().executeQuery("SELECT * FROM customer");
            while (rs.next()) {
                customers.add(new Customer(rs.getString("customerName"), rs.getString("customerId"), rs.getString("addressId"),
                        rs.getString("createdBy")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadAppointments () {
        try {
            ResultSet rs = DataSource.getConnection().createStatement().executeQuery("SELECT * FROM appointment");
            while (rs.next()) {
                appointments.add(new Appointment(rs.getString("appointmentID"), rs.getString("customerId"),rs.getString("title"),
                        rs.getString("location"), rs.getString("createdBy"), rs.getTimestamp("start"), rs.getTimestamp ("end")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    @FXML
    private void refreshCustomerTable() {
        customerTable.getItems().clear();
        loadCustomers();
    }

    @FXML
    private void refreshAppointmentTable() {
        appointmentTable.getItems().clear();
        loadAppointments();
    }

    @FXML
    private void handleEditCustomer() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if(selectedCustomer != null) {
            mainApp.showCustomerEditDialogue(selectedCustomer);
            refreshCustomerTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No selection");
            alert.setHeaderText("No customer selected");
            alert.setContentText("Please select a customer to edit.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleEditAppointment() {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if(selectedAppointment != null) {
            mainApp.showAppointmentEditDialogue(selectedAppointment);
            refreshAppointmentTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No selection");
            alert.setHeaderText("No appointment selected");
            alert.setContentText("Please select an appointment to edit.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewCustomer() {
            mainApp.showCustomerNewDialogue();
            refreshCustomerTable();
    }

    @FXML
    private void handleDeleteCustomer() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        int selectedIndex = customerTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            customerTable.getItems().remove(selectedIndex);
            if (selectedCustomer.deleteAddress() && selectedCustomer.deleteCustomer()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Deleted Customer");
                alert.setHeaderText("Confirmation of customer deletion.");
                alert.setContentText("Success!");
                alert.showAndWait();
            }
            refreshAppointmentTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No selection");
            alert.setHeaderText("No customer selected");
            alert.setContentText("Please select which customer you would like to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeleteAppointment() {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        int selectedIndex = appointmentTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            appointmentTable.getItems().remove(selectedIndex);
            Appointment.deleteAppointment(selectedAppointment.getAppointmentId());
            refreshAppointmentTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No selection");
            alert.setHeaderText("No customer selected");
            alert.setContentText("Please select which customer you would like to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewAppointment() {
        mainApp.showAppointmentNewDialogue();
        refreshAppointmentTable();
    }

    @FXML
    private void handleFilterCustomerTable() {
        FilteredList<Customer> filteredData = new FilteredList<>(customers);
        filteredData.setPredicate(row -> {
            String consultant = DataSource.getUserLoggedIn();
            return consultant.equals(row.getConsultant());
        });
        customerTable.setItems(filteredData);

    }

    @FXML
    private void handleNoFilterCustomerTable() {
        customers = FXCollections.observableArrayList();
        MainController.loadCustomers();
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        columnAddress2.setCellValueFactory(new PropertyValueFactory<>("address2"));
        columnPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        columnConsultant.setCellValueFactory(new PropertyValueFactory<>("consultant"));
        columnCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        columnCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        customerTable.setItems(customers);
    }

    @FXML
    private void handleNoFilterAppointmentTable() {
        appointments = FXCollections.observableArrayList();
        MainController.loadAppointments();
        appointmentName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentStartDate.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndDate.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentConsultant.setCellValueFactory(new PropertyValueFactory<>("consultant"));
        appointmentTable.setItems(appointments);
    }

    @FXML
    private void handleFilterAppointmentMonth() {
        weekNumberField.setText(null);
        handleNoFilterAppointmentTable();
        FilteredList<Appointment> filteredData = new FilteredList<>(appointments);
        filteredData.setPredicate(row -> {
            String month = row.getStartMonth().toString();
            return month.equals(monthChoiceBox.getSelectionModel().getSelectedItem().toString());
        });
        appointmentTable.setItems(filteredData);

    }

    @FXML
    private void handleFilterAppointmentWeek() {

        try {
            if (Integer.parseInt(weekNumberField.getText().toString()) < 1 | Integer.parseInt(weekNumberField.getText().toString()) >52 ) {
                throw new NumberFormatException();
            }
            monthChoiceBox.setValue("");
            FilteredList<Appointment> filteredData = new FilteredList<>(appointments);
            filteredData.setPredicate(row -> {
                int weekNumber = row.getWeekNumber();
                return weekNumber == (Integer.parseInt(weekNumberField.getText()));
            });
            appointmentTable.setItems(filteredData);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Week #");
            alert.setContentText("Please enter a valid integer value ranging from 1 to 52.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAppointmentTypes() {
        mainApp.showReportAppointmentTypes();
    }

    @FXML
    private void handleConsultantSchedules() {
        mainApp.showReportConsultantSchedules();
    }

    @FXML
    private void handleOutputCustomersToTerminal() {

        for (Customer x: customers){
            String customerId = "Customer ID: " + x.getCustomerId();
            String customerName = "Customer Name: " + x.getName();
            String customerAddress = "Address: " + x.getAddress();
            String customerAddress2 = "Address2: " + x.getAddress2();
            String customerCity = "City: " + x.getCity();
            String customerPostalCode = "Postal Code: " + x.getPostalCode();
            String customerCountry = "Country: " + x.getCountry();
            String customerPhone = "Phone: " + x.getPhone();
            String customerConsultant = "Consultant: " + x.getConsultant();
            System.out.printf("%-18s|", customerId);
            System.out.printf("%-35s|", customerName);
            System.out.printf("%-40s|", customerAddress);
            System.out.printf("%-30s|", customerAddress2);
            System.out.printf("%-25s|", customerCity);
            System.out.printf("%-25s|", customerPostalCode);
            System.out.printf("%-35s|", customerCountry);
            System.out.printf("%-25s|", customerPhone);
            System.out.printf("%-25s", customerConsultant);
            System.out.println("");
        }
    }
    
    public static ArrayList getUpcomingAppointments() {
        LocalDateTime now = LocalDateTime.now();
        ArrayList<Appointment> upcomingAppointments = new ArrayList();
        for (Appointment appointment: appointments) {
            if (Appointment.stringToLocalDateTime(appointment.getStart()).isAfter(now) 
                    && Appointment.stringToLocalDateTime(appointment.getStart()).isBefore(now.plusMinutes(15))) {
                upcomingAppointments.add(appointment);
            }
        }
        return upcomingAppointments;
    }
    
 
    @FXML
    public void handleExit() {
        DataSource.close();
        System.exit(0);
    }

    public static ObservableList<Appointment> getAppointments() {
        return appointments;
    }
    
    
}
