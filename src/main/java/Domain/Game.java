package Domain;

import javax.management.OperationsException;
import java.util.HashMap;
import java.util.LinkedList;

public class Game {
    private int id, idOfFeedBack, idOfNew;
    private String name;
    private double cost;
    private String linkToDownload;
    private HashMap<Integer, FeedBack> feedBacks; // Integer is id of feedback
    private HashMap<Integer, NewOfGame> news; // Integer is id of new
    private LinkedList<User> authors;

    public Game(int id, String name, double cost, String linkToDownload, LinkedList<User> authors) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.linkToDownload = linkToDownload;
        this.feedBacks = new HashMap<>();
        this.news = new HashMap<>();
        this.authors = authors;

        this.idOfFeedBack = 0;
        this.idOfNew = 0;
    }

    public LinkedList<User> getAuthors() {
        return authors;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public String getLinkToDownload() {
        return linkToDownload;
    }

    public HashMap<Integer, FeedBack> getFeedBacks() {
        return feedBacks;
    }

    public void addFeedBack(User user, String text) {
        feedBacks.put(idOfFeedBack, new FeedBack(user, text));
        idOfFeedBack++;
    }

    public void addNew(User user, String text) throws OperationsException {        //need to GemeService func
        if (authors.contains(user)) {
            news.put(idOfNew, new NewOfGame(user, text));
            idOfNew++;
        } else {
            throw new OperationsException("You are not author of this game.");
        }
    }
}