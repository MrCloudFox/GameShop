package Domain;

@FunctionalInterface
public interface IUserRepository {
    User get(String login);
}

