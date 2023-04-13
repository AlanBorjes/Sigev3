package mx.edu.utez.sigev.entity.DataTransferObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserUpdateDTO {

    @Size(min = 2, message = "El nombre debe tener mínimo 2 caracteres")
    @Size(max = 150, message = "El nombre debe tener máximo 150 caracteres")
    @NotBlank(message = "El nombre no puede estar vacío")
    String name;
    @Size(min = 2, message = "El primer apellido debe tener mínimo 2 caracteres")
    @Size(max = 150, message = "El primer apellido debe tener máximo 150 caracteres")
    @NotBlank(message = "El primer apellido no puede estar vacío")
    String lastname;
    @Size(max = 150, message = "El segundo apellido debe tener máximo 150 caracteres")
    String surname;
    @Size(min = 10, max = 10, message = "El teléfono debe tener mínimo 10 caracteres")
    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "\\d+", message = "Este campo solo puede contener números")
    String phone;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
