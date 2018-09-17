package Domain;

import java.util.Date;

@FunctionalInterface
public interface IPurchaseRepository {
    Purchase get(User user, Game game); //if purchase doesn't exist then return null
}
