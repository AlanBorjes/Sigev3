package mx.edu.utez.sigev.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "state")
public class State implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    @Size(min = 2, message = "El nombre debe tener mínimo 2 caracteres")
    @Size(max = 100, message =  "El nombre debe tener máximo 100 caracteres")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @Column(name = "status", nullable = false)
    @NotNull(message = "El estatus no puede estar vacío")
    private int status;

    public State(Long id, String name, int status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }


    public State(){
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
