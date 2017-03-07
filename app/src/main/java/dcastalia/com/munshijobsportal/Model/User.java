package dcastalia.com.munshijobsportal.Model;

/**
 * Created by PT on 3/7/2017.
 */

public class User {
    private String name;
    private String profession;
    private String dateOfBirth;
    private String passportNo;
    private String nationalId;
    private String email;
    private String phone;
    private String address;
    private String gender;

    public User() {

    }

    public User(String name, String dateOfBirth, String profession, String passportNo, String nationalId, String email, String phone, String address, String gender) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.profession = profession;
        this.passportNo = passportNo;
        this.nationalId = nationalId;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
