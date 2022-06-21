package de.hiqs.daybird.api.modules.time_entry_type.entrypoints;

import de.hiqs.daybird.api.modules.time_entry_type.TimeEntryTypeFactory;
import de.hiqs.daybird.api.modules.time_entry_type.dataproviders.TimeEntryTypeRestClient;
import de.hiqs.daybird.api.modules.time_entry_type.dataproviders.models.TimeEntryTypeRequestDto;
import de.hiqs.daybird.api.modules.time_entry_type.dataproviders.models.TimeEntryTypeRequestPostDto;
import de.hiqs.daybird.api.modules.time_entry_type.dataproviders.models.TimeEntryTypeRequestUpdateDto;
import de.hiqs.daybird.api.modules.time_entry_type.entrypoints.models.TimeEntryTypeDto;
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
class TimeEntryTypeRoleTest extends AbstractMockWebServerTest {

    @ParameterizedTest
    @CsvSource({"user, 123, 401"})
    void addTimeEntryTypeNoAuthTest(String username, String password, int statusCode) {
        RestAssured.defaultParser = Parser.JSON;
        TimeEntryTypeRequestPostDto timeEntryTypeRequestPostDto = TimeEntryTypeFactory.getTimeEntryTypeRequestPostDto();

        given()
                .auth().oauth2(getAccessToken(username, password) + "1")
                .header("Content-type", "application/json")
                .and().body(timeEntryTypeRequestPostDto)
                .when().post(getBasePath())
                .then().assertThat().statusCode(statusCode);
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void addTimeEntryTypeTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        TimeEntryTypeRequestPostDto timeEntryTypeRequestPostDto = TimeEntryTypeFactory.getTimeEntryTypeRequestPostDto();

        setMockResponse(TimeEntryTypeFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        TimeEntryTypeDto timeEntryTypePost = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-Type", "application/json")
                .and().body(timeEntryTypeRequestPostDto)
                .when().post(getBasePath())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(timeEntryTypeRequestPostDto.getTypeName(), timeEntryTypePost.getTypeName());
            Assertions.assertEquals(timeEntryTypeRequestPostDto.isName(), timeEntryTypePost.isName());
            Assertions.assertEquals(timeEntryTypeRequestPostDto.isStartTime(), timeEntryTypePost.isStartTime());
            Assertions.assertEquals(timeEntryTypeRequestPostDto.isEndTime(), timeEntryTypePost.isEndTime());
            Assertions.assertEquals(timeEntryTypeRequestPostDto.isDate(), timeEntryTypePost.isDate());
            Assertions.assertEquals(timeEntryTypeRequestPostDto.isDescription(), timeEntryTypePost.isDescription());
            Assertions.assertEquals(timeEntryTypeRequestPostDto.isWorkPackageUuid(), timeEntryTypePost.isWorkPackageUuid());
            Assertions.assertEquals(timeEntryTypeRequestPostDto.isEmployeeUuid(), timeEntryTypePost.isEmployeeUuid());
        }

    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void getTimeEntryTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        TimeEntryTypeRequestDto timeEntryTypeRequestDto = TimeEntryTypeFactory.getTimeEntryTypeRequestDto();

        setMockResponse(TimeEntryTypeFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        TimeEntryTypeDto timeEntryGet = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .when().get(getBasePath() + "/" + timeEntryTypeRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(timeEntryTypeRequestDto.getUuid(), timeEntryGet.getUuid());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void getTimeEntryQueryTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        TimeEntryTypeRequestDto timeEntryTypeRequestDto1 = TimeEntryTypeFactory.getTimeEntryTypeRequestDto();
        timeEntryTypeRequestDto1.setTypeName("Test1");

        TimeEntryTypeRequestDto timeEntryTypeRequestDto2 = TimeEntryTypeFactory.getTimeEntryTypeRequestDto();
        timeEntryTypeRequestDto2.setTypeName("Test2");

        setMockResponse(TimeEntryTypeFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        String timeEntryTypeQuery = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-Type", "application/json")
                .queryParam(TimeEntryTypeRestClient.PARAM_TYPE_NAME, checkAndGetValueOrNull(timeEntryTypeRequestDto1.getTypeName()))
                .queryParam(TimeEntryTypeRestClient.PARAM_NAME, timeEntryTypeRequestDto1.isName())
                .queryParam(TimeEntryTypeRestClient.PARAM_START, timeEntryTypeRequestDto1.isStartTime())
                .queryParam(TimeEntryTypeRestClient.PARAM_END, timeEntryTypeRequestDto1.isEndTime())
                .queryParam(TimeEntryTypeRestClient.PARAM_DATE, timeEntryTypeRequestDto1.isDate())
                .queryParam(TimeEntryTypeRestClient.PARAM_DESCRIPTION, timeEntryTypeRequestDto1.isDescription())
                .queryParam(TimeEntryTypeRestClient.PARAM_WORKPACKAGE_UUID, timeEntryTypeRequestDto1.isWorkPackageUuid())
                .queryParam(TimeEntryTypeRestClient.PARAM_EMPLOYEE_UUID, timeEntryTypeRequestDto1.isEmployeeUuid())
                .queryParam(TimeEntryTypeRestClient.PARAM_ARCHIVED, timeEntryTypeRequestDto1.isArchived())
                .when().get(getBasePath())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().asPrettyString();

        System.out.println(timeEntryTypeQuery);
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 204, 204", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void deleteTimeEntryTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        TimeEntryTypeRequestDto timeEntryTypeRequestDto = TimeEntryTypeFactory.getTimeEntryTypeRequestDto();

        setMockResponse(TimeEntryTypeFactory.getFullDispatcher(true, statusCodeRequest == 204, statusCodeRequest, ""));

        Response response = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-Type", "application/json")
                .when().delete(getBasePath() + "/" + timeEntryTypeRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().response();

        if (statusCodeResponse == 204) {
            System.out.println("Delete Status: " + response.getStatusCode());
            Assertions.assertEquals(204, response.getStatusCode());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void updateTimeEntryTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        TimeEntryTypeRequestDto timeEntryTypeRequestDto =
                TimeEntryTypeFactory.getTimeEntryTypeRequestDto();

        TimeEntryTypeRequestUpdateDto timeEntryTypeRequestUpdateDto =
                TimeEntryTypeFactory.getTimeEntryTypeUpdateRequestDto();

        setMockResponse(TimeEntryTypeFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        TimeEntryTypeDto updatedTimeEntry = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .and().body(timeEntryTypeRequestUpdateDto)
                .when().put(getBasePath() + "/" + timeEntryTypeRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(timeEntryTypeRequestUpdateDto.getTypeName(), updatedTimeEntry.getTypeName());
            Assertions.assertEquals(timeEntryTypeRequestUpdateDto.isName(), updatedTimeEntry.isName());
            Assertions.assertEquals(timeEntryTypeRequestUpdateDto.isStartTime(), updatedTimeEntry.isStartTime());
            Assertions.assertEquals(timeEntryTypeRequestUpdateDto.isEndTime(), updatedTimeEntry.isEndTime());
            Assertions.assertEquals(timeEntryTypeRequestUpdateDto.isDate(), updatedTimeEntry.isDate());
            Assertions.assertEquals(timeEntryTypeRequestUpdateDto.isDescription(), updatedTimeEntry.isDescription());
            Assertions.assertEquals(timeEntryTypeRequestUpdateDto.isWorkPackageUuid(), updatedTimeEntry.isWorkPackageUuid());
            Assertions.assertEquals(timeEntryTypeRequestUpdateDto.isEmployeeUuid(), updatedTimeEntry.isEmployeeUuid());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void archiveTimeEntryTypeTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        String uuid = UUID.randomUUID().toString();

        TimeEntryTypeRequestUpdateDto timeEntryTypeRequestUpdateDto = TimeEntryTypeFactory.getTimeEntryTypeUpdateRequestDto();

        setMockResponse(TimeEntryTypeFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        TimeEntryTypeDto timeEntryTypeDtoResponse = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .queryParam(TimeEntryTypeRestClient.PARAM_ARCHIVED, false)
                .queryParam("entity_type", EntityTypeEnum.TIMEENTRYTYPE)
                .queryParam("recursive", false)
                .and()
                .when().put(getBasePath() + "/archive/" + uuid)
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertFalse(timeEntryTypeDtoResponse.isArchived());
        }
    }

    protected String getBasePath() {
        return TimeEntryTypeResource.PATH;
    }

    private static String checkAndGetValueOrNull(String value) {
        return StringUtils.isNotEmpty(value) ? value : "";
    }
}
