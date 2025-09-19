package controllers.models;


import lombok.Data;

import javax.persistence.*;




@Data //Adnotacja @Data wystarczy aby Lombok wygenerował getery, setery, toString, equals i inne
@Entity
@Table(name = "ACCOUNTS")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name="user_generator", sequenceName = "user_seq", allocationSize=50)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "type")
    private String type;

    @Column(name = "deleted")
    private boolean deleted;


    public Account() {
        this.login = "0000";
        this.password = "0000";
        this.type = "1";
        this.deleted = false;
    }
    public Account(String login, String password) {
        this.login = login;
        this.password = password;
        this.type = "1";
        this.deleted = false;
    }

    public Account(String login, String password, String type) {
        this.login = login;
        this.password = password;
        this.type = type;
        this.deleted = false;
    }


}
