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
 * Start time:13:31:04 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;

/**
 * Start time:13:31:04 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CallingPartyCategoryImpl extends AbstractISUPParameter implements CallingPartyCategory {

	private byte callingPartyCategory = 0;

	public CallingPartyCategoryImpl(byte callingPartyCategory) {
		super();
		this.callingPartyCategory = callingPartyCategory;
	}

	public CallingPartyCategoryImpl() {
		super();

	}

	public CallingPartyCategoryImpl(byte[] representation) throws ParameterException {
		super();
		this.decode(representation);
	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length != 1) {
			throw new ParameterException("byte[] must not be null or have different size than 1");
		}
		this.callingPartyCategory = b[0];

		return 1;
	}

	public byte[] encode() throws ParameterException {

		return new byte[] { this.callingPartyCategory };
	}

	public int encode(ByteArrayOutputStream bos) throws ParameterException {
		bos.write(this.callingPartyCategory);
		return 1;
	}

	public byte getCallingPartyCategory() {
		return callingPartyCategory;
	}

	public void setCallingPartyCategory(byte callingPartyCategory) {
		this.callingPartyCategory = callingPartyCategory;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
