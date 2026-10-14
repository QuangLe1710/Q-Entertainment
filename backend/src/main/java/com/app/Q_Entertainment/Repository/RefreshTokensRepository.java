package com.app.Q_Entertainment.Repository;

import com.app.Q_Entertainment.Model.Entity.RefreshTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokensRepository extends JpaRepository<RefreshTokens, Long>, CrudRepository<RefreshTokens, Long> {

    List<RefreshTokens> findAllByUserId(int userId);

    @Query(
                    "SELECT " +
                        "id," +
                        " userId," +
                        " refreshTokenHash," +
                        " deviceInfo," +
                        " ipAddress," +
                        " expiresAt " +
                    "FROM RefreshTokens r " +
                    "WHERE r.userId = :userId AND r.id = :refreshTokenId"
    )
    Optional<RefreshTokens> findByUserIdAndRefreshTokenId(@Param("userId") int userId, @Param("refreshTokenId") long refreshTokenId );

    @Modifying
    @Query(
                    "UPDATE RefreshTokens r" +
                    " SET r.isRevoked = TRUE " +
                    "WHERE r.userId = :userId AND r.isRevoked IS FALSE"
    )
    void revokeAllByUserId(@Param("userId") int userId);

    @Modifying
    @Query("UPDATE RefreshTokens r" +
            " SET r.isRevoked = TRUE " +
            "WHERE r.id = :tokenId")
    void revokeById(@Param("tokenId") long tokenId);

    @Modifying
    @Query("UPDATE RefreshTokens r" +
            " SET r.refreshTokenHash = :hash" +
            " WHERE r.id = :tokenId")
    void updateHashById(@Param("tokenId") long tokenId, @Param("hash") String hash);

}
