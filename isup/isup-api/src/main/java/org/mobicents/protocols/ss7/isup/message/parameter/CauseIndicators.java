/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 * Start time:12:11:30 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:11:30 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CauseIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x12;

	
	/**
	 * See Q.850
	 */
	public static final int _CODING_STANDARD_ITUT = 0;

	/**
	 * See Q.850
	 */
	public static final int _CODING_STANDARD_IOS_IEC = 1;

	/**
	 * See Q.850
	 */
	public static final int _CODING_STANDARD_NATIONAL = 2;
	/**
	 * See Q.850
	 */
	public static final int _CODING_STANDARD_SPECIFIC = 3;
	

	/**
	 * See Q.850
	 */
	public static final int _LOCATION_USER = 0;

	/**
	 * See Q.850 private network serving the local user (LPN)
	 */
	public static final int _LOCATION_PRIVATE_NSLU = 1;

	/**
	 * See Q.850 public network serving the local user (LN)
	 */
	public static final int _LOCATION_PUBLIC_NSLU = 2;

	/**
	 * See Q.850 transit network (TN)
	 */
	public static final int _LOCATION_TRANSIT_NETWORK = 3;

	/**
	 * See Q.850 private network serving the remote user (RPN)
	 */
	public static final int _LOCATION_PRIVATE_NSRU = 5;

	/**
	 * See Q.850 public network serving the remote user (RLN)
	 */
	public static final int _LOCATION_PUBLIC_NSRU = 4;
	/**
	 * See Q.850
	 */
	public static final int _LOCATION_INTERNATIONAL_NETWORK = 7;

	/**
	 * See Q.850 network beyond interworking point (BI)
	 */
	public static final int _LOCATION_NETWORK_BEYOND_IP = 10;

	
	//cause values
	public static final int _CV_UNALLOCATED = 1;
	
	public static final int _CV_NO_ROUTE_TO_TRANSIT_NET = 2;
	
	public static final int _CV_NO_ROUTE_TO_DEST = 3;
	
	public static final int _CV_SEND_SPECIAL_TONE = 4;
	
	public static final int _CV_MISDIALED_TRUNK_PREFIX = 5;
	
	public static final int _CV_ALL_CLEAR = 16;
	
	public static final int _CV_USER_BUSY = 17;
	
	public static final int _CV_NO_USER_RESPONSE = 18;
	
	public static final int _CV_NO_ANSWER = 19;
	
	public static final int _CV_SUBSCRIBER_ABSENT = 20;
	
	public static final int _CV_CALL_REJECTED = 21;
	
	public static final int _CV_NUMBER_CHANGED = 22;
	
	public static final int _CV_REJECTED_DUE_TO_ACR_SUPP_SERVICES = 24;
	
	public static final int _CV_EXCHANGE_ROUTING_ERROR = 25;
	
	public static final int _CV_DESTINATION_OUT_OF_ORDER = 27;
	
	public static final int _CV_ADDRESS_INCOMPLETE = 28;
	
	public static final int _CV_FACILITY_REJECTED = 29;
	
	public static final int _CV_NORMAL_UNSPECIFIED = 31;
	
	public static final int _CV_NO_CIRCUIT_AVAILABLE = 34;
	
	public static final int _CV_NETWORK_OUT_OF_ORDER = 38;
	
	public static final int _CV_TEMPORARY_FAILURE = 41;
	
	public static final int _CV_SWITCH_EQUIPMENT_CONGESTION = 42;
	
	public static final int _CV_USER_INFORMATION_DISCARDED = 43;
	
	public static final int _CV_REQUESTED_CIRCUIT_UNAVAILABLE = 44;
	
	public static final int _CV_PREEMPTION = 47;
	
	public static final int _CV_RESOURCE_UNAVAILABLE = 47;
	
	public static final int _CV_FACILITY_NOT_SUBSCRIBED = 50;
	
	public static final int _CV_INCOMING_CALL_BARRED_WITHIN_CUG = 55;
	
	public static final int _CV_BEARER_CAPABILITY_NOT_AUTHORIZED = 57;
	
	public static final int _CV_BEARER_CAPABILITY_NOT_AVAILABLE = 58;
	
	public static final int _CV_SERVICE_OR_OPTION_NOT_AVAILABLE = 63;
	
	public static final int _CV_BEARER_CAPABILITY_NOT_IMPLEMENTED = 65;
	
	public static final int _CV_FACILITY_NOT_IMPLEMENTED = 69;
	
	public static final int _CV_RESTRICTED_DIGITAL_BEARED_AVAILABLE = 70;
	
	public static final int _CV_SERVICE_OR_OPTION_NOT_IMPLEMENTED = 79;
	
	public static final int _CV_INVALID_CALL_REFERENCE = 81;
	
	public static final int _CV_CALLED_USER_NOT_MEMBER_OF_CUG = 87;
	
	public static final int _CV_INCOMPATIBLE_DESTINATION = 88;
	
	public static final int _CV_INVALID_TRANSIT_NETWORK_SELECTION = 91;
	
	public static final int _CV_INVALID_MESSAGE_UNSPECIFIED = 95;
	
	public static final int _CV_MANDATORY_ELEMENT_MISSING = 96;
	
	public static final int _CV_MESSAGE_TYPE_NON_EXISTENT = 97;
	
	public static final int _CV_PARAMETER_NON_EXISTENT_DISCARD = 99;
	
	public static final int _CV_INVALID_PARAMETER_CONTENT = 100;
	
	public static final int _CV_TIMEOUT_RECOVERY = 102;
	
	public static final int _CV_PARAMETER_NON_EXISTENT_PASS_ALONG = 103;
	
	public static final int _CV_MESSAGE_WITH_UNRECOGNIZED_PARAMETER_DISCARDED = 110;
	
	public static final int _CV_PROTOCOL_ERROR = 111;		
	
	public static final int _CV_INTERNETWORKING_UNSPECIFIED = 127;
	
	public int getCodingStandard();

	public void setCodingStandard(int codingStandard);

	public int getLocation();

	public void setLocation(int location);

	public int getCauseValue();

	public void setCauseValue(int causeValue);

	public byte[] getDiagnostics();

	public void setDiagnostics(byte[] diagnostics);

}
