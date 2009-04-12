/* *********************************************************************** *
 * project: org.matsim.*
 * ExtractChoiceSets.java
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

package playground.anhorni.locationchoice.cs.choicesetextractors;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.matsim.api.basic.v01.Id;
import org.matsim.api.basic.v01.TransportMode;
import org.matsim.core.api.network.Link;
import org.matsim.core.api.population.Activity;
import org.matsim.core.api.population.Leg;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.AfterMobsimEvent;
import org.matsim.core.controler.listener.AfterMobsimListener;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.network.NetworkLayer;
import org.matsim.core.router.PlansCalcRoute;

import playground.anhorni.locationchoice.cs.helper.ChoiceSet;
import playground.anhorni.locationchoice.cs.helper.SpanningTree;
import playground.anhorni.locationchoice.cs.helper.ZHFacilities;
import playground.anhorni.locationchoice.cs.helper.ZHFacility;

/**
 * @author anhorni
 */
public class ExtractChoiceSetsRouting extends ChoiceSetExtractor implements AfterMobsimListener  {
	
	private final static Logger log = Logger.getLogger(ExtractChoiceSetsRouting.class);
	private String mode;

	public ExtractChoiceSetsRouting(Controler controler, ZHFacilities facilities, 
			List<ChoiceSet> choiceSets, String mode, int tt) {
		
		super(controler, choiceSets, tt);
		super.facilities = facilities;
		this.mode = mode;	
	}
	
	public void notifyAfterMobsim(final AfterMobsimEvent event) {
		
		if (event.getIteration() < Gbl.getConfig().controler().getLastIteration()) {
			return;
		}
		log.info("Number of ZH facilities " + this.facilities.getNumberOfFacilities());
		log.info("computing " + this.mode + " choice sets...:");
		super.computeChoiceSets();
	}
				
	protected void computeChoiceSet(ChoiceSet choiceSet, SpanningTree spanningTree, String type, 
			Controler controler, int tt) {
					
		// first add chosen facility and compute tt etc.
		ZHFacility chosenFacility = super.facilities.getZhFacilities().get(choiceSet.getChosenFacilityId());
		this.handleFacility(chosenFacility, choiceSet, controler, 0);
				
		Iterator<ZHFacility> facilities_it = this.facilities.getZhFacilities().values().iterator();
		while (facilities_it.hasNext()) {
			ZHFacility facility = facilities_it.next();	
			this.handleFacility(facility, choiceSet, controler, tt);	
		}	
	}
	
	/*
	 * int tt:	0: add it in any case
	 * 			1: check reported travel time budget
	 * 			2: use computed travel time budget to chosen facility
	 */
	
	private void handleFacility(ZHFacility facility, ChoiceSet choiceSet, Controler controler, int tt) {
		NetworkLayer network = controler.getNetwork();
		
		Id linkId = facility.getLinkId();
		
		//--------------------------------------------------
		/*
		 * this is NOT working: 
		 * Link linkBefore = choiceSet.getTrip().getBeforeShoppingAct().getLink(); ...
		 */
		//Link linkBefore = network.getNearestLink(choiceSet.getTrip().getBeforeShoppingAct().getLink().getCenter());
		Link linkBefore = network.getLink(choiceSet.getTrip().getBeforeShoppingAct().getLink().getId());
		Activity fromAct0 = new org.matsim.core.population.ActivityImpl("beforeShop", linkBefore);
		fromAct0.setEndTime(choiceSet.getTrip().getBeforeShoppingAct().getEndTime());
		fromAct0.setCoord(linkBefore.getCoord());
					
		Link link = network.getLink(linkId);
		Activity toAct0 = new org.matsim.core.population.ActivityImpl("shop", link);
		toAct0.setCoord(link.getCoord());
					
		Leg legBefore = computeLeg(fromAct0, toAct0, controler);				
		double travelTimeBeforeShopping = legBefore.getTravelTime();
		
		//--------------------------------------------------			
		Activity fromAct1 = new org.matsim.core.population.ActivityImpl(toAct0.getType(), toAct0.getLink());
		double endTime = choiceSet.getTrip().getBeforeShoppingAct().getEndTime() + 
		travelTimeBeforeShopping +
		choiceSet.getTrip().getShoppingAct().calculateDuration();			
		fromAct1.setEndTime(endTime);
		fromAct1.setCoord(toAct0.getCoord());
					
		//Link linkAfter = network.getNearestLink(choiceSet.getTrip().getAfterShoppingAct().getLink().getCenter());
		Link linkAfter = network.getLink(choiceSet.getTrip().getAfterShoppingAct().getLink().getId());
		Activity toAct1 = new org.matsim.core.population.ActivityImpl("afterShop", linkAfter);
		toAct1.setCoord(linkAfter.getCoord());
					
		Leg legAfter = computeLeg(fromAct1, toAct1, controler);	
		double travelTimeAfterShopping = legAfter.getTravelTime();
		//--------------------------------------------------
		
		double totalTravelTime = travelTimeBeforeShopping + travelTimeAfterShopping;	
		
		/*
		 * This is NOT working: legBefore.getRoute().getDist() + legAfter.getRoute().getDist()
		 */
		double totalTravelDist = 0.0;
		
		Iterator<Id> routeLinkBefore_it = legBefore.getRoute().getLinkIds().iterator();
		while (routeLinkBefore_it.hasNext()) {		
			Id lId = routeLinkBefore_it.next();
			totalTravelDist += network.getLink(lId).getLength();
		}
		
		Iterator<Id> routeLinkAfter_it = legAfter.getRoute().getLinkIds().iterator();
		while (routeLinkAfter_it.hasNext()) {		
			Id lId = routeLinkAfter_it.next();
			totalTravelDist += network.getLink(lId).getLength();
		}
		
		// chosen facility
		if (tt == 0) {
			choiceSet.addFacility(facility, totalTravelTime, totalTravelDist);
		}
		else if (tt == 1) {
			if (totalTravelTime <= choiceSet.getTravelTimeBudget() * 1.5) {			
				choiceSet.addFacility(facility, totalTravelTime, totalTravelDist);
			}
		}
		else if (tt == 2) {
			if (totalTravelTime <= 1.5 * choiceSet.getTravelTimeStartShopEnd(choiceSet.getChosenFacilityId())) {			
				choiceSet.addFacility(facility, totalTravelTime, totalTravelDist);
			}
		}	
	}
	
	
	private Leg computeLeg(Activity fromAct, Activity toAct, Controler controler) {	
		Leg leg = new org.matsim.core.population.LegImpl(TransportMode.car);
		PlansCalcRoute router = (PlansCalcRoute)controler.getRoutingAlgorithm();
		router.handleLeg(leg, fromAct, toAct, fromAct.getEndTime());
		
		return leg;
	}
}


