package mx.edu.utez.sigev.repository;

import mx.edu.utez.sigev.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ICategoryRepository extends JpaRepository<Category, Long>, PagingAndSortingRepository<Category, Long> {
    public Category findByName(String name);
}
