package UI.TmTest.AccessUI.Statistick;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
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
public class Access_1600Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;

    @Test
    @Issue(value = "TEL-1600")
    @Link(name = "ТМС-1729", url = "https://team-1okm.testit.software/projects/5/tests/1729?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Выделение документа при скролле")
    @Description("Переходим в Аналитика Мо по ОМП, выбираем блок где есть маршруты, переходим к пациенту обращаем внимание на документы - при скролле документов справа, они выделяются слева")
    public void Access_1600() throws InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);

        System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(analyticsMO.Analytics);

        System.out.println("Проверяем где есть маршруты, во всех блоках");
        WaitElement(analyticsMO.Tall);
        WaitElement(analyticsMO.Average);
        WaitElement(analyticsMO.low);
        analyticsMO.QuantityStackMethod();
        System.out.println(AnalyticsMO.TallMO);
        System.out.println(AnalyticsMO.AverageMO);
        System.out.println(AnalyticsMO.lowMO);

        System.out.println("Переходим в первый попавшийся блок, у которого есть мо с этапами");
        if (AnalyticsMO.TallMO) {
            ClickElement(analyticsMO.NameMOTallFirst);
        } else {
            if (AnalyticsMO.AverageMO) {
                ClickElement(analyticsMO.NameMOAverageFirst);
            } else {
                ClickElement(analyticsMO.NameMOlowFirst);
            }
        }
        System.out.println("Выбираем первого пациента и переходим к нему");
        ClickElement(analyticsMO.FirstPatient);
        WaitElement(analyticsMO.Snils);
        Thread.sleep(1500);

        System.out.println("Переходим к документам и проверяем сначала, что выделен 1 документ");
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 70);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablon, 70);
        WaitElement(analyticsMO.FirstDocsPattern);
        actions.moveToElement(driver.findElement(analyticsMO.FirstDocsPattern));
        actions.perform();
        WaitElement(analyticsMO.FirstDocsActive);

        System.out.println("Скроллим документы вниз и проверяем, что выделение документов слева меняется");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(analyticsMO.FiveDocsPattern));
        WaitNotElement(analyticsMO.FirstDocsActive);
    }
}
