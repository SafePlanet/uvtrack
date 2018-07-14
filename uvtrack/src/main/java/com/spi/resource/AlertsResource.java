/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.resource;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.stereotype.Component;

import com.spi.domain.Alert;
import com.spi.domain.PickOrDropRequest;
import com.spi.service.AlertsService;
import com.spi.service.UserService;
import com.spi.user.api.ExternalUser;
import com.spi.user.api.PickUpRequestModel;

/**
 */
@Path("/alerts")
@Component
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class AlertsResource {

	private ConnectionFactoryLocator connectionFactoryLocator;

	@Autowired
	protected AlertsService alertsService;

	@Autowired
	protected UserService userService;

	@Context
	protected UriInfo uriInfo;

	public AlertsResource() {
	}

	@Autowired
	public AlertsResource(ConnectionFactoryLocator connectionFactoryLocator) {
		this.connectionFactoryLocator = connectionFactoryLocator;
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher", "transporter", "superAdmin","gateKeeper"})
	@Path("{userId}")
	@GET
	public Response getPickAlert(@Context SecurityContext sc, @PathParam("userId") String userId) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		Pageable topTen = new PageRequest(0, 10);
		String userRole = userService.getUserRole(userId).name();
		Page<PickOrDropRequest> pickalert = null;
		if (userRole.equals("administrator")) {
			pickalert = alertsService.getpickAlerts(userMakingRequest.getPkId(), topTen);

		}
		return Response.ok().entity(pickalert).build();
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher", "transporter", "superAdmin","gateKeeper" })
	@Path("getAllAlerts/{userId}")
	@GET
	public Response getAllAlerts(@Context SecurityContext sc, @PathParam("userId") String userId) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		List<Alert> alerts = alertsService.getAllAlerts(userMakingRequest.getPkId());
		return Response.ok().entity(alerts).build();
	}

	@RolesAllowed({ "authenticated", "administrator", })
	@Path("getPickUpAlert/{userId}")
	@GET
	public Response getPickUpAlert(@Context SecurityContext sc, @PathParam("userId") String userId) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		List<PickUpRequestModel> alerts = alertsService.getPickUpAlerts(userMakingRequest.getPkId());
		return Response.ok().entity(alerts).build();
	}

}
