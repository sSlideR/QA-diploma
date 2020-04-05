package DataHelper.SqlUtils;

import lombok.Value;

@Value
public class CreditRequestEntity {
    private String id;
    private String bank_id;
    private String created;
    private String status;
}
