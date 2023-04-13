package mx.edu.utez.sigev.repository;

import mx.edu.utez.sigev.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IColorRepository extends JpaRepository<Color, Long> {

    public Color findColorById(long id);
}
