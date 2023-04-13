package mx.edu.utez.sigev.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "city_link")
public class CityLink implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    @NotNull(message="Este campo no puede estar vacio")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "city", nullable = false)
    @NotNull(message="Este campo no puede estar vacio")
    private City city;

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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "CityLink{" +
                "id=" + id +
                ", user=" + user +
                ", city=" + city +
                '}';
    }
}
