package de.hiqs.daybird.api.modules.address.entrypoints;

import de.hiqs.daybird.api.modules.address.AddressFactory;
import de.hiqs.daybird.api.modules.address.dataproviders.AddressRestClient;
import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestDto;
import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestPostDto;
import de.hiqs.daybird.api.modules.address.dataproviders.models.AddressRequestUpdateDto;
import de.hiqs.daybird.api.modules.address.entrypoints.models.AddressDto;
import de.hiqs.daybird.api.modules.address.entrypoints.models.AddressPostDto;
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
class AddressResourceTest extends AbstractMockWebServerTest {


    @Test
    void addAddressTest() {

        AddressRequestPostDto addressRequestPostDto = AddressFactory.getAddressRequestPostDto();

        setMockResponse(AddressFactory.getFullDispatcher(true,true, 200, ""));

        AddressPostDto addressDtoResponse = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .and().body(addressRequestPostDto)
                .when().post(getBasePath())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(addressRequestPostDto.getCity(), addressDtoResponse.getCity());
        Assertions.assertEquals(addressRequestPostDto.getCountry(), addressDtoResponse.getCountry());
        Assertions.assertEquals(addressRequestPostDto.getZipCode(), addressDtoResponse.getZipCode());
        Assertions.assertEquals(addressRequestPostDto.getStreet(), addressDtoResponse.getStreet());
        Assertions.assertEquals(addressRequestPostDto.getHouseNumber(), addressDtoResponse.getHouseNumber());
    }

   @Test
    void getAddressTest() {

        AddressRequestDto addressRequestDto = AddressFactory.getAddressRequestDto();

        setMockResponse(AddressFactory.getFullDispatcher(true, true, 200, ""));

       AddressDto addressDtoResponse = given()
               .auth().oauth2(getAccessToken("admin", "123"))
               .header("Content-type", "application/json")
               .when().get(getBasePath() + "/" + addressRequestDto.getUuid())
               .then().extract().as(new TypeRef<>() {
               });

       Assertions.assertEquals(addressRequestDto.getUuid(), addressDtoResponse.getUuid());
   }

    @Test
    void getMeTest() {


        setMockResponse(AddressFactory.getFullDispatcher(true, true, 200, ""));

        Response response = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .when().get("/users/me")
                .andReturn();

        System.out.println(response.prettyPrint());
    }

    @Test
    void getAddressQueryTest() {

        AddressRequestDto addressRequestDto1 = AddressFactory.getAddressRequestDto();
        addressRequestDto1.setCountry("Test1");

        AddressRequestDto addressRequestDto2 = AddressFactory.getAddressRequestDto();
        addressRequestDto2.setCountry("Test2");

        setMockResponse(AddressFactory.getFullDispatcher(true, true, 200, ""));

        List<AddressDto> addressQuery = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .queryParam(AddressRestClient.PARAM_COUNTRY, checkAndGetValueOrNull(addressRequestDto1.getCountry()))
                .queryParam(AddressRestClient.PARAM_CITY, checkAndGetValueOrNull(addressRequestDto1.getCity()))
                .queryParam(AddressRestClient.PARAM_STREET, checkAndGetValueOrNull(addressRequestDto1.getStreet()))
                .queryParam(AddressRestClient.PARAM_HOUSE_NUMBER, checkAndGetValueOrNull(addressRequestDto1.getHouseNumber()))
                .queryParam(AddressRestClient.PARAM_ZIP_CODE, checkAndGetValueOrNull(addressRequestDto1.getZipCode()))
                .queryParam(AddressRestClient.PARAM_ARCHIVED, addressRequestDto1.isArchived())
                .when().get(getBasePath())
                .then().extract().as(new TypeRef<>(){
                });

        Assertions.assertNotNull(addressQuery);
        Assertions.assertEquals(2, addressQuery.size());
    }

    @Test
    void deleteAddressTest() {

        AddressRequestDto addressRequestDto = AddressFactory.getAddressRequestDto();

        setMockResponse(AddressFactory.getFullDispatcher(true, true, 204, ""));

        Response response = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .when().delete(getBasePath() + "/" + addressRequestDto.getUuid())
                .then().extract().response();

        System.out.println("Delete Status: " + response.getStatusCode());
        Assertions.assertEquals(204, response.getStatusCode());
    }

    @Test
    void updateAddressTest() {

        AddressRequestDto addressRequestDto = AddressFactory.getAddressRequestDto();

        AddressRequestUpdateDto addressRequestUpdateDto = AddressFactory.getAddressUpdateRequestDto();

        setMockResponse(AddressFactory.getFullDispatcher(true, true, 200, ""));

        AddressDto updatedAddress = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .and().body(addressRequestUpdateDto)
                .when().put(getBasePath() + "/" + addressRequestDto.getUuid())
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(addressRequestUpdateDto.getCity(), updatedAddress.getCity());
    }

   @Test
    void archiveAddressTest() {

        String uuid = UUID.randomUUID().toString();

        AddressRequestUpdateDto addressRequestUpdateDto = AddressFactory.getAddressUpdateRequestDto();

        setMockResponse(AddressFactory.getFullDispatcher(true, true, 200, ""));

        AddressDto addressDtoResponse = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .queryParam(AddressRestClient.PARAM_ARCHIVED, false)
                .queryParam("entity_type", EntityTypeEnum.ADDRESS)
                .queryParam("recursive", false)
                .and()
                .when().put(getBasePath() + "/archive/" + uuid)
                .then().extract().as(new TypeRef<>() {
                });

        Assertions.assertFalse(addressDtoResponse.isArchived());
    }

    protected String getBasePath() {
        return AddressResource.PATH;
    }

    private static String checkAndGetValueOrNull(String value) {
        return StringUtils.isNotEmpty(value) ? value : "";
    }
}
