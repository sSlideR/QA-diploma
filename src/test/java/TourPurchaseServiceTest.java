import DataHelper.DataBaseElements.OrderEntity;
import DataHelper.DataProcessor;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.Data;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static DataHelper.DataBase.*;
import static PageObjects.TourPage.*;
import static com.codeborne.selenide.Selenide.open;

@Data
public class TourPurchaseServiceTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void openBrowser() {
        String sutUrl = (System.getProperty("sut.url") == null || "".equals(System.getProperty("sut.url"))) ? "http://localhost:8080" : System.getProperty("sut.url");
        open(sutUrl);
    }

    // Form Validation
    @Test
    void formShouldValidateInputData() {
        sendFormWithBrokenData();
    }

    @Test
    void formShouldntSendWithoutInputData() {
        sendBlankFormWithCard();
        sendBlankFormWithLoan();
    }

    // App test
    @Test
    void purchaseWithValidCardShouldBeSuccess() throws SQLException {
        DataProcessor.Card validCardForTest = DataProcessor.Card.getValidCardForTest();

        OrderEntity lastOrder = getOrdersStateBefore();
        selectPurchaseWithCard();
        sendFormWithValidCardData(validCardForTest);
        isNewOrderIsRecorded(lastOrder);
        verifyEntriesAreInDbWithCard(validCardForTest, 45000);
    }

    @Test
    void purchaseWithInvalidCardShouldBeDenied() throws SQLException {
        DataProcessor.Card invalidCardForTest = DataProcessor.Card.getInvalidCardForTest();

        OrderEntity lastOrder = getOrdersStateBefore();
        selectPurchaseWithCard();
        sendFormWithInvalidCardData(invalidCardForTest);
        isNewOrderIsRecorded(lastOrder);
        verifyEntriesAreInDbWithCard(invalidCardForTest, 45000);
    }

    @Test
    void purchaseWithLoanWithValidCardShouldBeSuccess() throws SQLException {
        DataProcessor.Card validCardForTest = DataProcessor.Card.getValidCardForTest();

        OrderEntity lastOrder = getOrdersStateBefore();
        selectPurchaseWithLoan();
        sendFormWithValidCardData(validCardForTest);
        isNewOrderIsRecorded(lastOrder);
        verifyEntriesAreInDbWithLoan(validCardForTest);
    }

    @Test
    void purchaseWithLoanWithInvalidCardShouldBeDenied() throws SQLException {
        DataProcessor.Card invalidCardForTest = DataProcessor.Card.getInvalidCardForTest();

        OrderEntity lastOrder = getOrdersStateBefore();
        selectPurchaseWithLoan();
        sendFormWithInvalidCardData(invalidCardForTest);
        isNewOrderIsRecorded(lastOrder);
        verifyEntriesAreInDbWithLoan(invalidCardForTest);
    }
}
