package UI.TmTest.AccessUI;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Cookie;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Проверка HeathCheck без авторизации")
public class HealthChecksTest extends BaseAPI {
    AuthorizationObject authorizationObject;

    @Description("Открываем HeathCheck проверяем отсутствие данных без авторизации. После берём Cookies авторизации и проверяем что данные отображаются")
    @DisplayName("Проверка HeathCheck без авторизации")
    @Test
    public void HealthCheck() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);

        driver.get(HostAddressWeb + "/health");

        /** Метод проверяющий, что все элементы прогрузились */
        WaitNotElement3(authorizationObject.LoadingTrue("1"), 120);
        authorizationObject.WaitIntegrationServicesMethod();
        System.out.println("Страница открывается");

        authorizationObject.GetIntegrationServicesMethod();
        Assertions.assertEquals(authorizationObject.addressTWApi, "");
        Assertions.assertEquals(authorizationObject.addressApi, "");
        Assertions.assertEquals(authorizationObject.addressHubs, "");
        Assertions.assertEquals(authorizationObject.addressUserStatuses, "");
        Assertions.assertEquals(authorizationObject.addressVWAPI, "");
        Assertions.assertEquals(authorizationObject.addressVAPI, "");
        System.out.println("Адреса не отображаются");

        System.out.println("Авторизуемся и берём куки");
        AuthorizationMethod(authorizationObject.OKB);
        Thread.sleep(2000);
        Cookie Session = driver.manage().getCookieNamed(".AspNetCore.Session");
        Cookie TelemedC1 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC1");
        Cookie TelemedC2 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC2");
        Cookie Telemed = driver.manage().getCookieNamed(".AspNet.Core.Telemed");

        System.out.println("Переходим в health, удаляём куки и вставляем новые");
        driver.get(HostAddressWeb + "/health");
        driver.manage().deleteAllCookies();
        driver.manage().addCookie(Session);
        driver.manage().addCookie(TelemedC1);
        driver.manage().addCookie(TelemedC2);
        driver.manage().addCookie(Telemed);

        System.out.println("Проверка, что адреса отображаются");
        authorizationObject.WaitIntegrationServicesMethod();
        authorizationObject.GetIntegrationServicesMethod();
        Assertions.assertNotEquals(authorizationObject.addressTWApi, "");
        Assertions.assertNotEquals(authorizationObject.addressApi, "");
        Assertions.assertNotEquals(authorizationObject.addressHubs, "");
        Assertions.assertNotEquals(authorizationObject.addressUserStatuses, "");
        Assertions.assertNotEquals(authorizationObject.addressVWAPI, "");
        Assertions.assertNotEquals(authorizationObject.addressVAPI, "");
    }
}
