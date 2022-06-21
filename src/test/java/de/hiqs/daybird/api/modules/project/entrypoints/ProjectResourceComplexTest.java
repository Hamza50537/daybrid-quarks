package de.hiqs.daybird.api.modules.project.entrypoints;

import de.hiqs.daybird.api.modules.project.ProjectFactory;
import de.hiqs.daybird.api.modules.project.dataproviders.ProjectRestClient;
import de.hiqs.daybird.api.modules.project.dataproviders.models.ProjectRequestDto;
import de.hiqs.daybird.api.modules.project.entrypoints.models.ProjectOverviewDto;
import de.hiqs.daybird.api.shared.AbstractMockWebServerTest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ProjectResourceComplexTest extends AbstractMockWebServerTest {

    @Test
    void getProjectQueryTest() {

        ProjectRequestDto projectRequestDto1 = ProjectFactory.getProjectRequestDto();
        projectRequestDto1.setName("Test1");

        ProjectRequestDto projectRequestDto2 = ProjectFactory.getProjectRequestDto();
        projectRequestDto2.setName("Test2");

        setMockResponse(ProjectFactory.getFullDispatcher(true, true, 200, ""));

        List<ProjectOverviewDto> projectQuery = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-Type", "application/json")
                .queryParam(ProjectRestClient.PARAM_CUSTOMER_UUID, checkAndGetValueOrNull(projectRequestDto1.getCustomerUuid()))
                .when().get(getBasePath() + "/overview")
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertNotNull(projectQuery);
        Assertions.assertNotNull(projectQuery.get(0).getCustomer());
        Assertions.assertEquals(2, projectQuery.size());
        Assertions.assertEquals(2, projectQuery.get(0).getWorkPackageList().size());

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
