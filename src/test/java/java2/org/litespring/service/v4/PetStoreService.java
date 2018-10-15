package java2.org.litespring.service.v4;

import java2.org.litespring.beans.factory.annotation.Autowired;
import java2.org.litespring.dao.v4.AccountDao;
import java2.org.litespring.dao.v4.ItemDao;
import java2.org.litespring.stereotype.Component;

@Component(value = "petStore")
public class PetStoreService {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private ItemDao itemDao;

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }
}
