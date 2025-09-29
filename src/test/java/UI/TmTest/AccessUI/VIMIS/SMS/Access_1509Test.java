package UI.TmTest.AccessUI.VIMIS.SMS;

import Base.SQL;
import Base.TestListener;
import UI.TmTest.PageObject.Administration.AcceessRoles;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.SMS;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
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
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Фрэмд_статус")
@Tag("Роли_доступа")
public class Access_1509Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    SMS sms;
    AcceessRoles acceessRoles;

    public void SetStatus(
            String status, String fremd_status, String NameStatus, Boolean Accept
    ) throws SQLException, InterruptedException {
        SQL sql = new SQL();
        sms = new SMS(driver);
        System.out.println(status);
        System.out.println(fremd_status);
        System.out.println("Устанавливаем status = " + status + ", fremd_status = " + fremd_status + "");
        if (fremd_status == "null" & status == "null") {
            sql.UpdateConnection(
                    "Update vimis.remd_sent_result set status = " + status + ", fremd_status = " + fremd_status + " where local_uid = '" + xml.uuid + "';");
        }
        if (fremd_status == "null" & status != "null") {
            sql.UpdateConnection(
                    "Update vimis.remd_sent_result set status = '" + status + "', fremd_status = " + fremd_status + " where local_uid = '" + xml.uuid + "';");
        }
        if (fremd_status != "null" & status == "null") {
            sql.UpdateConnection(
                    "Update vimis.remd_sent_result set status = " + status + ", fremd_status = '" + fremd_status + "' where local_uid = '" + xml.uuid + "';");
        }
        if (fremd_status != "null" & status != "null") {
            sql.UpdateConnection(
                    "Update vimis.remd_sent_result set status = '" + status + "', fremd_status = '" + fremd_status + "' where local_uid = '" + xml.uuid + "';");
        }
        ClickElement(sms.FilterWaitAdd);
        ClickElement(sms.SearchWait);
        Thread.sleep(1500);
        if (KingNumber == 4) {
            WaitNotElement3(sms.Loading, 20);
        }
        if (Accept == true) {
            WaitElement(sms.NotResultSearch);
            Assertions.assertEquals(
                    driver.findElement(sms.NotResultSearch).getText(), "" + NameStatus + "",
                    "При status = " + status + " и fremd_status = " + fremd_status + " статус не '" + NameStatus + "'"
            );
        } else {
            WaitElement(By.xpath("//tr/td[7]//span/div[contains(.,'" + xml.uuid + "')]"));
            WaitElement(sms.ResultSearchStatus);
            Assertions.assertEquals(
                    driver.findElement(sms.ResultSearchStatus).getText(), "" + NameStatus + "",
                    "При status = " + status + " и fremd_status = " + fremd_status + " статус не '" + NameStatus + "'"
            );
        }
    }

    @Issue(value = "TEL-1509")
    @Issue(value = "TEL-1508")
    @Issue(value = "TEL-2101")
    @Link(name = "ТМС-1676", url = "https://team-1okm.testit.software/projects/5/tests/1676?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отображение данных в ВИМИС - СМС в зависимости от фильтра")
    @Description("Авторизация - переход в роли доступа выставление доступа 19/2 и переход в Структурированные медицинские сведения. Проверяем корректное отображение при различных статусах status/fremd_status")
    void Access_1509() throws InterruptedException, SQLException, NullPointerException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        sms = new SMS(driver);
        xml.ApiSmd("SMS/SMS3.xml", "3", 99, 1, true, 1, 1, 9, 18, 1, 57, 21);
        acceessRoles = new AcceessRoles(driver);
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(acceessRoles.RolesWait1);

        System.out.println("Редактирование роли");
        WaitElement(acceessRoles.HeaderRoles);
        while (!isElementNotVisible(acceessRoles.RoleTester)) {
            acceessRoles.Next.click();
        }
        actionElementAndClick(acceessRoles.EditPolny);
        WaitElement(acceessRoles.EditRole);
        WaitElement(acceessRoles.InputWordWait);
        inputWord(acceessRoles.InputWord, "Доступ к структурированным медицинским сведениям по ");
        Thread.sleep(2500);
        if (!isElementNotVisible(acceessRoles.CheckActive("19.3. Доступ к структурированным медицинским сведениям по своей МО"))) {
            ClickElement(acceessRoles.CheckDisable("19.3. Доступ к структурированным медицинским сведениям по своей МО"));
        }
        if (!isElementNotVisible(acceessRoles.CheckActive("19.2. Доступ к структурированным медицинским сведениям по всем МО"))) {
            ClickElement(acceessRoles.CheckDisable("19.2. Доступ к структурированным медицинским сведениям по всем МО"));
        }
        Thread.sleep(1500);
        ClickElement(acceessRoles.UpdateWait);
        Thread.sleep(2500);
        WaitElement(sms.SMSWait);
        actionElementAndClick(sms.SMS);

        System.out.println("Выбор Направления - Иные профили");
        WaitElement(sms.DistrictWait);
        WaitElement(sms.OtherWait);
        sms.Other.click();
        Thread.sleep(1000);

        System.out.println("Выбор Фильтров");
        sms.Filter.click();
        WaitElement(sms.FilterWait);

        System.out.println("Ввод loacl_Uid");
        ClickElement(sms.Ident);
        WaitElement(sms.localUid);
        inputWord(driver.findElement(sms.localUid), "" + xml.uuid + " ");

        System.out.println("Поиск");
        sms.Search.click();
        Thread.sleep(1500);
        if (KingNumber == 4) {
            WaitNotElement3(sms.Loading, 20);
        }
        WaitElement(By.xpath("//tr/td[7]//span/div[contains(.,'" + xml.uuid + "')]"));
        WaitStatusKremd("vimis.remd_sent_result", ""+xml.uuid+"");
        SetStatus("null", "null", "В очереди", false);
        SetStatus("error", "null", "Не принято на регионе", false);
        SetStatus("success", "null", "Принято на регионе", false);
        SetStatus("success", "0", "Не принято ФРЭМД", false);
        SetStatus("success", "1", "Принято ФРЭМД", false);

        System.out.println("Выбор Фильтров");
        sms.Filter.click();
        WaitElement(sms.FilterWait);

        System.out.println("Выбираем Принятые");
        ClickElement(sms.Accepted);

        System.out.println("Поиск");
        sms.Search.click();
        Thread.sleep(1500);
        if (KingNumber == 4) {
            WaitNotElement3(sms.Loading, 20);
        }
        WaitElement(By.xpath("//tr/td[7]//span/div[contains(.,'" + xml.uuid + "')]"));
        WaitElement(sms.ResultSearchStatus);
        Assertions.assertEquals(
                driver.findElement(sms.ResultSearchStatus).getText(), "Принято ФРЭМД",
                "При status = success и fremd_status = 1 статус 'Принято ФРЭМД' не отображается"
        );
        SetStatus("success", "0", "Нет данных", true);
        SetStatus("success", "null", "Нет данных", true);
        SetStatus("error", "null", "Нет данных", true);
        SetStatus("null", "null", "Нет данных", true);

        System.out.println("Выбор Фильтров");
        sms.Filter.click();
        WaitElement(sms.FilterWait);

        System.out.println("Выбираем Не отправленные");
        ClickElement(sms.Accepted);

        System.out.println("Поиск");
        sms.Search.click();
        Thread.sleep(1500);
        if (KingNumber == 4) {
            WaitNotElement3(sms.Loading, 20);
        }
        WaitElement(sms.ResultSearchStatus);
        Assertions.assertEquals(
                driver.findElement(sms.ResultSearchStatus).getText(), "В очереди",
                "При status = null и fremd_status = null статус 'В очереди' не отображается"
        );
        SetStatus("error", "null", "Нет данных", true);
        SetStatus("success", "null", "Нет данных", true);
        SetStatus("success", "0", "Нет данных", true);
        SetStatus("success", "1", "Нет данных", true);
    }
}
