package Domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.*;

import javax.management.OperationsException;
import java.util.LinkedList;

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
    IGameRepository mGameRepository;
    @Mock
    IPurchaseRepository mPurchaseRepository;
    @Mock
    IUserRepository mUserRepository;

    @InjectMocks
    GameService gameService = new GameService(mDeliveryService, mPurchaseReturnsService, mPresentService, mFeedBackService, mNewsService);

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

        try {
            gameService.buyGame(user, game);
        } catch (OperationsException e) {
            e.printStackTrace();
        }

        verify(mDeliveryService, times(1)).deliverGame(user, game);
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
    }

}