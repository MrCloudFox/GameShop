package Domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.*;

import javax.management.OperationsException;
import java.util.Date;
import java.util.LinkedList;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTests {

    @Mock
    IDeliveryService mDeliveryService;
    @Mock
    IPurchaseReturnsService mPurchaseReturnsService;
    @Mock
    IPresentService mPresentService;
    @Mock
    IFeedBackService mFeedBackService;
    @Mock
    INewsService mNewsService;
    @Mock
    ICashService mCashService;
    @Mock
    IGameRepository mGameRepository;
    @Mock
    IPurchaseRepository mPurchaseRepository;
    @Mock
    IUserRepository mUserRepository;
    @Mock
    ICashRepository mCashRepository;
    @Mock
    Admin admin;

    @InjectMocks
    GameService gameService = new GameService(mDeliveryService, mPurchaseReturnsService, mPresentService, mFeedBackService, mNewsService, mCashService);

    @Test
    public void buyGameTest() {
        User checkedUser = new User("SomeLogin");
        when(mUserRepository.get(checkedUser.getLogin())).thenReturn(checkedUser);
        User user = mUserRepository.get(checkedUser.getLogin());
        user.replenishBalance(1000);

        LinkedList<User> authors = new LinkedList<>();
        authors.add(new User("Author"));

        Game checkedGame = new Game(1, "Dead By Daylight", 599, "someLink", authors);
        when(mGameRepository.get(checkedGame.getId())).thenReturn(checkedGame);
        Game game = mGameRepository.get(checkedGame.getId());
        Game notExpectedGame = new Game(2, "Destiny 2", 999, "destinyLink", null);

        try {
            gameService.buyGame(user, game);
        } catch (OperationsException e) {
            e.printStackTrace();
        }

        verify(mDeliveryService, times(1)).deliverGame(user, game);

        assertNotEquals(user.getGames().indexOf(game), user.getGames().indexOf(notExpectedGame));

        assertNotEquals(user.getGames().contains(game), false);
    }

    @Test
    public void returnPurchaseTest() {
        User checkedUser = new User("SomeLogin");
        when(mUserRepository.get(checkedUser.getLogin())).thenReturn(checkedUser);
        User user = mUserRepository.get(checkedUser.getLogin());
        user.replenishBalance(1000);

        LinkedList<User> authors = new LinkedList<>();
        authors.add(new User("Author"));

        Game checkedGame = new Game(1, "Dead By Daylight", 599, "someLink", authors);
        when(mGameRepository.get(checkedGame.getId())).thenReturn(checkedGame);
        Game game = mGameRepository.get(checkedGame.getId());

        Purchase checkedPurchase = new Purchase(user, game);
        when(mPurchaseRepository.get(checkedPurchase.getUser(), checkedPurchase.getGame())).thenReturn(checkedPurchase);
        Purchase returnedPurchase = mPurchaseRepository.get(user, game);

        try {
            gameService.buyGame(user, game);
            gameService.returnGame(returnedPurchase);
        } catch (OperationsException e) {
            e.printStackTrace();
        }

        verify(mPurchaseReturnsService, times(1)).returnPurchase(returnedPurchase);

        Game notExpectedGame = new Game(2, "Destiny 2", 999, "link", null);

        try {
            gameService.returnGame(new Purchase(user, notExpectedGame));
        } catch (Exception e) {
            assertEquals(e.getClass(), new OperationsException().getClass());
        }

        try {
            gameService.returnGame(new Purchase(user, null));
        } catch (Exception e) {
            assertEquals(e.getClass(), new OperationsException().getClass());
        }


    }

    @Test
    public void presentGameTest() {
        User checkedGivingUser = new User("Fill");
        User checkedAcceptedUser = new User("Billy");
        when(mUserRepository.get(checkedGivingUser.getLogin())).thenReturn(checkedGivingUser);
        User givingUser = mUserRepository.get(checkedGivingUser.getLogin());
        givingUser.replenishBalance(1000);

        when(mUserRepository.get(checkedAcceptedUser.getLogin())).thenReturn(checkedAcceptedUser);
        User acceptedUser = mUserRepository.get(checkedAcceptedUser.getLogin());

        LinkedList<User> authors = new LinkedList<>();
        authors.add(new User("Author"));

        Game checkedGame = new Game(1, "Dead By Daylight", 599, "someLink", authors);
        when(mGameRepository.get(checkedGame.getId())).thenReturn(checkedGame);
        Game game = mGameRepository.get(checkedGame.getId());

        try {
            gameService.presentGame(givingUser, acceptedUser, game);
        } catch (OperationsException e) {
            e.printStackTrace();
        }

        verify(mPresentService, times(1)).presentGame(acceptedUser, game);
        assertNotEquals(acceptedUser.getGames().contains(game), false);
        assertNotEquals(givingUser.getGames().contains(game), true);
    }

    @Test
    public void postFeedBackTest() {
        User checkedUser = new User("SomeLogin");
        when(mUserRepository.get(checkedUser.getLogin())).thenReturn(checkedUser);
        User user = mUserRepository.get(checkedUser.getLogin());

        LinkedList<User> authors = new LinkedList<>();
        authors.add(new User("Author"));

        Game checkedGame = new Game(1, "Dead By Daylight", 599, "someLink", authors);
        when(mGameRepository.get(checkedGame.getId())).thenReturn(checkedGame);
        Game game = mGameRepository.get(checkedGame.getId());

        String feedBackText = "Perfect game! 10 maniacs out of 10!";
        gameService.postFeedBack(user, game, feedBackText);

        verify(mFeedBackService, times(1)).postFeedBack(user,feedBackText);
    }

    @Test
    public void postNewsTest() {
        User checkedUser = new User("Author");
        when(mUserRepository.get(checkedUser.getLogin())).thenReturn(checkedUser);
        User user = mUserRepository.get(checkedUser.getLogin());

        LinkedList<User> authors = new LinkedList<>();
        authors.add(user);

        Game checkedGame = new Game(1, "Dead By Daylight", 599, "someLink", authors);
        when(mGameRepository.get(checkedGame.getId())).thenReturn(checkedGame);
        Game game = mGameRepository.get(checkedGame.getId());

        String newsText = "Update of game";
        try {
            gameService.postNew(user, game, newsText);
        } catch (OperationsException e) {
            e.printStackTrace();
        }

        verify(mNewsService, times(1)).postNew(user, game);

        assertEquals(game.getNews().get(0).getText(), newsText);

        try {
            gameService.postNew(user, game, "Perfect");
        } catch (OperationsException e) {
            e.printStackTrace();
        }

        assertNotEquals(game.getNews().get(0), game.getNews().get(1));
    }

    @Test
    public void replanishCashTest() {
        User checkedUser = new User("Author");
        when(mUserRepository.get(checkedUser.getLogin())).thenReturn(checkedUser);
        User user = mUserRepository.get(checkedUser.getLogin());

        when(mCashRepository.getCashOfUser(user)).thenReturn(0.0);

        double sum = 1000;
        double expectedSum = mCashRepository.getCashOfUser(user) + sum;

        try {
            gameService.replenishCash(user, sum);
        } catch (OperationsException e) {
            e.printStackTrace();
        }

        verify(mCashService, times(1)).setCash(user, sum);
        assertEquals(expectedSum, user.getBalance());

        assertNotEquals(expectedSum, 0);

        assertNotEquals(expectedSum, user.getBalance() - sum);
    }

    @Test
    public void withdrawCashTest() {
        User checkedUser = new User("Author");
        when(mUserRepository.get(checkedUser.getLogin())).thenReturn(checkedUser);
        User user = mUserRepository.get(checkedUser.getLogin());
        user.replenishBalance(1000);

        when(mCashRepository.getCashOfUser(user)).thenReturn(0.0);

        double sum = 400;
        double expectedSum = 600;

        try {
            gameService.withdrawCash(user, sum);
        } catch (OperationsException e) {
            e.printStackTrace();
        }

        verify(mCashService, times(1)).setCash(user, sum);

        assertNotEquals(user.getBalance(), 1000);

        assertEquals(user.getBalance(), expectedSum);
    }

    @Test
    public void seePurchasesTest() {
        User checkedUser = new User("Author");
        when(mUserRepository.get(checkedUser.getLogin())).thenReturn(checkedUser);
        User user = mUserRepository.get(checkedUser.getLogin());

        Game checkedGame1 = new Game(1, "Dead By Daylight", 599, "someLink", null);
        when(mGameRepository.get(checkedGame1.getId())).thenReturn(checkedGame1);
        Game game1 = mGameRepository.get(checkedGame1.getId());

        Game checkedGame2 = new Game(1, "Destiny 2", 999, "someLink", null);
        when(mGameRepository.get(checkedGame2.getId())).thenReturn(checkedGame2);
        Game game2 = mGameRepository.get(checkedGame2.getId());

        when(mPurchaseRepository.get(user, game1)).thenReturn(new Purchase(user, game1));
        when(mPurchaseRepository.get(user, game2)).thenReturn(new Purchase(user, game2));

        LinkedList<Purchase> expectedPurchases = new LinkedList<>();
        expectedPurchases.add(mPurchaseRepository.get(user, game1));
        expectedPurchases.add(mPurchaseRepository.get(user, game2));

        when(admin.seePurchases(user)).thenReturn(expectedPurchases);

        verify(mPurchaseRepository, times(1)).get(user, game1);
        verify(mPurchaseRepository, times(1)).get(user, game2);

    }
}