package common;

import controllers.models.Card;
import controllers.models.GameCard;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_game_cards")
public class TGameCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private int gameid;
    private int year;
    private int cardId;
    private String description;

    @Column(name = "player_id")
    private Integer playerid;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    public TGameCard(int id, int game, int playerid, Card card) {
        this.id = id;
        this.gameid = game;
        this.playerid = playerid;
        this.card = card;
    }

    public TGameCard(GameCard gamescard) {
        this.id = gamescard.getId();
        this.gameid = gamescard.getGameid();
        this.playerid = gamescard.getPlayerid();
        this.card = gamescard.getCardid();
    }

    public TGameCard() {
        this.id = -1;
        this.gameid = 0;
        this.playerid = 0;
        this.card = null;
    }


}
