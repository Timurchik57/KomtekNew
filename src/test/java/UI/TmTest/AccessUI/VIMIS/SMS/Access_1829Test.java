package UI.TmTest.AccessUI.VIMIS.SMS;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.SMS;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис")
@Tag("СМС")
@Tag("Проверка_БД")
public class Access_1829Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    SMS sms;
    private String NewDate;
    private String data;

    @Issue(value = "TEL-1823")
    @Link(name = "ТМС-1820", url = "https://team-1okm.testit.software/projects/5/tests/1820?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Description("Переходим в ВИМИС - СМС - выбираем дату начала/конца периода, смотрим верное отображение данных")
    @Test
    @DisplayName("Выбор даты в календаре в Структурированные медицинские сведения")
    public void Access_1829() throws IOException, InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        sms = new SMS(driver);

        System.out.println("отправляем смс для онкологии");
        xml.ApiSmd("SMS/SMS3.xml", "3", 1, 2, true, 3, 1, 9, 18, 1, 57, 21);

        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(sms.SMSWait);
        ClickElement(sms.OncologyWait);
        ClickElement(sms.FilterWaitAdd);
        SelectClickMethod(sms.BeforeTimeWait, sms.DataToDay);
        ClickElement(sms.SearchWait);
        Thread.sleep(1500);
        WaitNotElement3(sms.LoadingSend("2"), 30);
        Thread.sleep(1500);
        ClickElement(sms.SortAp);
        Thread.sleep(1500);
        WaitNotElement3(sms.LoadingSend("2"), 30);
        Thread.sleep(1500);
        if (isElementNotVisible(sms.FirstSearchData) == true) {
            WaitElement(sms.FirstSearchData);
            data = driver.findElement(sms.FirstSearchData).getText();
            NewDate = StringUtils.substring(data, 0, 2);

            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd");
            Date = formatForDateNow.format(date);

            System.out.println("Первая проверка - отображается с текущего числа");
            if (KingNumber == 4) {
                Thread.sleep(2000);
            }
            Assertions.assertEquals(NewDate, "" + Date + "", "Документы отображаются не с текущего числа");
        }

        if (KingNumber != 4) {
            ClickElement(sms.FilterWaitAdd);
            ClickElement(sms.Reset);
            Thread.sleep(1500);
            WaitNotElement3(sms.LoadingSend("2"), 30);
            Thread.sleep(1500);
        }
        ClickElement(sms.FilterWaitAdd);
        SelectClickMethod(sms.AfterTimeWait, sms.DataToDay);
        ClickElement(sms.SearchWait);
        Thread.sleep(1500);
        WaitNotElement3(sms.LoadingSend("2"), 30);
        Thread.sleep(1500);
        ClickElement(sms.SortDesk);
        Thread.sleep(1500);
        WaitNotElement3(sms.LoadingSend("2"), 30);
        Thread.sleep(1500);
        if (isElementNotVisible(sms.FirstSearchData) == true) {
            WaitElement(sms.FirstSearchData);
            data = driver.findElement(sms.FirstSearchData).getText();
            NewDate = StringUtils.substring(data, 0, 2);

            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd");
            Date = formatForDateNow.format(date);

            System.out.println("Вторая проверка - отображается до текущего числа");
            Assertions.assertEquals(NewDate, "" + Date + "", "Документы отображаются не до текущего числа");
        }

        if (KingNumber != 4) {
            ClickElement(sms.FilterWaitAdd);
            SelectClickMethod(sms.BeforeTimeWait, sms.DataToDay);
            ClickElement(sms.SearchWait);
            Thread.sleep(1500);
            WaitNotElement3(sms.LoadingSend("2"), 30);
            Thread.sleep(1500);
            ClickElement(sms.SortAp);
            Thread.sleep(1500);
            WaitNotElement3(sms.LoadingSend("2"), 30);
            Thread.sleep(1500);
            if (isElementNotVisible(sms.FirstSearchData) == true) {
                WaitElement(sms.FirstSearchData);
                data = driver.findElement(sms.FirstSearchData).getText();
                NewDate = StringUtils.substring(data, 0, 2);

                System.out.println("Третья проверка - отображается с текущего числа");
                Assertions.assertEquals(NewDate, "" + Date + "", "Документы отображаются не с текущего числа");
            }
            ClickElement(sms.SortDesk);
            Thread.sleep(1500);
            WaitNotElement3(sms.LoadingSend("2"), 30);
            Thread.sleep(1500);
            if (isElementNotVisible(sms.FirstSearchData) == true) {
                WaitElement(sms.FirstSearchData);
                data = driver.findElement(sms.FirstSearchData).getText();
                NewDate = StringUtils.substring(data, 0, 2);

                System.out.println("Четвёртая проверка - отображается до текущего числа");
                Assertions.assertEquals(NewDate, "" + Date + "", "Документы отображаются не до текущего числа");
            }
        }
    }
}
