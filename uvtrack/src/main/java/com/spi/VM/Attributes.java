package com.spi.VM;

import java.math.BigDecimal;

public class Attributes {

	private BigDecimal speedLimit = new BigDecimal(32.37);// Default 60 Km/hours

	public BigDecimal getSpeedLimit() {
		return speedLimit;
	}

	public void setSpeedLimit(BigDecimal speedLimit) {
		this.speedLimit = speedLimit;
	}

}
