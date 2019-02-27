package schedulingApp.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class City {

    private String cityId;
    private String city;
    private String countryId;
    private String country;

    public City() {
    }

    public City(String cityId) {
        this.setCityId(cityId);
        try (Statement statement = DataSource.getConnection().createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM city WHERE cityId = '" + cityId + "'")) {
            results.next();
            this.setCity(results.getString("city"));
            this.setCountryId(results.getString("countryId"));
            this.setCountry(getCountryId());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean isCity (String cityName) {
        try (Statement statement = DataSource.getConnection().createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM city WHERE city = '" + cityName + "'"))
        {
            List<City> cities = new ArrayList<>();
            while(results.next()) {
                City city = new City();
                city.setCityId(results.getString("cityId"));
                cities.add(city);
            }
            if (cities.size() > 0) {
                return true;
            } else {
                return false;
            }
        } catch(SQLException e) {
            System.out.println("Customer.getCustomerId() SQLExeption: " + e.getMessage());
            return false;
        }
    }

    public static String getCityId(String city) {
        try (Statement statement = DataSource.getConnection().createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM city WHERE city = '" + city + "'"))
        {
            results.next();
            String cityId = results.getString("CityId");
            return cityId;
        } catch(SQLException e) {
            System.out.println("City.getCityId() SQLExeption: " + e.getMessage());
            return null;
        }
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        if (country.equals("1")) {
            this.country = "United States of America";
        } else if (country.equals("2")) {
            this.country = "Germany";
        } else {
            this.country = "Default";
        }
    }
}

