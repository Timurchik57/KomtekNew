package UI.TmTest.AccessUI.Statistick;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import Base.BaseAPI;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
@Tag("Аналитика_МО_ОМП")
@Tag("Отображение_документов_ЛК")
@Tag("Основные")
public class Access_1862Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    DirectionsForQuotas directionsForQuotas;

    public boolean Result(Integer integer) {
        if (integer > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Test
    @Issue(value = "TEL-1862")
    @Issue(value = "TEL-1951")
    @Link(name = "ТМС-1787", url = "https://team-1okm.testit.software/projects/5/tests/1787?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Поиск по документам в ЛК Врача")
    @Description("Переходим в Лк Врача и вбиваем в поиск и смотрим фильтрацию по запросу")
    public void Access_1862() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(analyticsMO.Analytics);
        //Специальный пациент для проверки ЛК, небольшое количество СЭМД
        driver.get(HostAddressWeb + "/registry/patient/2ed97c3b-5f30-4fda-bcdc-39d930c6c224/dashboard");
        WaitElement(analyticsMO.Snils);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 40);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablon, 40);

        List<WebElement> list = driver.findElements(analyticsMO.Docs);
        System.out.println(list.size());
        inputWord(driver.findElement(analyticsMO.Search), "редакцияя");
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 40);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablon, 40);

        List<WebElement> list2 = driver.findElements(analyticsMO.Docs);
        System.out.println(list2.size());

        Integer number = list.size() - list2.size();
        System.out.println(number);
        Assertions.assertEquals(Result(number), true, "Поиск не работает");
    }
}
