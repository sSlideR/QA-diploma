package DataHelper.SqlUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class CreditRequestEntity {
    private String id;
    private String bank_id;
    private String created;
    private String status;
}
