package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Onkology.Analytics;
import UI.TmTest.PageObject.Onkology.Screenings;
import UI.TmTest.PageObject.Statistics.StatisticsConsultation;
import UI.TmTest.PageObject.VIMIS.Report;
import UI.TmTest.PageObject.VIMIS.SMS;
import Base.TestListenerApi;
import api.Before.Authorization;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Проверка_интерфейса")
@Tag("Основные")
public class Access_1445Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;
    ConsultationOutgoingUnfinished consultationOutgoingUn;
    Analytics analytics;
    Screenings screenings;
    SMS sms;
    Report report;
    StatisticsConsultation statisticsConsultation;
    ConsultationUnfinished consultationUnfinished;
    Authorization authorization = new Authorization();

    @Issue(value = "TEL-1445")
    @Issue(value = "TEL-1425")
    @Link(name = "ТМС-1536", url = "https://team-1okm.testit.software/projects/5/tests/1536?isolatedSection=aee82730-5a5f-42aa-a904-10b3057df4c4")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Order(1)
    @Story("Переходим в различные части Удкон, проверяем загрузку и скачивание файлов")
    @DisplayName("Проверяем добавление файла при создании консультации на диагностику")
    @Description("Добавляем файл при создании консультации на диагностику, при записи на приём, после записи на приём в направлении формируем направление и протокол. ")
    public void Access_1445_1() throws InterruptedException, SQLException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        consultationOutgoingUn = new ConsultationOutgoingUnfinished(driver);
        analytics = new Analytics(driver);
        screenings = new Screenings(driver);
        sms = new SMS(driver);
        report = new Report(driver);
        statisticsConsultation = new StatisticsConsultation(driver);

        String mo = null;
        String equipment = null;

        if (KingNumber == 1 || KingNumber == 2) {
            mo = "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"";
            equipment = "X-OMAT";
        }
        if (KingNumber == 4) {
            mo = "АУ ХМАО-Югры \"Кондинская районная стоматологическая поликлиника\"";
            equipment = "CDR Kit";
        }

        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("1 Проверка - Проверяем добавление файла при создании консультации на диагностику");
        AddConsulMethod("500", false, directionsForQuotas.SelectResearch, equipment);

        sql.StartConnection("Select * from telmed.directions order by id limit 1");
        while(sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }

        System.out.println("Переходим в Исходящие - Незавершённые - Созданное направление и проверяем скачивание/загрузку файлов");
        ClickElement(directionsForQuotas.ConsultationWait);
        Thread.sleep(1500);
        WaitNotElement3(authorizationObject.LoadingTrue("2"), 30);
        ClickElement(directionsForQuotas.SearchID(sql.value));
        Thread.sleep(1500);
        WaitNotElement3(authorizationObject.LoadingTrue("2"), 30);

        System.out.println("Формируем направление и скачиваем его");
        ClickElement(directionsForQuotas.CreateDirectionLow);
        WaitElement(directionsForQuotas.CreateDirectionAfter);
        ClickElement(directionsForQuotas.DownloadCreateDirection);
        WaitNotElement3(directionsForQuotas.AlertError, 1);

        System.out.println("Скачиваем архивом");
        ClickElement(directionsForQuotas.DownloadArhiv);
        WaitNotElement3(directionsForQuotas.AlertError, 1);

        System.out.println("Сформировать протокол \n");
        ClickElement(directionsForQuotas.CreateProtocol);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        Thread.sleep(2500);
    }

    @Test
    @Order(2)
    @Story("Переходим в различные части Удкон, проверяем загрузку и скачивание файлов")
    @DisplayName("Проверяем добавление файла при создании удалённой консультации")
    public void Access_1445_2() throws InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        consultationOutgoingUn = new ConsultationOutgoingUnfinished(driver);
        analytics = new Analytics(driver);
        screenings = new Screenings(driver);
        sms = new SMS(driver);
        report = new Report(driver);
        statisticsConsultation = new StatisticsConsultation(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);

        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("2 Проверка - Проверяем добавление файла при создании удалённой консультации");
        AddConsultationMethod(true);

        System.out.println("Переходим в Исходящие - Незавершённые - Созданное направление и проверяем скачивание/загрузку файлов");
        ClickElement(consultationOutgoingUn.Consultation);
        ClickElement(consultationOutgoingUn.ConsultationFirst);

        System.out.println("Добавляем файлы");
        ClickElement(consultationOutgoingUn.AddFileWait);
        Thread.sleep(1000);
        File file = new File("src/test/resources/test.txt");
        consultationOutgoingUn.File.sendKeys(file.getAbsolutePath());
        Thread.sleep(1500);
        WaitElement(consultationOutgoingUn.AddFileDownload2);
        ClickElement(consultationOutgoingUn.AddFileClose);

        System.out.println("Скачиваем архив");
        ClickElement(consultationOutgoingUn.DownloadWait);
        WaitNotElement3(directionsForQuotas.AlertError, 1);

        System.out.println("Завершаем консультацию");
        ClickElement(consultationUnfinished.Closed);
        WaitElement(consultationUnfinished.ClosedText);
        inputWord(driver.findElement(consultationUnfinished.ClosedText), "jл");
        SelectClickMethod(consultationUnfinished.DataDay, consultationUnfinished.NextMonth);
        ClickElement(consultationUnfinished.Closed2);
        Thread.sleep(3000);

        System.out.println("Сформировать протокол \n");
        ClickElement(consultationOutgoingUn.AddProtWait);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        WaitElement(consultationOutgoingUn.CreateDirectionAfter);
    }

    @Test
    @Order(3)
    @Story("Переходим в различные части Удкон, проверяем загрузку и скачивание файлов")
    @DisplayName("Проверяем скачивание Excel в Онкология - Аналитика")
    public void Access_1445_3() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        consultationOutgoingUn = new ConsultationOutgoingUnfinished(driver);
        analytics = new Analytics(driver);
        screenings = new Screenings(driver);
        sms = new SMS(driver);
        report = new Report(driver);
        statisticsConsultation = new StatisticsConsultation(driver);

        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("3 Проверка - Проверяем скачивание Excel в Онкология - Аналитика\n");
        ClickElement(analytics.AnalyticsWait);
        ClickElement(analytics.AddExcel);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
    }

    @Test
    @Order(4)
    @Story("Переходим в различные части Удкон, проверяем загрузку и скачивание файлов")
    @DisplayName("Проверяем скачивание Excel в Онкология - Скрининнги")
    public void Access_1445_4() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        consultationOutgoingUn = new ConsultationOutgoingUnfinished(driver);
        analytics = new Analytics(driver);
        screenings = new Screenings(driver);
        sms = new SMS(driver);
        report = new Report(driver);
        statisticsConsultation = new StatisticsConsultation(driver);
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("4 Проверка - Проверяем скачивание Excel в Онкология - Скрининнги\n");
        ClickElement(screenings.ScreeningWait);
        ClickElement(screenings.AddExcel);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
    }

    @Test
    @Order(5)
    @Story("Переходим в различные части Удкон, проверяем загрузку и скачивание файлов")
    @DisplayName("Проверяем скачивание Excel в Вимис - СМС")
    public void Access_1445_5() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        consultationOutgoingUn = new ConsultationOutgoingUnfinished(driver);
        analytics = new Analytics(driver);
        screenings = new Screenings(driver);
        sms = new SMS(driver);
        report = new Report(driver);
        statisticsConsultation = new StatisticsConsultation(driver);
        AuthorizationMethod(authorizationObject.OKB);

        if (KingNumber != 4) {
            System.out.println("5 Проверка - Проверяем скачивание Excel в Вимис - СМС\n");
            ClickElement(sms.SMSWait);

            if (KingNumber == 4) {
                Thread.sleep(5000);
            }
            ClickElement(sms.OncologyWait);
            ClickElement(sms.FilterWaitAdd);
            SelectClickMethod(sms.BeforeTimeWait, sms.DataToDay);
            ClickElement(sms.SearchWait);
            Thread.sleep(2000);
            WaitNotElement3(sms.Loading, 30);
            ClickElement(sms.AddExcel);
            WaitNotElement3(sms.ExcelLoading, 30);
            WaitNotElement3(directionsForQuotas.AlertError, 1);
            Thread.sleep(2000);
            WaitNotElement3(sms.Loading, 50);
            WaitNotElement3(directionsForQuotas.AlertError, 1);
            Thread.sleep(2500);
            ClickElement(sms.SMSWait);

            ClickElement(sms.PreventionWait);
            ClickElement(sms.FilterWaitAdd);
            SelectClickMethod(sms.BeforeTimeWait, sms.DataToDay);
            ClickElement(sms.SearchWait);
            Thread.sleep(2000);
            WaitNotElement3(sms.Loading, 30);
            ClickElement(sms.AddExcel);
            WaitNotElement3(sms.ExcelLoading, 30);
            WaitNotElement3(directionsForQuotas.AlertError, 1);
            WaitNotElement3(sms.Loading, 30);
            Thread.sleep(2000);
            ClickElement(sms.SMSWait);

            ClickElement(sms.AkineoWait);
            ClickElement(sms.FilterWaitAdd);
            SelectClickMethod(sms.BeforeTimeWait, sms.DataToDay);
            ClickElement(sms.SearchWait);
            Thread.sleep(2000);
            WaitNotElement3(sms.Loading, 30);
            ClickElement(sms.AddExcel);
            WaitNotElement3(sms.ExcelLoading, 30);
            WaitNotElement3(directionsForQuotas.AlertError, 1);
            WaitNotElement3(sms.Loading, 30);
            Thread.sleep(2000);
            ClickElement(sms.SMSWait);

            ClickElement(sms.SSZWait);
            ClickElement(sms.FilterWaitAdd);
            SelectClickMethod(sms.BeforeTimeWait, sms.DataToDay);
            ClickElement(sms.SearchWait);
            Thread.sleep(2000);
            WaitNotElement3(sms.Loading, 30);
            ClickElement(sms.AddExcel);
            WaitNotElement3(sms.ExcelLoading, 30);
            WaitNotElement3(directionsForQuotas.AlertError, 1);
            Thread.sleep(2000);
            WaitNotElement3(sms.Loading, 30);
        }
    }

    @Test
    @Order(6)
    @Story("Переходим в различные части Удкон, проверяем загрузку и скачивание файлов")
    @DisplayName("Проверяем скачивание Excel в Вимис - Отчёты - СМС")
    public void Access_1445_6() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        consultationOutgoingUn = new ConsultationOutgoingUnfinished(driver);
        analytics = new Analytics(driver);
        screenings = new Screenings(driver);
        sms = new SMS(driver);
        report = new Report(driver);
        statisticsConsultation = new StatisticsConsultation(driver);
        AuthorizationMethod(authorizationObject.OKB);

        if (KingNumber != 4) {
            System.out.println("6 Проверка - Проверяем скачивание Excel в Вимис - Отчёты - СМС\n");
            ClickElement(report.ReportWait);
            SelectClickMethod(report.Direction, report.Other);
            ClickElement(report.PeriodWait);
            if (isElementNotVisible(report.PeriodEnd)) {
                ClickElement(report.PeriodEnd);
                Thread.sleep(1500);
                if (isElementNotVisible(report.PeriodStartEnd)) {
                    ClickElement(report.PeriodStartEnd);
                } else {
                    ClickElement(report.PeriodStart);
                }
            } else {
                ClickElement(report.PeriodStartEnd);
                Thread.sleep(1500);
                if (isElementNotVisible(report.PeriodStartEnd)) {
                    ClickElement(report.PeriodStartEnd);
                } else {
                    ClickElement(report.PeriodStart);
                }
            }
            ClickElement(report.SearchWait);
            if (!isElementVisibleTime(report.NoData("1"), 3)) {
                Thread.sleep(1500);
                WaitNotElement3(report.AddExcelDisabled, 30);
                ClickElement(report.AddExcel);
                WaitNotElement3(directionsForQuotas.AlertError, 1);
            }
            Thread.sleep(2500);
            SelectClickMethod(report.Direction, report.Oncology);
            ClickElement(report.SearchWait);
            if (!isElementVisibleTime(report.NoData("1"), 3)) {
                Thread.sleep(1500);
                WaitNotElement3(report.AddExcelDisabled, 30);
                ClickElement(report.AddExcel);
                WaitNotElement3(directionsForQuotas.AlertError, 1);
            }
            Thread.sleep(1500);
            SelectClickMethod(report.Direction, report.Prevention);
            ClickElement(report.SearchWait);
            if (!isElementVisibleTime(report.NoData("1"), 3)) {
                Thread.sleep(1500);
                WaitNotElement3(report.AddExcelDisabled, 30);
                ClickElement(report.AddExcel);
                WaitNotElement3(directionsForQuotas.AlertError, 1);
            }
            Thread.sleep(1500);
            SelectClickMethod(report.Direction, report.Akineo);
            ClickElement(report.SearchWait);
            if (!isElementVisibleTime(report.NoData("1"), 3)) {
                Thread.sleep(1500);
                WaitNotElement3(report.AddExcelDisabled, 30);
                ClickElement(report.AddExcel);
                WaitNotElement3(directionsForQuotas.AlertError, 1);
            }
            Thread.sleep(1500);
            SelectClickMethod(report.Direction, report.SSZ);
            ClickElement(report.SearchWait);
            if (!isElementVisibleTime(report.NoData("1"), 3)) {
                Thread.sleep(1500);
                WaitNotElement3(report.AddExcelDisabled, 30);
                ClickElement(report.AddExcel);
                WaitNotElement3(directionsForQuotas.AlertError, 1);
                Thread.sleep(1500);
            }
        }
    }

    @Test
    @Order(7)
    @Story("Переходим в различные части Удкон, проверяем загрузку и скачивание файлов")
    @DisplayName("Проверяем скачивание Excel в Вимис - Отчёты - Отчёт по статусам")
    public void Access_1445_7() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        consultationOutgoingUn = new ConsultationOutgoingUnfinished(driver);
        analytics = new Analytics(driver);
        screenings = new Screenings(driver);
        sms = new SMS(driver);
        report = new Report(driver);
        statisticsConsultation = new StatisticsConsultation(driver);
        AuthorizationMethod(authorizationObject.OKB);

        if (KingNumber != 4) {
            System.out.println("7 Проверка - Проверяем скачивание Excel в Вимис - Отчёты - Отчёт по статусам\n");
            ClickElement(report.ReportWait);
            ClickElement(report.ReportInStatusWait);
            Thread.sleep(1500);
            WaitNotElement3(report.StatusDirectionDisabled, 30);
            SelectClickMethod(report.StatusDirection, report.Onko);
            System.out.println("Выбираем Период");
            ClickElement(report.StatusPeriodWait);
            if (isElementNotVisible(report.PeriodEnd)) {
                ClickElement(report.PeriodEnd);
                Thread.sleep(1500);
                if (isElementNotVisible(report.PeriodStartEnd)) {
                    ClickElement(report.PeriodStartEnd);
                } else {
                    ClickElement(report.PeriodStart);
                }
            } else {
                ClickElement(report.PeriodStartEnd);
                Thread.sleep(1500);
                if (isElementNotVisible(report.PeriodStartEnd)) {
                    ClickElement(report.PeriodStartEnd);
                } else {
                    ClickElement(report.PeriodStart);
                }
            }
            ClickElement(report.StatusSearchWait);
            Thread.sleep(1500);
            WaitNotElement3(report.AddExcel2Disabled, 30);
            ClickElement(report.AddExcel2);
            WaitNotElement3(directionsForQuotas.AlertError, 1);
            Thread.sleep(1500);
            WaitNotElement3(report.StatusDirectionDisabled, 30);
            SelectClickMethod(report.StatusDirection, report.Prev);
            ClickElement(report.StatusSearchWait);
            Thread.sleep(1500);
            WaitNotElement3(report.AddExcel2Disabled, 30);
            ClickElement(report.AddExcel2);
            WaitNotElement3(directionsForQuotas.AlertError, 1);
            Thread.sleep(1500);
            WaitNotElement3(report.StatusDirectionDisabled, 30);
            SelectClickMethod(report.StatusDirection, report.akineo);
            ClickElement(report.StatusSearchWait);
            Thread.sleep(1500);
            WaitNotElement3(report.AddExcel2Disabled, 30);
            ClickElement(report.AddExcel2);
            WaitNotElement3(directionsForQuotas.AlertError, 1);
            Thread.sleep(1500);
            WaitNotElement3(report.StatusDirectionDisabled, 30);
            SelectClickMethod(report.StatusDirection, report.ssz);
            ClickElement(report.StatusSearchWait);
            Thread.sleep(1500);
            WaitNotElement3(report.AddExcel2Disabled, 30);
            ClickElement(report.AddExcel2);
            WaitNotElement3(directionsForQuotas.AlertError, 1);
            Thread.sleep(1500);
        }
    }

    @Test
    @Order(8)
    @Story("Переходим в различные части Удкон, проверяем загрузку и скачивание файлов")
    @DisplayName("Проверяем скачивание Excel в Статистика - Статистика по консультациям")
    public void Access_1445_8() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        consultationOutgoingUn = new ConsultationOutgoingUnfinished(driver);
        analytics = new Analytics(driver);
        screenings = new Screenings(driver);
        sms = new SMS(driver);
        report = new Report(driver);
        statisticsConsultation = new StatisticsConsultation(driver);
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("8 Проверка - Проверяем скачивание Excel в Статистика - Статистика по консультациям \n");
        ClickElement(statisticsConsultation.ConsultationWait);
        ClickElement(statisticsConsultation.SearchWait1);
        ClickElement(statisticsConsultation.AddExcel);
        ClickElement(statisticsConsultation.AddExcelYes);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        ClickElement(statisticsConsultation.HeaderReportWait);
        ClickElement(statisticsConsultation.DateStartWait);
        ClickElement(statisticsConsultation.FirstMonth);
        ClickElement(statisticsConsultation.ToDay);
        ClickElement(statisticsConsultation.SearchWait);
        ClickElement(statisticsConsultation.AddExcel2);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
    }

    @Step("Метод для создания направления на диагностику с записью на приём и прикреплением файлов")
    public void AddConsulMethod(String Mass, Boolean MyMO, By Research, String equipment) throws InterruptedException{
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);

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
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        WaitElement(directionsForQuotas.PatientDataWait);
        WaitElement(directionsForQuotas.NextWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.Next);

        System.out.println("Заполнение информации о направившем враче");
        WaitElement(directionsForQuotas.InfoDoctorWait);
        WaitElement(directionsForQuotas.SubmittingDoctorWait);
        Thread.sleep(2000);
        SelectClickMethod(directionsForQuotas.SubmittingDoctorWait, directionsForQuotas.FIO);
        Thread.sleep(1500);
        SelectClickMethod(directionsForQuotas.Division, directionsForQuotas.SelectDivision);
        Thread.sleep(1500);
        WaitNotElement3(directionsForQuotas.LoadingDepartment, 30);
        SelectClickMethod(directionsForQuotas.DepartmentWait, directionsForQuotas.SelectDepartment);
        Thread.sleep(2000);
        SelectClickMethod(directionsForQuotas.Post, directionsForQuotas.SelectPost);
        Thread.sleep(1500);
        SelectClickMethod(directionsForQuotas.Specialization, directionsForQuotas.SelectSpecializationFirst);
        Thread.sleep(1500);
        driver.findElement(directionsForQuotas.AnatomicalAreas).sendKeys("ис");
        ClickElement(authorizationObject.Select("Височная кость"));
        if (MyMO) {
            ClickElement(directionsForQuotas.MyMODirection);
            Thread.sleep(2000);
            SelectClickMethod(directionsForQuotas.MyDivision, directionsForQuotas.MySelectDivision);
        }
        if (!MyMO) {
            if (KingNumber == 1 | KingNumber == 2) {
                SelectClickMethod(directionsForQuotas.MODirection, directionsForQuotas.SelectMODirection);
            }
            if (KingNumber == 4) {
                SelectClickMethod(directionsForQuotas.MODirection, directionsForQuotas.SelectMODirectionKon);
            }
        }
        directionsForQuotas.Diagnosis.sendKeys(Keys.SPACE);
        WaitElement(authorizationObject.BottomStart);
        Thread.sleep(1000);
        ClickElement(directionsForQuotas.SelectDiagnosisWait);
        SelectClickMethod(directionsForQuotas.Research, Research);
        SelectClickMethod(directionsForQuotas.BenefitCode, directionsForQuotas.SelectBenefitCode);
        WaitElement(directionsForQuotas.MassWait);
        inputWord(directionsForQuotas.Mass, Mass);
        System.out.println("Вес пациента " + Mass);
        inputWord(directionsForQuotas.NamePatient, "Тестовыйй");
        inputWord(directionsForQuotas.LastNamePatient, "Тестт");
        inputWord(directionsForQuotas.MiddleNamePatient, "Тестовичч");
        actionElementAndClick(directionsForQuotas.NextPatient);

        /** Окно направления на диагностику*/
        WaitElement(directionsForQuotas.Header);
        WaitElement(directionsForQuotas.CreateDirectionWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.CreateDirection);

        System.out.println("Прикрепление  файла");
        WaitElement(directionsForQuotas.FileWait);
        WaitElement(directionsForQuotas.AddFileWait);
        Thread.sleep(1000);
        java.io.File file = new File("src/test/resources/test.txt");
        directionsForQuotas.File.sendKeys(file.getAbsolutePath());
        Thread.sleep(500);
        WaitElement(directionsForQuotas.Download);
        WaitElement(directionsForQuotas.ReceptionWait);
        directionsForQuotas.Reception.click();
        Thread.sleep(1500);

        System.out.println("Запись на приём");
        authorizationObject.LoadingTime(40);
        equipmentSchedule.CheckDirection(equipment);
        ClickElement(equipmentSchedule.SlotsFreeWait);

        System.out.println("Выбрали свободный слот");
        WaitElement(equipmentSchedule.WriteWait);
        if (KingNumber == 1 | KingNumber == 2) {
            WaitElement(equipmentSchedule.AddFileWait);
            file = new File("src/test/resources/test.txt");
            equipmentSchedule.AddFile.sendKeys(file.getAbsolutePath());
            WaitNotElement3(directionsForQuotas.AlertError, 1);
            equipmentSchedule.AddFile2.sendKeys(file.getAbsolutePath());
            WaitNotElement3(directionsForQuotas.AlertError, 1);
            equipmentSchedule.AddFile3.sendKeys(file.getAbsolutePath());
            WaitNotElement3(directionsForQuotas.AlertError, 1);
            WaitElement(equipmentSchedule.FileWait1);
            WaitElement(equipmentSchedule.FileWait2);
            WaitElement(equipmentSchedule.FileWait3);
            Thread.sleep(1500);
            WaitNotElement3(equipmentSchedule.loadingWriteTwo, 30);
            ClickElement(equipmentSchedule.WriteTwo);
        }
        if (KingNumber == 4) {
            Thread.sleep(1500);
            WaitNotElement3(equipmentSchedule.loadingWriteTwo, 30);
            ClickElement(equipmentSchedule.WriteTwo);
        }
        Thread.sleep(1500);
        WaitNotElement3(equipmentSchedule.WriteLoading, 30);
        ClickElement(equipmentSchedule.AlertCloseWait);
    }

    @Step("Метод для создания удалённой консультации")
    public void AddConsultationMethod(Boolean MyMO) throws InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);

        /** Переход в создание консультации на оборудование */
        ClickElement(directionsForQuotas.ConsultationWait);

        System.out.println("Создание консультации");
        ClickElement(directionsForQuotas.CreateWait);

        directionsForQuotas.CreateRemoteConsul(false, "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"", "159 790 257 20", PName, "Женская консультация", "детской урологии-андрологии", "плановая", "Очное консультирование", "Паратиф A");
        WaitElement(directionsForQuotas.Download);
        Thread.sleep(500);
        ClickElement(directionsForQuotas.SendConsul);
        Thread.sleep(2000);
        if (KingNumber == 4) {
            Thread.sleep(2000);
        }
    }
}
