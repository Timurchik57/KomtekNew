package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.Administration.AcceessRoles;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Directions.Consultation.QueueHospitalizations;
import UI.TmTest.PageObject.NSI.*;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Проверка_интерфейса")
public class NameFieldsTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    Users users;
    AcceessRoles acceessRoles;
    TypeRegistr typeRegistr;
    MedOrganization medOrganization;
    Equipments equipments;
    QueueHospitalizations queueHosp;
    AnatomicalAreas anatomicalAreas;
    DiagnosticStudies diagnosticS;
    Researches researches;
    StandardsLaboratoryResearch standardsLR;
    CallIndications callIndications;
    DeliveryResult deliveryResult;
    AirAmbulanceAirports airAmbulanceAirports;
    ConsultationUnfinished consultationUnfinished;

    @Issue(value = "TEL-450")
    @Link(name = "ТМС-1054", url = "https://team-1okm.testit.software/projects/5/tests/1054?isolatedSection=ccb1fcf9-9e3b-44d1-9bad-7959d251a43d")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Проверка верного отображения элементов на вебе")
    @DisplayName("Проверка отображения названий полей")
    @Description("Переходим в различные части Удкон и проверяем, что названия полей не обрезаются, отображаются полностью")
    public void NameFields() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        users = new Users(driver);
        acceessRoles = new AcceessRoles(driver);
        typeRegistr = new TypeRegistr(driver);
        medOrganization = new MedOrganization(driver);
        equipments = new Equipments(driver);

        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переходим в Консультации - Создать консультацию - Консультация на диагностику");
        WaitElement(By.linkText("Незавершенные"));
        actionElementAndClick(directionsForQuotas.Consultation);

        System.out.println("Создание консультации");
        WaitElement(directionsForQuotas.Heading);
        WaitElement(directionsForQuotas.CreateWait);
        ClickElement(directionsForQuotas.CreateWait);

        System.out.println("Переходим Создание консультации - Направление на диагностику");
        WaitElement(directionsForQuotas.TypeConsultationWait);
        directionsForQuotas.DistrictDiagnostic.click();
        WaitElement(directionsForQuotas.DistrictDiagnosticWait);
        ClickElement(directionsForQuotas.NextWait);
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        WaitElement(directionsForQuotas.PatientDataWait);
        Thread.sleep(1000);

        System.out.println("Проверка размера шрифта в Направление на диагностику");
        directionsForQuotas.AssertFontSizeMethod(directionsForQuotas.PatientFIO);

        System.out.println("Переходим Создание консультации - Удалённая консультация");
        actionElementAndClick(directionsForQuotas.BackPatient);
        WaitElement(directionsForQuotas.RemoteConsultationWait);
        actionElementAndClick(directionsForQuotas.RemoteConsultation);
        ClickElement(directionsForQuotas.NextWait);
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        WaitElement(directionsForQuotas.PatientDataWait);

        System.out.println("Проверка размера шрифта в Удалённая консультация");
        directionsForQuotas.AssertFontSizeMethod(directionsForQuotas.PatientFIO);

        System.out.println("Переходим в Пользователи");
        actionElementAndClick(directionsForQuotas.ClosePatient);
        WaitElement(directionsForQuotas.ClosePatientWait);
        directionsForQuotas.YesClosePatient.click();
        actionElementAndClick(users.Users);
        WaitElement(users.HeaderUsersWait);
        WaitElement(users.AddUserWait);
        ClickElement(users.AddUserWait);
        users.InputSnils.sendKeys("159-790-257 20");
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 20);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);

        System.out.println("Проверка размера шрифта в Пользователи");
        directionsForQuotas.AssertFontSizeMethod(users.SnilsInputWait);
        actionElementAndClick(users.Close);

        System.out.println("Переходим в Роли доступа");
        acceessRoles.OpenRole();

        System.out.println("Проверка размера шрифта в Роли доступа");
        directionsForQuotas.AssertFontSizeMethod(acceessRoles.NameRole);
        ClickElement(acceessRoles.CloseBy);

        System.out.println("Переходим в Типы Регистров");
        actionElementAndClick(typeRegistr.TypeRegistr);
        WaitElement(typeRegistr.Header);
        typeRegistr.AddRegistr.click();
        WaitElement(typeRegistr.HeaderAddRegistrWait);

        System.out.println("Проверка размера шрифта в Типы Регистров");
        directionsForQuotas.AssertFontSizeMethod(typeRegistr.NameRegistrWait);
        typeRegistr.CloseRegistr.click();

        System.out.println("Переходим в Мед.организации");
        actionElementAndClick(medOrganization.Organization);
        WaitElement(medOrganization.HeaderOrganizationWait);
        ClickElement(medOrganization.Edit);
        WaitElement(medOrganization.HeaderMOWait);

        System.out.println("Проверка размера шрифта в Мед.организации");
        directionsForQuotas.AssertFontSizeMethod(medOrganization.HealthOrganization);
        medOrganization.InfoSystem.click();
        directionsForQuotas.AssertFontSizeMethod(medOrganization.NameSystem);
        medOrganization.CloseInfoSystem.click();

        System.out.println("Переходим в Оборудование");
        actionElementAndClick(equipments.Equipment);
        WaitElement(equipments.HeaderEquipmentWait);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(30);
        ClickElement(equipments.Edit1Wait);
        WaitElement(equipments.HeaderEditWait);

        System.out.println("Проверка размера шрифта в Оборудование");
        directionsForQuotas.AssertFontSizeMethod(equipments.DateInput);
    }

    @Issue(value = "TEL-866")
    @Link(name = "ТМС-1252", url = "https://team-1okm.testit.software/projects/5/tests/1252?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Проверка верного отображения элементов на вебе")
    @DisplayName("Проверка отображения расстояний между элементами")
    @Description("Переходим в различные части Удкон и проверяем, что названия полей не обрезаются, отображаются полностью")
    public void NameFields2() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        queueHosp = new QueueHospitalizations(driver);
        equipments = new Equipments(driver);
        anatomicalAreas = new AnatomicalAreas(driver);
        diagnosticS = new DiagnosticStudies(driver);
        researches = new Researches(driver);
        standardsLR = new StandardsLaboratoryResearch(driver);
        callIndications = new CallIndications(driver);
        deliveryResult = new DeliveryResult(driver);
        airAmbulanceAirports = new AirAmbulanceAirports(driver);
        callIndications = new CallIndications(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);

        System.out.println("Переходим в Консультации - Исходящие - Очередь госпитализаций");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(queueHosp.QueueHospitalizations);
        WaitElement(queueHosp.TableFirstWait);
        if (KingNumber == 1) {

            System.out.println("Находим и нажимаем на Эвакуирован");
            while (!isElementNotVisible(queueHosp.Evacuated)) {
                ClickElement(queueHosp.Next);
                Thread.sleep(1500);
            }
            ClickElement(queueHosp.Evacuated);
            ClickElement(queueHosp.ButtonHospital);

            System.out.println("Факт госпитализации - сравниваем нужные значения");
            WaitElement(queueHosp.FactWait);
            queueHosp.Fact.getCssValue("font-size").equals("18px");
            queueHosp.Date.getCssValue("font-size").equals("14px!important");
            ClickElement(queueHosp.ButtonClose);
            Thread.sleep(1500);
        }
        System.out.println("Направления - Консультации - Входящие - Незавершённые");
        ClickElement(consultationUnfinished.UnfinishedWait);
        if (KingNumber != 4) {

            System.out.println("Переходим в НСИ - Оборудование - Добавить");
            ClickElement(equipments.EquipmentWaitt);
            ClickElement(equipments.AddHeader);
            equipments.NameEquiments.getCssValue("font-size").equals("14px!important");
            equipments.Date.getCssValue("font-size").equals("14px");
            equipments.Close.click();
        }
        System.out.println("Переходим в НСИ - Анатомические области");
        ClickElement(anatomicalAreas.AnatomicalAreas);
        WaitElement(anatomicalAreas.NameWait);
        anatomicalAreas.Name.getCssValue("font-size").equals("13px");
        ClickElement(anatomicalAreas.Edit);
        WaitElement(anatomicalAreas.NameEditWait);
        anatomicalAreas.NameEdit.getCssValue("font-size").equals("14px!important");
        ClickElement(anatomicalAreas.Close);

        System.out.println("Переходим в НСИ - Диагностические исследования");
        ClickElement(diagnosticS.DiagnosticStudies);
        ClickElement(diagnosticS.Add);
        WaitElement(diagnosticS.NameWait);
        diagnosticS.Name.getCssValue("font-size").equals("14px!important");
        ClickElement(diagnosticS.Close);

        System.out.println("Переходим в НСИ - Исследования");
        ClickElement(researches.Researches);
        ClickElement(researches.Add);
        WaitElement(researches.NameWait);
        researches.Name.getCssValue("font-size").equals("14px!important");
        researches.Type.getCssValue("font-size").equals("14px");
        ClickElement(researches.Close);

        System.out.println("Переходим в НСИ - Нормы лабораторных исследований");
        ClickElement(standardsLR.StandardsLR);
        ClickElement(standardsLR.Add);
        WaitElement(standardsLR.NameWait);
        standardsLR.Name.getCssValue("font-size").equals("14px!important");
        ClickElement(standardsLR.Close);

        System.out.println("Переходим в НСИ - Показания вызова санитарной авиации");
        ClickElement(callIndications.CallIndications);
        ClickElement(callIndications.Add);
        WaitElement(callIndications.NameWait);
        callIndications.Name.getCssValue("font-size").equals("14px!important");
        ClickElement(callIndications.Close);

        System.out.println("Переходим в НСИ - Результат доставки");
        ClickElement(deliveryResult.DeliveryResult);
        ClickElement(deliveryResult.Add);
        WaitElement(deliveryResult.NameWait);
        deliveryResult.Name.getCssValue("font-size").equals("14px!important");
        ClickElement(deliveryResult.Close);

        System.out.println("Переходим в НСИ - Аэропорты санитарной авиации");
        ClickElement(airAmbulanceAirports.AirAmbulanceAirports);
        ClickElement(airAmbulanceAirports.Add);
        WaitElement(airAmbulanceAirports.NameWait);
        airAmbulanceAirports.Name.getCssValue("font-size").equals("14px!important");
        ClickElement(airAmbulanceAirports.Close);
    }
}
