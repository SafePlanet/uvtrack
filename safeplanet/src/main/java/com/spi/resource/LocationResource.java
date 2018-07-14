/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.resource;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.stereotype.Component;

import com.spi.domain.Location;
import com.spi.gateway.EmailServicesGateway;
import com.spi.service.LocationService;
import com.spi.service.VerificationTokenService;

@Path("location")
@Component
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class LocationResource {

	private ConnectionFactoryLocator connectionFactoryLocator;

	@Autowired
	protected LocationService locationService;

	@Autowired
	protected VerificationTokenService verificationTokenService;

	@Autowired
	protected EmailServicesGateway emailServicesGateway;

	@Context
	protected UriInfo uriInfo;
	
	public LocationResource(){}

	@Autowired
	public LocationResource(ConnectionFactoryLocator connectionFactoryLocator) {
		this.setConnectionFactoryLocator(connectionFactoryLocator);
	}

	// @RolesAllowed({ "authenticated" })
	@PermitAll
	@POST
	@Path("/getlocation")
	public Response getFleetLocation(@Context SecurityContext sc, @PathParam("userId") String userId, long routeId) {

		Location location = locationService.getFleetLocation(routeId);
		return Response.ok().entity(location).build();
	}

	public ConnectionFactoryLocator getConnectionFactoryLocator() {
		return connectionFactoryLocator;
	}

	public void setConnectionFactoryLocator(ConnectionFactoryLocator connectionFactoryLocator) {
		this.connectionFactoryLocator = connectionFactoryLocator;
	}

}
