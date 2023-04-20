package mx.edu.utez.sigev.repository;

import mx.edu.utez.sigev.entity.Color;
import mx.edu.utez.sigev.entity.Suburb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ISuburbRepository extends JpaRepository<Suburb, Long>, PagingAndSortingRepository<Suburb, Long> {

    public List<Suburb> findAllByCityId(long id);

    
}
