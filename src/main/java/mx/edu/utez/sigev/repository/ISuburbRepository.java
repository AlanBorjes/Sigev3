package mx.edu.utez.sigev.repository;

import mx.edu.utez.sigev.entity.Suburb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ISuburbRepository extends JpaRepository<Suburb, Long>, PagingAndSortingRepository<Suburb, Long> {
    
}
