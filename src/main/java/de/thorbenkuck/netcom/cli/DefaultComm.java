package de.thorbenkuck.netcom.cli;

import de.thorbenkuck.netcom.datatypes.interfaces.QueuedAction;

import java.io.Serializable;

/**
 * Diese Klasse stelle eine Implementierung der Comm's da, damit diese ohne weiter konkrete Implementierung des Comm implementier werden können.
 * Dabei werden die selben 3 Konstruktoren wie im Comm bereit gestellt.
 * @see Comm
 * {@inheritDoc}
 */
public class DefaultComm extends Comm  implements Serializable{
	/**
	 * {@inheritDoc}
	 */
	public DefaultComm (String identifier, QueuedAction queuedAction) {
		super(identifier, queuedAction);
	}
	/**
	 * {@inheritDoc}
	 */
	public DefaultComm (String identifier, QueuedAction queuedAction, String description) {
		super(identifier, queuedAction, description);
	}
	/**
	 * {@inheritDoc}
	 */
	public DefaultComm (String identifier, String description) {
		super(identifier, description);
	}
}
