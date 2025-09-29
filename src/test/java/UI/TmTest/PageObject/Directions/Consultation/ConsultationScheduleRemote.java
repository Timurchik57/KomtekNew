package UI.TmTest.PageObject.Directions.Consultation;

import Base.BaseAPI;
import Base.BaseTest;
import Base.SQL;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;
import java.sql.SQLException;

public class ConsultationScheduleRemote extends BaseAPI {

    public static Integer NumberColumn;

    /**
     * Направления - Консультации - Расписание консультаций
     **/
    public ConsultationScheduleRemote(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Направления - Консультации - Расписание консультаций
     **/
    public By ConsultationScheduleremote = By.xpath(
            "//a[@href='/operator/doctorschedule']");

    /**
     * Расписание консультаций - Селкты
     **/
    public By Selected(String value) {
        By Slots = By.xpath("//input[@placeholder='"+value+"']");
        return Slots;
    }

    /**
     * Расписание консультаций - Загрузка
     */
    public By Loading = By.xpath(
            "//article[@class='scheduler el-loading-parent--relative']//div[@class='el-loading-spinner']");

    /**
     * Расписание консультаций - Колонка со слотами
     */
    public By SlotsTrue = By.xpath(
            "//div[@class='vuecal__cell-events']/div[1]");

    /**
     * Расписание консультаций - Колонка со слотами - Все слоты (если одна колонка с консультантом)
     */
    public By Slots = By.xpath(
            "//div[@class='vuecal__cell-events']/div");

    /**
     * Расписание консультаций - Колонка со слотами - Все слоты у определённой колоннки
     */
    public By StotsSearch(Integer value) {
        By Slots = By.xpath(
                "(//div[@class='vuecal__cell-events'])[" + value + "]/div");
        return Slots;
    }

    /**
     * Расписание консультаций - Колонка где есть слоты - Первый свободный слот в нужной калонке
     */
    public By SlotsFreeWait(Integer value, boolean nameColumn) throws IOException, InterruptedException {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        /** Если нам нужно выбрать первый свободный слот у определённого Консультанта */
        By Slots;
        String str = null;
        if (nameColumn) {
            str = "(//div[@class='vuecal__cell vuecal__cell--selected vuecal__cell--has-splits vuecal__cell--has-events']/div[" + value + "]/div[@class='vuecal__cell-events']//div[@class='el-dropdown consultation-slot-template consultation-slot-template-free'])[1]";
        } else {
            str = "(//div[@class='vuecal__cell-events']//div[@class='el-tooltip item consultation-slot-template consultation-slot-template-free'])[1]";
        }
        Slots = By.xpath(str);

        // Нужно в тех случаях, когда слот свободен, но на него есть квота на ребенка
        if (!ReadPropFile("className").equals("Access_3082Test") | !ReadPropFile("className").equals("Access_2559Test")) {
            System.out.println("Зашли по клссу ");
            Integer number = 1;
            while (number < 10) {
                System.out.println(number + " - какой раз зашли в цикл");
                // Тут мы меняем предпоследнюю цифру, чтобы брать следующий свободный слот
                String result = str.substring(0, str.length() - 2) + number + str.substring(str.length() - 1);
                Slots = By.xpath(result);

                WaitElement(Slots);
                WebElement element = driver.findElement(Slots);
                actions.moveToElement(element);
                actions.perform();
                WaitElement(authorizationObject.Xplacement);
                Thread.sleep(1500);
                if (isElementVisibleTime(SlotKids("Слот только для записи ребенка"), 1) | isElementVisibleTime(
                        SlotKids("Слот только для записи детей до года"), 1)) {
                    System.out.println("Переключаемся дальше так как видим слот на ребёнка");
                    number++;
                } else {
                    System.out.println("останавливаемся и выбираем свободный слот по счёту - " + number);
                    break;
                }
            }
        }
        return Slots;
    }

    /**
     * Расписание консультаций - Колонка где есть слоты - Занятые слоты
     */
    public By SlotsCloseWait = By.xpath(
            "(//div[@class='el-dropdown consultation-slot-template consultation-slot-template-busy'])[1]");

    /**
     * Расписание консультаций - Определённая колонка у консультанта - Последний занятый слот - Время
     */
    public By SlotsBusyLastTime(Integer value) {
        Slots = By.xpath(
                "(//div[@class='vuecal__cell vuecal__cell--selected vuecal__cell--has-splits vuecal__cell--has-events']/div["+value+"]/div[@class='vuecal__cell-events']//div[@class='el-dropdown consultation-slot-template consultation-slot-template-busy'])[last()]//span/b");
        return Slots;
    }

    /**
     * Расписание консультаций - Определённая колонка у консультанта - Первый свободный слот - Время
     */
    public By SlotsFreeFirstTimePatient(Integer value) {
        Slots = By.xpath(
                "(//div[@class='vuecal__cell vuecal__cell--selected vuecal__cell--has-splits vuecal__cell--has-events']/div["+value+"]/div[@class='vuecal__cell-events']//div[@class='el-dropdown consultation-slot-template consultation-slot-template-free'])[1]//span/b");
        return Slots;
    }

    /**
     * Расписание консультаций - Определённая колонка у консультанта - Последний занятый слот - Время и Пациент
     */
    public By SlotsBusyLastTimePatient(Integer value, String patient) {
        Slots = By.xpath(
                "((//div[@class='vuecal__cell vuecal__cell--selected vuecal__cell--has-splits vuecal__cell--has-events']/div["+value+"]/div[@class='vuecal__cell-events']//div[@class='el-dropdown consultation-slot-template consultation-slot-template-busy'])//span/span[contains(.,'"+patient+"')])[last()]/preceding-sibling::b");
        return Slots;
    }

    /**
     * Расписание консультаций - Определённая колонка у консультанта - Последний занятый слот - Диагноз
     */
    public By SlotsBusyLastDiagnosis(Integer value) {
        Slots = By.xpath(
                "((//div[@class='vuecal__cell vuecal__cell--selected vuecal__cell--has-splits vuecal__cell--has-events']/div["+value+"]/div[@class='vuecal__cell-events']//div[@class='el-dropdown consultation-slot-template consultation-slot-template-busy'])[last()]//span/span)[1]");
        return Slots;
    }

    /**
     * Расписание консультаций - Определённая колонка у консультанта - Последний занятый слот - Диагноз и Пациент
     */
    public By SlotsBusyLastDiagnosisPatient(Integer value, String patient) {
        Slots = By.xpath(
                "((//div[@class='vuecal__cell vuecal__cell--selected vuecal__cell--has-splits vuecal__cell--has-events']/div["+value+"]/div[@class='vuecal__cell-events']//div[@class='el-dropdown consultation-slot-template consultation-slot-template-busy'])//span/span[contains(.,'"+patient+"')])[last()]");
        return Slots;
    }

    /**
     * Расписание консультаций -  Недоступный (квота на мо, серый цвет) слот с определённым временем
     */
    public By SlotDisable (String time) {
        By SlotDisable = By.xpath("//div[@class='el-dropdown consultation-slot-template consultation-slot-template-disabled']//b[contains(.,'"+time+"')]");
        return SlotDisable;
    }

    /**
     * Расписание консультаций - Недоступный (занятый) слот с определённым временем нужной колонке (Для расписания консультаций)
     */
    public By SlotDisableCons (Integer column, String time) {
        By SlotDisable = By.xpath("//div[@class='vuecal__cell vuecal__cell--selected vuecal__cell--has-splits vuecal__cell--has-events']/div["+column+"]/div[@class='vuecal__cell-events']//div[@class='el-dropdown consultation-slot-template consultation-slot-template-busy']//b[contains(.,'"+time+"')]");
        return SlotDisable;
    }

    /**
     * Расписание консультаций - Доступный слот с определённым временем нужной колонке (Для расписания консультаций)
     */
    public By SlotActiveCons (Integer column, String time) {
        By SlotDisable = By.xpath("//div[@class='vuecal__cell vuecal__cell--selected vuecal__cell--has-splits vuecal__cell--has-events']/div["+column+"]/div[@class='vuecal__cell-events']//div[@class='el-dropdown consultation-slot-template consultation-slot-template-free']//b[contains(.,'"+time+"')]");
        return SlotDisable;
    }

    /**
     * Расписание консультаций - Перезапись слота
     */
    public By Refresh = By.xpath(
            "//ul[@x-placement]//button[contains(.,'Перезаписать другого пациента')]");

    /**
     * Расписание консультаций - Отменить запись пациента
     */
    public By ClosePatient = By.xpath(
            "//ul[@x-placement]//button[contains(.,'Отменить запись пациента')]");

    /**
     * Расписание консультаций - Отменить запись пациента - Отменить
     */
    public By ClosePatientAdd = By.xpath(
            "//div[@x-placement]//p[contains(.,'Вы точно хотите отменить запись?')]/following-sibling::div/button[contains(.,'Отменить')]");

    /**
     * Расписание консультаций - Уведомление, что слот не доступен
     */
    public By SlotNotAvailable = By.xpath("//div[@x-placement]//li[contains(.,'Данный слот недоступен для записи из вашей медицинской организации. Обратитесь в МО')]");

    /** Расписание консультаций - Отобржение в поп ап нужной информации */
    public By SlotKids (String value) {
        By SlotKidss = By.xpath("//div[@x-placement]//li[contains(.,'"+value+"')]");
        return SlotKidss;
    }

    /**
     * Расписание консультаций - Информация о слоте - Слот только для записи ребёнка - Ошабка Данный слот распределен только для записи ребенка (младше 18 лет)
     */
    public By SlotKidsError (String value) {
        By Error = By.xpath("//div[@role='alert']//p[contains(.,'"+value+"')]");
        return Error;
    }

    /**
     * Расписание консультаций - Информация о слоте - Номер телефона пациента
     */
    public By SlotNumberPhone = By.xpath("//div[@role='tooltip']//ul/li[contains(.,'Телефон:')]");

    /**
     * Расписание консультаций - Информация о слоте - Диагноз
     */
    public By SlotDiagnose = By.xpath("//div[@role='tooltip']//ul/li[contains(.,'Диагноз:')]");

    /**
     * Расписание консультаций - Информация о слоте - Фио Пациента
     */
    public By SlotAllFio = By.xpath("//div[@role='tooltip']//ul/li[contains(.,'Пациент:')]");

    /**
     * Расписание консультаций - Информация о слоте - Дата рождения
     */
    public By SlotBirthday = By.xpath("//div[@role='tooltip']//ul/li[contains(.,'Дата рождения:')]");

    /**
     * Расписание консультаций - Выбранная дата
     */
    public By DateToDay = By.xpath(
            "//button[@class='datepicker__btn-view']");

    /**
     * Расписание консультаций - следующий день
     */
    public By NextDay = By.xpath(
            "//div[@class='datepicker']/button[@class='datepicker__btn-next']");

    /**
     * Расписание консультаций - Показать весь день
     */
    public By AllDay = By.xpath(
            "//button[contains(.,'Показывать весь день')]");

    /**
     * Расписание консультаций - Выбранный консультант - Дата в определённый день недели
     */
    public By WeekDay(Integer value) {
        By WeekDay = By.xpath(
                "//div[@class='vuecal__flex vuecal__weekdays-headings']/div[" + value + "]//span[4]");
        return WeekDay;
    }

    /**
     * Расписание консультаций - Ошибка записи на слот
     */
    public By AlertNotConsul = By.xpath(
            "//div[@role='alert']//p[contains(.,'Ошибка записи на слот')]");

    /**
     * Расписание консультаций - График консультаций
     **/
    public By ConsultationSchedule = By.xpath("//h1[contains(.,'Расписание консультаций')]/following-sibling::a");

    /**
     * График консультаций - Данные о консультанте
     **/
    public By User(String name, String time, String value) {
        By User = By.xpath("//tr/td[contains(.,'"+name+"')]/following-sibling::td[3][contains(.,'"+time+"')]/following-sibling::td[" + value + "]//span");
        return User;
    }

    /**
     * График консультаций - Редактировать у определённого консультанта
     **/
    public By UserEdit(String name, String date) {
        By User = By.xpath("//tr/td[contains(.,'"+name+"')]/following-sibling::td[3][contains(.,'"+date+"')]/following-sibling::td[4]//button[@tooltiptext='Редактировать']");
        return User;
    }

    /**
     * График консультаций - Редактировать у 1 консультанта
     **/
    public By UserEditFirst = By.xpath("//tr[1]/td/following-sibling::td[7]//button[@tooltiptext='Редактировать']");

    /**
     * График консультаций - Редактировать у 1 консультанта - Недоступно
     **/
    public By UserEditFirstNo = By.xpath("//tr[1]/td/following-sibling::td[7]//button[@class='el-button el-tooltip el-button--primary el-button--small is-disabled is-plain'][@tooltiptext='Редактировать']");

    /**
     * График консультаций - Следующая страница (не активно)
     **/
    public By NextDisabled = By.xpath("//button[@class='btn-next'][@disabled]");

    /**
     * График консультаций - Следующая страница (активно)
     **/
    public By Next = By.xpath("//button[@class='btn-next']");

    /**
     * График консультаций - Отображается на странице
     **/
    public By ReadOnly = By.xpath("//input[@readonly='readonly']");

    /**
     * График консультаций - Всего записей
     **/
    public By ConsultationTotal = By.xpath("//span[@class='el-pagination__total']");

    /**
     * График консультаций - Ошибка
     **/
    public By AlertNotSchedule = By.xpath("//div[@role='alert']//p[contains(.,'Ошибка при получении расписания')]");

    /**
     * График консультаций - Ошибка
     **/
    public By AccessDenied = By.xpath("//h3[contains(.,'Доступ к разделу запрещен')]");

    /**
     * Расписание консультаций - График консультаций - Добавить расписание
     **/
    public By ConsultationScheduleAdd = By.xpath(
            "//h1[contains(.,'Расписание консультаций / График консультаций')]/following-sibling::button");

    /**
     * Добавить расписание - Селекты
     **/
    public By Select(String value) {
        By Select = By.xpath("//label[contains(.,'" + value + "')]/following-sibling::div//input");
        return Select;
    }

    /**
     * Добавить расписание - Дата начала
     **/
    public By DateStart = By.xpath("//input[@placeholder='Дата начала']");

    /**
     * Добавить расписание - Дата начала - 1 число следующего месяца
     **/
    public By DateFirstMonth = By.xpath("(//td[@class='next-month']//span[text()=1])[1]");

    /**
     * Добавить расписание - Дата начала - завтра
     **/
    public By Tomorrow = By.xpath("//td[@class='available today']/following-sibling::td[1]");

    /**
     * Добавить Расписание оборудования - Оборудование - Дата начала - Завтра (если завтра со следующей недели)
     */
    public By TomorrowWeek = By.xpath("//tr/td[@class='available today']/parent::tr/following-sibling::tr[1]/td[1]");

    /**
     * Добавить расписание - Дата Окончания - Выбранная дата (кроме текущей даты)
     **/
    public By UsedDate = By.xpath("//td[@class='available in-range start-date end-date']");

    /**
     * Добавить расписание - Дата Окончания - Выбранная дата (нужно если завтрашнее число это 1 число следующего месяца)
     **/
    public By UsedDateToday = By.xpath("//td[@class='available today in-range start-date end-date']");

    /**
     * Добавить расписание - Дата окночания - 1 число следующего месяца
     **/
    public By DateLastMonth = By.xpath(
            "//div[@class='el-picker-panel__content el-date-range-picker__content is-right']//td[@class='available'][1]");

    /**
     * Добавить расписание - Время
     **/
    public By Time(String value) {
        By Select = By.xpath("//div[@x-placement]//div[@class='time-select-item'][contains(.,'" + value + "')][1]");
        return Select;
    }

    /**
     * Добавить расписание - Ошибка - Количество дней не может быть меньше 1
     **/
    public By ErrorDay = By.xpath(
            "//div[@class='el-form-item__error'][contains(.,'Количество дней не может быть меньше 1')]");

    /**
     * Добавить расписание - Дни активности
     **/
    public By DayActivity(String value) {
        By Select = By.xpath("//label[@role][" + value + "]");
        return Select;
    }

    /**
     * Добавить расписание - добавить исключение
     **/
    public By Exception = By.xpath("//span[@class='el-checkbox__input']");

    /**
     * Добавить расписание - добавить исключение - Выберите дату
     **/
    public By SearchDate = By.xpath("//input[@placeholder='Выберите дату']");

    /**
     * Добавить расписание - добавить исключение - Выберите дату - Один свободный день
     **/
    public By SearchDateTrue = By.xpath("//div[@x-placement]//td[@class='available']");

    /**
     * Добавить расписание - добавить исключение - Выберите дату - Один свободный день (нужно если завтрашнее число это 1 число следующего месяца)
     **/
    public By SearchDateTrueToday = By.xpath("//div[@x-placement]//td[@class='available today']");

    /**
     * Добавить расписание - Распределение квот
     **/
    public By Kvots = By.xpath("//div[@id='tab-quotas']");

    /**
     * Распределение Квот - Нужное время
     */
    public By KvotsTime (String date, String value) {
        By Data = By.xpath("(//tr/td[contains(.,'"+date+"')])["+value+"]/following-sibling::td[1]");
        return Data;
    }

    /**
     * Распределение Квот - Сохранить
     */
    public By EditSave = By.xpath("//button/span[contains(.,'Изменить')]");

    /**
     * Добавить расписание - Отменить
     **/
    public By Close = By.xpath("//div[@class='el-dialog__footer']//button/span[contains(.,'Отменить')]");

    /**
     * Добавить расписание - Добавить
     **/
    public By Add = By.xpath("//div[@class='el-dialog__footer']//button/span[contains(.,'Добавить')]");

    /**
     * Добавить расписание - Добавить - Ошибка пересечения расписания
     **/
    public By Error(String value) {
        By Select = By.xpath("//div[@role='alert']//p[contains(.,'" + value + "')]");
        return Select;
    }

    @Step("Метод добавления расписания для Удалённой консультации для консультанта {0} с целью {1}")
    public void AddConsul(String NameConsult, String profile, String Purpose, By dateStart, By dateEnd, String StartTime, String EndTime, String Day) throws InterruptedException {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);

        ClickElement(ConsultationScheduleAdd);
        ClickElement(Select("Консультант"));
        ClickElement(authorizationObject.Select(NameConsult));
        ClickElement(Select("Профиль"));
        ClickElement(authorizationObject.Select(profile));
        SelectClickMethod(Select("Цель консультаций"),
                authorizationObject.Select(Purpose));
        ClickElement(DateStart);
        if(isElementNotVisible(dateStart)) {
            ClickElement(dateStart);
        } else {
            ClickElement(TomorrowWeek);
        }
        ClickElement(dateEnd);
        SelectClickMethod(Select("Время начала"), Time(StartTime));
        SelectClickMethod(Select("Время окончания"), Time(EndTime));
        inputWord(driver.findElement(Select("Отображение расписания")), Day);
        for (int i = 1; i < 8; i++) {
            ClickElement(DayActivity("" + i + ""));
        }
        ClickElement(Add);
        if (isElementVisibleTime(
                Error("Такое расписание пересекается с ранее созданным расписанием указанного врача"),
                3) | isElementVisibleTime(Error("Такое расписание уже существуют"), 3)) {
            ClickElement(Close);
        }
    }