/* not using spanning tree at the moment: 
 * 
 * protected void computeChoiceSet(ChoiceSet choiceSet, SpanningTree spanningTree, String type, 
			Controler controler) {
	
		NetworkLayer network = controler.getNetwork();
				
		spanningTree.setOrigin(network.getNearestNode(choiceSet.getTrip().getBeforeShoppingAct().getCoord()));
		spanningTree.setDepartureTime(choiceSet.getTrip().getBeforeShoppingAct().getEndTime());
		spanningTree.run(network);
		List<Node> nodesList = new Vector<Node>();
		List<Double> nodesTravelTimesList = new Vector<Double>();					
		spanningTree.getNodesByTravelTimeBudget(choiceSet.getTravelTimeBudget(), nodesList, nodesTravelTimesList);
		
		int index = 0;
		Iterator<Node> nodes_it = nodesList.iterator();
		while (nodes_it.hasNext()) {		
			Node node = nodes_it.next();
			Map<Id, ? extends Link> linksList = node.getIncidentLinks();
			Iterator<? extends Link> links_it = linksList.values().iterator();
			while (links_it.hasNext()) {
				nodesTravelTimesList.get(index);
				Link link = links_it.next();
				// only one link per facility
				
				ArrayList<ZHFacility> facilities = 
					(ArrayList<ZHFacility>)this.zhFacilitiesByLink.get(link.getId());
				if (facilities != null) {
					
					// calculate travel time to after shopping location
					double travelTime2AfterShopingAct = this.getTravelTime(link, nodesTravelTimesList.get(index), 
							controler, choiceSet);
					
					double totalTravelTime = nodesTravelTimesList.get(index) + travelTime2AfterShopingAct;
					
					if (totalTravelTime <= choiceSet.getTravelTimeBudget()) {
						choiceSet.addFacilities(facilities, totalTravelTime);
					}
				}
			}
			index++;
		}		
	}
	
	
	
	
		private double handleWalkLeg(final Leg leg, final Act fromAct, final Act toAct, final double depTime) {
		// make simple assumption about distance and walking speed
		double dist = fromAct.getCoord().calcDistance(toAct.getCoord());
		double speed = 5.0 / 3.6; // 4.0 km/h --> m/s
		// create an empty route, but with realistic travel time
		CarRoute route = new NodeCarRoute(fromAct.getLink(), toAct.getLink());
		int travTime = (int)(dist / speed);
		route.setTravelTime(travTime);
		leg.setRoute(route);
		leg.setDepartureTime(depTime);
		leg.setTravelTime(travTime);
		leg.setArrivalTime(depTime + travTime);
		return travTime;
	}
	
	
 */
