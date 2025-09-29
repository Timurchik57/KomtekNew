package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.*;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.List;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Удалённая_консультация")
@Tag("Фильтры_в_направлении")
public class Access_3057Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    ConsultationScheduleRemote consultationSR;
    DirectionsForQuotas directionsForQuotas;
    ConsultationOutgoingUnfinished consultationOU;
    ConsultationOutgoingArchived consultationOA;
    ConsultationArchived consultationA;
    ConsultationUnfinished consultationUnfinished;

    Boolean Outgoing;
    String mo;

    @Issue(value = "TEL-3057")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка поиска косультаций по статусу")
    @Description("Переходим в Созданные консультации - выбираем в фильтре нужный статус - проверяем отображение нужного статуса")
    public void Access_3057() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        consultationSR = new ConsultationScheduleRemote(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);
        consultationOA = new ConsultationOutgoingArchived(driver);
        consultationA = new ConsultationArchived(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);

        AddRole(PRole, "Доступ ко всем направлениям на квоты", true);
        AddRole(PRole, "Доступ ко всем консультациям", true);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);

        Outgoing = false;
        System.out.println("Переходим в входящие незавершённые");
        ClickElement(consultationUnfinished.UnfinishedWait);
        ClickElement(consultationUnfinished.SelectAllClick);
        SearchStatus("Отправлен", "Отправлено");
        SearchStatus("Уточнить", "Запросить доп. информацию");
        SearchStatus("Назначено время", "Назначено время");
        SearchStatus("Назначен борт", "Назначен борт");

        System.out.println("Переходим в входящие архивные");
        ClickElement(consultationA.ArchivedWait);
        ClickElement(consultationA.SelectAllClick);
        SearchStatus("Запрос выполнен", "Выполнено");
        SearchStatus("Отказано", "Отказ в проведении");
        SearchStatus("Эвакуация не выполнена", "Эвакуация не выполнена");
        SearchStatus("Выполнено (передан протокол)", "Выполнено (передан протокол)");
        SearchStatus("Выполнено (Протокол успешно добавлен в РЭМД)", "Выполнено (Протокол успешно добавлен в РЭМД)");
        SearchStatus("Выполнено (Протокол получил ошибку)", "Выполнено (Протокол получил ошибку)");
        SearchStatus("Запрос отменен", "Запрос отменен");

        Outgoing = true;
        System.out.println("Переходим в исходящие незавершённые");
        ClickElement(consultationOU.Consultation);
        ClickElement(consultationOU.SelectAllClick);
        SearchStatus("Отправлен", "Отправлено");
        SearchStatus("Создан", "Создано");
        SearchStatus("Уточнить", "Запросить доп. информацию");
        SearchStatus("Назначено время", "Назначено время");
        SearchStatus("Отправлено в сторонний сервис", "Отправлено в сторонний сервис");

        System.out.println("Переходим в исходящие Архивные");
        ClickElement(consultationOA.OutgoingArchived);
        ClickElement(consultationOA.SelectAllClick);
        SearchStatus("Запрос выполнен", "Выполнено");
        SearchStatus("Выполнено (Протокол получил ошибку)", "Выполнено (Протокол получил ошибку)");
        SearchStatus("Запрос отменен", "Запрос отменен");
        SearchStatus("Отказано", "Отказ в проведении");
        SearchStatus("Эвакуация не выполнена", "Эвакуация не выполнена");
        SearchStatus("Выполнено (передан протокол)", "Выполнено (передан протокол)");
        SearchStatus("Выполнено (Протокол успешно добавлен в РЭМД)", "Выполнено (Протокол успешно добавлен в РЭМД)");
    }

    @Step("Метод для выбора нужного статуса и проверки отображения нужных консультаций")
    public void SearchStatus (String statusWeb, String statusBD) throws InterruptedException, SQLException {
        SelectClickMethod(consultationOU.Status("Статус"), authorizationObject.Select(statusWeb));
        ClickElement(consultationOU.Search);
        Thread.sleep(1500);
        if (isElementVisibleTime(consultationOU.ConsultationFirst, 3)) {

            System.out.println("Выбираем статус " + statusWeb + "");
            List<WebElement> list = driver.findElements(consultationOU.Line(statusWeb));
            List<WebElement> list2 = driver.findElements(consultationOU.ConsultationCountList);
            Assertions.assertEquals(list.size(), list2.size(), "На странице не все консультации со статусом " + statusWeb + "");
            Thread.sleep(1500);
        }

        String countConsul = driver.findElement(consultationOU.ConsultationCount).getText();

        /** consultationpurposeid = 1 - Уточнение диагноза
         *  consultationpurposeid = 2 - Уточнение тактики лечения
         *  consultationpurposeid = 3 - Определение возможности госпитализации в МО
         *  consultationpurposeid = 4 - Очное консультирование
         *  consultationpurposeid = 5 - Подозрение на COVID-19
         *  Outgoing - входящие или исходящие консультации */
        System.out.println("Берём количество консультаций в бд со статусом "+statusBD+"");
        if(Outgoing) {
            mo = "d.requestermoid";
        } else {
            mo = "d.targetmoid";
        }

        if (statusBD.equals("Отправлено") | statusBD.equals("Ошибка") | statusBD.equals("Повторно отправлено")) {
            sql.StartConnection("SELECT count(*) FROM telmed.directions d\n" +
                    "join telmed.statusdirectory s on d.status = s.id \n" +
                    "WHERE s.name = '"+ statusBD +"'\n" +
                    "AND d.directiontype = 2");
        } else {
            sql.StartConnection("SELECT count(*) FROM telmed.directions d\n" +
                    "join telmed.statusdirectory s on d.status = s.id \n" +
                    "left join telmed.doctorslots d2 on d.id = d2.iddirection \n" +
                    "WHERE s.name = '"+ statusBD +"'\n" +
                    "AND d.directiontype = 2");
        }
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }

        Assertions.assertEquals(countConsul.substring(6), sql.value, "Количество всех консультаций со статусом "+statusWeb+" не совпадает с БД");
    }
}
