package Domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@FunctionalInterface
public interface Admin {
    LinkedList<Purchase> seePurchases(User user);
}
