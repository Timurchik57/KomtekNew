package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationScheduleRemote;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Расписание_консультаций")
@Tag("Удалённая_консультация")
@Tag("Запись_без_создания_Конс")
@Tag("РРП")
@Tag("Основные")
public class Access_2426Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    ConsultationScheduleRemote consultationSR;
    DirectionsForQuotas directionsForQuotas;
    public String startTime;
    public String endTime;
    public String patient_diagnosis;
    public String mo;
    public String user_id;
    public String firstName;
    public String lastName;
    public String middleName;
    public String birthday;
    public String phone;

    @Step("Метод смены формата даты для распределения квот")
    public String DateMethod(String str) {
        String year = str.substring(0, 4);
        String month = str.substring(0, str.length() - 3).substring(5);
        String day = str.substring(8);
        String NewDateAll = "" + day + "." + month + "." + year + "";
        return NewDateAll;
    }

    @Issue(value = "TEL-2426")
    @Issue(value = "TEL-2427")
    @Issue(value = "TEL-2645")
    @Issue(value = "TEL-2646")
    @Issue(value = "TEL-2647")
    @Issue(value = "TEL-2710")
    @Issue(value = "TEL-2726")
    @Issue(value = "TEL-2727")
    @Issue(value = "TEL-2728")
    @Issue(value = "TEL-2729")
    @Issue(value = "TEL-2730")
    @Issue(value = "TEL-2733")
    @Issue(value = "TEL-2734")
    @Issue(value = "TEL-2737")
    @Issue(value = "TEL-2925")
    @Issue(value = "TEL-2987")
    @Issue(value = "TEL-3001")
    @Link(name = "ТМС-1945", url = "https://team-1okm.testit.software/projects/5/tests/1945?isolatedSection=a254a2c9-4e42-4504-9888-1ab573156017")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Запись пациента на удалённую консультацию без создания направления")
    @Description("Переходим в Расписание консультаций - нажимаем на свободный слот - записываем пациента")
    public void Access_2426() throws InterruptedException, SQLException, IOException {

        authorizationObject = new AuthorizationObject(driver);
        consultationSR = new ConsultationScheduleRemote(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        String Patient = "Тестировщик Т. Т.";
        String Snils = "15979025720";

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Открываем Расписание консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);
        ClickElement(consultationSR.SlotsFreeWait(consultationSR.NumberColumn, true));

        System.out.println("Выбрали свободный слот");
        WaitElement(directionsForQuotas.BigSnils);
        driver.findElement(directionsForQuotas.BigSnils).sendKeys(Snils);
        ClickElement(directionsForQuotas.SearchWait("1"));
        ClickElement(directionsForQuotas.listPatientFirst2);

        /** Проверяем, что запись пока не активна (3001) */
        Assertions.assertTrue(driver.findElement(By.xpath("//button[contains(.,'Записать на консультацию')]")).getAttribute("class").equals("el-button el-button--primary el-button--medium is-disabled"), "Кнопка Записать на консультацию должна быть недоступна");

        System.out.println("Выбираем диагноз для заявки (2645)");
        ClickElement(directionsForQuotas.Diagnose);
        inputWord(driver.findElement(directionsForQuotas.DiagnoseSearch), "AA");
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.DiagnoseSearchSave);
        ClickElement(directionsForQuotas.WriteConsul);
        Thread.sleep(1500);

        sql.StartConnection("select c.id, u.fname, u.sname, u.mname, c2.starttime, c2.endtime, c3.id, c3.iddirection, c3.patient_diagnosis, c3.medicalidmu, ms.namemu, \n" +
                "c3.user_id, op.firstname, op.lastname, op.middlename, op.birthdate, op.phone  from telmed.consultationslots c\n" +
                "join telmed.users u on c.userid = u.id\n" +
                "join telmed.consultationslotstime c2 on c.id = c2.scheduleid\n" +
                "join telmed.consultationstakenslots c3 on c2.id = c3.idtimeinterval\n" +
                "join dpc.mis_sp_mu ms on c3.medicalidmu = ms.medicalidmu\n" +
                "join iemk.op_patient_reg op on c3.patient_id = op.patient_id \n" +
                "where u.sname = 'Зотин' order by c3.id desc limit 1;");
        while (sql.resultSet.next()) {
            startTime = sql.resultSet.getString("starttime").substring(11);
            endTime = sql.resultSet.getString("endtime").substring(11);
            patient_diagnosis = sql.resultSet.getString("patient_diagnosis");
            mo = sql.resultSet.getString("namemu");
            user_id = sql.resultSet.getString("user_id");
            firstName = sql.resultSet.getString("firstname");
            lastName = sql.resultSet.getString("lastname");
            middleName = sql.resultSet.getString("middlename");
            middleName = sql.resultSet.getString("middlename");

        }

        GetRRP("", Snils);
        birthday = Response.getString("patients[0].BirthDate");
        phone = Response.getString("patients[0].Phone");

        System.out.println("Берём время у занятого слота");
        String time = consultationSR.GetTimeConsul();
        System.out.println(time);

        System.out.println("Берём диагноз у занятого слота");
        String diagnosis = driver.findElement(consultationSR.SlotsBusyLastDiagnosisPatient(consultationSR.NumberColumn, Patient)).getText();
        System.out.println(diagnosis);

        System.out.println("Берём ФИО у занятого слота");
        String FIO = driver.findElement(consultationSR.SlotsBusyLastDiagnosisPatient(consultationSR.NumberColumn, Patient)).getText();
        System.out.println(FIO);

        System.out.println("Приводим к единому формату");
        String NewStartTimeSql = startTime.substring(0, startTime.length() - 3);
        String NewEndTimeSql = endTime.substring(0, endTime.length() - 3);

        String NewStartTimeWeb = time.substring(2);
        String NewStartTimeWeb2 = NewStartTimeWeb.substring(0, NewStartTimeWeb.length() - 9);
        String NewEndTimeWeb = time.substring(11);

        String NewdiagnosisWeb = diagnosis.substring(19);
        System.out.println(NewdiagnosisWeb);
        String NewdiagnosisWeb2 = NewdiagnosisWeb.substring(0, NewdiagnosisWeb.length() - 1);
        System.out.println(NewdiagnosisWeb2);

        String FIoNew = FIO.substring(0, 17);

        Assertions.assertEquals(NewStartTimeSql + " " + NewEndTimeSql, NewStartTimeWeb2 + " " + NewEndTimeWeb, "Время не совпадает");
        Assertions.assertEquals(patient_diagnosis, NewdiagnosisWeb2, "Диагноз не совпадает");

        /** Проверяем дополнитнльно добавление МО осуществившую запись на слот в БД (2729) */
        Assertions.assertEquals(mo, "БУ ХМАО-Югры \"Окружная клиническая больница\"", "МО не совпадает");

        /** Проверяем дополнитнльно пользователя осуществившего запись в БД (2728) */
        Assertions.assertEquals(user_id, PId, "Пользователь не совпадает");

        /** Проверяем дополнитнльно пациента (2730) */
        Assertions.assertEquals(FIoNew, lastName + " " + firstName.substring(0,1) + ". " + middleName.substring(0,1) + ".", "Пациент не совпадает");

        /** Проверяем дополнитнльно номер телефона пациента (2726) */
        WaitNotElement3(consultationSR.Loading, 30);
        actions.moveToElement(driver.findElement(consultationSR.SlotDisableCons(consultationSR.NumberColumn, time)));
        actions.perform();
        WaitElement(consultationSR.SlotNumberPhone);
        String PhoneNumber = driver.findElement(consultationSR.SlotNumberPhone).getText();
        Assertions.assertEquals(PhoneNumber, "Телефон: " + phone, "Номер пациента не совпадает");

        /** Проверяем дополнитнльно полное ФИО пациента в поп ап (2925, 2987) */
        String AllFIO = driver.findElement(consultationSR.SlotAllFio).getText();
        Assertions.assertEquals(AllFIO, "Пациент: " + lastName + " " + firstName + " " + middleName, "ФИО пациента не совпадает");

        /** Проверяем дополнитнльно полное дату рождения пациента в поп ап (2925) */
        String AllBirthday = driver.findElement(consultationSR.SlotBirthday).getText();
        Assertions.assertEquals(AllBirthday, "Дата рождения: " + DateMethod(birthday), "ФИО пациента не совпадает");

        System.out.println("Авторизуемся под МО2 в которой нет Расписания Консультации");
        AuthorizationMethod(authorizationObject.YATCKIV);

        System.out.println("Открываем Расписание консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);
        SelectClickMethod(consultationSR.Selected("Выберите мед. организацию"), authorizationObject.Select("БУ ХМАО-Югры \"Окружная клиническая больница\""));

        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);

        /** Проверяем что данные записаные через МО1 не доступны для МО2 */
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);
        actions.moveToElement(driver.findElement(consultationSR.SlotDisableCons(consultationSR.NumberColumn, time)));
        actions.perform();
        Thread.sleep(1500);
        WaitNotElement(consultationSR.SlotNumberPhone);
        WaitNotElement(consultationSR.SlotDiagnose);

        System.out.println("Выбираем свободный слот");
        ClickElement(consultationSR.SlotsFreeWait(consultationSR.NumberColumn, true));

        System.out.println("Выбрали свободный слот");
        WaitElement(directionsForQuotas.BigSnils);
        driver.findElement(directionsForQuotas.BigSnils).sendKeys(Snils);
        ClickElement(directionsForQuotas.SearchWait("1"));
        ClickElement(directionsForQuotas.listPatientFirst2);

        System.out.println("Выбираем диагноз для заявки (2645)");
        ClickElement(directionsForQuotas.Diagnose);
        inputWord(driver.findElement(directionsForQuotas.DiagnoseSearch), "AA");
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.DiagnoseSearchSave);
        ClickElement(directionsForQuotas.WriteConsul);
        Thread.sleep(2500);

        System.out.println("Берём ФИО у занятого слота");
        FIO = driver.findElement(consultationSR.SlotsBusyLastDiagnosisPatient(consultationSR.NumberColumn, Patient)).getText();
        System.out.println(FIO);

        System.out.println("Берём время у занятого слота");
        time = consultationSR.GetTimeConsul();
        System.out.println(time);

        /** Проверяем номер телефона пациента */
        WaitNotElement3(consultationSR.Loading, 30);
        actions.moveToElement(driver.findElement(consultationSR.SlotDisableCons(consultationSR.NumberColumn, time)));
        actions.perform();
        WaitElement(consultationSR.SlotNumberPhone);
        PhoneNumber = driver.findElement(consultationSR.SlotNumberPhone).getText();
        Assertions.assertEquals(PhoneNumber, "Телефон: " + phone, "Номер пациента не совпадает");

        /** Проверяем дополнитнльно пациента */
        Assertions.assertEquals(FIoNew, lastName + " " + firstName.substring(0,1) + ". " + middleName.substring(0,1) + ".", "Пациент не совпадает");
    }
}
