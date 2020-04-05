package DataHelper.SqlUtils;

import lombok.Value;

@Value
public class PaymentEntity {
    private String id;
    private int amount;
    private String created;
    private String status;
    private String transaction_id;
}
