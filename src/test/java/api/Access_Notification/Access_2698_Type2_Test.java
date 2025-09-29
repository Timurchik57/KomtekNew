package api.Access_Notification;

import Base.TestListener;
import api.Access_1272Test;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Формирование одного колбэка ВИМИС для нескольких направлений тип 2")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Оповещение")
@Tag("Несколько_направлений")
public class Access_2698_Type2_Test extends BaseAPI {

    Access_1272Test access1272Test = new Access_1272Test();
    public String TransferId;

    @Issue(value = "TEL-2698")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Формирование одного колбэка ВИМИС для нескольких направлений vmcl = 1")
    @Description("Отправляем смс по нескольким направлениям, отправляем колбэк ВИМИС и смотрим, что уведомления приходят только по первому стоящему направлению")
    public void Access_1272_Vmcl1 () throws IOException, SQLException, InterruptedException {
        access1272Test.Access_1272Method(1, 3, 2, 3, 3, 2, 4, 2, 5, 3);
        Access_2698Method(1);
    }

    @Test
    @DisplayName("Формирование одного колбэка ВИМИС для нескольких направлений = 2")
    public void Access_1272_Vmcl2 () throws IOException, SQLException, InterruptedException {
        access1272Test.Access_1272Method(2, 3, 1, 3, 3, 2, 4, 2, 5, 3);
        Access_2698Method(2);
    }

    @Test
    @DisplayName("Формирование одного колбэка ВИМИС для нескольких направлений = 3")
    public void Access_1272_Vmcl3 () throws IOException, SQLException, InterruptedException {
        access1272Test.Access_1272Method(3, 2, 1, 3, 2, 3, 4, 2, 5, 3);
        Access_2698Method(3);
    }

    @Test
    @DisplayName("Формирование одного колбэка ВИМИС для нескольких направлений = 4")
    public void Access_1272_Vmcl4 () throws IOException, SQLException, InterruptedException {
        access1272Test.Access_1272Method(4, 2, 1, 3, 2, 3, 3, 2, 5, 3);
        Access_2698Method(4);
    }

    @Test
    @DisplayName("Формирование одного колбэка ВИМИС для нескольких направлений = 5")
    public void Access_1272_Vmcl5 () throws IOException, SQLException, InterruptedException {
        access1272Test.Access_1272Method(5, 3, 1, 3, 2, 3, 3, 2, 4, 2);
        Access_2698Method(5);
    }

    @Step("Метод проверки уведомления типа 2")
    public void Access_2698Method (Integer vmcl) throws SQLException, InterruptedException, IOException {

        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);
        Thread.sleep(1500);
        TableVmcl(vmcl);

        CollbekVimis(ReadPropFile("local_uid_1272"), "1", "Проверка 2698", smsBase, vmcl);
        Thread.sleep(1000);

        System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
        ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
        WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
        NotificationsTrue(2, ReadPropFile("local_uid_1272"), vmcl);
        System.out.println(ReadPropFile("local_uid_1272"));
        System.out.println(TEXT);
        Assertions.assertTrue(
                TEXT.contains("\"LocalUid\":\"" + ReadPropFile("local_uid_1272") + "\""),
                "Оповещение для vmcl = " + vmcl + " не добавилось"
        );
    }
}
