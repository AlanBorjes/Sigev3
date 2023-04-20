package mx.edu.utez.sigev.repository;

import mx.edu.utez.sigev.entity.Committee;
import mx.edu.utez.sigev.entity.CommitteePresident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICommitteeRepository extends JpaRepository<Committee, Long>, PagingAndSortingRepository<Committee, Long> {
    public Committee findById(long id);

    @Modifying
    @Query(value = "INSERT INTO committee_team VALUES (:idCom, :idUse)", nativeQuery = true)
    void insertTeam(@Param("idCom") long idCom, @Param("idUse") long idUse);



    @Query(value = "SELECT cp.* FROM committee_president cp INNER JOIN committee c ON c.id = cp.committee INNER JOIN suburb s ON s.id = c.suburb INNER JOIN city ci ON ci.id = s.city WHERE ci.id = :id", nativeQuery = true)
    public List<CommitteePresident> listPagination(@Param("id") long id);
}
