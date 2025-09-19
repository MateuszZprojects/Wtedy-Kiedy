package controllers.models;


import javax.persistence.*;

import lombok.Data;

@Data //Adnotacja @Data wystarczy aby Lombok wygenerował getery, setery, toString, equals i inne
@Entity
@Table(name = "GAMES")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_generator")
    @SequenceGenerator(name="game_generator", sequenceName = "game_seq", allocationSize=50)
    @Column(name = "id", updatable = false, nullable = false)
    public int id;

    @Column(name = "player1login")
    private String player1login;

    @Column(name = "player1score")
    private int player1score;

    @Column(name = "player2login")
    private String player2login;
    @Column(name = "player2score")
    private int player2score;
    @Column (name="deleted")
    private boolean deleted;


    public Game() {
        this.player1login = "";
        this.player1score = 0;
        this.player2login = "";
        this.player2score = 0;
        this.deleted=false;
    }

    public Game(String player1login) {
        this.player1login = player1login;
        this.player1score = 0;
        this.player2login = "";
        this.player2score = 0;
        this.deleted=false;
    }
    public Game(String player1login, int player1score, String player2login, int player2score, boolean deleted) {
        this.player1login = player1login;
        this.player1score = player1score;
        this.player2login = player2login;
        this.player2score = player2score;
        this.deleted = deleted;
    }


}
