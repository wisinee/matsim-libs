/*
 * *********************************************************************** *
 * project: org.matsim.*
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2019 by the members listed in the COPYING,        *
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
 * *********************************************************************** *
 */

package org.matsim.core.config.consistency;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.utils.collections.Tuple;

/**
 * @author Michal Maciejewski (michalm)
 */
public class BeanValidationConfigConsistencyCheckerTest {

	@Test
	public void checkConsistency() {
		Assertions.assertThat(getViolationTuples(new Config())).isEmpty();

		Config config = ConfigUtils.createConfig();
		Assertions.assertThat(getViolationTuples(config)).isEmpty();

		config.qsim().setFlowCapFactor(0);
		Assertions.assertThat(getViolationTuples(config))
				.containsExactlyInAnyOrder(Tuple.of("floswCapFactor", Positive.class));
		config.qsim().setFlowCapFactor(1);

		config.qsim().setSnapshotPeriod(-1);
		Assertions.assertThat(getViolationTuples(config))
				.containsExactlyInAnyOrder(Tuple.of("snapshotPeriod", PositiveOrZero.class));
		config.qsim().setSnapshotPeriod(0);

		config.qsim().setFlowCapFactor(0);
		config.qsim().setSnapshotPeriod(-1);
		Assertions.assertThat(getViolationTuples(config))
				.containsExactlyInAnyOrder(Tuple.of("flowCapFactor", Positive.class),
						Tuple.of("snapshotPeriod", PositiveOrZero.class));
	}

	private Tuple<String, Class<? extends Annotation>> violationTuples(ConstraintViolation<?> violation) {
		return Tuple.of(violation.getPropertyPath().toString(),
				violation.getConstraintDescriptor().getAnnotation().annotationType());
	}

	private List<Tuple<String, Class<? extends Annotation>>> getViolationTuples(Config config) {
		try {
			new BeanValidationConfigConsistencyChecker().checkConsistency(config);
			return Collections.emptyList();
		} catch (ConstraintViolationException e) {
			return e.getConstraintViolations().stream().map(this::violationTuples).collect(Collectors.toList());
		}
	}
}
