package de.fhe.ai.mc.network

import com.fasterxml.jackson.annotation.JsonProperty
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

class GeocodeReturnAddressContainer {
    @JsonProperty( "display_name" )
    var fullAddressString: String = ""
    @JsonProperty( "address" )
    var address: GeocodeReturnAddress? = null
}

class GeocodeReturnAddress {
    @JsonProperty( "house_number" )
    var houseNumber: String = ""
    @JsonProperty( "road" )
    var street: String = ""
    @JsonProperty( "suburb" )
    var suburb: String = ""
    @JsonProperty( "city" )
    var city: String = ""
    @JsonProperty( "state" )
    var state: String = ""
    @JsonProperty( "postcode" )
    var postcode: String = ""
    @JsonProperty( "country" )
    var country: String = ""
    @JsonProperty( "country_code" )
    var countryCode: String = ""

    constructor()

    override fun toString(): String {
        return "GeocodeReturnAddress(houseNumber='$houseNumber', street='$street', suburb='$suburb', city='$city', state='$state', postcode='$postcode', country='$country', countryCode='$countryCode')"
    }
}

@Path("/")
@RegisterRestClient(configKey="geocoding-service")
interface GeocodingApiClient {

    @GET
    @Path("/reverse")
    @Produces(MediaType.APPLICATION_JSON)
    fun reverseGeoCode(
        @QueryParam("lat") latitude: String?,
        @QueryParam("lon") longitude: String?,
        @QueryParam("format") format: String = "json",
    ): Uni<GeocodeReturnAddressContainer>

}

