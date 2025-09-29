package UI.TmTest.AccessUI.Regisrty;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import Base.TestListenerChange;
import UI.TmTest.AccessUI.Administration.Access_1898Test;
import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Registry.RegisterDispensaryPatients;
import api.Before.Authorization;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerChange.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Регистры")
@Tag("Кластеры")
public class Access_3825Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    Access_1898Test access1898Test;
    TypeRegistr typeRegistr;
    RegisterDispensaryPatients registerDispensaryPatients;
    Authorization authorization;

    /**
     1 - Центральный Кластер
     2 - Восточный Кластер
     3 - Западный Кластер
     */

    @Issue(value = "TEL-3825")
    @Issue(value = "TEL-3826")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка кластеров в Регистре Акинео")
    @Description("Переходим в Регистр Акинео и проверяем, что Кластеры отображаются в таблице")
    public void Access_3965 () throws InterruptedException, SQLException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        access1898Test = new Access_1898Test();
        typeRegistr = new TypeRegistr(driver);
        registerDispensaryPatients = new RegisterDispensaryPatients(driver);

        System.out.println("Редактирование роли - Добавляем все 6 доступа по СЭМД");
        AddRole(PRole, "Доступ к разделу \"Нозологические регистры\" на СЭМД", true);
        AddRole(PRole, "Доступ к разделу \"Нозологические регистры\" по любой МО", true);
        AddRole(PRole, "ВИМИС \"Онкология\"", true);
        AddRole(PRole, "ВИМИС \"ССЗ\"", true);
        AddRole(PRole, "ВИМИС \"АкиНео\"", true);
        AddRole(PRole, "ВИМИС \"Профилактика\"", true);
        AddRole(PRole, "ВИМИС \"Инфекционные заболевания\"", true);
        AddRole(PRole, "ВИМИС \"Иные профили\"", true);

        /** Узнаем отображаются ли Кластеры */
        if (GetSettings().get("result.isClustersInRegisters").equals(true)) {

            System.out.println("Авторизуемся");
            AuthorizationMethod(authorizationObject.OKB);
            ClickElement(typeRegistr.TypeRegistrWait);

            System.out.println("Проверяем создан ли нужный регистр");
            access1898Test.Access_1898Method("Регистр Акинео", typeRegistr.SelectSourceDataAkineo);

            ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
            ClickElement(registerDispensaryPatients.RegistrSelect2("Регистр Акинео"));

            WaitNotElement3(authorizationObject.LoadingTrue("2"), 60);
            Thread.sleep(1500);
            GetSql("select count(*) from vimis.akineo_sms_v5_register;", false);

            System.out.println("1 проверка - Колонка кластер отображается");
            WaitElement(registerDispensaryPatients.RegisterColumn("Кластер"));

            System.out.println("2 проверка - Фильтруем по Центральный кластер");
            ClickElement(registerDispensaryPatients.FiltersWait);
            ClickElement(registerDispensaryPatients.FiltersSelect("Кластер"));
            ClickElement(authorizationObject.Select("Центральный кластер"));
            ClickElement(registerDispensaryPatients.SearchWait);
            Thread.sleep(1500);
            GetSql("select count(*) from vimis.akineo_sms_v5_register v\n" +
                            "join vimis.akineosms a on v.sms_id = a.id \n" +
                            "join dpc.clustermo c on a.medicalidmu = c.medicalidmu\n" +
                            "where c.idcluster = '1';",
                    true);

            System.out.println("3 проверка - Фильтруем по Восточный Кластер");
            ClickElement(registerDispensaryPatients.FiltersWait);
            ClickElement(registerDispensaryPatients.FiltersSelect("Кластер"));
            ClickElement(authorizationObject.Select("Восточный кластер"));
            ClickElement(registerDispensaryPatients.SearchWait);
            Thread.sleep(1500);
            GetSql("select count(*) from vimis.akineo_sms_v5_register v\n" +
                            "join vimis.akineosms a on v.sms_id = a.id \n" +
                            "join dpc.clustermo c on a.medicalidmu = c.medicalidmu\n" +
                            "where c.idcluster = '2';",
                    true);

            System.out.println("4 проверка - Фильтруем по Западный Кластер");
            ClickElement(registerDispensaryPatients.FiltersWait);
            ClickElement(registerDispensaryPatients.FiltersSelect("Кластер"));
            ClickElement(authorizationObject.Select("Западный кластер"));
            ClickElement(registerDispensaryPatients.SearchWait);
            Thread.sleep(1500);
            GetSql("select count(*) from vimis.akineo_sms_v5_register v\n" +
                            "join vimis.akineosms a on v.sms_id = a.id \n" +
                            "join dpc.clustermo c on a.medicalidmu = c.medicalidmu\n" +
                            "where c.idcluster = '3';",
                    true);

        } else {
            System.out.println("Кластеры не отображаются");
            System.out.println("Авторизуемся");
            AuthorizationMethod(authorizationObject.OKB);
            ClickElement(typeRegistr.TypeRegistrWait);

            System.out.println("Проверяем создан ли нужный регистр");
            access1898Test.Access_1898Method("Регистр Акинео", typeRegistr.SelectSourceDataAkineo);

            ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
            ClickElement(registerDispensaryPatients.RegistrSelect2("Регистр Акинео"));
            WaitElement(registerDispensaryPatients.CountList);
            WaitNotElement3(registerDispensaryPatients.RegisterColumn("Кластер"), 5);
        }

    }

    @Step("Метод для сравнения количества отображаемых регистров")
    public void GetSql (String SQL, boolean filter) throws SQLException {
        Integer count = 0;
        String clusters = null;
        if (SQL.substring(SQL.length() - 3).substring(0, 1).equals("1")) {
            clusters = "Центральный Кластер";
        }
        if (SQL.substring(SQL.length() - 3).substring(0, 1).equals("2")) {
            clusters = "Восточный Кластер";
        }
        if (SQL.substring(SQL.length() - 3).substring(0, 1).equals("3")) {
            clusters = "Западный Кластер";
        }

        WaitElement(registerDispensaryPatients.CountList);
        String list = driver.findElement(registerDispensaryPatients.CountList).getText().substring(9);

        sql.StartConnection(SQL);
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        Assertions.assertEquals(list, sql.value,
                "Без фильтров, количество записей должно соответствовать числу из vimis.akineo_sms_v5_register");

        if (Integer.valueOf(sql.value) < 20) {
            count = Integer.valueOf(sql.value);
        } else {
            count = 20;
        }

        if (filter) {
            for (int i = 1; i < count + 1; i++) {
                // Берём отображаемую МО
                String mo = driver.findElement(By.xpath("//tbody/tr[" + i + "]/td[4]//span")).getText();
                sql.StartConnection("select \n" +
                        "case \n" +
                        "\twhen idcluster = '1' then 'Центральный Кластер'\n" +
                        "\twhen idcluster = '2' then 'Восточный Кластер'\n" +
                        "\twhen idcluster = '3' then 'Западный Кластер'\n" +
                        "end as clusterName, *\n" +
                        "from dpc.clustermo c \n" +
                        "join dpc.mis_sp_mu msm on msm.medicalidmu = c.medicalidmu \n" +
                        "where msm.namemu = '" + mo + "';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("clustername");
                }
                Assertions.assertEquals(sql.value, clusters,
                        "Для мо - " + mo + " должен отображаться кластер - " + clusters);
            }
        }
    }
}
