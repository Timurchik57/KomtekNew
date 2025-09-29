package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.NSI.DiagnosesOfConsultations;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Удалённая_консультация")
@Tag("Диагнозы_для_консультаций")
@Tag("Основные")
public class Access_2058Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    DiagnosesOfConsultations diagnosesOfCons;
    DirectionsForQuotas directionsForQuotas;

    @Issue(value = "TEL-2058")
    @Link(name = "ТМС-1888", url = "https://team-1okm.testit.software/projects/5/tests/1888?isolatedSection=767caae3-1d53-4e9c-a277-e3122b6d6370")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Ограничение по диагнозам в Удалённой консультации")
    @Description("Добавляем ограничения по консултации и проверяем его при создании консультации")
    public void Access_2058() throws InterruptedException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        diagnosesOfCons = new DiagnosesOfConsultations(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        sql.UpdateConnection("Delete from telmed.consultation_diagnoses where id != 0;");

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Создаём ограничение");
        ClickElement(diagnosesOfCons.DiagnosesOfConsultationslink);
        /** Ограничение Диагноз1 + Профиль1 + Цель1 */
        diagnosesOfCons.AddConsul(
                "Холера, вызванная холерным вибрионом 01, биовар cholerae",
                "БУ ХМАО-Югры \"Окружная клиническая больница\"", "авиационной и космической медицине",
                "Подозрение на COVID-19");

        /** Ограничение Диагноз2 + Профиль1 + Цель1 */
        diagnosesOfCons.AddConsul(
                "Холера неуточненная",
                "БУ ХМАО-Югры \"Окружная клиническая больница\"", "авиационной и космической медицине",
                "Подозрение на COVID-19");

        /** Ограничение Диагноз3 + Профиль1 + Цель2 */
        diagnosesOfCons.AddConsul(
                "Паратиф A",
                "БУ ХМАО-Югры \"Окружная клиническая больница\"", "авиационной и космической медицине",
                "Уточнение диагноза");

        System.out.println("Создание консультации");
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);
        ClickElement(directionsForQuotas.RemoteConsultationWait);
        ClickElement(directionsForQuotas.NextWait);
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        WaitElement(directionsForQuotas.NextWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.Next);

        System.out.println("Заполняем данные");
        SelectClickMethod(directionsForQuotas.DoctorWait, directionsForQuotas.DoctorSearch(PName));
        ClickElement(directionsForQuotas.MyMO);
        SelectClickMethod(directionsForQuotas.MyDivision,
                authorizationObject.Select("Административно-хозяйственный отдел"));

        System.out.println("1 Проверка - отображаются все диагнозы, при Диагноз1 + Профиль2 + Цель2");
        ClickElement(diagnosesOfCons.Select("Профиль"));
        ClickElement(authorizationObject.Select("акушерскому делу"));
        ClickElement(diagnosesOfCons.Select("Цель консультации"));
        ClickElement(authorizationObject.Select("Очное консультирование"));
        inputWord(directionsForQuotas.Diagnos, "  ");
        Thread.sleep(1500);
        WaitElement(authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"));
        WaitElement(authorizationObject.Select("A00.1 Холера, вызванная холерным вибрионом 01, биовар eltor"));

        System.out.println("2 Проверка - отображаются все диагнозы, при Диагноз1 + Профиль1 + Цель2");
        ClickElement(diagnosesOfCons.Select("Профиль"));
        ClickElement(authorizationObject.Select("авиационной и космической медицине"));
        inputWord(directionsForQuotas.Diagnos, "  ");
        Thread.sleep(1500);
        WaitElement(authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"));
        WaitElement(authorizationObject.Select("A00.1 Холера, вызванная холерным вибрионом 01, биовар eltor"));

        System.out.println(
                "3 Проверка - отображаются только выбранные диагнозы в ограничении, при Диагноз3 + Профиль1 + Цель1");
        ClickElement(diagnosesOfCons.Select("Цель консультации"));
        ClickElement(authorizationObject.Select("Подозрение на COVID-19"));
        inputWord(directionsForQuotas.Diagnos, "  ");
        Thread.sleep(1500);
        WaitElement(authorizationObject.Select("Холера неуточненная"));
        WaitElement(authorizationObject.Select("Холера, вызванная холерным вибрионом 01, биовар cholerae"));
        WaitNotElement(authorizationObject.Select("Паратиф A"));

        System.out.println(
                "4 Проверка - отображаются только выбранный диагноз в ограничении 3, при Диагноз3 + Профиль1 + Цель2");
        ClickElement(diagnosesOfCons.Select("Цель консультации"));
        ClickElement(authorizationObject.Select("Уточнение диагноза"));
        inputWord(directionsForQuotas.Diagnos, "  ");
        Thread.sleep(1500);
        WaitElement(authorizationObject.Select("Паратиф A"));
        WaitNotElement(authorizationObject.Select("Холера неуточненная"));
        WaitNotElement(authorizationObject.Select("Холера, вызванная холерным вибрионом 01, биовар cholerae"));
        ClickElement(authorizationObject.Select("Паратиф A"));
        ClickElement(directionsForQuotas.Plan);

        System.out.println("Создаём консультацию и прикрепляем файл");
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

        sql.UpdateConnection("Delete from telmed.consultation_diagnoses where id != 0;");
    }

    @Issue(value = "TEL-2594")
    @Link(name = "ТМС-1964", url = "https://team-1okm.testit.software/projects/5/tests/1964")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Редактирование ограничений диагнозов по консультаций")
    @Description("Добавляем ограничения по консультации и проверяем его при создании консультации, далее редактируем и проверяем псоздание повторно. После удаляем ограничение")
    public void Access_2594() throws InterruptedException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        diagnosesOfCons = new DiagnosesOfConsultations(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        sql.UpdateConnection("Delete from telmed.consultation_diagnoses where id != 0;");

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Создаём ограничение");
        ClickElement(diagnosesOfCons.DiagnosesOfConsultationslink);
        /** Ограничение Диагноз1 + Профиль1 + Цель1 */
        diagnosesOfCons.AddConsul(
                "Холера, вызванная холерным вибрионом 01, биовар cholerae",
                "БУ ХМАО-Югры \"Окружная клиническая больница\"", "авиационной и космической медицине",
                "Подозрение на COVID-19");

        System.out.println("Создание консультации");
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);
        ClickElement(directionsForQuotas.RemoteConsultationWait);
        ClickElement(directionsForQuotas.NextWait);
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        WaitElement(directionsForQuotas.NextWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.Next);

        System.out.println("Заполняем данные");
        SelectClickMethod(directionsForQuotas.DoctorWait, directionsForQuotas.DoctorSearch(PName));
        ClickElement(directionsForQuotas.MyMO);
        SelectClickMethod(directionsForQuotas.MyDivision,
                authorizationObject.Select("Административно-хозяйственный отдел"));

        System.out.println("1 Проверка - отображаются только выбранный диагноз, при Диагноз1 + Профиль1 + Цель1");
        ClickElement(diagnosesOfCons.Select("Профиль"));
        ClickElement(authorizationObject.Select("авиационной и космической медицине"));
        ClickElement(diagnosesOfCons.Select("Цель консультации"));
        ClickElement(authorizationObject.Select("Подозрение на COVID-19"));
        inputWord(directionsForQuotas.Diagnos, "  ");
        Thread.sleep(1500);
        WaitElement(authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"));
        WaitNotElement(authorizationObject.Select("Паратиф A"));

        System.out.println("Переходим в редактирование диагнозов");
        ClickElement(directionsForQuotas.CloseConsul);
        ClickElement(directionsForQuotas.CloseConsulYes);
        ClickElement(diagnosesOfCons.DiagnosesOfConsultationslink);
        ClickElement(diagnosesOfCons.EditLast);
        ClickElement(diagnosesOfCons.Select("Диагнозы"));
        ClickElement(authorizationObject.Select("Холера неуточненная"));
        ClickElement(diagnosesOfCons.AddLimitationHeader);
        ClickElement(diagnosesOfCons.Select("Профиль"));
        ClickElement(authorizationObject.Select("акушерству и гинекологии"));
        ClickElement(diagnosesOfCons.Select("Цель консультации"));
        ClickElement(authorizationObject.Select("Очное консультирование"));
        ClickElement(diagnosesOfCons.Update);
        Thread.sleep(2000);

        System.out.println("Создание консультации");
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);
        ClickElement(directionsForQuotas.RemoteConsultationWait);
        ClickElement(directionsForQuotas.NextWait);
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        WaitElement(directionsForQuotas.NextWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.Next);

        System.out.println("Заполняем данные");
        SelectClickMethod(directionsForQuotas.DoctorWait, directionsForQuotas.DoctorSearch(PName));
        ClickElement(directionsForQuotas.MyMO);
        SelectClickMethod(directionsForQuotas.MyDivision,
                authorizationObject.Select("Административно-хозяйственный отдел"));

        System.out.println("2 Проверка - отображаются два диагноза");
        ClickElement(diagnosesOfCons.Select("Профиль"));
        ClickElement(authorizationObject.Select("акушерству и гинекологии"));
        ClickElement(diagnosesOfCons.Select("Цель консультации"));
        ClickElement(authorizationObject.Select("Очное консультирование"));
        inputWord(directionsForQuotas.Diagnos, "  ");
        Thread.sleep(1500);
        WaitElement(authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"));
        WaitElement(authorizationObject.Select("A00.9 Холера неуточненная"));
        WaitNotElement(authorizationObject.Select("Паратиф A"));
        ClickElement(authorizationObject.Select("A00.9 Холера неуточненная"));
        ClickElement(directionsForQuotas.Plan);

        System.out.println("Создаём консультацию и прикрепляем файл");
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

        sql.UpdateConnection("Delete from telmed.consultation_diagnoses where id != 0;");
    }
}