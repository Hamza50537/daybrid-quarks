package de.hiqs.daybird.api.modules.work_package.entrypoints;

import de.hiqs.daybird.api.modules.work_package.WorkPackageFactory;
import de.hiqs.daybird.api.modules.work_package.dataproviders.WorkPackageRestClient;
import de.hiqs.daybird.api.modules.work_package.dataproviders.models.WorkPackageRequestDto;
import de.hiqs.daybird.api.modules.work_package.dataproviders.models.WorkPackageRequestPostDto;
import de.hiqs.daybird.api.modules.work_package.dataproviders.models.WorkPackageRequestUpdateDto;
import de.hiqs.daybird.api.modules.work_package.entrypoints.models.WorkPackageDto;
import de.hiqs.daybird.api.modules.work_package.entrypoints.models.WorkPackagePostDto;
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

import java.time.LocalDate;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
class WorkPackageRoleTest extends AbstractMockWebServerTest {

    @ParameterizedTest
    @CsvSource({"user, 123, 401"})
    void addWorkPackageNoAuthTest(String username, String password, int statusCode) {
        RestAssured.defaultParser = Parser.JSON;
        WorkPackageRequestPostDto workPackageRequestPostDto = WorkPackageFactory.getWorkPackageRequestPostDto();

        given()
                .auth().oauth2(getAccessToken(username, password) + "1")
                .header("Content-type", "application/json")
                .and().body(workPackageRequestPostDto)
                .when().post(getBasePath())
                .then().assertThat().statusCode(statusCode);
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void addWorkPackageTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        WorkPackageRequestPostDto workPackageRequestPostDto = WorkPackageFactory.getWorkPackageRequestPostDto();

        setMockResponse(WorkPackageFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        WorkPackagePostDto workPackagePost = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .and().body(workPackageRequestPostDto)
                .when().post(getBasePath())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(workPackageRequestPostDto.getName(), workPackagePost.getName());
            Assertions.assertEquals(workPackageRequestPostDto.getSign(), workPackagePost.getSign());
            Assertions.assertEquals(workPackageRequestPostDto.getStartDate(), workPackagePost.getStartDate());
            Assertions.assertEquals(workPackageRequestPostDto.getEndDate(), workPackagePost.getEndDate());
            Assertions.assertEquals(workPackageRequestPostDto.getProjectUuid(), workPackagePost.getProjectUuid());
            Assertions.assertEquals(workPackageRequestPostDto.getEmployeeUuid(), workPackagePost.getEmployeeUuid());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 200, 200"})
    void getWorkPackageTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        WorkPackageRequestDto workPackageRequestDto = WorkPackageFactory.getWorkPackageRequestDto();

        setMockResponse(WorkPackageFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        WorkPackageDto workPackageGet = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .when().get(getBasePath() + "/" + workPackageRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(workPackageRequestDto.getUuid(), workPackageGet.getUuid());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void getWorkPackageQueryTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        WorkPackageRequestDto workPackageRequestDto1 = WorkPackageFactory.getWorkPackageRequestDto();
        workPackageRequestDto1.setName("Test1");

        WorkPackageRequestDto workPackageRequestDto2 = WorkPackageFactory.getWorkPackageRequestDto();
        workPackageRequestDto2.setName("Test2");

        setMockResponse(WorkPackageFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        String workPackageQuery = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-Type", "application/json")
                .queryParam(WorkPackageRestClient.PATH_NAME, checkAndGetValueOrNull(workPackageRequestDto1.getName()))
                .queryParam(WorkPackageRestClient.PATH_SIGN, checkAndGetValueOrNull(workPackageRequestDto1.getSign()))
                .queryParam(WorkPackageRestClient.PATH_START_DATE, checkAndGetValueOrNull(workPackageRequestDto1.getStartDate()))
                .queryParam(WorkPackageRestClient.PATH_END_DATE, checkAndGetValueOrNull(workPackageRequestDto1.getEndDate()))
                .queryParam(WorkPackageRestClient.PARAM_PROJECT_UUID, checkAndGetValueOrNull(workPackageRequestDto1.getProjectUuid()))
                .queryParam(WorkPackageRestClient.PARAM_EMPLOYEE_UUID, checkAndGetValueOrNull(workPackageRequestDto1.getEmployeeUuid()))
                .queryParam(WorkPackageRestClient.PARAM_ARCHIVED, workPackageRequestDto1.isArchived())
                .when().get(getBasePath())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().asPrettyString();

        System.out.println(workPackageQuery);
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 204, 204", "controller, 123, 204, 204", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void deleteWorkPackageTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        WorkPackageRequestDto workPackageRequestDto = WorkPackageFactory.getWorkPackageRequestDto();

        setMockResponse(WorkPackageFactory.getFullDispatcher(true, statusCodeRequest == 204, statusCodeRequest, ""));

        Response response = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-Type", "application/json")
                .when().delete(getBasePath() + "/" + workPackageRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().response();

        if (statusCodeResponse == 204) {
            System.out.println("Delete Status: " + response.getStatusCode());
            Assertions.assertEquals(204, response.getStatusCode());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void updateWorkPackageTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        WorkPackageRequestDto workPackageRequestDto =
                WorkPackageFactory.getWorkPackageRequestDto();

        WorkPackageRequestUpdateDto workPackageRequestUpdateDto =
                WorkPackageFactory.getWorkPackageUpdateRequestDto();

        setMockResponse(WorkPackageFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        WorkPackageDto updatedWorkPackage = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .and().body(workPackageRequestUpdateDto)
                .when().put(getBasePath() + "/" + workPackageRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(workPackageRequestUpdateDto.getName(), updatedWorkPackage.getName());
            Assertions.assertEquals(workPackageRequestUpdateDto.getSign(), updatedWorkPackage.getSign());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void archiveWorkPackageTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        String uuid = UUID.randomUUID().toString();

        WorkPackageRequestUpdateDto workPackageRequestUpdateDto = WorkPackageFactory.getWorkPackageUpdateRequestDto();

        setMockResponse(WorkPackageFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        WorkPackageDto workPackageDtoResponse = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .queryParam(WorkPackageRestClient.PARAM_ARCHIVED, false)
                .queryParam("entity_type", EntityTypeEnum.WORKPACKAGE)
                .queryParam("recursive", false)
                .and()
                .when().put(getBasePath() + "/archive/" + uuid)
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertFalse(workPackageDtoResponse.isArchived());
        }
    }

    protected String getBasePath() {
        return WorkPackageResource.PATH;
    }

    private static String checkAndGetValueOrNull(String value) {
        return StringUtils.isNotEmpty(value) ? value : "";
    }

    private static String checkAndGetValueOrNull(LocalDate value) {
        return value != null ? value.toString() : "";
    }
}
