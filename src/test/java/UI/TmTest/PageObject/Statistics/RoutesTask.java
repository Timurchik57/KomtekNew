package UI.TmTest.PageObject.Statistics;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;


public class RoutesTask extends BaseTest {
    /**
     * Задания
     */
    public RoutesTask(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Задания - Заголовок
     */
    public By RoutesTaskHeader = By.xpath("//header[contains(.,'Задания')]");

    /**
     * Задания - Входящие
     */
    public By Incoming = By.xpath("//p[contains(.,'Входящие')]");

    /**
     * Задания - Любое название задания
     */
    public By RoutsName (String value) {
        By str = By.xpath(
                "//span[contains(.,'"+value+"')]");
        return str;
    }

    /**
     * Задания - Входящие - Всего заданий - Добавленное задание
     */
    public By Incoming1Add = By.xpath(
            "//p[contains(.,'Входящие')]/following-sibling::div/div[1]//span[@class='el-tooltip routes-tasks-item-title'][contains(.,'Проверяем заявку 1124 -------------------------------------------------------------')]");

    /**
     * Задания - Входящие - Персональные задания - Добавленное задание
     */
    public By Incoming2Add = By.xpath(
            "//p[contains(.,'Входящие')]/following-sibling::div/div[2]//span[@class='el-tooltip routes-tasks-item-title'][contains(.,'Проверяем заявку 1124 -------------------------------------------------------------')]");

    /**
     * Задания - Входящие - Персональные задания - Добавленное задание - Выполнено
     */
    public By Incoming3Add = By.xpath(
            "//p[contains(.,'Входящие')]/following-sibling::div/div[3]//span[@class='el-tooltip routes-tasks-item-title'][contains(.,'Проверяем заявку 1124 -------------------------------------------------------------')]");

    /**
     * Задания - Исходящие
     */
    public By Outgoing = By.xpath("//p[contains(.,'Исходящие')]");

    /**
     * Задания - Исходящие - Добавленное задание
     */
    public By Outgoing1Add = By.xpath(
            "//p[contains(.,'Исходящие')]/following-sibling::div/div[1]//span[@class='el-tooltip routes-tasks-item-title'][contains(.,'Проверяем заявку 1124 -------------------------------------------------------------')]");

    /**
     * Задания - Исходящие - Добавленное задание - скролл текста задания
     */
    public By Outgoing1AddScroll = By.xpath(
            "//p[contains(.,'Исходящие')]/following-sibling::div/div[1]//span[@class='routes-tasks-item-task-text'][contains(.,'Проверяем заявку 1124 -------------------------------------------------------------')]");

    /**
     * Задания - Появление полного названия заголовка
     */
    public By Tooltip = By.xpath(
            "//div[@role='tooltip'][contains(.,'Проверяем заявку 1124 -------------------------------------------------------------')]");

    /**
     * Задания - Не активный чек бокс Типа консультации или Мед услуги
     */
    public By RoutsMedNoActive (String nameRout, String name) {
        By Routs = By.xpath(
                "//div[@class='routes-tasks-item']/span[1][contains(.,'"+nameRout+"')]/following-sibling::div[@class='routes-tasks-item__expected']//div[contains(.,'"+name+"')]/following-sibling::label/span[@class='checkbox-label__el checkbox-label__el--disabled']");
        return Routs;
    }

    /**
     * Задания - Активный чек бокс Типа консультации или Мед услуги
     */
    public By RoutsMedActive (String nameRout, String name) {
        By Routs = By.xpath(
                "//div[@class='routes-tasks-item']/span[1][contains(.,'"+nameRout+"')]/following-sibling::div[@class='routes-tasks-item__expected']//div[contains(.,'"+name+"')]/following-sibling::label/span[@class='checkbox-label__el checkbox-label__el--disabled']");
        return Routs;
    }

    /**
     * Задания - Статус задания
     */
    public By StatusTask (String nameRout) {
        By Routs = By.xpath(
                "//div[@class='routes-tasks-item']/span[1][contains(.,'"+nameRout+"')]/following-sibling::div[@class='routes-tasks-item-bottom']/div[2]/span");
        return Routs;
    }
}
