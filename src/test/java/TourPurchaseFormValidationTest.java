import DataHelper.DataProcessor;
import DataHelper.SqlUtils.DataBase;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.Data;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static DataHelper.SqlUtils.DataBase.OrderEntityItem.getLastDbItemFromOrderEntity;
import static DataHelper.SqlUtils.DataBase.assertNewOrderIsNotRecordedInDb;
import static PageObjects.TourPage.*;
import static com.codeborne.selenide.Selenide.open;

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
        String sutUrl = System.getProperty("sut.url", "http://localhost");
        String sutPort = System.getProperty("sut.port", "8080");
        open(String.format("%s:%s", sutUrl, sutPort));
    }

    @Test
    void blankFormShouldntBeSendWithCard() throws SQLException {
        DataBase.OrderEntityItem lastOrder = getLastDbItemFromOrderEntity();

        selectPurchaseWithCard();
        clearForm();
        submitForm();

        assertButtonStateNotChangedWithErrorsInForm();
        assertNewOrderIsNotRecordedInDb(lastOrder);
        assertInvalidFormatErrorVisibleForCardExpirationFieldIfValid();
        assertInvalidFormatErrorVisibleForCardHolderField();
        assertInvalidFormatErrorVisibleForCardCvcCvvField();
        assertInvalidFormatErrorVisibleForCardNumberField();
        assertInvalidFormatErrorVisibleForCardCvcCvvField();
    }

    @Test
    void blankFormShouldntBeSendWithLoan() throws SQLException {
        DataBase.OrderEntityItem lastOrder = getLastDbItemFromOrderEntity();

        selectPurchaseWithLoan();
        clearForm();
        submitForm();

        assertButtonStateNotChangedWithErrorsInForm();
        assertNewOrderIsNotRecordedInDb(lastOrder);
        assertInvalidFormatErrorVisibleForCardExpirationFieldIfValid();
        assertInvalidFormatErrorVisibleForCardHolderField();
        assertInvalidFormatErrorVisibleForCardCvcCvvField();
        assertInvalidFormatErrorVisibleForCardNumberField();
        assertInvalidFormatErrorVisibleForCardCvcCvvField();
    }

    @Test
    void errorNotificationShouldBeVisibleIfShortenedCardNumberInsertedInCardNumberField() {
        DataProcessor.Card validCardForTest = DataProcessor.Card.getValidCardForTest();
        DataProcessor.Card brokenCardForTest = DataProcessor.Card.CardFaker.getCardWithShortenedCardNumber();

        selectPurchaseWithCard();
        fillCardNumberField(brokenCardForTest);
        fillCardMonthExpirationField(validCardForTest);
        fillCardYearExpirationField(validCardForTest);
        fillCardHolderField(validCardForTest);
        fillCardCvcCvvField(validCardForTest);
        submitForm();

        assertInvalidFormatErrorVisibleForCardNumberField();
        assertButtonStateNotChangedWithErrorsInForm();
    }

    @Test
    void errorNotificationShouldBeVisibleIfExpiredCardDueDatesInsertedInExpirationFields() {
        DataProcessor.Card validCardForTest = DataProcessor.Card.getValidCardForTest();
        DataProcessor.Card brokenCardForTest = DataProcessor.Card.CardFaker.getCardWithExpiredDates();

        selectPurchaseWithCard();
        fillCardNumberField(validCardForTest);
        fillCardMonthExpirationField(brokenCardForTest);
        fillCardYearExpirationField(brokenCardForTest);
        fillCardHolderField(validCardForTest);
        fillCardCvcCvvField(validCardForTest);
        submitForm();

        assertInvalidFormatErrorVisibleForCardExpirationFieldIfExpired();
        assertButtonStateNotChangedWithErrorsInForm();
    }

    @Test
    void errorNotificationShouldBeVisibleIfZeroDatesInsertedInExpirationFields() {
        DataProcessor.Card validCardForTest = DataProcessor.Card.getValidCardForTest();
        DataProcessor.Card brokenCardForTest = DataProcessor.Card.CardFaker.getCardWithZeroDates();

        selectPurchaseWithCard();
        fillCardNumberField(validCardForTest);
        fillCardMonthExpirationField(brokenCardForTest);
        fillCardYearExpirationField(brokenCardForTest);
        fillCardHolderField(validCardForTest);
        fillCardCvcCvvField(validCardForTest);
        submitForm();

        assertInvalidFormatErrorVisibleForCardExpirationFieldIfValid();
        assertButtonStateNotChangedWithErrorsInForm();
    }

    @Test
    void errorNotificationShouldBeVisibleIfShortenedCvcCvvInsertedInCardCvcCvvField() {
        DataProcessor.Card validCardForTest = DataProcessor.Card.getValidCardForTest();
        DataProcessor.Card brokenCardForTest = DataProcessor.Card.CardFaker.getCardWithShortenedCvcCvv();

        selectPurchaseWithCard();
        fillCardNumberField(validCardForTest);
        fillCardMonthExpirationField(validCardForTest);
        fillCardYearExpirationField(validCardForTest);
        fillCardHolderField(validCardForTest);
        fillCardCvcCvvField(brokenCardForTest);
        submitForm();

        assertInvalidFormatErrorVisibleForCardCvcCvvField();
        assertButtonStateNotChangedWithErrorsInForm();
    }

}
