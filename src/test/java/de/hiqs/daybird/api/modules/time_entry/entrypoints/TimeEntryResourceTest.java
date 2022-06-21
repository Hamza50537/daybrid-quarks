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
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
class TimeEntryResourceTest extends AbstractMockWebServerTest {

    @Test
    void addTimeEntryTest() {

        TimeEntryRequestPostDto timeEntryRequestPostDto = TimeEntryFactory.getTimeEntryRequestPostDto();

        setMockResponse(TimeEntryFactory.getFullDispatcher(true, true, 200, ""));

        TimeEntryPostDto timeEntryPost = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-Type", "application/json")
                .and().body(timeEntryRequestPostDto)
                .when().post(getBasePath())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(timeEntryRequestPostDto.getName(), timeEntryPost.getName());
        Assertions.assertEquals(timeEntryRequestPostDto.getDate(), timeEntryPost.getDate());
        Assertions.assertEquals(timeEntryRequestPostDto.getDescription(), timeEntryPost.getDescription());
        Assertions.assertEquals(timeEntryRequestPostDto.getWorkPackageUuid(), timeEntryPost.getWorkPackageUuid());
        Assertions.assertEquals(timeEntryRequestPostDto.getEmployeeUuid(), timeEntryPost.getEmployeeUuid());
        Assertions.assertEquals(timeEntryRequestPostDto.getTimeEntryTypeUuid(), timeEntryPost.getTimeEntryTypeUuid());
    }

    @Test
    void getTimeEntryTest() {

        TimeEntryRequestDto timeEntryRequestDto = TimeEntryFactory.getTimeEntryRequestDto();

        setMockResponse(TimeEntryFactory.getFullDispatcher(true, true, 200, ""));

        TimeEntryDto timeEntryGet = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .when().get(getBasePath() + "/" + timeEntryRequestDto.getUuid())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(timeEntryRequestDto.getUuid(), timeEntryGet.getUuid());
    }

    @Test
    void getTimeEntryQueryTest() {

        TimeEntryRequestDto timeEntryRequestDto1 = TimeEntryFactory.getTimeEntryRequestDto();
        timeEntryRequestDto1.setName("Test1");

        TimeEntryRequestDto timeEntryRequestDto2 = TimeEntryFactory.getTimeEntryRequestDto();
        timeEntryRequestDto2.setName("Test2");

        setMockResponse(TimeEntryFactory.getFullDispatcher(true, true, 200, ""));

        List<TimeEntryDto> timeEntryQuery = given()
                .auth().oauth2(getAccessToken("admin", "123"))
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
                .then().extract().as(new TypeRef<>(){
                });

        Assertions.assertNotNull(timeEntryQuery);
        Assertions.assertEquals(2, timeEntryQuery.size());
    }

    @Test
    void deleteTimeEntryTest() {

        TimeEntryRequestDto timeEntryRequestDto = TimeEntryFactory.getTimeEntryRequestDto();

        setMockResponse(TimeEntryFactory.getFullDispatcher(true, true, 204, ""));

        Response response = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-Type", "application/json")
                .when().delete(getBasePath() + "/" + timeEntryRequestDto.getUuid())
                .then().extract().response();

        System.out.println("Delete Status: " + response.getStatusCode());
        Assertions.assertEquals(204, response.getStatusCode());
    }

    @Test
    void updateTimeEntryTest() {

        TimeEntryRequestDto timeEntryRequestDto =
                TimeEntryFactory.getTimeEntryRequestDto();

        TimeEntryRequestUpdateDto timeEntryRequestUpdateDto =
                TimeEntryFactory.getTimeEntryUpdateRequestDto();

        setMockResponse(TimeEntryFactory.getFullDispatcher(true, true, 200, ""));

        TimeEntryDto updatedTimeEntry = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .and().body(timeEntryRequestUpdateDto)
                .when().put(getBasePath() + "/" + timeEntryRequestDto.getUuid())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(timeEntryRequestUpdateDto.getName(), updatedTimeEntry.getName());
        Assertions.assertEquals(timeEntryRequestUpdateDto.getDescription(), updatedTimeEntry.getDescription());
    }

    @Test
    void archiveTimeEntryTest() {

        String uuid = UUID.randomUUID().toString();

        TimeEntryRequestUpdateDto timeEntryRequestUpdateDto = TimeEntryFactory.getTimeEntryUpdateRequestDto();

        setMockResponse(TimeEntryFactory.getFullDispatcher(true, true, 200, ""));

        TimeEntryDto timeEntryDtoResponse = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .queryParam(TimeEntryRestClient.PARAM_ARCHIVED, false)
                .queryParam("entity_type", EntityTypeEnum.TIMEENTRY)
                .queryParam("recursive", false)
                .and()
                .when().put(getBasePath() + "/archive/" + uuid)
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertFalse(timeEntryDtoResponse.isArchived());
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
