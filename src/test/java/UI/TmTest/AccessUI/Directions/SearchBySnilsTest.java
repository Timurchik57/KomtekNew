package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Консультация_на_оборудование")
@Tag("Заполнение_карточки_пациента")
public class SearchBySnilsTest extends BaseAPI {
    DirectionsForQuotas directionsForQuotas;
    AuthorizationObject authorizationObject;
    String snils;

    @Description("Переход в Направления на квоты и поиск по снилсу пациента, который есть в списке")
    @DisplayName("Поиск пациента по СНИЛС в Направлениях на квоты")
    @Test
    public void SearchBySnils() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        System.out.println("Поиск пациента по СНИЛС в Направлениях на квоты");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переход в Направления на квоты");
        WaitElement(directionsForQuotas.Unfinished);
        actionElementAndClick(directionsForQuotas.Consultation);

        System.out.println("Выбор СНИЛС из списка");
        WaitElement(directionsForQuotas.Heading);
        WaitElement(directionsForQuotas.FirstLine);
        actions.moveToElement(directionsForQuotas.lastLine);
        actions.perform();
        Thread.sleep(1000);
        WaitElement(directionsForQuotas.TableSnilsWait);
        if (!(directionsForQuotas.TableSnils.getText().equals(""))) {
            snils = directionsForQuotas.TableSnils.getText();
            System.out.println("Проверяем этот снилс: " + snils);
        }
        System.out.println(snils);

        System.out.println("Вводим выбранный снилс");
        actions.moveToElement(directionsForQuotas.InputSnils);
        actions.perform();
        Thread.sleep(1000);
        directionsForQuotas.InputSnils.sendKeys(snils);
        directionsForQuotas.Search.click();
        WaitElement(directionsForQuotas.FirstLine);
        Thread.sleep(2000);

        System.out.println("Проверка СНИЛС");
        directionsForQuotas.TableSnils.getText().equals(snils);
    }
}
