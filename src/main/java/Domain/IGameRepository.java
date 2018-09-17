package Domain;

@FunctionalInterface
public interface IGameRepository {
    Game get(int id);
}
