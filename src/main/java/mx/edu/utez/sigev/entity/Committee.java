package mx.edu.utez.sigev.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "committee")
public class Committee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 150, nullable = false)
    @Size(min = 2, message = "El nombre debe contener mínimo 2 caracteres")
    @Size(max = 150, message = "El nombre debe contener máximo 150 caracteres")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @Column(name = "status", nullable = false)
    @NotNull(message = "El estatus no puede estar vacío")
    private int status;

    @ManyToOne
    @JoinColumn(name = "suburb", nullable = false)
    private Suburb suburb;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "committee_team", joinColumns = {
            @JoinColumn(name = "committee") }, inverseJoinColumns = {
            @JoinColumn(name = "user") })
    private Set<Users> users;


    public Committee() {
        this.status = 1;
        this.users = new HashSet<>();
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

    public Suburb getSuburb() {
        return suburb;
    }

    public void setSuburb(Suburb suburb) {
        this.suburb = suburb;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<Users> getUsers() {
        return users;
    }

    public void setUsers(Set<Users> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Committee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", suburb=" + suburb +
                ", users=" + users +
                '}';
    }
}
