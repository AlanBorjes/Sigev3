package mx.edu.utez.sigev.repository;

import mx.edu.utez.sigev.entity.Committee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ICommitteeRepository extends JpaRepository<Committee, Long>, PagingAndSortingRepository<Committee, Long> {
    public Committee findById(long id);
}
