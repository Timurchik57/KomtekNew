package api;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Мир Рис")
@Tag("Проверка_БД")
@Tag("Пакс")
@Tag("МирРис")
@Tag("Дополнительные_параметры")
public class Access_3588Test extends BaseAPI {
    public String directionGuid;

    @Issue(value = "TEL-3588")
    @Issue(value = "TEL-3589")
    @Issue(value = "TEL-3591")
    @Issue(value = "TEL-3592")
    @Issue(value = "TEL-3607")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отправка данных МирРис studies")
    @Description("Отправить запрос studies проверить добавление данных в telmed.directionsmosmedresult")
    public void Access_3588_studies() throws IOException, SQLException {
        System.out.println("Берём directionGuid из telmed.directions, которого нет в telmed.directionsmosmedresult");
        DirectionGuid();

        System.out.println("\n 1 Проверка - Отправляем валидные данные");
        Api(HostAddress + "/directions-mosmed-result/studies", "post", null, null,
                "{\n" +
                        "  \"directionGuid\": \" " + directionGuid + "\",\n" +
                        "  \"study_uid\": \"123\"\n" +
                        "}",
                200, true);
        Assertions.assertEquals(Response.getString("Description"), "Данные внесены");
        GetSql(directionGuid, "123", "1001", null, null, null, null, "" + Date + "");

        System.out.println("\n 2 Проверка - Повтнорно отправляем те же данные");
        Api(HostAddress + "/directions-mosmed-result/studies", "post", null, null,
                "{\n" +
                        "  \"directionGuid\": \" " + directionGuid + "\",\n" +
                        "  \"study_uid\": \"123\"\n" +
                        "}",
                400, true);
        Assertions.assertEquals(Response.getString("Description"), "Указанные идентификаторы уже внесены");

        System.out.println("\n 3 Проверка - Несуществующий directionGuid");
        Api(HostAddress + "/directions-mosmed-result/studies", "post", null, null,
                "{\n" +
                        "  \"directionGuid\": \"5c0903aa-efb2-48ac-887b-d468fbe13d05\",\n" +
                        "  \"study_uid\": \"123\"\n" +
                        "}",
                400, true);
        Assertions.assertEquals(Response.getString("Description"), "Направленный directionGuid не найден");
        sql.NotSQL(
                "SELECT count(*) FROM telmed.directionsmosmedresult where direction_guid = '5c0903aa-efb2-48ac-887b-d468fbe13d05';");

        System.out.println("\n 4 Проверка - Новый directionGuid, но старый study_uid");
        DirectionGuid();
        Api(HostAddress + "/directions-mosmed-result/studies", "post", null, null,
                "{\n" +
                        "  \"directionGuid\": \"" + directionGuid + "\",\n" +
                        "  \"study_uid\": \"123\"\n" +
                        "}",
                200, true);
        Assertions.assertEquals(Response.getString("Description"), "Данные внесены");
        GetSql(directionGuid, "123", "1001", null, null, null, null, "" + Date + "");

        System.out.println("\n 5 Проверка - Тот же directionGuid, но новый study_uid");
        Api(HostAddress + "/directions-mosmed-result/studies", "post", null, null,
                "{\n" +
                        "  \"directionGuid\": \"" + directionGuid + "\",\n" +
                        "  \"study_uid\": \"1234\"\n" +
                        "}",
                400, true);
        Assertions.assertEquals(Response.getString("Description"),
                "По указанному идентификатору уже внесен study_uid 123");

        System.out.println("\n 6 Проверка - Без study_uid");
        DirectionGuid();
        Api(HostAddress + "/directions-mosmed-result/studies", "post", null, null,
                "{\n" +
                        "  \"directionGuid\": \"" + directionGuid + "\",\n" +
                        "  \"study_uid\": \"\"\n" +
                        "}",
                200, true);
        Assertions.assertEquals(Response.getString("Description"), "Данные внесены");
        GetSql(directionGuid, "", "1001", null, null, null, null, "" + Date + "");

        System.out.println("\n 7 Проверка - Без directionGuid");
        Api(HostAddress + "/directions-mosmed-result/studies", "post", null, null,
                "{\n" +
                        "  \"directionGuid\": \"\",\n" +
                        "  \"study_uid\": \"null\"\n" +
                        "}",
                400, true);
        Assertions.assertEquals(Response.getString("Description"), "directionGuid обязателен для заполнения");

        System.out.println("\n 8 Проверка - Без directionGuid");
        Api(HostAddress + "/directions-mosmed-result/studies", "post", null, null,
                "{\n" +
                        "  \"study_uid\": \"null\"\n" +
                        "}",
                400, true);
        Assertions.assertEquals(Response.getString("Description"), "directionGuid обязателен для заполнения");

        System.out.println("\n 9 Проверка - study_uid = null");
        DirectionGuid();
        Api(HostAddress + "/directions-mosmed-result/studies", "post", null, null,
                "{\n" +
                        "  \"directionGuid\": \"" + directionGuid + "\",\n" +
                        "  \"study_uid\": null\n" +
                        "}",
                520, true);
        sql.NotSQL(
                "SELECT count(*) FROM telmed.directionsmosmedresult where direction_guid = '" + directionGuid + "';");

        System.out.println("\n 10 Проверка - Проверяем обновление данных, а не добавление новой записи");
        Api(HostAddress + "/directions-mosmed-result/studies", "post", null, null,
                "{\n" +
                        "  \"directionGuid\": \"" + directionGuid + "\",\n" +
                        "  \"study_uid\": \"321\"\n" +
                        "}",
                200, true);
        Assertions.assertEquals(Response.getString("Description"), "Данные внесены");
        GetSql(directionGuid, "321", "1001", null, null, null, null, "" + Date + "");
    }

