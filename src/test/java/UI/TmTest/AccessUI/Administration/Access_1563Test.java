package UI.TmTest.AccessUI.Administration;

import Base.TestListener;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Cookie;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Редактирование_пользователя")
@Tag("Проверка_БД")
@Tag("Основные")
public class Access_1563Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    Users users;
    public String UserId;
    public String MO;
    public String Role;
    public String Departmen;
    public String UserplaceworkID1;
    public String URL;
    public String departmentOid;
    public String placeWork;
    public String roleId;

    @Test
    @Issue(value = "TEL-1563")
    @Link(name = "ТМС-1741", url = "https://team-1okm.testit.software/projects/5/tests/1741?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Добавление места работы через API")
    @Description("Создаём нового пользователя, далее добавляем места работы этому пользователю через метод /permission/users/{userid}/placeworks и проверяем добавление в бд")
    public void Access_1563() throws SQLException, InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        users = new Users(driver);

        System.out.println("Генерация снилса для добавления пользователя");
        driver.get(GENERATE_SNILS);
        WaitElement(authorizationObject.ButtonNewNumberWait);
        authorizationObject.ButtonNewNumber.click();
        String text = authorizationObject.Snils.getText();
        System.out.println("Новый СНИЛС: " + text);

        /** Авторизация */
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(users.UsersWait);
        WaitElement(users.HeaderUsersWait);
        users.AddUser.click();
        users.InputSnils.sendKeys(text);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 15);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);
        if (KingNumber == 1) {
            AddUsersMethod(users.WorkYatskiv, users.Accounting, users.RoleUserTest213, "");
        }
        if (KingNumber == 2) {
            AddUsersMethod(users.WorkYatskiv, users.Accounting, users.RoleUserTest2, "");
        }
        if (KingNumber == 4) {
            AddUsersMethod(users.WorkYatskiv, users.Accounting, users.RoleUserTest999, "");
        }

        System.out.println("Новый пользователь добавлен в ИЭМК");
        System.out.println("Берём все Куки");
        if (KingNumber == 4) {
            Thread.sleep(3000);
        }
        Cookie Session = driver.manage().getCookieNamed(".AspNetCore.Session");
        Cookie TelemedC1 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC1");
        Cookie TelemedC2 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC2");
        Cookie Telemed = driver.manage().getCookieNamed(".AspNet.Core.Telemed");
        System.out.println("Ищем userid пользователя в таблице telmed.users");
        sql.StartConnection("Select * from telmed.users order by id desc limit 1;");
        while (sql.resultSet.next()) {
            UserId = sql.resultSet.getString("id");
            System.out.println(UserId);
        }

        System.out.println("Ищем запись в таблице telmed.userplacework");
        sql.StartConnection(
                "Select u.id, u.userid, u.placework, msm.namemu, u.quitdate, u.roleid, a.\"name\", u.departmentoid, mss.depart_name from telmed.userplacework u\n" +
                        "join dpc.mis_sp_mu msm on u.placework = msm.idmu \n" +
                        "join telmed.accessroles a on u.roleid = a.id\n" +
                        "join dpc.mo_subdivision_structure mss on u.departmentoid  = mss.depart_oid\n" +
                        "where userid = '" + UserId + "';");
        while (sql.resultSet.next()) {
            MO = sql.resultSet.getString("namemu");
            Role = sql.resultSet.getString("name");
            Departmen = sql.resultSet.getString("depart_name");
        }
        Assertions.assertEquals(
                MO, "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"", "МО не совпадает");
        if (KingNumber == 1) {
            Assertions.assertEquals(Role, "Тестовая роль 213", "Роль не совпадает");
        }
        if (KingNumber == 2) {
            Assertions.assertEquals(Role, "Тестовая роль", "Роль не совпадает");
        }
        if (KingNumber == 4) {
            Assertions.assertEquals(Role, "Тестовая 999", "Роль не совпадает");
        }
        Assertions.assertEquals(Departmen, "Бухгалтерия", "Подразделение не совпадает");
        if (KingNumber == 1) {
            URL = "https://tm-test.pkzdrav.ru";
            departmentOid = "1.2.643.5.1.13.13.12.2.10.13528.0.480047";
            placeWork = "110";
            roleId = "103";
        }
        if (KingNumber == 2) {
            URL = "https://tm-dev.pkzdrav.ru";
            departmentOid = "1.2.643.5.1.13.13.12.2.10.13528.0.480047";
            placeWork = "110";
            roleId = "103";
        }
        if (KingNumber == 4) {
            URL = "https://remotecons-test.miacugra.ru";
            departmentOid = "1.2.643.5.1.13.13.12.2.10.13528.0.480047";
            placeWork = "263";
            roleId = "103";
        }
        JsonPath response = given()
                .filter(new AllureRestAssured())
                .log().all()
                .cookie(String.valueOf(Session))
                .cookie(String.valueOf(TelemedC1))
                .cookie(String.valueOf(TelemedC2))
                .cookie(String.valueOf(Telemed))
                .contentType(ContentType.JSON)
                .when()
                .body("{\n" +
                        "  \"departmentOid\": \"" + departmentOid + "\",\n" +
                        "  \"placeWork\": " + placeWork + ",\n" +
                        "  \"roleId\": " + roleId + ",\n" +
                        "  \"positionIds\": [3]\n" +
                        "}")
                .post(URL + "/permission/users/" + UserId + "/placeworks")
                .prettyPeek()
                .jsonPath();

        System.out.println("Проверяем добавленную роль в таблице telmed.userplacework");
        sql.StartConnection(
                "Select u.id, u.userid, u.placework, msm.namemu, u.quitdate, u.roleid, a.\"name\", u.departmentoid, mss.depart_name from telmed.userplacework u\n" +
                        "join dpc.mis_sp_mu msm on u.placework = msm.idmu \n" +
                        "join telmed.accessroles a on u.roleid = a.id\n" +
                        "join dpc.mo_subdivision_structure mss on u.departmentoid  = mss.depart_oid\n" +
                        "where userid = '" + UserId + "' order by id desc limit 1;");
        while (sql.resultSet.next()) {
            MO = sql.resultSet.getString("placework");
            Role = sql.resultSet.getString("roleid");
            Departmen = sql.resultSet.getString("departmentoid");
            UserplaceworkID1 = sql.resultSet.getString("id");
        }
        if (KingNumber == 1) {
            Assertions.assertEquals(MO, "110", "МО не совпадает");
            Assertions.assertEquals(Role, "103", "Роль не совпадает");
            Assertions.assertEquals(
                    Departmen, "1.2.643.5.1.13.13.12.2.10.13528.0.480047", "Подразделение не совпадает");
        }
        if (KingNumber == 2) {
            Assertions.assertEquals(MO, "110", "МО не совпадает");
            Assertions.assertEquals(Role, "103", "Роль не совпадает");
            Assertions.assertEquals(
                    Departmen, "1.2.643.5.1.13.13.12.2.10.13528.0.480047", "Подразделение не совпадает");
        }
        if (KingNumber == 4) {
            Assertions.assertEquals(MO, "263", "МО не совпадает");
            Assertions.assertEquals(Role, "103", "Роль не совпадает");
            Assertions.assertEquals(
                    Departmen, "1.2.643.5.1.13.13.12.2.10.13528.0.480047", "Подразделение не совпадает");
        }
        System.out.println("Проверяем добавление данных в таблицу telmed.userpositions");
        sql.SQL("SELECT count(id) FROM telmed.userpositions where userplaceworkid = '" + UserplaceworkID1 + "';");

        /** Удаление созданного профиля, для возможности переиспользовать данный тест */
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(users.UsersWait);
        actionElementAndClick(users.Users);
        WaitElement(users.HeaderUsersWait);
        WaitElement(users.AddUserWait);
        users.AddUser.click();
        users.InputSnils.sendKeys(text);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 20);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);

        System.out.println("Изменение данных о месте работы");
        ClickElement(users.Edit1Wait);
        WaitElement(users.EditWait);
        SelectClickMethod(users.DateDismiss, users.DateDismissBackToday);
        users.UpdateWork.click();
        WaitNotElement(users.EditWait);
        WaitElement(users.SnilsInputWait);

        /** Увольнение со 2 работы */
        ClickElement(users.Edit2Wait);
        WaitElement(users.EditWait);
        SelectClickMethod(users.DateDismiss, users.DateDismissBackToday);
        users.UpdateWork.click();
        WaitNotElement(users.EditWait);
        WaitElement(users.SnilsInputWait);
        actionElementAndClick(users.Update);
    }
}
