/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "message_template")
public class MessageTemplate extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 8744254379814473440L;

	String name = null;
	String sampleMessage = null;
	char userInitiated;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSampleMessage() {
		return sampleMessage;
	}

	public void setSampleMessage(String sampleMessage) {
		this.sampleMessage = sampleMessage;
	}

	public char getUserInitiated() {
		return userInitiated;
	}

	public void setUserInitiated(char userInitiated) {
		this.userInitiated = userInitiated;
	}
}
