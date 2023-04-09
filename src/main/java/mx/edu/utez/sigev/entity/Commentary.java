package mx.edu.utez.sigev.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "commentary")
public class Commentary implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", length = 255, nullable = false)
    @Size(min = 2, message = "El contenido debe tener mínimo 2 caraceres")
    @Size(max = 255, message = "El contenido debe tener máximo 255 caracteres")
    @NotBlank(message = "El contenido no puede estar vacío")
    private String content;

    @ManyToOne
    @JoinColumn(name = "request", nullable = false)
    @NotNull(message = "Debe tener una solicitud asignada")
    private Request request;

    @Column(name = "autor", length = 100, nullable = false)
    @Size(min = 2, message = "El autor debe contener mínimo 2 caracteres")
    @Size(max = 100, message = "El autor debe contener máximo 100 caracteres")
    @NotBlank(message = "El autor no puede estar vacío")
    private String autor;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Commentary() {
        this.date = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}
