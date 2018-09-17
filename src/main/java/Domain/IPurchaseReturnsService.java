package Domain;

@FunctionalInterface
public interface IPurchaseReturnsService {
    void returnPurchase(Purchase purchase);
}
