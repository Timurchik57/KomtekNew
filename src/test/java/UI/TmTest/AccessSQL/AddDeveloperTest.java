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
@Feature("Проверка добавления ИС и разработчика на контура Севастополя и Тм-тест")
public class AddDeveloperTest extends BaseAPI {
    SQL sql;
    String ShortName;
    String Name;
    Boolean isunloading;

    @Test
    @Issue(value = "TEL-853")
    @Link(name = "ТМС-1228", url = "https://team-1okm.testit.software/projects/5/tests/1228?isolatedSection=ccb1fcf9-9e3b-44d1-9bad-7959d251a43d")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка добавления ИС и разработчика на контуре Севастополя")
    @Description("Переходим в таблицы telmed.mis_developers и telmed.centralized_unloading_systems. Проверяем добавленные данные.")
    public void AddDeveloperSeva() throws SQLException {
        sql = new SQL();
        if (KingNumber == 3) {
            sql.StartConnection("SELECT * FROM telmed.mis_developers where \"name\" ='ООО «Инкордмед»';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("enable");
            }
            Assertions.assertEquals(sql.value, "1");
            System.out.println("Значение " + sql.value + " совпадает с 1");
            sql.StartConnection(
                    "SELECT c.\"name\", c.isunloading, m.\"name\" name2 FROM telmed.centralized_unloading_systems c\n" +
                            "join telmed.mis_developers m on m.id = c.developer_id \n" +
                            "where fullname='Автоматизированная информационная система «Региональный акушерский мониторинг»';");
            while (sql.resultSet.next()) {
                ShortName = sql.resultSet.getString("name");
                isunloading = Boolean.valueOf(sql.resultSet.getString("isunloading"));
                Name = sql.resultSet.getString("name2");
            }
            Assertions.assertEquals(ShortName, "АИС \"РАМ\"");
            Assertions.assertEquals(sql.value, "1");
            Assertions.assertEquals(Name, "ООО «Инкордмед»");
        }
    }

    @Test
    @Issue(value = "TEL-853")
    @Link(name = "ТМС-1228", url = "https://team-1okm.testit.software/projects/5/tests/1228?isolatedSection=ccb1fcf9-9e3b-44d1-9bad-7959d251a43d")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка добавления ИС и разработчика на контуре Тм-тест")
    @Description("Переходим в таблицы telmed.mis_developers и telmed.centralized_unloading_systems. Проверяем добавленные данные.")
    public void AddDeveloperTmTest() throws SQLException {
        sql = new SQL();
        if (KingNumber == 1) {
            sql.StartConnection("SELECT * FROM telmed.mis_developers where \"name\" ='ООО «Инкордмед»';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("enable");
            }
            Assertions.assertEquals(sql.value, "0");
            System.out.println("Значение " + sql.value + " совпадает с 0");
            sql.StartConnection(
                    "SELECT c.\"name\", c.isunloading, m.\"name\" name2 FROM telmed.centralized_unloading_systems c\n" +
                            "join telmed.mis_developers m on m.id = c.developer_id \n" +
                            "where fullname='Автоматизированная информационная система «Региональный акушерский мониторинг»';");
            while (sql.resultSet.next()) {
                ShortName = sql.resultSet.getString("name");
                isunloading = Boolean.valueOf(sql.resultSet.getString("isunloading"));
                Name = sql.resultSet.getString("name2");
            }
            Assertions.assertEquals(ShortName, "АИС \"РАМ\"");
            Assertions.assertEquals(sql.value, "0");
            Assertions.assertEquals(Name, "ООО «Инкордмед»");
        }
    }
}
