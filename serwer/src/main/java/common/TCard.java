package common;

import controllers.models.Card;
import lombok.Data;

@Data //Adnotacja @Data wystarczy aby Lombok wygenerował getery, setery, toString, equals i inne
public class TCard {

    private int id;
    private Integer year;
    private String description;
    private boolean deleted;

    public TCard(int id, Integer year, String description, boolean deleted) {
        this.id = id;
        this.year = year;
        this.description = description;
        this.deleted = deleted;
    }

    public TCard(Card card) {
        this.id = card.getId();
        this.year = card.getYear();
        this.description = card.getDescription();
        this.deleted = card.isDeleted();
    }

    public TCard(String errorText) {
        this.id = -1;
        this.year = 0000;
        this.description = errorText;
    }

    public TCard() {
        this.id = -1;
        this.year = 0000;
        this.description = "brak";
        this.deleted = false;
    }

}
