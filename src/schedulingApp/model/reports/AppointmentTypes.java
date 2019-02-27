package schedulingApp.model.reports;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import schedulingApp.model.DataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;

public class AppointmentTypes {

    private Month month;
    private String monthString;
    private int checkup = 0;
    private int cleaning = 0;
    private int filling = 0;
    private int canal = 0;
    private int crown = 0;
    private static String[] appointmentTypes = new String[]{"Routine Checkup", "Teeth Cleaning",
                                            "Tooth Filling", "Root Canal", "Dental Crown"};
    static Month[] monthEnums = {Month.JANUARY, Month.FEBRUARY,
            Month.MARCH, Month.APRIL, Month.MAY, Month.JUNE, Month.JULY,
            Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER,
            Month.DECEMBER};
    private static ObservableList<AppointmentTypes> reports = FXCollections.observableArrayList();


    public AppointmentTypes() {
    }

    public static ObservableList<AppointmentTypes> getReports() {
        for (int m = 0; m<monthEnums.length; m++) {
            AppointmentTypes report = new AppointmentTypes();
            report.setMonth(monthEnums[m]);
            for (int i = 0; i < appointmentTypes.length; i++) {
                String query = "SELECT COUNT(*) " +
                        "FROM appointment " +
                        "WHERE title = '" + appointmentTypes[i] + "' AND MONTH(start) = '" + monthEnums[m].getValue() + "'";
                try {
                    ResultSet rs = DataSource.getConnection().createStatement().executeQuery(query);
                    rs.next();
                    if (appointmentTypes[i].equals("Routine Checkup")) {
                        report.setCheckup(rs.getInt(1));
                    }
                    if (appointmentTypes[i].equals("Teeth Cleaning")) {
                        report.setCleaning(rs.getInt(1));
                    }
                    if (appointmentTypes[i].equals("Tooth Filling")) {
                        report.setFilling(rs.getInt(1));
                    }
                    if (appointmentTypes[i].equals("Root Canal")) {
                        report.setCanal(rs.getInt(1));
                    }
                    if (appointmentTypes[i].equals("Dental Crown")) {
                        report.setCrown(rs.getInt(1));
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            reports.add(report);

        }

        return reports;
    }






    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public int getCheckup() {
        return checkup;
    }

    public void setCheckup(int checkup) {
        this.checkup = checkup;
    }

    public int getCleaning() {
        return cleaning;
    }

    public void setCleaning(int cleaning) {
        this.cleaning = cleaning;
    }

    public int getFilling() {
        return filling;
    }

    public void setFilling(int filling) {
        this.filling = filling;
    }

    public int getCanal() {
        return canal;
    }

    public void setCanal(int canal) {
        this.canal = canal;
    }

    public int getCrown() {
        return crown;
    }

    public void setCrown(int crown) {
        this.crown = crown;
    }

    public String getMonthString() {
        return monthString;
    }

    public void setMonthString(String monthString) {
        this.monthString = monthString;
    }
}
