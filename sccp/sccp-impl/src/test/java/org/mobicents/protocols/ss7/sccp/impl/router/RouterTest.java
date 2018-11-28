/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012. 
 * and individual contributors
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

package org.mobicents.protocols.ss7.sccp.impl.router;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;

import javolution.util.FastMap;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartListener;
import org.mobicents.protocols.ss7.mtp.RoutingLabelFormat;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.LongMessageRule;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.Mtp3Destination;
import org.mobicents.protocols.ss7.sccp.Mtp3ServiceAccessPoint;
import org.mobicents.protocols.ss7.sccp.Router;
import org.mobicents.protocols.ss7.sccp.Rule;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpResource;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class RouterTest {

	private SccpAddress primaryAddr1, primaryAddr2;

	private RouterImpl router = null;

	private TestSccpStackImpl testSccpStackImpl = null;

	public RouterTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@BeforeMethod
	public void setUp() throws IOException {
		testSccpStackImpl = new TestSccpStackImpl();

		primaryAddr1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123, GlobalTitle.getInstance(1,
				"333"), 0);
		primaryAddr2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 321, GlobalTitle.getInstance(1,
				"333"), 0);

		// cleans config file
		router = new RouterImpl("RouterTest", testSccpStackImpl);
		router.start();
		router.removeAllResourses();

	}

	@AfterMethod
	public void tearDown() {
		router.removeAllResourses();
		router.stop();
	}

	/**
	 * Test of add method, of class RouterImpl.
	 */
	@Test(groups = { "router", "functional" })
	public void testRouter() throws Exception {
		router.addPrimaryAddress(1, primaryAddr1);
		assertEquals(router.getPrimaryAddresses().size(), 1);

		router.addPrimaryAddress(2, primaryAddr2);
		assertEquals(router.getPrimaryAddresses().size(), 2);

		router.removePrimaryAddress(1);
		SccpAddress pa = router.getPrimaryAddresses().values().iterator().next();
		assertNotNull(pa);
		assertEquals(pa.getSignalingPointCode(), 321);
		assertEquals(router.getPrimaryAddresses().size(), 1);

		assertEquals(router.getBackupAddresses().size(), 0);

		router.addBackupAddress(1, primaryAddr1);
		assertEquals(router.getBackupAddresses().size(), 1);

		router.addBackupAddress(2, primaryAddr2);
		assertEquals(router.getBackupAddresses().size(), 2);

		router.removeBackupAddress(1);
		pa = router.getBackupAddresses().values().iterator().next();
		assertNotNull(pa);
		assertEquals(pa.getSignalingPointCode(), 321);
		assertEquals(router.getBackupAddresses().size(), 1);

		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "123456789"), 0);

		router.addRule(1, RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern, "R", 2, 2);
		assertEquals(router.getRules().size(), 1);

		router.addRule(2, RuleType.Loadshared, LoadSharingAlgorithm.Bit4, pattern, "K", 2, 2);
		assertEquals(router.getRules().size(), 2);

		router.removeRule(2);
		Rule rule = router.getRules().values().iterator().next();
		assertNotNull(rule);
		assertEquals(rule.getRuleType(), RuleType.Solitary);
		assertEquals(router.getRules().size(), 1);

		router.addLongMessageRule(1, 1, 2, LongMessageRuleType.XudtEnabled);
		assertEquals(router.getLongMessageRules().size(), 1);
		router.addLongMessageRule(2, 3, 4, LongMessageRuleType.LudtEnabled);
		assertEquals(router.getLongMessageRules().size(), 2);
		router.removeLongMessageRule(2);
		LongMessageRule lmr = router.getLongMessageRules().values().iterator().next();
		assertNotNull(lmr);
		assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.XudtEnabled);
		assertEquals(router.getLongMessageRules().size(), 1);

		router.addMtp3ServiceAccessPoint(1, 1, 11, 2);
		assertEquals(router.getMtp3ServiceAccessPoints().size(), 1);
		router.addMtp3ServiceAccessPoint(2, 2, 12, 2);
		assertEquals(router.getMtp3ServiceAccessPoints().size(), 2);
		router.removeMtp3ServiceAccessPoint(2);
		Mtp3ServiceAccessPoint sap = router.getMtp3ServiceAccessPoints().values().iterator().next();
		assertNotNull(sap);
		assertEquals(sap.getOpc(), 11);
		assertEquals(router.getLongMessageRules().size(), 1);

		router.addMtp3Destination(1, 1, 101, 110, 0, 255, 255);
		assertEquals(sap.getMtp3Destinations().size(), 1);
		router.addMtp3Destination(1, 2, 111, 120, 0, 255, 255);
		assertEquals(sap.getMtp3Destinations().size(), 2);
		router.removeMtp3Destination(1, 2);
		Mtp3Destination dest = sap.getMtp3Destinations().values().iterator().next();
		assertNotNull(dest);
		assertEquals(dest.getFirstDpc(), 101);
		assertEquals(sap.getMtp3Destinations().size(), 1);
	}

	@Test(groups = { "router", "functional.encode" })
	public void testSerialization() throws Exception {
		router.addPrimaryAddress(1, primaryAddr1);
		router.addPrimaryAddress(2, primaryAddr2);
		router.addBackupAddress(1, primaryAddr1);

		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "123456789"), 0);
		router.addRule(1, RuleType.Loadshared, LoadSharingAlgorithm.Bit4, pattern, "K", 1, 1);

		router.addLongMessageRule(1, 1, 2, LongMessageRuleType.XudtEnabled);
		router.addMtp3ServiceAccessPoint(3, 1, 11, 2);
		router.addMtp3Destination(3, 1, 101, 110, 0, 255, 255);
		router.stop();

		RouterImpl router1 = new RouterImpl(router.getName(), null);
		router1.start();

		Rule rl = router1.getRule(1);
		SccpAddress adp = router1.getPrimaryAddress(2);
		SccpAddress adb = router1.getBackupAddress(1);
		LongMessageRule lmr = router1.getLongMessageRule(1);
		Mtp3ServiceAccessPoint sap = router1.getMtp3ServiceAccessPoint(3);
		Mtp3Destination dst = sap.getMtp3Destination(1);

		assertEquals(rl.getPrimaryAddressId(), 1);
		assertEquals(rl.getSecondaryAddressId(), 1);
		assertEquals(rl.getLoadSharingAlgorithm(), LoadSharingAlgorithm.Bit4);
		assertEquals(adp.getSignalingPointCode(), primaryAddr2.getSignalingPointCode());
		assertEquals(adb.getSignalingPointCode(), primaryAddr1.getSignalingPointCode());
		assertEquals(lmr.getFirstSpc(), 1);
		assertEquals(sap.getMtp3Destinations().size(), 1);
		assertEquals(dst.getLastDpc(), 110);

		router1.stop();
	}

	/**
	 * Test of Ordering.
	 */
	@Test(groups = { "router", "functional.order" })
	public void testOrdering() throws Exception {
		primaryAddr1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123, GlobalTitle.getInstance(1,
				"333/---/4"), 0);
		router.addPrimaryAddress(1, primaryAddr1);

		SccpAddress pattern1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "800/????/9"), 0);
		router.addRule(1, RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern1, "R/K/R", 1, -1);

		// Rule 2
		SccpAddress pattern2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "*"), 0);
		SccpAddress primaryAddr2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
				GlobalTitle.getInstance(1, "-"), 0);
		router.addPrimaryAddress(2, primaryAddr2);

		router.addRule(2, RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern2, "K", 2, -1);

		// Rule 3
		SccpAddress pattern3 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "9/?/9/*"), 0);
		SccpAddress primaryAddr3 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
				GlobalTitle.getInstance(1, "-/-/-/-"), 0);
		router.addPrimaryAddress(3, primaryAddr3);
		router.addRule(3, RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern3, "K/K/K/K", 3, -1);

		// Rule 4
		SccpAddress pattern4 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "80/??/0/???/9"), 0);
		SccpAddress primaryAddr4 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
				GlobalTitle.getInstance(1, "90/-/1/-/7"), 0);
		router.addPrimaryAddress(4, primaryAddr4);
		router.addRule(4, RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern4, "R/K/R/K/R", 4, -1);

		// Rule 5
		SccpAddress pattern5 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "800/?????/9"), 0);
		SccpAddress primaryAddr5 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
				GlobalTitle.getInstance(1, "90/-/7"), 0);
		router.addPrimaryAddress(5, primaryAddr5);
		router.addRule(5, RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern5, "R/K/R", 5, -1);

		// Rule 6
		SccpAddress pattern6 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "123456"), 0);
		SccpAddress primaryAddr6 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
				GlobalTitle.getInstance(1, "-"), 0);
		router.addPrimaryAddress(6, primaryAddr6);
		router.addRule(6, RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern6, "K", 6, -1);

		// Rule 7
		SccpAddress pattern7 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "1234567890"), 0);
		SccpAddress primaryAddr7 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
				GlobalTitle.getInstance(1, "-"), 0);
		router.addPrimaryAddress(7, primaryAddr7);
		router.addRule(7, RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern7, "K", 7, -1);

		// Rule 8

		SccpAddress pattern8 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "999/*"), 0);
		SccpAddress primaryAddr8 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
				GlobalTitle.getInstance(1, "111/-"), 0);
		router.addPrimaryAddress(8, primaryAddr8);
		router.addRule(8, RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern8, "R/K", 8, -1);

		// TEST find rule

		// Rule 6
		SccpAddress calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "123456"), 0);
		Rule rule = router.findRule(calledParty);

		assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
		assertEquals(pattern6, rule.getPattern());
		assertEquals(RuleType.Solitary, rule.getRuleType());
		assertEquals(-1, rule.getSecondaryAddressId());
		assertEquals("K", rule.getMask());

		// Rule 7
		calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
				"1234567890"), 0);
		rule = router.findRule(calledParty);
		assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
		assertEquals(pattern7, rule.getPattern());
		assertEquals(RuleType.Solitary, rule.getRuleType());
		assertEquals(-1, rule.getSecondaryAddressId());
		assertEquals("K", rule.getMask());

		// Rule 1
		calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
				"80012039"), 0);
		rule = router.findRule(calledParty);
		assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
		assertEquals(pattern1, rule.getPattern());
		assertEquals(RuleType.Solitary, rule.getRuleType());
		assertEquals(-1, rule.getSecondaryAddressId());
		assertEquals("R/K/R", rule.getMask());

		// Rule 5
		calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
				"800120349"), 0);
		rule = router.findRule(calledParty);
		assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
		assertEquals(pattern5, rule.getPattern());
		assertEquals(RuleType.Solitary, rule.getRuleType());
		assertEquals(-1, rule.getSecondaryAddressId());
		assertEquals("R/K/R", rule.getMask());

		// Rule 4
		calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
				"801203459"), 0);
		rule = router.findRule(calledParty);
		assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
		assertEquals(pattern4, rule.getPattern());
		assertEquals(RuleType.Solitary, rule.getRuleType());
		assertEquals(-1, rule.getSecondaryAddressId());
		assertEquals("R/K/R/K/R", rule.getMask());

		// Rule 8
		calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
				"999123456"), 0);
		rule = router.findRule(calledParty);
		assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
		assertEquals(pattern8, rule.getPattern());
		assertEquals(RuleType.Solitary, rule.getRuleType());
		assertEquals(-1, rule.getSecondaryAddressId());
		assertEquals("R/K", rule.getMask());

		// Rule 3
		calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
				"919123456"), 0);
		rule = router.findRule(calledParty);
		assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
		assertEquals(pattern3, rule.getPattern());
		assertEquals(RuleType.Solitary, rule.getRuleType());
		assertEquals(-1, rule.getSecondaryAddressId());
		assertEquals("K/K/K/K", rule.getMask());

	}

	private class TestSccpStackImpl implements SccpStack {

		protected FastMap<Integer, Mtp3UserPart> mtp3UserParts = new FastMap<Integer, Mtp3UserPart>();

		TestSccpStackImpl() {
			Mtp3UserPartImpl mtp3UserPartImpl1 = new Mtp3UserPartImpl();
			Mtp3UserPartImpl mtp3UserPartImpl2 = new Mtp3UserPartImpl();

			mtp3UserParts.put(1, mtp3UserPartImpl1);
			mtp3UserParts.put(2, mtp3UserPartImpl2);
		}

		@Override
		public void start() throws IllegalStateException {
			// TODO Auto-generated method stub

		}

		@Override
		public void stop() {
			// TODO Auto-generated method stub

		}

		@Override
		public SccpProvider getSccpProvider() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getPersistDir() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setPersistDir(String persistDir) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setRemoveSpc(boolean removeSpc) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isRemoveSpc() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public SccpResource getSccpResource() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<Integer, Mtp3UserPart> getMtp3UserParts() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Mtp3UserPart getMtp3UserPart(int id) {
			return this.mtp3UserParts.get(id);
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getSstTimerDuration_Min() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getSstTimerDuration_Max() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double getSstTimerDuration_IncreaseFactor() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getZMarginXudtMessage() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getMaxDataMessage() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getReassemblyTimerDelay() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Router getRouter() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private class Mtp3UserPartImpl implements Mtp3UserPart {

		@Override
		public void addMtp3UserPartListener(Mtp3UserPartListener arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public int getMaxUserDataLength(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Mtp3TransferPrimitiveFactory getMtp3TransferPrimitiveFactory() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public RoutingLabelFormat getRoutingLabelFormat() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isUseLsbForLinksetSelection() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeMtp3UserPartListener(Mtp3UserPartListener arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void sendMessage(Mtp3TransferPrimitive arg0) throws IOException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setRoutingLabelFormat(RoutingLabelFormat arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setUseLsbForLinksetSelection(boolean arg0) {
			// TODO Auto-generated method stub

		}

	}
}
