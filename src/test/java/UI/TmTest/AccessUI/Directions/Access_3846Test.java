package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Удалённая_консультация")
@Tag("Карточка_консультации")
@Tag("Проверка_БД")
public class Access_3846Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    ConsultationOutgoingUnfinished consultationOU;

    @Issue(value = "TEL-3846")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Link(name = "ТМС-2272", url = "https://team-1okm.testit.software/projects/5/tests/2272?isolatedSection=d72e058a-4718-4994-b342-ea7fad3fa259")
    @DisplayName("Создание консультации с целью Врач - Врач - Пациент")
    @Description("Создаём консультлацию с целью Врач - Врач - Пациент и проверяем цвет консультации")
    public void Access_3846 () throws Exception {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переходм в создание удалённой консультации");
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);
        directionsForQuotas.CreateRemoteConsul(true, "", "159 790 257 20", "Зотин Андрей Владимирович",
                "Женская консультация", "детской урологии-андрологии", "плановая", "врач-врач-пациент",
                "Паратиф A");
        ClickElement(directionsForQuotas.CloseReception);
        ClickElement(consultationOU.Consultation);
        ClickElement(consultationOU.SortDesc);
        WaitElement(consultationOU.ConsultationFirst);
        String color = driver.findElement(consultationOU.ConsultationFirstColor).getCssValue("background-color");
        System.out.println(color);
        Assertions.assertEquals(color, "rgba(182, 252, 197, 1)", "Цвет не совпадает с зелёным для цели Врач - Врач - Пациент");

        ClickElement(consultationOU.ConsultationFirst);
        WaitElement(consultationOU.Consul("Цель консультации"));
        String complaints = driver.findElement(consultationOU.Consul("Цель консультации")).getText();
        Assertions.assertEquals(complaints, "врач-врач-пациент", "Цель в консультации не совпадает с Врач - Врач - Пациент");
    }
}