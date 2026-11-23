package com.contoso.orders;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

public class OrderDaoTest {

    @BeforeClass
    public static void useInMemoryDb() {
        DbConfig.override("db.url", "jdbc:h2:mem:orders;MODE=MySQL;DB_CLOSE_DELAY=-1");
        DbConfig.override("db.user", "sa");
        DbConfig.override("db.password", "");
    }

    @Test
    public void saveAndFindAll() throws Exception {
        OrderDao dao = new OrderDao();
        dao.init();
        dao.save(new Order("홍길동", "노트북", 2));
        assertEquals(1, dao.findAll().size());
        assertEquals("노트북", dao.findAll().get(0).getItem());
    }
}
