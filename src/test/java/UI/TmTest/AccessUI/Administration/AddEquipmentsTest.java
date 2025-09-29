package UI.TmTest.AccessUI.Administration;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.NSI.Equipments;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Консультация_на_оборудование")
@Tag("Расписание_оборудования")
@Tag("Оборудование")
@Tag("Мед_организации")
public class AddEquipmentsTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    MedOrganization medOrganization;
    Equipments equipments;
    EquipmentSchedule equipmentSchedule;
    DirectionsForQuotas directionsForQuotas;

    @Order(1)
    @Description("Добавление организации, для которой будет доступно оборудование. Редактирование самого оборудования, установка максимального веса и выбор исследования")
    @DisplayName("Добавление оборудования")
    @Test
    @Story("Создание консультации на оборудование")
    public void AddEquipment() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        medOrganization = new MedOrganization(driver);
        equipments = new Equipments(driver);

        System.out.println("Определяем параметр по МТБЗ");
        MTBZMethod();

        System.out.println("Добавление оборудования");
        AuthorizationMethod(authorizationObject.YATCKIV);

        System.out.println("Переход в добавление МО, получающей консультацию");
        WaitElement(medOrganization.OrganizationWait);
        actionElementAndClick(medOrganization.Organization);

        System.out.println("Выбор организации для редактирования");
        WaitElement(medOrganization.HeaderOrganizationWait);
        WaitElement(medOrganization.InputOrganizationWait);
        if (KingNumber == 1 | KingNumber == 2) {
            SelectClickMethod(medOrganization.InputOrganizationWait, medOrganization.SelectMO);
        }
        if (KingNumber == 4) {
            SelectClickMethod(medOrganization.InputOrganizationWait, medOrganization.SelectMOKon);
        }
        medOrganization.Search.click();

        System.out.println("Редактирование организации");
        ClickElement(medOrganization.Edit);
        WaitElement(medOrganization.HeaderMOWait);
        if (KingNumber == 4) {
            Thread.sleep(5000);
        }
        ClickElement(medOrganization.RecordingWait);

        System.out.println("Добавление организации для которой будет доступно оборудование");
        WaitElement(medOrganization.GammaCameraWait);
        AddEquipmentsMethod(medOrganization.GammaCameraWait, medOrganization.OKB, medOrganization.OKBTrue);
        WaitElement(medOrganization.KT);
        AddEquipmentsMethod(medOrganization.KT, medOrganization.OKB, medOrganization.OKBTrue);
        WaitElement(medOrganization.MRT);
        AddEquipmentsMethod(medOrganization.MRT, medOrganization.OKB, medOrganization.OKBTrue);
        Thread.sleep(1000);
        WaitElement(medOrganization.UpdateWait);
        actionElementAndClick(medOrganization.Update);
        Thread.sleep(2000);
        if (isElementNotVisible(medOrganization.HeaderMOWait)) {
            if (KingNumber == 2) {
                ClickElement(medOrganization.Close2("3"));
            } else {
                ClickElement(medOrganization.CloseWait);
            }
        }
        System.out.println("МО получающая консультацию добавлена");
        if (KingNumber == 4) {
            Thread.sleep(5000);
        }
        ClickElement(equipments.EquipmentWaitt);

        /** Выбор организации для редактирования */
        WaitElement(equipments.EquipmentWait);
        WaitElement(equipments.HeaderEquipmentWait);
        if (KingNumber == 1 | KingNumber == 2) {
            SelectClickMethod(equipments.InputMoWait, equipments.SelectMO);
            inputWord(equipments.Name, "X-OMATT");
            ClickElement(equipments.CheckIs);
            inputWord(equipments.Description, "1233");
            Thread.sleep(1500);
            WaitNotElement3(equipments.SearchLoading, 20);
            ClickElement(equipments.SearchWait);
            WaitElement(equipments.SearchNameWait);
            ClickElement(equipments.FirstLineSet);
        }
        if (KingNumber == 4) {
            SelectClickMethod(equipments.InputMoWait, equipments.SelectMOKon);
            ClickElement(equipments.CheckIs);
            inputWord(equipments.Description, "1233");
            Thread.sleep(1500);
            WaitNotElement3(equipments.SearchLoading, 20);
            ClickElement(equipments.SearchWait);
            WaitElement(equipments.LineSetXmao);
            ClickElement(equipments.LineSetXmao);
        }

        System.out.println("Поиск нужного оборудования");
        WaitElement(equipments.HeaderEditWait);
        if (!MTBZ) {
            if (KingNumber == 1 | KingNumber == 2) {
                SelectClickMethod(equipments.DivisionWait, equipments.SelectDivision);
                SelectClickMethod(equipments.TypeWait, equipments.SelectType);
                SelectClickMethod(equipments.ModelWait, equipments.SelectModel);
            }
            if (KingNumber == 4) {
                SelectClickMethod(equipments.DivisionWait, equipments.SelectDivisionBuh);
                SelectClickMethod(equipments.TypeWait, equipments.SelectType);
                SelectClickMethod(equipments.ModelWait, equipments.SelectModel);
            }
            inputWord(equipments.Weight, "500");
            Thread.sleep(3000);
            if (isElementNotVisible(equipments.CheckWait)) {
                equipments.Check.click();
            }
        }

        System.out.println("Редактирование Исследования");
        equipments.Researches.click();
        WaitElement(equipments.InputWordWait);
        if (!isElementNotVisible(equipments.ResearchesTrue)) {
            inputWord(equipments.InputWord, "HMH");
            WaitElement(equipments.BottomStartWait);
            WaitElement(equipments.HMP01Wait);
            actionElementAndClick(equipments.HMP01);
            wait.until(invisibilityOfElementLocated(equipments.BottomStartWait));
            equipments.Add.click();
            Thread.sleep(1000);
            equipments.Update.click();
            Thread.sleep(1500);
        } else {
            equipments.Close.click();
        }
        System.out.println("Оборудование отредактировано");
    }

    @Order(2)
    @Test
    @Story("Создание консультации на оборудование")
    @Description("Добавление расписания оборудования. Создание расписания оборудования, которое пересекается с существующим, проверка отображения валидного уведомления")
    @DisplayName("Переход в расписание оборудования и составление расписания")
    public void EquipmentSchedule() throws InterruptedException, SQLException {
        equipmentSchedule = new EquipmentSchedule(driver);
        authorizationObject = new AuthorizationObject(driver);

        System.out.println("Добавление расписания оборудования");
        if (KingNumber == 1 | KingNumber == 2) {
            AuthorizationMethod(authorizationObject.YATCKIV);
            equipmentSchedule.AddEquipmentSchedule(equipmentSchedule.SelectEq("X-OMAT"), equipmentSchedule.Tomorrow, equipmentSchedule.NextMouth,
                    "06:00",
                    "21:00",
                    "30");
        }
        if (KingNumber == 4) {
            AuthorizationMethod(authorizationObject.Kondinsk);
            equipmentSchedule.AddEquipmentSchedule(
                    equipmentSchedule.SelectEq("CDR Kit"), equipmentSchedule.Tomorrow, equipmentSchedule.NextMouth,
                    "06:00",
                    "21:00",
                    "30");
        }
        if (isElementNotVisible(By.xpath("//div[@role='alert']//h2[contains(.,'Успешно')]"))) {
            System.out.println("Новое расписание добавлено");
        } else {
            System.out.println("Проверка уведомления о пересечении расписания");
        }
    }

    @Order(3)
    @Issue(value = "TEL-180")
    @Link(name = "ТМС-959", url = "https://team-1okm.testit.software/projects/5/tests/959?isolatedSection=9087652d-2067-4fe3-9490-66bb61e906ba")
    @Description("Создание консультации. Заполнение информации о направившем враче. Прикрепление файла. Запись на приём. Проверка на то, что не видно отображения Фамилии пациента. Проверка на то, что нельзя записать пациента на занятый слот")
    @DisplayName("Создание консультации на оборудование")
    @Test
    @Story("Создание консультации на оборудование")
    public void ExcessWeight() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);

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

        System.out.println("Создание консультации на оборудование");
        AuthorizationMethod(authorizationObject.OKB);

        /** Метод создания консультации на оборудование и заполнении информации */
        directionsForQuotas.CreateConsultationEquipment(true, Snils,
                authorizationObject.Select("Женская консультация"), "Аорта", false,
                mo,
                authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"),
                authorizationObject.Select("HMP01"), "50", false, true, true, equipment);

        System.out.println("Дополнительная проверка на то, что не видно отображения Фамилии пациента (180)");
        Thread.sleep(1500);
        WaitNotElement(directionsForQuotas.SlotBusyName);
        System.out.println("Фамилия пациента не отображается!");

        System.out.println("Дополнительная проверка на то, что нельзя записать пациента на занятый слот");
        ClickElement(equipmentSchedule.OccupiedSlotWaitt);
        WaitElement(equipmentSchedule.OccupiedSlotWait);
        System.out.println("Пациент не записывается на занятый слот!");
    }

    @Order(4)
    @Description("Создание консультации. Заполнение информации о направившем враче. Ввод веса, который больше допустимого. Прикрепление файла. Запись на приём.")
    @DisplayName("Проверка уведомления о превышении веса")
    @Test
    @Story("Создание консультации на оборудование")
    public void PatientRegistrationEquipment() throws InterruptedException{
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);

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

        System.out.println("Проверка уведомления о превышении веса");
        AuthorizationMethod(authorizationObject.OKB);

        /** Метод создания консультации на оборудование и заполнении информации */
        directionsForQuotas.CreateConsultationEquipment(true, Snils,
                authorizationObject.Select("Женская консультация"), "Аорта", false,
                mo,
                authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"),
                authorizationObject.Select("HMP01"), "99", false, false, false, "");

        WaitElement(directionsForQuotas.CreateDirectionWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.CreateDirection);

        System.out.println("Прикрепление  файла");
        WaitElement(directionsForQuotas.FileWait);
        WaitElement(directionsForQuotas.AddFileWait);
        Thread.sleep(1000);
        File file = new File("src/test/resources/test.txt");
        directionsForQuotas.File.sendKeys(file.getAbsolutePath());
        Thread.sleep(2000);
        ClickElement(directionsForQuotas.ReceptionWait);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(40);
        equipmentSchedule.CheckDirection(equipment);
        ClickElement(equipmentSchedule.SlotsFreeWait);
        if(isElementNotVisible(equipmentSchedule.AddFileWait)) {
            file = new File("src/test/resources/test.txt");
            equipmentSchedule.AddFile.sendKeys(file.getAbsolutePath());
            Thread.sleep(1500);
        }
        WaitElement(equipmentSchedule.AlertErrorWait);
        System.out.println("Предупреждение о превышении веса появилось");
    }
}
