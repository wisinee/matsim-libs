/* *********************************************************************** *
 * project: org.matsim.*
 * PersonAgent.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.core.mobsim.qsim.agents;

import java.util.List;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.mobsim.framework.HasPerson;
import org.matsim.core.mobsim.framework.MobsimAgent;
import org.matsim.core.mobsim.framework.MobsimDriverAgent;
import org.matsim.core.mobsim.framework.MobsimPassengerAgent;
import org.matsim.core.mobsim.framework.PlanAgent;
import org.matsim.core.mobsim.qsim.interfaces.Netsim;
import org.matsim.core.population.routes.NetworkRoute;

/**
 * @author dgrether, nagel
 * <p/>
 * I think this class is reasonable in terms of what is public and/or final and what not.
 */
public class PersonDriverAgentImpl extends BasicPlanAgentImpl implements MobsimDriverAgent, MobsimPassengerAgent, HasPerson, PlanAgent {
	// yy cannot make this final since it is overridden at 65 locations
	// (but since all methods are final, it seems that all of these can be solved by delegation).
	// kai, nov'14

	private static final Logger log = Logger.getLogger(PersonDriverAgentImpl.class);

	private static int expectedLinkWarnCount = 0;

	private Id<Link> cachedNextLinkId = null;

	private int currentLinkIndex = 0 ;

	public PersonDriverAgentImpl(final Plan plan, final Netsim simulation) {
		super(plan, simulation.getScenario(), simulation.getEventsManager(), simulation.getSimTimer()) ;
		// deliberately does NOT keep a back pointer to the whole Netsim; this should also be removed in the constructor call.
	}

	@Override
	public final void notifyMoveOverNode(Id<Link> newLinkId) {
		if (expectedLinkWarnCount < 10 && !newLinkId.equals(this.cachedNextLinkId)) {
			log.warn("Agent did not end up on expected link. Ok for within-day replanning agent, otherwise not.  Continuing " +
					"anyway ... This warning is suppressed after the first 10 warnings.") ;
			expectedLinkWarnCount++;
		}
		this.setCurrentLinkId( newLinkId ) ;
		this.currentLinkIndex++ ;
		this.cachedNextLinkId = null; //reset cached nextLink
	}

