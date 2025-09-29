package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Удалённая_консультация")
@Tag("Проверка_интерфейса")
public class AccessButtonAddTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    ConsultationOutgoingUnfinished consultationOU;
    ConsultationUnfinished consultationUnfinished;

    @Issue(value = "TEL-861")
    @Link(name = "ТМС-1250", url = "https://team-1okm.testit.software/projects/5/tests/1250?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка отображения кнопок")
    @Description("Переходим в Создание консультации - удалённая консультация - добавить файл и в Консультации - Исходящие - незавершённые Выбрать только что созданную консультацию. Проверить отображение кнопки добавления файлов")
    public void AccessTextConfiguringQueue () throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);

        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переходим в Консультации - Создать консультацию - Удалённая консультация");
        ClickElement(directionsForQuotas.ConsultationWait);
        WaitElement(directionsForQuotas.Heading);
        ClickElement(directionsForQuotas.CreateWait);
        ClickElement(directionsForQuotas.RemoteConsultationWait);
        directionsForQuotas.Next.click();
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        WaitElement(directionsForQuotas.PatientDataWait);
        Thread.sleep(1000);
        if (isElementVisibleTime(directionsForQuotas.CitizenshipError, 2)) {
            ClickElement(directionsForQuotas.Citizenship);
            ClickElement(authorizationObject.Select("РОССИЯ Российская Федерация"));
        }
        ClickElement(directionsForQuotas.NextWait);

        System.out.println("Создание направления на удаленную консультацию");
        WaitElement(directionsForQuotas.MOWait);
        Thread.sleep(1500);
        SelectClickMethod(directionsForQuotas.DoctorWait, directionsForQuotas.SelectDoctorFirst);
        ClickElement(directionsForQuotas.MyMO);
        SelectClickMethod(directionsForQuotas.MyDivision, directionsForQuotas.MySelectDivision);
        Thread.sleep(1500);
        if (KingNumber != 4) {
            SelectClickMethod(directionsForQuotas.ProfileWait, directionsForQuotas.SelectProfileFirst);
        } else {
            ClickElement(directionsForQuotas.ProfileWait);
            Thread.sleep(2500);
            ClickElement(directionsForQuotas.SelectProfileFirst);
        }
        ClickElement(directionsForQuotas.Plan);
        SelectClickMethod(directionsForQuotas.Goal, directionsForQuotas.SelectGoal);
        inputWord(driver.findElement(directionsForQuotas.TypeConsul), "конс");
        ClickElement(authorizationObject.Select("Консультация детского уролога-андролога"));
        inputWord(directionsForQuotas.Diagnosis, "A011");
        Thread.sleep(1500);
        ClickElement(directionsForQuotas.SelectProfileFirst);
        if (isElementNotVisible(directionsForQuotas.Text("Жалобы пациента"))) {
            inputWord(driver.findElement(directionsForQuotas.Text("Жалобы пациента")), "йцуу");
            inputWord(driver.findElement(directionsForQuotas.Text("Анамнез")), "йцуу");
            inputWord(driver.findElement(directionsForQuotas.Text("Объективное состояние")), "йцуу");
        }
        directionsForQuotas.NextPatient.click();

        System.out.println("Добавление Файла и проверка соответствия");
        ClickElement(directionsForQuotas.CreateConsul);
        WaitElement(directionsForQuotas.AddFilesWait);
        directionsForQuotas.AddFiles.getAttribute("class").equals(
                "el-button el-button--primary el-button--medium margin-bottom-25");
        File file = new File("src/test/resources/test.txt");
        directionsForQuotas.AddFilesTwo.sendKeys(file.getAbsolutePath());
        Thread.sleep(1000);
        ClickElement(directionsForQuotas.SendConsul);
        Thread.sleep(1000);

        System.out.println("Переходим в Консультации - Исходящие - Незавершённые");
        ClickElement(consultationOU.Consultation);
        ClickElement(consultationOU.ConsultationFirst);
        WaitElement(consultationOU.AddLinkWait);
        consultationOU.AddLink.getAttribute("class").equals(consultationOU.Class);
        consultationOU.AddFile.getAttribute("class").equals(consultationOU.Class);
        consultationOU.Download.getAttribute("class").equals(consultationOU.Class);

        System.out.println("Завершаем консультацию");
        ClickElement(consultationUnfinished.Closed);
        WaitElement(consultationUnfinished.ClosedText);
        inputWord(driver.findElement(consultationUnfinished.ClosedText), "jл");
        SelectClickMethod(consultationUnfinished.DataDay, consultationUnfinished.NextMonth);
        ClickElement(consultationUnfinished.Closed2);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);
        Thread.sleep(1500);
        WaitElement(consultationOU.AddProtWait);
        consultationOU.AddProt.getAttribute("class").equals(consultationOU.Class);
    }

    @Test
    @Issue(value = "TEL-4123")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка отображения нужных диагнозов")
    @Description("Переходим в Создание направления - вводим определённый диагноз и проверяем отображение в селекте")
    public void Access_4123 () throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);

        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переходим в Консультации - Создать консультацию - Удалённая консультация");
        /** Переход в создание консультации на оборудование */
        ClickElement(directionsForQuotas.ConsultationWait);

        System.out.println("Создание консультации");
        WaitElement(directionsForQuotas.Heading);
        WaitElement(directionsForQuotas.CreateWait);
        directionsForQuotas.Create.click();
        WaitElement(directionsForQuotas.TypeConsultationWait);
        directionsForQuotas.DistrictDiagnostic.click();
        WaitElement(directionsForQuotas.DistrictDiagnosticWait);
        directionsForQuotas.Next.click();

        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159-790-257 20");
        WaitElement(directionsForQuotas.PatientDataWait);
        if (isElementVisibleTime(directionsForQuotas.CitizenshipError, 2)) {
            ClickElement(directionsForQuotas.Citizenship);
            ClickElement(authorizationObject.Select("РОССИЯ Российская Федерация"));
        }
        ClickElement(directionsForQuotas.NextWait);
        String[] diagnosis = {
                "M45", "S00.3", "S02.2", "S22.3", "S32.10", "S32.2",
                "S42", "S42.0", "S42.4", "S52.0", "S52", "S52.5",
                "S62.6", "S82.4", "S82.6", "S82.7", "S92.1", "S92.5",
                "S93.4"
        };

        for (int i = 0; i < diagnosis.length; i++) {
            if (i != 0) {
                directionsForQuotas.Diagnosis.clear();
            }
            directionsForQuotas.Diagnosis.sendKeys(diagnosis[i]);
            WaitElement(authorizationObject.Select(diagnosis[i]));
        }
    }
}
