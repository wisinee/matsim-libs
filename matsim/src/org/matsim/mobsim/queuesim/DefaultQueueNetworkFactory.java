/* *********************************************************************** *
 * project: org.matsim.*
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

package org.matsim.mobsim.queuesim;

import org.matsim.core.api.network.Link;
import org.matsim.core.api.network.Node;

/**
 * @author dgrether
 */
/*package*/ final class DefaultQueueNetworkFactory implements QueueNetworkFactory<QueueNode, QueueLink> {

	public QueueLink newQueueLink(final Link link, final QueueNetwork queueNetwork, final QueueNode toQueueNode) {
		return new QueueLink(link, queueNetwork, toQueueNode);
	}

	/**
	 * @see org.matsim.mobsim.queuesim.QueueNetworkFactory#newQueueNode(org.matsim.core.api.network.Node, org.matsim.mobsim.queuesim.QueueNetwork)
	 */
	public QueueNode newQueueNode(final Node node, final QueueNetwork queueNetwork) {
		return new QueueNode(node, queueNetwork);
	}

}
