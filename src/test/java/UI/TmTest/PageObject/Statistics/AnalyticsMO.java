package UI.TmTest.PageObject.Statistics;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


import java.util.List;

public class AnalyticsMO extends BaseTest {
    public static boolean TallMO;
    public static boolean AverageMO;
    public static boolean lowMO;

    /**
     * Статистика - Аналитика МО по ОМП
     */
    public AnalyticsMO(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Метод проверки, где есть маршруты
     */
    public void QuantityStackMethod() {
        for (int i = 1; i < 4; i++) {
            List<WebElement> Quantity = driver.findElements(By.xpath(
                    "//div[@class='rating-mo-grid']/div[@class='rating-mo-grid-info'][" + i + "]/div[@class='rating-mo-grid']/div"));
            if (Quantity.size() > 0 && i == 1) {
                TallMO = true;
                System.out.println("Есть МО в Высокий");
            }
            if (Quantity.size() > 0 && i == 2) {
                AverageMO = true;
                System.out.println("Есть МО в Средний");
            }
            if (Quantity.size() > 0 && i == 3) {
                lowMO = true;
                System.out.println("Есть МО в Низкий");
            }
        }
    }

    /**
     * Статистика - Аналитика МО по ОМП
     */
    public By Analytics = By.xpath("//div/a[@href='/stats/rating-mo']");

    /**
     * Аналитика МО по ОМП - Фильтр
     */
    public By Filter = By.xpath("//div/span[contains(.,'Фильтр')]");

    /**
     * Аналитика МО по ОМП - Выбрать мед. организацию
     */
    public By ChooseMO = By.xpath("//div[@class='rating-mo-filter-mo']");

    /**
     * Аналитика МО по ОМП - Выбрать диагноз
     */
    public By ChooseDiagnosis = By.xpath("//div[@class='rating-mo-filter-mkb']");

    /**
     * Аналитика МО по ОМП - Выбрать направление
     */
    public By Direction = By.xpath("//div[@class='rating-mo-filter-sms-direction-type']");

    /**
     * Выбрать направление - онкология
     */
    public By Onko = By.xpath("//div[@x-placement]//li/span[contains(.,'1 Онкология')]");

    /**
     * Выбрать направление - профилактика
     */
    public By Prev = By.xpath("//div[@x-placement]//li/span[contains(.,'2 Профилактика')]");

    /**
     * Выбрать направление - акинео
     */
    public By Akineo = By.xpath("//div[@x-placement]//li/span[contains(.,'3 Акушерство и неонатология')]");

    /**
     * Выбрать направление - ссз
     */
    public By SSZ = By.xpath("//div[@x-placement]//li/span[contains(.,'4 Сердечно-сосудистые заболевания')]");

    /**
     * Выбрать направление - иные профили
     */
    public By Other = By.xpath("//div[@x-placement]//li/span[contains(.,'99 Иные профили')]");

    /**
     * Аналитика МО по ОМП - Фильтр - Применить
     */
    public By Apply = By.xpath("//button/span[contains(.,'Применить')]");

    /**
     * Аналитика МО по ОМП - Фильтр - Применить - Загрузка
     */
    public By ApplyLoading = By.xpath("//button[@disabled='disabled']/span[contains(.,'Применить')]");

    /**
     * Аналитика МО по ОМП - Этапы ОМП
     */
    public By StateHeader = By.xpath("//h3[contains(.,'Этапы ОМП')]");

    /**
     * Аналитика МО по ОМП - Высокий - Количество МО
     */
    public By QuantityState = By.xpath("//ul[@class='stages-wmd__list']/li");

    /**
     * Аналитика МО по ОМП - Высокий
     */
    public By Tall = By.xpath("//section/div/div/span[contains(.,'Высокий')]");

    /**
     * Аналитика МО по ОМП - Высокий - Название 1 МО
     */
    public By NameMOTallFirst = By.xpath(
            "//section/div/div/span[contains(.,'Высокий')]/following-sibling::div/div[1]//p");

    /**
     * Аналитика МО по ОМП - Высокий - Количество МО
     */
    public By QuantityRoutestTall = By.xpath(
            "//section/div/div/span[contains(.,'Высокий')]/following-sibling::div[@class='rating-mo-grid']/div");

    /**
     * Аналитика МО по ОМП - Средний
     */
    public By Average = By.xpath("//section/div/div/span[contains(.,'Средний')]");

    /**
     * Аналитика МО по ОМП - Средний - Название 1 МО
     */
    public By NameMOAverageFirst = By.xpath(
            "//section/div/div/span[contains(.,'Средний')]/following-sibling::div/div[1]//p");

    /**
     * Аналитика МО по ОМП - Средний - Количество МО
     */
    public By QuantityRoutestAverage = By.xpath(
            "//section/div/div/span[contains(.,'Средний')]/following-sibling::div[@class='rating-mo-grid']/div");

    /**
     * Аналитика МО по ОМП - Низкий
     */
    public By low = By.xpath("//section/div/div/span[contains(.,'Низкий')]");

    /**
     * Аналитика МО по ОМП - Низкий - Название 1 МО
     */
    public By NameMOlowFirst = By.xpath(
            "//section/div/div/span[contains(.,'Низкий')]/following-sibling::div/div[1]//p");

    /**
     * Аналитика МО по ОМП - Низкий - Количество МО
     */
    public By QuantityRoutestlow = By.xpath(
            "//section/div/div/span[contains(.,'Низкий')]/following-sibling::div[@class='rating-mo-grid']/div");

    /**
     * Выбранная МО - Загрузка пацинтов
     */
    public By Loading = By.xpath("(//div[@class='el-loading-spinner'])[2]");

    /**
     * Выбранная МО - Чек бокс без отклонений
     */
    public By CheckBez = By.xpath("//div[@aria-label='checkbox-group']/label[1]/span[1]");

    /**
     * Выбранная МО - Чек бокс с отклонениями
     */
    public By CheckS = By.xpath("//div[@aria-label='checkbox-group']/label[2]/span[1]");

    /**
     * Выбранная МО - 1 Пациент
     */
    public By FirstPatient = By.xpath("//tbody/tr[1]/td[1]");

    /**
     * Выбранная МО - 2 Пациент
     */
    public By SecondPatient = By.xpath("//tbody/tr[last()]/td[1]");

    /**
     * Выбранная МО - Все пациенты
     */
    public By PatientAll = By.xpath("//tbody/tr");

    /**
     * Выбранная МО - Пациент - Отклонения по маршруту пациента - Заголовок
     */
    public By DeviationHeader = By.xpath("//h3[contains(.,'Отклонения по маршруту пациента')]");

    /**
     * Выбранная МО - Пациент - Отклонения по маршруту пациента - Тип отклонения (все значения)
     */
    public By TypeDeviation = By.xpath("//tbody/tr[@class='el-table__row deviation-table-text']/td[1]/div");

    /**
     * Выбранная МО - Пациент - Отклонения по маршруту пациента - Тип отклонения (все значения)
     */
    public By Deviation = By.xpath("//tbody/tr[@class='el-table__row deviation-table-text']/td[2]/div");

    /**
     * Выбранная МО - Пациент - Отклонения по маршруту пациента - Количество отклонений по клиническим рекомендациям
     */
    public By KR = By.xpath("//p[@class='deviation-line'][1]");

    /**
     * Выбранная МО - Пациент - Отклонения по маршруту пациента - Количество отклонений по ПГГ
     */
    public By PGG = By.xpath("//p[@class='deviation-line'][2]");

    /**
     * Выбранная МО - Пациент - Отклонения по маршруту пациента - Порядок ОМП
     */
    public By OMP = By.xpath("//p[@class='deviation-line'][3]");

    /**
     * Выбранная МО - Пациент - Отклонения по маршруту пациента - Закрыть
     */
    public By Close = By.xpath("(//button[@aria-label='Close'])[1]");

    /**
     * Выбранная МО - Пациент - Пациент не найден
     */
    public By NotPatient = By.xpath("//div[@role='alert']//p[contains(.,'Пациент не найден')]");

    /**
     * Пациент - Блок вверху с ФИО
     */
    public By BlockFIO = By.xpath("//div[@class='vimis-document-fio']");

    /**
     * Пациент - ФИО
     */
    public By FIO = By.xpath("//h3[contains(.,'Пациент')]/following-sibling::h4");

    /**
     * Пациент - Дата
     */
    public By Date = By.xpath("//h3[contains(.,'Дата рождения')]/following-sibling::h4");

    /**
     * Пациент - Телефон
     */
    public By Phone = By.xpath("//span[contains(.,'Телефон')]/following-sibling::span[1]");

    /**
     * Пациент - СНИЛС
     */
    public By Snils = By.xpath("//h3[contains(.,'СНИЛС')]/following-sibling::h4");

    /**
     * Пациент - Количество диагнозов
     */
    public By QuantityDiagnosis = By.xpath("//li/div[contains(.,'Диспансерное наблюдение')]/following-sibling::div//ul/li");

    /**
     * Пациент - Диспансеризация
     */
    public By MedicalExamination = By.xpath("//li[@class='patient-details__item'][contains(.,'Диспансеризация')]");

    /**
     * Пациент - Иммунизация
     */
    public By Immunization = By.xpath("//li[@class='patient-details__item'][contains(.,'Иммунизация')]");

    /**
     * Пациент - Вакцинация
     */
    public By Vaccination = By.xpath("//li[@class='patient-details__item'][contains(.,'Вакцинация')]");

    /**
     * Пациент - 1 Диагноз
     */
    public By FirstDiagnosis = By.xpath("//ul[@class='patient-details__content-list']/li[1]//span");

    /**
     * Пациент - Блок РРЭМД
     */
    public By RREMD = By.xpath("//section/header[contains(.,'РРЭМД')]");

    /**
     * Пациент - Диагноз - Загрузка маршрута ОМП
     */
    public By LoadingOMP = By.xpath(
            "//body[@class='el-loading-parent--relative el-loading-parent--hidden']//div[@class='el-loading-mask is-fullscreen']/div[@class='el-loading-spinner']");

    /**
     * Пациент - Маршрут
     */
    public By Route = By.xpath("//a[@href='/stats/pmc-patients/65833799054/diagnosis/C16/route']");

    /**
     * Пациент - 1 Маршрут
     */
    public By RouteFirst = By.xpath("//ul[@class='patient-details__content-list']/li[1]//span");

    /**
     * Пациент - Маршрут - Открытый Маршрут
     */
    public By Route1 = By.xpath("//div[@style='background-color:#F6F0F6;border-color:var(--pinkDD)']");
    public By Route2 = By.xpath("//div[@style='padding-top:134px']");
    public By Route3 = By.xpath("//div[@style='flex-wrap:wrap']");
    public By Route4 = By.xpath("//section[@class='patient-route']");

    /**
     * Пациент - Самый низ страницы
     */
    public By Footer = By.xpath("//footer[@role='contentinfo']");

    /**
     * Пациент - Документы - Сортировать
     */
    public By Sort = By.xpath("//input[@placeholder='Сортировать']");

    /**
     * Пациент - Документы - Сортировать - Дата создания документа
     */
    public By CreateDate = By.xpath("//div[@x-placement]//span[contains(.,'Дата создания документа')]");

    /**
     * Пациент - Документы - Сортировать - Документы есть на федеральном уровне
     */
    public By FederalTrue = By.xpath(
            "//span[contains(.,'Документ есть на федеральном уровне')]/preceding-sibling::span");

    /**
     * Пациент - Документы - Сортировка вниз
     */
    public By SortUp = By.xpath("//i[@class='fa fa-arrow-up']");

    /**
     * Пациент - Документы - Поиск
     */
    public By Search = By.xpath("//input[@placeholder='Введите наименование документа']");

    /**
     * Выбранная МО - Загрузка карты пациента
     */
    public By LoadingDocs = By.xpath("//div[@class='vimis-document-card']//div[@class='el-loading-spinner']");

    /**
     * Выбранная МО - Загрузка Шаблона
     */
    public By LoadingDocsShablon = By.xpath(
            "//section[@class='vimis-document-card__iframe-wrapper']/following-sibling::div/div[@class='el-loading-spinner']");

    /**
     * Выбранная МО - Загрузка Шаблона + Список документов
     */
    public By LoadingDocsShablonAndDocs = By.xpath(
            "//div[@class='vimis-document-card__wrapper el-row el-loading-parent--relative']/div[@class='el-loading-mask']/div[@class='el-loading-spinner']");

    /**
     * Выбранная МО - Загрузка документов Принятых на федеральном уровне
     */
    public By LoadingDocsFederal = By.xpath("(//div[@class='el-loading-spinner'])[7]");

    /**
     * Пациент - Документы - Все документы
     */
    public By Docs = By.xpath("//ul[@class='vimis-document-card__list']/li");

    /**
     * Пациент - Документы - Первый документ
     */
    public By FirstDocs = By.xpath("//ul[@class='vimis-document-card__list']/li[1]");

    /**
     * Пациент - Документы - Первый документ - Опубликовано в ФВИМИС
     */
    public By FirstDocsFVimis = By.xpath(
            "//ul[@class='vimis-document-card__list']/li[1]/div/div/div/p[2][contains(.,'Опубликовано в ФВИМИС')]");

    /**
     * Пациент - Документы - Первый документ - Опубликовано в ФВИМИС и ФРЭМД
     */
    public By FirstDocsFVimisFremd = By.xpath(
            "//ul[@class='vimis-document-card__list']/li[1]/div/div/div/p[2][contains(.,'Опубликовано в ФВИМИС и ФРЭМД')]");

    /**
     * Пациент - Документы - Последний документ документ
     */
    public By LastDocs = By.xpath("//ul[@class='vimis-document-card__list']/li[last()]");

    /**
     * Пациент - Документы - Первый документ - Выделенный
     */
    public By FirstDocsActive = By.xpath(
            "//ul[@class='vimis-document-card__list']/li[1]/div[@class='vimis-document-card-info vimis-document-card-info_active']");

    /**
     * Пациент - Документы - Шаблон последнего Документа
     */
    public By DocumentTemplate = By.xpath("//section[@class='vimis-document-card__iframe-wrapper']/div[last()]/iframe");

    /**
     * Пациент - Документы - Первый шаблон документа
     */
    public By FirstDocsPattern = By.xpath("//section[@class='vimis-document-card__iframe-wrapper']/div[1]");

    /**
     * Пациент - Документы - Первый шаблон документа - Скачать
     */
    public By Download = By.xpath("//section[@class='vimis-document-card__iframe-wrapper']/div[1]/button");

    /**
     * Пациент - Документы - Первый шаблон документа - Скачать - Загрузка скачивания
     */
    public By LoadingDownload = By.xpath(
            "//section[@class='vimis-document-card__iframe-wrapper']/div[1]/button/i[@class='el-icon-loading']");

    /**
     * Пациент - Документы - Пятый шаблон документа
     */
    public By FiveDocsPattern = By.xpath("//section[@class='vimis-document-card__iframe-wrapper']/div[5]");

    /**
     * Пациент - Действие
     */
    public By Action = By.xpath("//button/span[contains(.,'Действие')]");

    /**
     * Пациент - Действие - Добавить задание
     */
    public By ActionAddTask = By.xpath("//button[contains(.,'Добавить задание')]");

    /**
     * Пациент - Действие - Направление на диагностику
     */
    public By ActionAddDiagnostic = By.xpath("//button[contains(.,'Создать направление на диагностику')]");

    /**
     * Пациент - Действие - Добавить задание - Создать удаленную консультацию
     */
    public By ActionAddKonsul = By.xpath("//button[contains(.,'Создать удаленную консультацию')]");

    /**
     * Добавить задание - Задания пациента
     */
    public By TaskPatient = By.xpath("//h3[contains(.,'Задания по пациенту')]");

    /**
     * Добавить задание - Задания пациента - Загрузка
     */
    public By TaskPatientLoading = By.xpath(
            "//div[@class='tasks-patient el-loading-parent--relative']//div[@class='el-loading-spinner']");

    /**
     * Пациент - Задания пациента - Список отображаемых заданий
     */
    public By TaskList = By.xpath("//ul[@class='tasks-patient__list']/li");

    /**
     * Пациент - Задания пациента - Список заданий - Всего заданий
     */
    public By AllTask = By.xpath("//h3[contains(.,'Всего заданий')]/following-sibling::p");

    /**
     * Пациент - Задания пациента - Список заданий - Просрочено
     */
    public By ErrorTask = By.xpath("//h3[contains(.,'Просрочено')]/following-sibling::p");

    /**
     * Пациент - Задания пациента - Новое задание - Загрузка
     */
    public By NewTaskLoading = By.xpath("//button[@class='el-button tasks-patient__button el-button--primary el-button--medium is-disabled'][contains(.,'Новое задание')]");

    /**
     * Пациент - Задания пациента - Новое задание
     */
    public By NewTask = By.xpath("//button[contains(.,'Новое задание')]");

    /**
     * Пациент - Задания пациента - Новое задание - Недоступно
     */
    public By NewTaskDisabled = By.xpath("//button[@disabled='disabled'][contains(.,'Новое задание')]");

    /**
     * Новое задание - Готово
     */
    public By Done = By.xpath("//button/span[contains(.,'Готово')]");

    /**
     * Новое задание - Заголовок
     */
    @FindBy(xpath = "//input[@placeholder='Заголовок']")
    public WebElement HeaderInput;
    public By Header = By.xpath("//form[@class='el-form']/div[1]");

    /**
     * Новое задание - Текст задания
     */
    @FindBy(xpath = "//textarea[@placeholder='Текст задания']")
    public WebElement TextInput;
    public By Text = By.xpath("//form[@class='el-form']/div[2]");

    /**
     * Новое задание - Дата окончания
     */
    public By Data = By.xpath("//form[@class='el-form']/div[3]");

    /**
     * Новое задание - Дата окончания - 1 число
     */
    public By Day1 = By.xpath("//td[@class='next-month'][1]//span[contains(.,'1')]");

    /**
     * Новое задание - Мед организация
     */
    public By MO = By.xpath("//form[@class='el-form']/div[4]");

    /**
     * Новое задание - Мед организация - Нужная Мо
     */
    public By TrueMo(String value) {
        By TrueMo = By.xpath("//div[@x-placement]//li[contains(.,'"+value+"')]");
        return TrueMo;
    }

    /**
     * Новое задание - Медработник
     */
    public By Doctor = By.xpath("//input[@placeholder='Медработник']");

    /**
     * Новое задание - Поля в Ожидаемый результат и Подпись рекомендаций
     */
    public By Margin (String value, String number) {
        By True = By.xpath("(//label[contains(.,'"+value+"')]/following-sibling::div//input)["+number+"]");
        return True;
    }

    /**
     * Новое задание - Тип документа - Добавить
     */
    public By ADD = By.xpath("//button[contains(.,'Добавить')]");

    /**
     * Новое задание - Подпись рекомендации - Чек бокс текущий пользователь
     **/
    public By CheckBox = By.xpath("//span[contains(.,'Текущий пользователь')]/preceding::span[1]");


    /**
     * Добавленное задание - Заголовок
     */
    public By TaskHeader = By.xpath(
            "//ul[@class='tasks-patient__list']/li//p[contains(.,'1133---------------------------------------------------------------------------------------------------------')][1]");

    /**
     * Добавленное задание - Заголовок - Нужный заголовок
     */
    public By TaskHeaderTrue(String value) {
        By TaskHeaderTrue = By.xpath("//ul[@class='tasks-patient__list']/li//p[contains(.,'"+value+"')][1]");
        return TaskHeaderTrue;
    }

    /**
     * Добавленное задание - Текст задания
     */
    public By EndDate = By.xpath(
            "//ul[@class='tasks-patient__list']/li//p[contains(.,'1133---------------------------------------------------------------------------------------------------------')][1]/following-sibling::p//span");

    /**
     * Добавленное задание - Текст задания
     */
    public By TaskText = By.xpath(
            "//ul[@class='tasks-patient__list']/li//p[contains(.,'1133---------------------------------------------------------------------------------------------------------')][2]");

    /**
     * Добавленное задание - Появление полного названия заголовка
     */
    public By Tooltip = By.xpath(
            "//div[@role='tooltip'][contains(.,'1133---------------------------------------------------------------------------------------------------------')]");

    /**
     * Добавленное задание - Появление полного названия Текст задания
     */
    public By Tooltip1 = By.xpath(
            "//div[@role='tooltip'][contains(.,'1133---------------------------------------------------------------------------------------------------------')][2]");
}
