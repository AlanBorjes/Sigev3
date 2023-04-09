package mx.edu.utez.sigev.repository;

import mx.edu.utez.sigev.entity.Commentary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICommentaryRepository extends JpaRepository<Commentary, Long> {
    public Commentary findById(long id);
    @Query(value = "SELECT * FROM commentary c WHERE c.request = :id ORDER BY c.id ASC", nativeQuery = true)
    public List<Commentary> findAllByRequestId(@Param("id") long id);
}