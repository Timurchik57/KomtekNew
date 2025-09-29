package UI.TmTest.AccessUI.Statistick;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import Base.BaseAPI;
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
public class Access_1649Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    public String Host;

    @Test
    @Issue(value = "TEL-1649")
    @Issue(value = "TEL-1639")
    @Issue(value = "TEL-1749")
    @Issue(value = "TEL-3139")
    @Link(name = "ТМС-1728", url = "https://team-1okm.testit.software/projects/5/tests/1728?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Link(name = "ТМС-1759", url = "https://team-1okm.testit.software/projects/5/tests/1759?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Link(name = "ТМС-1761", url = "https://team-1okm.testit.software/projects/5/tests/1761?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение маршрута у всех диагнозов пациента")
    @Description("Авторизумся и переходим в Лк врача пациента, далее переходим в любой диагноз и проверяем срабатываение метода маршрута - hostaddres/vimis/pmc/patients/stages/route?snils=10071266094&mkb=C64")
    public void Access_1649() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(analyticsMO.Analytics);
        //Специальный пациент для проверки ЛК, небольшое количество СЭМД
        driver.get(HostAddressWeb + "/registry/patient/2ed97c3b-5f30-4fda-bcdc-39d930c6c224/dashboard");
        WaitElement(analyticsMO.Snils);
        String Snils = driver.findElement(analyticsMO.Snils).getText();
        if (KingNumber == 1) {
            Host = "https://tm-test.pkzdrav.ru";
        }
        if (KingNumber == 2) {
            Host = "https://tm-dev.pkzdrav.ru";
        }
        if (KingNumber == 4) {
            Host = "https://remotecons-test.miacugra.ru";
        }

        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 120);

        /** Берём список всех запросов и определяем нужный - проверка из заявки 1639 **/
        String scriptToExecute = "return JSON.stringify(window.performance.getEntries())";
        String netData = ((JavascriptExecutor) driver).executeScript(scriptToExecute).toString();
        Assertions.assertTrue(netData.contains("" + Host + "/vimis/nosologicalpatients/"));

        /** Берём список всех запросов и определяем нужный - проверка из заявки 1749 **/
        Thread.sleep(3000);
        if (KingNumber != 4) {
            Assertions.assertTrue(netData.contains("" + Host + "/vimis/sms/patient?patientGuid="));
        }

        /** Берём список всех запросов и определяем нужный - проверка из заявки 3139 **/
        Assertions.assertFalse(netData.contains("" + Host + "registry/patient/stats/dispensarymonitoring"));

        System.out.println("Переходим к первому диагнозу");
        WaitElement(analyticsMO.FirstDiagnosis);
        String NameDiagnosis = driver.findElement(analyticsMO.FirstDiagnosis).getText();
        ClickElement(analyticsMO.FirstDiagnosis);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingOMP, 40);

        /** Берём список всех запросов и определяем нужный - проверка из заявки 1649 **/
        netData = ((JavascriptExecutor) driver).executeScript(scriptToExecute).toString();
        Assertions.assertTrue(netData.contains(
                        "" + Host + "/vimis/pmc/patients/stages/route?snils=" + Snils + "&mkb=" + NameDiagnosis + ""),
                "Нет нужного запроса к маршруту пациента");
    }
}
