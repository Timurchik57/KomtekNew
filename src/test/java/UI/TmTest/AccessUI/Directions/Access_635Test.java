package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
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
@Feature("Направления")
@Tag("Удалённая_консультация")
public class Access_635Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;

    @Issue(value = "TEL-635")
    @Link(name = "ТМС-1483", url = "https://team-1okm.testit.software/projects/5/tests/1483?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отображение количества пациентов РРП")
    @Description("Переходим в создание консультации и выбираем расширенный поиск, ввести Иванов и проверить отображение количества пациентов в зависимости с параметром 10/20/30")
    public void Access_635() throws  InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        System.out.println("Авторизация и переход в пользователи");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(directionsForQuotas.Unfinished);
        actionElementAndClick(directionsForQuotas.Consultation);

        /** Переход в создание консультации  */
        WaitElement(directionsForQuotas.Unfinished);
        actionElementAndClick(directionsForQuotas.Consultation);

        System.out.println("Создать консультацию - добавить пациента");
        WaitElement(directionsForQuotas.Heading);
        WaitElement(directionsForQuotas.CreateWait);
        directionsForQuotas.Create.click();
        WaitElement(directionsForQuotas.TypeConsultationWait);
        directionsForQuotas.DistrictDiagnostic.click();
        directionsForQuotas.Next.click();
        ClickElement(directionsForQuotas.BigSearchWait);
        WaitElement(directionsForQuotas.LastNameWait);
        inputWord(directionsForQuotas.LastName, "тест ");
        ClickElement(directionsForQuotas.SearchWait("2"));
        Thread.sleep(1500);
        WaitNotElement3(directionsForQuotas.Loading, 30);

        System.out.println("Проверка отображения 10 пациентов");
        List<WebElement> list = driver.findElements(directionsForQuotas.PatientList);
        Assertions.assertEquals(list.size(), 10, "Количество не совпадает с 10");

        System.out.println("Проверка отображения 20 пациентов");
        ClickElement(directionsForQuotas.PatientListNumber);
        ClickElement(directionsForQuotas.PatientListNumber20);
        Thread.sleep(1500);
        WaitNotElement3(directionsForQuotas.Loading, 30);

        list = driver.findElements(directionsForQuotas.PatientList);
        Assertions.assertTrue(list.size() > 10, "Количество не совпадает с 20");

        System.out.println("Проверка отображения 30 пациентов");
        ClickElement(directionsForQuotas.PatientListNumber);
        ClickElement(directionsForQuotas.PatientListNumber30);
        Thread.sleep(1500);
        WaitNotElement3(directionsForQuotas.Loading, 30);

        list = driver.findElements(directionsForQuotas.PatientList);
        Assertions.assertTrue(list.size() > 10, "Количество не совпадает с 30");
    }
}
