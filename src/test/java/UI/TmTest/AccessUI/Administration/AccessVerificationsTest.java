package UI.TmTest.AccessUI.Administration;

import Base.TestListener;
import UI.TmTest.PageObject.Administration.AcceessRoles;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationScheduleRemote;
import UI.TmTest.PageObject.Directions.HospitalizationSchedule;
import UI.TmTest.PageObject.Statistics.Reports;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Роли_доступа")
@Tag("Редактирование_пользователя")
public class AccessVerificationsTest extends BaseAPI {

    AuthorizationObject authorizationObject;
    AcceessRoles acceessRoles;
    Users users;
    Reports statisticReport;
    ConsultationScheduleRemote consultationSR;
    HospitalizationSchedule hospitalizationSchedule;
    String ACCESS = "Доступ к аналитической отчетности из системы BI";
    String ACCESS1 = "Расписание консультаций";
    String ACCESS2 = "Расписание госпитализаций";

    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка отображения доступа у пользователя")
    @Description("Предоставляем тестовому пользователю определённую доступ, далее авторизуемся через него и проверяемость наличие данного доступа. Убираем доступ и проверяем его отсутствие")
    public void AccessVerification() throws InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        acceessRoles = new AcceessRoles(driver);
        users = new Users(driver);
        statisticReport = new Reports(driver);
        consultationSR = new ConsultationScheduleRemote(driver);
        hospitalizationSchedule = new HospitalizationSchedule(driver);

        /** На ХМАО нет возможности авторизоваться с созданного аккаунта */
        if (KingNumber == 1 | KingNumber == 2) {

            System.out.println("Генерируем снилс для нового пользователя");
            authorizationObject.GenerateSnilsMethod();

            /** Авторизация */
            AuthorizationMethod(authorizationObject.OKB);
            ClickElement(users.UsersWait);
            WaitElement(users.HeaderUsersWait);
            users.AddUser.click();
            users.InputSnils.sendKeys(authorizationObject.GenerationSnils);
            Thread.sleep(1500);
            WaitNotElement3(users.LoadingSnils, 20);
            ClickElement(users.ButtonSearchWait);
            WaitElement(users.SnilsInputWait);
            AddUsersMethod(users.WorkOkb, users.Accounting, users.Role("Тестовая роль"), "");

            System.out.println("Проверяемый СНИЛС: " + authorizationObject.GenerationSnils);

            System.out.println("Редактирование роли - включение доступа");
            AddRole("Тестовая роль","Доступ к аналитической отчетности из системы BI", true);
            AddRole("Тестовая роль","Доступ к разделу \"Расписание консультаций\"", true);
            AddRole("Тестовая роль","Доступ к разделу \"Расписание госпитализаций\"", true);

            System.out.println("Очищаем Cookie");
            driver.manage().deleteAllCookies();

            System.out.println("Авторизуемся под тестовым пользователем");
            driver.get(HostAddressWeb + "/auth/bysnils?snils=" + authorizationObject.SnilsFormat(authorizationObject.GenerationSnils) + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
            Thread.sleep(2000);
            if (isElementNotVisible(authorizationObject.Selected)) {
                authorizationObject.Select.click();
                WaitElement(authorizationObject.SelectWait);
                Thread.sleep(2000);
                actionElementAndClick(authorizationObject.Okb);
                authorizationObject.Сhoose.click();
            }
            if(isElementNotVisible(By.xpath("//h2[contains(.,'Предупреждение')]"))) {
                if(!isElementNotVisible(By.xpath("(//button/span[contains(.,'Ок')])[2]"))) {
                    ClickElement(By.xpath("//button/span[contains(.,'Ок')]"));
                } else {
                    ClickElement(By.xpath("(//button/span[contains(.,'Ок')])[2]"));
                }
            }
            if (isElementNotVisible(authorizationObject.Alert) || isElementNotVisible(authorizationObject.AlertWarning)) {
                ClickElement(authorizationObject.OKWait);
            }
            /** Проверка 1*/
            WaitElement(statisticReport.ReportsWait);
            System.out.println("Ошибки нет - Доступ " + ACCESS + " присутствует");

            /** Проверка 2*/
            WaitElement(consultationSR.ConsultationScheduleremote);
            System.out.println("Ошибки нет - Доступ " + ACCESS1 + " присутствует");

            /** Проверка 3*/
            WaitElement(hospitalizationSchedule.Hospitalization);
            System.out.println("Ошибки нет - Доступ " + ACCESS2 + " присутствует");

            System.out.println("Ввод нужного доступа");
            AddRole("Тестовая роль","Доступ к аналитической отчетности из системы BI", false);
            AddRole("Тестовая роль","Доступ к разделу \"Расписание консультаций\"", false);
            AddRole("Тестовая роль","Доступ к разделу \"Расписание госпитализаций\"", false);

            System.out.println("Очищаем Cookie");
            driver.manage().deleteAllCookies();

            System.out.println("Снова авторизуемся под тестовым пользователем");
            driver.get(HostAddressWeb + "/auth/bysnils?snils=" + authorizationObject.SnilsFormat(authorizationObject.GenerationSnils) + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
            Thread.sleep(2000);

            if (isElementNotVisible(authorizationObject.Selected)) {
                authorizationObject.Select.click();
                WaitElement(authorizationObject.SelectWait);
                Thread.sleep(2000);
                actionElementAndClick(authorizationObject.Okb);
                authorizationObject.Сhoose.click();
            }
            if (isElementNotVisible(authorizationObject.Alert) || isElementNotVisible(authorizationObject.AlertWarning)) {
                ClickElement(authorizationObject.OKWait);
            }
            /** Проверка 4*/
            WaitNotElement(statisticReport.ReportsWait);
            System.out.println("Ошибки нет - Доступ " + ACCESS + " отсутствует");

            /** Проверка 5*/
            WaitNotElement(consultationSR.ConsultationScheduleremote);
            System.out.println("Ошибки нет - Доступ " + ACCESS1 + " отсутствует");

            /** Проверка 6*/
            WaitNotElement(hospitalizationSchedule.Hospitalization);
            System.out.println("Ошибки нет - Доступ " + ACCESS2 + " отсутствует");

            System.out.println("Удаление созданного пользователя");
            users.DeleteUsersMethod(authorizationObject.OKB, authorizationObject.GenerationSnils,
                    users.BRBEditWaitFirst);
        }
    }
}
