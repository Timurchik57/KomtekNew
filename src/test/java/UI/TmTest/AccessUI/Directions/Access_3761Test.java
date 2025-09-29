package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import Base.TestListenerChange;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import api.Access_Notification.Access_3756_Type20_Test;
import api.Before.Authorization;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListenerChange.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Удалённая_консультация")
@Tag("Изменение_консультации")
@Tag("Api_Удалённая_консультация")
@Tag("Оповещение")
@Tag("/api/direction")
@Tag("Основные")
public class Access_3761Test extends BaseAPI {

    Authorization authorization;
    AuthorizationObject authorizationObject;
    ConsultationOutgoingUnfinished consultationOU;
    String Directionguid;
    Access_3756_Type20_Test access3756Type20Test = new Access_3756_Type20_Test();

    @Issue(value = "TEL-3761")
    @Issue(value = "TEL-3762")
    @Issue(value = "TEL-3763")
    @Issue(value = "TEL-3850")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Создаём консультацию через api, после изменяем PUT и проверяем GET")
    @Description("Создаём консультацию через api, после изменяем PUT и проверяем GET")
    public void Access_3764 () throws SQLException, InterruptedException, IOException {

        authorizationObject = new AuthorizationObject(driver);
        authorization = new Authorization();
        consultationOU = new ConsultationOutgoingUnfinished(driver);

        PatientGuid = "0F94EB3B-C391-463E-BF5B-D90D63936951";
        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        System.out.println("Отменяем созданную консультацию");
        consultationOU.CancelConsultation(PatientGuid);

        xml.changes.put("$.PatientGuid", PatientGuid);
        xml.changes.put("$.TargetMOId", POidMoRequest);
        String body = JsonMethod("SMS/Body/api_direction.json", xml.changes, false, null);

        Api(HostAddress + "/api/direction", "post", null, null, body, 200, false);
        Directionguid = Response.getString("Result");
        CheckSql("4", "15979025720", "string", "string2", "string3", "15979025720", "1", Date + "T09:37:12.083", "5",
                "1", "2", "A16.1", "stringR", "1", "0", "2");

        access3756Type20Test.CheckType20(true);

        body = "{\n" +
                "  \"PatientGuid\": \"" + PatientGuid + "\",\n" +
                "   \"TargetMOId\": \"" + POidMoRequest + "\",\n" +
                "  \"DoctorInformation\": {\n" +
                "                \"Position\": 3,\n" +
                "                \"DoctorSnils\": \"15979025720\"\n" +
                "            },\n" +
                "  \"InformationHeadDepartment\": {\n" +
                "        \"LastName\": \"stringll\",\n" +
                "    \"FirstName\": \"string2ll\",\n" +
                "    \"MiddleName\": \"string3ll\",\n" +
                "    \"position\": 6,\n" +
                "    \"HeadDoctorSnils\": \"15979025720\"\n" +
                "  },\n" +
                "  \"DateDirection\": \"" + Date + "T09:37:12.083Z\",\n" +
                "  \"ConsultationTypeId\": 3,\n" +
                "  \"Profile\": 3,\n" +
                "  \"DiagnosisCode\": \"A16.2\",\n" +
                "  \"Reason\": \"stringRll\",\n" +
                "  \"DirectedFrom\": 3,\n" +
                "  \"consultationReason\": 2,\n" +
                "  \"requireVideoConsult\": true,\n" +
                "  \"patientPlacement\": 1\n" +
                "}";

        Api(HostAddress + "/api/direction/" + Directionguid, "put", null, null, body, 200, false);
        Directionguid = Response.getString("Result");
        CheckSql("3", "15979025720", "stringll", "string2ll", "string3ll", "15979025720", "6",
                Date + "T09:37:12.083", "2", "3", "3", "A16.2", "stringRll", "3", "1", "1");

        access3756Type20Test.CheckType20(false);
    }

    @Issue(value = "TEL-3775")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Создаём консультацию через api, проверяем DoctorSnils")
    @Description("В telmed.users.snils меняем снилс без тире, отправляем запрос создания консультации, ловим ошибку")
    public void Access_3775 () throws SQLException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        authorization = new Authorization();
        consultationOU = new ConsultationOutgoingUnfinished(driver);

