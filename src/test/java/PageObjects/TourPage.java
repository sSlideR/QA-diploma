package PageObjects;

import DataHelper.DataProcessor;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.conditions.Text;
import lombok.Value;
import lombok.val;

import static DataHelper.DataProcessor.Card.getFakeCardForTest;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class TourPage {
    private static SelenideElement purchaseWithCardButton = $(byText("Купить"));
    private static SelenideElement purchaseWithLoanButton = $(byText("Купить в кредит"));

    private static SelenideElement cardNumber = $("input[maxlength='19']");
    private static SelenideElement cardMonthExpiration = $$("input[maxlength='2']").first();
    private static SelenideElement cardYearExpiration = $$("input[maxlength='2']").last();
    private static SelenideElement cardHolder = $(byText("Владелец")).parent().$("input");
    private static SelenideElement cardCvcCvv = $("input[maxlength='3']");

    private static SelenideElement continueButton = $(withText("Продолжить"));
    private static SelenideElement waitingButton = $(withText("Отправляем запрос в Банк"));


    public static void clearForm() {
        cardNumber.setValue("");
        cardMonthExpiration.setValue("");
        cardYearExpiration.setValue("");
        cardHolder.setValue("");
        cardCvcCvv.setValue("");
    }

    public static void selectPurchaseWithCard() {
        purchaseWithCardButton.click();
        $(byText("Оплата по карте")).shouldBe(visible);
    }

    public static void selectPurchaseWithLoan() {
        purchaseWithLoanButton.click();
        $(byText("Кредит по данным карты")).shouldBe(visible);
    }

    public static void sendFormWithValidCardData(DataProcessor.Card card) {
        cardNumber.setValue(card.getCardNumber());
        cardMonthExpiration.setValue(card.getCardMonthExpiration());
        cardYearExpiration.setValue(card.getCardYearExpiration());
        cardHolder.setValue(card.getCardHolder());
        cardCvcCvv.setValue(card.getCardCvcCvv());
        continueButton.click();
        continueButton.shouldBe(hidden);
        waitingButton.waitUntil(visible, 1000);
        $$("div.notification").find(text("Операция одобрена банком")).waitUntil(visible, 10000);
        $$("div.notification").find(text("Ошибка! Банк отказал в операции")).waitUntil(hidden, 10000);
    }

    public static void sendFormWithInvalidCardData(DataProcessor.Card card) {
        cardNumber.setValue(card.getCardNumber());
        cardMonthExpiration.setValue(card.getCardMonthExpiration());
        cardYearExpiration.setValue(card.getCardYearExpiration());
        cardHolder.setValue(card.getCardHolder());
        cardCvcCvv.setValue(card.getCardCvcCvv());
        continueButton.click();
        continueButton.shouldBe(hidden);
        waitingButton.waitUntil(visible, 1000);
        $$("div.notification").find(text("Операция одобрена банком")).waitUntil(hidden, 10000);
        $$("div.notification").find(text("Ошибка! Банк отказал в операции")).waitUntil(visible, 10000);
    }

    public static void sendBlankFormWithCard() {
        selectPurchaseWithCard();
        clearForm();
        continueButton.click();
        assertTrue(continueButton.getText().contains("Продолжить"));

        cardNumber.parent().parent().$("span.input__sub").shouldHave(exactText("Неверный формат"));
        cardNumber.parent().parent().$("span.input__sub").shouldBe(visible);
        cardMonthExpiration.parent().parent().$("span.input__sub").shouldHave(exactText("Неверный формат"));
        cardMonthExpiration.parent().parent().$("span.input__sub").shouldBe(visible);
        cardYearExpiration.parent().parent().$("span.input__sub").shouldHave(exactText("Неверный формат"));
        cardYearExpiration.parent().parent().$("span.input__sub").shouldBe(visible);
        cardHolder.parent().parent().$("span.input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
        cardHolder.parent().parent().$("span.input__sub").shouldBe(visible);
        cardCvcCvv.parent().parent().$("span.input__sub").shouldHave(exactText("Неверный формат"));
        cardCvcCvv.parent().parent().$("span.input__sub").shouldBe(visible);
    }

    public static void sendBlankFormWithLoan() {
        selectPurchaseWithLoan();
        clearForm();
        continueButton.click();
        assertTrue(continueButton.getText().contains("Продолжить"));

        cardNumber.parent().parent().$("span.input__sub").shouldHave(exactText("Неверный формат"));
        cardNumber.parent().parent().$("span.input__sub").shouldBe(visible);
        cardMonthExpiration.parent().parent().$("span.input__sub").shouldHave(exactText("Неверный формат"));
        cardMonthExpiration.parent().parent().$("span.input__sub").shouldBe(visible);
        cardYearExpiration.parent().parent().$("span.input__sub").shouldHave(exactText("Неверный формат"));
        cardYearExpiration.parent().parent().$("span.input__sub").shouldBe(visible);
        cardHolder.parent().parent().$("span.input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
        cardHolder.parent().parent().$("span.input__sub").shouldBe(visible);
        cardCvcCvv.parent().parent().$("span.input__sub").shouldHave(exactText("Неверный формат"));
        cardCvcCvv.parent().parent().$("span.input__sub").shouldBe(visible);
    }

    public static void sendFormWithBrokenData() {
        selectPurchaseWithCard();
        clearForm();
        cardNumber.setValue(getFakeCardForTest().getCardNumber());
        cardMonthExpiration.setValue(getFakeCardForTest().getCardMonthExpiration());
        cardYearExpiration.setValue(getFakeCardForTest().getCardYearExpiration());
        cardHolder.setValue(getFakeCardForTest().getCardHolder());
        cardCvcCvv.setValue(getFakeCardForTest().getCardCvcCvv());
        continueButton.click();
        assertTrue(continueButton.getText().contains("Продолжить"));

        cardNumber.parent().parent().$("span.input__sub").shouldHave(exactText("Неверный формат"));
        cardNumber.parent().parent().$("span.input__sub").shouldBe(visible);
        cardYearExpiration.parent().parent().$("span.input__sub").shouldHave(exactText("Истёк срок действия карты"));
        cardYearExpiration.parent().parent().$("span.input__sub").shouldBe(visible);
        cardHolder.parent().parent().$("span.input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
        cardHolder.parent().parent().$("span.input__sub").shouldBe(visible);
        cardCvcCvv.parent().parent().$("span.input__sub").shouldHave(exactText("Неверный формат"));
        cardCvcCvv.parent().parent().$("span.input__sub").shouldBe(visible);
    }
}
