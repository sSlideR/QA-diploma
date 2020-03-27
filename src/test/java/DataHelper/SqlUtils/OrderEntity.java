package DataHelper.SqlUtils;

import lombok.Data;

@Data
class OrderEntity {
    private String id;
    private String created;
    private String credit_id;
    private String payment_id;
}
