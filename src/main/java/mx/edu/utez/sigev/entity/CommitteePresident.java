package mx.edu.utez.sigev.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "committee_president")
public class CommitteePresident implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    @NotNull(message = "El usuario no puede estar vacío")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "committee", nullable = false)
    @NotNull(message = "El comité no puede estar vacío")
    private Committee committee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Committee getCommittee() {
        return committee;
    }

    public void setCommittee(Committee committee) {
        this.committee = committee;
    }

    @Override
    public String toString() {
        return "CommitteePresident{" +
                "id=" + id +
                ", user=" + user +
                ", committee=" + committee +
                '}';
    }
}
