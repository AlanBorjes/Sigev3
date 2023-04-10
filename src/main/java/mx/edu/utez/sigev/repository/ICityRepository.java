package mx.edu.utez.sigev.repository;

import mx.edu.utez.sigev.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICityRepository extends JpaRepository<City, Long> , PagingAndSortingRepository<City, Long> {
    public City findById(long id);

    public  List<City> findAllByStatus(int status);

    @Query(value = "SELECT c.* FROM city c WHERE c.state = :id", nativeQuery = true)
    public List<City> findAllCitiesByStateId(@Param("id") long id);
}
