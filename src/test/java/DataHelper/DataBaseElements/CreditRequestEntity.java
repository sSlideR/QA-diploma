package DataHelper.DataBaseElements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

import static DataHelper.DataBase.getDbAuthInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequestEntity {
    private String id;
    private String bank_id;
    private String created;
    private String status;

    public static String getDbName() {
        return "credit_request_entity";
    }

    public static CreditRequestEntity getLastDbItem() throws SQLException {
        val dataBase = getDbAuthInfo();

        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(dataBase.getDBUrl(), dataBase.getDBUser(), dataBase.getDBUserPass());
        ) {
            val result = runner.query(conn, String.format("SELECT * FROM %s ORDER BY created DESC", getDbName()), new BeanHandler<>(CreditRequestEntity.class));
            return result;
        }
    }
}
