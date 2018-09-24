package Domain;

import javax.management.OperationsException;

public class GameService {
    IDeliveryService deliveryService;
    IPurchaseReturnsService purchaseReturnsService;
    IPresentService presentService;
    IFeedBackService feedBackService;
    INewsService newsService;
    ICashService cashService;

    public GameService(IDeliveryService deliveryService, IPurchaseReturnsService purchaseReturnsService,
                       IPresentService presentService, IFeedBackService feedBackService,
                       INewsService newsService, ICashService cashService) {
        this.deliveryService = deliveryService;
        this.purchaseReturnsService = purchaseReturnsService;
        this.presentService = presentService;
        this.feedBackService = feedBackService;
        this.newsService = newsService;
    }

    public void buyGame(User user, Game game) throws OperationsException {
        user.byuGame(game);
        deliveryService.deliverGame(user, game);
    }

    public void returnGame(Purchase purchase) throws OperationsException{
        purchase.getUser().returnGame(purchase);
        purchaseReturnsService.returnPurchase(purchase);
    }

    public void presentGame(User givingUser, User acceptedUser, Game game) throws OperationsException{
        givingUser.presentGame(acceptedUser, game);
        presentService.presentGame(acceptedUser, game);
        deliveryService.deliverGame(acceptedUser, game);
    }

    public void postFeedBack(User user, Game game, String text) {
        user.postFeedBack(game, text);
        feedBackService.postFeedBack(user, text);
    }

    public void postNew(User user, Game game, String text) throws OperationsException {
        game.addNew(user, text);
        newsService.postNew(user, game);
    }

    public void replenishCash(User user, double sum) throws OperationsException{
        user.replenishBalance(sum);
        cashService.setCash(user, sum);
    }

    public void withdrawCash(User user, double sum) throws OperationsException{
        user.withdrawMoney(sum);
        cashService.setCash(user, sum);
    }
}
