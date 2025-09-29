package UI.TmTest.PageObject.Administration;

import Base.SQL;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;


import java.sql.SQLException;

public class Limits extends BaseTest {

    public Limits(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Администрирование - Лимиты
     */
    public By Limit = By.xpath("//a[@href='/permission/setting-limit']");

    /**
     * Администрирование - Лимиты - Редактировать Лимит
     */
    public By LimitEdit(String value, String columns) {
        By LimitDelete = By.xpath(
                "(//tbody/tr/td[contains(.,'" + value + "')])[1]/following-sibling::td[" + columns + "]//button[@tooltiptext='Редактировать']");
        return LimitDelete;
    }

    /**
     * Администрирование - Лимиты - Редактировать Лимит по 2 параметрам
     */
    public By LimitEdit2Param(String value, String columns, String value2, String columns2) {
        By LimitDelete = By.xpath(
                "(//tbody/tr/td[contains(.,'"+value+"')]/following-sibling::td["+columns+"][contains(.,'"+value2+"')]/following-sibling::td["+columns2+"]//button[@tooltiptext='Редактировать'])[1]");
        return LimitDelete;
    }

    /**
     * Администрирование - Лимиты - Удалить Лимит
     */
    public By LimitDelete(String value, String columns) {
        By LimitDelete = By.xpath(
                "(//tbody/tr/td[contains(.,'" + value + "')])[1]/following-sibling::td[" + columns + "]//button[@text='Удалить данный пункт?']");
        return LimitDelete;
    }

    /**
     * Администрирование - Лимиты - Удалить Лимит - Подтверждение удаления
     */
    public By LimitDeleteYes = By.xpath(
            "//div[@x-placement]/p[contains(.,'Удалить данный пункт?')]/following-sibling::div/button/span[contains(.,'Удалить')]");

    /**
     * Администрирование - Лимиты - Удалить Лимит - Подтверждение удаления - Закрыть всплывшее окно
     */
    public By LimitDeleteAlert = By.xpath(
            "//div[@role='alert']//div[@class='el-notification__closeBtn el-icon-close']");

    /**
     * Лимиты - Добавить ограничение
     */
    public By AddLimit = By.xpath("//button/span[contains(.,'Добавить ограничение')]");

    /**
     * Добавить ограничение - Селекты
     */
    public By Select(String value, boolean word) {
        if (word == false) {
            By Select = By.xpath("//label[contains(.,'" + value + "')]/following-sibling::div");
            return Select;
        } else {
            By Select = By.xpath("//label[contains(.,'" + value + "')]/following-sibling::div//input");
            return Select;
        }
    }

    /**
     * Добавить ограничение - Выбранный тип лимита
     */
    public By TypeLimits = By.xpath("//label[contains(.,'Тип лимита')]/following-sibling::div//input");

    /**
     * Добавить ограничение - Селекты - Ошибки
     */
    public By SelectError(String value) {
        By Select = By.xpath(
                "//label[contains(.,'" + value + "')]/following-sibling::div/div[contains(.,'Поле обязательно для заполнения')]");
        return Select;
    }

    /**
     * Добавить ограничение - Без ограничения по лимитам
     */
    public By NotLimits = By.xpath("//span[contains(.,'Без ограничения по лимитам')]/preceding-sibling::span");

    /**
     * Добавить ограничение - Период
     */
    public By Period(String value) {
        By Select = By.xpath(
                "//label[contains(.,'Период потребления информации')]/following-sibling::div/div/label/span[contains(.,'" + value + "')]/preceding-sibling::span");
        return Select;
    }

    /**
     * Добавить ограничение - Добавить
     */
    public By Add = By.xpath("//div[@class='el-dialog__footer']//button[contains(.,'Добавить')]");

    /**
     * Добавить ограничение - Обновить
     */
    public By Update = By.xpath("//div[@class='el-dialog__footer']//button[contains(.,'Обновить')]");

    @Step("Метод с помощью которого создаём тип лимита - {0}")
    public void Method_2166(String TypeLimit, String InfoSystem, String Doc, boolean Nolimits, String snils, String MO, String type, String period, String count, String research) throws InterruptedException, SQLException {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        Limits limits = new Limits(driver);
        sql = new SQL();

        if (isElementVisible(limits.LimitDeleteAlert)) {
            ClickElement(limits.LimitDeleteAlert);
        }

        ClickElement(limits.AddLimit);
        SelectClickMethod(limits.Select("Тип лимита", false), authorizationObject.Select(TypeLimit));

        SelectClickMethod(limits.Select("Информационная система", false),
                authorizationObject.Select(InfoSystem));

        if (Nolimits == true) {
            ClickElement(limits.NotLimits);
        }
        if (TypeLimit == "Запроса СЭМД") {
            inputWord(driver.findElement(limits.Select("Вид документа", true)),
                    Doc);
            Thread.sleep(1500);
            ClickElement(authorizationObject.SelectFirst);
            inputWord(driver.findElement(limits.Select("Количество", true)), count);
            ClickElement(limits.Period(period));
        }

        if (TypeLimit == "Доступности СЭМД") {
            inputWord(driver.findElement(limits.Select("Вид документа", true)),
                    Doc);
            Thread.sleep(1500);
            ClickElement(authorizationObject.SelectFirst);
            inputWord(driver.findElement(limits.Select("Мед.работник (СНИЛС)", true)), snils);
            SelectClickMethod(limits.Select("МО", false), authorizationObject.Select(MO));
        }

        if (TypeLimit == "Запроса медицинских изображений") {
            inputWord(driver.findElement(limits.Select("Количество", true)), count);
            ClickElement(limits.Period(period));
            inputWord(driver.findElement(limits.Select("Вид исследования", true)), research);
            Thread.sleep(1500);
            ClickElement(authorizationObject.SelectFirst);
        }

        if (TypeLimit == "Доступности медицинских изображений") {
            inputWord(driver.findElement(limits.Select("Мед.работник (СНИЛС)", true)), snils);
            SelectClickMethod(limits.Select("МО", false), authorizationObject.Select(MO));
            inputWord(driver.findElement(limits.Select("Вид исследования", true)), research);
            Thread.sleep(1500);
            ClickElement(authorizationObject.SelectFirst);
        }
        ClickElement(limits.Add);
        Thread.sleep(2500);

        sql.StartConnection(
                "SELECT l.id, l.\"type\", t.fullname, l.doctype, e.doctype, l.count, l.nolimit, l.\"period\", l.doctorsnils, l.medicalidmu, l.research, l.createdate FROM telmed.limit_settings l\n" +
                        "left join telmed.centralized_unloading_systems t on l.centralizedunloadingsystemid = t.id\n" +
                        "left join dpc.emd_types e on l.doctype = e.id where l.\"type\" = '" + type + "' order by l.id desc limit 1;");
        if (type == "1") {
            while (sql.resultSet.next()) {
                Assertions.assertEquals(sql.resultSet.getString("type"), type);
                Assertions.assertEquals(sql.resultSet.getString("fullname"), "МИС \"Пациент\"");
                Assertions.assertEquals(sql.resultSet.getString("doctype"), "3");
                Assertions.assertEquals(sql.resultSet.getString("count"), "573");
                Assertions.assertEquals(sql.resultSet.getString("nolimit"), "1");
                Assertions.assertEquals(sql.resultSet.getString("period"), "1");
                Assertions.assertEquals(sql.resultSet.getString("doctorsnils"), null);
                Assertions.assertEquals(sql.resultSet.getString("medicalidmu"), null);
                Assertions.assertEquals(sql.resultSet.getString("research"), null);

                String date = sql.resultSet.getString("createdate");
                System.out.println(Date);
                System.out.println(date);
                Assertions.assertEquals(date.substring(0, 10), Date);
            }
        }
        if (type == "2") {
            while (sql.resultSet.next()) {
                Assertions.assertEquals(sql.resultSet.getString("type"), type);
                Assertions.assertEquals(sql.resultSet.getString("fullname"), "МИС \"Пациент\"");
                Assertions.assertEquals(sql.resultSet.getString("doctype"), "3");
                Assertions.assertEquals(sql.resultSet.getString("count"), null);
                Assertions.assertEquals(sql.resultSet.getString("nolimit"), "1");
                Assertions.assertEquals(sql.resultSet.getString("period"), null);
                Assertions.assertEquals(sql.resultSet.getString("doctorsnils"), "32178945688");
                if (KingNumber == 12) {
                    Assertions.assertEquals(sql.resultSet.getString("medicalidmu"), "820001");
                } else {
                    Assertions.assertEquals(sql.resultSet.getString("medicalidmu"), "21126");
                }
                Assertions.assertEquals(sql.resultSet.getString("research"), null);

                String date = sql.resultSet.getString("createdate");
                System.out.println(date);
                Assertions.assertEquals(date.substring(0, 10), Date);
            }
        }
        if (type == "3") {
            while (sql.resultSet.next()) {
                Assertions.assertEquals(sql.resultSet.getString("type"), type);
                Assertions.assertEquals(sql.resultSet.getString("fullname"), "МИС \"Пациент\"");
                Assertions.assertEquals(sql.resultSet.getString("doctype"), null);
                Assertions.assertEquals(sql.resultSet.getString("count"), "573");
                Assertions.assertEquals(sql.resultSet.getString("nolimit"), "1");
                Assertions.assertEquals(sql.resultSet.getString("period"), "1");
                Assertions.assertEquals(sql.resultSet.getString("doctorsnils"), null);
                Assertions.assertEquals(sql.resultSet.getString("medicalidmu"), null);
                if (KingNumber != 4 & KingNumber != 12) {
                    Assertions.assertEquals(sql.resultSet.getString("research"), "5217");
                } else {
                    Assertions.assertEquals(sql.resultSet.getString("research"), "3514");
                }

                String date = sql.resultSet.getString("createdate");
                System.out.println(date);
                Assertions.assertEquals(date.substring(0, 10), Date);
            }
        }
        if (type == "4") {
            while (sql.resultSet.next()) {
                Assertions.assertEquals(sql.resultSet.getString("type"), type);
                Assertions.assertEquals(sql.resultSet.getString("fullname"), "МИС \"Пациент\"");
                Assertions.assertEquals(sql.resultSet.getString("doctype"), null);
                Assertions.assertEquals(sql.resultSet.getString("count"), null);
                Assertions.assertEquals(sql.resultSet.getString("nolimit"), "1");
                Assertions.assertEquals(sql.resultSet.getString("period"), "1");
                Assertions.assertEquals(sql.resultSet.getString("doctorsnils"), "32178945688");
                if (KingNumber == 12) {
                    Assertions.assertEquals(sql.resultSet.getString("medicalidmu"), "820001");
                } else {
                    Assertions.assertEquals(sql.resultSet.getString("medicalidmu"), "21126");
                }
                if (KingNumber != 4 & KingNumber != 12) {
                    Assertions.assertEquals(sql.resultSet.getString("research"), "5217");
                } else {
                    Assertions.assertEquals(sql.resultSet.getString("research"), "3514");
                }

                String date = sql.resultSet.getString("createdate");
                System.out.println(date);
                Assertions.assertEquals(date.substring(0, 10), Date);
            }
        }
    }

    @Step("Метод с помощью которого редактируем тип лимита - {0}")
    public void Method_2166_Edit( String InfoSystem, String Doc, boolean Nolimits, String snils, String MO, String period, String period2, String count, String research) throws InterruptedException, SQLException {
        
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        Limits limits = new Limits(driver);
        sql = new SQL();

        String TypeLimit = getShadow(driver.findElement(limits.TypeLimits));
        System.out.println(TypeLimit);
        String type = null;

        SelectClickMethod(limits.Select("Информационная система", false),
                authorizationObject.Select(InfoSystem));

        if (Nolimits == true) {
            ClickElement(limits.NotLimits);
        }
        if (TypeLimit.contains("Запроса СЭМД")) {
            type = "1";
            SelectClickMethod(limits.Select("Вид документа", false),
                    authorizationObject.Select(Doc));
            inputWord(driver.findElement(limits.Select("Количество", true)), count);
            ClickElement(limits.Period(period));
            Thread.sleep(1500);
            ClickElement(limits.Period(period2));
        }

        if (TypeLimit.contains("Доступности СЭМД")) {
            type = "2";
            inputWord(driver.findElement(limits.Select("Вид документа", true)),
                    Doc);
            Thread.sleep(1500);
            ClickElement(authorizationObject.Select(Doc));
            inputWord(driver.findElement(limits.Select("Мед.работник (СНИЛС)", true)), snils);
            SelectClickMethod(limits.Select("МО", false), authorizationObject.Select(MO));
        }

        if (TypeLimit.contains("Запроса медицинских изображений")) {
            type = "3";
            inputWord(driver.findElement(limits.Select("Количество", true)), count);
            ClickElement(limits.Period(period));
            Thread.sleep(2500);
            ClickElement(limits.Period(period2));
        }

        if (TypeLimit.contains("Доступности медицинских изображений")) {
            type = "4";
            inputWord(driver.findElement(limits.Select("Мед.работник (СНИЛС)", true)), snils);
            SelectClickMethod(limits.Select("МО", false), authorizationObject.Select(MO));
        }
        ClickElement(limits.Update);
        Thread.sleep(1500);
        WaitElement(limits.LimitEdit("Запроса СЭМД", "8"));

        sql.StartConnection(
                "SELECT l.id, l.\"type\", t.fullname, l.doctype, e.doctype, l.count, l.nolimit, l.\"period\", l.doctorsnils, l.medicalidmu, l.research, l.createdate FROM telmed.limit_settings l\n" +
                        "left join telmed.centralized_unloading_systems t on l.centralizedunloadingsystemid = t.id\n" +
                        "left join dpc.emd_types e on l.doctype = e.id where l.\"type\" = '" + type + "' order by l.id desc limit 1;");
        if (type == "1") {
            while (sql.resultSet.next()) {
                Assertions.assertEquals(sql.resultSet.getString("type"), type);
                Assertions.assertEquals(sql.resultSet.getString("fullname"), "МИС \"Югра\"");
                Assertions.assertEquals(sql.resultSet.getString("doctype"), "81");
                Assertions.assertEquals(sql.resultSet.getString("count"), "987");
                Assertions.assertEquals(sql.resultSet.getString("nolimit"), "0");
                Assertions.assertEquals(sql.resultSet.getString("period"), "2");
                Assertions.assertEquals(sql.resultSet.getString("doctorsnils"), null);
                Assertions.assertEquals(sql.resultSet.getString("medicalidmu"), null);
                Assertions.assertEquals(sql.resultSet.getString("research"), null);

                String date = sql.resultSet.getString("createdate");
                System.out.println(Date);
                Assertions.assertEquals(date.substring(0, 10), Date);
            }
        }
        if (type == "2") {
            while (sql.resultSet.next()) {
                Assertions.assertEquals(sql.resultSet.getString("type"), type);
                Assertions.assertEquals(sql.resultSet.getString("fullname"), "МИС \"Югра\"");
                Assertions.assertEquals(sql.resultSet.getString("doctype"), "81");
                Assertions.assertEquals(sql.resultSet.getString("count"), null);
                Assertions.assertEquals(sql.resultSet.getString("nolimit"), "0");
                Assertions.assertEquals(sql.resultSet.getString("period"), null);
                Assertions.assertEquals(sql.resultSet.getString("doctorsnils"), "99977766644");
                Assertions.assertEquals(sql.resultSet.getString("medicalidmu"), "15107");
                Assertions.assertEquals(sql.resultSet.getString("research"), null);

                String date = sql.resultSet.getString("createdate");
                Assertions.assertEquals(date.substring(0, 10), Date);
            }
        }
        if (type == "3") {
            while (sql.resultSet.next()) {
                Assertions.assertEquals(sql.resultSet.getString("type"), type);
                Assertions.assertEquals(sql.resultSet.getString("fullname"), "МИС \"Югра\"");
                Assertions.assertEquals(sql.resultSet.getString("doctype"), null);
                Assertions.assertEquals(sql.resultSet.getString("count"), "987");
                Assertions.assertEquals(sql.resultSet.getString("nolimit"), "0");
                Assertions.assertEquals(sql.resultSet.getString("period"), "2");
                Assertions.assertEquals(sql.resultSet.getString("doctorsnils"), null);
                Assertions.assertEquals(sql.resultSet.getString("medicalidmu"), null);
                if (KingNumber != 4 & KingNumber != 12) {
                    Assertions.assertEquals(sql.resultSet.getString("research"), "5217");
                } else {
                    Assertions.assertEquals(sql.resultSet.getString("research"), "3514");
                }

                String date = sql.resultSet.getString("createdate");
                Assertions.assertEquals(date.substring(0, 10), Date);
            }
        }
        if (type == "4") {
            while (sql.resultSet.next()) {
                Assertions.assertEquals(sql.resultSet.getString("type"), type);
                Assertions.assertEquals(sql.resultSet.getString("fullname"), "МИС \"Югра\"");
                Assertions.assertEquals(sql.resultSet.getString("doctype"), null);
                Assertions.assertEquals(sql.resultSet.getString("count"), null);
                Assertions.assertEquals(sql.resultSet.getString("nolimit"), "0");
                Assertions.assertEquals(sql.resultSet.getString("period"), null);
                Assertions.assertEquals(sql.resultSet.getString("doctorsnils"), "99977766644");
                Assertions.assertEquals(sql.resultSet.getString("medicalidmu"), "15107");
                if (KingNumber != 4 & KingNumber != 12) {
                    Assertions.assertEquals(sql.resultSet.getString("research"), "5217");
                } else {
                    Assertions.assertEquals(sql.resultSet.getString("research"), "3514");
                }

                String date = sql.resultSet.getString("createdate");
                Assertions.assertEquals(date.substring(0, 10), Date);
            }
        }
    }
}
