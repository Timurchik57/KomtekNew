package UI.TmTest.AccessUI.Administration;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Редактирование_пользователя")
@Tag("Удалённая_консультация")
@Tag("Заполнение_карточки_пациента")
@Tag("Основные")
public class DeleteProfilesTest extends BaseAPI {
    MedOrganization medOrganization;
    AuthorizationObject authorizationObject;
    Users users;
    DirectionsForQuotas directionsForQuotas;

    @Issue(value = "TEL-532")
    @Link(name = "ТМС-1119", url = "https://team-1okm.testit.software/projects/5/tests/1119")
    @Owner(value = "Галиакберов Тимур")
    @Description("Генерация нового снилса. Добавление Роли пользователю и профиля с датой увольнения. Проверка, что после увольнения профиль недоступен. Проверка, что при создании консультации профиль недоступен")
    @DisplayName("Проверка отображения профиля пользователя при окончании работы по профилю")
    @Test
    public void DeleteProfile() throws InterruptedException {

        medOrganization = new MedOrganization(driver);
        authorizationObject = new AuthorizationObject(driver);
        users = new Users(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        System.out.println("Генерация снилса для добавления пользователя");
        driver.get(GENERATE_SNILS);
        WaitElement(authorizationObject.ButtonNewNumberWait);
        authorizationObject.ButtonNewNumber.click();
        String text = authorizationObject.Snils.getText();
        System.out.println("Новый СНИЛС: " + text);

        System.out.println("Авторизация и переход в пользователи");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(users.UsersWait);
        actionElementAndClick(users.Users);

        System.out.println("Выбор профиля и мед.организации");
        WaitElement(users.HeaderUsersWait);
        WaitElement(users.ProfileWait);
        SelectClickMethod(users.ProfileWait, users.SelectProfile);
        SelectClickMethod(users.MedOrgWait, users.SelectMedOrg);
        users.Search.click();

        System.out.println("Проверка что нет выбраннго профиля у других пользваотелей");
        if (!isElementNotVisible(users.NotDataWait)) {
            SelectClickMethod(users.ProfileWait, users.SelectProfile1);
            users.Search.click();
            users.Condition = "0";
        }
        System.out.println(users.Condition);

        System.out.println("Создание пользователя с новым снилс");
        WaitElement(users.UsersWait);
        actionElementAndClick(users.Users);
        WaitElement(users.HeaderUsersWait);
        users.AddUser.click();
        Thread.sleep(2500);
        users.InputSnils.sendKeys(text);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 20);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);

        /** Условие, нужно ли заполнять данные о пользователе */
        if (!isElementNotVisible(users.NotHeaderIEMK)) {
            if (KingNumber == 1) {
                AddUsersMethod(users.WorkYatskiv, users.DivisionChildPolyclinic, users.RoleUserTest213, "");
            }
            if (KingNumber == 2) {
                AddUsersMethod(users.WorkYatskiv, users.DivisionChildPolyclinic, users.RoleUserTest2, "");
            }
            if (KingNumber == 4) {
                AddUsersMethod(users.WorkYatskiv, users.DivisionChildPolyclinic, users.RoleUserTest999, "");
            }
            System.out.println("Новый пользователь добавлен в ИЭМК");
        } else {
            WaitElement(users.Alert);
            users.AlertClose.click();
            System.out.println("Пользователь с данным СНИЛС уже есть в базе ИЕМК");
            users.Update.click();
        }

        System.out.println("Редактирование роли, профиля и добавление даты увольнения");
        WaitElement(users.HeaderUsersWait);
        WaitElement(users.AddUserWait);
        ClickElement(users.AddUserWait);
        WaitElement(users.InputSnilsWait);
        users.InputSnils.sendKeys(text);

