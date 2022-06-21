package de.hiqs.daybird.api.modules.customer.entrypoints;

import de.hiqs.daybird.api.modules.customer.CustomerFactory;
import de.hiqs.daybird.api.modules.customer.dataproviders.CustomerRestClient;
import de.hiqs.daybird.api.modules.customer.dataproviders.models.CustomerRequestDto;
import de.hiqs.daybird.api.modules.customer.dataproviders.models.CustomerRequestPostDto;
import de.hiqs.daybird.api.modules.customer.dataproviders.models.CustomerRequestUpdateDto;
import de.hiqs.daybird.api.modules.customer.entrypoints.models.CustomerDto;
import de.hiqs.daybird.api.modules.customer.entrypoints.models.CustomerPostDto;
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
class CustomerRoleTest extends AbstractMockWebServerTest {

    @ParameterizedTest
    @CsvSource({"user, 123, 401"})
    void addCustomerNoAuthTest(String username, String password, int statusCode) {
        RestAssured.defaultParser = Parser.JSON;
        CustomerRequestPostDto customerRequestPostDto = CustomerFactory.getCustomerRequestPostDto();

        given()
                .auth().oauth2(getAccessToken(username, password) + "1")
                .header("Content-type", "application/json")
                .and().body(customerRequestPostDto)
                .when().post(getBasePath())
                .then().assertThat().statusCode(statusCode);

    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 403, 500", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void addCustomerTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        CustomerRequestPostDto customerRequestPostDto = CustomerFactory.getCustomerRequestPostDto();

        setMockResponse(CustomerFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        CustomerPostDto customerPost = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .and().body(customerRequestPostDto)
                .when().post(getBasePath())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(customerRequestPostDto.getName(), customerPost.getName());
            Assertions.assertEquals(customerRequestPostDto.getSign(), customerPost.getSign());
            Assertions.assertEquals(customerRequestPostDto.getEmail(), customerPost.getEmail());
            Assertions.assertEquals(customerRequestPostDto.getPhoneNumber(), customerPost.getPhoneNumber());
            Assertions.assertEquals(customerRequestPostDto.getAddressUuid(), customerPost.getAddressUuid());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void getCustomerTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        CustomerRequestDto customerRequestDto = CustomerFactory.getCustomerRequestDto();

        setMockResponse(CustomerFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        CustomerDto customerGet = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .when().get(getBasePath() + "/" + customerRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(customerRequestDto.getUuid(), customerGet.getUuid());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void getCustomerQueryTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        CustomerRequestDto customerRequestDto1 =
                CustomerFactory.getCustomerRequestDto();
        customerRequestDto1.setName("Test1");

        CustomerRequestDto customerRequestDto2 =
                CustomerFactory.getCustomerRequestDto();
        customerRequestDto2.setName("Test2");

        setMockResponse(CustomerFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        String customerQuery = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .queryParam(CustomerRestClient.PARAM_NAME, checkAndGetValueOrNull(customerRequestDto1.getName()))
                .queryParam(CustomerRestClient.PARAM_SIGN, checkAndGetValueOrNull(customerRequestDto1.getSign()))
                .queryParam(CustomerRestClient.PARAM_EMAIL, checkAndGetValueOrNull(customerRequestDto1.getEmail()))
                .queryParam(CustomerRestClient.PARAM_PHONE_NUMBER, checkAndGetValueOrNull(customerRequestDto1.getPhoneNumber()))
                .queryParam(CustomerRestClient.PARAM_ADDRESS_UUID, checkAndGetValueOrNull(customerRequestDto1.getAddressUuid()))
                .queryParam(CustomerRestClient.PARAM_ARCHIVED, customerRequestDto1.isArchived())
                .when().get(getBasePath())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().asPrettyString();

        System.out.println(customerQuery);
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 204, 204", "controller, 123, 204, 204", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void deleteCustomerTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        CustomerRequestDto customerRequestDto = CustomerFactory.getCustomerRequestDto();

        setMockResponse(CustomerFactory.getFullDispatcher(true, statusCodeRequest == 204, statusCodeRequest, ""));

        Response response = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .when().delete(getBasePath() + "/" + customerRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().response();

        if (statusCodeResponse == 204) {
            System.out.println("Delete Status: " + response.getStatusCode());
            Assertions.assertEquals(204, response.getStatusCode());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void updateCustomerTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        CustomerRequestDto customerRequestDto =
                CustomerFactory.getCustomerRequestDto();

        CustomerRequestUpdateDto customerRequestUpdateDto =
                CustomerFactory.getCustomerUpdateRequestDto();

        setMockResponse(CustomerFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        CustomerDto updatesCustomer = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .and().body(customerRequestUpdateDto)
                .when().put(getBasePath() + "/" + customerRequestDto.getUuid())
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertEquals(customerRequestUpdateDto.getName(), updatesCustomer.getName());
            Assertions.assertEquals(customerRequestUpdateDto.getSign(), updatesCustomer.getSign());
            Assertions.assertEquals(customerRequestUpdateDto.getEmail(), updatesCustomer.getEmail());
            Assertions.assertEquals(customerRequestUpdateDto.getPhoneNumber(), updatesCustomer.getPhoneNumber());
        }
    }

    @ParameterizedTest
    @CsvSource({"admin, 123, 200, 200", "controller, 123, 200, 200", "hr, 123, 403, 500", "finance, 123, 403, 500", "user, 123, 403, 500"})
    void archiveCustomerTest(String username, String password, int statusCodeRequest, int statusCodeResponse) {

        String uuid = UUID.randomUUID().toString();

        CustomerRequestUpdateDto addressRequestUpdateDto = CustomerFactory.getCustomerUpdateRequestDto();

        setMockResponse(CustomerFactory.getFullDispatcher(true, statusCodeRequest == 200, statusCodeRequest, ""));

        CustomerDto customerDtoResponse = given()
                .auth().oauth2(getAccessToken(username, password))
                .header("Content-type", "application/json")
                .queryParam(CustomerRestClient.PARAM_ARCHIVED, false)
                .queryParam("entity_type", EntityTypeEnum.CUSTOMER)
                .queryParam("recursive", false)
                .and()
                .when().put(getBasePath() + "/archive/" + uuid)
                .then().assertThat().statusCode(statusCodeResponse)
                .extract().as(new TypeRef<>() {
                });

        if (statusCodeResponse == 200) {
            Assertions.assertFalse(customerDtoResponse.isArchived());
        }
    }

    protected String getBasePath() {
        return CustomerResource.PATH;
    }

    private static String checkAndGetValueOrNull(String value) {
        return StringUtils.isNotEmpty(value) ? value : "";
    }
}
