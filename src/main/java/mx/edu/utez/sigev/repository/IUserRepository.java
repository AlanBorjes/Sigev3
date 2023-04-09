package mx.edu.utez.sigev.repository;

import mx.edu.utez.sigev.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IUserRepository extends JpaRepository<Users, Long>, PagingAndSortingRepository<Users, Long> {
    public Users findById(long id);

    Users findByUsername(String username);

    @Query(value = "SELECT u.password FROM users u WHERE u.id = :id", nativeQuery = true)
    String findPasswordById(@Param("id")long id);

    @Query(value = "select u.id, u.email, u.enabled, u.lastname, u.name, u.password, u.phone, u.registered_date, u.surname, u.username from users u inner join user_role r on u.id = r.user where r.role = :id", nativeQuery = true)
    List<Users> findAllByRole(@Param("id")long id);
}
