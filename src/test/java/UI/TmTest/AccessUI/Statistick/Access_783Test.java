package UI.TmTest.AccessUI.Statistick;

import Base.BaseAPI;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.JavascriptExecutor;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
@Tag("Аналитика_МО_ОМП")
@Tag("Маршрут_ОМП")
public class Access_783Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;

    @Test
    @Issue(value = "TEL-783")
    @Link(name = "ТМС-1464", url = "https://team-1okm.testit.software/projects/5/tests/1464?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение аналитики по этапам ОМП")
    @Description("Перейти в Статистика - Аналитика МО по ОМП. Проверить Отображение аналитики по этапам ОМП")
    public void Access_783() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);

        SQLAkineo("f316a98f-48f5-4094-9b3d-5cdfe937b12c");

        System.out.println("Авторизуемся и переходим в Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(analyticsMO.Analytics);
        driver.get(HostAddressWeb + "/registry/patient/f316a98f-48f5-4094-9b3d-5cdfe937b12c/dashboard");
        WaitElement(analyticsMO.Snils);
        Thread.sleep(1500);
        ClickElement(analyticsMO.RouteFirst);
        Thread.sleep(2000);
        WaitNotElement3(analyticsMO.LoadingOMP, 40);

        /** Берём список всех запросов и определяем нужный **/
        String scriptToExecute = "return JSON.stringify(window.performance.getEntries())";
        String netData = ((JavascriptExecutor) driver).executeScript(scriptToExecute).toString();
        Assertions.assertEquals(netData.contains("/vimis/pmc/patients/stages/route?snils"), true, "Должен быть переход на маршрут пациента");
    }
}
