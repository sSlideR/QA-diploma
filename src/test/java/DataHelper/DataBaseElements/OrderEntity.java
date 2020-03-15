package DataHelper.DataBaseElements;

import lombok.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

import static DataHelper.DataBase.getDbAuthInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    private String id;
    private String created;
    private String credit_id;
    private String payment_id;

    public static String getDbName() {
        return "order_entity";
    }

    public static OrderEntity getLastDbItem() throws SQLException {
        val dataBase = getDbAuthInfo();

        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(dataBase.getDBUrl(), dataBase.getDBUser(), dataBase.getDBUserPass());
        ) {
            val result = runner.query(conn, String.format("SELECT * FROM %s ORDER BY created DESC", getDbName()), new BeanHandler<>(OrderEntity.class));
            return result;
        }
    }


}
