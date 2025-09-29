package UI.TmTest.BaseTest;

import Base.BaseAPI;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.sql.SQLException;

abstract public class BaseNew extends BaseAPI {

    @BeforeEach
    public void initSpecNew () throws IOException, SQLException {
        ParametersAdd();
        if (KingNumber == 13) {
            GetEquipmentRequest("X-OMAT", POidMoTarget);
            GetEquipmentTarget("КТР", POidMoTarget);
        }
    }

    @Step("Метод установки нужных параметров")
    public static void ParametersAdd () {
        if (KingNumber == 13) {
            PName = "Тестировщик Тест Тестович";
            PNameGlobal = "Тест";
            PLastNameGlobal = "Тестировщик";
            PMiddleNameGlobal = "Тестович";
            PSnils = "159-790-257 20";
            PSnils_ = "15979025720";
            PId = "5885";
            PRole = "Тестировщик";
            PGuid = "e3c3323e-1e05-4f59-b733-9abe7dfc88ce";

            PMORequest = "БУ ХМАО-Югры \"Окружная клиническая больница\"";
            POidMoRequest = "1.2.643.5.1.13.13.12.2.86.8902";

            PMOTarget = "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"";
            POidMoTarget = "1.2.643.5.1.13.13.12.2.86.9003";
        }

        PNameGlobal_FIO = "Пользователь Для Тестов";
        PNameGlobal_Rezerv = "Для";
        PLastNameGlobal_Rezerv = "Пользователь";
        PMiddleNameGlobal_Rezerv = "Тестов";
        PSnils_Rezerv = "849-041-434 11";
        PNameRezerv_Id = "23165";

        PMOTarge2 = "БУ ХМАО-Югры \"Белоярская районная больница\"";
        PIdMoTarget2 = "54";
        POidMoTarget2 = "1.2.643.5.1.13.13.12.2.86.8986";
    }

    @Step("Метод для поиска данных для оборудования из RequestMO")
    public void GetEquipmentRequest (String nameEquipment, String mooid) throws SQLException {
        sql.StartConnection(
                "select * from telmed.equipmentregistry e where model = '" + nameEquipment + "' and isavailableforothermo = 1 and mooid = '" + mooid + "';");
        while (sql.resultSet.next()) {
            PidEquipment = sql.resultSet.getString("id");
        }

        sql.StartConnection(
                "SELECT * FROM dpc.mis_sp_mu  WHERE namemu = '" + mooid + "'");
        while (sql.resultSet.next()) {
            PIdMoRequest = sql.resultSet.getString("idmu");
        }

        PNameEquipment = nameEquipment;

        System.out.println("RequestMO");
        System.out.println("PidEquipment - " + PidEquipment);
        System.out.println("PIdMoRequest - " + PIdMoRequest);
        System.out.println("PNameEquipment - " + PNameEquipment);
    }

    @Step("Метод для поиска данных для оборудования из TargetMo")
    public void GetEquipmentTarget (String nameEquipment, String mooid) throws SQLException {
        sql.StartConnection(
                "select * from telmed.equipmentregistry e where model = '" + nameEquipment + "' and isavailableforothermo = 1 and mooid = '" + mooid + "';");
        while (sql.resultSet.next()) {
            PidEquipment2 = sql.resultSet.getString("id");
        }

        sql.StartConnection(
                "SELECT * FROM dpc.mis_sp_mu  WHERE namemu = '" + mooid + "'");
        while (sql.resultSet.next()) {
            PIdMoTarget = sql.resultSet.getString("idmu");
        }

        PNameEquipment2 = nameEquipment;

        System.out.println("TargetMo");
        System.out.println("PidEquipment2 - " + PidEquipment);
        System.out.println("PIdMoTarget - " + PIdMoRequest);
        System.out.println("PNameEquipment2 - " + PNameEquipment);
    }
}
