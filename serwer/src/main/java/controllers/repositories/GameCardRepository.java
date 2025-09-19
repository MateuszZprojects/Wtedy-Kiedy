package controllers.repositories;


import controllers.models.Account;
import controllers.models.Card;
import controllers.models.Game;
import controllers.models.GameCard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameCardRepository extends JpaRepository<GameCard, Integer> {


    public GameCard findById(int id);
    List<GameCard> findByGameid(Integer gameId);
    GameCard findByGameidAndPlayerid(Integer gameId, Integer userId);

    @Query("SELECT COUNT(gc) FROM GameCard gc " +
            "WHERE gc.gameid = :gameId " +
            "AND gc.playerid = :playerId " +
            "AND gc.cardid.year BETWEEN :startYear AND :endYear")
    Long countGameCardsBetweenYears(@Param("gameId") Integer gameId,
                                    @Param("playerId") Integer playerId,
                                    @Param("startYear") Integer startYear,
                                    @Param("endYear") Integer endYear);


    @Query("SELECT DISTINCT gc.playerid FROM GameCard gc WHERE gc.gameid = :gameID AND gc.playerid != 0 AND gc.playerid <> :userID")
    Integer findSecondPlayerIdInGame(@Param("gameID") int gameID, @Param("userID") int userID);

    @Query("SELECT gc.gameid as gameId, gc.playerid as playerId, gc.cardid.id as cardId, c.year as cardYear " +
            "FROM GameCard gc JOIN Card c ON gc.cardid=c.id " +
            "WHERE gc.gameid = 1")
    List<Object[]> findYear();


    boolean existsByGameidAndCardid(int gameId, Card cardId);


}

