package UI.TmTest.AccessUI.NSI;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.NSI.Equipments;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("НСИ")
@Tag("Проверка_БД")
@Tag("Оборудование")
@Tag("Основные")
public class Access_1092Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    Equipments equipments;
    String BODY;
    String id;
    String SerialNumber;
    String InventoryNumber;
    String MO;
    String Name;
    String Type;

    @Test
    @Issue(value = "TEL-1092")
    @Link(name = "ТМС-1378", url = "https://team-1okm.testit.software/projects/5/tests/1378?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Изменения в отображении справочника по оборудованию")
    @Description("Перейти в НСИ - Оборудование. Редактирование оборудования по всем параметрам, проверка изменения отображения, сверка данных с БД")
    public void EquipmentAccessDepartment() throws SQLException, InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        equipments = new Equipments(driver);

        System.out.println("Определяем параметр по МТБЗ");
        MTBZMethod();

        AddRole(PRole,"Разрешено редактировать справочник оборудования своего МО", true);
        AddRole(PRole,"Разрешено редактировать справочник оборудования любого МО", true);

        System.out.println("Переход в НСИ - Оборудование");
        AuthorizationMethod(authorizationObject.Select(PMOTarget));
        ClickElement(equipments.EquipmentWaitt);
        WaitElement(equipments.HeaderEquipmentWait);
        WaitElement(equipments.NameWait);

        inputWord(equipments.Name, "X-OMATT");
        inputWord(equipments.Description, "1233");
        ClickElement(equipments.CheckIs);
        WaitNotElement3(equipments.SearchLoading, 120);
        ClickElement(equipments.SearchWait);
        WaitElement(equipments.NameColumnWait);
        Thread.sleep(1500);
        WaitNotElement3(equipments.Loading, 30);

        List <WebElement> list = driver.findElements(By.xpath("//tr/th"));

        System.out.println("Проверка наименования столбцов");
        for (int i = 1; i < list.size(); i++) {
            String value = driver.findElement(By.xpath("(//thead/tr/th/div)[" + i + "]")).getText();
            if (i == 1) {
                value.contains("Наименование");
            }
            if (i == 2) {
                value.contains("Тип оборудования");
            }
            if (i == 3) {
                value.contains("Мед. организация");
            }
            // Специально убрали, чтобы не было дублей
//            if (i == 4) {
//                value.contains("Подразделение");
//            }
//            if (i == 5) {
//                value.contains("Отделение");
//            }
            if (i == 4) {
                value.contains("Описание");
            }
            if (i == 5) {
                value.contains("Дата ввода");
            }
            if (i == 6) {
                value.contains("Дата списания");
            }
            if (i == 7) {
                value.contains("");
            }
        }

        System.out.println("Проверка цвета оборудования (зелёный)");
        String Color = equipments.FirstLine.getCssValue("background-color");
        Assertions.assertEquals(Color, "rgba(207, 255, 207, 1)", " Цвет не совпадает с зелёным");

        if (!MTBZ) {

            System.out.println("Редактировать 1 элемент из списка");
            ClickElement(equipments.Edit1Wait);
            WaitElement(equipments.HeaderEditWait);

            System.out.println("Берём название Типа оборудования");
            ClickElement(equipments.TypeWait);
            Thread.sleep(1000);
            String equipmenttypename;
            if ((isElementNotVisible(equipments.SelectTypeTrueWait))) {
                equipmenttypename = equipments.SelectTypeTrue.getText();
            } else {
                equipmenttypename = equipments.SelectTypeTrueHover.getText();
            }
            ClickElement(equipments.TypeWait);

            System.out.println("Берём Модальность оборудования");
            ClickElement(equipments.ModelWait);
            Thread.sleep(1000);
            String full_name;
            if ((isElementNotVisible(equipments.SelectTypeTrueWait))) {
                full_name = equipments.SelectTypeTrue.getText();
            } else {
                full_name = equipments.SelectTypeTrueHover.getText();
            }
            ClickElement(equipments.ModelWait);

            System.out.println("Берём Подразделение МО");
            String depart_name;
            if ((isElementNotVisible(equipments.DivisionNotWait))) {
                ClickElement(equipments.DivisionWait);
                ClickElement(equipments.SelectDivisionBuh);
                ClickElement(equipments.DivisionWait);
                Thread.sleep(1000);
                if ((isElementNotVisible(equipments.SelectTypeTrueWait))) {
                    depart_name = equipments.SelectTypeTrue.getText();
                } else {
                    depart_name = equipments.SelectTypeTrueHover.getText();
                }
                ClickElement(equipments.DivisionWait);
            } else {
                ClickElement(equipments.DivisionWait);
                Thread.sleep(1000);
                if ((isElementNotVisible(equipments.SelectTypeTrueWait))) {
                    depart_name = equipments.SelectTypeTrue.getText();
                } else {
                    depart_name = equipments.SelectTypeTrueHover.getText();
                }
            }
            ClickElement(equipments.DivisionWait);

            System.out.println("Берём инвентарный номер");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String inventorynumber = (String) js.executeScript("return arguments[0].value", equipments.InventNumber);

            System.out.println("Берём серийный номер");
            String serialnumber = (String) js.executeScript("return arguments[0].value", equipments.SerialNumber);
            System.out.println(
                    "Поиск нужного оборудования в БД и проверка, что isavailableforothermo равно 1 (зелёный)");
            String TextSQL = "select eq.model, eq.equipmenttypename, eq.moname, mss.depart_name,  mb.hospital_name, mb.\"oid\", eq.description, h.full_name, eq.isavailableforothermo, eq.begindate, eq.enddate, \n" +
                    "eq.reasoninaccessibilityid, eq.inaccessibilitydate, eq.inaccessibilityinitiatorsnils  From telmed.equipmentregistry eq\n" +
                    "left join dpc.MIS_SP_MU dpcmo on dpcmo.medicalidmu = eq.medicalidmu\n" +
                    "         left join telmed.mo_permissions mop on dpcmo.idmu = mop.idmo\n" +
                    "         inner join telmed.equipmentdisplay d on d.id = eq.equipmenttypeid\n" +
                    "         inner join telmed.equipmenttypes et on et.id = eq.equipmenttypeid\n" +
                    "         left join (Select equipmentid,\n" +
                    "                           STRING_AGG(medicalresearchid::character varying, '; '\n" +
                    "                                      order by medicalresearchid) as ReseachIds\n" +
                    "                    from telmed.equipmedresearch\n" +
                    "                    group by equipmentid) idsTbl on idsTbl.equipmentid = eq.id\n" +
                    "         left join (Select equipmentid,\n" +
                    "                           STRING_AGG(m.name, '; ') as ResearchNames\n" +
                    "                    from telmed.equipmedresearch es\n" +
                    "                             left join telmed.medicalresearch m on m.id = es.medicalresearchid\n" +
                    "                    where es.equipmentid = equipmentid\n" +
                    "                    group by es.equipmentid) namesTbl on namesTbl.equipmentid = eq.id\n" +
                    "         left join dpc.mo_subdivision_structure mss on eq.buildingid = mss.depart_oid\n" +
                    "         left join dpc.mo_branches mb on eq.cabinet = mb.oid\n" +
                    "         left join telmed.hst0492 h on eq.modality = h.code \n" +
                    "where eq.model = 'X-OMAT' and eq.moname = 'БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"'\n" +
                    "and eq.equipmenttypename = '" + equipmenttypename + "' and eq.inventorynumber = '" + inventorynumber + "' and eq.serialnumber = '" + serialnumber + "' and h.full_name = '" + full_name + ";'\n" +
                    "and mss.depart_name = '" + depart_name + "';";
            sql.StartConnection(TextSQL);
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("isavailableforothermo");
                Assertions.assertEquals("1", sql.value, "Значение isavailableforothermo не равно 1");
            }
            System.out.println("Добавляем дату списания");
            ClickElement(equipments.DateDowns);
            ClickElement(equipments.DateDownsToDay);
            ClickElement(equipments.UpdateWait);

            System.out.println("Проверка добавления даты списания в таблице");
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
            Date = formatForDateNow.format(date);
            WaitElement(By.xpath("//tbody/tr[1]/td[8]//span[contains(.,'" + Date + "')]"));

            System.out.println("Проверка цвета оборудования (серый)");
            Color = equipments.FirstLine.getCssValue("background-color");
            Assertions.assertEquals(Color, "rgba(224, 220, 220, 1)", "Цвет не совпадает с серым");

            System.out.println("Проверка даты в бд");
            formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
            Date = formatForDateNow.format(date);
            sql.StartConnection(TextSQL);
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("enddate");
                Assertions.assertEquals("" + Date + " 00:00:00", sql.value, "Значение enddate не равно текущей дате");
            }
            if (KingNumber == 1) {
                BODY = "{\"Username\": \"1.2.643.5.1.13.13.12.2.86.9003\",\n" +
                        "                   \"SystemId\": 22,\n" +
                        "                    \"Password\": \"612D11DB39CE0E0C434CCA701855CDDC\"}";
            }
            if (KingNumber == 2) {
                BODY = "{\"Username\": \"1.2.643.5.1.13.13.12.2.86.9003\",\n" +
                        "                   \"SystemId\": 22,\n" +
                        "                    \"Password\": \"561D9DF2BBAD98CF327DE72BEB3FB33C\"}";
            }
            if (KingNumber == 4) {
                BODY = "{\"Username\": \"1.2.643.5.1.13.13.12.2.86.8902\",\n" +
                        "                   \"SystemId\": 13,\n" +
                        "                    \"Password\": \"8487D0E97267061375210E5D4031C567\"}";
            }
            String Token = given()
                    .filter(new AllureRestAssured())
                    .contentType(ContentType.JSON)
                    .body(BODY)
                    .when()
                    .post(HostAddress + "/auth.svc")
                    .then()
                    .statusCode(200)
                    .extract().jsonPath().getString("Result.Value");
            if (KingNumber == 1) {
                id = "14037";
                SerialNumber = "313141";
                InventoryNumber = "1010404095";
                MO = "1.2.643.5.1.13.13.12.2.86.9003";
                Name = "X-OMAT";
                Type = "54";
            }
            if (KingNumber == 2) {
                id = "17478";
                SerialNumber = "313141";
                InventoryNumber = "1010404095";
                MO = "1.2.643.5.1.13.13.12.2.86.9003";
                Name = "X-OMAT";
                Type = "54";
            }
            if (KingNumber == 4) {
                id = "50019";
                SerialNumber = "3CA14Y2040";
                InventoryNumber = "101240000953";
                MO = "1.2.643.5.1.13.13.12.2.86.8902";
                Name = "Aquilon LB";
                Type = "35";
            }
            String result = given()
                    .filter(new AllureRestAssured())
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + Token)
                    .body("{\n" +
                            "  \"Id\": " + id + ",\n" +
                            "  \"Name\": \"" + Name + "\",\n" +
                            "  \"Type\": " + Type + ",\n" +
                            "  \"Modality\": 1,\n" +
                            "  \"MedicalOid\": \"" + MO + "\",\n" +
                            "  \"PatientMaxWeight\": 50.0,\n" +
                            "  \"IsAvailableForOtherMo\": true,\n" +
                            "  \"DateAnnulment\": \"\",\n" +
                            "  \"SerialNumber\": \"" + SerialNumber + "\",\n" +
                            "  \"InventoryNumber\": \"" + InventoryNumber + "\",\n" +
                            "  \"Description\": \"123\"\n" +
                            "}")
                    .when()
                    .put(HostAddress + "/api/equipment/update")
                    .then()
                    .statusCode(200)
                    .extract().jsonPath().getString("Result.Id");
            if (KingNumber == 1) {
                Assertions.assertEquals(result, "14037", "Дата списания не удалилась");
            }
            if (KingNumber == 2) {
                Assertions.assertEquals(result, "17478", "Дата списания не удалилась");
            }
            if (KingNumber == 4) {
                Assertions.assertEquals(result, "50019", "Дата списания не удалилась");
            }
            driver.navigate().refresh();
            WaitElement(equipments.HeaderEquipmentWait);
            if (KingNumber == 4) {
                SelectClickMethod(equipments.InputMoWait, equipments.SelectMOOKB);
            } else {
                SelectClickMethod(equipments.InputMoWait, equipments.SelectMO);
                inputWord(equipments.Name, "X-OMATT");
            }
            inputWord(equipments.Description, "1233");
            ClickElement(equipments.CheckIs);
            WaitNotElement3(equipments.SearchLoading, 20);
            ClickElement(equipments.SearchWait);
            WaitElement(equipments.NameColumnWait);

            System.out.println("Проверка цвета оборудования (зелёный)");
            Color = equipments.FirstLine.getCssValue("background-color");
            Assertions.assertEquals(Color, "rgba(207, 255, 207, 1)", " Цвет не совпадает с зелёным");

            System.out.println("Редактировать 1 элемент из списка, добавляем причину недоступности");
            ClickElement(equipments.Edit1Wait);
            WaitElement(equipments.HeaderEditWait);
            ClickElement(equipments.CheckInput);
            ClickElement(equipments.Reason);

            System.out.println("Сверяем причину недоступности с БД");
            Thread.sleep(1000);
            List<String> Reason = new ArrayList<String>();
            List<WebElement> ReasonWeb = driver.findElements(equipments.SelectReason);
            for (int i = 0; i < ReasonWeb.size(); i++) {
                Reason.add(ReasonWeb.get(i).getText());

            }
            Collections.sort(Reason);
            List<String> ReasonSql = new ArrayList<String>();
            sql.StartConnection("SELECT * FROM dpc.equipment_reasons_inaccessibility;");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("name");
                ReasonSql.add(sql.value);
            }
            Collections.sort(ReasonSql);
            Assertions.assertEquals(Reason, ReasonSql, "Не совпадает Причина недоступности с значениями в БД");
            ClickElement(equipments.SelectReasonFirst);
            ClickElement(equipments.UpdateWait);
            System.out.println("Убираем доступно для других МО");
            ClickElement(equipments.CheckIs);
            if (KingNumber == 4) {
                SelectClickMethod(equipments.InputMoWait, equipments.SelectMOOKB);
            } else {
                SelectClickMethod(equipments.InputMoWait, equipments.SelectMO);
                inputWord(equipments.Name, "X-OMATT");
            }
            inputWord(equipments.Description, "1233");
            WaitNotElement3(equipments.SearchLoading, 20);
            ClickElement(equipments.SearchWait);
            System.out.println("Проверка цвета оборудования (красный)");
            Color = equipments.FirstLine.getCssValue("background-color");
            Assertions.assertEquals(Color, "rgba(255, 184, 184, 1)", " Цвет не совпадает с красным");
            System.out.println("Проверка изменений значений Причины недоступности в БД");
            formatForDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date = formatForDateNow.format(date);
            sql.StartConnection(TextSQL);
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("isavailableforothermo");
                String reasoninaccessibilityid = sql.resultSet.getString("reasoninaccessibilityid");
                String inaccessibilitydate = sql.resultSet.getString("inaccessibilitydate");
                String inaccessibilityinitiatorsnils = sql.resultSet.getString("inaccessibilityinitiatorsnils");
                Assertions.assertEquals("0", sql.value, "Значение isavailableforothermo не равно 0");
                Assertions.assertEquals("" + Date + "", inaccessibilitydate,
                        "Значение isavailableforothermo не равно 0");
                Assertions.assertEquals("3", reasoninaccessibilityid, "Значение reasoninaccessibilityid не равно 3");
                Assertions.assertEquals(
                        PSnils_, inaccessibilityinitiatorsnils,
                        "Значение inaccessibilityinitiatorsnils не равно " + PSnils_
                );
            }

            System.out.println("Редактировать 1 элемент из списка убираем причину недоступности");
            ClickElement(equipments.Edit1Wait);
            WaitElement(equipments.HeaderEditWait);
            ClickElement(equipments.CheckInput);
            ClickElement(equipments.UpdateWait);
            WaitElement(equipments.NameColumnWait);

            System.out.println("Проверка цвета оборудования (зелёный)");
            Thread.sleep(1500);
            WaitNotElement3(equipments.Loading, 30);
            Color = equipments.FirstLine.getCssValue("background-color");
            Assertions.assertEquals(Color, "rgba(207, 255, 207, 1)", " Цвет не совпадает с зелёным");
            sql.StartConnection(TextSQL);
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("isavailableforothermo");
                String reasoninaccessibilityid = sql.resultSet.getString("reasoninaccessibilityid");
                String inaccessibilitydate = sql.resultSet.getString("inaccessibilitydate");
                String inaccessibilityinitiatorsnils = sql.resultSet.getString("inaccessibilityinitiatorsnils");
                Assertions.assertEquals("1", sql.value, "Значение isavailableforothermo не равно 0");
                Assertions.assertNull(inaccessibilitydate, "Значение isavailableforothermo не равно 0");
                Assertions.assertNull(reasoninaccessibilityid, "Значение reasoninaccessibilityid не равно 3");
                Assertions.assertNull(
                        inaccessibilityinitiatorsnils,
                        "Значение inaccessibilityinitiatorsnils не равно " + PSnils_
                );
            }
        }
    }
}