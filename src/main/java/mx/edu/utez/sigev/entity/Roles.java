package mx.edu.utez.sigev.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "roles")
public class Roles implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "authority", nullable = false, length = 150)
    @Size(min = 2, message = "El rol debe tener mínimo 2 caracteres")
    @Size(max = 150, message = "El rol debe tener máximo 150 caracteres")
    private String authority;

    public Roles(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public Roles() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "Roles [authority=" + authority + ", id=" + id + "]";
    }
    
}
