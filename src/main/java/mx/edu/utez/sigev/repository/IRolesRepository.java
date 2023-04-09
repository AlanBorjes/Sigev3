package mx.edu.utez.sigev.repository;

import mx.edu.utez.sigev.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRolesRepository extends JpaRepository<Roles, Long> {
    
    public Roles findById(long id);
    Roles findByAuthority(String authority);

}
