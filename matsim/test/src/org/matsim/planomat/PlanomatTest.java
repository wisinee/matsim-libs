/* *********************************************************************** *
 * project: org.matsim.*
 * PlanOptimizeTimesTest.java
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

package org.matsim.planomat;

import org.apache.log4j.Logger;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.IntegerGene;
import org.matsim.api.basic.v01.Id;
import org.matsim.api.core.v01.ScenarioImpl;
import org.matsim.core.api.population.Person;
import org.matsim.core.api.population.Plan;
import org.matsim.core.api.population.Population;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.config.Config;
import org.matsim.core.events.Events;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.population.PopulationImpl;
import org.matsim.core.population.PopulationWriter;
import org.matsim.core.router.costcalculators.TravelTimeDistanceCostCalculator;
import org.matsim.core.router.util.TravelCost;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.scoring.CharyparNagelScoringFunctionFactory;
import org.matsim.core.scoring.ScoringFunctionFactory;
import org.matsim.core.trafficmonitoring.TravelTimeCalculator;
import org.matsim.core.utils.misc.CRCChecksum;
import org.matsim.planomat.costestimators.CetinCompatibleLegTravelTimeEstimator;
import org.matsim.planomat.costestimators.CharyparEtAlCompatibleLegTravelTimeEstimator;
import org.matsim.planomat.costestimators.DepartureDelayAverageCalculator;
import org.matsim.planomat.costestimators.LegTravelTimeEstimator;
import org.matsim.planomat.costestimators.LinearInterpolatingTTCalculator;
import org.matsim.population.algorithms.PlanAnalyzeSubtours;
import org.matsim.testcases.MatsimTestCase;

public class PlanomatTest extends MatsimTestCase {

	private enum PlanomatTestRun {NOEVENTS_CAR, WITHEVENTS_CAR, NOEVENTS_CAR_PT, WITHEVENTS_CAR_PT;}

	private final static Id TEST_PERSON_ID = new IdImpl("100");
	
	private static final Logger log = Logger.getLogger(PlanomatTest.class);

	private ScenarioImpl scenario;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Config config = super.loadConfig(this.getClassInputDirectory() + "config.xml");
		config.plans().setInputFile(this.getPackageInputDirectory() + "testPlans.xml");
		if (this.getName().equals("testRunDefaultManyModes")) {
			config.plans().setInputFile(this.getInputDirectory() + "input_plans.xml.gz");
		}
		this.scenario = new ScenarioImpl(config);
	}

	public void testRunDefault() {
		this.runATestRun(PlanomatTestRun.NOEVENTS_CAR);
	}

	public void testRunDefaultWithEvents() {
		this.runATestRun(PlanomatTestRun.WITHEVENTS_CAR);
	}

	public void testRunCarPt() {
		this.runATestRun(PlanomatTestRun.NOEVENTS_CAR_PT);
	}

	public void testRunCarPtWithEvents() {
		this.runATestRun(PlanomatTestRun.WITHEVENTS_CAR_PT);
	}

	public void testRunDefaultManyModes() {
		this.runATestRun(PlanomatTestRun.NOEVENTS_CAR);
	}

	private void runATestRun(final PlanomatTestRun testRun) {

		TravelTimeCalculator tTravelEstimator = new TravelTimeCalculator(this.scenario.getNetwork(), 900);
		TravelCost travelCostEstimator = new TravelTimeDistanceCostCalculator(tTravelEstimator, this.scenario.getConfig().charyparNagelScoring());
		DepartureDelayAverageCalculator depDelayCalc = new DepartureDelayAverageCalculator(this.scenario.getNetwork(), 900);

		Events events = new Events();
		events.addHandler(tTravelEstimator);
		events.addHandler(depDelayCalc);

		LegTravelTimeEstimator ltte = new CetinCompatibleLegTravelTimeEstimator(tTravelEstimator, travelCostEstimator, depDelayCalc, this.scenario.getNetwork());
		ScoringFunctionFactory scoringFunctionFactory = new CharyparNagelScoringFunctionFactory(this.scenario.getConfig().charyparNagelScoring());

		Planomat testee = new Planomat(ltte, scoringFunctionFactory);
		testee.getSeedGenerator().setSeed(this.scenario.getConfig().global().getRandomSeed());

		log.info("Testing " + testRun.toString() + "...");

		if (
				PlanomatTestRun.NOEVENTS_CAR_PT.equals(testRun) ||
				PlanomatTestRun.WITHEVENTS_CAR_PT.equals(testRun)) {

			this.scenario.getConfig().planomat().setPossibleModes("car,pt");
		}

		tTravelEstimator.resetTravelTimes();
		depDelayCalc.resetDepartureDelays();
		if (
				PlanomatTestRun.WITHEVENTS_CAR.equals(testRun) ||
				PlanomatTestRun.WITHEVENTS_CAR_PT.equals(testRun)) {

			new MatsimEventsReader(events).readFile(this.getClassInputDirectory() + "equil-times-only-1000.events.txt.gz");

		}

		// init test Plan
		
		final int TEST_PLAN_NR = 0;

		// first person
		Person testPerson = this.scenario.getPopulation().getPersons().get(TEST_PERSON_ID);
		// only plan of that person
		Plan testPlan = testPerson.getPlans().get(TEST_PLAN_NR);

		// actual test
		testee.run(testPlan);

		// write out the test person and the modified plan into a file
		Population outputPopulation = new PopulationImpl();
		outputPopulation.addPerson(testPerson);

		log.info("Writing plans file...");
		PopulationWriter plans_writer = new PopulationWriter(outputPopulation, this.getOutputDirectory() + "output_plans.xml.gz", "v4");
		plans_writer.write();
		log.info("Writing plans file...DONE.");

		// actual test: compare checksums of the files
		final long expectedChecksum = CRCChecksum.getCRCFromFile(this.getInputDirectory() + "plans.xml.gz");
		final long actualChecksum = CRCChecksum.getCRCFromFile(this.getOutputDirectory() + "output_plans.xml.gz");
		assertEquals("different plans files.", expectedChecksum, actualChecksum);

		log.info("Testing " + testRun.toString() + "...done.");
	}

	public void testInitSampleChromosome() {

		// init test Plan
		final int TEST_PLAN_NR = 0;

		// first person
		Person testPerson = this.scenario.getPopulation().getPersons().get(TEST_PERSON_ID);
		// only plan of that person
		Plan testPlan = testPerson.getPlans().get(TEST_PLAN_NR);

		Configuration jgapConfiguration = new Configuration();

		IChromosome testChromosome = null;

		Planomat testee = new Planomat(null, null);

		PlanAnalyzeSubtours planAnalyzeSubtours = new PlanAnalyzeSubtours();
		planAnalyzeSubtours.run(testPlan);

		testChromosome = testee.initSampleChromosome(testPlan, planAnalyzeSubtours, jgapConfiguration);
		assertEquals(2, testChromosome.getGenes().length);
		assertEquals(IntegerGene.class, testChromosome.getGenes()[0].getClass());
		assertEquals(IntegerGene.class, testChromosome.getGenes()[1].getClass());

	}

	public void testWriteChromosome2Plan() {

		// writeChromosome2Plan() has 3 arguments:
		Plan testPlan = null;
		IChromosome testChromosome = null;
		LegTravelTimeEstimator ltte = null;

		// init test Plan
		final int TEST_PLAN_NR = 0;

		// first person
		Person testPerson = this.scenario.getPopulation().getPersons().get(TEST_PERSON_ID);
		// only plan of that person
		testPlan = testPerson.getPlans().get(TEST_PLAN_NR);

		// init IChromosome (from JGAP)
		PlanAnalyzeSubtours planAnalyzeSubtours = new PlanAnalyzeSubtours();
		planAnalyzeSubtours.run(testPlan);
		int numActs = planAnalyzeSubtours.getSubtourIndexation().length;

		Configuration jgapConfiguration = new Configuration();

		try {
			Gene[] testGenes = new Gene[numActs + planAnalyzeSubtours.getNumSubtours()];

			Integer i31 = Integer.valueOf(31);
			Integer i32 = Integer.valueOf(32);
			Integer i0 = Integer.valueOf(0);
			
			for (int ii=0; ii < testGenes.length; ii++) {
				switch(ii) {
				case 0:
					testGenes[ii] = new IntegerGene(jgapConfiguration);
					testGenes[ii].setAllele(i31);
					break;
				case 1:
					testGenes[ii] = new IntegerGene(jgapConfiguration);
					testGenes[ii].setAllele(i32);
					break;
				case 2:
					testGenes[ii] = new IntegerGene(jgapConfiguration);
					testGenes[ii].setAllele(i0);
					break;
				}

			}

			testChromosome = new Chromosome(jgapConfiguration, testGenes);

		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

		// init LegTravelTimeEstimator
		TravelTime tTravelEstimator = new LinearInterpolatingTTCalculator(this.scenario.getNetwork(), 900);
		TravelCost travelCostEstimator = new TravelTimeDistanceCostCalculator(tTravelEstimator, this.scenario.getConfig().charyparNagelScoring());
		DepartureDelayAverageCalculator depDelayCalc = new DepartureDelayAverageCalculator(this.scenario.getNetwork(), 900);
		ltte = new CharyparEtAlCompatibleLegTravelTimeEstimator(tTravelEstimator, travelCostEstimator, depDelayCalc, this.scenario.getNetwork());

		// run the method
		Planomat testee = new Planomat(ltte, null);

		testee.writeChromosome2Plan(testChromosome, testPlan, planAnalyzeSubtours);

		// write out the test person and the modified plan into a file
		Population outputPopulation = new PopulationImpl();
		outputPopulation.addPerson(testPerson);

		System.out.println("Writing plans file...");
		PopulationWriter plans_writer = new PopulationWriter(outputPopulation, this.getOutputDirectory() + "output_plans.xml.gz", "v4");
		plans_writer.write();
		System.out.println("Writing plans file...DONE.");

		// actual test: compare checksums of the files
		final long expectedChecksum = CRCChecksum.getCRCFromFile(this.getInputDirectory() + "plans.xml.gz");
		final long actualChecksum = CRCChecksum.getCRCFromFile(this.getOutputDirectory() + "output_plans.xml.gz");
		log.info("Expected checksum: " + Long.toString(expectedChecksum));
		log.info("Actual checksum: " + Long.toString(actualChecksum));
		assertEquals(expectedChecksum, actualChecksum);

	}


	@Override
	protected void tearDown() throws Exception {
		this.scenario = null;
		super.tearDown();
	}

}
