package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Удалённая_консультация")
@Tag("Проверка_БД")
@Tag("Основные")
public class ChangeConsultationHospitalizationTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    ConsultationUnfinished consultationUn;
    DirectionsForQuotas directionsForQuotas;

    @Test
    @Issue(value = "TEL-495")
    @Link(name = "ТМС-1272", url = "https://team-1okm.testit.software/projects/5/tests/1272?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Смена цели консультации на госпитализацию")
    @Description("Переходим в Направления - Консультации - Незавершённые - Выбираем консультацию со статусом отправлен/уточнить и нажимаем Требуется госпитализация. После проверяем в бд, что у данной консультации статус = 19(В очереди)")
    public void ChangeConsultationHospitalization() throws InterruptedException, SQLException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        consultationUn = new ConsultationUnfinished(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        System.out.println("Авторизуемся и переходим в создание удалённой консультации");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);
        directionsForQuotas.CreateRemoteConsul(false, "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"", "159 790 257 20", PName, "Женская консультация", "детской урологии-андрологии", "плановая", "Очное консультирование", "Паратиф A");
        ClickElement(directionsForQuotas.SendConsul);

        System.out.println("Переход в Направления - Консультации - Незавершённые ");
        if (KingNumber != 4) {
            AuthorizationMethod(authorizationObject.YATCKIV);
        } else {
            AuthorizationMethod(authorizationObject.Kondinsk);
        }
        ClickElement(consultationUn.UnfinishedWait);
        WaitElement(consultationUn.Header);
        WaitElement(consultationUn.FirstWait);

        System.out.println("Берём количество записей на странице c \"Отправлено\" и выбираем только те, которые \"Плановые\"");
        List<Integer> QuantityNumbers = new ArrayList<Integer>();
        List<WebElement> quantity = driver.findElements(consultationUn.FormWait);
        for (int i = 1; i < quantity.size() + 1; i++) {
            String form = driver.findElement(By.xpath(
                    "(//tbody/tr/td/div/span[contains(.,'Отправлен')]/ancestor::td/following-sibling::td//span)[" + i + "]")).getText();
            if ((form.equals("плановая"))) {
                consultationUn.Number = i;
                QuantityNumbers.add(i);
            }
        }
        System.out.println(QuantityNumbers);

        System.out.println("Запоминаем id этой записи");
        String id = driver.findElement(By.xpath(
                "(//tbody/tr/td/div/span[contains(.,'Отправлен')]/ancestor::td/following-sibling::td//span)[" + QuantityNumbers.get(
                        0) + "]/preceding::td[8]")).getText();
        System.out.println(id);

        System.out.println("Переходим и нажимаем Требуется госпитализация");
        driver.findElement(By.xpath(
                "(//tbody/tr/td/div/span[contains(.,'Отправлен')]/ancestor::td/following-sibling::td//span)[" + QuantityNumbers.get(
                        0) + "]")).click();
        WaitElement(consultationUn.RequiredWait);
        actionElementAndClick(consultationUn.Required);
        WaitElement(consultationUn.YesWait);
        consultationUn.Yes.click();
        Thread.sleep(1500);
        String status = null;
        sql.StartConnection("Select d.status from telmed.directions d where id = '" + id + "'");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("status");
            status = sql.value;
        }
        System.out.println(status);
        Assertions.assertEquals(status, "19");
    }
}
