package Domain;

@FunctionalInterface
public interface INewsService {
    void postNew(User user, Game game);
}
