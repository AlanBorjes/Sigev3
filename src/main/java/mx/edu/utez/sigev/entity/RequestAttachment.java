package mx.edu.utez.sigev.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "request_attachments")
public class RequestAttachment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    @Size(min = 2, message = "El nombre del archivo debe tener mínimo 2 caracteres")
    @Size(max = 100, message = "El nombre del archivo debe tener máximo 100 caracteres")
    @NotBlank(message = "El nombre del archivo no puede estar vacío")
    private String name;

    public RequestAttachment(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public RequestAttachment(String name) {
        this.name = name;
    }

    public RequestAttachment() {
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
    
}
