package de.hiqs.daybird.api.modules.customer.entrypoints;

import de.hiqs.daybird.api.modules.customer.dataproviders.CustomerRestClient;
import de.hiqs.daybird.api.modules.customer.domains.CustomerService;
import de.hiqs.daybird.api.modules.customer.domains.models.Customer;
import de.hiqs.daybird.api.modules.customer.domains.models.CustomerUpdate;
import de.hiqs.daybird.api.modules.customer.entrypoints.models.CustomerDto;
import de.hiqs.daybird.api.modules.customer.entrypoints.models.CustomerPostDto;
import de.hiqs.daybird.api.modules.customer.entrypoints.models.CustomerUpdateDto;
import de.hiqs.daybird.api.shared.domains.models.EntityTypeEnum;
import de.hiqs.daybird.api.shared.entrypoints.AbstractReactiveResource;
import io.smallrye.mutiny.Uni;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(CustomerResource.PATH)
@ApplicationScoped
public class CustomerResource extends AbstractReactiveResource<CustomerDto> {

    public static final String PATH = "/customers";

    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    public CustomerResource(CustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<CustomerDto> addCustomer(CustomerPostDto customerDto) {
        return customerService.addCustomer(modelMapper.map(customerDto, Customer.class))
                .map(customer -> modelMapper.map(customer, CustomerDto.class));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<CustomerDto>> list(@QueryParam(CustomerRestClient.PARAM_NAME) String name,
                                       @QueryParam(CustomerRestClient.PARAM_SIGN) String sign,
                                       @QueryParam(CustomerRestClient.PARAM_EMAIL) String email,
                                       @QueryParam(CustomerRestClient.PARAM_PHONE_NUMBER) String phoneNumber,
                                       @QueryParam(CustomerRestClient.PARAM_ADDRESS_UUID) String addressUuid,
                                       @DefaultValue("false") @QueryParam(CustomerRestClient.PARAM_ARCHIVED) boolean archived) {
        return customerService.list(name, sign, email, phoneNumber, addressUuid, archived)
                .map(customers -> modelMapper.map(customers, new TypeToken<List<CustomerDto>>() {
                }.getType()));
    }

    @GET
    @Path("/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<CustomerDto> getCustomer(@PathParam("uuid") String uuid) {
        return customerService.getCustomer(uuid)
                .map(customer -> modelMapper.map(customer, CustomerDto.class));
    }

    @GET
    @Path("/uuid")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<CustomerDto> getByUuid(@QueryParam("uuid") String uuid) {
        return customerService.getByUuid(uuid)
                .map(customer -> modelMapper.map(customer, CustomerDto.class));
    }

    @DELETE
    @Path("/{uuid}")
    public Uni<Void> deleteCustomer(@PathParam("uuid") String uuid) {
        return customerService.deleteCustomer(uuid);
    }

    @PUT
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<CustomerDto> update(@PathParam("uuid") String uuid, CustomerUpdateDto customerUpdateDto) {
        return customerService.update(uuid, modelMapper.map(customerUpdateDto, CustomerUpdate.class))
                .map(customer -> modelMapper.map(customer, CustomerDto.class));
    }

    @PUT
    @Path("/archive/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<CustomerDto> updateArchived(@PathParam("uuid") String uuid,
                                    @QueryParam(CustomerRestClient.PARAM_ARCHIVED) boolean archived,
                                    @QueryParam("entity_type") EntityTypeEnum entityTypeEnum,
                                    @QueryParam("recursive") boolean recursive) {
        return customerService.updateArchived(uuid, archived, entityTypeEnum, recursive)
                .map(customer -> modelMapper.map(customer, CustomerDto.class));
    }
}
