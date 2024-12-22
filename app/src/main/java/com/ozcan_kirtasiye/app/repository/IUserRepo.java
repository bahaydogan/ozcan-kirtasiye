package com.ozcan_kirtasiye.app.repository;

import com.ozcan_kirtasiye.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepo extends JpaRepository<User, Long> {
    User findByActivationToken(String token);

    User findByEmail(String email);


    //örnek
//    @Query (value="Select u from User u")
//    Page<UserProjection> getAllUserRecords):


}
