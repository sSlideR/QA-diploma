package DataHelper.SqlUtils;

import lombok.Data;

@Data
public class CreditRequestEntity {
    private String id;
    private String bank_id;
    private String created;
    private String status;
}
