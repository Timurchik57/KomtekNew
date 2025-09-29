package UI.TmTest.AccessUI.Administration;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.Administration.AcceessRoles;
import UI.TmTest.PageObject.Administration.AuditRoles;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import static UI.TmTest.PageObject.Administration.AuditUsers.AssertAudit;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Редактирование_пользователя")
@Tag("Аудит_ролей")
@Tag("Основные")
public class Access_1670Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    AcceessRoles acceessRoles;
    AuditRoles auditRoles;

    @Step("Ищем роль = {0} и нажимаем редактировать")
    public void Access_1670Method(String NameRole) throws InterruptedException {
        while (!isElementNotVisible(By.xpath("(//table//tbody//tr//span[contains(.,'" + NameRole + "')])[1]"))) {
            acceessRoles.Next.click();
        }
        ClickElement(By.xpath(
                "(//table//tbody//td[contains(.,'" + NameRole + "')])[1]/following-sibling::td//button[@tooltiptext='Редактировать']"));
    }

    @Issue(value = "TEL-1670")
    @Link(name = "ТМС-1762", url = "https://team-1okm.testit.software/projects/5/tests/1762?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Owner(value = "Галиакберов Тимур")
    @Description("Создаём/изменяем/удаляем роль доступа - после переходим в Аудит ролей и проверяем записи")
    @Test
    @DisplayName("Создаём/изменяем/удаляем роль доступа")
    public void Access_1670() throws  InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        acceessRoles = new AcceessRoles(driver);
        auditRoles = new AuditRoles(driver);

        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переход в Роли Доступа");
        WaitElement(acceessRoles.RolesWait);
        actionElementAndClick(acceessRoles.Roles);

        System.out.println("Редактирование роли");
        ClickElement(acceessRoles.AddRoles);
        String NameRole = "ТестРоли";
        WaitElement(acceessRoles.NameRoleText);
        inputWord(driver.findElement(acceessRoles.NameRoleText), NameRole + " ");
        inputWord(driver.findElement(acceessRoles.DescriptionRoleText), "ОписаниеРоли ");
        ClickElement(acceessRoles.Check("Доступ к разделу \"Нозологические регистры\""));
        Thread.sleep(500);
        ClickElement(acceessRoles.Add);
        Thread.sleep(1000);

        System.out.println("Изменяем описание у роли");
        Access_1670Method(NameRole);
        WaitElement(acceessRoles.DescriptionRoleText);
        inputWord(driver.findElement(acceessRoles.DescriptionRoleText), "ОписаниеРоли2 ");
        ClickElement(acceessRoles.UpdateWait);
        Thread.sleep(1000);

        System.out.println("Изменяем доступы у роли");
        Access_1670Method(NameRole);
        WaitElement(acceessRoles.DescriptionRoleText);
        ClickElement(acceessRoles.Check("1.1. Доступ к отдельным нозологическим группам"));
        ClickElement(acceessRoles.UpdateWait);
        Thread.sleep(1000);

        System.out.println("Удаляем роль");
        while (!isElementNotVisible(By.xpath("(//table//tbody//tr//span[contains(.,'" + NameRole + "')])[1]"))) {
            acceessRoles.Next.click();
        }
        ClickElement(By.xpath(
                "(//table//tbody//td[contains(.,'" + NameRole + "')])[1]/following-sibling::td//button[@text='Удалить данный пункт?']"));
        ClickElement(acceessRoles.YesDelete);
        Thread.sleep(1000);

        System.out.println("Переходим в Аудит ролей");
        ClickElement(auditRoles.AuditRoles);
        ClickElement(auditRoles.DataStart);
        String number = driver.findElement(auditRoles.DataToday).getText();
        ClickElement(auditRoles.DataToday);
        driver.findElement(By.xpath("(//div[@x-placement]//td//span[contains(.,'" + number + "')])[1]")).click();

        System.out.println("Проверка действий");
        AssertAudit(auditRoles.AccessRoles, auditRoles.AccessRolesWait, "Изменение доступов роли");
        AssertAudit(auditRoles.AddRoles, auditRoles.AddRolesWait, "Добавление роли");
        AssertAudit(auditRoles.AddAccessRoles, auditRoles.AddAccessRolesWait, "Добавление доступов роли");
        AssertAudit(auditRoles.DeleteRoles, auditRoles.DeleteRolesWait, "Удаление роли");
        AssertAudit(auditRoles.UpdateRoles, auditRoles.UpdateRolesWait, "Изменение роли");
    }
}
