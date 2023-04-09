package mx.edu.utez.sigev.entity.DataTransferObject;

public class UserDto {
    
    private String name;
    private String lastname;
    private String surname;
    private String email;
    private String username;
    private long city;
    private String password;
    private String phone;

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public long getCity() {
        return city;
    }
    public void setCity(long city) {
        this.city = city;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "UserDto [city=" + city + ", email=" + email + ", lastname=" + lastname + ", name=" + name
                + ", password=" + password + ", phone=" + phone + ", surname=" + surname + ", username=" + username
                + "]";
    }

}
