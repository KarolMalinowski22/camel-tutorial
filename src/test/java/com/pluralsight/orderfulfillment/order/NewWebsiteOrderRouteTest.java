package com.pluralsight.orderfulfillment.test.order.fulfillment;

import com.pluralsight.orderfulfillment.config.IntegrationConfig;
import com.pluralsight.orderfulfillment.test.TestIntegration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestIntegration.class,
        IntegrationConfig.class})
@WebAppConfiguration
public class NewWebsiteOrderRouteTest {
    @Inject
    private JdbcTemplate jdbcTemplate;
    @Before
    public void setUp(){
        jdbcTemplate.execute("insert into orders.catalogitem (id, itemnumber, itemname, itemtype)"
        + "values (1, '076-1344200444', 'Build your Own JavaScript Framework in Just 24 Hours', 'Book')");
        jdbcTemplate.execute("insert into orders.customer (id, firstname, lastname, email)"
        +"values (1, 'Larry', 'Horse', 'larry@hello.com')");
    }
    @After
    public void tearDown(){
        jdbcTemplate.execute("delete from orders.orderitem");
        jdbcTemplate.execute("delete from orders.\"order\"");
        jdbcTemplate.execute("delete from orders.catalogitem");
        jdbcTemplate.execute("delete from orders.customer");
    }
    @Test
    public void testNewWebsiteOrderRouteSuccess() throws InterruptedException {
        jdbcTemplate.execute("insert into orders.\"order\" (id, customer_id, ordernumber, timeorderplaced, lastupdate, status)"
        + "values (1, 1, '1001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'N')");
        jdbcTemplate.execute("insert into orders.orderitem (id, order_id, catalogitem_id, status, price, quantity, lastupdate)"
        +"values (1, 1, 1, 'N', 20.00, 1, CURRENT_TIMESTAMP)");
        Thread.sleep(5000);
        int total = jdbcTemplate.queryForObject("select count(id) from orders.\"order\" where status = 'P'",
                Integer.class);
        assertEquals(1, total);
    }
}
