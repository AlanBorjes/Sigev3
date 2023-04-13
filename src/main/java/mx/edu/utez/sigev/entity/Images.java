package mx.edu.utez.sigev.entity;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "login")
    private String login;
    @Column(name = "logo1")
    private String logo1;
    @Column(name = "logo2")
    private String logo2;

    public Images() {
    }

    public Images(Long id, String login, String logo1, String logo2) {
        this.id = id;
        this.login = login;
        this.logo1 = logo1;
        this.logo2 = logo2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogo1() {
        return logo1;
    }

    public void setLogo1(String logo1) {
        this.logo1 = logo1;
    }

    public String getLogo2() {
        return logo2;
    }

    public void setLogo2(String logo2) {
        this.logo2 = logo2;
    }
}
