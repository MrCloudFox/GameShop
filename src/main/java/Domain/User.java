package Domain;

import javax.management.OperationsException;
import java.util.Date;
import java.util.LinkedList;

public class User {
    private String login;
    private LinkedList<Game> games;
    private double balance;

    public User(String login) {
        this.login = login;
        games = new LinkedList<>();
        balance = 0;
    }

    public String getLogin() {
        return login;
    }

    public LinkedList<Game> getGames() {
        return games;
    }

    public double getBalance() {
        return balance;
    }

    public void byuGame(Game game) throws OperationsException{
        checkConditionsForBuy(this, game);
        withdrawMoney(game.getCost());
        new Purchase(this, game); // during the creation of purchase, the data is entered into the database
        games.add(game);
    }

    private void checkConditionsForBuy(User user, Game game) throws OperationsException{
        if (user.getGames().contains(game)) {
            throw new OperationsException("Game already exist in User Account");
        }
        if (balance <= game.getCost()) {
            throw new OperationsException("Balance lees the game cost");
        }
    }

    public void returnGame(Purchase purchase) throws OperationsException{
        if (!games.contains(purchase.getGame())) {        //small protection
            throw new OperationsException("This game you do not have");
        }
        if ((purchase.getDateOfPurchase().getTime() - (new Date()).getTime())/(24*60*60*1000) > 14) {
            throw new OperationsException("The repayment period has expired");
        }

        games.remove(purchase.getGame());
        replenishBalance(purchase.getGame().getCost());
    }

    public void presentGame(User acceptedUser, Game game) throws OperationsException{
        checkConditionsForBuy(acceptedUser, game); // need realisation
        withdrawMoney(game.getCost());
        new Purchase(this, game); // the purchase is assigned to the buyer, so concept gift not needed
        acceptedUser.getGames().add(game);
    }

    public void postFeedBack(Game game, String text) {
        game.addFeedBack(this, text);
    }

    public void checkToWithdrawMoney(double sum) throws OperationsException{
        if(sum > balance) {
            throw new OperationsException("Insufficient funds");
        }
    }

    public String replenishBalance(double sum) {
        balance += sum;
        return "Your balance " + balance;
    }

    public void withdrawMoney(double sum) throws OperationsException{
        checkToWithdrawMoney(sum);
        balance -= sum;
    }

}