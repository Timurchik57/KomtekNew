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
import org.openqa.selenium.JavascriptExecutor;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Консультация_на_оборудование")
@Tag("Заполнение_карточки_пациента")
@Tag("Заполнение_карточки_пациента")
public class Access_1151Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;

    @Test
    @Issue(value = "TEL-1151")
    @Link(name = "ТМС-1428", url = "https://team-1okm.testit.software/projects/5/tests/1428?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Некорректное отображение данных о пациенте при поиске по снилс в РРП")
    @Description("Перейти в Консультации - создать консультацию на диагностику, произвести поиск через Расширенный поиск и без Расширенного одного такого же пользователя, данные должны совпадать")
    public void Access_1151() throws  InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        System.out.println("Авторизация и переход в создание консультации");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);

        System.out.println("Создать консультацию - добавить пациента");
        WaitElement(directionsForQuotas.Heading);
        ClickElement(directionsForQuotas.CreateWait);
        ClickElement(directionsForQuotas.DistrictDiagnosticWait);
        ClickElement(directionsForQuotas.NextWait);
        ClickElement(directionsForQuotas.BigSearchWait);

        System.out.println("Ищем пациента");
        WaitElement(directionsForQuotas.LastNameWait);
        if (KingNumber == 1 | KingNumber == 2) {
            inputWord(directionsForQuotas.LastName, "Ивановв");
        }
        if (KingNumber == 4) {
            inputWord(directionsForQuotas.LastName, "Возраст ");
            inputWord(directionsForQuotas.Name, "Меньше ");
        }
        ClickElement(directionsForQuotas.SearchWait("2"));

        System.out.println("Берём его снилс и ЕНП");
        WaitElement(directionsForQuotas.listPatientFirstnils);
        String snils = driver.findElement(directionsForQuotas.listPatientFirstnils).getText();
        ClickElement(directionsForQuotas.listPatientFirst);
        ClickElement(directionsForQuotas.Choose("1"));

        System.out.println("Проверяем, что выбраны нужные пункты");
        if (KingNumber == 1 | KingNumber == 2) {
            WaitElement(directionsForQuotas.ManWaitTrue);
        }
        if (KingNumber == 4) {
            WaitElement(directionsForQuotas.ManWaitTrue);
        }
        if (isElementNotVisible(directionsForQuotas.VahtTrue)) {
            WaitElement(directionsForQuotas.VahtTrue);
        } else {
            WaitElement(directionsForQuotas.VahtFalse);
        }
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String Type = (String) js.executeScript(
                "return arguments[0].value", driver.findElement(directionsForQuotas.TypeDocument));
        System.out.println(Type);

        System.out.println("Сбрасываем форму и ищем пациента по снилсу без расширенного поиска");
        ClickElement(directionsForQuotas.ResetForm);
        directionsForQuotas.Snils.sendKeys(snils);

        System.out.println("Проверяем, что выбраны нужные пункты");
        if (KingNumber == 1 | KingNumber == 2) {
            WaitElement(directionsForQuotas.ManWaitTrue);
        }
        if (KingNumber == 4) {
            WaitElement(directionsForQuotas.ManWaitTrue);
        }
        if (isElementNotVisible(directionsForQuotas.VahtTrue)) {
            WaitElement(directionsForQuotas.VahtTrue);
        } else {
            WaitElement(directionsForQuotas.VahtFalse);
        }
        String Type2 = (String) js.executeScript(
                "return arguments[0].value", driver.findElement(directionsForQuotas.TypeDocument));
        System.out.println(Type);
        Assertions.assertEquals(Type, Type2, "Документы не совпадают");
    }
}
