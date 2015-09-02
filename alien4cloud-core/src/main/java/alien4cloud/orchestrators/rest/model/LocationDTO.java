package alien4cloud.orchestrators.rest.model;

import alien4cloud.model.orchestrators.locations.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDTO {

    private Location location;

    private LocationResources resources;
}
