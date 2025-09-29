import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;

public class ParsingXml {

    static String pathFile = "src/main/java/file.xml";
    public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, IOException, TransformerException, SAXException {
      //  SetXml();
      //  getXml("/ClinicalDocument/component//section[title='Общие сведения']/text//tr[3]/td[2]/content/text()");
      //  getXml("/ClinicalDocument/component//section[title='Общие сведения']/text//td/content[text()='Место проведения']//td/content/text()");
     //   getXml("/ClinicalDocument/component//section[title='ОБЩИЕ СВЕДЕНИЯ']/entry[1]//value/@code");

        //3272
//        getXml("//recordTarget/patientRole/identity:IdentityDoc[identity:IdentityCardType/@code=24]/identity:Series");
//        getXml("//recordTarget/patientRole/identity:IdentityDoc[identity:IdentityCardType/@code=24]/identity:Number");
//        getXml("//recordTarget/patientRole/id/@extension");
//        getXml("//recordTarget/patientRole/patient/name/family");
//        getXml("//recordTarget/patientRole/patient/administrativeGenderCode/@code");
//        getXml("//recordTarget/patientRole/patient/birthTime/@value");
        getXml("(//author/assignedAuthor/assignedPerson/name/given[1]/text())");
    }

    public static void SetXml () throws XPathExpressionException, ParserConfigurationException, IOException, TransformerException, SAXException {
        //let iD = pm.environment.get("ID");
        //let setId = pm.environment.get("SETID");
        //let versionNumber = pm.environment.get("VN");
        //
        //let mo = pm.environment.get("MO");
        //let namemo = pm.environment.get("nameMO");
        //let guid = pm.environment.get("patientGuid");
        //let depart = pm.environment.get("Departmen");
        //let snils = pm.environment.get("Snils");

        // set("(//code[@codeSystem='1.2.643.5.1.13.13.11.1070'])[1]/@code", "TDU1.1.4");
        // getXml("(//code[@codeSystem='1.2.643.5.1.13.13.11.1070'])[1]/@code");

        set("/ClinicalDocument/id/@extension", "${iD}");
        set("/ClinicalDocument/id/@root", "${mo}.100.1.1.51");
        set("/ClinicalDocument/setId/@extension", "${setId}");
        set("/ClinicalDocument/versionNumber/@value", "${versionNumber}");
        set("/ClinicalDocument/recordTarget/patientRole/id[1]/@root", "${mo}.100.1.1.10");
        set("/ClinicalDocument/recordTarget/patientRole/id[1]/@extension", "${guid}");

        set("/ClinicalDocument/recordTarget/patientRole/id[2]/@extension", "${snils}");
        set("/ClinicalDocument/author/assignedAuthor/id[2]/@extension", "${snils}");
        set("/ClinicalDocument/legalAuthenticator/assignedEntity/id[2]/@extension", "${snils}");
        set("/ClinicalDocument/participant/associatedEntity/id[2]/@extension", "${snils}");
        set("/ClinicalDocument/documentationOf/serviceEvent/performer/assignedEntity/id[2]/@extension", "${snils}");
        set("/ClinicalDocument/component//section//entry//performer/assignedEntity/id[2]/@extension", "${snils}");
        set("/ClinicalDocument/authenticator/assignedEntity/id[2]/@extension", "${snils}");
        set("/ClinicalDocument/participant/associatedEntity/id[1]/@extension", "${snils}");

        set("/ClinicalDocument/author//representedOrganization/id/@root", "${mo}");
        set("/ClinicalDocument/author//representedOrganization/name/text()", "${namemo}");
        set("/ClinicalDocument/custodian//representedCustodianOrganization/id/@root", "${mo}");
        set("/ClinicalDocument/custodian//representedCustodianOrganization/name/text()", "${namemo}");
        //  getXml("/ClinicalDocument/component//section[title='ОБЩИЕ СВЕДЕНИЯ']/entry[1]//value/@code");
        // getXml("//code[@displayName='Номер медицинского свидетельства о рождении']/following-sibling::value/text()");
    }

    /**
     * Метод для замены текста в XML
     */
    public static void set(String path, String replace) throws ParserConfigurationException,
            IOException, SAXException, XPathExpressionException, TransformerException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(pathFile);

        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList nl = (NodeList) xpath.evaluate(path, doc, XPathConstants.NODESET);
        for (int i = 0; i < nl.getLength(); i++) {
            System.out.println(nl.item(i).getNodeValue());
            nl.item(i).setNodeValue(replace);
        }

        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(new DOMSource(doc), new StreamResult(new File(pathFile)));
    }

    /**
     * Метод для поиска текста в XML
     */
    public static String getXml(String path) throws ParserConfigurationException,
            IOException, SAXException, XPathExpressionException, TransformerException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(pathFile);

        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList nl = (NodeList) xpath.evaluate(path, doc, XPathConstants.NODESET);
        String str = null;
        for (int i = 0; i < nl.getLength(); i++) {
            System.out.println(nl.item(i).getNodeValue());
            str = nl.item(i).getNodeValue();
        }
        return str;
    }
}
