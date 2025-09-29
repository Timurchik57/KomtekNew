package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import UI.TmTest.AccessUI.Administration.Access_1898Test;
import UI.TmTest.PageObject.Administration.AcceessRoles;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationArchived;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingArchived;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Directions.Kvots.IncomingArchive;
import UI.TmTest.PageObject.Directions.Kvots.IncomingUnfinished;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Удалённая_консультация")
@Tag("Роли_доступа")
public class Access_3764Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    AcceessRoles acceessRoles;
    DirectionsForQuotas directionsForQuotas;
    ConsultationOutgoingUnfinished consultationOU;
    ConsultationArchived consultationArchived;
    ConsultationUnfinished consultationUnfinished;
    ConsultationOutgoingArchived consultationOutgoingArchived;
    IncomingArchive incomingArchive;
    IncomingUnfinished incomingUnfinished;
    Access_1898Test access1898Test;

    @Issue(value = "TEL-3764")
    @Link(name = "ТМС-3764", url = "https://team-1okm.testit.software/projects/5/tests/3764")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка доступа к санитарным бортам")
    @Description("Проверка отображения санитарных бортов в направлениях и консультациях при включенном/выключенном доступе")
    public void Access_3764() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        acceessRoles = new AcceessRoles(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);
        consultationArchived = new ConsultationArchived(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        consultationOutgoingArchived = new ConsultationOutgoingArchived(driver);
        incomingArchive = new IncomingArchive(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        access1898Test = new Access_1898Test();

        System.out.println("Авторизуемся и переходим в роли доступа");
        AuthorizationMethod(authorizationObject.OKB);

        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                System.out.println("Добавляем доступ к санитарным бортам");
                AddRole(PRole,"Доступ к санитарным бортам на уровне региона", true);
            } else {
                System.out.println("Отключаем доступ к санитарным бортам");
                AddRole(PRole,"Доступ к санитарным бортам на уровне региона", false);
            }

            System.out.println("Проверяем отображение в направлениях на квоты");
            AuthorizationMethod(authorizationObject.OKB);
            ClickElement(directionsForQuotas.ConsultationWait);

            // Проверяем входящие
            ClickElement(incomingUnfinished.ConsultationWait);
            WaitElement(By.xpath("//tbody/tr[1]"));

            // Проверяем незавершенные
            ClickElement(directionsForQuotas.Unfinished);
            WaitElement(By.xpath("//tbody/tr[1]"));

            // Проверяем архивные
            ClickElement(incomingArchive.IncomingArchiveWait);
            WaitElement(By.xpath("//tbody/tr[1]"));

            // Проверяем исходящие консультации
            ClickElement(consultationOU.Consultation);
            WaitElement(consultationOU.ConsultationFirst);

            System.out.println("Проверяем отображение в консультациях");
            // Проверяем входящие
            ClickElement(consultationUnfinished.UnfinishedWait);
            WaitElement(By.xpath("//tbody/tr[1]"));

            // Проверяем незавершенные
            ClickElement(consultationUnfinished.UnfinishedWait);
            WaitElement(By.xpath("//tbody/tr[1]"));

            // Проверяем архивные
            ClickElement(consultationArchived.ArchivedWait);
            WaitElement(By.xpath("//tbody/tr[1]"));

            // Проверяем исходящие архивные
            ClickElement(consultationOutgoingArchived.OutgoingArchived);
            WaitElement(By.xpath("//tbody/tr[1]"));
        }
    }
}
