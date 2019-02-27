package schedulingApp.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import schedulingApp.interfaces.DeleteAppointmentLambda;
import schedulingApp.interfaces.EditAppointmentLambda;
import schedulingApp.interfaces.InsertAppointmentLambda;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import schedulingApp.MainApp;
import schedulingApp.view.MainController;

public class Appointment {

    private StringProperty customerName = new SimpleStringProperty("");
    private StringProperty title = new SimpleStringProperty("");
    private StringProperty location = new SimpleStringProperty("");
    private StringProperty contact = new SimpleStringProperty("");
    private StringProperty url = new SimpleStringProperty("");
    private StringProperty start = new SimpleStringProperty(""); //LocalDateTime string
    private StringProperty end = new SimpleStringProperty(""); //LocalDateTime string
    private StringProperty consultant = new SimpleStringProperty("");
    private int weekNumber;
    private String appointmentId;
    private String customerId;

    public Appointment() {
    }
        
    public Appointment(String appointmentId, String customerId, String title, String location, String createdBy, Timestamp tsStart, Timestamp tsEnd) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.customerName = new SimpleStringProperty(Customer.getCustomerName(customerId));
        this.title = new SimpleStringProperty(title);
        this.location = new SimpleStringProperty(location);
        this.start = new SimpleStringProperty(localDateTimetoString(getLDTfromUtcZdt(tsStart).toLocalDateTime()));
        this.end = new SimpleStringProperty(localDateTimetoString(getLDTfromUtcZdt(tsEnd).toLocalDateTime()));
        this.consultant = new SimpleStringProperty(createdBy);
        this.setWeekNumber();
    }
    
    public static LocalDateTime stringToLocalDateTime (String string) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");
        LocalDateTime ldtStart = LocalDateTime.parse(string, df);
        return ldtStart;
    }
    
    public static String localDateTimetoString (LocalDateTime ldt) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");
        String formatDateTime = ldt.format(df);
        return formatDateTime;
    }
    
    public static LocalDate stringToLocalDate (String string) {
        DateTimeFormatter dFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(string.substring(0, 10), dFormatter);
        return localDate;
    }
    
    public static LocalTime stringToLocalTime (String string) {
        DateTimeFormatter tFormatter = DateTimeFormatter.ofPattern("kk:mm"); 
        LocalTime localTime = LocalTime.parse(string.substring(11), tFormatter);
        return localTime;
    }
    
    public static LocalDateTime LDTtoUTC(LocalDateTime ldt) {
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime zdtStart = ldt.atZone(zid);
        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of("UTC"));
        return utcStart.toLocalDateTime();
    }
    
    public static Timestamp LDTtoTimeStamp (LocalDateTime ldt) {
        return Timestamp.valueOf(ldt);
    }
    
    public static LocalDateTime getLDTFromDateAndTimeFields(DatePicker datePicker, ChoiceBox timeBox) {
        int year = (datePicker.getValue().getYear());
        int month = (datePicker.getValue().getMonthValue());
        int day = (datePicker.getValue().getDayOfMonth());
        String startTime = timeBox.getSelectionModel().getSelectedItem().toString();
        int hour = Integer.parseInt(startTime.substring(0, 2));
        int minute = Integer.parseInt(startTime.substring(3, 5));
        LocalDateTime appointmentDateTime = LocalDateTime.of(year, month, day, hour, minute);
        return appointmentDateTime;
    }
    
    public static ZonedDateTime getLDTfromUtcZdt(Timestamp timestamp) {
        ZoneId newzid = ZoneId.systemDefault();
        ZonedDateTime newzdtStart = timestamp.toLocalDateTime().atZone(ZoneId.of("UTC"));
        ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(newzid);
        return newLocalStart; //returns ZDT in local time
    }
    

    
    public void insertAppointment() {
        insertAppointmentLambda.insertAppointment();
    }

    public static void deleteAppointment(String appointmentId) {
        deleteAppointmentLambda.deleteAppointment(appointmentId);
    }

    public void editAppointment() {
        editAppointmentLambda.editAppointment();
    }

    private InsertAppointmentLambda insertAppointmentLambda = () -> {
        try {
            String query = "INSERT INTO appointment (customerId, title, description, location, contact, url, " +
                    "start, end, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, now(), ?, now(), ?)";
            PreparedStatement preparedStatement;
            preparedStatement = DataSource.getConnection().prepareStatement(query);
            preparedStatement.setString(1, this.getCustomerId());
            preparedStatement.setString(2, this.getTitle());
            preparedStatement.setString(3, "");
            preparedStatement.setString(4, this.getLocation());
            preparedStatement.setString(5, "");
            preparedStatement.setString(6, "");
            preparedStatement.setString(7, LDTtoUTC(stringToLocalDateTime(this.getStart())).toString());
            preparedStatement.setString(8, LDTtoUTC(stringToLocalDateTime(this.getEnd())).toString());
            preparedStatement.setString(9, DataSource.getUserLoggedIn());
            preparedStatement.setString(10, DataSource.getUserLoggedIn());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Appointment.insertAppointment() exception: " + e.getMessage());
        }
    };
    
    private EditAppointmentLambda editAppointmentLambda = () -> {
        try {
            String query = "UPDATE appointment " +
                    "SET title = ?, description = ?, location = ?, start = ?, " +
                    "end = ?, lastUpdate = now(), lastUpdateBy = ? " +
                    "WHERE appointmentId = ?";
            PreparedStatement preparedStatement = DataSource.getConnection().prepareStatement(query);
            preparedStatement.setString(1, this.getTitle());
            preparedStatement.setString(2, " ");
            preparedStatement.setString(3, this.getLocation());
            preparedStatement.setString(4, LDTtoUTC(stringToLocalDateTime(this.getStart())).toString());
            preparedStatement.setString(5, LDTtoUTC(stringToLocalDateTime(this.getEnd())).toString());
            preparedStatement.setString(6, DataSource.getUserLoggedIn());
            preparedStatement.setString(7, this.getAppointmentId());
            preparedStatement.execute();
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Appointment.editAppointment() failed: " + e.getMessage());
        }
    };

    private static DeleteAppointmentLambda deleteAppointmentLambda = (String appointmentId) -> {
        try {
            String query = "DELETE FROM appointment WHERE appointmentId = ?";
            PreparedStatement preparedStatement = DataSource.getConnection().prepareStatement(query);
            preparedStatement.setString(1, appointmentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Appointment.deleteAppointment() failed: " + e.getMessage());
        }
    };
    
    
    public static boolean checkAppointmentOverlap(Appointment newAppointment) {
        ObservableList<Appointment> returnList = FXCollections.observableArrayList();
        ObservableList<Appointment> appointments = MainController.getAppointments();
        LocalDateTime potentialEnd = Appointment.stringToLocalDateTime(newAppointment.getEnd());
        boolean overlapFlag = false;
        try {
            for (Appointment appointment: appointments) {
                LocalDateTime existingStart = Appointment.stringToLocalDateTime(appointment.getStart());
                LocalDateTime existingEnd = Appointment.stringToLocalDateTime(appointment.getEnd());
                if (existingStart.isBefore(potentialEnd) && potentialEnd.isBefore(existingEnd)) {
                    returnList.add(appointment);
                    overlapFlag = true;
                }
            }
            if (overlapFlag)
                throw new Exception();
        } catch (Exception e) {
            
            String overlappingAppointments = "";
            for (Appointment appointment: returnList){
                overlappingAppointments += ("Customer: " + appointment.getCustomerName() + " Title: " + 
                        appointment.getTitle() + "Date: " + appointment.getStart() + " - " + appointment.getEnd() + "\n");
            }
            
            MainApp mainApp = new MainApp();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.getDialogPane().setMinWidth(550);
            alert.setTitle("Overlapping appointments");
            alert.setContentText(overlappingAppointments);
            alert.showAndWait();
        }
        
        return overlapFlag;
    }

    public void setWeekNumber() {
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        weekNumber = (Appointment.stringToLocalDateTime(this.getStart())).get(woy);
    }

    public Month getStartMonth () {
        return Appointment.stringToLocalDateTime(this.getStart()).getMonth();
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getStart() {
        return start.get();
    }

    public StringProperty startProperty() {
        return start;
    }

    public void setStart(String start) {
        this.start.set(start);
    }

    public String getEnd() {
        return end.get();
    }

    public StringProperty endProperty() {
        return end;
    }

    public void setEnd(String end) {
        this.end.set(end);
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getConsultant() {
        return consultant.get();
    }

    public StringProperty consultantProperty() {
        return consultant;
    }

    public void setConsultant(String consultant) {
        this.consultant.set(consultant);
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

}




