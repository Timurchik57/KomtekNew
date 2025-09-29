package UI.TmTest.AccessUI.VIMIS.SMS;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.SMS;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис")
@Tag("СМС")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Принудительная_отправка")
@Disabled
public class Access_1238 extends BaseAPI {
    AuthorizationObject authorizationObject;
    SMS sms;

    @Test
    @Story("Проверка, что СМС принудительно отправляется в ВИМИС")
    @Issue(value = "TEL-1238")
    @Link(name = "ТМС-1484", url = "https://team-1okm.testit.software/projects/5/tests/1484?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка, что СМС принудительно отправляется в ВИМИС для Онкологии")
    @Description("Отправляем смс - переходим в Структурированные медицинские сведения - принудительно отправляем в ВИМИС через удкон, проверяем, что добавились значения msg_id и request_id")
    public void Access_1238_1() throws SQLException, InterruptedException, IOException {
        sms = new SMS(driver);
        Access_1238Method("SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21,  sms.OncologyWait);
    }

    @Test
    @Story("Проверка, что СМС принудительно отправляется в ВИМИС")
    @DisplayName("Проверка, что СМС принудительно отправляется в ВИМИС для Профилактики")
    public void Access_1238_2() throws SQLException, InterruptedException, IOException {
        sms = new SMS(driver);
        Access_1238Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21,  sms.PreventionWait);
    }

    @Test
    @Story("Проверка, что СМС принудительно отправляется в ВИМИС")
    @DisplayName("Проверка, что СМС принудительно отправляется в ВИМИС для Акушерство и неонатология")
    public void Access_1238_3() throws SQLException, InterruptedException, IOException {
        sms = new SMS(driver);
        Access_1238Method("SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21,  sms.AkineoWait);
    }

    @Test
    @Story("Проверка, что СМС принудительно отправляется в ВИМИС")
    @DisplayName("Проверка, что СМС принудительно отправляется в ВИМИС для Сердечно-сосудистые заболевания")
    public void Access_1238_4() throws SQLException, InterruptedException, IOException {
        sms = new SMS(driver);
        Access_1238Method("SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1,57, 21,  sms.SSZWait);
    }

    @Test
    @Story("Проверка, что СМС принудительно отправляется в ВИМИС")
    @DisplayName("Проверка, что СМС принудительно отправляется в ВИМИС для Инфекция")
    public void Access_1238_5() throws SQLException, InterruptedException, IOException {
        sms = new SMS(driver);
        Access_1238Method("SMS/SMS3.xml", "3", 5, 1, true, 3, 1, 9, 18, 1, 57, 21, sms.InfectionWait);
    }

    @Step("Метод принудительной отправки документа")
    public void Access_1238Method(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, By District) throws InterruptedException, IOException, SQLException {
        authorizationObject = new AuthorizationObject(driver);

        TableVmcl(vmcl);

        System.out.println("Отправляем смс с id = 3");
        xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1, position1,
                speciality1);

        System.out.println("Проверяем, что значения msg_id и request_id пустые");
        sql.StartConnection("select * from " + smsBase + " where local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("msg_id");
            Assertions.assertNull(sql.value, "Значение msg_id должно быть пустым");
            sql.value = sql.resultSet.getString("request_id");
            Assertions.assertNull(sql.value, "Значение request_id должно быть пустым");
        }

        System.out.println("Переходим в СМС, находим отправленный документ и отправляем принудительно");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(sms.SMSWait);
        ClickElement(District);
        ClickElement(sms.FilterWaitAdd);
        ClickElement(sms.Ident);
        Thread.sleep(1500);
        inputWord(driver.findElement(sms.localUid), "" + xml.uuid + " ");
        ClickElement(sms.SearchWait);
        Thread.sleep(1000);
        ClickElement(By.xpath("(//tr[1]/td[7][contains(.,'" + xml.uuid + "')]/preceding-sibling::td[6]//span)[1]"));
        Thread.sleep(1500);
        ClickElement(sms.Send);
        WaitNotElement3(sms.LoadingSend("3"), 30);
        WaitElement(sms.Success);
        Thread.sleep(2000);

        System.out.println("Проверяем, что значения msg_id и request_id заполнены");
        sql.StartConnection("select * from " + smsBase + " where local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("msg_id");
            Assertions.assertNotEquals(sql.value, null, "Значение msg_id не заполнено");
            sql.value = sql.resultSet.getString("request_id");
            Assertions.assertNotEquals(sql.value, null, "Значение request_id не заполнено");
        }
    }
}
