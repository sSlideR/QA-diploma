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
    private SelenideElement purchaseWithCardButton = $(byText("Купить"));
    private SelenideElement purchaseWithLoanButton = $(byText("Купить в кредит"));

    private SelenideElement cardNumberField = $("input[maxlength='19']");
    private SelenideElement cardMonthExpirationField = $$("input[maxlength='2']").first();
    private SelenideElement cardYearExpirationField = $$("input[maxlength='2']").last();
    private SelenideElement cardHolderField = $(byText("Владелец")).parent().$("input");
    private SelenideElement cardCvcCvvField = $("input[maxlength='3']");

    private SelenideElement continueButton = $(withText("Продолжить"));
    private SelenideElement waitingButton = $(withText("Отправляем запрос в Банк"));


    public void clearForm() {
        cardNumberField.setValue("");
        cardMonthExpirationField.setValue("");
        cardYearExpirationField.setValue("");
        cardHolderField.setValue("");
        cardCvcCvvField.setValue("");
    }

    public void selectPurchaseWithCard() {
        purchaseWithCardButton.click();
        $(byText("Оплата по карте")).shouldBe(visible);
    }

    public void selectPurchaseWithLoan() {
        purchaseWithLoanButton.click();
        $(byText("Кредит по данным карты")).shouldBe(visible);
    }

    public void fillCardNumberField(DataProcessor.Card card) {
        cardNumberField.setValue(card.getCardNumber());
    }

    public void fillCardMonthExpirationField(DataProcessor.Card card) {
        cardMonthExpirationField.setValue(card.getCardMonthExpiration());
    }

    public void fillCardYearExpirationField(DataProcessor.Card card) {
        cardYearExpirationField.setValue(card.getCardYearExpiration());
    }

    public void fillCardHolderField(DataProcessor.Card card) {
        cardHolderField.setValue(card.getCardHolder());
    }

    public void fillCardCvcCvvField(DataProcessor.Card card) {
        cardCvcCvvField.setValue(card.getCardCvcCvv());
    }

    public void submitForm() {
        continueButton.click();
    }

    public void assertButtonStateChangedOnSuccessfulSubmit() {
        continueButton.shouldBe(hidden);
        waitingButton.waitUntil(visible, 1000);
    }

    public void assertButtonStateNotChangedWithErrorsInForm() {
        assertTrue(continueButton.getText().contains("Продолжить"));
    }

    public void assertSuccessNotificationIsVisibleIfPaymentApproved() {
        $$("div.notification").find(text("Операция одобрена банком")).waitUntil(visible, 10000);
        $$("div.notification").find(text("Ошибка! Банк отказал в операции")).waitUntil(hidden, 10000);
    }

    public void assertRejectedNotificationIsVisibleIfPaymentDeclined() {
        $$("div.notification").find(text("Операция одобрена банком")).waitUntil(hidden, 10000);
        $$("div.notification").find(text("Ошибка! Банк отказал в операции")).waitUntil(visible, 10000);
    }

    // Валидация полей формы
    public void assertInvalidFormatErrorVisibleForCardNumberField() {
        cardNumberField.parent().parent().$("span.input__sub").shouldHave(exactText("Неверный формат"));
        cardNumberField.parent().parent().$("span.input__sub").shouldBe(visible);
    }

    public void assertInvalidFormatErrorVisibleForCardExpirationFieldIfValid() {
        cardYearExpirationField.parent().parent().$("span.input__sub").shouldHave(exactText("Неверный формат"));
        cardYearExpirationField.parent().parent().$("span.input__sub").shouldBe(visible);
    }

    public void assertInvalidFormatErrorVisibleForCardExpirationFieldIfExpired() {
        cardYearExpirationField.parent().parent().$("span.input__sub").shouldHave(exactText("Истёк срок действия карты"));
        cardYearExpirationField.parent().parent().$("span.input__sub").shouldBe(visible);
    }

    public void assertInvalidFormatErrorVisibleForCardHolderField() {
        cardHolderField.parent().parent().$("span.input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
        cardHolderField.parent().parent().$("span.input__sub").shouldBe(visible);
    }

    public void assertInvalidFormatErrorVisibleForCardCvcCvvField() {
        cardCvcCvvField.parent().parent().$("span.input__sub").shouldHave(exactText("Неверный формат"));
        cardCvcCvvField.parent().parent().$("span.input__sub").shouldBe(visible);
    }

}



