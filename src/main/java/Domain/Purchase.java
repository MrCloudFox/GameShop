package Domain;

import java.util.Date;

public class Purchase {
    private User user;
    private Game game;
    private Date dateOfPurchase;

    public Purchase(User user, Game game) {
        this.user = user;
        this.game = game;
        this.dateOfPurchase = new Date(); //current date
        // making a purchase in the database
    }

    public User getUser() {
        return user;
    }

    public Game getGame() {
        return game;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }
}