    @Test
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отправка данных МирРис study-data")
    @Description("Отправить запрос study-data проверить добавление данных в telmed.directionsmosmedresult")
    public void Access_3588_study_data() throws IOException, SQLException, InterruptedException {
        System.out.println("Берём directionGuid из telmed.directions, которого нет в telmed.directionsmosmedresult");
        DirectionGuid();

        System.out.println("\n 1 Проверка - Отправляем валидные данные");
        Api(HostAddress + "/directions-mosmed-result/study-data", "post", null, null,
                "{\n" +
                        "\"Guid\":\"" + directionGuid + "\", \n" +
                        "\"CreatedAt\": \"2024-10-21T14:39:13.238+0300\",\n" +
                        "\"Report\": \"норм\"\n" +
                        "}",
                200, true);
        Assertions.assertEquals(Response.getString("Description"), "Результат внесен");
        GetSql(directionGuid, null, null, "2024-10-21", "норм", null, null, "" + Date + "");

        System.out.println("\n 2 Проверка - Отправляем все поля");
        DirectionGuid();
        Api(HostAddress + "/directions-mosmed-result/study-data", "post", null, null,
                "{\n" +
                        "\"Guid\":\"" + directionGuid + "\", \n" +
                        "\"CreatedAt\": \"2024-10-21T14:39:13.238+0300\",\n" +
                        "\"Report\": \"норм\",\n" +
                        "\"Error\": \"Body part error\",\n" +
                        "\"Description\": \"Часть тела в DICOM-файле не соответствует части тела в сообщении Kafka и не поддерживается сервисом\"\n" +
                        "}",
                400, true);
        Assertions.assertEquals(Response.getString("Description"),
                "При направлении error или description поля report не должно быть в запросе");
        sql.NotSQL(
                "SELECT count(*) FROM telmed.directionsmosmedresult where direction_guid = '" + directionGuid + "';");

        System.out.println("\n 3 Проверка - Несуществующий DirectionGuid");
        directionGuid = "7306e9f2-0821-4524-bb39-19af87010312";
        Api(HostAddress + "/directions-mosmed-result/study-data", "post", null, null,
                "{\n" +
                        "\"Guid\":\"" + directionGuid + "\", \n" +
                        "\"CreatedAt\": \"2024-10-21T14:39:13.238+0300\",\n" +
                        "\"Report\": \"норм\",\n" +
                        "}",
                400, true);
        Assertions.assertEquals(Response.getString("Description"), "Направленный guid не найден");
        sql.NotSQL(
                "SELECT count(*) FROM telmed.directionsmosmedresult where direction_guid = '" + directionGuid + "';");

        System.out.println("\n 4 Проверка - Проверяем дополнение данных после studies + Report");
        DirectionGuid();
        Api(HostAddress + "/directions-mosmed-result/studies", "post", null, null,
                "{\n" +
                        "  \"directionGuid\": \"" + directionGuid + "\",\n" +
                        "  \"study_uid\": \"321\"\n" +
                        "}",
                200, true);
        Assertions.assertEquals(Response.getString("Description"), "Данные внесены");
        GetSql(directionGuid, "321", "1001", null, null, null, null, "" + Date + "");

        Api(HostAddress + "/directions-mosmed-result/study-data", "post", null, null,
                "{\n" +
                        "\"Guid\":\"" + directionGuid + "\", \n" +
                        "\"CreatedAt\": \"2024-10-21T14:39:13.238+0300\",\n" +
                        "\"Report\": \"норм\",\n" +
                        "}",
                200, true);
        Assertions.assertEquals(Response.getString("Description"), "Результат внесен");
        GetSql(directionGuid, "321", "1001", "2024-10-21", "норм", null, null, "" + Date + "");

        System.out.println("\n 5 Проверка - Проверяем дополнение данных после studies + Error + Description");
        DirectionGuid();
        Api(HostAddress + "/directions-mosmed-result/studies", "post", null, null,
                "{\n" +
                        "  \"directionGuid\": \"" + directionGuid + "\",\n" +
                        "  \"study_uid\": \"321\"\n" +
                        "}",
                200, true);
        Assertions.assertEquals(Response.getString("Description"), "Данные внесены");
        GetSql(directionGuid, "321", "1001", null, null, null, null, "" + Date + "");

        Api(HostAddress + "/directions-mosmed-result/study-data", "post", null, null,
                "{\n" +
                        "\"Guid\":\"" + directionGuid + "\", \n" +
                        "\"CreatedAt\": \"2024-10-21T14:39:13.238+0300\",\n" +
                        "\"Error\": \"норм\",\n" +
                        "\"Description\": \"норм\"\n" +
                        "}",
                200, true);
        Assertions.assertEquals(Response.getString("Description"), "Результат внесен");
        GetSql(directionGuid, "321", "1001", "2024-10-21", null, "норм", "норм", "" + Date + "");

//        System.out.println("\n 6 Проверка - Проверяем дополнение данных после study-data");
//        DirectionGuid();
//        Api(HostAddress + "/directions-mosmed-result/study-data", "post", null, null,
//                "{\n" +
//                        "\"Guid\":\""+directionGuid+"\", \n" +
//                        "\"CreatedAt\": \"2024-10-21T14:39:13.238+0300\",\n" +
//                        "\"Report\": \"норм\",\n" +
//                        "}",
//                200, true);
//        Assertions.assertEquals(Response.getString("Description"), "Результат внесен");
//        GetSql(directionGuid, null, null, "2024-10-21", "норм", null, null, ""+Date+"");
//
//        Api(HostAddress + "/directions-mosmed-result/studies", "post", null, null,
//                "{\n" +
//                        "  \"directionGuid\": \""+directionGuid+"\",\n" +
//                        "  \"study_uid\": \"321\"\n" +
//                        "}",
//                200, true);
//        Assertions.assertEquals(Response.getString("Description"), "Данные внесены");
//        GetSql(directionGuid, "321", "1001", "2024-10-21", "норм", null, null, ""+Date+"");

        System.out.println("\n 7 Проверка - Используем тот же DirectionGuid + Report");
        Api(HostAddress + "/directions-mosmed-result/study-data", "post", null, null,
                "{\n" +
                        "\"Guid\":\"" + directionGuid + "\", \n" +
                        "\"CreatedAt\": \"2024-10-21T14:39:13.238+0300\",\n" +
                        "\"Report\": \"норм\",\n" +
                        "}",
                400, true);
        Assertions.assertEquals(Response.getString("Description"), "Ошибка. Результат по направлению уже внесен");

        System.out.println("\n 8 Проверка - Используем тот же DirectionGuid + Error");
        Api(HostAddress + "/directions-mosmed-result/study-data", "post", null, null,
                "{\n" +
                        "\"Guid\":\"" + directionGuid + "\", \n" +
                        "\"CreatedAt\": \"2024-10-21T14:39:13.238+0300\",\n" +
                        "\"Error\": \"норм\",\n" +
                        "}",
                400, true);
        Assertions.assertEquals(Response.getString("Description"), "Ошибка. Результат по направлению уже внесен");

        System.out.println("\n 9 Проверка - Используем тот же DirectionGuid + Description");
        Api(HostAddress + "/directions-mosmed-result/study-data", "post", null, null,
                "{\n" +
                        "\"Guid\":\"" + directionGuid + "\", \n" +
                        "\"CreatedAt\": \"2024-10-21T14:39:13.238+0300\",\n" +
                        "\"Description\": \"норм\",\n" +
                        "}",
                400, true);
        Assertions.assertEquals(Response.getString("Description"), "Ошибка. Результат по направлению уже внесен");

        System.out.println("\n 10 Проверка - Отправляем только Error");
        DirectionGuid();
        Api(HostAddress + "/directions-mosmed-result/study-data", "post", null, null,
                "{\n" +
                        "\"Guid\":\"" + directionGuid + "\", \n" +
                        "\"CreatedAt\": \"2024-10-21T14:39:13.238+0300\",\n" +
                        "\"Error\": \"норм\",\n" +
                        "}",
                200, true);
        Assertions.assertEquals(Response.getString("Description"), "Результат внесен");
        GetSql(directionGuid, null, null, "2024-10-21", null, "норм", null, "" + Date + "");

        System.out.println("\n 11 Проверка - После отправки только Error + тот же DirectionGuid + Description");
        Api(HostAddress + "/directions-mosmed-result/study-data", "post", null, null,
                "{\n" +
                        "\"Guid\":\"" + directionGuid + "\", \n" +
                        "\"CreatedAt\": \"2024-10-21T14:39:13.238+0300\",\n" +
                        "\"Description\": \"норм\",\n" +
                        "}",
                400, true);
        Assertions.assertEquals(Response.getString("Description"), "Ошибка. Результат по направлению уже внесен");

        System.out.println("\n 12 Проверка - После отправки только Error + тот же DirectionGuid + Report");
        Api(HostAddress + "/directions-mosmed-result/study-data", "post", null, null,
                "{\n" +
                        "\"Guid\":\"" + directionGuid + "\", \n" +
                        "\"CreatedAt\": \"2024-10-21T14:39:13.238+0300\",\n" +
                        "\"Report\": \"норм\",\n" +
                        "}",
                400, true);
        Assertions.assertEquals(Response.getString("Description"), "Ошибка. Результат по направлению уже внесен");

        System.out.println("\n 13 Проверка - Отправляем только Description");
        DirectionGuid();
        Api(HostAddress + "/directions-mosmed-result/study-data", "post", null, null,
                "{\n" +
                        "\"Guid\":\"" + directionGuid + "\", \n" +
                        "\"CreatedAt\": \"2024-10-21T14:39:13.238+0300\",\n" +
                        "\"Description\": \"норм\",\n" +
                        "}",
                200, true);
        Assertions.assertEquals(Response.getString("Description"), "Результат внесен");
        GetSql(directionGuid, null, null, "2024-10-21", null, null, "норм", "" + Date + "");

        System.out.println("\n 14 Проверка - После отправки только Description + тот же DirectionGuid + Report");
        Api(HostAddress + "/directions-mosmed-result/study-data", "post", null, null,
                "{\n" +
                        "\"Guid\":\"" + directionGuid + "\", \n" +
                        "\"CreatedAt\": \"2024-10-21T14:39:13.238+0300\",\n" +
                        "\"Report\": \"норм\",\n" +
                        "}",
                400, true);
        Assertions.assertEquals(Response.getString("Description"), "Ошибка. Результат по направлению уже внесен");

        System.out.println("\n 15 Проверка - После отправки только Description + тот же DirectionGuid + Error");
        Api(HostAddress + "/directions-mosmed-result/study-data", "post", null, null,
                "{\n" +
                        "\"Guid\":\"" + directionGuid + "\", \n" +
                        "\"CreatedAt\": \"2024-10-21T14:39:13.238+0300\",\n" +
                        "\"Error\": \"норм\",\n" +
                        "}",
                400, true);
        Assertions.assertEquals(Response.getString("Description"), "Ошибка. Результат по направлению уже внесен");

        System.out.println("\n 16 Проверка - Совместно используем Description + Error");
        DirectionGuid();
        Api(HostAddress + "/directions-mosmed-result/study-data", "post", null, null,
                "{\n" +
                        "\"Guid\":\"" + directionGuid + "\", \n" +
                        "\"CreatedAt\": \"2024-10-21T14:39:13.238+0300\",\n" +
                        "\"Error\": \"норм\",\n" +
                        "\"Description\": \"норм\"\n" +
                        "}",
                200, true);
        Assertions.assertEquals(Response.getString("Description"), "Результат внесен");
        GetSql(directionGuid, null, null, "2024-10-21", null, "норм", "норм", "" + Date + "");
    }