        PatientGuid = "0F94EB3B-C391-463E-BF5B-D90D63936951";
        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        System.out.println("Отменяем созданную консультацию");
        consultationOU.CancelConsultation(PatientGuid);

        System.out.println("Меняем снилс");
        sql.UpdateConnection("update telmed.users set snils = '15979025720' where snils = '159-790-257 20';");

        xml.changes.put("$.PatientGuid", PatientGuid);
        xml.changes.put("$.TargetMOId", POidMoRequest);
        String body = JsonMethod("SMS/Body/api_direction.json", xml.changes, false, null);
        Api(HostAddress + "/api/direction", "post", null, null, body, 200, false);
        Assertions.assertEquals(Response.getString("ErrorMessage"),
                "Указанный врач, направивший консультацию, не принадлежит вашей МО",
                "Должна быть ошибка - Указанный врач, направивший консультацию, не принадлежит вашей МО");

        sql.UpdateConnection("update telmed.users set snils = '159-790-257 20' where snils = '15979025720';");

    }

    @Step("Метод проверки созданной кносультации в БД методом /api/direction")
    public void CheckSql (String position_sql_, String doctorsnils_sql_, String lastname_, String firstname_, String middlename_, String snils_, String position_, String datedirection_, String consultationreason_, String consultationtypeid_, String medprofile_, String diagnos_, String reason_, String directedfrom_, String requirevideoconsult_, String patientplacement_) throws SQLException, IOException {
        String position_sql = null;
        String doctorsnils_sql = null;
        String lastname = null;
        String firstname = null;
        String middlename = null;
        String snils = null;
        String position = null;
        String datedirection = null;
        String consultationreason = null;
        String consultationtypeid = null;
        String medprofile = null;
        String diagnos = null;
        String reason = null;
        String directedfrom = null;
        String requirevideoconsult = null;
        String patientplacement = null;
        String status = null;

        sql.StartConnection(
                "select d.status, directionguid, doc.positionid position_sql, doc.doctorsnils doctorsnils_sql, i.*, d.datedirection, d.consultationpurpose ConsultationReason, d.consultationtype ConsultationTypeId, d.medprofile, d.diagnos, d.reason, d.directedfrom, d.requirevideoconsult, d.inhospital PatientPlacement from telmed.directions d \n" +
                        "join telmed.informationheaddepartment i on d.createheaddepartment = i.id\n" +
                        "join telmed.doctorinfo doc on d.createdoctorinfo = doc.id \n" +
                        "where directionguid = '" + Directionguid + "';");
        while (sql.resultSet.next()) {
            position_sql = sql.resultSet.getString("position_sql");
            doctorsnils_sql = sql.resultSet.getString("doctorsnils_sql");
            lastname = sql.resultSet.getString("lastname");
            firstname = sql.resultSet.getString("firstname");
            middlename = sql.resultSet.getString("middlename");
            snils = sql.resultSet.getString("snils");
            position = sql.resultSet.getString("position");
            datedirection = sql.resultSet.getString("datedirection");
            consultationreason = sql.resultSet.getString("consultationreason");
            consultationtypeid = sql.resultSet.getString("consultationtypeid");
            medprofile = sql.resultSet.getString("medprofile");
            diagnos = sql.resultSet.getString("diagnos");
            reason = sql.resultSet.getString("reason");
            directedfrom = sql.resultSet.getString("directedfrom");
            requirevideoconsult = sql.resultSet.getString("requirevideoconsult");
            patientplacement = sql.resultSet.getString("patientplacement");
            status = sql.resultSet.getString("status");
        }

        Assertions.assertEquals(position_sql, position_sql_, "Идентификатор должности не совпадает");
        Assertions.assertEquals(doctorsnils_sql, doctorsnils_sql_, "Снилс Врача не совпадает");
        Assertions.assertEquals(lastname, lastname_, "Фамилия не совпадает");
        Assertions.assertEquals(firstname, firstname_, "Имя не совпадает");
        Assertions.assertEquals(middlename, middlename_, "Отчество не совпадает");
        Assertions.assertEquals(snils, snils_, "Снилс не совпадает");
        Assertions.assertEquals(position, position_, "Должностьне совпадает");
        Assertions.assertEquals(datedirection.substring(0, 10), datedirection_.substring(0, 10), "Дата не совпадает");
        Assertions.assertEquals(consultationreason, consultationreason_, "Цель консультации не совпадает");
        Assertions.assertEquals(consultationtypeid, consultationtypeid_, "Тип консультации не совпадает");
        Assertions.assertEquals(medprofile, medprofile_, "Профиль не совпадает");
        Assertions.assertEquals(diagnos, diagnos_, "Диагноз не совпадает");
        Assertions.assertEquals(reason, reason_, "Причина не совпадает");
        Assertions.assertEquals(directedfrom, directedfrom_, "Откуда отправлен не совпадает");
        Assertions.assertEquals(requirevideoconsult, requirevideoconsult_, "Наличие видео консультации не совпадает");
        Assertions.assertEquals(patientplacement, patientplacement_, "В стационаре не совпадает");
        Assertions.assertEquals(status, "6", "Статус не совпадает");

        System.out.println("\nОтправляем Get запрос");
        String params[] = {"directionGuid", Directionguid,
                "patientGuid", PatientGuid};
        Api(HostAddress + "/api/direction", "get", params, null, "", 200, false);
        Assertions.assertEquals(Response.getString("Result[0].DirectionGuid"), Directionguid,
                "DirectionGuid не совпадает");
        Assertions.assertEquals(Response.getString("Result[0].PatientGuid"), PatientGuid, "PatientGuid не совпадает");
        Assertions.assertEquals(Response.getString("Result[0].TargetMOId"), POidMoRequest, "TargetMOId не совпадает");
        Assertions.assertEquals(Response.getString("Result[0].DoctorInformation.Position"), position_sql_,
                "Position не совпадает");
        Assertions.assertEquals(Response.getString("Result[0].DoctorInformation.DoctorSnils"), doctorsnils_sql_,
                "DoctorSnils не совпадает");
        Assertions.assertEquals(Response.getString("Result[0].InformationHeadDepartment.LastName"), lastname_,
                "LastName не совпадает");
        Assertions.assertEquals(Response.getString("Result[0].InformationHeadDepartment.FirstName"), firstname_,
                "FirstName не совпадает");
        Assertions.assertEquals(Response.getString("Result[0].InformationHeadDepartment.MiddleName"), middlename_,
                "MiddleName не совпадает");
        Assertions.assertEquals(Response.getString("Result[0].InformationHeadDepartment.Position"), position_,
                "Position совпадает");
        Assertions.assertEquals(Response.getString("Result[0].InformationHeadDepartment.HeadDoctorSnils"), snils_,
                "HeadDoctorSnils не совпадает");
        Assertions.assertEquals(Response.getString("Result[0].DateDirection").substring(0, 10),
                datedirection_.substring(0, 10), "DateDirection не совпадает");
        Assertions.assertEquals(Response.getString("Result[0].ConsultationReason"), consultationreason_,
                "ConsultationReason не совпадает");
        Assertions.assertEquals(Response.getString("Result[0].ConsultationTypeId"), consultationtypeid_,
                "ConsultationTypeId не совпадает");
        Assertions.assertEquals(Response.getString("Result[0].Profile"), medprofile_, "Profile не совпадает");
        Assertions.assertEquals(Response.getString("Result[0].DiagnosisCode"), diagnos_, "DiagnosisCode не совпадает");
        Assertions.assertEquals(Response.getString("Result[0].Reason"), reason_, "Reason не совпадает");
        Assertions.assertEquals(Response.getString("Result[0].DirectedFrom"), directedfrom_,
                "DirectedFrom не совпадает");
        if (requirevideoconsult_.equals("0")) {
            Assertions.assertEquals(Response.getString("Result[0].RequireVideoConsult"), "false",
                    "RequireVideoConsult не совпадает");
        } else {
            Assertions.assertEquals(Response.getString("Result[0].RequireVideoConsult"), "true",
                    "RequireVideoConsult не совпадает");
        }
        Assertions.assertEquals(Response.getString("Result[0].PatientPlacement"), patientplacement_,
                "PatientPlacement не совпадает");
    }
}
