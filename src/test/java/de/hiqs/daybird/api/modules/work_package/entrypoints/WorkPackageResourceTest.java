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
class WorkPackageResourceTest extends AbstractMockWebServerTest {

    @Test
    void addWorkPackageTest() {

        WorkPackageRequestPostDto workPackageRequestPostDto = WorkPackageFactory.getWorkPackageRequestPostDto();

        setMockResponse(WorkPackageFactory.getFullDispatcher(true, true, 200, ""));

        WorkPackagePostDto workPackagePost = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .and().body(workPackageRequestPostDto)
                .when().post(getBasePath())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(workPackageRequestPostDto.getName(), workPackagePost.getName());
        Assertions.assertEquals(workPackageRequestPostDto.getSign(), workPackagePost.getSign());
        Assertions.assertEquals(workPackageRequestPostDto.getStartDate(), workPackagePost.getStartDate());
        Assertions.assertEquals(workPackageRequestPostDto.getEndDate(), workPackagePost.getEndDate());
        Assertions.assertEquals(workPackageRequestPostDto.getProjectUuid(), workPackagePost.getProjectUuid());
        Assertions.assertEquals(workPackageRequestPostDto.getEmployeeUuid(), workPackagePost.getEmployeeUuid());
    }

    @Test
    void getWorkPackageTest() {

        WorkPackageRequestDto workPackageRequestDto = WorkPackageFactory.getWorkPackageRequestDto();

        setMockResponse(WorkPackageFactory.getFullDispatcher(true, true, 200, ""));

        WorkPackageDto workPackageGet = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .when().get(getBasePath() + "/" + workPackageRequestDto.getUuid())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(workPackageRequestDto.getUuid(), workPackageGet.getUuid());
    }

    @Test
    void getWorkPackageQueryTest() {

        WorkPackageRequestDto workPackageRequestDto1 = WorkPackageFactory.getWorkPackageRequestDto();
        workPackageRequestDto1.setName("Test1");

        WorkPackageRequestDto workPackageRequestDto2 = WorkPackageFactory.getWorkPackageRequestDto();
        workPackageRequestDto2.setName("Test2");

        setMockResponse(WorkPackageFactory.getFullDispatcher(true, true, 200, ""));

        List<WorkPackageDto> workPackageQuery = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-Type", "application/json")
                .queryParam(WorkPackageRestClient.PATH_NAME, checkAndGetValueOrNull(workPackageRequestDto1.getName()))
                .queryParam(WorkPackageRestClient.PATH_SIGN, checkAndGetValueOrNull(workPackageRequestDto1.getSign()))
                .queryParam(WorkPackageRestClient.PATH_START_DATE, checkAndGetValueOrNull(workPackageRequestDto1.getStartDate()))
                .queryParam(WorkPackageRestClient.PATH_END_DATE, checkAndGetValueOrNull(workPackageRequestDto1.getEndDate()))
                .queryParam(WorkPackageRestClient.PARAM_PROJECT_UUID, checkAndGetValueOrNull(workPackageRequestDto1.getProjectUuid()))
                .queryParam(WorkPackageRestClient.PARAM_EMPLOYEE_UUID, checkAndGetValueOrNull(workPackageRequestDto1.getEmployeeUuid()))
                .queryParam(WorkPackageRestClient.PARAM_ARCHIVED, workPackageRequestDto1.isArchived())
                .when().get(getBasePath())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertNotNull(workPackageQuery);
        Assertions.assertEquals(2, workPackageQuery.size());
    }

    @Test
    void getWorkPackageQueryProjectUuidTest() {

        WorkPackageRequestDto workPackageRequestDto1 = WorkPackageFactory.getWorkPackageRequestDto();
        workPackageRequestDto1.setName("Test1");

        WorkPackageRequestDto workPackageRequestDto2 = WorkPackageFactory.getWorkPackageRequestDto();
        workPackageRequestDto2.setName("Test2");

        setMockResponse(WorkPackageFactory.getFullDispatcher(true, true, 200, ""));

        List<WorkPackageDto> workPackageQuery = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-Type", "application/json")
                .queryParam(WorkPackageRestClient.PARAM_PROJECT_UUID, checkAndGetValueOrNull(workPackageRequestDto1.getProjectUuid()))
                .when().get(getBasePath())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertNotNull(workPackageQuery);
        Assertions.assertEquals(2, workPackageQuery.size());
    }

    @Test
    void deleteWorkPackageTest() {

        WorkPackageRequestDto workPackageRequestDto = WorkPackageFactory.getWorkPackageRequestDto();

        setMockResponse(WorkPackageFactory.getFullDispatcher(true, true, 204, ""));

        Response response = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-Type", "application/json")
                .when().delete(getBasePath() + "/" + workPackageRequestDto.getUuid())
                .then().extract().response();

        System.out.println("Delete Status: " + response.getStatusCode());
        Assertions.assertEquals(204, response.getStatusCode());
    }

    @Test
    void updateWorkPackageTest() {

        WorkPackageRequestDto workPackageRequestDto =
                WorkPackageFactory.getWorkPackageRequestDto();

        WorkPackageRequestUpdateDto workPackageRequestUpdateDto =
                WorkPackageFactory.getWorkPackageUpdateRequestDto();

        setMockResponse(WorkPackageFactory.getFullDispatcher(true, true, 200, ""));

        WorkPackageDto updatedWorkPackage = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .and().body(workPackageRequestUpdateDto)
                .when().put(getBasePath() + "/" + workPackageRequestDto.getUuid())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(workPackageRequestUpdateDto.getName(), updatedWorkPackage.getName());
        Assertions.assertEquals(workPackageRequestUpdateDto.getSign(), updatedWorkPackage.getSign());
    }

    @Test
    void archiveWorkPackageTest() {

        String uuid = UUID.randomUUID().toString();

        WorkPackageRequestUpdateDto workPackageRequestUpdateDto = WorkPackageFactory.getWorkPackageUpdateRequestDto();

        setMockResponse(WorkPackageFactory.getFullDispatcher(true, true, 200, ""));

        WorkPackageDto workPackageDtoResponse = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .queryParam(WorkPackageRestClient.PARAM_ARCHIVED, false)
                .queryParam("entity_type", EntityTypeEnum.WORKPACKAGE)
                .queryParam("recursive", false)
                .and()
                .when().put(getBasePath() + "/archive/" + uuid)
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertFalse(workPackageDtoResponse.isArchived());
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