    @Step("Метод для определения какого direction_guid нет в telmed.directionsmosmedresult")
    public void DirectionGuid() throws SQLException {
        Integer number = 0;

        // Получаем все direction_guid из таблицы directionsmosmedresult
        sql.StartConnection("SELECT direction_guid FROM telmed.directionsmosmedresult;");
        Set<String> mosmedGuids = new HashSet<>();
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("direction_guid");
            // System.out.println(sql.value);
            mosmedGuids.add(sql.value);
        }

        sql.StartConnection("Select * from telmed.directions ORDER BY id DESC;");
        while (sql.resultSet.next() & number < 300) {
            sql.value = sql.resultSet.getString("directionguid");

            boolean isMatching = false;

            // Сравниваем с каждым элементом из telmed.directionsmosmedresult
            for (String guid : mosmedGuids) {
                if (sql.value.equals(guid)) {
                    isMatching = true;
                    number++;
                    break; // Прерываем, если есть совпадение
                }
            }
            if (!isMatching) {
                directionGuid = sql.value;
                break;
            }
        }
    }

    @Step("Метод сравнения данных в telmed.directionsmosmedresult")
    public void GetSql(String guid, String Study_uid, String Medicalidmu, String Created_at, String Report, String Error, String Description, String Added_date) throws SQLException {
        String study_uid = null;
        String medicalidmu = null;
        String created_at = null;
        String report = null;
        String error = null;
        String description = null;
        String added_date = null;

        /** Проверяем, что отображается всегда 1 запись, что записи не дублируются */
        sql.SQL("SELECT count(*) FROM telmed.directionsmosmedresult where direction_guid = '" + guid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
            Assertions.assertEquals("1", sql.value);
        }

        sql.StartConnection("SELECT * FROM telmed.directionsmosmedresult where direction_guid = '" + guid + "';");
        while (sql.resultSet.next()) {
            study_uid = sql.resultSet.getString("study_uid");
            medicalidmu = sql.resultSet.getString("medicalidmu");
            created_at = sql.resultSet.getString("created_at");
            report = sql.resultSet.getString("report");
            error = sql.resultSet.getString("error");
            description = sql.resultSet.getString("description");
            added_date = sql.resultSet.getString("added_date");
        }

        Assertions.assertEquals(study_uid, Study_uid, "study_uid не совпадает");
        Assertions.assertEquals(medicalidmu, Medicalidmu, "medicalidmu не совпадает");
        if (Created_at == null) {
            Assertions.assertEquals(created_at, Created_at, "created_at не совпадает");
        } else {
            Assertions.assertEquals(created_at.substring(0, 10), Created_at, "created_at не совпадает");
        }
        Assertions.assertEquals(report, Report, "report не совпадает");
        Assertions.assertEquals(error, Error, "error не совпадает");
        Assertions.assertEquals(description, Description, "description не совпадает");
        if (Added_date == null) {
            Assertions.assertEquals(added_date, Added_date, "added_date не совпадает");
        } else {
            Assertions.assertEquals(added_date.substring(0, 10), Added_date, "added_date не совпадает");
        }
    }
}
