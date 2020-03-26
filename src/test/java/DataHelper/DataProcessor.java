package DataHelper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DataProcessor {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Card {
        private String cardNumber;
        private String cardMonthExpiration;
        private String cardYearExpiration;
        private String cardHolder;
        private String cardCvcCvv;

        public static Card getValidCardForTest() {
            return new Card("4444 4444 4444 4441", "08", "21", "Stepan Ivanov", "123");
        }

        public static Card getInvalidCardForTest() {
            return new Card("4444 4444 4444 4442", "10", "22", "Stepan Ivanov", "321");
        }

        class CardFaker {
            public Card getCardWithShortenedCardNumber() {
                return new Card("4444 4444 4444 444", "", "", "", "");
            }

            public Card getCardWithExpiredDates() {
                return new Card("", "10", "10", "", "");
            }

            public Card getCardWithZeroDates() {
                return new Card("", "00", "00", "", "");
            }

            public Card getCardWithShortenedCvcCvv() {
                return new Card("", "", "", "", "12");
            }
        }
    }
}
