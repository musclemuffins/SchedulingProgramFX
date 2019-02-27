package schedulingApp.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Address {

    private String addressId;
    private String address;
    private String address2;
    private String cityId;
    private String postalCode;
    private String phone;

    public Address() {

    }

    public Address(String addressId) {
        try (Statement statement = DataSource.getConnection().createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM address WHERE addressId = '" + addressId + "'"))
        {
            results.next();
            this.setAddressId(results.getString("addressId"));
            this.setAddress(results.getString("address"));
            this.setAddress2(results.getString("address2"));
            this.setCityId(results.getString("cityId"));
            this.setPostalCode(results.getString("postalCode"));
            this.setPhone(results.getString("phone"));
        } catch(SQLException e) {
            System.out.println("Address.initializer problem SQLExeption: " + e.getMessage());
        }
    }

    public static String getAddressId(String address) {
        try (Statement statement = DataSource.getConnection().createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM address WHERE address = '" + address + "'"))
        {
            results.next();
            String addressId = results.getString("addressId");
            return addressId;
        } catch(SQLException e) {
            System.out.println("Address.getAddressId() SQLExeption: " + e.getMessage());
            return null;
        }
    }

    public static String getCustomerIdFromAddressId(String addressId) {
        try (Statement statement = DataSource.getConnection().createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM customer WHERE addressId = '" + addressId + "'"))
        {
            results.next();
            String customerId = results.getString("customerId");
            return customerId;
        } catch(SQLException e) {
            System.out.println("Address.getCustomerIdFromAddressId() SQLExeption: " + e.getMessage());
            return null;
        }
    }


    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
