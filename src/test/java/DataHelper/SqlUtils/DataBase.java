package DataHelper.SqlUtils;

import DataHelper.DataProcessor;
import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

import static DataHelper.GateApi.creditGateRequest;
import static DataHelper.GateApi.paymentGateRequest;
import static org.junit.jupiter.api.Assertions.*;

@Value
public class DataBase {
    private String dBUrl;
    private String dBUser;
    private String dBUserPass;

    protected static DataBase getDbAuthInfo() {
        String databaseUrl = System.getProperty("datasource.url");
        String databaseUser = System.getProperty("datasource.username") == null || "".equals(System.getProperty("datasource.username")) ? "app" : System.getProperty("datasource.username");
        String databasePassword = System.getProperty("datasource.password") == null || "".equals(System.getProperty("datasource.password")) ? "pass" : System.getProperty("datasource.password");
        return new DataBase(databaseUrl, databaseUser, databasePassword);
    }

    public static OrderEntity getLastDbItemFromOrderEntityBeforeTest() throws SQLException {
        return getLastDbItemFromOrderEntity();
    }

    protected static CreditRequestEntity getLastDbItemFromCreditRequestEntity() throws SQLException {
        val dataBase = getDbAuthInfo();
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(dataBase.getDBUrl(), dataBase.getDBUser(), dataBase.getDBUserPass());
        ) {
            val result = runner.query(conn, "SELECT * FROM credit_request_entity ORDER BY created DESC", new BeanHandler<>(CreditRequestEntity.class));
            return result;
        }
    }

    protected static OrderEntity getLastDbItemFromOrderEntity() throws SQLException {
        val dataBase = getDbAuthInfo();

        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(dataBase.getDBUrl(), dataBase.getDBUser(), dataBase.getDBUserPass());
        ) {
            val result = runner.query(conn, "SELECT * FROM order_entity ORDER BY created DESC", new BeanHandler<>(OrderEntity.class));
            return result;
        }
    }

    protected static PaymentEntity getLastDbItemFromPaymentEntity() throws SQLException {
        val dataBase = getDbAuthInfo();

        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(dataBase.getDBUrl(), dataBase.getDBUser(), dataBase.getDBUserPass());
        ) {
            val result = runner.query(conn, "SELECT * FROM payment_entity ORDER BY created DESC", new BeanHandler<>(PaymentEntity.class));
            return result;
        }
    }

    public static void isNewOrderIsRecorded(OrderEntity orderEntity) throws SQLException {
        String lastOrderTime = "";
        String lastOrderId = "";

        if (orderEntity != null) {
            lastOrderTime = orderEntity.getCreated();
            lastOrderId = orderEntity.getId();
            val currentOrderTime = getLastDbItemFromOrderEntity().getCreated();
            val currentOrderId = getLastDbItemFromOrderEntity().getId();
            val result = (!lastOrderTime.equals(currentOrderTime)) && (!lastOrderId.equals(currentOrderId));
            assertTrue(result);
        } else {
            val currentOrderTime = getLastDbItemFromOrderEntity().getCreated();
            val currentOrderId = getLastDbItemFromOrderEntity().getId();
            assertNotNull(currentOrderTime);
            assertNotNull(currentOrderId);
        }
    }

    public static void verifyEntriesAreInDbWithValidCard(DataProcessor.Card card, int tourPrice) throws SQLException {
        assertEquals("APPROVED", paymentGateRequest(card.getCardNumber()));
        assertEquals(paymentGateRequest(card.getCardNumber()), getLastDbItemFromPaymentEntity().getStatus());
        assertEquals(getLastDbItemFromPaymentEntity().getTransaction_id(), getLastDbItemFromOrderEntity().getPayment_id());
        assertEquals(tourPrice, getLastDbItemFromPaymentEntity().getAmount());
    }

    public static void verifyEntriesAreInDbWithInvalidCard(DataProcessor.Card card, int tourPrice) throws SQLException {
        assertEquals("DECLINED", getLastDbItemFromPaymentEntity().getStatus());
        assertEquals(paymentGateRequest(card.getCardNumber()), getLastDbItemFromPaymentEntity().getStatus());
        assertNull(getLastDbItemFromOrderEntity().getCredit_id());
        assertNull(getLastDbItemFromOrderEntity().getPayment_id());
        assertEquals(tourPrice, getLastDbItemFromPaymentEntity().getAmount());
    }

    public static void verifyEntriesAreInDbWithValidCardWithLoan(DataProcessor.Card card) throws SQLException {
        assertEquals("APPROVED", creditGateRequest(card.getCardNumber()));
        assertEquals(creditGateRequest(card.getCardNumber()), getLastDbItemFromCreditRequestEntity().getStatus());
        assertEquals(getLastDbItemFromCreditRequestEntity().getBank_id(), getLastDbItemFromOrderEntity().getCredit_id());
    }

    public static void verifyEntriesAreInDbWithInvalidCardWithLoan(DataProcessor.Card card) throws SQLException {
        assertEquals("DECLINED", getLastDbItemFromCreditRequestEntity().getStatus());
        assertEquals(creditGateRequest(card.getCardNumber()), getLastDbItemFromCreditRequestEntity().getStatus());
        assertNull(getLastDbItemFromOrderEntity().getCredit_id());
        assertNull(getLastDbItemFromOrderEntity().getPayment_id());
    }

}
