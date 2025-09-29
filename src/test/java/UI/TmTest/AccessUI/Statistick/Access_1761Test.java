package UI.TmTest.AccessUI.Statistick;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.JavascriptExecutor;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
@Tag("Аналитика_МО_ОМП")
@Tag("Отображение_документов_ЛК")
@Tag("Основные")
public class Access_1761Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    public String Host;

    @Test
    @Issue(value = "TEL-1761")
    @Link(name = "ТМС-1771", url = "https://team-1okm.testit.software/projects/5/tests/1771?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение статуса смс в ЛК Врача")
    @Description("Отправляем смс - добавляеям статус 1 в logs - добавляем статус 1 в remd. Авторизумся и переходим в Лк врача пациента и проверяем, что статус меняется на Опубликовано в ФВИМИС/Опубликовано в ФВИМИС и ФРЭМД")
    public void Access_1761() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);

        System.out.println("Очищаем смс с нужной датой, чтобы не было ошибок, после падения теста");
        sql.UpdateConnection(
                "update vimis.additionalinfo set effectivetime = '2020-08-10 12:47:00.000 +0500' where effectivetime = '2100-08-10 12:47:00.000 +0500';");

        DeleteBD_1761();

        PatientGuid = "fe2405d0-6aca-45b7-ba6f-4e040a190de8";

        System.out.println("Отправляем смс в ВИМИС и РЭМД");
        xml.ApiSmd("SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21);

        System.out.println("Берём id отправленной смс и меняем у неё дату");
        sql.StartConnection(
                "Select * from vimis.sms where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            System.out.println(sql.value);
        }
        sql.UpdateConnection(
                "Update vimis.additionalinfo set effectivetime = '2100-08-10 12:47:00.000 +0500' where smsid = '" + sql.value + "';");

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(analyticsMO.Analytics);
        System.out.println("Выбираем нужного пациента и переходим к нему");

        driver.get(HostAddressWeb + "/registry/patient/" + PatientGuid + "/dashboard");

        System.out.println("Первая проверка - статуса у смс нет");
        WaitNotElement3(analyticsMO.FirstDocsFVimis, 1);

        System.out.println("Устанавливаем status = 1 в vimis.documentlogs");
        CollbekVimis("" + xml.uuid + "", "1", "Проверка 1761", "vimis.sms", 1);
        WaitStatusKremd("vimis.remd_onko_sent_result", "" + xml.uuid + "");

        driver.navigate().refresh();

        System.out.println("Вторая проверка - статус у смс Опубликовано в ФВИМИС");
        WaitElement(analyticsMO.FirstDocsFVimis);

        System.out.println("В таблице vimis.remd_onko_sent_result Создаём запись со статусом 1");
        CollbekKremd("" + xml.uuid + "", "success", "Проверка уведомления 1761", "vimis.remd_onko_sent_result");
        driver.navigate().refresh();

        System.out.println("Третья проверка - статус у смс Опубликовано в ФВИМИС и ФРЭМД");
        WaitElement(analyticsMO.FirstDocsFVimisFremd);
    }

    @Step("Сортируем документы по дате в обратном порядке")
    public void Access_1761Method() throws InterruptedException {
        analyticsMO = new AnalyticsMO(driver);

        WaitElement(analyticsMO.Snils);
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 120);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablon, 120);
        Thread.sleep(1500);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView(false);", driver.findElement(analyticsMO.FederalTrue));
        Thread.sleep(1500);
        ClickElement(analyticsMO.Sort);
        ClickElement(analyticsMO.CreateDate);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 120);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablon, 120);
        jse.executeScript("arguments[0].scrollIntoView(false);", driver.findElement(analyticsMO.FederalTrue));
        ClickElement(analyticsMO.SortUp);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 120);
    }

    public void DeleteBD_1761 () throws SQLException {

        String[] seasons  = new String[] {"vimis.sms", "vimis.preventionsms", "vimis.akineosms", "vimis.cvdsms", "vimis.infectionsms", "vimis.remd_sent_result"};

        for (int i = 0; i < seasons.length; i++) {
            sql.UpdateConnection(
                    "update "+seasons[i]+" set patient_guid = 'fe2405d0-6aca-45b6-ba6f-4e040a190de8' where patient_guid = 'fe2405d0-6aca-45b7-ba6f-4e040a190de8';");
        }
    }
}

