package mx.edu.utez.sigev.repository;

import mx.edu.utez.sigev.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStateRepository extends JpaRepository<State, Long> {
    public State findById(long id);
}
