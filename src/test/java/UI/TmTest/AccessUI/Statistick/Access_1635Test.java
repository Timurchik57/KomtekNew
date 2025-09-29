package UI.TmTest.AccessUI.Statistick;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.JavascriptExecutor;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
@Tag("Аналитика_МО_ОМП")
@Tag("Отображение_документов_ЛК")
public class Access_1635Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    public String Host;

    @Test
    @Issue(value = "TEL-1635")
    @Link(name = "ТМС-1726", url = "https://team-1okm.testit.software/projects/5/tests/1726?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение ЛК врача только из РРП")
    @Description("Авторизумся и переходим к пациенту, который есть/нет в РРП")
    public void Access_1635() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(analyticsMO.Analytics);

        System.out.println("Проверяем где есть маршруты, во всех блоках");
        WaitElement(analyticsMO.Tall);
        WaitElement(analyticsMO.Average);
        WaitElement(analyticsMO.low);
        if (KingNumber == 1) {
            Host = "https://tm-test.pkzdrav.ru";
        }
        if (KingNumber == 2) {
            Host = "https://tm-dev.pkzdrav.ru";
        }
        if (KingNumber == 4) {
            Host = "https://remotecons-test.miacugra.ru";
        }

        System.out.println("Переходим к ЛК пациенту, которого нет в РРП");
        driver.get("" + Host + "/registry/patient/7e80a735-6f61-44ec-80a7-356f6144ec7b/dashboard");
        WaitElement(analyticsMO.NotPatient);
        Thread.sleep(3000);

        /** Берём список всех запросов и определяем нужный **/
        String scriptToExecute = "return JSON.stringify(window.performance.getEntries())";
        String netData = ((JavascriptExecutor) driver).executeScript(scriptToExecute).toString();
        Assertions.assertTrue(
                netData.contains("" + Host + "/registry/patient/search?guid=7e80a735-6f61-44ec-80a7-356f6144ec7b"),
                "Нет нужного запроса к пациенту в РРП"
        );

        System.out.println("Переходим к пациенту, который есть в РРП");
        driver.get("" + Host + "/registry/patient/4743e15e-488a-44c6-af50-dff0778dd01a/dashboard");
        WaitNotElement(analyticsMO.NotPatient);
        Thread.sleep(3000);

        /** Берём список всех запросов и определяем нужный **/
        scriptToExecute = "return JSON.stringify(window.performance.getEntries())";
        netData = ((JavascriptExecutor) driver).executeScript(scriptToExecute).toString();
        Assertions.assertTrue(
                netData.contains("" + Host + "/registry/patient/search?guid=4743e15e-488a-44c6-af50-dff0778dd01a"),
                "Нет нужного запроса к пациенту в РРП"
        );
    }
}
