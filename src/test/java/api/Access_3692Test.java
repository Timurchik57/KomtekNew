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

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Проверка получения документов в HTML и PDF")
@Tag("Проверка_БД")
@Tag("Проверка_json")
@Tag("Основные")
@Tag("Дополнительные_параметры")
public class Access_3692Test extends BaseAPI {

    public boolean Web;
    public boolean Pdf;

    @Issue(value = "TEL-3692")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отправляем смс и проверяем его HTML")
    @Description("Отправить запрос api/smd после Get методом получить его HTML")
    public void Access_3692() throws IOException, SQLException, InterruptedException {
        AddXml("SMS/SMS3.xml", "3", 1, 3, 1, 9, 18, 1, 57, 21);
        AddXml("SMS/SMS3.xml", "3", 2, 3, 1, 9, 18, 1, 57, 21);
        AddXml("SMS/SMS3.xml", "3", 3, 2, 1, 9, 18, 1, 57, 21);
        AddXml("SMS/SMS3.xml", "3", 4, 2, 1, 9, 18, 1, 57, 21);
        AddXml("SMS/SMS3.xml", "3", 5, 3, 1, 9, 18, 1, 57, 21);
        AddXml("SMS/SMS3.xml", "3", 99, 3, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Отправляем смс и проверяем его HTML ВЕБ")
    @Description("Отправить запрос api/smd после Get методом получить его HTML метод для Веба")
    public void Access_3692_web() throws IOException, SQLException, InterruptedException {
        Web = true;
        AddXml("SMS/SMS3.xml", "3", 1, 3, 1, 9, 18, 1, 57, 21);
        AddXml("SMS/SMS3.xml", "3", 2, 3, 1, 9, 18, 1, 57, 21);
        AddXml("SMS/SMS3.xml", "3", 3, 2, 1, 9, 18, 1, 57, 21);
        AddXml("SMS/SMS3.xml", "3", 4, 2, 1, 9, 18, 1, 57, 21);
        AddXml("SMS/SMS3.xml", "3", 5, 3, 1, 9, 18, 1, 57, 21);
        AddXml("SMS/SMS3.xml", "3", 99, 3, 1, 9, 18, 1, 57, 21);
    }

//    @Test
//    @DisplayName("Отправляем смс и проверяем его PDF")
//    @Description("Отправить запрос api/smd после Get методом получить его PDF")
//    public void Access_3694() throws IOException, SQLException, InterruptedException {
//        Pdf = true;
//        AddXml("SMS/SMS3.xml", "3", 1, 3, 1, 9, 18, 1, 57, 21);
//        AddXml("SMS/SMS3.xml", "3", 2, 3, 1, 9, 18, 1, 57, 21);
//        AddXml("SMS/SMS3.xml", "3", 3, 2, 1, 9, 18, 1, 57, 21);
//        AddXml("SMS/SMS3.xml", "3", 4, 2, 1, 9, 18, 1, 57, 21);
//        AddXml("SMS/SMS3.xml", "3", 5, 3, 1, 9, 18, 1, 57, 21);
//        AddXml("SMS/SMS3.xml", "3", 99, 3, 1, 9, 18, 1, 57, 21);
//    }
//
//    @Test
//    @DisplayName("Отправляем смс и проверяем его PDF веба")
//    @Description("Отправить запрос api/smd после Get методом получить его PDF метод для Веба")
//    public void Access_3694_web() throws IOException, SQLException, InterruptedException {
//        Web = true;
//        AddXml("SMS/SMS3.xml", "3", 1, 3, 1, 9, 18, 1, 57, 21);
//        AddXml("SMS/SMS3.xml", "3", 2, 3, 1, 9, 18, 1, 57, 21);
//        AddXml("SMS/SMS3.xml", "3", 3, 2, 1, 9, 18, 1, 57, 21);
//        AddXml("SMS/SMS3.xml", "3", 4, 2, 1, 9, 18, 1, 57, 21);
//        AddXml("SMS/SMS3.xml", "3", 5, 3, 1, 9, 18, 1, 57, 21);
//        AddXml("SMS/SMS3.xml", "3", 99, 3, 1, 9, 18, 1, 57, 21);
//    }

    @Step("Метод для отправки 2 версии документа ВИМИС")
    public void AddXml (String FileName, String DocType, Integer vmcl, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, SQLException, InterruptedException {
        TableVmcl(vmcl);

        xml.ApiSmd(FileName, DocType, vmcl, 1, true, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);
        xml.ReplacementWordInFileBack(FileName);
        if (vmcl != 99) {
            CollbekVimis("" + xml.uuid + "", "0", "Проверка 3692", smsBase, vmcl);
        } else {
            CollbekKremd("" + xml.uuid + "", "error", "Проверка 3692", remdBase);
        }

        /** Другой документ нужен для того, чтобы документы отличаелись и мы могли убедиться, что в поиске берётся вторая версия документа */
        xml.ApiSmd("SMS/SMS3_1.xml", DocType, vmcl, 0, false, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);
        xml.ReplacementWordInFileBack("SMS/SMS3_1.xml");

        getDocument(""+xml.uuid+"",  vmcl);
    }


    @Step("Метод для поиска тела документа (HTML)")
    public void getDocument (String localUid, Integer vmcl) throws IOException, SQLException {
        String url = null;

        if (!Web) {
            if (Pdf) {
                url = ApiVimis + "/api/smd/prettyfile/pdf/"+localUid+"";
            } else {
                url = ApiVimis + "/api/smd/prettyfile/cda/"+localUid+"";
            }
        } else {
            sql.StartConnection("Select * from "+smsBase+" where local_uid = '"+localUid+"';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
            }
            if (Pdf) {
                url = WebVimis + "/sms/pdf/"+sql.value+"?smsType="+vmcl+"";
            } else {
                url = WebVimis + "/sms/cda/"+sql.value+"?smsType="+vmcl+"";
            }
        }

        String params[] = {"smsType", ""+vmcl+""};
        Api(url, "get", params, null, "", 200, true);

        if (!Pdf) {
            Assertions.assertEquals(Response.getString("result.localUid"), localUid, "Не нашёлся документ по LocalUid - " + localUid + "");
            if (!Web) {
                Assertions.assertTrue(Response.getString("result.patientFullName").contains("Новосельцев Михаилллллллллллллллллллллллллллллллллллл"), "Не нашёлся документ по LocalUid - " + localUid + "");
            }
            if (vmcl == 99) {
                Assertions.assertEquals(Response.getString("result.documentType"), "РЭМД", "Не нашёлся документ по LocalUid - " + localUid + "");
            } else {
                Assertions.assertEquals(Response.getString("result.documentType"), "ВИМИС", "Не нашёлся документ по LocalUid - " + localUid + "");
            }
        } else {
//            // Сохранение PDF файла
//            byte[] pdfData = Response.getString("").getBytes();
//            File pdfFile = new File("src/test/resources/downloadedFile.pdf");
//            try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
//                fos.write(pdfData);
//            }
//
//            // Открытие PDF и проверка содержимого
//            try (PDDocument document = PDDocument.load(pdfFile)) {
//                PDFTextStripper pdfStripper = new PDFTextStripper();
//                String pdfContent = pdfStripper.getText(document);
//
//                // Пример проверки - ищем нужный текст
//                assertTrue(pdfContent.contains("Ожидаемый текст в PDF"), "PDF не содержит ожидаемого текста");
//            }
       }
    }
}
