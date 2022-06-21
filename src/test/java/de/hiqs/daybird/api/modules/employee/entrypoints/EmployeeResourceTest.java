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
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
class EmployeeResourceTest extends AbstractMockWebServerTest {

    @Test
    void addEmployeeTest() {

        EmployeeRequestPostDto employeeRequestPostDto = EmployeeFactory.getEmployeeRequestPostDto();

        setMockResponse(EmployeeFactory.getFullDispatcher(true, true, 200, ""));

        EmployeePostDto employeePost = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .and().body(employeeRequestPostDto)
                .when().post(getBasePath())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(employeeRequestPostDto.getFirstName(), employeePost.getFirstName());
        Assertions.assertEquals(employeeRequestPostDto.getLastName(), employeePost.getLastName());
        Assertions.assertEquals(employeeRequestPostDto.getAddressUuid(), employeePost.getAddressUuid());
        Assertions.assertEquals(employeeRequestPostDto.getEmail(), employeePost.getEmail());
        Assertions.assertEquals(employeeRequestPostDto.getJobTitle(), employeePost.getJobTitle());
        Assertions.assertEquals(employeeRequestPostDto.getTargetHours(), employeePost.getTargetHours());
    }

    @Test
    void getEmployeeTest() {

        EmployeeRequestDto employeeRequestDto = EmployeeFactory.getEmployeeRequestDto();

        setMockResponse(EmployeeFactory.getFullDispatcher(true, true, 200, ""));

        EmployeeDto employeeGet = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .when().get(getBasePath() + "/" + employeeRequestDto.getUuid())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(employeeRequestDto.getUuid(), employeeGet.getUuid());
    }

    @Test
    void getEmployeeQueryTest() {

        EmployeeRequestDto employeeRequestDto1 = EmployeeFactory.getEmployeeRequestDto();
        employeeRequestDto1.setFirstName("Test1");

        EmployeeRequestDto employeeRequestDto2 = EmployeeFactory.getEmployeeRequestDto();
        employeeRequestDto2.setFirstName("Test2");

        setMockResponse(EmployeeFactory.getFullDispatcher(true, true, 200, ""));

        List<EmployeeDto> employeeQuery = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .queryParam(EmployeeRestClient.PARAM_FIRST_NAME, checkAndGetValueOrNull(employeeRequestDto1.getFirstName()))
                .queryParam(EmployeeRestClient.PARAM_LAST_NAME, checkAndGetValueOrNull(employeeRequestDto1.getLastName()))
                .queryParam(EmployeeRestClient.PARAM_EMAIL, checkAndGetValueOrNull(employeeRequestDto1.getEmail()))
                .queryParam(EmployeeRestClient.PARAM_ADDRESS_UUID, checkAndGetValueOrNull(employeeRequestDto1.getAddressUuid()))
                .queryParam(EmployeeRestClient.PARAM_JOB_TITLE, checkAndGetValueOrNull(employeeRequestDto1.getJobTitle()))
                .queryParam(EmployeeRestClient.PARAM_ARCHIVED, employeeRequestDto1.isArchived())
                .when().get(getBasePath())
                .then().extract().as(new TypeRef<>(){
                });

        Assertions.assertNotNull(employeeQuery);
        Assertions.assertEquals(2, employeeQuery.size());
    }

    @Test
    void deleteEmployeeTest() {

        EmployeeRequestDto employeeRequestDto = EmployeeFactory.getEmployeeRequestDto();

        setMockResponse(EmployeeFactory.getFullDispatcher(true, true, 204, ""));

        Response response = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .when().delete(getBasePath() + "/" + employeeRequestDto.getUuid())
                .then().extract().response();

        System.out.println("Delete Status: " + response.getStatusCode());
        Assertions.assertEquals(204, response.getStatusCode());
    }

   @Test
    void updateEmployeeTest() {

        EmployeeRequestDto employeeRequestDto =
                EmployeeFactory.getEmployeeRequestDto();

        EmployeeRequestUpdateDto employeeRequestUpdateDto =
                EmployeeFactory.getEmployeeUpdateRequestDto();

        setMockResponse(EmployeeFactory.getFullDispatcher(true, true, 200, ""));

        EmployeeDto updatedEmployee = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .and().body(employeeRequestUpdateDto)
                .when().put(getBasePath() + "/" + employeeRequestDto.getUuid())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(employeeRequestUpdateDto.getFirstName(), updatedEmployee.getFirstName());
        Assertions.assertEquals(employeeRequestUpdateDto.getLastName(), updatedEmployee.getLastName());
        Assertions.assertEquals(employeeRequestUpdateDto.getEmail(), updatedEmployee.getEmail());
        Assertions.assertEquals(employeeRequestUpdateDto.getJobTitle(), updatedEmployee.getJobTitle());
        Assertions.assertEquals(employeeRequestUpdateDto.getTargetHours(), updatedEmployee.getTargetHours());
    }

    @Test
    void archiveEmployeeTest() {

        String uuid = UUID.randomUUID().toString();

        EmployeeRequestUpdateDto employeeRequestUpdateDto = EmployeeFactory.getEmployeeUpdateRequestDto();

        setMockResponse(EmployeeFactory.getFullDispatcher(true, true, 200, ""));

        EmployeeDto employeeDtoResponse = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .queryParam(EmployeeRestClient.PARAM_ARCHIVED, false)
                .queryParam("entity_type", EntityTypeEnum.EMPLOYEE)
                .queryParam("recursive", false)
                .and()
                .when().put(getBasePath() + "/archive/" + uuid)
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertFalse(employeeDtoResponse.isArchived());
    }

    protected String getBasePath() {
        return EmployeeResource.PATH;
    }

    private static String checkAndGetValueOrNull(String value) {
        return StringUtils.isNotEmpty(value) ? value : "";
    }
}
