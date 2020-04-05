import DataHelper.DataProcessor;
import DataHelper.SqlUtils.OrderEntity;
import PageObjects.TourPage;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.Data;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static DataHelper.SqlUtils.DataBase.getLastDbItemFromOrderEntity;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Form validation")
@Data
public class TourPurchaseFormValidationTest {
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
    void blankFormShouldntBeSendWithCard() throws SQLException {
        TourPage tourPage = new TourPage();
        OrderEntity lastOrder = getLastDbItemFromOrderEntity();

        tourPage.selectPurchaseWithCard();
        tourPage.clearForm();
        tourPage.submitForm();

        tourPage.assertButtonStateNotChangedWithErrorsInForm();
        assertEquals(lastOrder, getLastDbItemFromOrderEntity());
        tourPage.assertInvalidFormatErrorVisibleForCardExpirationFieldIfValid();
        tourPage.assertInvalidFormatErrorVisibleForCardHolderField();
        tourPage.assertInvalidFormatErrorVisibleForCardCvcCvvField();
        tourPage.assertInvalidFormatErrorVisibleForCardNumberField();
        tourPage.assertInvalidFormatErrorVisibleForCardCvcCvvField();
    }

    @Test
    void blankFormShouldntBeSendWithLoan() throws SQLException {
        TourPage tourPage = new TourPage();
        OrderEntity lastOrder = getLastDbItemFromOrderEntity();

        tourPage.selectPurchaseWithLoan();
        tourPage.clearForm();
        tourPage.submitForm();

        tourPage.assertButtonStateNotChangedWithErrorsInForm();
        assertEquals(lastOrder, getLastDbItemFromOrderEntity());
        tourPage.assertInvalidFormatErrorVisibleForCardExpirationFieldIfValid();
        tourPage.assertInvalidFormatErrorVisibleForCardHolderField();
        tourPage.assertInvalidFormatErrorVisibleForCardCvcCvvField();
        tourPage.assertInvalidFormatErrorVisibleForCardNumberField();
        tourPage.assertInvalidFormatErrorVisibleForCardCvcCvvField();
    }

    @Test
    void errorNotificationShouldBeVisibleIfShortenedCardNumberInsertedInCardNumberField() {
        TourPage tourPage = new TourPage();
        DataProcessor.Card validCardForTest = DataProcessor.Card.getValidCardForTest();
        DataProcessor.Card brokenCardForTest = DataProcessor.Card.CardFaker.getCardWithShortenedCardNumber();

        tourPage.selectPurchaseWithCard();
        tourPage.fillCardNumberField(brokenCardForTest);
        tourPage.fillCardMonthExpirationField(validCardForTest);
        tourPage.fillCardYearExpirationField(validCardForTest);
        tourPage.fillCardHolderField(validCardForTest);
        tourPage.fillCardCvcCvvField(validCardForTest);
        tourPage.submitForm();

        tourPage.assertInvalidFormatErrorVisibleForCardNumberField();
        tourPage.assertButtonStateNotChangedWithErrorsInForm();
    }

    @Test
    void errorNotificationShouldBeVisibleIfExpiredCardDueDatesInsertedInExpirationFields() {
        TourPage tourPage = new TourPage();
        DataProcessor.Card validCardForTest = DataProcessor.Card.getValidCardForTest();
        DataProcessor.Card brokenCardForTest = DataProcessor.Card.CardFaker.getCardWithExpiredDates();

        tourPage.selectPurchaseWithCard();
        tourPage.fillCardNumberField(validCardForTest);
        tourPage.fillCardMonthExpirationField(brokenCardForTest);
        tourPage.fillCardYearExpirationField(brokenCardForTest);
        tourPage.fillCardHolderField(validCardForTest);
        tourPage.fillCardCvcCvvField(validCardForTest);
        tourPage.submitForm();

        tourPage.assertInvalidFormatErrorVisibleForCardExpirationFieldIfExpired();
        tourPage.assertButtonStateNotChangedWithErrorsInForm();
    }

    @Test
    void errorNotificationShouldBeVisibleIfZeroDatesInsertedInExpirationFields() {
        TourPage tourPage = new TourPage();
        DataProcessor.Card validCardForTest = DataProcessor.Card.getValidCardForTest();
        DataProcessor.Card brokenCardForTest = DataProcessor.Card.CardFaker.getCardWithZeroDates();

        tourPage.selectPurchaseWithCard();
        tourPage.fillCardNumberField(validCardForTest);
        tourPage.fillCardMonthExpirationField(brokenCardForTest);
        tourPage.fillCardYearExpirationField(brokenCardForTest);
        tourPage.fillCardHolderField(validCardForTest);
        tourPage.fillCardCvcCvvField(validCardForTest);
        tourPage.submitForm();

        tourPage.assertInvalidFormatErrorVisibleForCardExpirationFieldIfValid();
        tourPage.assertButtonStateNotChangedWithErrorsInForm();
    }

    @Test
    void errorNotificationShouldBeVisibleIfShortenedCvcCvvInsertedInCardCvcCvvField() {
        TourPage tourPage = new TourPage();
        DataProcessor.Card validCardForTest = DataProcessor.Card.getValidCardForTest();
        DataProcessor.Card brokenCardForTest = DataProcessor.Card.CardFaker.getCardWithShortenedCvcCvv();

        tourPage.selectPurchaseWithCard();
        tourPage.fillCardNumberField(validCardForTest);
        tourPage.fillCardMonthExpirationField(validCardForTest);
        tourPage.fillCardYearExpirationField(validCardForTest);
        tourPage.fillCardHolderField(validCardForTest);
        tourPage.fillCardCvcCvvField(brokenCardForTest);
        tourPage.submitForm();

        tourPage.assertInvalidFormatErrorVisibleForCardCvcCvvField();
        tourPage.assertButtonStateNotChangedWithErrorsInForm();
    }

}
