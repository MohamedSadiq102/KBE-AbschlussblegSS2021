package userManagement.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import userManagement.model.MyUser;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<MyUser, String> {

    @Query(value = "SELECT * FROM myuser "
            + "WHERE userid = ?1", nativeQuery = true)
    List<MyUser> findUser(String userId);
}
