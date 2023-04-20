/*package mx.edu.utez.sigev.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "committee_team")
public class CommitteeTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinColumn(name = "user", nullable = false)
    @NotNull(message="Este campo no puede estar vacio")
    private Set<Users> users;

    @ManyToMany
    @JoinColumn(name = "committee", nullable = false)
    @NotNull(message ="Este campo no puede estar vacio")
    private Set<Committee> committees;





} */
