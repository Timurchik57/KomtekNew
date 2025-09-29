package Base;

import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;

import static Base.BaseAPI.*;

public class TestListenerChange implements TestWatcher {
    SQL sql;
    Users users;
    AuthorizationObject authorizationObject;

    @SneakyThrows
    @Override
    public void testFailed (ExtensionContext context, Throwable cause) {
        sql = new SQL();
        users = new Users(driver);
        authorizationObject = new AuthorizationObject(driver);
        TypeRegistr typeRegistr = new TypeRegistr(driver);
        try {
            if (ReadPropFile("className").equals("RegisterDispensariesTest")) {
                AuthorizationMethod(authorizationObject.OKB);
                System.out.println("Переход в Типы регистров для обратного добавления удалённого диагноза");
                ClickElement(typeRegistr.TypeRegistrWait);

                Thread.sleep(1500);
                while (!isElementNotVisible(By.xpath("//table//tbody//tr//span[contains(.,'Регистр Профилактики')]"))) {
                    if (isElementNotVisible(typeRegistr.NextDisabled) == true) {
                        break;
                    } else {
                        typeRegistr.Next.click();
                    }
                }
                ClickElement(typeRegistr.SearchEdit("Регистр Профилактики"));

                System.out.println("Добавляем диагноз ");
                ClickElement(typeRegistr.AddDiagnosisWait);
                WaitElement(typeRegistr.SelectAddDiagnosisWait);
                inputWord(typeRegistr.CodRegistr, typeRegistr.SelectedDiagnosis + "1");
                Thread.sleep(2500);
                typeRegistr.FirstCodRegistr.click();
                WaitNotElement(typeRegistr.SelectAddDiagnosisWait);
                ClickElement(typeRegistr.UpdateWait);
                Thread.sleep(1500);
            }

            if (ReadPropFile("className").equals("EmailDeleteUserTest")) {
                users.DeleteUsersMethodSql(ReadPropFile("Snils_Email"));
            }

            if (ReadPropFile("className").equals("Access_1111Test")) {
                users.DeleteUsersMethodSql(ReadPropFile("Snils_1111"));

                AddRole(PRole, "Доступ ко всем направлениям на квоты", true);
                AddRole(PRole, "Доступ ко всем консультациям", true);
            }

            if (ReadPropFile("className").equals("Access_1124Test")) {
                sql.UpdateConnection(
                        "Delete from vimis.routes_tasks where task_header = 'Проверяем заявку 1124 -------------------------------------------------------------';");
            }

            if (ReadPropFile("className").equals("Access_2027")) {
                sql.UpdateConnection("Update telmed.features set enabled = 0 where key = 'MoLevels';");
            }

            if (ReadPropFile("className").equals("Access_2552Test")) {
                System.out.println("Удаление созданного пользователя");
                users.DeleteUsersMethodSql(ReadPropFile("Snils_2552"));

                AddRole(PRole, "Доступ к разделу \"Расписание консультаций\"", true);
                AddRole("Тестовая роль",
                        "Доступ к созданию/редактированию графиков консультаций в \"Расписание консультаций\"", true);
                AddRole("Тестовая роль", "Доступ к просмотру графиков консультаций в \"Расписание консультаций\"",
                        true);
                AddRole("Тестовая роль", "Доступ к записи на консультацию в \"Расписание консультаций\"", true);
            }

            if (ReadPropFile("className").equals("Access_3761Test")) {
                sql.UpdateConnection("update telmed.users set snils = '159-790-257 20' where snils = '15979025720';");
            }

            if (ReadPropFile("className").equals("Access_3776Test")) {
                sql.UpdateConnection("Delete from telmed.patterns where name = 'Название';");
                sql.UpdateConnection("Delete from telmed.patterns where name = 'Названиеsql';");
                sql.UpdateConnection("Delete from telmed.patterns where name = 'НазваниеSQL';");
            }

            if (ReadPropFile("className").equals("Access_3965Test") |
                    ReadPropFile("className").equals("Access_4143Test") |
                    ReadPropFile("className").equals("Access_4137Test")) {
                sql.UpdateConnection("delete from telmed.mosmedinforesults where id = '" + ReadPropFile(
                        "idMosmedResult_3965") + "';");
                sql.UpdateConnection(
                        "delete from telmed.mosmedinfo where id = '" + ReadPropFile("idMosmed_3965") + "';");
                AddRole(PRole, "Доступ ко всем направлениям на квоты", false);
                AddRole(PRole, "Доступ ко всем консультациям", false);
            }

        } catch (IOException | SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Override
    public void testSuccessful (ExtensionContext context) {
        sql = new SQL();
        try {
            if (ReadPropFile("className").equals("Access_3965Test")) {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
