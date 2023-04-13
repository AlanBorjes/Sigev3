package mx.edu.utez.sigev.repository;
import mx.edu.utez.sigev.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagesRepository extends JpaRepository<Images, Long> {

    public Images findImagesById(long id);
}
