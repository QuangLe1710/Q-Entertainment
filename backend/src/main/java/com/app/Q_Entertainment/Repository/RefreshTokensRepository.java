package com.app.Q_Entertainment.Repository;

import com.app.Q_Entertainment.Model.Entity.RefreshTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokensRepository extends JpaRepository<RefreshTokens, Integer>, CrudRepository<RefreshTokens, Integer> {

    List<RefreshTokens> findAllByUserId(int userId);

    @Modifying
    @Query(
                    "UPDATE RefreshTokens r" +
                    " SET r.isRevoked = TRUE " +
                    "WHERE r.userId = :userId AND r.isRevoked IS FALSE"
    )
    void revokeAllByUserId(@Param("userId") int userId);

}
