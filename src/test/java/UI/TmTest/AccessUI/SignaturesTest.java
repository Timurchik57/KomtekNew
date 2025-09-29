package UI.TmTest.AccessUI;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Onkology.Analytics;
import UI.TmTest.PageObject.Onkology.Screenings;
import UI.TmTest.PageObject.Registry.RegisterDispensaryPatients;
import UI.TmTest.PageObject.Registry.RegistrPatients;
import UI.TmTest.PageObject.Registry.RegistryZNO;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Единообразие подписи полей фильтрации на интерфейсе")
public class SignaturesTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    Screenings screenings;
    Analytics analytics;
    RegistrPatients registrP;
    RegistryZNO registryZNO;
    EquipmentSchedule equipmentS;
    DirectionsForQuotas directionsFQ;
    ConsultationUnfinished consultationUn;
    RegisterDispensaryPatients registerDP;

    @Description("Проверка единообразия подписи полей фильтрации на интерфейсе во всех вкладках. Проверяет нижний отступ от Наименования поля поиска. Проверка на расстояние между полями.")
    @DisplayName("Единообразие подписи полей фильтрации на интерфейсе")
    @Test
    public void Signature() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        screenings = new Screenings(driver);
        analytics = new Analytics(driver);
        registrP = new RegistrPatients(driver);
        registryZNO = new RegistryZNO(driver);
        equipmentS = new EquipmentSchedule(driver);
        directionsFQ = new DirectionsForQuotas(driver);
        consultationUn = new ConsultationUnfinished(driver);
        System.out.println("Авторизация");
        AuthorizationMethod(authorizationObject.OKB);
        /** Переход в Скрининги */
        System.out.println("Переход в Скрининги");
        WaitElement(screenings.ScreeningWait);
        actionElementAndClick(screenings.Screening);
        AssertSignatures(screenings.MO);
        AssertSignatures(screenings.Period);
        AssertSignatures(screenings.Type);
        AssertSignaturesButtonRightAndLeft(screenings.DistanceMO);
        AssertSignaturesButtonRightAndLeft(screenings.DistancePeriod);
        AssertSignaturesButtonRightAndLeft(screenings.DistanceType);
        AssertSignaturesButtonRight10(screenings.Search);
        AssertSignaturesButtonRight10(screenings.DistanceSearch);
        /** Переход в Аналитика */
        System.out.println("Переход в Аналитика");
        actionElementAndClick(analytics.Analytics);
        AssertSignatures(analytics.MO);
        AssertSignatures(analytics.Period);
        AssertSignatures(analytics.Type);
        AssertSignaturesButtonRightAndLeft(analytics.DistanceMO);
        AssertSignaturesButtonRightAndLeft(analytics.DistancePeriod);
        AssertSignaturesButtonRightAndLeft(analytics.DistanceType);
        AssertSignaturesButtonRight10(analytics.Search);
        /** Переход в Регистр пациентов */
        System.out.println("Переход в Регистр пациентов");
        actionElementAndClick(registrP.Registry);
        AssertSignatures(registrP.FIO);
        AssertSignaturesButtonRight10(registrP.DistanceFIO);
        AssertSignaturesButtonRight10(registrP.Distance);
        /** Переход в Регистр пациентов для скрининга ЗНО */
        System.out.println("Переход в Регистр пациентов для скрининга ЗНО");
        actionElementAndClick(registryZNO.RegistryZNO);
        AssertSignatures(registryZNO.Diagnosis);
        AssertSignatures(registryZNO.Status);
        AssertSignaturesButtonRightAndLeft(registryZNO.DistanceDiagnosis);
        AssertSignaturesButtonRightAndLeft(registryZNO.DistanceStatus);
        AssertSignaturesButtonRightAndLeft(registryZNO.Distance);
        AssertSignaturesButtonRight10(registryZNO.Search);
        /** Переход в Расписание оборудования */
        System.out.println("Переход в Расписание оборудования");
        actionElementAndClick(equipmentS.EquipmentSchedules);
        AssertSignatures(equipmentS.MO);
        AssertSignatures(equipmentS.Type);
        AssertSignatures(equipmentS.Equipment);
        /** Переход в Направления на квоты - Незавершённые */
        System.out.println("Переход в Направления на квоты - Незавершённые");
        actionElementAndClick(directionsFQ.Consultation);
        AssertSignatures(directionsFQ.Type);
        AssertSignatures(directionsFQ.Period);
        AssertSignatures(directionsFQ.MOTwo);
        AssertSignatures(directionsFQ.FOI);
        AssertSignatures(directionsFQ.SnilsTwo);
        AssertSignaturesButtonRightAndLeft(directionsFQ.DistanceType);
        AssertSignaturesButtonRightAndLeft(directionsFQ.DistancePeriod);
        AssertSignaturesButtonRightAndLeft(directionsFQ.DistanceMO);
        AssertSignaturesButtonRightAndLeft(directionsFQ.DistanceFOI);
        AssertSignaturesButtonRightAndLeft(directionsFQ.DistanceSnils);
        AssertSignaturesButtonRightAndLeft(directionsFQ.Distance);
        AssertSignaturesButtonRight10(directionsFQ.SearchTwo);
        /** Переход в Консультации */
        System.out.println("Переход в Консультации");
        WebElement element5 = driver.findElement(By.xpath(
                "//a[@href='/direction/incomingConsultation/uncompleted?directionType=2&directionTarget=2&directionPageType=1']"));
        actionElementAndClick(element5);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Тип даты')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Период')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Мед.организация')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'ФИО пациента')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Профиль')]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[1]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[2]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-5'])[1]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-5'])[2]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-6']"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-3']"));
        AssertSignaturesButtonRight10(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        /** Переход в Расписание госпитализации */
        System.out.println("Переход в Расписание госпитализации");
        WebElement element6 = driver.findElement(By.xpath("//a[@href='/hospitalschedule']"));
        actionElementAndClick(element6);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Подразделение')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Отделение')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Профиль')]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-10'])[1]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-10'])[2]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-12']"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-1']"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='d-flex align-items-end el-col el-col-4']"));
        AssertSignaturesButtonRight10(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        /** Переход в Очередь госпитализации */
        System.out.println("Переход в Очередь госпитализации");
        actionElementAndClick(driver.findElement(By.xpath(
                "//a[@href='/direction/requestConsultation/evacuation?directionType=2&directionTarget=1&directionPageType=4']")));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Тип даты')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Период')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Мед.организация')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'ФИО пациента')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Профиль')]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[1]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[2]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-5'])[1]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-5'])[2]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-6']"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-3']"));
        AssertSignaturesButtonRight10(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        /** Переход в Направления на эвакуацию */
        System.out.println("Переход в Направления на эвакуацию");
        WebElement element8 = driver.findElement(By.xpath("//a[@href='/direction/evacuationopen?statusIds=14']"));
        actionElementAndClick(element8);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Тип даты')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Дата начала')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Дата окончания')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Мед.организация')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'ФИО пациента')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Профиль')]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[1]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[2]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-24'])[1]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-24'])[2]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-6']"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-5']"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-3']"));
        AssertSignaturesButtonRight10(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        /** Переход в Исследование */
        System.out.println("Переход в Исследование");
        WebElement element9 = driver.findElement(By.xpath("//a[@href='/nsi/medicalreserch']"));
        actionElementAndClick(element9);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Исследование')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Код')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Версия справочника')]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[1]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[2]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[3]"));
        AssertSignaturesButtonRight10(By.xpath("//i[@class='el-icon-search search-icon']"));
        AssertSignaturesButtonRight10(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        /** Переход в Лабараторные исследования */
        System.out.println("Переход в Лабараторные исследования");
        WebElement element10 = driver.findElement(By.xpath("//a[@href='/nsi/labresearch']"));
        actionElementAndClick(element10);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Полное наименование')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Краткое наименование')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Группа')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'НМУ')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Статус')]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[1]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[2]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[3]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[4]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[5]"));
        AssertSignaturesButtonRight10(By.xpath("//i[@class='el-icon-search search-icon']"));
        AssertSignaturesButtonRight10(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        /** Переход в Нормы лабараторных исследований */
        System.out.println("Переход в Нормы лабараторных исследований");
        WebElement element11 = driver.findElement(By.xpath("//a[@href='/nsi/nlabresearch']"));
        actionElementAndClick(element11);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Лабораторное исследование')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Норма')]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[1]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[2]"));
        AssertSignaturesButtonRight10(By.xpath("//i[@class='el-icon-search search-icon']"));
        AssertSignaturesButtonRight10(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        /** Переход в МКБ-10 */
        System.out.println("Переход в МКБ-10");
        WebElement element12 = driver.findElement(By.xpath("//a[@href='/nsi/icd']"));
        actionElementAndClick(element12);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Введите код')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Введите имя')]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[1]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[2]"));
        AssertSignaturesButtonRight10(By.xpath("//i[@class='el-icon-search search-icon']"));
        AssertSignaturesButtonRight10(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        /** Переход в Оборудование */
        System.out.println("Переход в Оборудование");
        WebElement element13 = driver.findElement(By.xpath("//a[@href='/nsi/equipment']"));
        actionElementAndClick(element13);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Наименование')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Исследование (введите для поиска)')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Тип оборудования')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Мед. организация')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Описание')]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[1]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[2]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[3]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[4]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[5]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[6]"));
        AssertSignaturesButtonRight10(By.xpath("//i[@class='el-icon-search search-icon']"));
        AssertSignaturesButtonRight10(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        /** Переход в Простые медицинские услуги */
        System.out.println("Переход в Простые медицинские услуги");
        WebElement element14 = driver.findElement(By.xpath("//a[@href='/nsi/smedservice']"));
        actionElementAndClick(element14);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Код услуги')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Наименование')]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[1]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[2]"));
        AssertSignaturesButtonRight10(By.xpath("//i[@class='el-icon-search search-icon']"));
        AssertSignaturesButtonRight10(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        /** Переход в Справочник профилей */
        System.out.println("Переход в Справочник профилей");
        WebElement element15 = driver.findElement(By.xpath("//a[@href='/nsi/profiledirectory']"));
        actionElementAndClick(element15);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Номер профиля')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Наименование')]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[1]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[2]"));
        AssertSignaturesButtonRight10(By.xpath("//i[@class='el-icon-search search-icon']"));
        AssertSignaturesButtonRight10(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        /** Переход в Отчёты */
        System.out.println("Переход в Отчёты");
        WebElement element16 = driver.findElement(By.xpath("//a[@href='/vimis/sms-statistics']"));
        actionElementAndClick(element16);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Направление')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Период')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Мед. организации')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Информационная система')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Тип документа')]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--small'])[1]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--small'])[2]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--small'])[3]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--small'])[4]"));
        AssertSignaturesButtonRight5(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        driver.findElement(By.xpath("//div[@id][contains(.,'Отчёт по статусам отправки СМС')]")).click();
        AssertSignatures(By.xpath("(//div[@class='form-item-label'][contains(.,'Направление')])[2]"));
        AssertSignatures(By.xpath("(//div[@class='form-item-label'][contains(.,'Период')])[2]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Статус')]"));
        AssertSignatures(By.xpath("(//div[@class='form-item-label'][contains(.,'Информационная система')])[2]"));
        AssertSignaturesButtonRight5(By.xpath("//button[@type='button'][contains(.,'Поиск')]"));
        /** Переход в Клинические рекомендации */
        System.out.println("Переход в Клинические рекомендации");
        WebElement element17 = driver.findElement(By.xpath("//a[@href='/vimis/clinical-recommendations']"));
        actionElementAndClick(element17);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Направление')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Название документа')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Возрастная группа')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Диагнозы')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Дата начала действия')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Дата окончания действия')]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[1]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[2]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[3]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-5'])[1]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-5'])[2]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-5'])[3]"));
        AssertSignaturesButtonLeft5(
                By.xpath("//div[@class='el-form-item text-nowrap margin-left-5 el-form-item--small']"));
        AssertSignaturesButtonRight5(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        /** Переход в Мед. организации */
        System.out.println("Переход в Мед. организации");
        WebElement element18 = driver.findElement(By.xpath("//a[@href='/permission/access']"));
        actionElementAndClick(element18);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Название медицинской организации')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Оказывает консультацию')]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[1]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[2]"));
        AssertSignaturesButtonRight10(By.xpath("//i[@class='el-icon-search search-icon']"));
        AssertSignaturesButtonRight10(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        /** Переход в Структура коечного фонда */
        System.out.println("Переход в Структура коечного фонда");
        WebElement element19 = driver.findElement(By.xpath("//a[@href='/hospital-structure']"));
        actionElementAndClick(element19);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Название медицинской организации')]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--medium'])[1]"));
        AssertSignaturesButtonRight10(By.xpath("//i[@class='el-icon-search search-icon']"));
        AssertSignaturesButtonRight10(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        /** Переход в Пользователи */
        System.out.println("Переход в Пользователи");
        WebElement element20 = driver.findElement(By.xpath("//a[@href='/permission/users']"));
        actionElementAndClick(element20);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'ФИО пользователя')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Дата рождения')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'СНИЛС')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Профиль')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Email адрес')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Дата регистрации')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Медицинская организация')]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[1]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[2]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[3]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[4]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[5]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[6]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[7]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-5']"));
        AssertSignaturesButtonRight10(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        /** Переход в Аудит пользователей */
        System.out.println("Переход в Аудит пользователей");
        WebElement element21 = driver.findElement(By.xpath("//a[@href='/permission/users-audit']"));
        actionElementAndClick(element21);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Период')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Наименование МО')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Действие')]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--small'])[1]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--small'])[2]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--small'])[3]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--small'])[4]"));
        AssertSignaturesButtonRight5(By.xpath("//button[@type='button'][contains(.,'Поиск')]"));
        /** Переход в Настройки отчетов */
        System.out.println("Переход в Настройки отчетов");
        WebElement element22 = driver.findElement(By.xpath("//a[@href='/permission/bi-report-settings']"));
        actionElementAndClick(element22);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Название отчета')]"));
        AssertSignaturesButtonRight10(By.xpath("//div[@class='el-form-item el-form-item--small']"));
        AssertSignaturesButtonRight5(By.xpath("//button[@type='submit'][contains(.,'Поиск')]"));
        /** Переход в Статистика по консультациям */
        System.out.println("Переход в Статистика по консультациям");
        WebElement element23 = driver.findElement(By.xpath("//a[@href='/stats/consultations']"));
        actionElementAndClick(element23);
        AssertSignatures(By.xpath("(//div[@class='form-item-label'][contains(.,'Период')])[1]"));
        AssertSignatures(By.xpath("(//div[@class='form-item-label'][contains(.,'Медицинская организация')])[1]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-5'])[1]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-10'])[1]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-6'])[1]"));
        AssertSignaturesButtonRight5(By.xpath("(//button[@type='button'][contains(.,'Поиск')])[1]"));
        driver.findElement(By.xpath("//div[@id='tab-1']")).click();
        AssertSignatures(By.xpath("(//div[@class='form-item-label'][contains(.,'Период')])[2]"));
        AssertSignatures(By.xpath("(//div[@class='form-item-label'][contains(.,'Медицинская организация')])[2]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-5'])[2]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-10'])[2]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-6'])[2]"));
        AssertSignaturesButtonRight5(By.xpath("(//button[@type='button'][contains(.,'Поиск')])[2]"));
        /** Переход в Статистика по записи на КТ/МРТ */
        System.out.println("Переход в Статистика по записи на КТ/МРТ");
        WebElement element24 = driver.findElement(By.xpath("//a[@href='/stats/diagnostics']"));
        actionElementAndClick(element24);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Период')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Медицинская организация')]"));
        /** Переход в Форма №7 */
        System.out.println("Переход в Форма №7");
        WebElement element25 = driver.findElement(By.xpath("//a[@href='/stats/oncology/form-seventh']"));
        actionElementAndClick(element25);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Период')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Медицинская организация')]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-5']"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-12']"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-4']"));
        /** Переход в Форма №32 */
        System.out.println("Переход в Форма №32");
        WebElement element26 = driver.findElement(By.xpath("//a[@href='/stats/form32']"));
        actionElementAndClick(element26);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Период')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Медицинская организация')]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[1]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-15']"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[2]"));
        /** Переход в Форма №13 */
        System.out.println("Переход в Форма №13");
        WebElement element27 = driver.findElement(By.xpath("//a[@href='/stats/form13']"));
        actionElementAndClick(element27);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Период')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Медицинская организация')]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[1]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-15']"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-4'])[2]"));
        /** Переход в Использование ЛК врача */
        System.out.println("Переход в Использование ЛК врача");
        WebElement element28 = driver.findElement(By.xpath("//a[@href='/stats/doctor-lk']"));
        actionElementAndClick(element28);
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Период')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Медицинская организация')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Разработчик')]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--small'])[1]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--small'])[2]"));
        AssertSignaturesButtonRight10(By.xpath("(//div[@class='el-form-item el-form-item--small'])[3]"));
        AssertSignaturesButtonRight5(By.xpath("//button[@type='button'][contains(.,'Поиск')]"));
        /** Переход в Отчеты */
        if (KingNumber == 1 | KingNumber == 2) {
            System.out.println("Переход в Отчеты");
            WebElement element29 = driver.findElement(By.xpath("//a[@href='/stats/bi-reports']"));
            actionElementAndClick(element29);
            AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Наименование')]"));
        }
        /** Переход в Мониторинг получения карт диспансерного учета */
        System.out.println("Переход в Мониторинг получения карт диспансерного учета");
        if (KingNumber == 1) {
            driver.get("https://tm-test.pkzdrav.ru/registry/monitoring");
        }
        if (KingNumber == 2) {
            driver.get("https://tm-dev.pkzdrav.ru/registry/monitoring");
        }
        if (KingNumber == 4) {
            driver.get("https://remotecons-test.miacugra.ru//registry/monitoring");
        }
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Мед.организация')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Период постановки на учёт')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Период снятия с учёта')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Год рождения пациента')]"));
        AssertSignatures(By.xpath("//div[@class='form-item-label'][contains(.,'Код диагноза')]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-5'])[1]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-5'])[2]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-5'])[3]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("(//div[@class='el-col el-col-5'])[4]"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-4']"));
        AssertSignaturesButtonRightAndLeft(By.xpath("//div[@class='el-col el-col-3']"));
        AssertSignaturesButtonRight5(By.xpath("//button[@class='el-button mr-5 el-button--primary el-button--small']"));

    }

    @Test
    @Issue(value = "TEL-740")
    @Link(name = "ТМС-1267", url = "https://team-1okm.testit.software/projects/5/tests/1267?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Единообразие кнопки поиск")
    @Description("Переходим в Регистр диспансерных больных - Онко - выбрать пациента - Нажать перейти на страницу пациента, Открыть мониторинг посещений. Проверить, что кнопка Поиск такая же как и в разделе Скрининги")
    public void ChangeConsultationHospitalization() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        registerDP = new RegisterDispensaryPatients(driver);
        screenings = new Screenings(driver);
        if (KingNumber == 1) {
            /** Переходим в регистр диспансерных больных - Онко */
            System.out.println("Переходим в регистр диспансерных больных - Онко");
            AuthorizationMethod(authorizationObject.OKB);
            WaitElement(registerDP.RegistrDBOnkoWait);
            actionElementAndClick(registerDP.RegistrDBOnko);
            WaitElement(registerDP.HeaderOnko);
            /** Открываем карту страницу 1 пациента */
            System.out.println("Открываем карту страницу 1 пациента");
            WaitElement(registerDP.CartWait);
            actionElementAndClick(registerDP.Cart);
            /** Переходим в Мониторинг посещений */
            System.out.println("Переходим в Мониторинг посещений");
            WaitElement(registerDP.MonitoringWait);
            actionElementAndClick(registerDP.Monitoring);
            /** Проверка единообразия полей фильтрации */
            System.out.println("Проверка единообразия полей фильтрации");
            AssertSignatures(registerDP.Type);
            AssertSignatures(registerDP.Period);
            AssertSignaturesButtonRightAndLeft(registerDP.DistanceType);
            AssertSignaturesButtonRightAndLeft(registerDP.DistancePeriod);
            /** Взять параметры кнопки поиск */
            System.out.println("Взять параметры кнопки поиск");
            String Search = registerDP.SearchMonitoring.getAttribute("class");
            String SearchCss = registerDP.SearchMonitoring.getCssValue("padding");
            System.out.println(Search + "   " + SearchCss);
            /** Перейти в Скрининги и сравнить кнопки Поиск */
            System.out.println("Перейти в Скрининги и сравнить кнопки Поиск");
            actionElementAndClick(screenings.Screening);
            WaitElement(screenings.Search);
            String SearchCssScreenings = screenings.SearchWeb.getCssValue("padding");
            Assertions.assertEquals(SearchCss, SearchCssScreenings, "Кнопки поиск не совпадают");
        }
    }
}
