package UI.TmTest.AccessUI;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.Administration.RouteOMP;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import UI.TmTest.PageObject.Directions.HospitalizationSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.VIMIS.ConfiguringQueue;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Проверка заголовков")
public class AccessTextConfiguringQueueTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    ConfiguringQueue configuringQ;
    HospitalizationSchedule hospitalizationS;
    ConsultationUnfinished consultationUnfinished;
    DirectionsForQuotas directionsForQuotas;
    RouteOMP routeOMP;

    @Issue(value = "TEL-860")
    @Issue(value = "TEL-1323")
    @Link(name = "ТМС-1249", url = "https://team-1okm.testit.software/projects/5/tests/1249?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Link(name = "ТМС-1495", url = "https://team-1okm.testit.software/projects/5/tests/1495?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка заголовков")
    @Description("Переходим в различные части удкон и проверяем, что название заголовков не обрезаются")
    public void AccessTextConfiguringQueue() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        configuringQ = new ConfiguringQueue(driver);
        hospitalizationS = new HospitalizationSchedule(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        routeOMP = new RouteOMP(driver);

        System.out.println("Создаём удалённую консультацию");
        AddConsulMetgod();

        System.out.println("Переходим в Настройки очереди и проверяем шрифт");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(configuringQ.QueueWait);
        WaitElement(configuringQ.Header);
        WaitElement(configuringQ.QueueSendingWait);
        configuringQ.QueueSending.getCssValue("font-size").equals("14px!important");

        System.out.println("Переходим в Расписание госпитализаций и проверяем шрифт");
        ClickElement(hospitalizationS.HospitalizationWait);
        ClickElement(hospitalizationS.Settings);
        ClickElement(hospitalizationS.Add);
        WaitElement(hospitalizationS.PeriodWait);
        hospitalizationS.Period.getCssValue("font-size").equals("14px!important");
        ClickElement(hospitalizationS.Close);

        System.out.println("Переходим в Отправленную входящую консультацию - Нажать назначить время и проверяем шрифт");
        ClickElement(consultationUnfinished.UnfinishedWait);
        ClickElement(consultationUnfinished.FirstWait);
        ClickElement(consultationUnfinished.Time);
        ClickElement(consultationUnfinished.AddTime);
        WaitElement(consultationUnfinished.Doctor);
        consultationUnfinished.DoctorSize.getCssValue("font-size").equals("14px!important");
        ClickElement(consultationUnfinished.Close);

        System.out.println("Переходим в Отправленную входящую консультацию - Нажать Завершить консультацию");
        ClickElement(consultationUnfinished.UnfinishedWait);
        ClickElement(consultationUnfinished.FirstWait);
        ClickElement(consultationUnfinished.Closed);
        WaitElement(consultationUnfinished.ClosedText);
        consultationUnfinished.ClosedTimeSize.getCssValue("font-size").equals("14px!important");
        consultationUnfinished.DoctorsSize.getCssValue("font-size").equals("14px!important");
        consultationUnfinished.MESize.getCssValue("font-size").equals("14px!important");
        consultationUnfinished.ConclusionSize.getCssValue("font-size").equals("14px!important");
        ClickElement(consultationUnfinished.Exit);

        System.out.println("Переходим в Администрирование - Настройка маршрутов ОМП - Редактировать/Добавить маршрут");
        ClickElement(routeOMP.RouteOMPWait);
        ClickElement(routeOMP.Add);
        routeOMP.NameMarchSize.getCssValue("font-size").equals("14px!important");
        routeOMP.NameRouteSize.getCssValue("font-size").equals("14px!important");
        routeOMP.RegistershSize.getCssValue("font-size").equals("14px!important");
        routeOMP.OMPSize.getCssValue("font-size").equals("14px!important");
        routeOMP.PodSize.getCssValue("font-size").equals("14px!important");
        routeOMP.NextPodSize.getCssValue("font-size").equals("14px!important");
        routeOMP.NormTimeSize.getCssValue("font-size").equals("14px!important");
        Thread.sleep(1500);
    }

    public void AddConsulMetgod() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        System.out.println("Авторизуемся и переходим в создание удалённой консультации");
        AuthorizationMethod(authorizationObject.YATCKIV);
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);
        ClickElement(directionsForQuotas.RemoteConsultationWait);
        ClickElement(directionsForQuotas.NextWait);
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        WaitElement(directionsForQuotas.PatientDataWait);
        ClickElement(directionsForQuotas.NextWait);
        Thread.sleep(1000);

        System.out.println("Заполняем данные");
        SelectClickMethod(directionsForQuotas.DoctorWait, directionsForQuotas.SelectDoctor);
        if (KingNumber == 1 | KingNumber == 2) {
            SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMOOKB);
        }
        if (KingNumber == 4) {
            SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMOKon);
        }
        SelectClickMethod(directionsForQuotas.ProfileWait, authorizationObject.SelectFirst);
        SelectClickMethod(directionsForQuotas.ProfileWait, authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.Plan);
        SelectClickMethod(directionsForQuotas.Goal, directionsForQuotas.SelectGoal);
        inputWord(directionsForQuotas.Diagnos, "AA");
        Thread.sleep(1000);
        ClickElement(authorizationObject.SelectFirst);
        if (isElementNotVisible(directionsForQuotas.Text("Жалобы пациента"))) {
            inputWord(driver.findElement(directionsForQuotas.Text("Жалобы пациента")), "йцуу");
            inputWord(driver.findElement(directionsForQuotas.Text("Анамнез")), "йцуу");
            inputWord(driver.findElement(directionsForQuotas.Text("Объективное состояние")), "йцуу");
        }
        ClickElement(directionsForQuotas.NextConsul);
        ClickElement(directionsForQuotas.CreateConsul);

        System.out.println("Прикрепление  файла");
        WaitElement(directionsForQuotas.AddFilesWait);
        Thread.sleep(1000);
        File file = new File("src/test/resources/test.txt");
        directionsForQuotas.File.sendKeys(file.getAbsolutePath());
        Thread.sleep(500);
        ClickElement(directionsForQuotas.SendConsul);
        Thread.sleep(2000);
    }
}


