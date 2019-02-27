package schedulingApp.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import schedulingApp.MainApp;
import schedulingApp.model.reports.AppointmentTypes;

public class ReportAppointmentTypes {

    private Stage dialogStage;
    MainApp mainApp = new MainApp();


    @FXML
    private TableView<AppointmentTypes> reportTable;
    @FXML
    private TableColumn<AppointmentTypes, String> columnMonth;
    @FXML
    private TableColumn<AppointmentTypes, String> columnCheckup;
    @FXML
    private TableColumn<AppointmentTypes, String> columnCleaning;
    @FXML
    private TableColumn<AppointmentTypes, String> columnFilling;
    @FXML
    private TableColumn<AppointmentTypes, String> columnCanal;
    @FXML
    private TableColumn<AppointmentTypes, String> columnCrown;

    public ReportAppointmentTypes() {
    }

    @FXML
    private void initialize() {
        columnMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        columnCheckup.setCellValueFactory(new PropertyValueFactory<>("checkup"));
        columnCleaning.setCellValueFactory(new PropertyValueFactory<>("cleaning"));
        columnFilling.setCellValueFactory(new PropertyValueFactory<>("filling"));
        columnCanal.setCellValueFactory(new PropertyValueFactory<>("canal"));
        columnCrown.setCellValueFactory(new PropertyValueFactory<>("crown"));
        reportTable.setItems(AppointmentTypes.getReports());
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage;}

}
