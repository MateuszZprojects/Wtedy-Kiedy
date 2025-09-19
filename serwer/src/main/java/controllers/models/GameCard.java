package controllers.models;


import lombok.Data;

import javax.persistence.*;

@Data //Adnotacja @Data wystarczy aby Lombok wygenerował getery, setery, toString, equals i inne
@Entity
@Table(name = "GAMESCARDS")
public class GameCard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gamescards_generator")
    @SequenceGenerator(name="gamescards_generator", sequenceName = "gamescards_seq", allocationSize=50)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Column(name = "GameID")
    private Integer gameid;

    @Column(name = "PlayerID")
    private Integer playerid;

    @ManyToOne
    @JoinColumn(name = "CardID")
    private Card cardid;




    public GameCard() {
        this.gameid = 0;
        this.playerid = 0;
        this.cardid = null;
    }

    public GameCard(Integer GameId, Integer PlayerId, Card Card) {
        this.gameid = GameId;
        this.playerid = PlayerId;
        this.cardid = Card;
    }


}
