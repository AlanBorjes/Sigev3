package mx.edu.utez.sigev.repository;

import mx.edu.utez.sigev.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface IUserRepository extends JpaRepository<Users, Long>, PagingAndSortingRepository<Users, Long> {
    public Users findById(long id);

    Users findByUsername(String username);

    @Query(value = "SELECT u.password FROM users u WHERE u.id = :id", nativeQuery = true)
    String findPasswordById(@Param("id")long id);
}
