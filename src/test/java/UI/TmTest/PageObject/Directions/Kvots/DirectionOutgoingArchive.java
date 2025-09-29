package UI.TmTest.PageObject.Directions.Kvots;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

public class DirectionOutgoingArchive extends BaseTest{

    /**
     * Направления на квоты / Исходящие / Архивные
     */
    public DirectionOutgoingArchive(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Направления на квоты / Исходящие / Архивные
     */
    public By DirectionOutgoingArchiveWait = By.xpath(
            "//a[@href='/direction/requestDiagnostic/archival?directionType=1&directionTarget=1&directionPageType=2']");
}
