package api.Access_Notification;

import Base.BaseAPI;

import java.util.UUID;

public class Document_667 extends BaseAPI {
    public UUID uuid = UUID.randomUUID();
    public String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<SOAP-ENV:Envelope\n" +
            "\txmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "\t<SOAP-ENV:Header>\n" +
            "\t\t<To\n" +
            "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\" wsu:Id=\"id-to\"\n" +
            "\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">https://ips-test.rosminzdrav.ru/b6789b2b47581\n" +
            "\t\t</To>\n" +
            "\t\t<RelatesTo\n" +
            "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\">" + uuid + "\n" +
            "\t\t</RelatesTo>\n" +
            "\t\t<Action\n" +
            "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\" wsu:Id=\"id-action\"\n" +
            "\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">sendResult\n" +
            "\t\t</Action>\n" +
            "\t\t<MessageID\n" +
            "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\" wsu:Id=\"id-messageid\"\n" +
            "\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">9dd49f16-3aa2-45e7-b786-1333f341fbd2\n" +
            "\t\t</MessageID>\n" +
            "\t\t<ReplyTo\n" +
            "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\" wsu:Id=\"id-replyto\"\n" +
            "\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
            "\t\t\t<Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</Address>\n" +
            "\t\t</ReplyTo>\n" +
            "\t\t<transportHeader\n" +
            "\t\t\txmlns=\"http://egisz.rosminzdrav.ru\">\n" +
            "\t\t\t<authInfo>\n" +
            "\t\t\t\t<clientEntityId>e5daa377-b705-64e2-52c4-3ddebea35d7b</clientEntityId>\n" +
            "\t\t\t</authInfo>\n" +
            "\t\t</transportHeader>\n" +
            "\t\t<wsse:Security\n" +
            "\t\t\txmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n" +
            "\t\t\t<wsse:BinarySecurityToken wsu:Id=\"id-x509\" EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\"\n" +
            "\t\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">MIIDazCCAlOgAwIBAgIUEch0BCakzmHyN1a7EoVrgAlaA5kwDQYJKoZIhvcNAQELBQAwRTELMAkGA1UEBhMCQVUxEzARBgNVBAgMClNvbWUtU3RhdGUxITAfBgNVBAoMGEludGVybmV0IFdpZGdpdHMgUHR5IEx0ZDAeFw0yMDA0MTAxODQ4NTlaFw0yMTA0MTAxODQ4NTlaMEUxCzAJBgNVBAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRlcm5ldCBXaWRnaXRzIFB0eSBMdGQwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCrJmNObcyjysnS5dV/Hvp6RzHZyoN+n4fxX0QhiMMU4tBPh1zjaMDSe8Cw4i+3B91P33I2vyI0vfvX8EqWumDiDdV0eWc9h+3EeaG9uozqfpMjY/gnyPntwReTqsWZjPy2yyZgRN+IHdvpCfuC+tv8G0H9/UK31RbIUI7P1zeMCZ68v9LVgbG7r5oa0QZId4p/LQ4xPtjvuWiObEwFAUfW6b/vbDfGXSZ4Tq63UwyD9DzXZcs3f7w5EjHmEU22LU8pEzRqjsv/VraQd7T2OrYKbnOP4EAzZIQwuk3OcIVJFtNBjj0+F5gJ+BOmCY0UYcH4oaGjVA5ez147E46oM5AhAgMBAAGjUzBRMB0GA1UdDgQWBBTiuUZkT5Q9j0wfM566jk5aHldSYTAfBgNVHSMEGDAWgBTiuUZkT5Q9j0wfM566jk5aHldSYTAPBgNVHRMBAf8EBTADAQH/MA0GCSqGSIb3DQEBCwUAA4IBAQAM5pQpQMcuE2unZTMLzlfXW5xY3J8mfdgHVZGnBC6fLKfwmlpyoleqnTuNik8X/REe2hN2heO/irHllBAGt+TUB8fdRJpaGbdi6H5FuzrCoG7rozJVZp70B3ij9XuBqkjBmZvac4QcWyXmBYyuWG0TqxKpRwwtgZNr5ZApk2PB79sTlaI7+sLyp/G3ntA4wwQ/4785Sx/8F90/Mrg5fwVi5sQz+Id6ykRV5IZnrE/Z47KdcJ/U4rePGHV01XECn1uR2X98e3npPzR1MX4hoo/f4iZuJ1KWYZ66u1z0edW1E8BFUAsuEe5Z5dytncpodvRsIaICCcXE6do2dYQqpJi4\n" +
            "\t\t\t</wsse:BinarySecurityToken>\n" +
            "\t\t\t<Signature\n" +
            "\t\t\t\txmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
            "\t\t\t\t<SignedInfo>\n" +
            "\t\t\t\t\t<CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
            "\t\t\t\t\t<SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\" />\n" +
            "\t\t\t\t\t<Reference URI=\"#body\">\n" +
            "\t\t\t\t\t\t<Transforms>\n" +
            "\t\t\t\t\t\t\t<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
            "\t\t\t\t\t\t</Transforms>\n" +
            "\t\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\" />\n" +
            "\t\t\t\t\t\t<DigestValue>YEOGiS0cujt2sVo0moV4VS7mBVuS0Pl8sQeaHkDrqa0=</DigestValue>\n" +
            "\t\t\t\t\t</Reference>\n" +
            "\t\t\t\t\t<Reference URI=\"#id-messageid\">\n" +
            "\t\t\t\t\t\t<Transforms>\n" +
            "\t\t\t\t\t\t\t<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
            "\t\t\t\t\t\t</Transforms>\n" +
            "\t\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\" />\n" +
            "\t\t\t\t\t\t<DigestValue>j8myuFMZfVE+B7gUZ0jf51ymcwKUgV2qwVN0BAw+INA=</DigestValue>\n" +
            "\t\t\t\t\t</Reference>\n" +
            "\t\t\t\t\t<Reference URI=\"#id-replyto\">\n" +
            "\t\t\t\t\t\t<Transforms>\n" +
            "\t\t\t\t\t\t\t<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
            "\t\t\t\t\t\t</Transforms>\n" +
            "\t\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\" />\n" +
            "\t\t\t\t\t\t<DigestValue>tpA2zvq1rD20AP46VFS6MUOE9Noe/s3zfGq3XeC9vPc=</DigestValue>\n" +
            "\t\t\t\t\t</Reference>\n" +
            "\t\t\t\t\t<Reference URI=\"#id-to\">\n" +
            "\t\t\t\t\t\t<Transforms>\n" +
            "\t\t\t\t\t\t\t<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
            "\t\t\t\t\t\t</Transforms>\n" +
            "\t\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\" />\n" +
            "\t\t\t\t\t\t<DigestValue>5qMzs7IC+8NoRSv4m3hf+XXJW3Wy7BkaQwFsPpL9744=</DigestValue>\n" +
            "\t\t\t\t\t</Reference>\n" +
            "\t\t\t\t\t<Reference URI=\"#id-action\">\n" +
            "\t\t\t\t\t\t<Transforms>\n" +
            "\t\t\t\t\t\t\t<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
            "\t\t\t\t\t\t</Transforms>\n" +
            "\t\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\" />\n" +
            "\t\t\t\t\t\t<DigestValue>4x0l+FI91mxd0FRM/0pitJuXGT+JtVgHi1xmLKuJDzk=</DigestValue>\n" +
            "\t\t\t\t\t</Reference>\n" +
            "\t\t\t\t</SignedInfo>\n" +
            "\t\t\t\t<SignatureValue>YZ2C7Q1DbmTgm+9BSd73WEThN/er2wd/TQY/qnlKG3+MuztFAJIUhv0htOJte9imifNMtrqE60eJ4Mq7dq0p0tda0cEpMYG6YyIUMkEwRwkrYejiwyGtORoeK9GwRCV1JiUwqP2tucRDbqrHG8cg6/xCGSaMU4rXTiKIj5x+aKvlsVhPpuSNMs/tf8VJ09XtJgd5kyTlxwPxjxH1PIFUanLAJwSEv/4OoWzmqxl1KdEA0ihgdK8x/V5MBBPvdiSkTYdp6LmsdC1005VnFwG29VwW+0X1wxAvVE0ac6kyVQjYGgJWhYR+VsFHdYBV+drY0+rJWM6q0ehgymiLqoT5yw==</SignatureValue>\n" +
            "\t\t\t\t<KeyInfo>\n" +
            "\t\t\t\t\t<wsse:SecurityTokenReference>\n" +
            "\t\t\t\t\t\t<wsse:Reference ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" URI=\"#id-x509\" />\n" +
            "\t\t\t\t\t</wsse:SecurityTokenReference>\n" +
            "\t\t\t\t</KeyInfo>\n" +
            "\t\t\t</Signature>\n" +
            "\t\t</wsse:Security>\n" +
            "\t</SOAP-ENV:Header>\n" +
            "\t<SOAP-ENV:Body wsu:Id=\"body\"\n" +
            "\t\txmlns:p2=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"\n" +
            "\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
            "\t\t<sendResult\n" +
            "\t\t\txmlns=\"http://callback.mis.vimis.rosminzdrav.ru/\">\n" +
            "\t\t\t<msg_id\n" +
            "\t\t\t\txmlns=\"\">" + uuid + "\n" +
            "\t\t\t</msg_id>\n" +
            "\t\t\t<status\n" +
            "\t\t\t\txmlns=\"\">1</status>\n" +
            "\t\t\t<description\n" +
            "\t\t\t\txmlns=\"\">Проверяем отправку уведомлений</description>\n" +
            "\t\t</sendResult>\n" +
            "\t</SOAP-ENV:Body>\n" +
            "</SOAP-ENV:Envelope>";
}
