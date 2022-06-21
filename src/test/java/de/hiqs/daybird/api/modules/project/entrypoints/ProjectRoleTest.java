package de.hiqs.daybird.api.modules.project.entrypoints;

import de.hiqs.daybird.api.modules.project.ProjectFactory;
import de.hiqs.daybird.api.modules.project.dataproviders.ProjectRestClient;
import de.hiqs.daybird.api.modules.project.dataproviders.models.ProjectRequestDto;
import de.hiqs.daybird.api.modules.project.dataproviders.models.ProjectRequestPostDto;
import de.hiqs.daybird.api.modules.project.dataproviders.models.ProjectRequestUpdateDto;
import de.hiqs.daybird.api.modules.project.entrypoints.models.ProjectDto;
import de.hiqs.daybird.api.modules.project.entrypoints.models.ProjectPostDto;
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
class ProjectRoleTest extends AbstractMockWebServerTest {

    @ParameterizedTest
    @CsvSource({"user, 123, 401"})
    void addProjectNoAuthTest(String username, String password, int statusCode) {
        RestAssured.defaultParser = Parser.JSON;
        ProjectRequestPostDto projectRequestPostDto = ProjectFactory.getProjectRequestPostDto();

        given()
                .auth().oauth2(getAccessToken(username, password) + "1")
                .header("Content-type", "application/json")
                .and().body(projectRequestPostDto)
                .when().post(getBasePath())
                .then().assertThat().statusCode(statusCode);
    }

    @ParameterizedTest
    @CsvSource({"admin,123,200, 200", "controller,123,200, 200", "hr,123,403, 500", "finance,123,403, 500", "user,123,403, 500"})
    void addProjectTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        ProjectRequestPostDto projectRequestPostDto = ProjectFactory.getProjectRequestPostDto();

        setMockResponse(ProjectFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        ProjectPostDto projectPost = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .and().body(projectRequestPostDto)
                .when().post(getBasePath())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(projectRequestPostDto.getName(), projectPost.getName());
            Assertions.assertEquals(projectRequestPostDto.getSign(), projectPost.getSign());
            Assertions.assertEquals(projectRequestPostDto.getStartDate(), projectPost.getStartDate());
            Assertions.assertEquals(projectRequestPostDto.getEndDate(), projectPost.getEndDate());
            Assertions.assertEquals(projectRequestPostDto.getCustomerUuid(), projectPost.getCustomerUuid());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin,123,200, 200", "controller,123,200, 200", "hr,123,403, 500", "finance,123,403, 500", "user,123,403, 500"})
    void getProjectTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        ProjectRequestDto projectRequestDto = ProjectFactory.getProjectRequestDto();

        setMockResponse(ProjectFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        ProjectDto projectGet = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .when().get(getBasePath() + "/" + projectRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(projectRequestDto.getUuid(), projectGet.getUuid());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin,123,200, 200", "controller,123,200, 200", "hr,123,403, 500", "finance,123,403, 500", "user,123,403, 500"})
    void getProjectQueryTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        ProjectRequestDto projectRequestDto1 = ProjectFactory.getProjectRequestDto();
        projectRequestDto1.setName("Test1");

        ProjectRequestDto projectRequestDto2 = ProjectFactory.getProjectRequestDto();
        projectRequestDto2.setName("Test2");

        setMockResponse(ProjectFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        String projectQuery = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-Type", "application/json")
                .queryParam(ProjectRestClient.PATH_NAME, checkAndGetValueOrNull(projectRequestDto1.getName()))
                .queryParam(ProjectRestClient.PATH_SIGN, checkAndGetValueOrNull(projectRequestDto1.getSign()))
                .queryParam(ProjectRestClient.PATH_START_DATE, checkAndGetValueOrNull(projectRequestDto1.getStartDate()))
                .queryParam(ProjectRestClient.PATH_END_DATE, checkAndGetValueOrNull(projectRequestDto1.getEndDate()))
                .queryParam(ProjectRestClient.PARAM_CUSTOMER_UUID, checkAndGetValueOrNull(projectRequestDto1.getCustomerUuid()))
                .queryParam(ProjectRestClient.PARAM_ARCHIVED, projectRequestDto1.isArchived())
                .when().get(getBasePath())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().asPrettyString();

        System.out.println(projectQuery);
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 204, 204", "controller, 123, 204, 204", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void deleteProjectTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        ProjectRequestDto projectRequestDto = ProjectFactory.getProjectRequestDto();

        setMockResponse(ProjectFactory.getFullDispatcher(true, statusCodeRequest == 204, statusCodeRequest, ""));

        Response response = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-Type", "application/json")
                .when().delete(getBasePath() + "/" + projectRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().response();

        if (statusCodeResponse == 204) {
            System.out.println("Delete Status: " + response.getStatusCode());
            Assertions.assertEquals(204, response.getStatusCode());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void updateProjectTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        ProjectRequestDto projectRequestDto =
                ProjectFactory.getProjectRequestDto();

        ProjectRequestUpdateDto projectRequestUpdateDto =
                ProjectFactory.getProjectUpdateRequestDto();

        setMockResponse(ProjectFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        ProjectDto updatedProject = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .and().body(projectRequestUpdateDto)
                .when().put(getBasePath() + "/" + projectRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(projectRequestUpdateDto.getName(), updatedProject.getName());
            Assertions.assertEquals(projectRequestUpdateDto.getSign(), updatedProject.getSign());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void archiveProjectTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        String uuid = UUID.randomUUID().toString();

        ProjectRequestUpdateDto projectRequestUpdateDto = ProjectFactory.getProjectUpdateRequestDto();

        setMockResponse(ProjectFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        ProjectDto projectDtoResponse = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .queryParam(ProjectRestClient.PARAM_ARCHIVED, false)
                .queryParam("entity_type", EntityTypeEnum.PROJECT)
                .queryParam("recursive", false)
                .and()
                .when().put(getBasePath() + "/archive/" + uuid)
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertFalse(projectDtoResponse.isArchived());
        }
    }

    protected String getBasePath() {
        return ProjectResource.PATH;
    }

    private static String checkAndGetValueOrNull(String value) {
        return StringUtils.isNotEmpty(value) ? value : "";
    }

    private static String checkAndGetValueOrNull(LocalDate value) {
        return value != null ? value.toString() : "";
    }
}
