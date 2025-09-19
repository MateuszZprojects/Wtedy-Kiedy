package controllers.models;


import javax.persistence.*;

import lombok.Data;

@Data //Adnotacja @Data wystarczy aby Lombok wygenerował getery, setery, toString, equals i inne
@Entity
@Table(name = "CARDS")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_generator")
    @SequenceGenerator(name="card_generator", sequenceName = "card_seq", allocationSize=50)
    @Column(name = "id", updatable = false, nullable = false)
    public int id;

    @Column(name = "year")
    private Integer year;

    @Column(name = "description")
    private String description;

    @Column(name = "deleted")
    private boolean deleted;


    public Card() {
        this.year = 0000;
        this.description = "Nic";
        this.deleted = false;
    }

    public Card(Integer year, String description) {
        this.year = year;
        this.description = description;
        this.deleted = false;
    }


}
