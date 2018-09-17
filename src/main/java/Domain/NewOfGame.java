package Domain;

import java.util.Date;

public class NewOfGame {
    private Date dateOfNew;
    private User user;
    private String text;

    public NewOfGame(User user, String text) {
        this.user = user;
        this.text = text;
        this.dateOfNew = new Date();
    }

    public Date getDateOfNew() {
        return dateOfNew;
    }

    public User getUser() {
        return user;
    }
}
