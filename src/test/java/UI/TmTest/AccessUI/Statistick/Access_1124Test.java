package UI.TmTest.AccessUI.Statistick;

import Base.*;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.RoutesTask;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListenerChange.class)
@Epic("Тесты UI")
@Feature("Статистика")
@Tag("Задания")
@Tag("Основные")
public class Access_1124Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    SQL sql;
    RoutesTask routesTask;
    public Integer Quantity;
    public String mo_recieve;
    public String nosological_patient_id;
    boolean Author;

    @Test
    @Issue(value = "TEL-1124")
    @Link(name = "ТМС-1438", url = "https://team-1okm.testit.software/projects/5/tests/1438?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Метод получения информации о входящих/исходящих задачах по пациентам")
    @Description("Переходим в {hostaddres}/stats/routes-tasks, добавляем новое задание в vimis.routes_tasks, изменяем и проверяем отображение на ui")
    public void Access_1124 () throws SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        sql = new SQL();
        routesTask = new RoutesTask(driver);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        Thread.sleep(2000);
        driver.get(HostAddressWeb + "/stats/routes-tasks");
        WaitElement(routesTask.RoutesTaskHeader);
        WaitElement(routesTask.Incoming);
        WaitElement(routesTask.Outgoing);

        System.out.println("Сверяем количество заданий во входящие");
        QuantityRoutesMethod("Входящие");

        System.out.println("Сверяем количество заданий в исходящие");
        QuantityRoutesMethod("Исходящие");

        System.out.println("Создаём задание в МО под которой авторизованы на врача отличного от авторизованного");
        if (KingNumber == 1) {
            nosological_patient_id = "149";
        }
        if (KingNumber == 2) {
            nosological_patient_id = "344061";
        }
        if (KingNumber == 4) {
            nosological_patient_id = "1660";
        }

        // Создадим сначала через Автора 1 = авторизации, а после автор 2 != авторизации
        System.out.println("1 проверка - doc_recieve = авторизация, МО = авторизация");
        sql.UpdateConnection(
                "insert into vimis.routes_tasks (author_id, task_header, task, create_date, estimated_end_date, mo_recieve, doc_recieve, nosological_patient_id) values ('" + PId + "', 'Проверяем заявку 1124 -------------------------------------------------------------', 'Проверяем заявку 1124 -------------------------------------------------------------', '" + Date + " 00:00:10.234', '" + Date + " 16:34:10.234', '" + PIdMoRequest + "', '" + PId + "', '" + nosological_patient_id + "');");
        Thread.sleep(1500);

        sql.StartConnection(
                "Select id from vimis.routes_tasks where task_header = 'Проверяем заявку 1124 -------------------------------------------------------------';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }

        System.out.println("Проверяем задание в Входящие - Персональные задания");
        driver.navigate().refresh();
        WaitElement(routesTask.Incoming2Add);

        System.out.println("2 проверка - doc_recieve = авторизация, МО != авторизация");
        SetSql(false, PId, "1");

        System.out.println("3 проверка - doc_recieve != авторизация, МО = авторизация, статус = 1");
        SetSql(true, "25", "1");

        System.out.println("4 проверка - doc_recieve != авторизация, МО != авторизация, статус = 1");
        SetSql(false, "25", "1");

        System.out.println("5 проверка - doc_recieve = авторизация, МО = авторизация, статус = 3");
        SetSql(true, PId, "3");

        System.out.println("6 проверка - doc_recieve != авторизация, МО = авторизация, статус = 3");
        SetSql(true, "25", "3");

        System.out.println("7 проверка - doc_recieve = авторизация, МО != авторизация, статус = 3");
        SetSql(false, PId, "3");

        System.out.println("8 проверка - doc_recieve != авторизация, МО != авторизация, статус = 3");
        SetSql(false, "25", "3");

        System.out.println("9 проверка - doc_recieve = null, МО = авторизация, статус = 1");
        SetSql(true, null, "1");

        System.out.println("10 проверка - doc_recieve = null, МО != авторизация, статус = 1");
        SetSql(false, null, "1");

        System.out.println("11 проверка - doc_recieve = null, МО = авторизация, статус = 3");
        SetSql(true, null, "3");

        System.out.println("12 проверка - doc_recieve = null, МО != авторизация, статус = 3");
        SetSql(false, null, "3");

        System.out.println("Проверяем отображение полного текста при наведении на заголовок");
        actions.moveToElement(driver.findElement(routesTask.Outgoing1Add));
        actions.perform();
        WaitElement(routesTask.Tooltip);

        System.out.println("Проверяем скролл текста задания");
        String Scroll = driver.findElement(routesTask.Outgoing1AddScroll).getCssValue("overflow");
        Assertions.assertEquals(Scroll, "scroll", "Нет скролла у текста задания");

        System.out.println(
                "Меняем автора != авторизации doc_recieve = авторизация, МО = авторизация, статус = 1 и проверяем повторно все кейсы");
        Author = true;
        sql.UpdateConnection(
                "update vimis.routes_tasks set author_id = '25', mo_recieve = '" + PIdMoRequest + "',  doc_recieve = '" + PId + "', status = '1' where id = '" + sql.value + "';");

        System.out.println("Проверяем задание в Входящие - Персональные задания");
        driver.navigate().refresh();
        WaitElement(routesTask.Incoming2Add);

        System.out.println("13 проверка - doc_recieve = авторизация, МО != авторизация");
        SetSql(false, PId, "1");

        System.out.println("14 проверка - doc_recieve != авторизация, МО = авторизация, статус = 1");
        SetSql(true, "25", "1");

        System.out.println("15 проверка - doc_recieve != авторизация, МО != авторизация, статус = 1");
        SetSql(false, "25", "1");

        System.out.println("16 проверка - doc_recieve = авторизация, МО = авторизация, статус = 3");
        SetSql(true, PId, "3");

        System.out.println("17 проверка - doc_recieve != авторизация, МО = авторизация, статус = 3");
        SetSql(true, "25", "3");

        System.out.println("18 проверка - doc_recieve = авторизация, МО != авторизация, статус = 3");
        SetSql(false, PId, "3");

        System.out.println("19 проверка - doc_recieve != авторизация, МО != авторизация, статус = 3");
        SetSql(false, "25", "3");

        System.out.println("20 проверка - doc_recieve = null, МО = авторизация, статус = 1");
        SetSql(true, null, "1");

        System.out.println("21 проверка - doc_recieve = null, МО != авторизация, статус = 1");
        SetSql(false, null, "1");

        System.out.println("22 проверка - doc_recieve = null, МО = авторизация, статус = 3");
        SetSql(true, null, "3");

        System.out.println("23 проверка - doc_recieve = null, МО != авторизация, статус = 3");
        SetSql(false, null, "3");

        sql.UpdateConnection("delete from vimis.routes_tasks where id = '" + sql.value + "';");
    }

    @Step("Метод меняющий данные в задании")
    public void SetSql (boolean mo, String doc, String status) throws SQLException, InterruptedException {
        if (mo) {
            mo_recieve = PIdMoRequest;
        } else {
            if (KingNumber == 1 || KingNumber == 2) {
                mo_recieve = "176";
            }
            if (KingNumber == 4) {
                mo_recieve = "79";
            }
        }

        if (doc != null) {
            sql.UpdateConnection(
                    "update vimis.routes_tasks set mo_recieve = '" + mo_recieve + "',  doc_recieve = '" + doc + "', status = '" + status + "' where id = '" + sql.value + "';");
        } else {
            sql.UpdateConnection(
                    "update vimis.routes_tasks set mo_recieve = '" + mo_recieve + "',  doc_recieve = null, status = '" + status + "' where id = '" + sql.value + "';");
        }
        Thread.sleep(1500);
        driver.navigate().refresh();
        Thread.sleep(1500);
        if (mo & Objects.equals(doc, PId) & status.equals("1") |
                !mo & Objects.equals(doc, PId) & status.equals("1")) {
            // Входящие - персональные задания
            WaitElement(routesTask.Incoming2Add);
        }

        if (mo & !Objects.equals(doc, PId) & status.equals("1") |
                mo & doc == null & status.equals("1")) {
            // Входящие - Всего заданий
            WaitElement(routesTask.Incoming1Add);
        }

        if (mo & Objects.equals(doc, PId) & status.equals("3") |
                mo & !Objects.equals(doc, PId) & status.equals("3") |
                !mo & Objects.equals(doc, PId) & status.equals("3") |
                mo & doc == null & status.equals("3")) {
            // Входящие - Выполнено
            WaitElement(routesTask.Incoming3Add);
        }

        if (!mo & !Objects.equals(doc, PId) & status.equals("1") |
                !mo & !Objects.equals(doc, PId) & status.equals("3") |
                !mo & doc == null & status.equals("1") |
                !mo & doc == null & status.equals("3")) {
            if (Author) {
                // Не отображается
                WaitNotElement3(routesTask.RoutsName(
                        "Проверяем заявку 1124 -------------------------------------------------------------"), 2);
            } else {
                // Исходящие - всего заданий
                WaitElement(routesTask.Outgoing1Add);
            }
        }
    }

    /**
     Подсчет количества заданий во всех блоках
     */
    @Step("Метод подсчета количества заданий в блоке {0}")
    public void QuantityRoutesMethod (String Name) throws SQLException, InterruptedException {
        Thread.sleep(2500);
        if (Name == "Входящие") {
            for (int i = 1; i < 4; i++) {
                List<WebElement> RoutWeb = driver.findElements(
                        By.xpath("//p[contains(.,'" + Name + "')]/following-sibling::div/div[" + i + "]/div"));
                if (RoutWeb.size() > 1) {
                    if (i == 1) {
                        sql.StartConnection("select count(r.id) from vimis.routes_tasks r\n" +
                                "left join dpc.mis_sp_mu msm on r.mo_recieve = msm.idmu \n" +
                                "left join telmed.users u on r.doc_recieve = u.id \n" +
                                "where msm.namemu = 'БУ ХМАО-Югры \"Окружная клиническая больница\"' and r.status != '3' and (r.doc_recieve != '"+PId+"' or r.doc_recieve is null);");
                        while (sql.resultSet.next()) {
                            Quantity = Integer.valueOf(sql.resultSet.getString("count"));
                        }

                        Assertions.assertEquals(
                                RoutWeb.size() - 1, Quantity,
                                "Количество заданий  в " + Name + " - Всего заданий не совпадает "
                        );
                        System.out.println(RoutWeb.size() - 1);
                    }

                    if (i == 2) {
                        sql.StartConnection("select count(r.id) from vimis.routes_tasks r\n" +
                                "left join dpc.mis_sp_mu msm on r.mo_recieve = msm.idmu \n" +
                                "left join telmed.users u on r.doc_recieve = u.id \n" +
                                "where u.sname = '" + PLastNameGlobal + "' and r.status != '3';");
                        while (sql.resultSet.next()) {
                            Quantity = Integer.valueOf(sql.resultSet.getString("count"));
                        }
                        Assertions.assertEquals(
                                RoutWeb.size() - 1, Quantity,
                                "Количество заданий  в " + Name + " - Персональные задания не совпадает "
                        );
                        System.out.println(RoutWeb.size() - 1);
                    }

                    if (i == 3) {
                        sql.StartConnection("select count(r.id) from vimis.routes_tasks r\n" +
                                "left join dpc.mis_sp_mu msm on r.mo_recieve = msm.idmu \n" +
                                "left join telmed.users u on r.doc_recieve = u.id \n" +
                                "where msm.namemu != 'БУ ХМАО-Югры \"Окружная клиническая больница\"' and r.status = '3' and u.sname = '" + PLastNameGlobal + "' \n" +
                                "or msm.namemu = 'БУ ХМАО-Югры \"Окружная клиническая больница\"' and r.status = '3';");
                        while (sql.resultSet.next()) {
                            Quantity = Integer.valueOf(sql.resultSet.getString("count"));
                        }
                        Assertions.assertEquals(
                                RoutWeb.size() - 1, Quantity,
                                "Количество заданий  в " + Name + " - Выполнено не совпадает "
                        );
                        System.out.println(RoutWeb.size() - 1);
                    }
                } else {
                    if (i == 1) {
                        System.out.println("Нет заданий в блоке - Всего заданий");
                    }
                    if (i == 2) {
                        System.out.println("Нет заданий в блоке - Персональные задания");
                    }
                    if (i == 3) {
                        System.out.println("Нет заданий в блоке - Выполнено");
                    }
                }
            }
        }
        if (Name == "Исходящие") {
            List<WebElement> RoutWeb = driver.findElements(
                    By.xpath("//p[contains(.,'" + Name + "')]/following-sibling::div/div[1]/div"));
            if (RoutWeb.size() > 1) {
                sql.StartConnection("select count(r.id) from vimis.routes_tasks r\n" +
                        "left join dpc.mis_sp_mu msm on r.mo_recieve = msm.idmu \n" +
                        "left join telmed.users u on r.doc_recieve = u.id \n" +
                        "where msm.namemu != 'БУ ХМАО-Югры \"Окружная клиническая больница\"' and u.sname != '" + PLastNameGlobal + "' or msm.namemu != 'БУ ХМАО-Югры \"Окружная клиническая больница\"' and r.doc_recieve is null;");
                while (sql.resultSet.next()) {
                    Quantity = Integer.valueOf(sql.resultSet.getString("count"));
                }
                Assertions.assertEquals(
                        RoutWeb.size() - 1, Quantity, "Количество заданий  в " + Name + " не совпадает ");
                System.out.println(RoutWeb.size() - 1);
            } else {
                System.out.println("Нет заданий в блоке - " + Name + "");
            }
        }
    }
}
