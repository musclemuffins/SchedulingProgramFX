package schedulingApp.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Customer {

    private StringProperty name = new SimpleStringProperty("");
    private StringProperty consultant = new SimpleStringProperty("");
    private StringProperty address = new SimpleStringProperty("");
    private StringProperty address2 = new SimpleStringProperty("");
    private StringProperty city = new SimpleStringProperty("");
    private StringProperty country = new SimpleStringProperty("");
    private StringProperty postalCode = new SimpleStringProperty("");
    private StringProperty phone = new SimpleStringProperty("");
    private String countryId;
    private String cityId;
    private String customerId;
    private String addressId;

    public Customer() {
    }

    public Customer(String name, String customerId, String addressId, String consultant) {
        this.name = new SimpleStringProperty(name);
        this.customerId = customerId;
        this.addressId = addressId;
        this.consultant = new SimpleStringProperty(consultant);
        Address address = new Address(addressId);
        this.address = new SimpleStringProperty(address.getAddress());
        this.address2 = new SimpleStringProperty(address.getAddress2());
        this.postalCode = new SimpleStringProperty(address.getPostalCode());
        this.phone = new SimpleStringProperty(address.getPhone());
        this.cityId = address.getCityId();
        City city = new City(this.cityId);
        this.city = new SimpleStringProperty(city.getCity());
        this.country = new SimpleStringProperty(city.getCountry());
        this.countryId = city.getCountryId();
    }

    public boolean insertCustomer() {
        try {
            String query = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + " VALUES (?, ?, ?, now(), ?, now(), ?)";
            PreparedStatement preparedStatement = DataSource.getConnection().prepareStatement(query);
            preparedStatement.setString(1, this.getName());
            preparedStatement.setString(2, this.getAddressId());
            preparedStatement.setString(3, "1");
            preparedStatement.setString(4, DataSource.getUserLoggedIn());
            preparedStatement.setString(5, DataSource.getUserLoggedIn());
            if (!preparedStatement.execute()) {
                return true;
            } else {
                System.out.println("Customer insert failed.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Customer.insertCustomer() failed: " + e.getMessage());
            return false;
        }
    }

    public boolean insertAddress() {
        try {
            String query = "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + " VALUES (?, ?, ?, ?, ?, now(), ?, now(), ?)";
            PreparedStatement preparedStatement = DataSource.getConnection().prepareStatement(query);
            preparedStatement.setString(1, this.getAddress());
            preparedStatement.setString(2, this.getAddress2());
            preparedStatement.setString(3, City.getCityId(this.getCity()));
            preparedStatement.setString(4, this.getPostalCode());
            preparedStatement.setString(5, this.getPhone());
            preparedStatement.setString(6, DataSource.getUserLoggedIn());
            preparedStatement.setString(7, DataSource.getUserLoggedIn());
            if (!preparedStatement.execute()) {
                return true;
            } else {
                System.out.println("Address insert failed.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

        public boolean editCustomer(String customerId) {
        try {
            String query = "UPDATE customer SET customerName = ?, lastUpdate = now(), lastUpdateBy = ? "
                    + " WHERE customerId = ?";
            PreparedStatement preparedStatement = DataSource.getConnection().prepareStatement(query);
            preparedStatement.setString(1, this.getName());
            preparedStatement.setString(2, DataSource.getUserLoggedIn());
            preparedStatement.setString(3, customerId);
            if (!preparedStatement.execute()) {
                return true;
            } else {
                System.out.println("Customer update failed.");
                return false;

            }
        } catch (SQLException e) {
            System.out.println("Customer.editCustomer() failed: " + e.getMessage());
            return false;
        }
    }


    public boolean deleteCustomer() {
        try {
            String query = "DELETE FROM customer WHERE customerName = ?";
            PreparedStatement preparedStatement = DataSource.getConnection().prepareStatement(query);
            preparedStatement.setString(1, this.getName());
            if (!preparedStatement.execute()) {
                return true;
            } else {
                System.out.println("Customer.deleteCustomer() failed.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Customer.deleteCustomer() failed: " + e.getMessage());
            return false;
        }
    }

    public boolean insertCity() {
        if (!City.isCity(this.getCity())) {
            try {
                String query = "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) "
                        + " VALUES (?, ?, now(), ?, now(), ?)";
                PreparedStatement preparedStatement = DataSource.getConnection().prepareStatement(query);
                preparedStatement.setString(1, this.getCity());
                preparedStatement.setString(2, this.getCountryId());
                preparedStatement.setString(3, DataSource.getUserLoggedIn());
                preparedStatement.setString(4, DataSource.getUserLoggedIn());
                if (!preparedStatement.execute()) {
                    return true;
                } else {
                    System.out.println("City insert failed");
                    return false;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        return false;
    }

    public boolean editAddress() {
        try {
            String query = "UPDATE address SET address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ?, lastUpdate = now(), lastUpdateBy = ? "
                    + " WHERE addressId = ?";
            PreparedStatement preparedStatement = DataSource.getConnection().prepareStatement(query);
            preparedStatement.setString(1, this.getAddress());
            preparedStatement.setString(2, this.getAddress2());
            preparedStatement.setString(3, this.getCityId());
            preparedStatement.setString(4, this.getPostalCode());
            preparedStatement.setString(5, this.getPhone());
            preparedStatement.setString(6, DataSource.getUserLoggedIn());
            preparedStatement.setString(7, this.getAddressId());
            if (!preparedStatement.execute()) {
                return true;
            } else {
                System.out.println("Address.editAddress() failed");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Address.editAddress() failed: " + e.getMessage());
            return false;
        }
    }

    public void editCity() {
        try {
            String query = "UPDATE city SET city = ?, countryId = ?, lastUpdate = now(), lastUpdateBy = ? "
                    + " WHERE cityId = ?";
            PreparedStatement preparedStatement = DataSource.getConnection().prepareStatement(query);
            preparedStatement.setString(1, this.getCity());
            preparedStatement.setString(2, this.getCountryId());
            preparedStatement.setString(3, DataSource.getUserLoggedIn());
            preparedStatement.setString(4, cityId);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("City.editCity() failed: " + e.getMessage());
        }
    }



    public boolean deleteAddress() {
        try {
            String query = "DELETE FROM address WHERE addressId = ?";
            PreparedStatement preparedStatement = DataSource.getConnection().prepareStatement(query);
            preparedStatement.setString(1, this.getAddressId());
            if (!preparedStatement.execute()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Address.deleteAddress() failed: " + e.getMessage());
            return false;
        }
    }

    public static String getCustomerId(String customerName) {
        try (Statement statement = DataSource.getConnection().createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM customer WHERE customerName = '" + customerName + "'"))
        {
            results.next();
            String customerId = results.getString("customerId");
            return customerId;
        } catch(SQLException e) {
            System.out.println("Customer.getCustomerId() SQLExeption: " + e.getMessage());
            return null;
        }
    }

    public static String getAddressId(String customerName) {
        try (Statement statement = DataSource.getConnection().createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM customer WHERE customerName = '" + customerName + "'"))
        {
            results.next();
            String addressId = results.getString("addressId");
            return addressId;
        } catch(SQLException e) {
            System.out.println("Customer.getAddressId() SQLExeption: " + e.getMessage());
            return null;
        }
    }

        public static ArrayList getAllCustomerNames() {
        try (Statement statement = DataSource.getConnection().createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM customer"))
        {
            ArrayList returnArray = new ArrayList();
            while(results.next()) {
                returnArray.add(results.getString("customerName"));
            }
            return returnArray;
        } catch(SQLException e) {
            System.out.println(".queryCustomer() SQLExeption: " + e.getMessage());
            return null;
        }
    }

    public static String getCustomerName(String customerID) {
        try (Statement statement = DataSource.getConnection().createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM customer WHERE customerId = '" + customerID + "'"))
        {
            results.next();
            String customerName = results.getString("customerName");
            return customerName;
        } catch(SQLException e) {
            System.out.println("Customer.getCustomerName() SQLExeption: " + e.getMessage());
            return null;
        }
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
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

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getAddress2() {
        return address2.get();
    }

    public StringProperty address2Property() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2.set(address2);
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
}
