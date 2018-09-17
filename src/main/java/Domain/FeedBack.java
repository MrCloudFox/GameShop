package Domain;

import java.util.Date;

public class FeedBack {
    private User user;
    private Date dateOfFeedBack;
    private String text;

    public FeedBack(User user, String text) {
        this.dateOfFeedBack = new Date();
        this.text = text;
    }


    public User getUser() {
        return user;
    }

    public Date getDateOfFeedBack() {
        return dateOfFeedBack;
    }

    public String getText() {
        return text;
    }
}