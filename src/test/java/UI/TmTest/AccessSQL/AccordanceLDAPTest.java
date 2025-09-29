package UI.TmTest.AccessSQL;

import Base.SQL;
import Base.TestListener;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Feature("Проверка соответствия с LDAP Тест-Севы")
public class AccordanceLDAPTest extends SQL {
    SQL sql;

    @Test
    @Issue(value = "TEL-725")
    @Link(name = "ТМС-1198", url = "https://team-1okm.testit.software/projects/5/tests/1198?isolatedSection=ccb1fcf9-9e3b-44d1-9bad-7959d251a43d")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка соответствия с LDAP Тест-Севы")
    public void AccordanceLDAP() throws SQLException {
        sql = new SQL();
        if (KingNumber == 3) {
            sql.StartConnection("SELECT * FROM telmed.sp_mu sm  where idmu  ='6829293';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("adgroup");
            }
            Assertions.assertEquals(sql.value, "GBUZS SGBSME");
            System.out.println("Значение " + sql.value + " совпадает с GBUZS SGBSME");
        }
    }

    @Test
    @Issue(value = "TEL-725")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка соответствия с LDAP Тест-Севы")
    public void AccordanceLDAP1() throws SQLException {
        sql = new SQL();
        if (KingNumber == 3) {
            sql.StartConnection("SELECT * FROM telmed.sp_mu sm  where idmu  ='9188';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("adgroup");
                Assertions.assertNotEquals("0", sql.value);
            }
            Assertions.assertEquals(sql.value, "GBUZS KVD");
            System.out.println("Значение " + sql.value + " совпадает с GBUZS KVD");
        }
    }
}
