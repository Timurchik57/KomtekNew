package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Удалённая_консультация")
@Tag("Проверка_интерфейса")
public class AccessNumberCharactersTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    ConsultationUnfinished consultationU;

    @Issue(value = "TEL-536")
    @Link(name = "ТМС-1195", url = "https://team-1okm.testit.software/projects/5/tests/1195?isolatedSection=3f797ff4-168c-4eff-b708-5d08ab80a28e")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка ограничения на количество символов при уточнении консультации")
    @Description("Переходим в Консультации - незавершённые, выбираем со статусом Отправлен, открываем и нажимаем Запросить дополнительную информацию. В поле Причина запроса дополнительной информации, ввести более 1000 символов")
    public void AccessNumberCharacters() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        consultationU = new ConsultationUnfinished(driver);
        AuthorizationMethod(authorizationObject.YATCKIV);

        System.out.println("Переходим в Направления - Консультации - Входящие - Незавершённые");
        WaitElement(consultationU.UnfinishedWait);
        actionElementAndClick(consultationU.Unfinished);
        WaitElement(consultationU.Header);

        System.out.println("Выбираем любой отправленный");
        WaitElement(consultationU.Header);
        WaitElement(consultationU.FirstWait);

        System.out.println("Берём количество записей на странице c \"Отправлено\" и выбираем только те, которые \"Плановые\"");
        List<Integer> QuantityNumbers = new ArrayList<Integer>();
        List<WebElement> quantity = driver.findElements(consultationU.FormWait);
        for (int i = 1; i < quantity.size() + 1; i++) {
            String form = driver.findElement(By.xpath(
                    "(//tbody/tr/td/div/span[contains(.,'Отправлен')]/ancestor::td/following-sibling::td//span)[" + i + "]")).getText();
            if ((form.equals("плановая"))) {
                consultationU.Number = i;
                QuantityNumbers.add(i);
            }
        }
        System.out.println(QuantityNumbers);

        System.out.println("Переходим");
        driver.findElement(By.xpath(
                "(//tbody/tr/td/div/span[contains(.,'Отправлен')]/ancestor::td/following-sibling::td//span)[" + QuantityNumbers.get(
                        0) + "]")).click();
        WaitElement(consultationU.HeaderTwo);

        System.out.println("Нажимаем запросить доп информацию");
        WaitElement(consultationU.InformationWait);
        actionElementAndClick(consultationU.Information);
        WaitElement(consultationU.HeaderInfo);

        System.out.println("Вводим более 1000 символов");
        WaitElement(consultationU.InputWait);
        inputWord(consultationU.Input, consultationU.word);

        System.out.println("Уведомление об ошибке");
        WaitElement(consultationU.Error);
    }
}
