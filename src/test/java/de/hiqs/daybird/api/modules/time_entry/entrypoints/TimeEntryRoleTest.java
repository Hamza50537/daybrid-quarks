package de.hiqs.daybird.api.modules.time_entry.entrypoints;

import de.hiqs.daybird.api.modules.time_entry.TimeEntryFactory;
import de.hiqs.daybird.api.modules.time_entry.dataproviders.TimeEntryRestClient;
import de.hiqs.daybird.api.modules.time_entry.dataproviders.models.TimeEntryRequestDto;
import de.hiqs.daybird.api.modules.time_entry.dataproviders.models.TimeEntryRequestPostDto;
import de.hiqs.daybird.api.modules.time_entry.dataproviders.models.TimeEntryRequestUpdateDto;
import de.hiqs.daybird.api.modules.time_entry.entrypoints.models.TimeEntryDto;
import de.hiqs.daybird.api.modules.time_entry.entrypoints.models.TimeEntryPostDto;
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
class TimeEntryRoleTest extends AbstractMockWebServerTest {

    @ParameterizedTest
    @CsvSource({"user, 123, 401"})
    void addTimeEntryNoAuthTest(String username, String password, int statusCode) {
        RestAssured.defaultParser = Parser.JSON;
        TimeEntryRequestPostDto timeEntryRequestPostDto = TimeEntryFactory.getTimeEntryRequestPostDto();

        given()
                .auth().oauth2(getAccessToken(username, password) + "1")
                .header("Content-type", "application/json")
                .and().body(timeEntryRequestPostDto)
                .when().post(getBasePath())
                .then().assertThat().statusCode(statusCode);
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 200, 200"})
    void addTimeEntryTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        TimeEntryRequestPostDto timeEntryRequestPostDto = TimeEntryFactory.getTimeEntryRequestPostDto();

        setMockResponse(TimeEntryFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        TimeEntryPostDto timeEntryPost = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-Type", "application/json")
                .and().body(timeEntryRequestPostDto)
                .when().post(getBasePath())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(timeEntryRequestPostDto.getName(), timeEntryPost.getName());
            Assertions.assertEquals(timeEntryRequestPostDto.getDate(), timeEntryPost.getDate());
            Assertions.assertEquals(timeEntryRequestPostDto.getDescription(), timeEntryPost.getDescription());
            Assertions.assertEquals(timeEntryRequestPostDto.getWorkPackageUuid(), timeEntryPost.getWorkPackageUuid());
            Assertions.assertEquals(timeEntryRequestPostDto.getEmployeeUuid(), timeEntryPost.getEmployeeUuid());
            Assertions.assertEquals(timeEntryRequestPostDto.getTimeEntryTypeUuid(), timeEntryPost.getTimeEntryTypeUuid());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 200, 200", "user, 123, 200, 200"})
    void getTimeEntryTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        TimeEntryRequestDto timeEntryRequestDto = TimeEntryFactory.getTimeEntryRequestDto();

        setMockResponse(TimeEntryFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        TimeEntryDto timeEntryGet = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .when().get(getBasePath() + "/" + timeEntryRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(timeEntryRequestDto.getUuid(), timeEntryGet.getUuid());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 200, 200", "user, 123, 200, 200"})
    void getTimeEntryQueryTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        TimeEntryRequestDto timeEntryRequestDto1 = TimeEntryFactory.getTimeEntryRequestDto();
        timeEntryRequestDto1.setName("Test1");

        TimeEntryRequestDto timeEntryRequestDto2 = TimeEntryFactory.getTimeEntryRequestDto();
        timeEntryRequestDto2.setName("Test2");

        setMockResponse(TimeEntryFactory.getFullDispatcher(true, statusCodeRequest==200, statusCodeRequest, ""));

        String timeEntryQuery = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-Type", "application/json")
                .queryParam(TimeEntryRestClient.PARAM_NAME, checkAndGetValueOrNull(timeEntryRequestDto1.getName()))
                .queryParam(TimeEntryRestClient.PARAM_DATE_FROM, checkAndGetValueOrNull(timeEntryRequestDto1.getDate()))
                .queryParam(TimeEntryRestClient.PARAM_DATE_TO, checkAndGetValueOrNull(timeEntryRequestDto1.getDate()))
                .queryParam(TimeEntryRestClient.PARAM_DATE, checkAndGetValueOrNull(timeEntryRequestDto1.getDate()))
                .queryParam(TimeEntryRestClient.PARAM_WORKPACKAGE_UUID, checkAndGetValueOrNull(timeEntryRequestDto1.getWorkPackageUuid()))
                .queryParam(TimeEntryRestClient.PARAM_TIME_ENTRY_TYPE_UUID, checkAndGetValueOrNull(timeEntryRequestDto1.getTimeEntryTypeUuid()))
                .queryParam(TimeEntryRestClient.PARAM_EMPLOYEE_UUID, checkAndGetValueOrNull(timeEntryRequestDto1.getEmployeeUuid()))
                .queryParam(TimeEntryRestClient.PARAM_ARCHIVED, timeEntryRequestDto1.isArchived())
                .when().get(getBasePath())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().asPrettyString();

        System.out.println(timeEntryQuery);
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 204, 204", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 204, 204"})
    void deleteTimeEntryTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        TimeEntryRequestDto timeEntryRequestDto = TimeEntryFactory.getTimeEntryRequestDto();

        setMockResponse(TimeEntryFactory.getFullDispatcher(true, statusCodeRequest == 204, statusCodeRequest, ""));

        Response response = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-Type", "application/json")
                .when().delete(getBasePath() + "/" + timeEntryRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().response();

        if (statusCodeResponse == 204) {
            System.out.println("Delete Status: " + response.getStatusCode());
            Assertions.assertEquals(204, response.getStatusCode());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 200, 200", "user, 123, 200, 200"})
    void updateTimeEntryTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        TimeEntryRequestDto timeEntryRequestDto =
                TimeEntryFactory.getTimeEntryRequestDto();

        TimeEntryRequestUpdateDto timeEntryRequestUpdateDto =
                TimeEntryFactory.getTimeEntryUpdateRequestDto();

        setMockResponse(TimeEntryFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        TimeEntryDto updatedTimeEntry = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .and().body(timeEntryRequestUpdateDto)
                .when().put(getBasePath() + "/" + timeEntryRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(timeEntryRequestUpdateDto.getName(), updatedTimeEntry.getName());
            Assertions.assertEquals(timeEntryRequestUpdateDto.getDescription(), updatedTimeEntry.getDescription());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 200, 200", "user, 123, 200, 200"})
    void archiveTimeEntryTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        String uuid = UUID.randomUUID().toString();

        TimeEntryRequestUpdateDto timeEntryRequestUpdateDto = TimeEntryFactory.getTimeEntryUpdateRequestDto();

        setMockResponse(TimeEntryFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        TimeEntryDto timeEntryDtoResponse = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .queryParam(TimeEntryRestClient.PARAM_ARCHIVED, false)
                .queryParam("entity_type", EntityTypeEnum.TIMEENTRY)
                .queryParam("recursive", false)
                .and()
                .when().put(getBasePath() + "/archive/" + uuid)
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertFalse(timeEntryDtoResponse.isArchived());
        }
    }

    protected String getBasePath() {
        return TimeEntryResource.PATH;
    }

    private static String checkAndGetValueOrNull(String value) {
        return StringUtils.isNotEmpty(value) ? value : "";
    }

    private static String checkAndGetValueOrNull(LocalDate value) {
        return value != null ? value.toString() : "";
    }
}