	/**
	 * Returns the next link the vehicle will drive along.
	 *
	 * @return The next link the vehicle will drive on, or null if an error has happened.
	 */
	@Override
	public final Id<Link> chooseNextLinkId() {
		// To note: there is something really stupid going on here: A vehicle that is at the end of its route and on the destination link will arrive.
		// However, a vehicle that is on the destination link BUT NOT AT THE END OF ITS ROUTE will NOT arrive.  This makes the whole thing
		// very extremely messy.  kai, nov'14

		// Please, let's try, amidst all checking and caching, to have this method return the same thing
		// if it is called several times in a row. Otherwise, you get Heisenbugs.
		// I just fixed a situation where this method would give a warning about a bad route and return null
		// the first time it is called, and happily return a link id when called the second time.  michaz 2013-08

		// Agreed.  One should also not assume that anything here is the result of one consistent design process.  Rather, many people added
		// and removed material as they needed it for their own studies.  Making the whole code more consistent would be highly
		// desirable.  kai, nov'14

		// (1) if there is a cached link id, use that one: 
		if (this.cachedNextLinkId != null && !(this.cachedNextLinkId.equals(this.getCurrentLinkId())) ) {
			// cachedNextLinkId used to be set to null when a leg started.  Now the BasicPlanAgentImpl does not longer have access to cached
			// value.  kai, nov'14

			return this.cachedNextLinkId;
		}

		// (2) routes that are not network routes cannot be interpreted
		if ( ! ( this.getCurrentLeg().getRoute() instanceof NetworkRoute ) ) {
			return null ;
		}

		List<Id<Link>> routeLinkIds = ((NetworkRoute) this.getCurrentLeg().getRoute()).getLinkIds();
		
		// (3) if route has run dry, we essentially return the destination link:
		if (this.currentLinkIndex >= routeLinkIds.size() ) {

			// special case:
			if (this.getCurrentLinkId().equals( this.getDestinationLinkId() )  && this.getCurrentLinkIndex() > routeLinkIds.size()) {
				// this can happen if the last link in a route is a loop link. Don't ask, it can happen in special transit simulation cases... mrieser/jan2014

				// the condition for arrival used to be "route has run dry AND destination link not attached to current link".  now with loop links,
				// this condition is never triggered.  So no wonder that for such cases a special condition was needed.  kai, nov'14
				
				// The special condition may not be necessary any more. kai, nov'14

				return null;
			}

			this.cachedNextLinkId = this.getDestinationLinkId();
			return this.cachedNextLinkId;

		}

		Id<Link> nextLinkId = routeLinkIds.get(this.getCurrentLinkIndex());
		Link currentLink = this.getScenario().getNetwork().getLinks().get(this.getCurrentLinkId());
		Link nextLink = this.getScenario().getNetwork().getLinks().get(nextLinkId);

		// (4) if destination link is connected to current link, we return the destination link: 
		if (currentLink.getToNode().equals(nextLink.getFromNode())) {
			this.cachedNextLinkId = nextLinkId; //save time in later calls, if link is congested
			return this.cachedNextLinkId;
		}

		// (5) if destination link is NOT connected to current link, we return null: 
		log.warn(this + " [no link to next routenode found: routeindex= " + this.getCurrentLinkIndex() + " ]");
		// yyyyyy personally, I would throw some kind of abort event here.  kai, aug'10
		return null;
	}

	@Override
	public final boolean isArrivingOnCurrentLink( ) {
		
		if ( ! ( this.getCurrentLeg().getRoute() instanceof NetworkRoute ) ) {
			// non-network links in the past have always returned true (i.e. "null" to the chooseNextLink question). kai, nov'14
			return true ;
		}

		final int routeLinkIdsSize = ((NetworkRoute) this.getCurrentLeg().getRoute()).getLinkIds().size();

		// the standard condition used to be "route has run dry AND destination link not attached to current link":
		// 2nd condition essentially means "destination link EQUALS current link" but really stupid way of stating this.  Thus
		// changing the second condition for the time being to "being at destination". kai, nov'14
		if ( this.currentLinkIndex >= routeLinkIdsSize && this.getCurrentLinkId().equals( this.getDestinationLinkId() ) ) {
			
			this.currentLinkIndex = 0 ; 
			// (this is not so great; should be done at departure; but there is nothing there to notify the DriverAgent at departure ...  kai, nov'14)

			return true ;
		} else {
			return false ;
		}

	}


	// ============================================================================================================================
	// below there only (package-)private methods or setters/getters

	/**
	 * Some data of the currently simulated Leg is cached to speed up
	 * the simulation. If the Leg changes (for example the Route or
	 * the Destination Link), those cached data has to be reseted.
	 *</p>
	 * If the Leg has not changed, calling this method should have no effect
	 * on the Results of the Simulation!
	 */
	/* package */ final void resetCaches() {

		// moving this method not to WithinDay for the time being since it seems to make some sense to keep this where the internals are
		// known best.  kai, oct'10
		// Compromise: package-private here; making it public in the Withinday class.  kai, nov'10

		this.cachedNextLinkId = null;

		if (this.getCurrentPlanElement() instanceof Leg) {
			if (getCurrentLeg().getRoute() == null) {
				log.error("The agent " + this.getId() + " has no route in its leg. Setting agent state to abort." );
				this.setState(MobsimAgent.State.ABORT) ;
			}
		} else {			
			this.calculateAndSetDepartureTime((Activity) this.getCurrentPlanElement());
		}
	}

	final int getCurrentLinkIndex() {
		return currentLinkIndex;
	}

}
