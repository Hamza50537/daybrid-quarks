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
class ProjectResourceTest extends AbstractMockWebServerTest {

    @Test
    void addProjectTest() {

        ProjectRequestPostDto projectRequestPostDto = ProjectFactory.getProjectRequestPostDto();

        setMockResponse(ProjectFactory.getFullDispatcher(true, true, 200, ""));

        ProjectPostDto projectPost = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .and().body(projectRequestPostDto)
                .when().post(getBasePath())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(projectRequestPostDto.getName(), projectPost.getName());
        Assertions.assertEquals(projectRequestPostDto.getSign(), projectPost.getSign());
        Assertions.assertEquals(projectRequestPostDto.getStartDate(), projectPost.getStartDate());
        Assertions.assertEquals(projectRequestPostDto.getEndDate(), projectPost.getEndDate());
        Assertions.assertEquals(projectRequestPostDto.getCustomerUuid(), projectPost.getCustomerUuid());
    }

    @Test
    void getProjectTest() {

        ProjectRequestDto projectRequestDto = ProjectFactory.getProjectRequestDto();

        setMockResponse(ProjectFactory.getFullDispatcher(true, true, 200, ""));

        ProjectDto projectGet = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .when().get(getBasePath() + "/" + projectRequestDto.getUuid())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(projectRequestDto.getUuid(), projectGet.getUuid());
    }

   @Test
    void getProjectQueryTest() {

        ProjectRequestDto projectRequestDto1 = ProjectFactory.getProjectRequestDto();
        projectRequestDto1.setName("Test1");

        ProjectRequestDto projectRequestDto2 = ProjectFactory.getProjectRequestDto();
        projectRequestDto2.setName("Test2");

        setMockResponse(ProjectFactory.getFullDispatcher(true, true, 200, ""));

        List<ProjectDto> projectQuery = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-Type", "application/json")
                .queryParam(ProjectRestClient.PATH_NAME, checkAndGetValueOrNull(projectRequestDto1.getName()))
                .queryParam(ProjectRestClient.PATH_SIGN, checkAndGetValueOrNull(projectRequestDto1.getSign()))
                .queryParam(ProjectRestClient.PATH_START_DATE, checkAndGetValueOrNull(projectRequestDto1.getStartDate()))
                .queryParam(ProjectRestClient.PATH_END_DATE, checkAndGetValueOrNull(projectRequestDto1.getEndDate()))
                .queryParam(ProjectRestClient.PARAM_CUSTOMER_UUID, checkAndGetValueOrNull(projectRequestDto1.getCustomerUuid()))
                .queryParam(ProjectRestClient.PARAM_ARCHIVED, projectRequestDto1.isArchived())
                .when().get(getBasePath())
                .then().extract().as(new TypeRef<>(){
                });

        Assertions.assertNotNull(projectQuery);
        Assertions.assertEquals(2, projectQuery.size());
    }

    @Test
    void deleteProjectTest() {

        ProjectRequestDto projectRequestDto = ProjectFactory.getProjectRequestDto();

        setMockResponse(ProjectFactory.getFullDispatcher(true, true, 204, ""));

        Response response = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-Type", "application/json")
                .when().delete(getBasePath() + "/" + projectRequestDto.getUuid())
                .then().extract().response();

        System.out.println("Delete Status: " + response.getStatusCode());
        Assertions.assertEquals(204, response.getStatusCode());
    }

    @Test
    void updateProjectTest() {

        ProjectRequestDto projectRequestDto =
                ProjectFactory.getProjectRequestDto();

        ProjectRequestUpdateDto projectRequestUpdateDto =
                ProjectFactory.getProjectUpdateRequestDto();

        setMockResponse(ProjectFactory.getFullDispatcher(true, true, 200, ""));

        ProjectDto updatedProject = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .and().body(projectRequestUpdateDto)
                .when().put(getBasePath() + "/" + projectRequestDto.getUuid())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(projectRequestUpdateDto.getName(), updatedProject.getName());
        Assertions.assertEquals(projectRequestUpdateDto.getSign(), updatedProject.getSign());
    }

    @Test
    void archiveProjectTest() {

        String uuid = UUID.randomUUID().toString();

        ProjectRequestUpdateDto projectRequestUpdateDto = ProjectFactory.getProjectUpdateRequestDto();

        setMockResponse(ProjectFactory.getFullDispatcher(true, true, 200, ""));

        ProjectDto projectDtoResponse = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .queryParam(ProjectRestClient.PARAM_ARCHIVED, false)
                .queryParam("entity_type", EntityTypeEnum.PROJECT)
                .queryParam("recursive", false)
                .and()
                .when().put(getBasePath() + "/archive/" + uuid)
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertFalse(projectDtoResponse.isArchived());
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
