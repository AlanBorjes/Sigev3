package mx.edu.utez.sigev.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 150, nullable = false)
    @Size(min = 2, message = "El nombre debe tener mínimo 2 caracteres")
    @Size(max = 150, message = "El nombre debe tener máximo 150 caracteres")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @Column(name = "lastname", length = 150, nullable = false)
    @Size(min = 2, message = "El primer apellido debe tener mínimo 2 caracteres")
    @Size(max = 150, message = "El primer apellido debe tener máximo 150 caracteres")
    @NotBlank(message = "El primer apellido no puede estar vacío")
    private String lastname;

    @Column(name = "surname", length = 150, nullable = true)
    @Size(max = 150, message = "El segundo apellido debe tener máximo 150 caracteres")
    private String surname;

    @Column(name = "username", nullable = false, unique = true, length = 150)
    @Size(min = 2, message = "El nombre de usuario debe tener mínimo 2 caracteres")
    @Size(max = 150, message = "El nombre de usuario debe tener máximo 150 caracteres")
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String username;

    @Column(name = "phone", nullable = false, length = 10)
    @Size(min = 10, max = 10, message = "El teléfono debe tener mínimo 10 caracteres")
    @NotBlank(message = "El teléfono no puede estar vacío")
    private String phone;

    @Column(name = "password", nullable = false, length = 255)
    @Size(min = 5, message = "La contraseña debe tener mínimo 5 caracteres")
    @Size(max = 255, message = "La contraseña debe tener máximo 255 caracteres")
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

    @Column(name = "enabled", nullable = false)
    @NotNull(message = "El estatus no puede estar vacío")
    private int enabled;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    @Size(min = 7, message = "El correo debe tener mínimo 7 caracteres")
    @Size(max = 150, message = "El correo debe tener máximo 150 caracteres")
    @NotBlank(message = "El correo no puede estar vacío")
    private String email;

    @Column(name = "registered_date", nullable = false)
    @NotNull(message = "La fecha de registro no puede estar vacía")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredDate;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user"), inverseJoinColumns = @JoinColumn(name = "role"))
    @NotNull(message = "Los roles no pueden estar vacíos")
    private Set<Roles> roles;

    public Users() {
        this.enabled = 1;
        this.registeredDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }

    public void addRole(Roles role) {
        if (roles == null) {
            roles = new HashSet<Roles>();
        }
        roles.add(role);
    }

    @Override
    public String toString() {
        return "Users [email=" + email + ", enabled=" + enabled + ", id=" + id + ", lastname=" + lastname + ", name="
                + name + ", password=" + password + ", phone=" + phone + ", registeredDate=" + registeredDate
                + ", roles=" + "aaa" + ", surname=" + surname + ", username=" + username + "]";
    }

    
    
}
