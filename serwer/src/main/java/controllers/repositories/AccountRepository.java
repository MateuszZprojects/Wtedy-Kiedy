package controllers.repositories;


import controllers.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {


    public Account findById(int id);

    public Account findByLoginAndPasswordAndDeletedFalse(String Login, String Password);
    public Account findByLogin(String Login);
    public Boolean existsByLogin(String Login);


}

