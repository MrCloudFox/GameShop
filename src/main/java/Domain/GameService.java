package Domain;

import javax.management.OperationsException;

public class GameService {
    IDeliveryService deliveryService;
    IPurchaseReturnsService purchaseReturnsService;
    IPresentService presentService;
    IFeedBackService feedBackService;
    INewsService newsService;

    public GameService(IDeliveryService deliveryService, IPurchaseReturnsService purchaseReturnsService,
                       IPresentService presentService, IFeedBackService feedBackService,
                       INewsService newsService) {
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
}
