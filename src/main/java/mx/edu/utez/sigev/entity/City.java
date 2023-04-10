package mx.edu.utez.sigev.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "city")
public class City implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    @NotBlank(message="El nombre no puede estar vacio")
    @Size(min = 2, message="El mensaje debe ser minimo de 2 caracteres")
    @Size(max = 150, message="El mensaje debe ser maximo de 150 caracteres")
    private String name;

    @Column(name = "status", nullable = false)
    private int status;


    public City() {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
