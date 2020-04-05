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
    static final String databaseUrl = System.getProperty("datasource.url", "jdbc:mysql://localhost:3306/app");
    static final String databaseUser = System.getProperty("datasource.username", "app");
    static final String databasePassword = System.getProperty("datasource.password", "pass");

    public static CreditRequestEntity getLastDbItemFromCreditRequestEntity() throws SQLException {
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)
        ) {
            return runner.query(conn, "SELECT * FROM credit_request_entity ORDER BY created DESC", new BeanHandler<>(CreditRequestEntity.class));
        }
    }


    public static OrderEntity getLastDbItemFromOrderEntity() throws SQLException {
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)
        ) {
            return runner.query(conn, "SELECT * FROM order_entity ORDER BY created DESC", new BeanHandler<>(OrderEntity.class));
        }
    }

    public static PaymentEntity getLastDbItemFromPaymentEntity() throws SQLException {
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)
        ) {
            return runner.query(conn, "SELECT * FROM payment_entity ORDER BY created DESC", new BeanHandler<>(PaymentEntity.class));
        }
    }

    public static void assertNewOrderIsRecordedInDb(OrderEntity orderEntityBeforeTest) throws SQLException {
        if (orderEntityBeforeTest != null) {
            assertNotEquals(orderEntityBeforeTest, getLastDbItemFromOrderEntity());
        } else {
            assertNotNull(getLastDbItemFromOrderEntity());
        }
    }

    public static void assertNewOrderIsNotRecordedInDb(OrderEntity orderEntityBeforeTest) throws SQLException {
        if (orderEntityBeforeTest != null) {
            assertEquals(orderEntityBeforeTest, getLastDbItemFromOrderEntity());
        } else {
            assertNull(getLastDbItemFromOrderEntity());
        }
    }

    public static void verifyEntriesAreInDbWithValidCard(DataProcessor.Card card) throws SQLException {
        assertEquals(paymentGateRequest(card.getCardNumber()), getLastDbItemFromPaymentEntity().getStatus());
        assertEquals(getLastDbItemFromPaymentEntity().getTransaction_id(), getLastDbItemFromOrderEntity().getPayment_id());
    }

    public static void assertAmountOfPurchaseInDbEqualsToTourPrice(int tourPrice) throws SQLException {
        assertEquals(tourPrice, getLastDbItemFromPaymentEntity().getAmount());
    }

    public static void verifyEntriesAreInDbWithInvalidCard(DataProcessor.Card card) throws SQLException {
        assertEquals(paymentGateRequest(card.getCardNumber()), getLastDbItemFromPaymentEntity().getStatus());
        assertNull(getLastDbItemFromOrderEntity().getCredit_id());
        assertNull(getLastDbItemFromOrderEntity().getPayment_id());
    }

    public static void verifyEntriesAreInDbWithValidCardWithLoan(DataProcessor.Card card) throws SQLException {
        assertEquals(creditGateRequest(card.getCardNumber()), getLastDbItemFromCreditRequestEntity().getStatus());
        assertEquals(getLastDbItemFromCreditRequestEntity().getBank_id(), getLastDbItemFromOrderEntity().getCredit_id());
    }

    public static void verifyEntriesAreInDbWithInvalidCardWithLoan(DataProcessor.Card card) throws SQLException {
        assertEquals(creditGateRequest(card.getCardNumber()), getLastDbItemFromCreditRequestEntity().getStatus());
        assertNull(getLastDbItemFromOrderEntity().getCredit_id());
        assertNull(getLastDbItemFromOrderEntity().getPayment_id());
    }

}
