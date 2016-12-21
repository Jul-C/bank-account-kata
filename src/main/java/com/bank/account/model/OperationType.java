package com.bank.account.model;

public enum OperationType {
	DEPOSIT(OperationSign.POSITIVE),
	WITHDRAWAL(OperationSign.NEGATIVE);
	
	private enum OperationSign { POSITIVE, NEGATIVE; }

	private OperationSign sign;
	
	private OperationType(OperationSign sign) {
		this.sign = sign;
	}
	
	public int getMultiplcator() {
		if (OperationSign.NEGATIVE.equals(sign)) {
			return -1;
		}
		return 1;
	}

}
