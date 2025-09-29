package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Directions.Kvots.IncomingArchive;
import UI.TmTest.PageObject.Directions.Kvots.IncomingUnfinished;
import api.Before.Authorization;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Консультация_на_оборудование")
@Tag("Api_Направление_на_диагностику")
@Tag("Интерпретация")
@Tag("Проверка_БД")
@Tag("Основные")
public class Access_4046Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    IncomingUnfinished incomingUnfinished;
    IncomingArchive incomingArchive;
    Authorization authorization;

    @Issue(value = "TEL-3883")
    @Issue(value = "TEL-4015")
    @Issue(value = "TEL-4016")
    @Issue(value = "TEL-4021")
    @Issue(value = "TEL-4022")
    @Issue(value = "TEL-4023")
    @Issue(value = "TEL-4024")
    @Issue(value = "TEL-4054")
    @Issue(value = "TEL-4060")
    @Issue(value = "TEL-4061")
    @Issue(value = "TEL-4062")
    @Issue(value = "TEL-4066")
    @Issue(value = "TEL-4071")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Link(name = "ТМС-2269", url = "https://team-1okm.testit.software/projects/5/tests/2269?isolatedSection=d72e058a-4718-4994-b342-ea7fad3fa259")
    @Story("Интерпретация")
    @DisplayName("Отправка направления на интерпретацию - статус 26")
    @Description("Создаём направление, отправляем на интерпетацию в разные МО, проверяем новый доступ, который регулирует отправку")
    public void Access_4046 () throws InterruptedException, SQLException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        authorization = new Authorization();

        AddRole(PRole, "Возможность перенаправлять направления на описание в другое МО", true);
        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        xml.changes.put("$.TargetMedicalOid", POidMoTarget);
        xml.changes.put("$.ResearchCode", "HMP01");
        xml.changes.put("$.PatientGuid", PatientGuid);
        String modifiedJson = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, modifiedJson, 200, false);
        String id = Response.getString("Result.DirectionId");

        System.out.println(
                "1 проверка - проверяем из МО откуда отправили отображение кнопок при всех валидных статусах");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.SortDesc);
        ClickElement(directionsForQuotas.SearchID(id));
        // Статус 6
        incomingUnfinished.WaitButton(new String[]{"Отменить направление", "Отправить на интерпретацию"});
        SqlUpdate(id, "8", true, true);
        SqlUpdate(id, "14", true, true);
        SqlUpdate(id, "15", true, true);
        SqlUpdate(id, "28", true, true);
        SqlUpdate(id, "30", true, true);
        SqlUpdate(id, "6", true, true);

        directionsForQuotas.AddFile("БУ ХМАО-Югры \"Окружная клиническая больница\"", id, PNameEquipment2);

        System.out.println("2 проверка - проверяем в МО куда отправили отображение кнопок при всех валидных статусах");
        AuthorizationMethod(authorizationObject.Select(PMOTarget));
        ClickElement(incomingUnfinished.ConsultationWait);
        ClickElement(incomingUnfinished.DESK);
        ClickElement(incomingUnfinished.SearchID(id));
        // Статус 3
        incomingUnfinished.WaitButton(
                new String[]{"Отклонить направление", "Согласовать", "Отправить на интерпретацию"});
        SqlUpdate(id, "8", false, true);
        SqlUpdate(id, "14", false, true);
        SqlUpdate(id, "15", false, true);
        SqlUpdate(id, "28", false, true);
        SqlUpdate(id, "30", false, true);

        System.out.println(
                "3 проверка - проверяем, что Отправить на интерпретацию не отображается если выключить доступ");
        AddRole(PRole, "Возможность перенаправлять направления на описание в другое МО", false);
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + id);
        SqlUpdate(id, "6", true, false);
        SqlUpdate(id, "8", true, false);
        SqlUpdate(id, "14", true, false);
        SqlUpdate(id, "15", true, false);
        SqlUpdate(id, "28", true, false);
        SqlUpdate(id, "30", true, false);
        SqlUpdate(id, "3", true, false);

        AuthorizationMethod(authorizationObject.Select(PMOTarget));
        WaitElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + id);
        SqlUpdate(id, "8", false, false);
        SqlUpdate(id, "14", false, false);
        SqlUpdate(id, "15", false, false);
        SqlUpdate(id, "28", false, false);
        SqlUpdate(id, "30", false, false);
        SqlUpdate(id, "3", false, false);

        System.out.println("4 проверка - отправляем на Интерпретацию в МО1");
        AddRole(PRole, "Возможность перенаправлять направления на описание в другое МО", true);
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + id);
        incomingUnfinished.WaitButton(new String[]{"Отменить направление", "Отправить на интерпретацию"});
        ClickElement(directionsForQuotas.Button("Отправить на интерпретацию"));
        ClickElement(authorizationObject.Selected);
        ClickElement(authorizationObject.Select("БУ ХМАО-Югры \"Окружная клиническая больница\""));
        ClickElement(directionsForQuotas.Send);
        incomingUnfinished.WaitButton(new String[]{"Отменить направление", "Выполнить исследование"});
        SqlWait(id, "БУ ХМАО-Югры \"Окружная клиническая больница\"");

        System.out.println("5 проверка - отправляем на Интерпретацию в МО2");
        xml.changes.put("$.TargetMedicalOid", POidMoTarget);
        xml.changes.put("$.ResearchCode", "HMP01");
        xml.changes.put("$.PatientGuid", PatientGuid);
        modifiedJson = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, modifiedJson, 200, false);
        id = Response.getString("Result.DirectionId");

        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + id);
        incomingUnfinished.WaitButton(new String[]{"Отменить направление", "Отправить на интерпретацию"});
        ClickElement(directionsForQuotas.Button("Отправить на интерпретацию"));
        ClickElement(authorizationObject.Selected);
        ClickElement(authorizationObject.Select(PMOTarget));
        ClickElement(directionsForQuotas.Send);
        incomingUnfinished.WaitButton(new String[]{"Отменить направление"});
        SqlWait(id, PMOTarget);

        AuthorizationMethod(authorizationObject.Select(PMOTarget));
        WaitElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + id);
        incomingUnfinished.WaitButton(new String[]{"Отклонить направление", "Согласовать"});

        System.out.println("6 проверка - отправляем на Интерпретацию в МО2");
        xml.changes.put("$.TargetMedicalOid", POidMoTarget);
        xml.changes.put("$.ResearchCode", "HMP01");
        xml.changes.put("$.PatientGuid", PatientGuid);
        modifiedJson = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, modifiedJson, 200, false);
        id = Response.getString("Result.DirectionId");

        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + id);
        incomingUnfinished.WaitButton(new String[]{"Отменить направление", "Отправить на интерпретацию"});
        ClickElement(directionsForQuotas.Button("Отправить на интерпретацию"));
        ClickElement(authorizationObject.Selected);
        ClickElement(authorizationObject.Select("БУ ХМАО-Югры \"Белоярская районная больница\""));
        ClickElement(directionsForQuotas.Send);
        incomingUnfinished.WaitButton(new String[]{"Отменить направление"});
        SqlWait(id, "БУ ХМАО-Югры \"Белоярская районная больница\"");

        AuthorizationMethod(authorizationObject.Select("БУ ХМАО-Югры \"Белоярская районная больница\""));
        ClickElement(incomingUnfinished.ConsultationWait);
        ClickElement(incomingUnfinished.DESK);
        ClickElement(incomingUnfinished.SearchID(id));
        incomingUnfinished.WaitButton(new String[]{"Отклонить направление", "Согласовать"});
    }

    @Issue(value = "TEL-4025")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Link(name = "ТМС-2269", url = "https://team-1okm.testit.software/projects/5/tests/2269?isolatedSection=d72e058a-4718-4994-b342-ea7fad3fa259")
    @Story("Интерпретация")
    @DisplayName("Проверка отображения кнопопк при статусе 26")
    @Description("Создаём направление, отправляем на интерпетацию в разные МО, переходим в эти Мо и смотрим какие кнопки отображаются")
    public void Access_4025 () throws InterruptedException, SQLException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        authorization = new Authorization();
        incomingArchive = new IncomingArchive(driver);

        AddRole(PRole, "Доступ ко всем направлениям на квоты", false);
        AddRole(PRole, "Возможность перенаправлять направления на описание в другое МО", true);
        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        xml.changes.put("$.TargetMedicalOid", POidMoTarget);
        xml.changes.put("$.ResearchCode", "HMP01");
        xml.changes.put("$.PatientGuid", PatientGuid);
        String modifiedJson = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, modifiedJson, 200, false);
        String id = Response.getString("Result.DirectionId");

        // requestMO - targetMO - referringMO - AuthoMO
        System.out.println("1 проверка - МО1 - МО2 - МО2 - МО1");
        AuthorisationAdd(true, "", false, id);
        // Статус 6
        incomingUnfinished.WaitButton(new String[]{"Отменить направление", "Отправить на интерпретацию"});
        ClickElement(directionsForQuotas.Button("Отправить на интерпретацию"));
        ClickElement(authorizationObject.Selected);
        ClickElement(authorizationObject.Select(PMOTarget));
        ClickElement(directionsForQuotas.Send);
        Thread.sleep(1500);
        incomingUnfinished.WaitButton(new String[]{"Отменить направление"});

        System.out.println("2 проверка - МО1 - МО2 - МО2 - МО2 (В Архивные не отображается)");
        AuthorizationMethod(authorizationObject.Select(PMOTarget));
        ClickElement(incomingArchive.IncomingArchiveWait);
        ClickElement(incomingArchive.DESK);
        Thread.sleep(2500);
        WaitElement(incomingArchive.FirstLineMO);
        WaitNotElement(incomingArchive.SearchID(id));

        System.out.println("3 проверка - МО1 - МО2 - МО2 - МО2");
        AuthorisationAdd(false, PMOTarget, true, id);
        incomingUnfinished.WaitButton(new String[]{"Отклонить направление", "Согласовать"});

        System.out.println("4 проверка - МО1 - МО2 - МО1 - МО1");
        sql.UpdateConnection("Update telmed.directions set referringmoid = '12' where id = '" + id + "';");
        AuthorisationAdd(true, "", false, id);
        incomingUnfinished.WaitButton(new String[]{"Отменить направление", "Выполнить исследование"});

        System.out.println("5 проверка - МО1 - МО2 - МО1 - МО2");
        AuthorisationAdd(false, PMOTarget, true, id);
        incomingUnfinished.WaitButton(new String[]{"Отклонить направление", "Согласовать"});

        System.out.println("6 проверка - МО1 - МО2 - МО3 - МО1");
        sql.UpdateConnection("Update telmed.directions set referringmoid = '54' where id = '" + id + "';");
        AuthorisationAdd(true, "", false, id);
        incomingUnfinished.WaitButton(new String[]{"Отменить направление"});

        System.out.println("7 проверка - МО1 - МО2 - МО3 - МО2");
        sql.UpdateConnection("Update telmed.directions set referringmoid = '54' where id = '" + id + "';");
        AuthorisationAdd(false, PMOTarget, true, id);
        incomingUnfinished.WaitButton(new String[]{"Отклонить направление", "Согласовать"});

        System.out.println("8 проверка - МО1 - МО2 - МО3 - МО3");
        AuthorisationAdd(false, "БУ ХМАО-Югры \"Белоярская районная больница\"", true, id);
        incomingUnfinished.WaitButton(new String[]{"Отклонить направление", "Согласовать"});

        System.out.println("9 проверка - МО1 - МО1 - МО3 - МО1");
        xml.changes.put("$.TargetMedicalOid", "1.2.643.5.1.13.13.12.2.86.8902");
        xml.changes.put("$.ResearchCode", "HMP01");
        xml.changes.put("$.PatientGuid", PatientGuid);
        modifiedJson = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, modifiedJson, 200, false);
        id = Response.getString("Result.DirectionId");

        AuthorisationAdd(true, "", false, id);
        incomingUnfinished.WaitButton(new String[]{"Отменить направление", "Отправить на интерпретацию"});
        ClickElement(directionsForQuotas.Button("Отправить на интерпретацию"));
        ClickElement(authorizationObject.Selected);
        ClickElement(authorizationObject.Select("БУ ХМАО-Югры \"Белоярская районная больница\""));
        ClickElement(directionsForQuotas.Send);
        Thread.sleep(1500);
        incomingUnfinished.WaitButton(new String[]{"Отменить направление"});

        System.out.println("10 проверка - МО1 - МО1 - МО3 - МО2");
        AuthorisationAdd(false, PMOTarget, false, id);

        System.out.println("11 проверка - МО1 - МО1 - МО3 - МО3");
        AuthorisationAdd(false, "БУ ХМАО-Югры \"Белоярская районная больница\"", true, id);
        incomingUnfinished.WaitButton(new String[]{"Отклонить направление", "Согласовать"});

        System.out.println("12 проверка - МО1 - МО1 - МО1 - МО1");
        sql.UpdateConnection("Update telmed.directions set referringmoid = '12' where id = '" + id + "';");
        AuthorisationAdd(true, "", true, id);
        incomingUnfinished.WaitButton(new String[]{"Отменить направление", "Выполнить исследование"});

        System.out.println("13 проверка - МО1 - МО1 - МО1 - МО2");
        AuthorisationAdd(false, PMOTarget, false, id);
    }

    @Issue(value = "TEL-4038")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Link(name = "ТМС-2269", url = "https://team-1okm.testit.software/projects/5/tests/2269?isolatedSection=d72e058a-4718-4994-b342-ea7fad3fa259")
    @Story("Интерпретация")
    @DisplayName("Отправка на массовую интерпретацию")
    @Description("Создаём несколько направлений, отправляем на массовую интерпретацию")
    public void Access_4038 () throws InterruptedException, SQLException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        authorization = new Authorization();
        incomingArchive = new IncomingArchive(driver);

        AddRole(PRole, "Доступ ко всем направлениям на квоты", false);
        AddRole(PRole, "Возможность перенаправлять направления на описание в другое МО", true);

        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        System.out.println("Создаём 2 направления");
        xml.changes.put("$.TargetMedicalOid", POidMoTarget);
        xml.changes.put("$.ResearchCode", "HMP01");
        xml.changes.put("$.PatientGuid", PatientGuid);
        String modifiedJson = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, modifiedJson, 200, false);
        String id = Response.getString("Result.DirectionId");

        modifiedJson = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, modifiedJson, 200, false);
        String id2 = Response.getString("Result.DirectionId");

        System.out.println("Переходим в направления и нажимаем Отправка на интепретацию");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.SortDesc);

        System.out.println("Проверяем, что созданные направления в статусе Создан");
        String direction1 = driver.findElement(directionsForQuotas.GetDirection(id, "8")).getText();
        String direction2 = driver.findElement(directionsForQuotas.GetDirection(id2, "8")).getText();
        Assertions.assertEquals(direction1, "Создан", "Направление должно быть со статусом Создан");
        Assertions.assertEquals(direction2, "Создан", "Направление должно быть со статусом Создан");

        System.out.println("Выбираем 2 отправленных направления");
        ClickElement(directionsForQuotas.MassInter);
        ClickElement(directionsForQuotas.MassInterCheck(id));
        ClickElement(directionsForQuotas.MassInterCheck(id2));
        ClickElement(directionsForQuotas.MassInterAdd);
        ClickElement(authorizationObject.Selected);
        ClickElement(authorizationObject.Select(PMORequest));
        ClickElement(directionsForQuotas.Send);
        Thread.sleep(1500);

        SqlWait(id, PMORequest);
        SqlWait(id2, PMORequest);
    }

    @Issue(value = "TEL-4038")
    @Issue(value = "TEL-4151")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Link(name = "ТМС-2269", url = "https://team-1okm.testit.software/projects/5/tests/2269?isolatedSection=d72e058a-4718-4994-b342-ea7fad3fa259")
    @Story("Интерпретация")
    @DisplayName("Проверка отображения кнопок у Врача, которого только одна МО")
    @Description("Создаём направление из МО1 в МО2 через врача у которого есть МО1, после авторизуемся через врача, у которого есть только МО2 и проверяем кнопки")
    public void Access_4038_1 () throws Exception {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        authorization = new Authorization();
        incomingArchive = new IncomingArchive(driver);

        AddRole(PRole, "Доступ ко всем направлениям на квоты", false);
        AddRole(PRole, "Возможность перенаправлять направления на описание в другое МО", true);

        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        System.out.println("Создаём направление");
        xml.changes.put("$.TargetMedicalOid", POidMoTarget);
        xml.changes.put("$.ResearchCode", "HMP01");
        xml.changes.put("$.PatientGuid", PatientGuid);
        // Указываем врача у которого одна МО
        xml.changes.put("$.DoctorInformation.DoctorSnils", "96198623569");
        String modifiedJson = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, modifiedJson, 200, false);
        String id = Response.getString("Result.DirectionId");
        String directionGuid = Response.getString("Result.Guid");

        directionsForQuotas.AddFile("БУ ХМАО-Югры \"Окружная клиническая больница\"", id, PNameEquipment2);
        Thread.sleep(2500);

        /** Проверяем дополнительно скачивание файлов у направления (4151) */
        String IdFile = null;
        Integer number = 0;
        while (IdFile == null & number < 10) {
            Api(HostAddress + "/api/direction/" + directionGuid + "/listfiles", "get", null, null, "", 0, false);
            IdFile = Response.getString("Result[0].Id");
            number++;
        }

        String response = given()
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .when()
                .get(HostAddress + "/api/direction/file/" + IdFile)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract().response().asString();
        Assertions.assertEquals(response, "fgjjfjh", "В ответе должен быть текст из файла");

        System.out.println("Переходим к направлению Входящие - незавершённые");
        driver.get(HostAddressWeb + "/auth/bysnils?snils=024-605-477 32&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
        ClickElement(incomingUnfinished.ConsultationWait);
        ClickElement(incomingUnfinished.DESK);
        ClickElement(incomingUnfinished.SearchID(id));
        incomingUnfinished.WaitButton(
                new String[]{"Отклонить направление", "Согласовать", "Отправить на интерпретацию"});
        ClickElement(incomingUnfinished.Button("Согласовать"));
        Thread.sleep(1500);
        incomingUnfinished.WaitButton(
                new String[]{"Отклонить направление", "Выполнить исследование", "Отправить на интерпретацию"});

        sql.StartConnection("Select *, m.namemu from telmed.directions d\n" +
                "left join dpc.mis_sp_mu m on d.referringmoid = m.idmu \n" +
                "where id = '" + id + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("status");
        }
        Assertions.assertEquals(sql.value, "15", "Статус должен быть равен 15");

        System.out.println("Авторизуемся через МО1 из которой отправили и проверяем, что отображаются нужные кнопки");
        driver.get(HostAddressWeb + "/auth/bysnils?snils=961-986-235 69&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.SortDesc);
        ClickElement(directionsForQuotas.SearchID(id));
        incomingUnfinished.WaitButton(
                new String[]{"Отменить направление", "Изменить дату приема", "Отправить на интерпретацию"});
    }

    @Step("Метод для перехода либо во Входящие либо в Исходящие")
    private void AuthorisationAdd (boolean MyMo, String mo, boolean wait, String direction) throws InterruptedException {
        if (MyMo) {
            AuthorizationMethod(authorizationObject.OKB);
            ClickElement(directionsForQuotas.ConsultationWait);
            ClickElement(directionsForQuotas.SortDesc);
            ClickElement(incomingUnfinished.SearchID(direction));
        } else {
            AuthorizationMethod(authorizationObject.Select(mo));
            ClickElement(incomingUnfinished.ConsultationWait);
            ClickElement(incomingUnfinished.DESK);
            Thread.sleep(2500);
            if (wait) {
                ClickElement(incomingUnfinished.SearchID(direction));
            } else {
                WaitElement(incomingUnfinished.FirstLineMO);
                WaitNotElement(incomingUnfinished.SearchID(direction));
            }
        }
    }

    @Step("Метод для замены статус в бд у направления")
    private void SqlUpdate (String value, String status, boolean MyMo, boolean role) throws SQLException {
        sql.UpdateConnection("update telmed.directions set status = '" + status + "' where id = '" + value + "';");
        driver.navigate().refresh();
        WaitElement(incomingUnfinished.NumberConsultation);
        // Здесь можно было указать любой другой статус, который не входит в список 3,6,8,15,28,30, я выбрал 14
        if (!status.equals("14") & role) {
            WaitElement(incomingUnfinished.Button("Отправить на интерпретацию"));
        } else if (status.equals("14")) {
            WaitNotElement3(incomingUnfinished.Button("Отправить на интерпретацию"), 2);
        }
        if (MyMo) {
            if (status.equals("6") | status.equals("15")) {
                WaitElement(incomingUnfinished.Button("Отменить направление"));
            }
        }
        if (!MyMo) {
            if (status.equals("15")) {
                WaitElement(incomingUnfinished.Button("Отклонить направление"));
                WaitElement(incomingUnfinished.Button("Выполнить исследование"));
            }
        }

    }

    @Step("Метод для проверки МО куда отправил на интерпретацию")
    private void SqlWait (String value, String MO) throws SQLException {
        sql.StartConnection("Select *, m.namemu from telmed.directions d\n" +
                "join dpc.mis_sp_mu m on d.referringmoid = m.idmu \n" +
                "where id = '" + value + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("namemu");
        }
        Assertions.assertEquals(MO, sql.value,
                "Мы отправили на интепретацию в МО - " + MO + " она должна совпадать со значением из telmed.referringmoid - " + sql.value);
    }
}
