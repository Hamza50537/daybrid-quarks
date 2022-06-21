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
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CustomerResourceTest extends AbstractMockWebServerTest {

    @Test
    void addCustomerTest() {

        CustomerRequestPostDto customerRequestPostDto = CustomerFactory.getCustomerRequestPostDto();

        setMockResponse(CustomerFactory.getFullDispatcher(true, true, 200, ""));

        CustomerPostDto customerPost = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .and().body(customerRequestPostDto)
                .when().post(getBasePath())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(customerRequestPostDto.getName(), customerPost.getName());
        Assertions.assertEquals(customerRequestPostDto.getSign(), customerPost.getSign());
        Assertions.assertEquals(customerRequestPostDto.getEmail(), customerPost.getEmail());
        Assertions.assertEquals(customerRequestPostDto.getPhoneNumber(), customerPost.getPhoneNumber());
        Assertions.assertEquals(customerRequestPostDto.getAddressUuid(), customerPost.getAddressUuid());
    }

    @Test
    void getCustomerTest() {

        CustomerRequestDto customerRequestDto = CustomerFactory.getCustomerRequestDto();

        setMockResponse(CustomerFactory.getFullDispatcher(true, true, 200, ""));

        CustomerDto customerGet = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .when().get(getBasePath() + "/" + customerRequestDto.getUuid())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(customerRequestDto.getUuid(), customerGet.getUuid());
    }

    @Test
    void getCustomerQueryTest() {

        CustomerRequestDto customerRequestDto1 =
                CustomerFactory.getCustomerRequestDto();
        customerRequestDto1.setName("Test1");

        CustomerRequestDto customerRequestDto2 =
                CustomerFactory.getCustomerRequestDto();
        customerRequestDto2.setName("Test2");

        setMockResponse(CustomerFactory.getFullDispatcher(true, true, 200, ""));

        List<CustomerDto> customerQuery = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .queryParam(CustomerRestClient.PARAM_NAME, checkAndGetValueOrNull(customerRequestDto1.getName()))
                .queryParam(CustomerRestClient.PARAM_SIGN, checkAndGetValueOrNull(customerRequestDto1.getSign()))
                .queryParam(CustomerRestClient.PARAM_EMAIL, checkAndGetValueOrNull(customerRequestDto1.getEmail()))
                .queryParam(CustomerRestClient.PARAM_PHONE_NUMBER, checkAndGetValueOrNull(customerRequestDto1.getPhoneNumber()))
                .queryParam(CustomerRestClient.PARAM_ADDRESS_UUID, checkAndGetValueOrNull(customerRequestDto1.getAddressUuid()))
                .queryParam(CustomerRestClient.PARAM_ARCHIVED, customerRequestDto1.isArchived())
                .when().get(getBasePath())
                .then().extract().as(new TypeRef<>(){
                });

        Assertions.assertNotNull(customerQuery);
        Assertions.assertEquals(2, customerQuery.size());
    }

    @Test
    void deleteCustomerTest() {

        CustomerRequestDto customerRequestDto = CustomerFactory.getCustomerRequestDto();

        setMockResponse(CustomerFactory.getFullDispatcher(true, true, 204, ""));

        Response response = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .when().delete(getBasePath() + "/" + customerRequestDto.getUuid())
                .then().extract().response();

        System.out.println("Delete Status: " + response.getStatusCode());
        Assertions.assertEquals(204, response.getStatusCode());
    }

    @Test
    void updateCustomerTest() {

        CustomerRequestDto customerRequestDto =
                CustomerFactory.getCustomerRequestDto();

        CustomerRequestUpdateDto customerRequestUpdateDto =
                CustomerFactory.getCustomerUpdateRequestDto();

        setMockResponse(CustomerFactory.getFullDispatcher(true, true, 200, ""));

        CustomerDto updatesCustomer = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .and().body(customerRequestUpdateDto)
                .when().put(getBasePath() + "/" + customerRequestDto.getUuid())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(customerRequestUpdateDto.getName(), updatesCustomer.getName());
        Assertions.assertEquals(customerRequestUpdateDto.getSign(), updatesCustomer.getSign());
        Assertions.assertEquals(customerRequestUpdateDto.getEmail(), updatesCustomer.getEmail());
        Assertions.assertEquals(customerRequestUpdateDto.getPhoneNumber(), updatesCustomer.getPhoneNumber());
    }

    @Test
    void archiveCustomerTest() {

        String uuid = UUID.randomUUID().toString();

        CustomerRequestUpdateDto addressRequestUpdateDto = CustomerFactory.getCustomerUpdateRequestDto();

        setMockResponse(CustomerFactory.getFullDispatcher(true, true, 200, ""));

        CustomerDto customerDtoResponse = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .queryParam(CustomerRestClient.PARAM_ARCHIVED, false)
                .queryParam("entity_type", EntityTypeEnum.CUSTOMER)
                .queryParam("recursive", false)
                .and()
                .when().put(getBasePath() + "/archive/" + uuid)
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertFalse(customerDtoResponse.isArchived());
    }

    protected String getBasePath() {
        return CustomerResource.PATH;
    }

    private static String checkAndGetValueOrNull(String value) {
        return StringUtils.isNotEmpty(value) ? value : "";
    }
}
