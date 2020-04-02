import DataHelper.DataProcessor;
import DataHelper.SqlUtils.DataBase;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.Data;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static DataHelper.SqlUtils.DataBase.*;
import static DataHelper.SqlUtils.DataBase.OrderEntityItem.getLastDbItemFromOrderEntity;


import static PageObjects.TourPage.*;
import static com.codeborne.selenide.Selenide.open;

@DisplayName("Service test")
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
        String sutUrl = System.getProperty("sut.url", "http://localhost");
        String sutPort = System.getProperty("sut.port", "8080");
        open(String.format("%s:%s", sutUrl, sutPort));
    }

    @Test
    void purchaseWithValidCardShouldBeSuccess() throws SQLException {
        DataProcessor.Card validCardForTest = DataProcessor.Card.getValidCardForTest();

        DataBase.OrderEntityItem lastOrder = getLastDbItemFromOrderEntity();
        selectPurchaseWithCard();
        fillCardNumberField(validCardForTest);
        fillCardMonthExpirationField(validCardForTest);
        fillCardYearExpirationField(validCardForTest);
        fillCardHolderField(validCardForTest);
        fillCardCvcCvvField(validCardForTest);
        submitForm();
        assertButtonStateChangedOnSuccessfulSubmit();

        assertSuccessNotificationIsVisibleIfPaymentApproved();
        assertNewOrderIsRecordedInDb(lastOrder);
        verifyEntriesAreInDbWithValidCard(validCardForTest);
        assertAmountOfPurchaseInDbEqualsToTourPrice(45000);
    }

    @Test
    void purchaseWithInvalidCardShouldBeDenied() throws SQLException {
        DataProcessor.Card invalidCardForTest = DataProcessor.Card.getInvalidCardForTest();

        DataBase.OrderEntityItem lastOrder = getLastDbItemFromOrderEntity();
        selectPurchaseWithCard();
        fillCardNumberField(invalidCardForTest);
        fillCardMonthExpirationField(invalidCardForTest);
        fillCardYearExpirationField(invalidCardForTest);
        fillCardHolderField(invalidCardForTest);
        fillCardCvcCvvField(invalidCardForTest);
        submitForm();
        assertButtonStateChangedOnSuccessfulSubmit();

        assertRejectedNotificationIsVisibleIfPaymentDeclined();
        assertNewOrderIsRecordedInDb(lastOrder);
        verifyEntriesAreInDbWithInvalidCard(invalidCardForTest);
        assertAmountOfPurchaseInDbEqualsToTourPrice(45000);
    }

    @Test
    void purchaseWithLoanWithValidCardShouldBeSuccess() throws SQLException {
        DataProcessor.Card validCardForTest = DataProcessor.Card.getValidCardForTest();

        DataBase.OrderEntityItem lastOrder = getLastDbItemFromOrderEntity();
        selectPurchaseWithLoan();
        fillCardNumberField(validCardForTest);
        fillCardMonthExpirationField(validCardForTest);
        fillCardYearExpirationField(validCardForTest);
        fillCardHolderField(validCardForTest);
        fillCardCvcCvvField(validCardForTest);
        submitForm();
        assertButtonStateChangedOnSuccessfulSubmit();

        assertSuccessNotificationIsVisibleIfPaymentApproved();
        assertNewOrderIsRecordedInDb(lastOrder);
        verifyEntriesAreInDbWithValidCardWithLoan(validCardForTest);
    }

    @Test
    void purchaseWithLoanWithInvalidCardShouldBeDenied() throws SQLException {
        DataProcessor.Card invalidCardForTest = DataProcessor.Card.getInvalidCardForTest();

        DataBase.OrderEntityItem lastOrder = getLastDbItemFromOrderEntity();
        selectPurchaseWithLoan();
        fillCardNumberField(invalidCardForTest);
        fillCardMonthExpirationField(invalidCardForTest);
        fillCardYearExpirationField(invalidCardForTest);
        fillCardHolderField(invalidCardForTest);
        fillCardCvcCvvField(invalidCardForTest);
        submitForm();
        assertButtonStateChangedOnSuccessfulSubmit();

        assertRejectedNotificationIsVisibleIfPaymentDeclined();
        assertNewOrderIsRecordedInDb(lastOrder);
        verifyEntriesAreInDbWithInvalidCardWithLoan(invalidCardForTest);
    }

    @Test
    void purchaseWithUnexpectedCardShouldBeDenied() throws SQLException {
        DataProcessor.Card invalidCardForTest = DataProcessor.Card.getUnexpectedCardForTest();

        DataBase.OrderEntityItem lastOrder = getLastDbItemFromOrderEntity();
        selectPurchaseWithCard();
        fillCardNumberField(invalidCardForTest);
        fillCardMonthExpirationField(invalidCardForTest);
        fillCardYearExpirationField(invalidCardForTest);
        fillCardHolderField(invalidCardForTest);
        fillCardCvcCvvField(invalidCardForTest);
        submitForm();
        assertButtonStateChangedOnSuccessfulSubmit();

        assertRejectedNotificationIsVisibleIfPaymentDeclined();
        assertNewOrderIsRecordedInDb(lastOrder);
        verifyEntriesAreInDbWithInvalidCard(invalidCardForTest);
        assertAmountOfPurchaseInDbEqualsToTourPrice(45000);
    }
    @Test
    void purchaseWithLoanWithUnexpectedCardShouldBeDenied() throws SQLException {
        DataProcessor.Card invalidCardForTest = DataProcessor.Card.getUnexpectedCardForTest();

        DataBase.OrderEntityItem lastOrder = getLastDbItemFromOrderEntity();
        selectPurchaseWithLoan();
        fillCardNumberField(invalidCardForTest);
        fillCardMonthExpirationField(invalidCardForTest);
        fillCardYearExpirationField(invalidCardForTest);
        fillCardHolderField(invalidCardForTest);
        fillCardCvcCvvField(invalidCardForTest);
        submitForm();
        assertButtonStateChangedOnSuccessfulSubmit();

        assertRejectedNotificationIsVisibleIfPaymentDeclined();
        assertNewOrderIsRecordedInDb(lastOrder);
        verifyEntriesAreInDbWithInvalidCardWithLoan(invalidCardForTest);
    }
}
