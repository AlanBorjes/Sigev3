package mx.edu.utez.sigev.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "suburb")
public class Suburb implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "name", nullable = false, length = 150)
    @Size(min = 2, message = "El nombre debe tener mínimo 2 caracteres")
    @Size(max = 150, message = "El nombre debe tener máximo 150 caracteres")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @Column(name = "postal_code", nullable = false, length = 5, unique = true)
    @Size(min = 5, message = "El Código postal debe tener mínimo 5 caracteres")
    @Size(max = 5, message = "El código postal debe tener máximo 5 caracteres")
    @NotBlank(message = "El código postal no puede estar vacío")
    private String postalCode;

    @Column(name = "status", nullable = false)
    @NotNull(message = "El estatus no puede estar vacío")
    private int status;
    @ManyToOne
    @JoinColumn(name = "city", nullable = false)
    @NotNull(message = "La ciudad no puede estar vacía")
    private City city;




    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Suburb() {
        this.status = 1;
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

    public Suburb(Long id, String name, String postalCode, int status, City city) {
        this.id = id;
        this.name = name;
        this.postalCode = postalCode;
        this.status = status;
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
