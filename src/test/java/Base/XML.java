package Base;

import api.Before.Authorization;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIn.isOneOf;

public class XML extends BaseAPI {
    public String body;
    public String content;
    public UUID uuid;
    public static String Type;
    public String encodedString;
    public Map<String, Object> changes = new HashMap<>();
    public Map<String, Object> originalChanges = new HashMap<>();
    /**
     * Для добавления значений в JSON
     */
    public Map<String, List<Map<String, Object>>> addElements = new HashMap<>();
    public Map<String, Object> addElementsFirst = new HashMap<>();
    public List<Map<String, Object>> associationList = new ArrayList<>();

    /**
     * Метод для отправки СМС
     * @param FileName                    название файла
     * @param DocType                     doctype фалйа XML
     * @param vmcl                        направление СЭМД, vmcl фалйа XML
     * @param number                      переменная, которая отвечает за увеличение генерируемых переменных для отправки СЭМД XML
     * @param RanLoc                      true = нужно генерировать новый LocalUid, false = не нужно
     * @param docTypeVersion              docTypeVersion фалйа XML
     * @param Role                        роль подписанта
     * @param position                    должность подписанта
     * @param speciality                  специальность подписанта
     * @param Role1,position1,speciality1 может быть несколько подписантов
     */
    @Step("Отправка СМС Doctype = {1}, vmcl = {2}")
    public void ApiSmd(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, InterruptedException {

        TableVmcl(vmcl);

        ReplacementWordInFile(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality,
                Role1, position1, speciality1);

        Api(HostAddress + "/api/smd", "post", null, null, body, 200, true);
        String value = Response.getString("result[0].message");
        if (vmcl == 99) {
            assertThat(value,
                    isOneOf("СМС предназначенная только для передачи в РЭМД успешно опубликована в ЦУ РС ЕГИСЗ.",
                            "СМС предназначенная только для передачи в РЭМД успешно опубликована в РИЭМК."));
        }
        if (vmcl == 1) {
            if (KingNumber == 3) {
                Assertions.assertEquals(value, "СМС по направлению \"Онкология\" успешно опубликован в РИЭМК.");
            } else {
                Assertions.assertEquals(value,
                        "СМС по направлению \"Онкология\" успешно опубликован в ЦУ РС ЕГИСЗ.");
            }
        }
        if (vmcl == 2) {
            if (KingNumber == 3) {
                Assertions.assertEquals(value, "СМС по направлению \"Профилактика\" успешно опубликован в РИЭМК.");
            } else {
                Assertions.assertEquals(value,
                        "СМС по направлению \"Профилактика\" успешно опубликован в ЦУ РС ЕГИСЗ.");
            }
        }
        if (vmcl == 3) {
            if (KingNumber == 3) {
                Assertions.assertEquals(value, "СМС по направлению \"АкиНео\" успешно опубликован в РИЭМК.");
            } else {
                Assertions.assertEquals(value,
                        "СМС по направлению \"АкиНео\" успешно опубликован в ЦУ РС ЕГИСЗ.");
            }
        }
        if (vmcl == 4) {
            if (KingNumber == 3) {
                Assertions.assertEquals(value, "СМС по направлению \"ССЗ\" успешно опубликован в РИЭМК.");
            } else {
                Assertions.assertEquals(value,
                        "СМС по направлению \"ССЗ\" успешно опубликован в ЦУ РС ЕГИСЗ.");
            }
        }
        if (vmcl == 5) {
            if (KingNumber == 3) {
                Assertions.assertEquals(value,
                        "СМС по направлению \"Инфекционные болезни\" успешно опубликован в РИЭМК.");
            } else {
                Assertions.assertEquals(value,
                        "СМС по направлению \"Инфекционные болезни\" успешно опубликован в ЦУ РС ЕГИСЗ.");
            }
        }
    }

    /**
     * Метод для замены слов в файле
     * @params FileName - название файла
     * @params Search - слово в файле, которое нужно заменить
     * @params Value - слово на которое нужно заменить
     */
    public void ReplaceWord(String File, String Word, String Replace) throws IOException, InterruptedException {
        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        props = new Properties();
        props.load(in);
        in.close();
        Charset charset = StandardCharsets.UTF_8;
        Path path = Paths.get(File);
        Files.write(
                path,
                new String(Files.readAllBytes(path), charset).replace(Word, Replace)
                        .getBytes(charset)
        );
    }

    /**
     * Метод для замены слов в файле
     */
    public void ReplaceWord1() throws IOException, InterruptedException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("test.txt")));
        String newStr = "";
        while ((newStr = in.readLine()) != null) {
            newStr = newStr.replace("15", "45");
        }
        System.out.println(in);
    }

    /**
     * Метод для замены нужных переменных в смс
     * FileName - название файла
     */
    @Step("Возвращаем значения в Файле")
    public void ReplacementWordInFileBack(String FileName) throws IOException, InterruptedException {
        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        props = new Properties();
        props.load(in);
        in.close();
        if (FileName == "SMS/SMS22-vmcl=3.xml") {
            if (KingNumber == 1) {
                ReplaceWord(FileName, "1.2.643.5.1.13.13.12.2.86.9006", "${ksomo}");
                ReplaceWord(FileName, "2022", "${ksoyear}");
                ReplaceWord(FileName, "22-86-00002", "${ksonumber}");
            }
            if (KingNumber == 2) {
                ReplaceWord(FileName, "1.2.643.5.1.13.13.12.2.86.9006", "${ksomo}");
                ReplaceWord(FileName, "2021", "${ksoyear}");
                ReplaceWord(FileName, "21-86-05295", "${ksonumber}");
            }
            if (KingNumber == 4) {
                ReplaceWord(FileName, "1.2.643.5.1.13.13.12.2.86.9006", "${ksomo}");
                ReplaceWord(FileName, "2021", "${ksoyear}");
                ReplaceWord(FileName, "21-77-65842", "${ksonumber}");
            }
        }
        ReplaceWord(FileName, props.getProperty("value_ID"), "${iD}");
        ReplaceWord(FileName, props.getProperty("value_SetID"), "${setId}");
        ReplaceWord(FileName, props.getProperty("value_VN"), "${versionNumber}");
        ReplaceWord(FileName, Departmen, "${depart}");
        ReplaceWord(FileName, MO, "${mo}");
        ReplaceWord(FileName, PatientGuid, "${guid}");
        ReplaceWord(FileName, NameMO, "${namemo}");
        ReplaceWord(FileName, Snils, "${snils}");
    }

    /**
     * Метод для замены нужных переменных
     * @param FileName                    название файла
     * @param DocType                     doctype фалйа XML
     * @param vmcl                        направление СЭМД, vmcl фалйа XML
     * @param number                      переменная, которая отвечает за увеличение генерируемых переменных для отправки СЭМД XML
     * @param RanLoc                      true = нужно генерировать новый LocalUid, false = не нужно
     * @param docTypeVersion              docTypeVersion фалйа XML
     * @param Role                        роль подписанта
     * @param position                    должность подписанта
     * @param speciality                  специальность подписанта
     * @param Role1,position1,speciality1 может быть несколько подписантов
     */
    public String ReplacementWordInFile(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, InterruptedException {

        Authorization authorization = new Authorization();
        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        props = new Properties();
        props.load(in);
        in.close();

        Token = TokenInit();
        Type = FileName;

        if (number == 1) {
            Thread.sleep(1000);
            ID = (int) Math.floor(timestamp.getTime() / 1000);
            SetID = (int) Math.floor(timestamp.getTime() / 1000) + 1;
            VN = (int) Math.floor(timestamp.getTime() / 1000) + 2;
        }
        if (number == 2) {
            if (props.getProperty("value_ID") == null) {
                ID = (int) Math.floor(timestamp.getTime() / 1000) + 5;
                SetID = (int) Math.floor(timestamp.getTime() / 1000) + 5;
                VN = (int) Math.floor(timestamp.getTime() / 1000) + 5;
            } else {
                ID = Long.parseLong(props.getProperty("value_ID")) + 5;
                SetID = Long.parseLong(props.getProperty("value_SetID")) + 5;
                VN = Long.parseLong(props.getProperty("value_VN")) + 5;
            }
        }
        InputPropFile("value_ID", String.valueOf(ID));
        InputPropFile("value_SetID", String.valueOf(SetID));
        InputPropFile("value_VN", String.valueOf(VN));

        if (FileName == "SMS/SMS22-vmcl=3.xml") {
            if (KingNumber == 1) {
                ReplaceWord(FileName, "${ksomo}", "1.2.643.5.1.13.13.12.2.86.9006");
                ReplaceWord(FileName, "${ksoyear}", "2022");
                ReplaceWord(FileName, "${ksonumber}", "22-86-00002");
            }
            if (KingNumber == 2) {
                ReplaceWord(FileName, "${ksomo}", "1.2.643.5.1.13.13.12.2.86.9006");
                ReplaceWord(FileName, "${ksoyear}", "2021");
                ReplaceWord(FileName, "${ksonumber}", "21-86-05295");
            }
            if (KingNumber == 4) {
                ReplaceWord(FileName, "${ksomo}", "1.2.643.5.1.13.13.12.2.86.9006");
                ReplaceWord(FileName, "${ksoyear}", "2021");
                ReplaceWord(FileName, "${ksonumber}", "21-77-65842");
            }
        }
        ReplaceWord(FileName, "${iD}", String.valueOf(ID));
        ReplaceWord(FileName, "${setId}", String.valueOf(SetID));
        ReplaceWord(FileName, "${versionNumber}", String.valueOf(VN));
        ReplaceWord(FileName, "${depart}", Departmen);
        ReplaceWord(FileName, "${mo}", MO);
        ReplaceWord(FileName, "${guid}", PatientGuid);
        ReplaceWord(FileName, "${namemo}", NameMO);
        ReplaceWord(FileName, "${snils}", Snils);

        content = new String(Files.readAllBytes(Paths.get(FileName)));
        PrintWriter out = new PrintWriter("src/test/resources/ignored/Xml.xml");
        out.println(content);
        out.close();
        encodedString = Base64.getEncoder().encodeToString(content.getBytes());

        /** Подсчёт чек Суммы для XML */
        Api("http://192.168.2.126:10117/CheckSum/CalculateCheckSum", "post", null, null,
                "{\n" +
                        "  \"base64String\": \"" + encodedString + "\"\n" +
                        "}", 200, false);
        CheckSum = Response.getString("checkSum");

        /** Получение подписи */
        Api(AddressSignature, "post", null, null,
                "{\"requestId\": \"2\",\n" +
                        "            \"serialNumber\": \"" + SerialNumberXml + "\",\n" +
                        "            \"message\": \"" + encodedString + "\",\n" +
                        "            \"isBase64\": true}", 200, false);
        Signatures = Response.getString("result.signedMessage");

        /** Подсчёт чек Суммы для Подписи */
        Api("http://192.168.2.126:10117/CheckSum/CalculateCheckSum", "post", null, null,
                "{\n" +
                        "  \"base64String\": \"" + Signatures + "\"\n" +
                        "}", 200, false);
        CheckSumSign = Response.getString("checkSum");

        if (RanLoc) {
            uuid = UUID.randomUUID();
            System.out.println(uuid);
        }
        String VMCL = null;
        if (vmcl == 99) {
            VMCL = "{\n" +
                    "    \"vmcl\": 99\n" +
                    "}";
        } else {
            VMCL = "{\n" +
                    "\"vmcl\": " + vmcl + ",\n" +
                    "\"triggerPoint\": 2,\n" +
                    "\"docTypeVersion\": " + docTypeVersion + "\n" +
                    "\n" +
                    "}\n";
        }

        body = "{\n" +
                "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                "    \"directionGuid\":\"d9af67c3-90c1-43ae-9f84-a3e3db0a161f\",\n" +
                "    \"DocType\":\"" + DocType + "\",\n" +
                "    \"DocContent\":\n" +
                "    {\n" +
                "        \"Document\":\"" + encodedString + "\",\n" +
                "        \"CheckSum\":" + CheckSum + "\n" +
                "        },\n" +
                "        \"LocalUid\":\"" + uuid + "\",\n" +
                "        \"Payment\":1,\n" +
                "        \"ReasonForAbsenceIdcase\":\n" +
                "        {\n" +
                "            \"CodeSystemVersion\":\"1.1\",\n" +
                "            \"Code\":10,\n" +
                "            \"CodeSystem\":\"1.2.643.5.1.13.13.99.2.286\"\n" +
                "        },\n" +
                "            \"VMCL\":\n" +
                "            [" + VMCL + " \n" +
                "            ],\n" +
                "            \"OrgSignature\":\n" +
                "            {\n" +
                "                \"Data\":\"" + Signatures + "\",\n" +
                "                \"CheckSum\":" + CheckSumSign + "\n" +
                "                },\n" +
                "                \"PersonalSignatures\":\n" +
                "                [\n" +
                "                    {\n" +
                "                        \"Signer\":\n" +
                "                        {\n" +
                "                            \"LocalId\":\"0001510378\",\n" +
                "                            \"Role\":{\"$\":\"" + Role + "\",\"@version\":\"2.4\"},\n" +
                "                            \"LastName\":\"Коситченков\",\n" +
                "                            \"FirstName\":\"Андрей\",\n" +
                "                            \"MiddleName\":\"Александрович\",\n" +
                "                            \"Snils\":\"18259202174\",\n" +
                "                            \"Position\":{\"$\":\"" + position + "\",\"@version\":\"" + Position + "\"},\n" +
                "                            \"Speciality\":{\"$\":" + speciality + ",\"@version\":\"" + Speciality + "\"},\n" +
                "                            \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                "                        },\n" +
                "                            \"Signature\":\n" +
                "                            {\n" +
                "                                \"Data\":\"" + Signatures + "\",\n" +
                "                                \"CheckSum\":" + CheckSumSign + "\n" +
                "                            },\n" +
                "                            \"Description\":\"Подпись сотрудника\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"Signer\":\n" +
                "                                {\n" +
                "                                    \"LocalId\":\"0003948083\",\n" +
                "                                    \"Role\":{\"$\":\"" + Role1 + "\",\"@version\":\"2.4\"},\n" +
                "                                    \"LastName\":\"Стрекнев\",\n" +
                "                                    \"FirstName\":\"Денис\",\n" +
                "                                    \"MiddleName\":\"Юрьевич\",\n" +
                "                                    \"Snils\":\"18287265004\",\n" +
                "                                    \"Position\":{\"$\":\"" + position1 + "\",\"@version\":\"" + Position + "\"},\n" +
                "                                    \"Speciality\":{\"$\":" + speciality1 + ",\"@version\":\"" + Speciality + "\"},\n" +
                "                                    \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                "                                },\n" +
                "                                    \"Signature\":\n" +
                "                                    {\n" +
                "                                        \"Data\":\"" + Signatures + "\",\n" +
                "                                        \"CheckSum\":" + CheckSumSign + "\n" +
                "                                        },\n" +
                "                                        \"Description\":\"Подпись сотрудника\"\n" +
                "                                    }\n" +
                "        ]\n" +
                "    }";
        System.out.println("Записали тело запроса в файл");
        PrintWriter out2 = new PrintWriter("src/test/resources/ignored/JsonBody.json");
        out2.println(body);
        out2.close();
        return body;
    }

    @Step("Метод замены нужных меременных в XML документе")
    public void XmlStart(String FileName, String DepartS, String MOS, String PatientGuidS, String NameMOS, String SnilsS, Integer number) throws IOException, InterruptedException {
        Token = TokenInit();

        Type = FileName;
        System.out.println("Формируем XML");

        Thread.sleep(1000);
        if (number == 1) {
            ID = (int) Math.floor(timestamp.getTime() / 1000);
            SetID = (int) Math.floor(timestamp.getTime() / 1000) + 1;
            VN = (int) Math.floor(timestamp.getTime() / 1000) + 2;
        } else {
            ID = Long.parseLong(ReadPropFile("value_ID")) + 5;
            SetID = Long.parseLong(ReadPropFile("value_SetID")) + 5;
            VN = Long.parseLong(ReadPropFile("value_VN")) + 5;
        }
        InputPropFile("value_ID", String.valueOf(ID));
        InputPropFile("value_SetID", String.valueOf(SetID));
        InputPropFile("value_VN", String.valueOf(VN));

        if (FileName == "SMS/SMS22-vmcl=3.xml") {
            if (KingNumber == 1) {
                ReplaceWord(FileName, "${ksomo}", "1.2.643.5.1.13.13.12.2.86.9006");
                ReplaceWord(FileName, "${ksoyear}", "2022");
                ReplaceWord(FileName, "${ksonumber}", "22-86-00002");
            }
            if (KingNumber == 2) {
                ReplaceWord(FileName, "${ksomo}", "1.2.643.5.1.13.13.12.2.86.9006");
                ReplaceWord(FileName, "${ksoyear}", "2021");
                ReplaceWord(FileName, "${ksonumber}", "21-86-05295");
            }
            if (KingNumber == 4) {
                ReplaceWord(FileName, "${ksomo}", "1.2.643.5.1.13.13.12.2.86.9006");
                ReplaceWord(FileName, "${ksoyear}", "2021");
                ReplaceWord(FileName, "${ksonumber}", "21-77-65842");
            }
        }

        ReplaceWord(FileName, "${iD}", String.valueOf(ID));
        ReplaceWord(FileName, "${setId}", String.valueOf(SetID));
        ReplaceWord(FileName, "${versionNumber}", String.valueOf(VN));
        ReplaceWord(FileName, "${depart}", DepartS);
        ReplaceWord(FileName, "${mo}", MOS);
        ReplaceWord(FileName, "${guid}", PatientGuidS);
        ReplaceWord(FileName, "${namemo}", NameMOS);
        ReplaceWord(FileName, "${snils}", SnilsS);

        content = new String(Files.readAllBytes(Paths.get(FileName)));
        encodedString = Base64.getEncoder().encodeToString(content.getBytes());

        /** Подсчёт чек Суммы для XML */
        Api("http://192.168.2.126:10117/CheckSum/CalculateCheckSum", "post", null, null,
                "{\n" +
                        "  \"base64String\": \"" + encodedString + "\"\n" +
                        "}", 200, false);
        CheckSum = Response.getString("checkSum");

        /** Получение подписи */
        Api(AddressSignature, "post", null, null,
                "{\"requestId\": \"2\",\n" +
                        "            \"serialNumber\": \"" + SerialNumberXml + "\",\n" +
                        "            \"message\": \"" + encodedString + "\",\n" +
                        "            \"isBase64\": true}", 200, false);
        Signatures = Response.getString("result.signedMessage");

        /** Подсчёт чек Суммы для Подписи */
        Api("http://192.168.2.126:10117/CheckSum/CalculateCheckSum", "post", null, null,
                "{\n" +
                        "  \"base64String\": \"" + Signatures + "\"\n" +
                        "}", 200, false);
        CheckSumSign = Response.getString("checkSum");
        uuid = UUID.randomUUID();
        System.out.println(uuid + " uuid который будет в теле запроса смс");
    }

    @Step("Метод подстановки в тело запроса api/smd")
    public void BodyXml(String DocType, Integer vmcl, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) {
        String textVmxl = "";
        if (vmcl != 99) {
            textVmxl = "{\n" +
                    "                    \"vmcl\": " + vmcl + ",\n" +
                    "                    \"triggerPoint\": 2,\n" +
                    "                    \"docTypeVersion\": " + docTypeVersion + "\n" +
                    "                    \n" +
                    "                }";
        } else {
            textVmxl = "{\n" +
                    "                    \"vmcl\": " + vmcl + "\n" +
                    "                }";
        }
        body = "{\n" +
                "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                "    \"DocType\":\"" + DocType + "\",\n" +
                "    \"BasedOnLocalUids\":[\"" + uuid + "\"], \n" +
                "    \"DocContent\":\n" +
                "    {\n" +
                "        \"Document\":\"" + encodedString + "\",\n" +
                "        \"CheckSum\":" + CheckSum + "\n" +
                "        },\n" +
                "        \"LocalUid\":\"" + uuid + "\",\n" +
                "        \"Payment\":1,\n" +
                "        \"ReasonForAbsenceIdcase\":\n" +
                "        {\n" +
                "            \"CodeSystemVersion\":\"1.1\",\n" +
                "            \"Code\":10,\n" +
                "            \"CodeSystem\":\"1.2.643.5.1.13.13.99.2.286\"\n" +
                "        },\n" +
                "            \"VMCL\":\n" +
                "            [" + textVmxl + "],\n" +
                "            \"OrgSignature\":\n" +
                "            {\n" +
                "                \"Data\":\"" + xml.Signatures + "\",\n" +
                "                \"CheckSum\":" + xml.CheckSumSign + "\n" +
                "                },\n" +
                "                \"PersonalSignatures\":\n" +
                "                [\n" +
                "                    {\n" +
                "                        \"Signer\":\n" +
                "                        {\n" +
                "                            \"LocalId\":\"0001510378\",\n" +
                "                            \"Role\":{\"$\":\"" + Role + "\",\"@version\":\"2.4\"},\n" +
                "                            \"LastName\":\"Коситченков\",\n" +
                "                            \"FirstName\":\"Андрей\",\n" +
                "                            \"MiddleName\":\"Александрович\",\n" +
                "                            \"Snils\":\"18259202174\",\n" +
                "                            \"Position\":{\"$\":\"" + position + "\",\"@version\":\"" + Position + "\"},\n" +
                "                            \"Speciality\":{\"$\":" + speciality + ",\"@version\":\"" + Speciality + "\"},\n" +
                "                            \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                "                        },\n" +
                "                            \"Signature\":\n" +
                "                            {\n" +
                "                                \"Data\":\"" + Signatures + "\",\n" +
                "                                  \"CheckSum\":" + CheckSumSign + "\n" +
                "                            },\n" +
                "                            \"Description\":\"Подпись сотрудника\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"Signer\":\n" +
                "                                {\n" +
                "                                    \"LocalId\":\"0003948083\",\n" +
                "                                    \"Role\":{\"$\":\"" + Role1 + "\",\"@version\":\"2.4\"},\n" +
                "                                    \"LastName\":\"Стрекнев\",\n" +
                "                                    \"FirstName\":\"Денис\",\n" +
                "                                    \"MiddleName\":\"Юрьевич\",\n" +
                "                                    \"Snils\":\"18287265004\",\n" +
                "                                    \"Position\":{\"$\":\"" + position1 + "\",\"@version\":\"" + Position + "\"},\n" +
                "                                    \"Speciality\":{\"$\":" + speciality1 + ",\"@version\":\"" + Speciality + "\"},\n" +
                "                                    \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                "                                },\n" +
                "                                    \"Signature\":\n" +
                "                                    {\n" +
                "                                        \"Data\":\"" + Signatures + "\",\n" +
                "                                       \"CheckSum\":" + CheckSumSign + "\n" +
                "                                        },\n" +
                "                                        \"Description\":\"Подпись сотрудника\"\n" +
                "                                    }\n" +
                "        ]\n" +
                "    }";
    }

    @Step("Метод замены нужных меременных в теле запроса документа при отправке СМС")
    public void JsonChange(String docType, String vmcl, String triggerPoint, String docTypeVersion, String Role, String position, String speciality, String Role1,
            String position1, String speciality1) {

        changes.put("$.PatientGuid", PatientGuid);
        changes.put("$.DocType", docType);
        changes.put("$.DocContent.Document", encodedString);
        changes.put("$.DocContent.CheckSum", CheckSum);
        changes.put("$.LocalUid", "" + uuid + "");

        changes.put("$.VMCL[0].vmcl", vmcl);
        changes.put("$.VMCL[0].triggerPoint", triggerPoint);
        changes.put("$.VMCL[0].docTypeVersion", docTypeVersion);

        changes.put("$.OrgSignature.Data", Signatures);
        changes.put("$.OrgSignature.CheckSum", CheckSumSign);
        changes.put("$.PersonalSignatures[0].Signer.Role.$", Role);
        changes.put("$.PersonalSignatures[0].Signer.Position.$", position);
        changes.put("$.PersonalSignatures[0].Signer.Position.@version", Position);
        changes.put("$.PersonalSignatures[0].Signer.Speciality.$", speciality);
        changes.put("$.PersonalSignatures[0].Signer.Speciality.@version", Speciality);
        changes.put("$.PersonalSignatures[0].Signer.Department", Departmen);
        changes.put("$.PersonalSignatures[0].Signature.Data", Signatures);
        changes.put("$.PersonalSignatures[0].Signature.CheckSum", CheckSumSign);

        changes.put("$.PersonalSignatures[1].Signer.Role.$", Role1);
        changes.put("$.PersonalSignatures[1].Signer.Position.$", position1);
        changes.put("$.PersonalSignatures[1].Signer.Position.@version", Position);
        changes.put("$.PersonalSignatures[1].Signer.Speciality.$", speciality1);
        changes.put("$.PersonalSignatures[1].Signer.Speciality.@version", Speciality);
        changes.put("$.PersonalSignatures[1].Signer.Department", Departmen);
        changes.put("$.PersonalSignatures[1].Signature.Data", Signatures);
        changes.put("$.PersonalSignatures[1].Signature.CheckSum", CheckSumSign);

        System.out.println("Заменили нужные переменные в теле запроса документа");
    }
}
