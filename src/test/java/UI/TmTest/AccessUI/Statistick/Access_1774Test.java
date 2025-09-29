package UI.TmTest.AccessUI.Statistick;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import Base.BaseAPI;
import io.qameta.allure.*;
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
@Tag("Основные")
public class Access_1774Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    DirectionsForQuotas directionsForQuotas;

    @Test
    @Issue(value = "TEL-1774")
    @Link(name = "ТМС-1768", url = "https://team-1okm.testit.software/projects/5/tests/1768?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Скачивание документа из ЛК Врача")
    @Description("Переходим в Лк Врача и скачиваем любой документ")
    public void Access_1774() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(analyticsMO.Analytics);
        //Специальный пациент для проверки ЛК, небольшое количество СЭМД
        driver.get(HostAddressWeb + "/registry/patient/f316a98f-48f5-4094-9b3d-5cdfe937b12c/dashboard");
        WaitElement(analyticsMO.Snils);
        Thread.sleep(2500);
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 60);
        Thread.sleep(2500);
        WaitNotElement3(analyticsMO.LoadingDocsShablon, 60);

        System.out.println("Нажимаем скачать документ");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView(false);",
                driver.findElement(analyticsMO.LastDocs));
        ClickElement(analyticsMO.Download);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDownload, 60);
        Thread.sleep(1500);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
    }
}
