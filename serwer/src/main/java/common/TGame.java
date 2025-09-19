package common;

import controllers.models.Game;
import lombok.Data;

@Data //Adnotacja @Data wystarczy aby Lombok wygenerował getery, setery, toString, equals i inne
public class TGame {

    private int id;
    private String player1login;
    private Integer player1score;
    private String player2login;
    private Integer player2score;
    private Boolean deleted;

    public TGame(int id, String player1login, Integer player1score, String player2login, Integer player2score, boolean deleted) {
        this.id = id;
        this.player1login = player1login;
        this.player1score = player1score;
        this.player2login = player2login;
        this.player2score = player2score;
        this.deleted = deleted;
    }


    public TGame(Game games) {
        this.id = games.getId();
        this.player1login = games.getPlayer1login();
        this.player1score = games.getPlayer1score();
        this.player2login = games.getPlayer2login();
        this.player2score = games.getPlayer2score();
      //  this.deleted = games.getDeleted();
    }

    public TGame(String errorText) {
        this.id = -1;
        this.player1login = errorText;
        this.player2login = errorText;
    }

    public TGame() {
        this.id = -1;
        this.player1login = "Brak";
        this.player2login = "Brak";
    }

}
