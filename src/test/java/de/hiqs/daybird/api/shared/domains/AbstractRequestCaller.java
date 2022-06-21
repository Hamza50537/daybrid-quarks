package de.hiqs.daybird.api.shared.domains;

import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;

public abstract class AbstractRequestCaller<T, U> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRequestCaller.class);

    private final Class<T> type;

    protected AbstractRequestCaller(Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }

    protected abstract String getBasePath();

    protected abstract EntityTypeEnum getEntityType();

    public Response doDeleteByUuid(String uuid) {
        return given()
                .header("Content-type", "application/json")
                .when().delete(getBasePath() + "/" + uuid)
                .then().extract().response();
    }

    public List<T> doGetAll() {
        LOGGER.info("call doGetAll: " + getBasePath());
        return given()
                .contentType(ContentType.JSON)
                .when().get(getBasePath())
                .then().log().all().extract().as(new TypeRef<>() {
                });
    }

    public List<T> doGetAllWithArchived(boolean withArchived) {
        LOGGER.info("call doGetAll: " + getBasePath());
        return given()
                .contentType(ContentType.JSON)
                .queryParam("archived", withArchived)
                .when().get(getBasePath())
                .then().log().all().extract().as(new TypeRef<>() {
                });
    }

    public Response doGetAllResponse() {
        LOGGER.info("call doGetAllResponse: " + getBasePath());
        return given()
                .contentType(ContentType.JSON)
                .when().get(getBasePath())
                .then().log().all()
                .extract().response();
    }


    public T doGetById(String uuid) {
        return given()
                .header("Content-type", "application/json")
                .when().get(getBasePath() + "/" + uuid)
                .then().extract().as(type);
    }

    public T doGetByUuid(String uuid) {
        return given()
                .header("Content-type", "application/json")
                .queryParam("uuid", uuid)
                .when().get(getBasePath() + "/uuid")
                .then().extract().as(type);
    }

    public T doPut(String uuid, U instance) {
        return given()
                .header("Content-type", "application/json")
                .and().body(instance)
                .when().put(getBasePath() + "/" + uuid)
                .then().assertThat().statusCode(200)
                .extract().as(type);
    }

    public T doArchive(String uuid, boolean archived, EntityTypeEnum entityTypeEnum, boolean recursive) {
        return given()
                .header("Content-type", "application/json")
                .queryParam("archived", archived)
                .queryParam("entity_type", entityTypeEnum)
                .queryParam("recursive", recursive)
                .and()
                .when().put(getBasePath() + "/archive/" + uuid)
                .then().extract().as(type);
    }

    public T doPost(T instance) {
        return given()
                .header("Content-type", "application/json")
                .and().body(instance)
                .when().post(getBasePath())
                .then().extract().as(type);
    }
}
