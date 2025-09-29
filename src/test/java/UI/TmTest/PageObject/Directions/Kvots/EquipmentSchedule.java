package UI.TmTest.PageObject.Directions.Kvots;

import Base.BaseAPI;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.NSI.Equipments;
import api.Before.Authorization;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.sql.SQLException;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;

public class EquipmentSchedule extends BaseAPI {
    /**
     Расписание оборудования
     */
    public EquipmentSchedule (WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    public int NumberColumn;
    public int trueNumber = 0;
    public boolean Box = true;

    /**
     Расписание оборудования
     */
    @FindBy(xpath = "//a[@href='/operator/slots']")
    public WebElement EquipmentSchedules;
    public By EquipmentSchedulesWait = By.xpath("//a[@href='/operator/slots']");

    /**
     Заголовок - Расписание оборудования
     */
    public By HeaderEquipmentSchedulesWait = By.xpath("//h1[contains(.,'Расписание оборудования')]");

    /**
     Медицинская организация
     */
    public By MO = By.xpath("//div[@class='form-item-label'][contains(.,'Медицинская организация')]");

    /**
     Тип оборудования
     */
    public By Type = By.xpath("//div[@class='form-item-label'][contains(.,'Тип оборудования')]");

    /**
     Оборудование
     */
    public By Equipment = By.xpath("//div[@class='form-item-label'][contains(.,'Оборудование')]");

    /**
     Расписание оборудования - Показать весь день
     */
    public By AllDay = By.xpath(
            "//button[contains(.,'Показывать только рабочие часы')]");

    /**
     Расписание оборудования - Загрузка
     */
    public By Loading = By.xpath(
            "//article[@class='scheduler el-loading-parent--relative']//div[@class='el-loading-spinner']");

    /**
     Расписание оборудования - Следующая страница
     */
    @FindBy(xpath = "//div[@class='datepicker']/button[@class='datepicker__btn-next']")
    public WebElement NextPage;
    public By NextPageWait = By.xpath("//div[@class='datepicker']/button[@class='datepicker__btn-next']");

    /**
     Расписание оборудования - Недоступный слот
     */
    public By NotPageWait = By.xpath(
            "//div[@class='vuecal__cell-events']/div[1]/div[@class='el-tooltip event-info item disabled']");

    /**
     Расписание оборудования - Колонка где есть слоты
     */
    public By SlotsTrue = By.xpath("//div[@class='vuecal__cell-events']/div[1]");

    /**
     Расписание оборудования - Колонка где есть слоты - Свободные слоты
     */
    @FindBy(xpath = "(//div[@class='vuecal__cell-events']//div[@class='el-tooltip event-info item free'])[1]")
    public WebElement SlotsFree;
    public By SlotsFreeWait = By.xpath(
            "(//div[@class='vuecal__cell-events']//div[@class='el-tooltip item event-info free'])[1]");

    public By SlotsFreeWaitEquipment (Integer value, boolean nameColumn) {
        /** Если нам нужно выбрать первый свободный слот у определённого оборудования */
        By Slots;
        if (nameColumn) {
            Slots = By.xpath(
                    "(//div[@class='vuecal__cell vuecal__cell--selected vuecal__cell--has-splits vuecal__cell--has-events']/div[" + value + "]//div[@class='el-tooltip item event-info free'])[1]");
        } else {
            Slots = By.xpath(
                    "(//div[@class='vuecal__cell-events']//div[@class='el-tooltip item event-info free'])[1]");
        }
        return Slots;
    }

    /**
     Свободный слот - Запись на приём
     */
    public By WriteWait = By.xpath("//h3[contains(.,'Запись на прием')]");

    /**
     Свободный слот - Запись на приём - Прикрепите соответсвующие файлы
     */
    @FindBy(xpath = "(//input[@type='file'])[1]")
    public WebElement AddFile;
    @FindBy(xpath = "(//input[@type='file'])[2]")
    public WebElement AddFile2;
    @FindBy(xpath = "(//input[@type='file'])[3]")
    public WebElement AddFile3;
    public By AddFileWait = By.xpath("//p[contains(.,'Прикрепите соответсвующие файлы')]");

    /**
     Свободный слот - Запись на приём - Файлы
     */
    public By FileWait1 = By.xpath("(//a[contains(.,'test.txt')])[1]");
    public By FileWait2 = By.xpath("(//a[contains(.,'test.txt')])[2]");
    public By FileWait3 = By.xpath("(//a[contains(.,'test.txt')])[3]");

    /**
     Свободный слот - Запись на приём - Недоступно Записать
     */
    public By loadingWriteTwo = By.xpath(
            "//button[@class='el-button el-button--success el-button--medium is-disabled']/span[contains(.,'Записать')]");

    /**
     Свободный слот - Запись наприём - Записать
     */
    @FindBy(xpath = "//button/span[contains(.,'Записать')]")
    public WebElement Write;
    public By WriteTwo = By.xpath("//button/span[contains(.,'Записать')]");

    /**
     Свободный слот - Запись наприём - Записать - Загрузка
     */
    public By WriteLoading = By.xpath(
            "//section[@class='el-loading-parent--relative']//div[@class='el-loading-spinner']");

    /**
     Свободный слот - Запись наприём - Запись на прием успешна создана
     */
    @FindBy(xpath = "//button/span[contains(.,'Завершить')]")
    public WebElement AlertClose;
    public By AlertCloseWait = By.xpath("//button/span[contains(.,'Завершить')]");
    public By AlertWait = By.xpath("//p[contains(.,'Запись на прием успешна создана')]");

    /**
     Свободный слот - Запись наприём - Предупреждение о превышении веса
     */
    public By AlertErrorWait = By.xpath("//div[@aria-label='Предупреждение']");

    /**
     Отображение фамилии при записи
     */
    public By FioWait = By.xpath(
            "(//div[@class='vuecal__cell-events']/div[1]/div[@class='el-tooltip event-info item busy'])[1]/p[contains(.,'Галиакберов Т.О.')]p[contains(.,'Запись на прием успешна создана')]");

    /**
     Занятый слот
     */
    @FindBy(xpath = "(//div[@class='vuecal__cell-events']/div[1]/div[@class='el-tooltip event-info item busy'])[last()]")
    public WebElement OccupiedSlot;
    public By OccupiedSlotWaitt = By.xpath(
            "(//div[@class='el-tooltip item event-info busy'])[last()]");
    public By OccupiedSlotWait = By.xpath("//div[@x-placement][contains(.,'Слот занят')]");

    /**
     График работы оборудования
     */
    @FindBy(xpath = "//a[@href='/operator/schedule']")
    public WebElement ButtonWorkSchedule;
    public By ButtonWorkScheduleWait = By.xpath("//a[@href='/operator/schedule']");

    /**
     Расписание оборудования / График работы оборудования
     */
    public By HeaderWorkSchedule = By.xpath("//h1[.='Расписание оборудования / График работы оборудования']");

    /**
     Добавить Расписание оборудования
     */
    @FindBy(xpath = "//button//span[contains(.,'Добавить расписание')]")
    public WebElement ButtonEquipmentSchedules;
    public By ButtonEquipmentSchedulesWait = By.xpath("//button//span[contains(.,'Добавить расписание')]");

    /**
     Добавить Расписание оборудования - Оборудование
     */
    @FindBy(xpath = "//label[contains(.,'Оборудование')]/following-sibling::div")
    public WebElement SelectEquipment;
    public By SelectEquipmentWait = By.xpath("//label[contains(.,'Оборудование')]/following-sibling::div");

    /**
     Добавить Расписание оборудования - Оборудование - X-OMAT
     */
    public By SelectEquipmentChoice = By.xpath("//div[@x-placement]//li/span[contains(.,'X-OMAT')]");
    public By SelectEquipmentChorus = By.xpath("//div[@x-placement]//li/span[contains(.,'Chorus 1.5T')]");
    public By SelectEquipmentCDR = By.xpath("//div[@x-placement]//li/span[contains(.,'CDR Kit')]");

    public By SelectEq (String value) {
        By select = By.xpath("//div[@x-placement]//li/span[contains(.,'" + value + "')]");
        return select;
    }

    /**
     Добавить Расписание оборудования - Оборудование - Дата начала
     */
    @FindBy(xpath = "//input[@placeholder='Дата начала']")
    public WebElement DataStart;
    public By DataStartBy = By.xpath("//input[@placeholder='Дата начала']");

    /**
     Добавить Расписание оборудования - Оборудование - Дата начала - Текущая дата
     */
    @FindBy(xpath = "//tr/td[@class='available today']")
    public WebElement DataToday;
    public By DataTodayWait = By.xpath("//tr/td[@class='available today']");

    /**
     Добавить Расписание оборудования - Оборудование - Дата начала - Завтра
     */
    public By Tomorrow = By.xpath("//tr/td[@class='available today']/following-sibling::td[1]");

    /**
     Добавить Расписание оборудования - Оборудование - Дата начала - Завтра (если завтра со следующей недели)
     */
    public By TomorrowWeek = By.xpath("//tr/td[@class='available today']/parent::tr/following-sibling::tr[1]/td[1]");

    /**
     Добавить Расписание оборудования - Оборудование - 1 число следующего месяца
     */
    public By NextMouth = By.xpath(
            "(//div[@class='el-picker-panel__content el-date-range-picker__content is-right']//td[@class='available'])[1]");

    /**
     Добавить Расписание оборудования - Оборудование - Дата начала - Через день, после текущей даты
     */
    @FindBy(xpath = "//tr/td[@class='available today in-range start-date end-date']/following-sibling::td[2]")
    public WebElement DataTodayTwo;
    public By DataTodayTwoWait = By.xpath(
            "//tr/td[@class='available today in-range start-date end-date']/following-sibling::td[2]");

    /**
     Добавить Расписание оборудования - Оборудование - Дата начала - Другая дата
     */
    @FindBy(css = ".is-right tr:nth-child(4) td:nth-child(1)")
    public WebElement DataAnother;
    public By DataAnotherWait = By.cssSelector(".is-right tr:nth-child(4) td:nth-child(1)");

    /**
     Добавить Расписание оборудования - Оборудование - Время начала
     */
    public By TimeStart = By.xpath("//input[@placeholder='Начало']");

    public By SelectTime (String value) {
        By str = By.xpath("//div[@x-placement]//div[@class='time-select-item'][contains(.,'" + value + "')]");
        return str;
    }

    /**
     Добавить Расписание оборудования - Оборудование - Время окончания
     */
    public By TimeEnd = By.xpath("//input[@placeholder='Окончание']");
    public By SelectTimeEnd = By.xpath("//div[@x-placement]//div[@class='time-select-item'][150]");

    /**
     Добавить Расписание оборудования - Оборудование - Интервал
     */
    @FindBy(xpath = "//input[@placeholder='от 1 до 60']")
    public WebElement Interval;

    /**
     Добавить Расписание оборудования - Оборудование - Дни
     */
    @FindBy(xpath = "//div[@role='group']/label[1]")
    public WebElement Day1;
    @FindBy(xpath = "//div[@role='group']/label[2]")
    public WebElement Day2;
    @FindBy(xpath = "//div[@role='group']/label[3]")
    public WebElement Day3;
    @FindBy(xpath = "//div[@role='group']/label[4]")
    public WebElement Day4;
    @FindBy(xpath = "//div[@role='group']/label[5]")
    public WebElement Day5;
    @FindBy(xpath = "//div[@role='group']/label[6]")
    public WebElement Day6;
    @FindBy(xpath = "//div[@role='group']/label[7]")
    public WebElement Day7;

    /**
     Добавить Расписание оборудования - Оборудование - Добавить
     */
    @FindBy(xpath = "(//button//span[contains(.,'Добавить')])[3]")
    public WebElement Add;

    /**
     Редактировать определённое оборудование (последнее созданное)
     */
    public By Equipment (String value) {
        By Equipments = By.xpath("(//tr/td[contains(.,'" + value + "')]/following-sibling::td[6]/div/span)[1]");
        return Equipments;
    }

    /**
     Редактировать определённое оборудование (с нужной датой)
     */
    public By EquipmentTime (String value, String time) {
        By Equipments = By.xpath(
                "(//tr/td[contains(.,'" + value + "')]/following-sibling::td[1][contains(.,'" + time + "')]/following-sibling::td[5]/div/span)[1]");
        return Equipments;
    }

    /**
     Определённое оборудование (с нужной датой)
     */
    public By EquipmentTimeWait (String value, String time) {
        String str = "(//tr/td[contains(.,'" + value + "')]/following-sibling::td[1][contains(.,'" + time + "')]/following-sibling::td[5]/div/span)[1]";
        System.out.println(str);
        By Equipments = By.xpath(
                "(//tr/td[contains(.,'" + value + "')]/following-sibling::td[1][contains(.,'" + time + "')])[1]");
        return Equipments;
    }

    /**
     Редактировать оборудование - Распределение Квот
     */
    public By Kvots = By.xpath("//div[@id='tab-quotas'][contains(.,'Распределение квот')]");

    /**
     Распределение Квот - нужная дата
     */
    public By KvotsData (String value) {
        By Data = By.xpath("//tr/td[contains(.,'" + value + "')]");
        return Data;
    }

    /**
     Распределение Квот - Нужное время
     */
    public By KvotsTime (String date, String value) {
        By Data = By.xpath("(//tr/td[contains(.,'" + date + "')])[" + value + "]/following-sibling::td[1]");
        return Data;
    }

    /**
     Распределение Квот - Недоступная квота в нужную дату, в нужное время
     */
    public By KvotsTimeDateDisabled (String date, String time) {
        By Data = By.xpath(
                "//tr/td[contains(.,'" + date + "')]/following-sibling::td[contains(.,'" + time + "')]/following-sibling::td[2]//input[@disabled='disabled']");
        return Data;
    }

    /**
     Распределение Квот - Все недоступные квоты в нужную дату
     */
    public By AllKvotsTimeDateDisabled (String date) {
        By Data = By.xpath(
                "(//tr/td[contains(.,'" + date + "')])/following-sibling::td//input[@disabled='disabled'][@placeholder='МО']");
        return Data;
    }

    /**
     Распределение Квот - Все доступные квоты в нужную дату
     */
    public By AllKvotsTimeDate (String date) {
        By Data = By.xpath(
                "(//tr/td[contains(.,'" + date + "')])/following-sibling::td//input[@placeholder='МО']");
        return Data;
    }

    /**
     Распределение Квот - следующая страница
     */
    public By Next = By.xpath("//div[@id='pane-quotas']//button[@class='btn-next']");

    /**
     Распределение Квот - Сохранить
     */
    public By Save = By.xpath("//button/span[contains(.,'Сохранить')]");

    /**
     Добавить Расписание оборудования - Оборудование - Отменить
     */
    public By Close = By.xpath("//button//span[contains(.,'Отменить')]");

    /**
     Добавить Расписание оборудования - Оборудование - Уведомление о пересечении расписания оборудования
     */
    public By Alert = By.xpath("//div[@role='alert']");

    @Step("Метод нажатия на редактирование оборудования")
    public void ClickEquipment() throws InterruptedException {
        Authorization authorization = new Authorization();
        WebElement element = null;

        // Смотрим есть ли расписание до 1 числа следующего месяца, так как в автотестах оно создаётся с завтрашнего числа по 1 число следующего месяца
        System.out.println();
        if (isElementVisibleTime(EquipmentTimeWait(PNameEquipment2, "01." + SetDate(0, 1).substring(0, 2) + "." + Year), 2)) {
            element = driver.findElement(
                    EquipmentTime(PNameEquipment2, "01." + SetDate(0, 1).substring(0, 2) + "." + Year));
        }

        // Смотрим есть ли на завтра расписание
        else if (isElementVisibleTime(EquipmentTimeWait(PNameEquipment2, SetDate(1, 0).substring(3) + "." + SetDate(1, 0).substring(0, 2) + "." + Year), 2)) {
            element = driver.findElement(
                    EquipmentTime(PNameEquipment2, SetDate(1, 0).substring(3) + "." + SetDate(1, 0).substring(0, 2) + "." + Year));
        }

        // Смотрим есть ли на послезавтра расписание
        else if (isElementVisibleTime(EquipmentTimeWait(PNameEquipment2, SetDate(2, 0).substring(3) + "." + SetDate(2, 0).substring(0, 2) + "." + Year), 2)) {
            element = driver.findElement(
                    EquipmentTime(PNameEquipment2, SetDate(2, 0).substring(3) + "." + SetDate(2, 0).substring(0, 2) + "." + Year));
        }

        actions.moveToElement(element);
        actions.click();
        actions.perform();
        Thread.sleep(1500);
    }

    /**
     Метод для добавления расписания оборудования
     SelectEquipment - выбираемое оборудование
     Datastart - дата начала расписания оборудования
     Dataend - дата окончания расписания оборудования
     */
    @Step("Метод для добавления расписания оборудования")
    public void AddEquipmentSchedule (By SelectEquipment, By Datastart, By Dataend, String timeStart, String timeEnd, String interval) throws InterruptedException, SQLException {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        EquipmentSchedule equipmentSchedule = new EquipmentSchedule(driver);
        Equipments equipments = new Equipments(driver);

        System.out.println("Определяем параметр по МТБЗ");
        MTBZMethod();

        System.out.println("Переход в Создание расписания оборудования");
        ClickElement(EquipmentSchedulesWait);
        WaitElement(HeaderEquipmentSchedulesWait);
        ClickElement(ButtonWorkScheduleWait);

        System.out.println("Расписание оборудования / График работы оборудования");
        WaitElement(HeaderWorkSchedule);
        ClickElement(ButtonEquipmentSchedulesWait);

        System.out.println("Составление расписания");
        Thread.sleep(1500);
        SelectClickMethod(SelectEquipmentWait, SelectEquipment);

        equipmentSchedule.DataStart.click();
        System.out.println("Выбор даты");
        WaitElement(authorizationObject.Xplacement);
        if (isElementNotVisible(Datastart)) {
            ClickElement(Datastart);
        } else {
            ClickElement(TomorrowWeek);
        }
        ClickElement(Dataend);
        WaitNotElement(authorizationObject.Xplacement);

        System.out.println("Выбор Времени");
        SelectClickMethod(TimeStart, SelectTime(timeStart));
        SelectClickMethod(TimeEnd, SelectTime(timeEnd));

        System.out.println("Интервал");
        inputWord(equipmentSchedule.Interval, interval + "1");

        System.out.println("Дни");
        equipmentSchedule.Day1.click();
        equipmentSchedule.Day2.click();
        equipmentSchedule.Day3.click();
        equipmentSchedule.Day4.click();
        equipmentSchedule.Day5.click();
        equipmentSchedule.Day6.click();
        equipmentSchedule.Day7.click();
        Thread.sleep(1500);
        equipmentSchedule.Add.click();
        Thread.sleep(1500);
        if (isElementNotVisible(By.xpath(
                "//div[@role='alert']//p[contains(.,'Добавляемое расписание имеет пересечения с имеющимся в системе расписанием.')]"))) {
            ClickElement(equipmentSchedule.Close);
        }
    }

    /**
     SelectEquipment - выбираемое оборудование
     Datastart - дата начала расписания оборудования
     Dataend - дата окончания расписания оборудования
     */
    @Step("Метод для добавления исследования нужному оборудованию")
    public void AddResearch (String MO, String Name, String description, String[] researches) throws InterruptedException {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        Equipments equipments = new Equipments(driver);

        ClickElement(equipments.EquipmentWaitt);

        /** Выбор организации для редактирования */
        WaitElement(equipments.EquipmentWait);
        WaitElement(equipments.HeaderEquipmentWait);

        SelectClickMethod(equipments.InputMoWait, authorizationObject.Select(MO));
        ClickElement(equipments.CheckIs);
        inputWord(equipments.Description, description + " ");
        inputWord(driver.findElement(equipments.NameWait), Name + " ");
        Thread.sleep(1500);
        WaitNotElement3(equipments.SearchLoading, 50);
        ClickElement(equipments.SearchWait);
        WaitElement(equipments.Edit1Wait);

        System.out.println("Проверка цвета оборудования (зелёный)");
        String Color = equipments.FirstLine.getCssValue("background-color");
        Assertions.assertEquals(Color, "rgba(207, 255, 207, 1)", " Цвет не совпадает с зелёным");

        System.out.println("Редактировать 1 элемент из списка");
        ClickElement(equipments.FirstLineSet);
        WaitElement(equipments.HeaderEditWait);

        System.out.println("Редактирование Исследования");
        equipments.Researches.click();
        WaitElement(equipments.InputWordWait);
        for (String str : researches) {
            if (!isElementNotVisible(equipments.Researches(str))) {
                inputWord(equipments.InputWord, str);
                ClickElement(authorizationObject.Select(str));
                wait.until(invisibilityOfElementLocated(equipments.BottomStartWait));
                equipments.Add.click();
            }
        }
        Thread.sleep(1000);
        equipments.Update.click();
        Thread.sleep(1500);
        System.out.println("Оборудование отредактировано");
    }

    @Step("Метод проверки свободных слотов для направления")
    public Boolean CheckDirection (String nameEquipment) throws InterruptedException {

        System.out.println("Ищем свободный слот для направления");
        WaitElement(AllDay);
        Thread.sleep(1500);
        WaitNotElement3(Loading, 30);
        ClickElement(NextPageWait);
        Thread.sleep(1500);

        Integer number = 0;
        while (number < 3 & !isElementNotVisible(By.xpath(
                "//div[@class='vuecal__flex vuecal__split-days-headers']/div[contains(.,'" + nameEquipment + "')]"))) {
            number++;
            Thread.sleep(1500);
            WaitNotElement3(Loading, 30);
            ClickElement(NextPageWait);
        }
        if (number == 3) {
            return false;
        } else {
            /** Определяем какой столбец с нужным Оборудованием, чтобы в дальнейшем выбрать свободный слот именно в этой колонке */
            for (int i = 1; i < 20; i++) {
                if (isElementVisibleTime(By.xpath(
                                "//div[@class='vuecal__flex vuecal__split-days-headers']/div[" + i + "]//abbr[contains(.,'" + nameEquipment + "')]"),
                        1)) {
                    NumberColumn = i;
                    break;
                }
            }

            /** Теперь перелистываем страницы до тех пор пока не будет свободного слота у нужного пользователя */
            number = 0;
            while (!isElementNotVisible(SlotsFreeWaitEquipment(NumberColumn, true)) & number < 3) {
                number++;
                Thread.sleep(1500);
                WaitNotElement3(Loading, 30);
                ClickElement(NextPageWait);
            }
            return true;
        }
    }

    @Step("Метод поиска определённой квоты с нужной датой и временем")
    public void CheckKvotTime (String date, String time) throws InterruptedException {
        Integer Listnumber = 0;
        trueNumber = 0;

        while (!isElementVisibleTime(By.xpath(
                        "(//tr/td[contains(.,'" + date + "')])/following-sibling::td[1][contains(.,'" + time + "')]"),
                1) & Listnumber < 5) {

            System.out.println("Перелистываем страницу");
            ClickElement(Next);
            Thread.sleep(1500);
            Listnumber++;
        }
    }

    @Step("Метод проверки выбора свободной квоты")
    public void CheckKvot (String date) throws InterruptedException {
        Integer Listnumber = 0;
        trueNumber = 0;

        /** Логика следующая
         Мы уже находимся на странице с нужной датой
         Проверяем есть ли недоступные квоты
         ______если есть, то
         проверяем доступна ли нужная строка (занята)
         проверяем отображается ли нужная строка
         берём мо в селекте
         проверяем пустое ли значение
         Если Box = true,то проверяем ещё и отжатые чекбоксы
         Если  Box = false,то не проверяем отжатые чекбоксы
         */

        if (isElementVisibleTime(AllKvotsTimeDateDisabled(date), 2)) {
            System.out.println("Сюда зашли если есть недоступные");
            WaitElement(AllKvotsTimeDateDisabled(date));
            while (Listnumber < 4) {
                WaitElement(By.xpath(
                        "(//tr/td[contains(.,'" + date + "')])[1]/following-sibling::td//input[@class='el-input__inner']"));
                for (int j = 1; j <= 20; j++) {
                    System.out.println("Начали счётчик, смотрим " + j + " запись с датой " + date);
                    if (!isElementVisibleTime(By.xpath(
                                    "(//tr/td[contains(.,'" + date + "')])[" + j + "]/following-sibling::td//input[@disabled='disabled']"),
                            1)
                            &&
                            // 2 проверка для того, чтобы убедиться что данный слот вообще отображается на странице (например 21 нет на странице)
                            isElementVisibleTime(By.xpath(
                                            "(//tr/td[contains(.,'" + date + "')])[" + j + "]/following-sibling::td//input[@class='el-input__inner']"),
                                    1)
                            &&
                            // 3 и 4 проверка нужна чтобы не бралась квота, у которой уже стоят чек боксы детей
                            !isElementVisibleTime(By.xpath(
                                            "(//tr/td[contains(.,'" + date + "')])[" + j + "]/following-sibling::td[4]//label[@class='el-checkbox child-checkbox is-checked']"),
                                    1)
                            &&
                            !isElementVisibleTime(By.xpath(
                                            "(//tr/td[contains(.,'" + date + "')])[" + j + "]/following-sibling::td[5]//label[@class='el-checkbox child-checkbox is-checked']"),
                                    1)) {

                        // Нужно для определения записанного МО, то есть мы можем уже выбрать в селекте нужную МО, но оно будет доступно ещё для редактирования, мы исключаем этот вариант
                        String mo = getShadow(driver.findElement(By.xpath(
                                "(//tr/td[contains(.,'" + date + "')])[" + j + "]/following-sibling::td//input[@class='el-input__inner']")));

                        if (mo.isEmpty()) {
                            trueNumber = j;
                            System.out.println("Нашли нужную строчку - " + trueNumber);
                            break;
                        }
                    }
                }
                if (trueNumber != 0) {
                    System.out.println("Завершаем цикл");
                    break;
                }

                System.out.println("Перелистываем страницу");
                ClickElement(Next);
                Thread.sleep(1500);
                Listnumber++;
            }
        } else {
            for (int i = 1; i < 20; i++) {
                System.out.println(
                        "(//tr/td[contains(.,'" + date + "')])[" + i + "]/following-sibling::td//input[@disabled='disabled']");
                if (!isElementVisibleTime(By.xpath(
                                "(//tr/td[contains(.,'" + date + "')])[" + i + "]/following-sibling::td//input[@disabled='disabled']"),
                        1)) {
                    String mo = getShadow(driver.findElement(By.xpath(
                            "(//tr/td[contains(.,'" + date + "')])[" + i + "]/following-sibling::td//input[@class='el-input__inner']")));
                    if (mo.isEmpty()) {
                        trueNumber = i;
                        break;
                    }
                }
            }
        }
    }
}
