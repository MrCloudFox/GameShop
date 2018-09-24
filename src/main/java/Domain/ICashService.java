package Domain;

import javax.management.OperationsException;

public interface ICashService {
    void setCash(User user, double sum);
}