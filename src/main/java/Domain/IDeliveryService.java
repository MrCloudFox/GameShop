package Domain;

@FunctionalInterface
public interface IDeliveryService {
    void deliverGame(User user, Game game);
}