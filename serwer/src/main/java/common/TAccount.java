package common;

import controllers.models.Account;
import controllers.models.Card;
import lombok.Data;

@Data //Adnotacja @Data wystarczy aby Lombok wygenerował getery, setery, toString, equals i inne
public class TAccount {

    private int id;
    private String login;
    private String type;
    private boolean deleted;

    public TAccount(int id, String login, String type, boolean deleted) {
        this.id = id;
        this.login = login;
        this.type = type;
        this.deleted = deleted;
    }

    public TAccount(Account account) {
        this.id = account.getId();
        this.login = account.getLogin();
        this.type = account.getType();
        this.deleted = account.isDeleted();
    }

    public TAccount(String errorText) {
        this.id = -1;
        this.login = errorText;
        this.type = "0";
    }

    public TAccount() {
        this.id = -1;
        this.login = "0000";
        this.type = "0";
        this.deleted = false;
    }

}
