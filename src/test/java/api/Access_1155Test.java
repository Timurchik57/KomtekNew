package api;

import Base.BaseAPI;
import Base.SQL;
import Base.TestListener;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Проверка добавления записи в info без тега author в xml")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Проверка_info")
public class Access_1155Test extends BaseAPI {
    SQL sql;
    public String value1155;

    @Issue(value = "TEL-1155")
    @Link(name = "ТМС-1434", url = "https://team-1okm.testit.software/projects/5/tests/1434?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка добавления записи в info без тега author в xml для vmcl=2")
    @Description("Отправить смс, у которой в xml отсутствует тег author. После Проверить в info добавление записи и пустые строки в в столбцах authorfname,authorlname,authormname,authorsnils.")
    public void Access_1155Sms_33Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1155Method(
                "SMS/SMS33.xml", "115", 2, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms",
                "vimis.preventionadditionalinfo"
        );
    }

    public void Access_1155Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String info
    ) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем  смс c DocType " + DocType + " с vmlc=" + vmcl + "");
            xml.ApiSmd(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            System.out.println("Переходим в " + sms + "");
            sql.StartConnection(
                    "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                value1155 = sql.resultSet.getString("id");
                System.out.println(value1155);
            }
            sql.StartConnection("Select * from " + info + " where smsid = '" + value1155 + "';");
            while (sql.resultSet.next()) {
                String authorfname = sql.resultSet.getString("authorfname");
                String authorlname = sql.resultSet.getString("authorlname");
                String authormname = sql.resultSet.getString("authormname");
                String authorsnils = sql.resultSet.getString("authorsnils");
                Assertions.assertNull(authorfname, "Добавилась запись authorfname");
                Assertions.assertNull(authorlname, "Добавилась запись authorlname");
                Assertions.assertNull(authormname, "Добавилась запись authormname");
                Assertions.assertNull(authorsnils, "Добавилась запись authorsnils");
            }
        }
    }
}