        System.out.println("Редактирование существующего пользователя");
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 15);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);
        WaitElement(users.OneWait);

        System.out.println("Добавление нового места работы");
        users.AddWork.click();
        WaitElement(users.HeaderAddWork);
        SelectClickMethod(users.SelectWork, users.WorkYatskiv);
        SelectClickMethod(users.SelectDivision, users.Division5);
        SelectClickMethod(users.SelectRoleUser, users.RoleUser5);
        users.AddWorkButton.click();

        System.out.println("Добавление нового профиля с датой увольнения");
        Thread.sleep(1000);
        WaitElement(users.ProfileButtonWait);
        users.Profile.click();
        WaitElement(users.HeaderProfileWait);
        users.ProfileAdd.click();
        //WaitElement(users.ProfileAddNotData);

        /** Условие какой прифиль выбрать */
        if (users.Condition == "0") {
            SelectClickMethod(users.SelectMedProfile, users.MedProfileOptike);
        }
        SelectClickMethod(users.SelectMedProfile, users.MedProfileKosmetologi);
        ClickElement(users.EditProfileDateWait);
        ClickElement(users.EditProfileDatePreviousWait);
        ClickElement(users.EditProfileApplyWait);
        Thread.sleep(1500);

        System.out.println("Проверка, что профиль после увольнения доступен");
        users.ProfileAdd.click();
        WaitNotElement(users.ProfileAddNotData);
        users.MedProfileButton.click();
        WaitElement(authorizationObject.BottomStart);
        if (users.Condition == "0") {
            WaitElement(users.DisableMedProfileOptike);
        }
        WaitElement(users.DisableMedProfileKosmetologiTrue);
        users.EditProfileCancel.click();
        Thread.sleep(300);
        users.EditProfileClose.click();
        Thread.sleep(500);
        users.Update.click();
        if (KingNumber == 4) {
            Thread.sleep(3000);
        }

        /** Переход в создание консультации  */
        WaitElement(directionsForQuotas.Unfinished);
        ClickElement(directionsForQuotas.ConsultationWait);

        System.out.println("Создать консультацию - добавить пациента");
        WaitElement(directionsForQuotas.Heading);
        WaitElement(directionsForQuotas.CreateWait);
        directionsForQuotas.Create.click();
        WaitElement(directionsForQuotas.TypeConsultationWait);
        directionsForQuotas.RemoteConsultation.click();
        directionsForQuotas.Next.click();
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys(text);
        Thread.sleep(1500);
        ClickElement(directionsForQuotas.BigSearchWait);

        System.out.println("Создание пациента для консультации");
        WaitElement(directionsForQuotas.CreatePatientWait);
        directionsForQuotas.CreatePatient.click();
        WaitElement(directionsForQuotas.ReferralsRemoteConsultation);
        directionsForQuotas.AddSnils.sendKeys(text);
        inputWord(directionsForQuotas.LastName, "Тестт");
        inputWord(directionsForQuotas.Name, "Тестт");
        inputWord(directionsForQuotas.MiddleName, "Тестт");
        SelectClickMethod(directionsForQuotas.DateWait, directionsForQuotas.SelectDate);
        directionsForQuotas.Man.click();
        SelectClickMethod(directionsForQuotas.TypeDocument, directionsForQuotas.SelectTypeDocument);
        inputWord(directionsForQuotas.Serial, "12344");
        inputWord(directionsForQuotas.Number, "1234566");
        actionElementAndClick(directionsForQuotas.Address);
        inputWord(directionsForQuotas.Address, "г Москва, ул Арбат, д 9АФ");
        ClickElement(directionsForQuotas.Select);
        actionElementAndClick(directionsForQuotas.AddressHome);
        ClickElement(directionsForQuotas.Select);
        actionElementAndClick(directionsForQuotas.NextPatient);

        System.out.println("Проверка отсутствия профиля");
        WaitElement(directionsForQuotas.MO);
        Thread.sleep(1000);
        SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMO);
        WaitElement(directionsForQuotas.ProfileWait);
        directionsForQuotas.Profile.click();
        if (users.Condition == "0") {
            WaitNotElement(directionsForQuotas.SelectProfileWaitOptiki);
        }
        WaitNotElement(directionsForQuotas.SelectProfileWaitKosmetologi);

        /** Удаление созданного профиля, для возможности переиспользовать данный тест */
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(users.UsersWait);
        actionElementAndClick(users.Users);
        WaitElement(users.HeaderUsersWait);
        WaitElement(users.AddUserWait);
        users.AddUser.click();
        users.InputSnils.sendKeys(text);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 20);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);

        /** Проверка, что добавленный профиль отображается */
        WaitElement(users.OneWait);

        /** Увольнение с 1 работы */
        System.out.println("Изменение данных о месте работы");
        users.Edit1.click();
        WaitElement(users.EditWait);
        SelectClickMethod(users.DateDismiss, users.DateDismissBackToday);
        users.UpdateWork.click();
        WaitNotElement(users.EditWait);
        WaitElement(users.SnilsInputWait);

        /** Увольнение со 2 работы */
        users.Edit2.click();
        WaitElement(users.EditWait);
        SelectClickMethod(users.DateDismiss, users.DateDismissBackToday);
        users.UpdateWork.click();
        WaitNotElement(users.EditWait);
        WaitElement(users.SnilsInputWait);
        actionElementAndClick(users.Update);
        Thread.sleep(1500);
    }
}
