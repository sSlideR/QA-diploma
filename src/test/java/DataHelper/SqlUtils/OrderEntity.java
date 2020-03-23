package DataHelper.SqlUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderEntity {
    private String id;
    private String created;
    private String credit_id;
    private String payment_id;
}
