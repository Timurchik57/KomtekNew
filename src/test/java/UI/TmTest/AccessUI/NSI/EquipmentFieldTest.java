package UI.TmTest.AccessUI.NSI;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.NSI.Equipments;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("НСИ")
@Tag("Оборудование")
public class EquipmentFieldTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    EquipmentSchedule equipmentSchedule;
    Equipments equipments;

    @Test
    @Issue(value = "TEL-722")
    @Link(name = "ТМС-1197", url = "https://team-1okm.testit.software/projects/5/tests/1197?isolatedSection=ccb1fcf9-9e3b-44d1-9bad-7959d251a43d")
    @DisplayName("Проверка отображения доступных полей при редактировании оборудования")
    @Description("Переход в оборудование, редактирование - Проверка отображения доступных полей при редактировании оборудования")
    public void EquipmentSchedule() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        equipments = new Equipments(driver);

        System.out.println("Определяем параметр по МТБЗ");
        MTBZMethod();

        if (!MTBZ) {
            System.out.println("Авторизация  и переход в создание направления");
            AuthorizationMethod(authorizationObject.YATCKIV);
            WaitElement(equipments.EquipmentWait);
            actionElementAndClick(equipments.Equipment);
            WaitElement(equipments.HeaderEquipmentWait);

            System.out.println("Редактировать 1 элемент из списка");
            WaitElement(equipments.Edit1Wait);
            equipments.Edit1.click();
            WaitElement(equipments.HeaderEditWait);
            List<String> getDisabled = new ArrayList<String>();
            List<WebElement> Disabled = driver.findElements(equipments.DisabledWait);
            for (int i = 0; i < Disabled.size(); i++) {
                getDisabled.add(Disabled.get(i).getText());
            }

            System.out.println("Всего недостпных элементов " + getDisabled.size());
            if (getDisabled.size() == 2) {
                ClickElement(equipments.DivisionWait);
                ClickElement(equipments.SelectDivisionAdm);
                List<String> getDisabled2 = new ArrayList<String>();
                List<WebElement> Disabled2 = driver.findElements(equipments.DisabledWait);
                for (int i = 0; i < Disabled2.size(); i++) {
                    getDisabled2.add(Disabled2.get(i).getText());
                }
                System.out.println("Всего недостпных элементов " + getDisabled.size());
                Assertions.assertEquals(getDisabled2.size(), 1);
                System.out.println("Значение недостпных элементов " + getDisabled.size() + " совпадает с " + "1");
            } else {
                Assertions.assertEquals(getDisabled.size(), 1);
                System.out.println("Значение недостпных элементов " + getDisabled.size() + " совпадает с " + "1");
            }
        }
    }
}
