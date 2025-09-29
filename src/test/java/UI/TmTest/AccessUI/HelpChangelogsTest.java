package UI.TmTest.AccessUI;

import Base.BaseAPI;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Проверка страниц help и changelog")
public class HelpChangelogsTest extends BaseAPI {
    String Test = "https://tm-test.pkzdrav.ru";
    String Dev = "https://tm-dev.pkzdrav.ru";
    String Hmao = "https://remotecons-test.miacugra.ru";
    String URL;
    AuthorizationObject authorizationObject;

    @Description("Переход на страницы help и changelog")
    @DisplayName("Проверка страниц help и changelog")
    @Test
    public void HelpChangelog() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);

        System.out.println("Авторизация и открытие help");
        AuthorizationMethod(authorizationObject.OKB);
        Thread.sleep(1000);
        driver.get(HostAddressWeb + "/help");
        WaitElement(By.xpath("//div[@class='center-block']"));
        System.out.println("help работает");

        System.out.println("Открытие changelog");
        driver.get(HostAddressWeb + "/changelog");
        WaitElement(By.xpath("//div[@class='center-block']"));
        System.out.println("changelog работает");
    }
}
