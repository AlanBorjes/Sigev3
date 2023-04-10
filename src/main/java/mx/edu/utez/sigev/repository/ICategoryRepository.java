package mx.edu.utez.sigev.repository;

import mx.edu.utez.sigev.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICategoryRepository extends JpaRepository<Category, Long>, PagingAndSortingRepository<Category, Long> {
    public Category findByName(String name);

    @Query(value = "SELECT * FROM category WHERE status = :status", nativeQuery = true)
    public List <Category> findAllByStatus(@Param("status")int status);
}
