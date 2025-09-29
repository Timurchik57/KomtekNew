package UI.TmTest.PageObject.Directions.Kvots;

import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import Base.BaseAPI;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.io.IOException;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class DirectionsForQuotas extends BaseAPI {
    /**
     Направления на квоты / Исходящие / Незавершенные
     */
    public DirectionsForQuotas (WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    AuthorizationObject authorizationObject;
    EquipmentSchedule equipmentSchedule;
    DirectionsForQuotas directionsForQuotas;
    public int CountAsk;
    public boolean DoctorConsul = false;

    /**
     Незавершенные
     */
    public By Unfinished = By.linkText("Незавершенные");

    /**
     Направления на квоты / Исходящие / Незавершенные
     */
    @FindBy(xpath = "//a[@href='/direction/requestDiagnostic/uncompleted?directionType=1&directionTarget=1&directionPageType=1']")
    public WebElement Consultation;
    public By ConsultationWait = By.xpath(
            "//a[@href='/direction/requestDiagnostic/uncompleted?directionType=1&directionTarget=1&directionPageType=1']");

    /**
     Направления на квоты / Исходящие / Незавершенные - Заголовок
     */
    public By Heading = By.xpath("//h1[.='Направления на квоты / Исходящие / Незавершенные']");

    /**
     Тип даты
     */
    public By Type = By.xpath("//div[@class='form-item-label'][contains(.,'Тип даты')]");

    /**
     Период
     */
    public By Period = By.xpath("//div[@class='form-item-label'][contains(.,'Период')]");

    /**
     Разворачиваем селекты
     */
    public By SelectAllClick = By.xpath(
            "//button[@class='el-button el-tooltip m-0 el-button--default el-button--small']");

    /**
     Любой селект
     */
    public By SelectAll (String value) {
        By str = null;
        if (value.equals("Наличие патологии")) {
            str = By.xpath("(//div[contains(.,'" + value + "')]/following-sibling::div)[1]");
        } else {
            str = By.xpath("//div[contains(.,'" + value + "')]/following-sibling::span");
        }
        return str;
    }

    /**
     Мед.организация
     */
    public By MOTwo = By.xpath("//div[@class='form-item-label'][contains(.,'Мед.организация')]");

    /**
     ФИО пациента
     */
    public By FOI = By.xpath("//div[@class='form-item-label'][contains(.,'ФИО пациента')]");

    /**
     Снилс пациента
     */
    public By SnilsTwo = By.xpath("//div[@class='form-item-label'][contains(.,'Снилс пациента')]");

    /**
     Дистанция
     */
    public By DistanceType = By.xpath("(//div[@class='el-col el-col-4'])[1]");

    /**
     Дистанция
     */
    public By DistancePeriod = By.xpath("(//div[@class='el-col el-col-4'])[2]");

    /**
     Дистанция
     */
    public By DistanceMO = By.xpath("(//div[@class='el-col el-col-5'])[1]");

    /**
     Дистанция
     */
    public By DistanceFOI = By.xpath("(//div[@class='el-col el-col-5'])[2]");

    /**
     Дистанция
     */
    public By DistanceSnils = By.xpath("//div[@class='el-col el-col-6']");

    /**
     Дистанция
     */
    public By Distance = By.xpath("//div[@class='el-col el-col-3']");

    /**
     Поиск
     */
    public By SearchTwo = By.xpath("//button[@type='submit'][contains(.,'Поиск')]");

    /**
     Направления на квоты / Исходящие / Незавершенные - Снилс
     */
    @FindBy(xpath = "//div[text()='Снилс пациента']/following-sibling::div/input")
    public WebElement InputSnils;

    /**
     Направления на квоты / Исходящие / Незавершенные - Сортировка по id в обратном порядке
     */
    public By SortDesc = By.xpath("(//span/i[@class='sort-caret descending'])[1]");

    /**
     Направления на квоты / Исходящие / Незавершенные - Отправка на интепретацию
     */
    public By MassInter = By.xpath("//button[contains(.,'Отправка на интерпретацию')]");

    /**
     Направления на квоты / Исходящие / Незавершенные - Отправка на интепретацию - Нужная строка - Чек бокс
     */
    public By MassInterCheck (String id) {
        By str = By.xpath("(//tbody/tr/td[2][contains(.,'" + id + "')]/preceding-sibling::td//span)[1]");
        return str;
    }

    /**
     Направления на квоты / Исходящие / Незавершенные - Отправить
     */
    public By MassInterAdd = By.xpath("//button[contains(.,'Отправить')]");

    /**
     Направления на квоты / Исходящие / Незавершенные - Выбираем направление с нужным id и берём значение нужного поля
     */
    public By GetDirection (String id, String column) {
        By str = By.xpath("//tbody/tr/td[1][contains(.,'" + id + "')]/following-sibling::td[" + column + "]//span");
        return str;
    }

    /**
     Направления на квоты / Входящие / Незавершенные - строка с нужным id
     */
    public By SearchID (String id) {
        By str = By.xpath("(//tbody/tr//div/span[contains(.,'" + id + "')])[1]");
        return str;
    }

    /**
     Направления на квоты / Исходящие / Незавершенные - цвет нужной ячейки
     */
    public By Color (String value) {
        By str = By.xpath("//tbody/tr/td[1][contains(.,'" + value + "')]");
        return str;
    }

    /**
     Направления на квоты / Исходящие / Незавершенные - Поиск
     */
    @FindBy(xpath = "//button[@type='submit']")
    public WebElement Search;

    /**
     Направления на квоты / Исходящие / Незавершенные - 1 строка в таблице
     */
    public By FirstLine = By.xpath("//tbody/tr[1]");

    /**
     Направления на квоты / Исходящие / Незавершенные - 1 строка в таблице - МО
     */
    public By FirstLineMO = By.xpath("//tbody/tr[1]/td[2]//span");

    /**
     Направления на квоты / Исходящие / Незавершенные - 1 строка в таблице - Дата
     */
    public By FirstLineDate = By.xpath("//tbody/tr[1]/td[4]//span");

    /**
     Направления на квоты / Исходящие / Незавершенные - 1 строка в таблице - Пациент
     */
    public By FirstLinePatient = By.xpath("//tbody/tr[1]/td[7]//span");

    /**
     Направления на квоты / Исходящие / Незавершенные - 1 строка в таблице - Снилс
     */
    public By FirstLineSnils = By.xpath("//tbody/tr[1]/td[8]//span");

    /**
     Направления на квоты / Исходящие / Незавершенные - 1 строка в таблице - Статус
     */
    public By FirstLineStatus = By.xpath("(//tbody/tr[1]/td[9]//span)[3]");

    /**
     Направления на квоты / Исходящие / Незавершенные - Последняя строка в таблице
     */
    @FindBy(xpath = "//tbody/tr[last()]")
    public WebElement lastLine;

    /**
     Направления на квоты / Исходящие / Незавершенные - столбец со снилс
     */
    @FindBy(xpath = "//tbody/tr[last()]/td[8]")
    public WebElement TableSnils;
    public By TableSnilsWait = By.xpath("//tbody/tr[last()]/td[8]");

    /**
     Направления на квоты / Исходящие / Незавершенные - Всего записей
     */
    public By AllCount = By.xpath("//span[@class='el-pagination__total']");

    /**
     Направления на квоты / Исходящие / Незавершенные - Создать направление
     */
    @FindBy(xpath = "//button//span[contains(.,'Создать направление')]")
    public WebElement Create;
    public By CreateWait = By.xpath("//button//span[contains(.,'Создать направление')]");

    /**
     Выбранное направление - Записать на приём
     */
    public By AddReception = By.xpath("//button/span[contains(.,'Записать на приём')]");

    /**
     Выбранное направление - Загрузка
     */
    public By LoadingAddPatient = By.xpath("(//div[@class='el-loading-spinner'])[5]");

    /**
     Выбранное направление - Добавить файлы
     */
    public By Addfiles = By.xpath("//button[contains(.,'Добавить файлы')]");

    /**
     Выбранное направление - Добавить файлы - Закрыть
     */
    public By Clouses = By.xpath("//footer//button[contains(.,'Закрыть')]");

    /**
     Выбранное направление - Номер направления
     */
    public By NumberConsult = By.xpath("//span[contains(.,'Номер направления')]");

    /**
     Выбранное направление - Нужная кнопка
     */
    public By Button (String value) {
        By str = By.xpath("(//button/span[contains(.,'" + value + "')])[1]");
        return str;
    }

    /**
     Выбранное направление - Отметить отказ пациента (для статусов 6 и 15) - Причина отказа
     */
    public By ReasonRefusal = By.xpath("//label[contains(.,'Причина отказа')]/following-sibling::div//textarea");

    /**
     Выбранное направление - Отказы пациента - Нужные данные из таблицы
     */
    public By ReasonTable (String value, String column) {
        By str = By.xpath(
                "(//div[contains(.,'Отказы пациента')]/following-sibling::div)[1]//table/tbody/tr[" + value + "]/td[" + column + "]//span");
        return str;
    }

    /**
     Выбранное направление - accessionNumber
     */
    public By AccessionNumber = By.xpath("//span[contains(.,'AccessionNumber')]");

    /**
     Выбранное направление - Отпарвить на интепретацию - Отправить
     */
    public By Send = By.xpath(
            "//div[@class='el-dialog__header'][contains(.,'Отправка исследования на интерпретацию')]/following-sibling::div[2]//button[contains(.,'Отправить')]");

    /**
     Выбранное направление - Цами или ИИ - Выбранная строка и столбец
     */
    public By CamiAndII (String Name, String line, String column) {
        By str = By.xpath(
                "//div[contains(.,'" + Name + "')]/following-sibling::div[@class='panel-body']//table[@class='el-table__body']//tr[" + line + "]/td[" + column + "]//span");
        return str;
    }

    /**
     Выбранное направление - Цами - Выбранная строка - Кнопка отправить
     */
    public By CamiAdd (String line) {
        By str = By.xpath(
                "//div[contains(.,'ЦАМИ')]/following-sibling::div[@class='panel-body']//table[@class='el-table__body']//tr[" + line + "]/td[4]//button");
        return str;
    }

    /**
     Выбранное направление - Сформировать направление
     */
    public By CreateDirectionLow = By.xpath("//button//span[contains(.,'Сформировать направление')]");

    /**
     Выбранное направление - Сформировать направление - Сформированное направление
     */
    public By CreateDirectionAfter = By.xpath("//td[contains(.,'Направление_Тестировщик_Т.Т..docx')]");

    /**
     Выбранное направление - Сформировать направление - Сформированное направление - Скачать
     */
    public By DownloadCreateDirection = By.xpath(
            "//td[contains(.,'Направление_Тестировщик_Т.Т..docx')]/following-sibling::td[3]/a");

    /**
     Выбранное направление - Ошибка
     */
    public By AlertError = By.xpath("//div[@role='alert'][contains(.,'Ошибка')]");

    /**
     Выбранное направление - Сформировать направление - Сформированное направление - Скачать архивом
     */
    public By DownloadArhiv = By.xpath("(//a[contains(.,'Скачать архивом')])[1]");

    /**
     Выбранное направление - Сформировать направление - Сформированное протокол
     */
    public By CreateProtocol = By.xpath("//button//span[contains(.,'Сформировать протокол')]");

    /** -----------------------------Создание направления----------------------- */

    /**
     Создать направление - Тип консультации
     */
    public By TypeConsultationWait = By.xpath("//h3[.='Выберите текущий тип консультации ?']");

    /**
     Создать направление - Направление на диагностику
     */
    @FindBy(xpath = "//label//span[contains(.,'Направление на диагностику')]")
    public WebElement DistrictDiagnostic;
    public By DistrictDiagnosticWait = By.xpath("//div//span[contains(.,'Направление на диагностику')]");

    /**
     Создать направление - Удаленная консультация
     */
    @FindBy(xpath = "//label//span[contains(.,'Удаленная консультация')]")
    public WebElement RemoteConsultation;
    public By RemoteConsultationWait = By.xpath("//label//span[contains(.,'Удаленная консультация')]");

    /**
     Далее
     */
    @FindBy(xpath = "//button//span[contains(.,'Далее')]")
    public WebElement Next;
    public By NextWait = By.xpath(
            "//button[@class='el-button el-button--primary el-button--medium']//span[contains(.,'Далее')]");

    /**
     Введите СНИЛС
     */
    @FindBy(xpath = "//section[@class='patient-form']//input[@placeholder='Введите СНИЛС']")
    public WebElement Snils;
    public By SnilsWait = By.xpath("//section[@class='patient-form']//input[@placeholder='Введите СНИЛС']");

    /** ----------Направление на диагностику - Заполнение данных о направившем враче------ */

    /**
     ----------------- Направление на диагностику - Общие данные о пациенте-------------
     */
    public By PatientDataWait = By.xpath(
            "//div[@class='el-divider el-divider--horizontal']//div[contains(.,'Общие данные о пациенте')]");

    /**
     Направление на диагностику - Создание направления на диагностику
     */
    public By HeaderAdd = By.xpath(
            "//h3/span[contains(.,'направления на диагностику')]");

    /**
     Направление на диагностику - Информация о направившем враче
     */
    public By InfoDoctorWait = By.xpath(
            "(//form[@class='el-form']//div[contains(.,'Информация о направившем враче')])[1]");

    /**
     Направление на диагностику - Направивший врач
     */
    public By SubmittingDoctorWait = By.xpath("(//div/input[@placeholder='Введите значение для поиска'])[1]");

    /**
     Направление на диагностику - ФИО
     */
    public By FIO = By.xpath("(//div[@x-placement]//li/span)[1]");

    /**
     Направление на диагностику - ожидание загрузки
     */
    public By loading = By.xpath(
            "//div[@class='el-select el-select--medium el-loading-parent--relative']//div[@class='el-loading-spinner']");

    /**
     Направление на диагностику - Подразделение
     */
    public By Division = By.xpath("(//input[@placeholder='Введите значение для поиска'])[2]");
    public By SelectDivision = By.xpath("//div[@x-placement]//li/span[contains(.,'Женская консультация')]");

    /**
     Направление на диагностику - Загрузка отделения
     */
    public By LoadingDepartment = By.xpath(
            "(//div[@class='el-form-item is-required el-form-item--medium']//div[@class='el-loading-spinner'])[1]");

    /**
     Направление на диагностику - Отделение
     */
    @FindBy(xpath = "(//input[@placeholder='Введите значение для поиска'])[3]")
    public WebElement Department;
    public By DepartmentWait = By.xpath("(//input[@placeholder='Введите значение для поиска'])[3]");
    public By SelectDepartment = By.xpath("//div[@x-placement]//li/span[contains(.,'Акушерско-гинекологические')]");

    /**
     Направление на диагностику - Должность
     */
    public By Post = By.xpath("(//input[@placeholder='Введите значение для поиска'])[4]");

    public By SelectPost = By.xpath(
            "//div[@x-placement]//li/span[contains(.,'главный врач медицинской организации')]");

    /**
     Направление на диагностику - Специальность
     */
    public By Specialization = By.xpath("(//input[@placeholder='Введите значение для поиска'])[5]");
    public By SelectSpecialization = By.xpath(
            "//div[@x-placement]//li/span[contains(.,'Общественное здравоохранение')]");
    public By SelectSpecializationFirst = By.xpath("//div[@x-placement]//li[1]");
    public By SelectSpecializationBottom = By.xpath("//div[@x-placement]//ul/li");

    /**
     Направление на диагностику - Анатомические области
     */
    public By AnatomicalAreas = By.xpath(
            "(//label[contains(.,'Анатомические области')]/following-sibling::div//input)[1]");
    public By SelectAnatomicalAreas = By.xpath("//div[@x-placement]//li/span[contains(.,'Головной мозг')]");
    @FindBy(xpath = "//div[@x-placement='top-start']//li/span[contains(.,'Головной мозг')]")
    public WebElement SelectAnatomicalAreasTop;

    /**
     Направление на диагностику - Мо, куда направлен
     */
    public By MODirection = By.xpath("(//input[@placeholder='Введите значение для поиска'])[6]");
    public By SelectMODirection = By.xpath(
            "//div[@x-placement]//li/span[contains(.,'БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"')]");
    public By SelectMODirectionKon = By.xpath(
            "//div[@x-placement]//ul/li/span[contains(.,'Кондинская районная стоматологическая поликлиника')]");

    public By SelectMOAll (String value) {
        By str = By.xpath(
                "//div[@x-placement]//ul/li/span[contains(.,'" + value + "')]");
        return str;
    }

    /**
     Направление на диагностику - Мо, куда направлен - Мо через которую авторизованы
     */
    public By MyMODirection = By.xpath("//label[contains(.,'МО, куда направлен')]/following-sibling::div//button");

    /**
     Направление на диагностику - Подразделение при Мо через которую авторизованы
     */
    public By MyDivision = By.xpath(
            "//label[contains(.,'Подразделение')]/following-sibling::div//input[@placeholder='Укажите подразделение']");
    public By MySelectDivision = By.xpath("//div[@x-placement]//ul//li/span[text()='Женская консультация']");
    public By MySelectDivisionApteka = By.xpath("//div[@x-placement]//ul//li/span[text()='Аптека']");

    /**
     Направление на диагностику - Диагноз
     */
    @FindBy(xpath = "//input[@placeholder='Введите значение или пробел для поиска']")
    public WebElement Diagnosis;
    @FindBy(xpath = "//ul/li/span[contains(.,'A01.0 Брюшной тиф')]")
    public WebElement SelectDiagnosis;
    public By SelectDiagnosisWait = By.xpath("//ul/li/span[contains(.,'A01.0 Брюшной тиф')]");

    /**
     Направление на диагностику - Исследование
     */
    public By Research = By.xpath("//label[contains(.,'Исследование')]/following-sibling::div//input");
    public By SelectResearch = By.xpath("//div//li/span[contains(.,'HMP01')]");
    public By SelectResearch_HMP30 = By.xpath("(//div//li/span[contains(.,'HMP30')])[1]");
    public By SelectResearch_HMP04 = By.xpath("(//div//li/span[contains(.,'HMP04')])[1]");

    public By SelectRe (String value) {
        By select = By.xpath("(//div//li/span[contains(.,'" + value + "')])[1]");
        return select;
    }

    /**
     Направление на диагностику - Код льготы
     */
    public By BenefitCode = By.xpath("(//input[@placeholder='Введите значение для поиска'])[8]");
    public By SelectBenefitCode = By.xpath("//div//li/span[contains(.,'010.Инвалиды войны')]");

    /**
     Направление на диагностику - Вес
     */
    @FindBy(xpath = "//label[contains(.,'Вес пациента')]/following-sibling::div//input")
    public WebElement Mass;
    public By MassWait = By.xpath("//label[contains(.,'Вес пациента')]/following-sibling::div//input");

    /**
     Направление на диагностику - Цель направления
     */
    public By GoalDirections = By.xpath("//label[contains(.,'Цель направления')]/following-sibling::div//input");

    /**
     Направление на диагностику - Фамилия
     */
    @FindBy(xpath = "//input[@placeholder='Фамилия']")
    public WebElement NamePatient;

    /**
     Направление на диагностику - Имя
     */
    @FindBy(xpath = "//input[@placeholder='Имя']")
    public WebElement LastNamePatient;

    /**
     Направление на диагностику - Отчество
     */
    @FindBy(xpath = "//input[@placeholder='Отчество']")
    public WebElement MiddleNamePatient;

    /**
     Направление на диагностику - Cito
     */
    public By Cito = By.xpath("//div[@role='switch']");

    /**
     Направление на диагностику - Header
     */
    public By Header = By.xpath("//th[contains(.,'Направление на диагностику')]");

    /**
     Направление на диагностику - Создать направление
     */
    @FindBy(xpath = "//footer/button/span[contains(.,'Создать направление')]")
    public WebElement CreateDirection;
    public By CreateDirectionWait = By.xpath("//footer/button/span[contains(.,'Создать направление')]");

    /**
     Направление на диагностику - Отправить на согласование
     */
    public By Agreement (String value) {
        By SearchWait = By.xpath("//footer/button/span[contains(.,'" + value + "')]");
        return SearchWait;
    }

    /**
     Направление на диагностику - Данные диагностики - Цель направления
     */
    public By GoalDirection (String value) {
        By SearchWait = By.xpath(
                "//tbody/tr/td[contains(.,'Цель направления')]/following-sibling::td[contains(.,'" + value + "')]");
        return SearchWait;
    }

    /**
     Направление на диагностику - Прикрепление файла
     */
    @FindBy(xpath = "//input[@type='file']")
    public WebElement File;
    public By FileWait = By.xpath("//h3[contains(.,'Файлы')]");
    public By AddFileWait = By.xpath("//label[contains(.,'Добавить файлы')]");
    public By AddFileWait1 = By.xpath("//span[contains(.,'Выбрать файл')]");

    /**
     Направление на диагностику - Прикрепление файла - Скачать
     */
    public By Download = By.xpath("//a/span[contains(.,'Скачать')]");

    /**
     Направление на диагностику - Прикрепление файла - Закрыть
     */
    public By Close = By.xpath("(//footer/button[contains(.,'Закрыть')])[2]");

    /**
     Направление на диагностику - Записать на прием
     */
    @FindBy(xpath = "//a[contains(.,'Записать на прием')]")
    public WebElement Reception;
    public By ReceptionWait = By.xpath("//a[contains(.,'Записать на прием')]");

    /**------------------- Создание пациента для консультации ------------------------ */

    /**
     Расширенный поиск
     */
    @FindBy(xpath = "//button//span[contains(.,'Расширенный поиск')]")
    public WebElement BigSearch;
    public By BigSearchWait = By.xpath("//button//span[contains(.,'Расширенный поиск')]");

    @FindBy(xpath = "//button//span[contains(.,'Создать пациента')]")
    public WebElement CreatePatient;
    public By CreatePatientWait = By.xpath("//button//span[contains(.,'Создать пациента')]");

    /**
     Расширенный поиск - Фамилия
     */
    public By BigLastName = By.xpath("(//input[@placeholder='Фамилия'])[3]");

    /**
     Расширенный поиск - Снилс
     */
    public By BigSnils = By.xpath("//input[@placeholder='СНИЛС']");

    /**
     Расширенный поиск - Поиск
     */
    public By SearchWait (String value) {
        By SearchWait = By.xpath("(//button[@type='submit'])[" + value + "]");
        return SearchWait;
    }

    /**
     Расширенный поиск - Поиск - мать без снилс
     */
    public By SearchMother = By.xpath("//td[contains(.,'мать без снилс')]");

    /**
     Расширенный поиск - количество найденных пациентов
     */
    public By PatientList = By.xpath("//tbody/tr[@class='el-table__row cursor-pointer']");

    /**
     Расширенный поиск - Выбор количества найденных пациентов
     */
    public By PatientListNumber = By.xpath("(//div[@class='grid-table__pagination']//input[@readonly='readonly'])[2]");

    /**
     Расширенный поиск - Выбор количества найденных пациентов - 20
     */
    public By PatientListNumber20 = By.xpath("//div[@x-placement]//li[contains(.,'20')]");

    /**
     Расширенный поиск - Выбор количества найденных пациентов - 30
     */
    public By PatientListNumber30 = By.xpath("//div[@x-placement]//li[contains(.,'30')]");

    /**
     Расширенный поиск - Выбор количества найденных пациентов - Загрузка
     */
    public By Loading = By.xpath("//div[@class='grid-table']//div[@class='el-loading-spinner']");

    /**
     Расширенный поиск - Список пациентов - Первый - ЕНП
     */
    @FindBy(xpath = "((//tbody)[2]/tr)[1]/td[6]//span")
    public WebElement listPatientFirstENP;

    /**
     Расширенный поиск - Список пациентов - Пятый - ЕНП
     */
    @FindBy(xpath = "((//tbody)[2]/tr)[5]/td[6]//span")
    public WebElement listPatientENP;

    /**
     Расширенный поиск - Список пациентов - Первый
     */
    public By listPatientFirst = By.xpath("(//tbody)[2]/tr[1]");

    /**
     Расширенный поиск - Список пациентов - Первый (ещё один вариант)
     */
    public By listPatientFirst2 = By.xpath("//tbody/tr");

    /**
     Расширенный поиск - Список пациентов - Первый - Снилс
     */
    public By listPatientFirstnils = By.xpath("(//tbody)[2]/tr[1]/td[5]//span");

    /**
     Расширенный поиск - Список пациентов - Пятый - Снилс
     */
    @FindBy(xpath = "((//tbody)[2]/tr)[5]/td[5]//span")
    public WebElement listPatientSnils;
    public By listPatientSnilsWait = By.xpath("((//tbody)[2]/tr)[5]/td[5]//span");

    /**
     Расширенный поиск - Список пациентов - Нужная строка- Нужное поле
     */
    public By SnilsPatient (String value, String number) {
        By Snils = By.xpath("((//tbody)[2]/tr)[" + value + "]/td[" + number + "]//span");
        return Snils;
    }

    /**
     Расширенный поиск - Список пациентов - Выбрать
     */
    public By Choose (String value) {
        By Choose = By.xpath("(//button/span[contains(.,'Выбрать')])[" + value + "]");
        return Choose;
    }

    /**
     Расширенный поиск - Список пациентов - Выбрать - Создание данных для матери новорожденного
     */
    public By AddMother = By.xpath("//button/span[contains(.,'Добавить')]");

    /**
     Расширенный поиск - Диагноз
     */
    public By Diagnose = By.xpath("//div[contains(.,'Диагноз:')]/following-sibling::span");

    /**
     Расширенный поиск - Диагноз - Поле ввода
     */
    public By DiagnoseSearch = By.xpath("//input[@placeholder='Введите код регистра']");

    /**
     Расширенный поиск - Диагноз - Поле ввода - Сохранить
     */
    public By DiagnoseSearchSave = By.xpath("//button/span[contains(.,'Сохранить')]");

    /**
     Расширенный поиск - Записать на консультацию
     */
    public By WriteConsul = By.xpath("//button/span[contains(.,'Записать на консультацию')]");

    /**
     Расширенный поиск - Изменить пациента для консультации
     */
    public By ChangePatient = By.xpath("//button/span[contains(.,'Изменить пациента для консультации')]");

    /**
     Создание пациента для консультации - Введите снилс
     */
    @FindBy(xpath = "(//input[@placeholder='Введите СНИЛС'])[1]")
    public WebElement AddSnils;

    /**
     Сбросить форму
     */
    public By ResetForm = By.xpath("//button//span[contains(.,'Сбросить форму')]");

    /**
     Создание пациента для консультации - направления на удаленную консультацию
     */
    public By ReferralsRemoteConsultation = By.xpath(
            "//div//span[contains(.,'направления на удаленную консультацию')]");

    /**
     Создание пациента для консультации - направления на диагностику
     */
    public By RemoteConsultationForDiagnostics = By.xpath("//div//span[contains(.,'направления на диагностику')]");

    /**
     Создание пациента для консультации - Пациент (ФИО)
     */
    public By PatientFIO = By.xpath("//label[contains(.,'Пациент (ФИО)')]");

    /**
     Создание пациента для консультации - Отсутствие СНИЛС
     */
    public By NotSnils = By.xpath("//input[@placeholder='Отсутствует']");

    /**
     Создание пациента для консультации - Отсутствие СНИЛС - ребенок в возрасте до 2 мес
     */
    public By NotSnils2Mouth = By.xpath("(//div[@x-placement]//li[contains(.,'ребенок в возрасте до 2 мес')])[2]");

    /**
     Создание пациента для консультации - Снилс Матери - Поиск
     */
    public By MatherSnils = By.xpath(
            "//label[contains(.,'СНИЛС матери')]/following-sibling::div//button[contains(.,'Поиск')]");

    /**
     Создание пациента для консультации - Фамилия
     */
    @FindBy(xpath = "(//input[@placeholder='Фамилия'])[1]")
    public WebElement LastName;
    public By LastNameWait = By.xpath("(//input[@placeholder='Фамилия'])[1]");

    /**
     Создание пациента для консультации - Имя
     */
    @FindBy(xpath = "(//input[@placeholder='Имя'])[1]")
    public WebElement Name;

    /**
     Создание пациента для консультации - Отчество
     */
    @FindBy(xpath = "(//input[@placeholder='Отчество'])[1]")
    public WebElement MiddleName;

    /**
     Создание пациента для консультации - Любое поле
     */
    public By InputPatient (String value) {
        By Str = By.xpath("(//input[@placeholder='" + value + "'])[1]");
        return Str;
    }

    /**
     Создание пациента для консультации - Ошибка не более 40 символов
     */
    public By Error40 = By.xpath("//div[@class='el-form-item__error'][contains(.,'Не больше 40 символов')]");

    /**
     Создание пациента для консультации - Укажите дату
     */
    @FindBy(xpath = "//input[@placeholder = 'Укажите дату']")
    public WebElement Date;
    public By DateWait = By.xpath("//input[@placeholder = 'Укажите дату']");
    public By SelectDate = By.xpath("(//div[@x-placement]//td[@class='available today'])[1]");

    /**
     Создание пациента для консультации - Укажите дату - год
     */
    @FindBy(xpath = "(//span[@role='button'])[1]")
    public WebElement Year;
    public By YearWait = By.xpath("(//span[@role='button'])[1]");

    /**
     Создание пациента для консультации - Укажите дату - Предыдущий год
     */
    @FindBy(xpath = "//button[@aria-label='Предыдущий год']")
    public WebElement BeforeYear;
    public By BeforeYearWait = By.xpath("//button[@aria-label='Предыдущий год']");

    /**
     Создание пациента для консультации - Укажите дату - Следующий год
     */
    @FindBy(xpath = "//button[@aria-label='Следующий год']")
    public WebElement AfterYear;
    public By AfterYearWait = By.xpath("//button[@aria-label='Следующий год']");

    /**
     Создание пациента для консультации - Укажите дату - Предыдущий месяц
     */
    public By BeforeMonth = By.xpath("//button[@aria-label='Предыдущий месяц']");

    /**
     Создание пациента для консультации - Укажите дату - Первое число месяца
     */
    public By DayOne = By.xpath("(//table[@class='el-date-table']//tr/td[@class='available'])[1]");

    /**
     Создание пациента для консультации - Укажите дату - Текущее число месяца
     */
    public By Today = By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=7])[1]");

    /**
     Создание пациента для консультации - Укажите Время
     */
    public By Time = By.xpath("//input[@placeholder = 'Укажите время']");

    /**
     Создание пациента для консультации - Введите серию справки о рождении
     */
    public By SerialBorn = By.xpath("//input[@placeholder = 'Введите серию справки о рождении']");

    /**
     Создание пациента для консультации - Введите номер справки о рождении
     */
    public By NumberBorn = By.xpath("//input[@placeholder = 'Введите номер справки о рождении']");

    /**
     Создание пациента для консультации - номер ребёнка
     */
    public By NumberChild = By.xpath("//input[@placeholder = 'Номер']");

    /**
     Создание пациента для консультации - Пол
     */
    @FindBy(xpath = "(//div//span[contains(.,'Мужской')])[1]")
    public WebElement Man;
    public By ManWait = By.xpath("(//div//span[contains(.,'Мужской')])[1]");

    /**
     Создание пациента для консультации - Выбранный Пол - Мужской
     */
    public By ManWaitTrue = By.xpath("//label[@aria-checked='true']//span[contains(.,'Мужской')]");

    /**
     Создание пациента для консультации - Выбранный Пол - Женский
     */
    public By WomenWaitTrue = By.xpath("//label[@aria-checked='true']//span[contains(.,'Женский')]");

    /**
     Создание пациента для консультации - Гражданство - Ошибка
     */
    public By CitizenshipError = By.xpath(
            "//label[contains(.,'Гражданство')]/following-sibling::div[contains(.,'Поле обязателено для заполнения')]");

    /**
     Создание пациента для консультации - Гражданство
     */
    public By Citizenship = By.xpath(
            "//label[contains(.,'Гражданство')]/following-sibling::div//input[@placeholder='Введите значение для поиска']");

    /**
     Создание пациента для консультации - Социальный статус
     */
    public By Status = By.xpath("(//label[contains(.,'Социальный статус')]/following-sibling::div//input)[1]");
    public By StatusError = By.xpath(
            "//label[contains(.,'Социальный статус')]/following-sibling::div[contains(.,'Поле обязателено для заполнения')]");
    public By StatusSelectT = By.xpath("//div[@x-placement]//ul/li/span");
    public By StatusSelectJob = By.xpath("//div[@x-placement='bottom-start']//ul/li/span[text()='Работающий']");

    /**
     Создание пациента для консультации - Выбран Вахтовик - Нет
     */
    public By VahtFalse = By.xpath("//label[@aria-checked='true']//span[contains(.,'Нет')]");

    /**
     Создание пациента для консультации - Выбран Вахтовик - Нет
     */
    public By VahtTrue = By.xpath("//label[@aria-checked='true']//span[contains(.,'Да')]");

    /**
     Создание пациента для консультации - Выберите тип документа
     */
    public By TypeDocument = By.xpath("//input[@placeholder = 'Выберите тип документа']");
    public By SelectTypeDocument = By.xpath("//div//span[contains(.,'Паспорт гражданина РФ')]");
    public By SelectTypeDocumentSR = By.xpath("//div//span[contains(.,'Свидетельство о рождении')]");

    /**
     Создание пациента для консультации - Серия
     */
    @FindBy(xpath = "//input[@placeholder='Введите серию документа']")
    public WebElement Serial;
    public By SerialWait = By.xpath(
            "//input[@placeholder='Введите серию документа']");

    /**
     Создание пациента для консультации - Серия - Ошибка
     */
    public By SerialError = By.xpath(
            "//input[@placeholder='Введите серию документа']/ancestor::div[1]/following-sibling::div");

    /**
     Создание пациента для консультации - Номер
     */
    @FindBy(xpath = "//input[@placeholder='Введите номер документа']")
    public WebElement Number;

    /**
     Создание пациента для консультации - Номер - Ошибка
     */
    public By NumberError = By.xpath(
            "//input[@placeholder='Введите номер документа']/ancestor::div[1]/following-sibling::div");

    /**
     Создание пациента для консультации - Введите адрес постоянной регистрации
     */
    @FindBy(xpath = "//input[@placeholder='Введите адрес постоянной регистрации']")
    public WebElement Address;

    /**
     Создание пациента для консультации - Введите адрес проживания
     */
    @FindBy(xpath = "//input[@placeholder='Введите адрес проживания']")
    public WebElement AddressHome;

    /**
     Создание пациента для консультации - Bottom1
     */
    @FindBy(xpath = "//div[@x-placement='bottom-start']//li[1]")
    public WebElement Bottom1Click;
    public By Bottom1 = By.xpath("//div[@x-placement='bottom-start']//li[1]");
    public By Select = By.xpath("//div[@x-placement]//li[1]");

    /**
     Создание пациента для консультации - Информация о работе
     */
    public By Jobinfo = By.xpath("//div[@role='button'][contains(.,'Информация о работе')]");
    @FindBy(xpath = "//input[@placeholder='Введите информацию о месте работы']")
    public WebElement Job;
    @FindBy(xpath = "//input[@placeholder='Введите должность']")
    public WebElement PostJob;
    public By JobError = By.xpath(
            "//label[contains(.,'Место работы')]/following-sibling::div[contains(.,'Поле обязателено для заполнения')]");
    public By JobErrorSimvol = By.xpath("//div[@class='el-form-item__error'][contains(.,'Не больше 100 символов')]");
    public By JobPost = By.xpath(
            "//label[contains(.,'Должность')]/following-sibling::div[contains(.,'Поле обязателено для заполнения')]");

    /**
     Создание пациента для консультации - Данные о страховом полисе
     */
    public By Polis = By.xpath("//div[@role='button'][contains(.,'Данные о страховом полисе')]");

    /**
     Создание пациента для консультации - Данные о страховом полисе - Тип полиса / ЕНП(обязательное)
     */
    public By PolisTypeError = By.xpath(
            "//label[contains(.,'Тип полиса / ЕНП')]/following-sibling::div[contains(.,'Тип полиса обязателен для заполнения')]");

    /**
     Создание пациента для консультации - Данные о страховом полисе - Выберите тип полиса
     */
    @FindBy(xpath = "//input[@placeholder='Выберите тип полиса']")
    public WebElement PolisType;
    public By PolisTypeWait = By.xpath("//input[@placeholder='Выберите тип полиса']");
    public By PolisTypeSelect = By.xpath("//li/span[contains(.,'Полис ОМС старого образца')]");
    public By PolisTypeSelectEdin = By.xpath("//li/span[contains(.,'Полис ОМС единого образца')]");

    /**
     Создание пациента для консультации - Данные о страховом полисе - Введите значение ЕНП
     */
    @FindBy(xpath = "//input[@placeholder='Введите значение ЕНП']")
    public WebElement PolisENP;

    /**
     Создание пациента для консультации - Данные о страховом полисе - Тип полиса / ЕНП - Недоступно указание
     */
    public By PolisTypeError2 = By.xpath(
            "//input[@placeholder='Введите значение ЕНП']/ancestor::div[1]/following-sibling::div[contains(.,'Недоступно указание значения в поле «ЕНП»')]");

    /**
     Создание пациента для консультации - Данные о страховом полисе - Тип полиса / ЕНП - Длина должна равняться 16 символам
     */
    public By PolisTypeError3 = By.xpath(
            "//input[@placeholder='Введите значение ЕНП']/ancestor::div[1]/following-sibling::div[contains(.,'Длина должна равняться 16 символам')]");

    /**
     Создание пациента для консультации - Top1
     */
    @FindBy(xpath = "//div[@x-placement='top-start']//li[1]")
    public WebElement Top1Click;
    public By Top1 = By.xpath("//div[@x-placement='top-start']//li[1]");

    /**
     Создание пациента для консультации - Далее
     */
    @FindBy(xpath = "//footer/button[contains(.,'Далее')]")
    public WebElement NextPatient;
    public By NextPatientWait = By.xpath("//footer/button[contains(.,'Далее')]");

    /**
     Создание пациента для консультации - Назад
     */
    @FindBy(xpath = "//footer/button[contains(.,'Назад')]")
    public WebElement BackPatient;

    /**
     Создание пациента для консультации - Закрыть
     */
    @FindBy(xpath = "//footer/button[contains(.,'Закрыть')]")
    public WebElement ClosePatient;
    public By ClosePatientWait = By.xpath("//div[@class='el-message-box']");

    /**
     Создание пациента для консультации - Да, закрыть окно
     */
    @FindBy(xpath = "//div[@class='el-message-box']//span[contains(.,'Да, закрыть окно')]")
    public WebElement YesClosePatient;

    /**
     Создание пациента для консультации - Врач
     */
    @FindBy(xpath = "//label[@for='userId']/following-sibling::div//input")
    public WebElement Doctor;
    public By DoctorWait = By.xpath("//label[@for='userId']/following-sibling::div//input");
    public By SelectDoctor = By.xpath("//ul/li/span[contains(.,'Тестировщик Тест Тестович')]");
    public By SelectDoctorDuble = By.xpath("//ul/li/span[contains(.,'Тестировщик Тест Тестович')]");
    public By SelectDoctorFirst = By.xpath("//div[@x-placement]//ul/li[1]/span");
    public By SelectDoctorSecond = By.xpath("//div[@x-placement]//ul/li[2]/span");

    public By DoctorSearch (String value) {
        By DoctorSearch = By.xpath("//ul/li/span[contains(.,'" + value + "')]");
        return DoctorSearch;
    }

    /**
     Создание пациента для консультации - МО, куда направлен
     */
    public By MO = By.xpath("//label[contains(.,'МО, куда направлен')]");
    public By MOWait = By.xpath("//label[contains(.,'МО, куда направлен')]/following-sibling::div//input");
    public By SelectMO = By.xpath(
            "//ul//span[contains(.,'БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"')]");
    public By SelectMOOKB = By.xpath("//ul//span[contains(.,'БУ ХМАО-Югры \"Окружная клиническая больница\"')]");
    public By SelectMOKon = By.xpath(
            "//ul//span[contains(.,'АУ ХМАО-Югры \"Кондинская районная стоматологическая поликлиника\"')]");
    public By MyMO = By.xpath("//div[@class='el-select el-select--medium']/following-sibling::button");

    /**
     Создание пациента для консультации - Профиль
     */
    @FindBy(xpath = "(//input[@placeholder='Введите значение для поиска'])[3]")
    public WebElement Profile;
    public By ProfileWait = By.xpath("//label[contains(.,'Профиль')]/following-sibling::div//input");
    public By SelectProfileWaitKosmetologi = By.xpath("//div//ul/li/span[contains(.,'косметологии')]");
    public By SelectProfileWaitOptiki = By.xpath("//div//ul/li/span[contains(.,'медицинской оптике')]");
    public By SelectProfileFirst = By.xpath("//div[@x-placement]//ul/li[1]/span");

    /**
     Создание пациента для консультации - Диагноз
     */
    @FindBy(xpath = "//label[contains(.,'Диагноз')]/following-sibling::div//input")
    public WebElement Diagnos;
    public By DiagnosWait = By.xpath("//label[contains(.,'Диагноз')]/following-sibling::div//input");

    /**
     Создание пациента для консультации - Пациент в стационаре
     */
    public By Hospital (String value) {
        By str = By.xpath(
                "//label[contains(.,'Пациент в стационаре')]/following-sibling::div//span[contains(.,'" + value + "')]");
        return str;
    }

    /**
     Создание пациента для консультации - Форма консультации
     */
    public By Plan = By.xpath("(//label[contains(.,'Форма консультации')]/following-sibling::div//span)[1]");

    /**
     Создание пациента для консультации - Врач Консультант
     */
    public By SelectField (String value) {
        By Doctor = By.xpath("(//label[contains(.,'" + value + "')]/following-sibling::div//span)[1]");
        return Doctor;
    }

    /**
     Создание пациента для консультации - Тип консультации
     */
    public By TypeConsul = By.xpath("//label[contains(.,'Тип консультации')]/following-sibling::div//input");

    /**
     Форма консультации - Выбор из кнопок
     */
    public By FormConsul (String label, String value) {
        By FormConsul = By.xpath(
                "//label[contains(.,'" + label + "')]/following-sibling::div//span[contains(.,'" + value + "')]");
        return FormConsul;
    }

    /**
     Цель консультации
     */
    public By Goal = By.xpath("//label[contains(.,'Цель консультации')]/following-sibling::div//input");
    public By SelectGoal = By.xpath("//div[@x-placement]//ul/li[3]");
    public By SelectCovid = By.xpath("//div[@x-placement]//ul/li[contains(.,'Подозрение на COVID-19')]");
    public By SelectUtoch = By.xpath("//div[@x-placement]//ul/li[contains(.,'Уточнение диагноза')]");
    public By SelectOchnoe = By.xpath("//div[@x-placement]//ul/li[contains(.,'Очное консультирование')]");

    /**
     Создание удалённой консультации - Текстовые поля
     */
    public By Text (String value) {
        By Text = By.xpath("//label[contains(.,'" + value + "')]/following-sibling::div//textarea");
        return Text;
    }

    /**
     Создание удалённой консультации - Описание заполненных полей
     */
    public By TextWait (String value) {
        By Text = By.xpath("//td[contains(.,'" + value + "')]/following-sibling::td");
        return Text;
    }

    /**
     Создание направления на удаленную консультацию - Создать консультацию
     */
    public By CreateConsul = By.xpath("//footer/button[contains(.,'Создать консультацию')]");

    /**
     Создание направления на удаленную консультацию - Изменить консультацию
     */
    public By SetConsul = By.xpath("//footer/button[contains(.,'Изменить консультацию')]");

    /**
     Создание направления на удаленную консультацию - Далее
     */
    public By NextConsul = By.xpath("//footer/button[contains(.,'Далее')]");

    /**
     Создание направления на удаленную консультацию - Закрыть
     */
    public By CloseConsul = By.xpath("//footer/button[contains(.,'Закрыть')]");

    /**
     Создание направления на удаленную консультацию - Закрыть - Да закрыть
     */
    public By CloseConsulYes = By.xpath("//button[contains(.,'Да, закрыть окно')]");

    /**
     Создать консультацию - Добавить файлы
     */
    @FindBy(xpath = "//label[@for='file-upload-context-after-create'][contains(.,'Добавить файлы')]")
    public WebElement AddFiles;
    @FindBy(xpath = "//input[@type='file']")
    public WebElement AddFilesTwo;
    public By AddFilesWait = By.xpath("//label[@for='file-upload-context-after-create'][contains(.,'Добавить файлы')]");

    /**
     Добавить файлы - Отправить консультацию
     */
    public By SendConsul = By.xpath(
            "//footer/button[@class='el-button el-button--success el-button--medium'][contains(.,'Отправить консультацию')]");

    /**
     Добавить файлы - Запись на приём
     */
    public By SendReception = By.xpath(
            "//footer/button[@class='el-button el-button--primary el-button--medium'][contains(.,'Запись на прием')]");

    /**
     Добавить файлы - Закрыть
     */
    public By CloseReception = By.xpath(
            "(//footer/button[@class='el-button el-button--default el-button--medium'][contains(.,'Закрыть')])[2]");

    /**
     Отправить консультацию - Ошибка - Введены недопустимые символы в имени
     */
    public By ErrorName = By.xpath(
            "//h2[contains(.,'Ошибка')]/following-sibling::div/p[contains(.,'Введены недопустимые символы в имени;')]");

    /**
     Запись на приём - Показывать только рабочие часы
     */
    public By WorkTime = By.xpath("//button[contains(.,'Показывать только рабочие часы')]");

    /**
     Запись на приём - Показывать только рабочие часы
     */
    public By AllDay = By.xpath("//button[contains(.,'Показывать весь день')]");

    /**
     Запись на приём - Недоступный слот с определённым временем
     */
    public By SlotDisable (String time) {
        By SlotDisable = By.xpath("//div[@class='el-tooltip item event-info disabled']/p[contains(.,'" + time + "')]");
        return SlotDisable;
    }

    /**
     Запись на приём - Доступный слот с определённым временем
     */
    public By SlotActive (String time) {
        By SlotDisable = By.xpath("//div[@class='el-tooltip item event-info free']/p[contains(.,'" + time + "')]");
        return SlotDisable;
    }

    /**
     Запись на приём - Недоступный слот последний
     */
    public By SlotBusy = By.xpath("(//div[@class='el-tooltip item event-info busy'])[last()]");

    /**
     Запись на приём - Недоступный слот последний - Отображается Имя пациента
     */
    public By SlotBusyName = By.xpath(
            "(//div[@class='el-tooltip item event-info busy'])[last()][contains(.,'Тестировщик Т. Т.')]");

    /**
     Запись на приём - Уведомление, что слот не доступен
     */
    public By SlotNotAvailable = By.xpath(
            "//div[@x-placement]//li[contains(.,'Данный слот недоступен для записи из вашей медицинской организации. Обратитесь в МО')]");

    /**
     Метод создания удалённой консультации
     MyMO - true = в МО, по которой авторизовались, false = в другую МО
     Doctor - Врач назначающий консультацию
     ClickDirection - Условие надо ли перед созданием коснультации заходить в "Добавить консультацию"
     */
    @Step("Метод создания удалённой консультации и заполнении информации о направившем враче")
    public void CreateRemoteConsul (Boolean MyMO, String MO, String snils, String DoctorName, String Division, String profile, String FormConsul, String Purpose, String Diagnose) throws InterruptedException, IOException {
        directionsForQuotas = new DirectionsForQuotas(driver);
        authorizationObject = new AuthorizationObject(driver);

        System.out.println("Создание удалённой консультации");
        if (!BaseAPI.ReadPropFile("className").equals("Access_1510Test") &
                !BaseAPI.ReadPropFile("className").equals("Access_1811Test") &
                !BaseAPI.ReadPropFile("className").equals("Access_1870Test") &
                !BaseAPI.ReadPropFile("className").equals("Access_1872Test")) {
            ClickElement(directionsForQuotas.RemoteConsultationWait);
            ClickElement(directionsForQuotas.NextWait);
            WaitElement(directionsForQuotas.SnilsWait);
            directionsForQuotas.Snils.sendKeys(snils);
            WaitElement(directionsForQuotas.PatientDataWait);
            if (isElementVisibleTime(directionsForQuotas.CitizenshipError, 2)) {
                ClickElement(directionsForQuotas.Citizenship);
                ClickElement(authorizationObject.Select("РОССИЯ Российская Федерация"));
            }
            WaitElement(directionsForQuotas.NextWait);
            Thread.sleep(1000);
            actionElementAndClick(directionsForQuotas.Next);
        } else {
            ClickElement(directionsForQuotas.NextWait);
        }

        System.out.println("Заполняем данные");
        SelectClickMethod(directionsForQuotas.DoctorWait, directionsForQuotas.DoctorSearch(DoctorName));
        if (!MyMO) {
            if (KingNumber == 1 | KingNumber == 2) {
                SelectClickMethod(directionsForQuotas.MOWait, authorizationObject.Select(MO));
            }
            if (KingNumber == 4) {
                SelectClickMethod(directionsForQuotas.MOWait, authorizationObject.Select(MO));
            }
        } else {
            ClickElement(directionsForQuotas.MyMO);
            SelectClickMethod(directionsForQuotas.MyDivision, authorizationObject.Select(Division));
        }
        SelectClickMethod(directionsForQuotas.ProfileWait, authorizationObject.Select(profile));
        if (ReadPropFile("className").equals("Access_3186Test") ||
                DoctorConsul) {
            ClickElement(directionsForQuotas.SelectField("Врач консультант"));
            ClickElement(authorizationObject.Select("Зотин Андрей Владимирович"));
        }
        ClickElement(directionsForQuotas.FormConsul("Форма консультации", FormConsul)); // Форма консультации
        ClickElement(directionsForQuotas.FormConsul("Вид консультации", "Врач - Врач - Пациент")); // Вид консультации
        SelectClickMethod(directionsForQuotas.Goal, authorizationObject.Select(Purpose)); // Цель консультации
        inputWord(driver.findElement(directionsForQuotas.TypeConsul), "конс");
        ClickElement(authorizationObject.Select("Консультация детского уролога-андролога"));
        inputWord(directionsForQuotas.Diagnos, Diagnose);
        Thread.sleep(1000);
        ClickElement(authorizationObject.SelectFirst);

        // Пациент в стационаре (3737)
        ClickElement(directionsForQuotas.Hospital("Да"));

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
    }

    @Step("Метод создания консультации на оборудование")
    public void CreateConsultationEquipment (Boolean ClickDirection, String snils, By division, String anatonObl, Boolean MyMO, String Mo, By diagnosis, By Research, String Mass, boolean cito, boolean createDirection, boolean writeConsul, String equipment) throws InterruptedException {
        this.authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);

        /** Условие надо ли перед созданием коснультации заходить в "Добавить консультацию" **/
        if (ClickDirection) {
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
        }
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys(snils);
        WaitElement(directionsForQuotas.PatientDataWait);
        if (isElementVisibleTime(directionsForQuotas.CitizenshipError, 2)) {
            ClickElement(directionsForQuotas.Citizenship);
            ClickElement(authorizationObject.Select("РОССИЯ Российская Федерация"));
        }
        ClickElement(directionsForQuotas.NextWait);

        System.out.println("Заполнение информации о направившем враче");
        WaitElement(directionsForQuotas.InfoDoctorWait);
        WaitElement(directionsForQuotas.SubmittingDoctorWait);
        Thread.sleep(2000);
        SelectClickMethod(directionsForQuotas.SubmittingDoctorWait, directionsForQuotas.FIO);
        SelectClickMethod(directionsForQuotas.Division, division);
        SelectClickMethod(directionsForQuotas.Post, directionsForQuotas.SelectPost);
        SelectClickMethod(directionsForQuotas.Specialization, directionsForQuotas.SelectSpecializationFirst);
        WaitNotElement3(directionsForQuotas.LoadingDepartment, 30);
        SelectClickMethod(directionsForQuotas.DepartmentWait, authorizationObject.SelectFirst);
        driver.findElement(directionsForQuotas.AnatomicalAreas).sendKeys(anatonObl);
        ClickElement(authorizationObject.Select(anatonObl));
        if (MyMO) {
            ClickElement(directionsForQuotas.MyMODirection);
            Thread.sleep(2000);
            SelectClickMethod(directionsForQuotas.MyDivision, directionsForQuotas.MySelectDivision);
        }
        if (!MyMO) {
            SelectClickMethod(directionsForQuotas.MODirection, SelectMOAll(Mo));
        }
        directionsForQuotas.Diagnosis.sendKeys(Keys.SPACE);
        WaitElement(this.authorizationObject.Xplacement);
        Thread.sleep(1000);
        ClickElement(diagnosis);
        SelectClickMethod(directionsForQuotas.Research, Research);
        SelectClickMethod(directionsForQuotas.BenefitCode, directionsForQuotas.SelectBenefitCode);
        WaitElement(directionsForQuotas.MassWait);
        inputWord(directionsForQuotas.Mass, Mass + "0");
        System.out.println("Вес пациента " + Mass);
        inputWord(directionsForQuotas.NamePatient, "Тестовыйй");
        inputWord(directionsForQuotas.LastNamePatient, "Тестт");
        inputWord(directionsForQuotas.MiddleNamePatient, "Тестовичч");
        if (cito) {
            ClickElement(directionsForQuotas.Cito);
        }
        ClickElement(directionsForQuotas.NextPatientWait);

        /** Окно направления на диагностику */
        if (createDirection) {
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
            WaitElement(directionsForQuotas.ReceptionWait);
            if (!writeConsul) {
                ClickElement(directionsForQuotas.Close);
            } else {
                System.out.println("Запись на приём");
                ClickElement(directionsForQuotas.ReceptionWait);
                Thread.sleep(1500);
                authorizationObject.LoadingTime(40);
                equipmentSchedule.CheckDirection(equipment);
                ClickElement(equipmentSchedule.SlotsFreeWait);
                if (isElementNotVisible(equipmentSchedule.AddFileWait)) {
                    file = new File("src/test/resources/test.txt");
                    equipmentSchedule.AddFile.sendKeys(file.getAbsolutePath());
                    Thread.sleep(1500);
                }
                ClickElement(equipmentSchedule.WriteTwo);
                Thread.sleep(1500);
                authorizationObject.LoadingTime(30);
                ClickElement(equipmentSchedule.AlertCloseWait);
                System.out.println("Запись на прием успешна создана!");
            }
        }
    }

    @Step("Метод для записи направлени на слот и смена статуса с 6 на 3")
    public void AddFile (String MO, String idDirection, String nameEquipment) throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);

        AuthorizationMethod(authorizationObject.Select(MO));
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.SortDesc);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(20);
        ClickElement(directionsForQuotas.GetDirection(idDirection, "1"));
        System.out.println("Прикрепление  файла");
        ClickElement(directionsForQuotas.Addfiles);
        Thread.sleep(1000);
        java.io.File file = new File("src/test/resources/test.txt");
        directionsForQuotas.File.sendKeys(file.getAbsolutePath());
        Thread.sleep(1500);
        ClickElement(directionsForQuotas.Clouses);
        ClickElement(directionsForQuotas.AddReception);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(40);
        equipmentSchedule.CheckDirection(nameEquipment);
        ClickElement(equipmentSchedule.SlotsFreeWait);
        if (isElementNotVisible(equipmentSchedule.AddFileWait)) {
            file = new File("src/test/resources/test.txt");
            equipmentSchedule.AddFile.sendKeys(file.getAbsolutePath());
            Thread.sleep(1500);
        }
        ClickElement(equipmentSchedule.WriteTwo);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(30);
        ClickElement(equipmentSchedule.AlertCloseWait);
        System.out.println("Запись на прием успешна создана!");
        Thread.sleep(1500);
    }

    /**
     Метод на заполнение информации о направившем враче
     **/
    public void DoctorMethod () throws InterruptedException {
        directionsForQuotas = new DirectionsForQuotas(driver);
        authorizationObject = new AuthorizationObject(driver);

        System.out.println("Заполнение информации о направившем враче");
        WaitElement(directionsForQuotas.InfoDoctorWait);
        WaitElement(directionsForQuotas.SubmittingDoctorWait);
        Thread.sleep(2000);
        SelectClickMethod(
                directionsForQuotas.SubmittingDoctorWait,
                directionsForQuotas.FIO
        );
        if (KingNumber == 4) {
            Thread.sleep(2500);
        }
        SelectClickMethod(
                directionsForQuotas.Division,
                directionsForQuotas.SelectDivision
        );
        Thread.sleep(1500);
        WaitNotElement3(directionsForQuotas.LoadingDepartment, 20);

        SelectClickMethod(directionsForQuotas.DepartmentWait, directionsForQuotas.SelectDepartment);
        if (KingNumber == 4) {
            Thread.sleep(2500);
        }
        SelectClickMethod(directionsForQuotas.Post,
                directionsForQuotas.SelectPost);
        if (KingNumber == 4) {
            Thread.sleep(2500);
        }
        SelectClickMethod(
                directionsForQuotas.Specialization,
                directionsForQuotas.SelectSpecializationFirst
        );
        if (KingNumber == 4) {
            Thread.sleep(2500);
        }
        driver.findElement(directionsForQuotas.AnatomicalAreas).sendKeys("ис");
        ClickElement(authorizationObject.Select("Височная кость"));
        if (KingNumber == 4) {
            Thread.sleep(2500);
            ClickElement(directionsForQuotas.MyMODirection);
            Thread.sleep(2000);
            SelectClickMethod(directionsForQuotas.MyDivision, directionsForQuotas.MySelectDivision);
        } else {
            SelectClickMethod(directionsForQuotas.MODirection, directionsForQuotas.SelectMODirection);
        }
        directionsForQuotas.Diagnosis.sendKeys(Keys.SPACE);
        WaitElement(authorizationObject.BottomStart);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.SelectDiagnosis);
        if (KingNumber == 4) {
            if (KingNumber == 4) {
                Thread.sleep(1500);
            }
            SelectClickMethod(directionsForQuotas.Research, authorizationObject.SelectFirst);
        } else {
            SelectClickMethod(directionsForQuotas.Research, directionsForQuotas.SelectResearch);
        }
        SelectClickMethod(directionsForQuotas.BenefitCode, directionsForQuotas.SelectBenefitCode);
        WaitElement(directionsForQuotas.MassWait);
        inputWord(directionsForQuotas.Mass, "500");
        inputWord(directionsForQuotas.NamePatient, "Тестовыйй");
        inputWord(directionsForQuotas.LastNamePatient, "Тестт");
        inputWord(directionsForQuotas.MiddleNamePatient, "Тестовичч");
        actionElementAndClick(directionsForQuotas.NextPatient);
        /** Окно направления на диагностику*/
        WaitElement(directionsForQuotas.Header);
        WaitElement(directionsForQuotas.CreateDirectionWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.CreateDirection);
        /** Прикрепление файла */
        System.out.println("Прикрепление  файла");
        WaitElement(directionsForQuotas.FileWait);
        WaitElement(directionsForQuotas.AddFileWait);
        ClickElement(directionsForQuotas.Close);
    }

    /**
     Метод создания пациента в Консультации
     Snils - Снилс, который используем при поиске пациента
     **/
    public void CreatePatientMethod (String Snils) throws InterruptedException {
        directionsForQuotas = new DirectionsForQuotas(driver);
        authorizationObject = new AuthorizationObject(driver);
        Faker faker = new Faker();
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys(Snils);
        inputWord(directionsForQuotas.LastName, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
        inputWord(directionsForQuotas.Name, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
        inputWord(directionsForQuotas.MiddleName, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
        System.out.println("Заполняем данные о пациенте для консультации");
        SelectClickMethod(DateWait, SelectDate);
        ClickElement(directionsForQuotas.ManWait);
        SelectClickMethod(directionsForQuotas.TypeDocument, directionsForQuotas.SelectTypeDocument);
        inputWord(directionsForQuotas.Serial, "12344");
        inputWord(directionsForQuotas.Number, "1234566");
        actionElementAndClick(directionsForQuotas.Address);
        inputWord(directionsForQuotas.Address, "г Москва, ул Арбат, д 9АФ");
        ClickElement(directionsForQuotas.Select);
        actionElementAndClick(directionsForQuotas.AddressHome);
        ClickElement(directionsForQuotas.Select);
        actionElementAndClick(directionsForQuotas.NextPatient);
    }

    /**
     Метод только для создания консультации на оборудование
     **/
    public void CreateConsultationEquipmentMethod () throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);

        /** Переход в создание консультации на оборудование */
        WaitElement(By.linkText("Незавершенные"));
        actionElementAndClick(directionsForQuotas.Consultation);

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
    }

    /**
     Метод определения нужного запроса из списка Network
     message - запрос JavaScript
     word - искомый запрос из Network
     **/
    public int searchWord (String message, String word) {
        message = message.toLowerCase();
        word = word.toLowerCase();
        int i = message.indexOf(word);
        int count = 0;
        while (i >= 0) {
            count++;
            i = message.indexOf(word, i + 1);
        }
        CountAsk = count;
        return CountAsk;
    }

    /**
     Метод для проверки размера шрифта
     locator - локатор на веб странице
     **/
    public void AssertFontSizeMethod (By locator) {
        wait.until(visibilityOfElementLocated(locator));
        String element = driver.findElement(locator).getCssValue("font-size");
        Assertions.assertEquals("14px", element);
        System.out.println("---Значение на интерфейсе " + element + " совпадает с 14px");
    }
}