    @Step("Метод проверки свободных слотов для удалённой консультации")
    public Boolean CheckConsulRemote(String nameDoctor) throws InterruptedException, IOException {
        ConsultationScheduleRemote consultationSR = new ConsultationScheduleRemote(driver);

        WaitElement(consultationSR.AllDay);
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);
        ClickElement(consultationSR.NextDay);
        Thread.sleep(1500);

        Integer number = 0;
        while (number < 3 & !isElementNotVisible(By.xpath("//div[@class='vuecal__flex vuecal__split-days-headers']/div[contains(.,'" + nameDoctor + "')]"))) {
            number++;
            Thread.sleep(1500);
            WaitNotElement3(consultationSR.Loading, 30);
            ClickElement(consultationSR.NextDay);
        }
        if (number == 3) {
            return false;
        } else {
            /** Определяем какой столбец с нужным Консультантом, чтобы в дальнейшем выбрать свободный слот именно в этой колонке */
            for (int i = 1; i < 20; i++) {
                if (isElementVisibleTime(By.xpath("//div[@class='vuecal__flex vuecal__split-days-headers']/div[" + i + "]//abbr[contains(.,'" + nameDoctor + "')]"), 1)) {
                    NumberColumn = i;
                    break;
                }
            }

            /** Теперь перелистываем страницы до тех пор пока не будет свободного слота у нужного пользователя */
            number = 0;
            while (!isElementNotVisible(By.xpath("(//div[@class='vuecal__cell vuecal__cell--selected vuecal__cell--has-splits vuecal__cell--has-events']/div[" + NumberColumn + "]/div[@class='vuecal__cell-events']//div[@class='el-dropdown consultation-slot-template consultation-slot-template-free'])[1]")) & number < 3) {
                number++;
                Thread.sleep(1500);
                WaitNotElement3(consultationSR.Loading, 30);
                ClickElement(consultationSR.NextDay);
            }
            return true;
        }
    }

    @Step("Метод для того, чтобы взять время записанного слота Консультации")
    public String GetTimeConsul () throws InterruptedException, IOException, SQLException {

        // с 08:00 до 08:30
        SQL sql = new SQL();
        String starttime = null;
        String endtime = null;

        System.out.println("Проверяем в БД");
        sql.StartConnection(
                "SELECT cs.id, cs.userid, u.fname, u.sname, u.mname, cs.datefrom, cs.dateto, cts.patient_diagnosis, msm.namemu morasp, msm2.namemu mocons, msm3.namemu mokvot,\n" +
                        "ct.id , ct.starttime, ct.endtime, ct.for_kid, cts.patient_id, opr.lastname, opr.firstname, opr.middlename FROM telmed.consultationstakenslots cts\n" +
                        "join telmed.consultationslotstime ct on cts.idtimeinterval = ct.id\n" +
                        "join telmed.consultationslots cs on ct.scheduleid = cs.id\n" +
                        "join telmed.users u on cs.userid = u.id\n" +
                        "join telmed.profiledirectory p on cs.profile = p.id\n" +
                        "join dpc.mis_sp_mu msm on cs.medicalidmu = msm.medicalidmu\n" +
                        "left join dpc.mis_sp_mu msm2 on cts.medicalidmu = msm2.medicalidmu\n" +
                        "left join dpc.mis_sp_mu msm3 on ct.distributeto_medicalidmu = msm3.medicalidmu\n" +
                        "left join telmed.directions d on cts.iddirection = d.id\n" +
                        "left join dpc.mis_sp_mu msm4 on d.targetmoid = msm4.idmu\n" +
                        "left join iemk.op_patient_reg opr on cts.patient_id = opr.patient_id\n" +
                        "order by cts.id desc limit 1;");
        while (sql.resultSet.next()) {
            starttime = sql.resultSet.getString("starttime");
            endtime = sql.resultSet.getString("endtime");
        }
        //2025-05-31 07:30:00
        System.out.println(starttime);
        System.out.println(endtime);

        String time = "с " + starttime.substring(11, 16) + " до " + endtime.substring(11, 16);
        return time;
    }
}
