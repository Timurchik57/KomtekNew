package UI.TmTest.AccessSQL;

import Base.BaseAPI;
import Base.SQL;
import Base.TestListener;
import Base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Feature("Проверка добавления МО на контур Севастополя")
public class AddMOTest extends BaseAPI {
    SQL sql = new SQL();
    String oid;
    String NameShort;

    @Test
    @Issue(value = "TEL-851")
    @Link(name = "ТМС-1228", url = "https://team-1okm.testit.software/projects/5/tests/1228?isolatedSection=ccb1fcf9-9e3b-44d1-9bad-7959d251a43d")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка добавления МО на контур Севастополя")
    @Description("Переходим в таблицы dpc.mis_sp_mu и dpc.reestr_mo Проверяем добавленные данные.")
    public void AddDeveloperSeva() throws SQLException {
        if (KingNumber == 3) {
            sql.StartConnection("select * from dpc.reestr_mo rm \n" +
                                        "join dpc.mis_sp_mu ms on ms.oid = rm.oid \n" +
                                        "where rm.id = '6829263';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                oid = sql.resultSet.getString("oid");
                NameShort = sql.resultSet.getString("nameshort");
            }
            Assertions.assertEquals(sql.value, "6829263");
            Assertions.assertEquals(oid, "1.2.643.5.1.13.13.12.2.92.9193");
            Assertions.assertEquals(NameShort, "ГБУЗС \"СЕВАСТОПОЛЬСКАЯ ГОРОДСКАЯ ПСИХИАТРИЧЕСКАЯ БОЛЬНИЦА\"");
            System.out.println("Значение " + oid + " совпадает с 1.2.643.5.1.13.13.12.2.92.9193");
            sql.StartConnection("select * from dpc.mis_sp_mu where oid = '1.2.643.5.1.13.13.12.2.92.9193';");
            while (sql.resultSet.next()) {
                oid = sql.resultSet.getString("oid");
                NameShort = sql.resultSet.getString("namemu");
            }
            Assertions.assertEquals(oid, "1.2.643.5.1.13.13.12.2.92.9193");
            Assertions.assertEquals(NameShort, "ГБУЗС \"СЕВАСТОПОЛЬСКАЯ ГОРОДСКАЯ ПСИХИАТРИЧЕСКАЯ БОЛЬНИЦА\"");
            System.out.println("Значение " + oid + " совпадает с 1.2.643.5.1.13.13.12.2.92.9193");
        }
    }
}
