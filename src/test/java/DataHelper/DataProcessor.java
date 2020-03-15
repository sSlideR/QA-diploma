package DataHelper;

import lombok.*;

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

        public static Card getFakeCardForTest() {
            return new Card("4444 4444 4444 444", "08", "15", "", "12");
        }
    }
}
