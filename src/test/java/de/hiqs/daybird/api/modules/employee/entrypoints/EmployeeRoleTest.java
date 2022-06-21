package de.hiqs.daybird.api.modules.employee.entrypoints;

import de.hiqs.daybird.api.modules.employee.EmployeeFactory;
import de.hiqs.daybird.api.modules.employee.dataproviders.EmployeeRestClient;
import de.hiqs.daybird.api.modules.employee.dataproviders.models.EmployeeRequestDto;
import de.hiqs.daybird.api.modules.employee.dataproviders.models.EmployeeRequestPostDto;
import de.hiqs.daybird.api.modules.employee.dataproviders.models.EmployeeRequestUpdateDto;
import de.hiqs.daybird.api.modules.employee.entrypoints.models.EmployeeDto;
import de.hiqs.daybird.api.modules.employee.entrypoints.models.EmployeePostDto;
import de.hiqs.daybird.api.shared.AbstractMockWebServerTest;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
class EmployeeRoleTest extends AbstractMockWebServerTest {

    @ParameterizedTest
    @CsvSource({"user, 123, 401"})
    void addEmployeeNoAuthTest(String username, String password, int statusCode) {
        RestAssured.defaultParser = Parser.JSON;
        EmployeeRequestPostDto employeeRequestPostDto = EmployeeFactory.getEmployeeRequestPostDto();

        given()
                .auth().oauth2(getAccessToken(username, password) + "1")
                .header("Content-type", "application/json")
                .and().body(employeeRequestPostDto)
                .when().post(getBasePath())
                .then().assertThat().statusCode(statusCode);
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 200, 200", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void addEmployeeTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        EmployeeRequestPostDto employeeRequestPostDto = EmployeeFactory.getEmployeeRequestPostDto();

        setMockResponse(EmployeeFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        EmployeePostDto employeePost = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .and().body(employeeRequestPostDto)
                .when().post(getBasePath())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(employeeRequestPostDto.getFirstName(), employeePost.getFirstName());
            Assertions.assertEquals(employeeRequestPostDto.getLastName(), employeePost.getLastName());
            Assertions.assertEquals(employeeRequestPostDto.getAddressUuid(), employeePost.getAddressUuid());
            Assertions.assertEquals(employeeRequestPostDto.getEmail(), employeePost.getEmail());
            Assertions.assertEquals(employeeRequestPostDto.getJobTitle(), employeePost.getJobTitle());
            Assertions.assertEquals(employeeRequestPostDto.getTargetHours(), employeePost.getTargetHours());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 200, 200", "user, 123, 200, 200"})
    void getEmployeeTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        EmployeeRequestDto employeeRequestDto = EmployeeFactory.getEmployeeRequestDto();

        setMockResponse(EmployeeFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        EmployeeDto employeeGet = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .when().get(getBasePath() + "/" + employeeRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(employeeRequestDto.getUuid(), employeeGet.getUuid());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 200, 200", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void getEmployeeQueryTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        EmployeeRequestDto employeeRequestDto1 = EmployeeFactory.getEmployeeRequestDto();
        employeeRequestDto1.setFirstName("Test1");

        EmployeeRequestDto employeeRequestDto2 = EmployeeFactory.getEmployeeRequestDto();
        employeeRequestDto2.setFirstName("Test2");

        setMockResponse(EmployeeFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        String employeeQuery = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .queryParam(EmployeeRestClient.PARAM_FIRST_NAME, checkAndGetValueOrNull(employeeRequestDto1.getFirstName()))
                .queryParam(EmployeeRestClient.PARAM_LAST_NAME, checkAndGetValueOrNull(employeeRequestDto1.getLastName()))
                .queryParam(EmployeeRestClient.PARAM_EMAIL, checkAndGetValueOrNull(employeeRequestDto1.getEmail()))
                .queryParam(EmployeeRestClient.PARAM_ADDRESS_UUID, checkAndGetValueOrNull(employeeRequestDto1.getAddressUuid()))
                .queryParam(EmployeeRestClient.PARAM_JOB_TITLE, checkAndGetValueOrNull(employeeRequestDto1.getJobTitle()))
                .queryParam(EmployeeRestClient.PARAM_ARCHIVED, employeeRequestDto1.isArchived())
                .when().get(getBasePath())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().asPrettyString();

        System.out.println(employeeQuery);
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 204, 204", "controller, 123, 403, 500", "hr, 123, 204, 204", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void deleteEmployeeTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        EmployeeRequestDto employeeRequestDto = EmployeeFactory.getEmployeeRequestDto();

        setMockResponse(EmployeeFactory.getFullDispatcher(true, statusCodeRequest == 204, statusCodeRequest, ""));

        Response response = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .when().delete(getBasePath() + "/" + employeeRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().response();

        if (statusCodeResponse == 204) {
            System.out.println("Delete Status: " + response.getStatusCode());
            Assertions.assertEquals(204, response.getStatusCode());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 200, 200"})
    void updateEmployeeTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        EmployeeRequestDto employeeRequestDto =
                EmployeeFactory.getEmployeeRequestDto();

        EmployeeRequestUpdateDto employeeRequestUpdateDto =
                EmployeeFactory.getEmployeeUpdateRequestDto();

        setMockResponse(EmployeeFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        EmployeeDto updatedEmployee = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .and().body(employeeRequestUpdateDto)
                .when().put(getBasePath() + "/" + employeeRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(employeeRequestUpdateDto.getFirstName(), updatedEmployee.getFirstName());
            Assertions.assertEquals(employeeRequestUpdateDto.getLastName(), updatedEmployee.getLastName());
            Assertions.assertEquals(employeeRequestUpdateDto.getEmail(), updatedEmployee.getEmail());
            Assertions.assertEquals(employeeRequestUpdateDto.getJobTitle(), updatedEmployee.getJobTitle());
            Assertions.assertEquals(employeeRequestUpdateDto.getTargetHours(), updatedEmployee.getTargetHours());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 200, 200", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void archiveEmployeeTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        String uuid = UUID.randomUUID().toString();

        EmployeeRequestUpdateDto employeeRequestUpdateDto = EmployeeFactory.getEmployeeUpdateRequestDto();

        setMockResponse(EmployeeFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        EmployeeDto employeeDtoResponse = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .queryParam(EmployeeRestClient.PARAM_ARCHIVED, false)
                .queryParam("entity_type", EntityTypeEnum.EMPLOYEE)
                .queryParam("recursive", false)
                .and()
                .when().put(getBasePath() + "/archive/" + uuid)
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertFalse(employeeDtoResponse.isArchived());
        }
    }

    protected String getBasePath() {
        return EmployeeResource.PATH;
    }

    private static String checkAndGetValueOrNull(String value) {
        return StringUtils.isNotEmpty(value) ? value : "";
    }
}
