package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Консультация_на_оборудование")
@Tag("Заполнение_карточки_пациента")
public class Access_1198Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;

    @Issue(value = "TEL-1198")
    @Link(name = "ТМС-1500", url = "https://team-1okm.testit.software/projects/5/tests/1500?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Максимальное количество символов для \"Место работы\"")
    @Description("Перейти в Консультации - Создать направление - Направление на диагностику - Проверяем максимальное количество символов у Место работы")
    public void Access_1198() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переход в создание консультации на оборудование");
        ClickElement(directionsForQuotas.ConsultationWait);

        System.out.println("Создание консультации");
        ClickElement(directionsForQuotas.CreateWait);

        System.out.println("Выбираем консультацию");
        ClickElement(directionsForQuotas.DistrictDiagnosticWait);
        ClickElement(directionsForQuotas.NextWait);
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        Thread.sleep(1500);

        System.out.println("Нажимаем на Информация о работе");
        ClickElement(directionsForQuotas.Jobinfo);

        System.out.println("Вводим более 100 символов и проверяем ошибку");
        inputWord(directionsForQuotas.Job,
                "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        WaitElement(directionsForQuotas.JobErrorSimvol);

        System.out.println("Вводим 100 символов и проверяем отсутствие ошибки");
        inputWord(directionsForQuotas.Job,
                "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        directionsForQuotas.PostJob.click();
        WaitNotElement(directionsForQuotas.JobErrorSimvol);

        System.out.println("Вводим менее 100 символов и проверяем отсутствие ошибки");
        inputWord(directionsForQuotas.Job,
                "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        directionsForQuotas.PostJob.click();
        WaitNotElement(directionsForQuotas.JobErrorSimvol);

        System.out.println("Вводим более 100 символов и проверяем ошибку");
        inputWord(directionsForQuotas.PostJob,
                "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        WaitElement(directionsForQuotas.JobErrorSimvol);

        System.out.println("Вводим 100 символов и проверяем отсутствие ошибки");
        inputWord(directionsForQuotas.PostJob, "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        WaitNotElement(directionsForQuotas.JobErrorSimvol);

        System.out.println("Вводим менее 100 символов и проверяем отсутствие ошибки");
        inputWord(directionsForQuotas.PostJob, "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        WaitNotElement(directionsForQuotas.JobErrorSimvol);
    }
}
