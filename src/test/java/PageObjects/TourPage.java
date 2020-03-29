package PageObjects;

import DataHelper.DataProcessor;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TourPage {
    private static SelenideElement purchaseWithCardButton = $(byText("Купить"));
    private static SelenideElement purchaseWithLoanButton = $(byText("Купить в кредит"));

    private static SelenideElement cardNumberField = $("input[maxlength='19']");
    private static SelenideElement cardMonthExpirationField = $$("input[maxlength='2']").first();
    private static SelenideElement cardYearExpirationField = $$("input[maxlength='2']").last();
    private static SelenideElement cardHolderField = $(byText("Владелец")).parent().$("input");
    private static SelenideElement cardCvcCvvField = $("input[maxlength='3']");

    private static SelenideElement continueButton = $(withText("Продолжить"));
    private static SelenideElement waitingButton = $(withText("Отправляем запрос в Банк"));


    public static void clearForm() {
        cardNumberField.setValue("");
        cardMonthExpirationField.setValue("");
        cardYearExpirationField.setValue("");
        cardHolderField.setValue("");
        cardCvcCvvField.setValue("");
    }

    public static void selectPurchaseWithCard() {
        purchaseWithCardButton.click();
        $(byText("Оплата по карте")).shouldBe(visible);
    }

    public static void selectPurchaseWithLoan() {
        purchaseWithLoanButton.click();
        $(byText("Кредит по данным карты")).shouldBe(visible);
    }

    public static void fillCardNumberField(DataProcessor.Card card) {
        cardNumberField.setValue(card.getCardNumber());
    }

    public static void fillCardMonthExpirationField(DataProcessor.Card card) {
        cardMonthExpirationField.setValue(card.getCardMonthExpiration());
    }

    public static void fillCardYearExpirationField(DataProcessor.Card card) {
        cardYearExpirationField.setValue(card.getCardYearExpiration());
    }

    public static void fillCardHolderField(DataProcessor.Card card) {
        cardHolderField.setValue(card.getCardHolder());
    }

    public static void fillCardCvcCvvField(DataProcessor.Card card) {
        cardCvcCvvField.setValue(card.getCardCvcCvv());
    }

    public static void submitForm() {
        continueButton.click();
    }

    public static void assertButtonStateChangedOnSuccessfulSubmit() {
        continueButton.shouldBe(hidden);
        waitingButton.waitUntil(visible, 1000);
    }

    public static void assertButtonStateNotChangedWithErrorsInForm() {
        assertTrue(continueButton.getText().contains("Продолжить"));
    }

    public static void assertSuccessNotificationIsVisibleIfPaymentApproved() {
        $$("div.notification").find(text("Операция одобрена банком")).waitUntil(visible, 10000);
        $$("div.notification").find(text("Ошибка! Банк отказал в операции")).waitUntil(hidden, 10000);
    }

    public static void assertRejectedNotificationIsVisibleIfPaymentDeclined() {
        $$("div.notification").find(text("Операция одобрена банком")).waitUntil(hidden, 10000);
        $$("div.notification").find(text("Ошибка! Банк отказал в операции")).waitUntil(visible, 10000);
    }

    // Валидация полей формы
    public static void assertInvalidFormatErrorVisibleForCardNumberField() {
        cardNumberField.parent().parent().$("span.input__sub").shouldHave(exactText("Неверный формат"));
        cardNumberField.parent().parent().$("span.input__sub").shouldBe(visible);
    }

    public static void assertInvalidFormatErrorVisibleForCardExpirationFieldIfValid() {
        cardYearExpirationField.parent().parent().$("span.input__sub").shouldHave(exactText("Неверный формат"));
        cardYearExpirationField.parent().parent().$("span.input__sub").shouldBe(visible);
    }

    public static void assertInvalidFormatErrorVisibleForCardExpirationFieldIfExpired() {
        cardYearExpirationField.parent().parent().$("span.input__sub").shouldHave(exactText("Истёк срок действия карты"));
        cardYearExpirationField.parent().parent().$("span.input__sub").shouldBe(visible);
    }

    public static void assertInvalidFormatErrorVisibleForCardHolderField() {
        cardHolderField.parent().parent().$("span.input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
        cardHolderField.parent().parent().$("span.input__sub").shouldBe(visible);
    }

    public static void assertInvalidFormatErrorVisibleForCardCvcCvvField() {
        cardCvcCvvField.parent().parent().$("span.input__sub").shouldHave(exactText("Неверный формат"));
        cardCvcCvvField.parent().parent().$("span.input__sub").shouldBe(visible);
    }

}



