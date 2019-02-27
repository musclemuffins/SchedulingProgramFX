package schedulingApp.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import schedulingApp.MainApp;
import schedulingApp.model.Appointment;
import schedulingApp.model.DataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReportConsultantSchedulesController {
    private Stage dialogStage;
    MainApp mainApp = new MainApp();

    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();


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
    private ChoiceBox consultantChoiceBox;
    ObservableList<String> consultantList = FXCollections.observableArrayList(loadConsultants());

    public ReportConsultantSchedulesController() {
    }

    @FXML
    private void initialize() {
        consultantChoiceBox.setItems(consultantList);
        appointmentName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentStartDate.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndDate.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentConsultant.setCellValueFactory(new PropertyValueFactory<>("consultant"));
        appointmentTable.setItems(appointments);
    }

    public static void loadAppointments () {
        try {
            ResultSet rs = DataSource.getConnection().createStatement().executeQuery("SELECT * FROM appointment");
            while (rs.next()) {
                appointments.add(new Appointment(rs.getString("appointmentID"), rs.getString("customerId"), rs.getString("title"),
                        rs.getString("location"), rs.getString("createdBy"), rs.getTimestamp("start"),rs.getTimestamp ("end")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList loadConsultants() {
        ArrayList consultantStringBuilder = new ArrayList();
        try {
            ResultSet rs = DataSource.getConnection().createStatement().executeQuery("SELECT * FROM user");
            while (rs.next()) {
                consultantStringBuilder.add(rs.getString("userName"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return  consultantStringBuilder;
    }

    @FXML
    private void handleFilterConsultants() {
        FilteredList<Appointment> filteredData = new FilteredList<>(appointments);
        filteredData.setPredicate(row -> {
            String consultant = row.getConsultant().toString();
            return consultant.equals(consultantChoiceBox.getSelectionModel().getSelectedItem().toString());
        });
        appointmentTable.setItems(filteredData);

    }


    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage;}

}
