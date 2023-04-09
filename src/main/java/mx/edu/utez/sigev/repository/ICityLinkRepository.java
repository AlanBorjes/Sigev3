package mx.edu.utez.sigev.repository;

import mx.edu.utez.sigev.entity.CityLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ICityLinkRepository extends JpaRepository<CityLink, Long> {
    @Query(value = "SELECT COUNT(*) FROM city_link cl WHERE cl.city = :id", nativeQuery = true)
    public long totalCityLinkCountByCityId(@Param("id") long id);

    @Query(value = "SELECT * FROM city_link cl WHERE cl.user = :id", nativeQuery = true)
    public CityLink findByUserId(long id);
}
