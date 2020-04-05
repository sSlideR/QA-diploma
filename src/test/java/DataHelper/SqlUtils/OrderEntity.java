package DataHelper.SqlUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderEntity {
    private String id;
    private String created;
    private String credit_id;
    private String payment_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(created, that.created) &&
                Objects.equals(credit_id, that.credit_id) &&
                Objects.equals(payment_id, that.payment_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created, credit_id, payment_id);
    }
}
