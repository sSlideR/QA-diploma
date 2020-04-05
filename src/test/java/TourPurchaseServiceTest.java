import DataHelper.DataProcessor;
import DataHelper.SqlUtils.OrderEntity;
import PageObjects.TourPage;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.Data;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static DataHelper.SqlUtils.DataBase.*;
import static DataHelper.SqlUtils.DataBase.getLastDbItemFromOrderEntity;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
        String sutUrl = System.getProperty("sut.url");
        String sutPort = System.getProperty("sut.port");
        open(String.format("%s:%s", sutUrl, sutPort));
    }

    @Test
    void purchaseWithValidCardShouldBeSuccess() throws SQLException {
        TourPage tourPage = new TourPage();
        DataProcessor.Card validCardForTest = DataProcessor.Card.getValidCardForTest();
        OrderEntity lastOrder = getLastDbItemFromOrderEntity();

        tourPage.selectPurchaseWithCard();
        tourPage.fillCardNumberField(validCardForTest);
        tourPage.fillCardMonthExpirationField(validCardForTest);
        tourPage.fillCardYearExpirationField(validCardForTest);
        tourPage.fillCardHolderField(validCardForTest);
        tourPage.fillCardCvcCvvField(validCardForTest);
        tourPage.submitForm();
        tourPage.assertButtonStateChangedOnSuccessfulSubmit();

        tourPage.assertSuccessNotificationIsVisibleIfPaymentApproved();
        assertNotEquals(lastOrder, getLastDbItemFromOrderEntity());
        verifyEntriesAreInDbWithValidCard(validCardForTest);
        assertAmountOfPurchaseInDbEqualsToTourPrice(45000);
    }

    @Test
    void purchaseWithInvalidCardShouldBeDenied() throws SQLException {
        TourPage tourPage = new TourPage();
        DataProcessor.Card invalidCardForTest = DataProcessor.Card.getInvalidCardForTest();
        OrderEntity lastOrder = getLastDbItemFromOrderEntity();

        tourPage.selectPurchaseWithCard();
        tourPage.fillCardNumberField(invalidCardForTest);
        tourPage.fillCardMonthExpirationField(invalidCardForTest);
        tourPage.fillCardYearExpirationField(invalidCardForTest);
        tourPage.fillCardHolderField(invalidCardForTest);
        tourPage.fillCardCvcCvvField(invalidCardForTest);
        tourPage.submitForm();
        tourPage.assertButtonStateChangedOnSuccessfulSubmit();

        tourPage.assertRejectedNotificationIsVisibleIfPaymentDeclined();
        assertNotEquals(lastOrder, getLastDbItemFromOrderEntity());
        verifyEntriesAreInDbWithInvalidCard(invalidCardForTest);
        assertAmountOfPurchaseInDbEqualsToTourPrice(45000);
    }

    @Test
    void purchaseWithLoanWithValidCardShouldBeSuccess() throws SQLException {
        TourPage tourPage = new TourPage();
        DataProcessor.Card validCardForTest = DataProcessor.Card.getValidCardForTest();
        OrderEntity lastOrder = getLastDbItemFromOrderEntity();

        tourPage.selectPurchaseWithLoan();
        tourPage.fillCardNumberField(validCardForTest);
        tourPage.fillCardMonthExpirationField(validCardForTest);
        tourPage.fillCardYearExpirationField(validCardForTest);
        tourPage.fillCardHolderField(validCardForTest);
        tourPage.fillCardCvcCvvField(validCardForTest);
        tourPage.submitForm();
        tourPage.assertButtonStateChangedOnSuccessfulSubmit();

        tourPage.assertSuccessNotificationIsVisibleIfPaymentApproved();
        assertNotEquals(lastOrder, getLastDbItemFromOrderEntity());
        verifyEntriesAreInDbWithValidCardWithLoan(validCardForTest);
    }

    @Test
    void purchaseWithLoanWithInvalidCardShouldBeDenied() throws SQLException {
        TourPage tourPage = new TourPage();
        DataProcessor.Card invalidCardForTest = DataProcessor.Card.getInvalidCardForTest();
        OrderEntity lastOrder = getLastDbItemFromOrderEntity();

        tourPage.selectPurchaseWithLoan();
        tourPage.fillCardNumberField(invalidCardForTest);
        tourPage.fillCardMonthExpirationField(invalidCardForTest);
        tourPage.fillCardYearExpirationField(invalidCardForTest);
        tourPage.fillCardHolderField(invalidCardForTest);
        tourPage.fillCardCvcCvvField(invalidCardForTest);
        tourPage.submitForm();
        tourPage.assertButtonStateChangedOnSuccessfulSubmit();

        tourPage.assertRejectedNotificationIsVisibleIfPaymentDeclined();
        assertNotEquals(lastOrder, getLastDbItemFromOrderEntity());
        verifyEntriesAreInDbWithInvalidCardWithLoan(invalidCardForTest);
    }

    @Test
    void purchaseWithUnexpectedCardShouldBeDenied() throws SQLException {
        TourPage tourPage = new TourPage();
        DataProcessor.Card invalidCardForTest = DataProcessor.Card.getUnexpectedCardForTest();
        OrderEntity lastOrder = getLastDbItemFromOrderEntity();

        tourPage.selectPurchaseWithCard();
        tourPage.fillCardNumberField(invalidCardForTest);
        tourPage.fillCardMonthExpirationField(invalidCardForTest);
        tourPage.fillCardYearExpirationField(invalidCardForTest);
        tourPage.fillCardHolderField(invalidCardForTest);
        tourPage.fillCardCvcCvvField(invalidCardForTest);
        tourPage.submitForm();
        tourPage.assertButtonStateChangedOnSuccessfulSubmit();

        tourPage.assertRejectedNotificationIsVisibleIfPaymentDeclined();
        assertNotEquals(lastOrder, getLastDbItemFromOrderEntity());
        verifyEntriesAreInDbWithInvalidCard(invalidCardForTest);
        assertAmountOfPurchaseInDbEqualsToTourPrice(45000);
    }
    @Test
    void purchaseWithLoanWithUnexpectedCardShouldBeDenied() throws SQLException {
        TourPage tourPage = new TourPage();
        DataProcessor.Card invalidCardForTest = DataProcessor.Card.getUnexpectedCardForTest();
        OrderEntity lastOrder = getLastDbItemFromOrderEntity();

        tourPage.selectPurchaseWithLoan();
        tourPage.fillCardNumberField(invalidCardForTest);
        tourPage.fillCardMonthExpirationField(invalidCardForTest);
        tourPage.fillCardYearExpirationField(invalidCardForTest);
        tourPage.fillCardHolderField(invalidCardForTest);
        tourPage.fillCardCvcCvvField(invalidCardForTest);
        tourPage.submitForm();
        tourPage.assertButtonStateChangedOnSuccessfulSubmit();

        tourPage.assertRejectedNotificationIsVisibleIfPaymentDeclined();
        assertNotEquals(lastOrder, getLastDbItemFromOrderEntity());
        verifyEntriesAreInDbWithInvalidCardWithLoan(invalidCardForTest);
    }
}
