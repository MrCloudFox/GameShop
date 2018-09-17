package Domain;

@FunctionalInterface
public interface IFeedBackService {
    void postFeedBack(User user, String text);
}
