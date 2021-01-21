package com.codecool.binder.repository;

import com.codecool.binder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail (String email);
    @Query("SELECT u FROM Binder u WHERE ?1 MEMBER OF u.interests")
    List<User> findByInterestsContaining (String interest);
    List<User> findByLastNameIsInOrFirstNameIsIn(Collection<String> lastName, Collection<String> firstName);
}
