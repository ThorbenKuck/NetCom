package de.thorbenkuck.netcom.cli;

import de.thorbenkuck.netcom.datatypes.interfaces.QueuedAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Ein Comm ist ein, von dem {@link CliParser} verstandener Command.
 * Dieser besteht in Wesentlichen aus 3 Teilen.
 * 1. Dem Identifier.
 * <p>Dieser ist identifizierend für diesen Comm, sollte also eindeutig sein.</p>
 * 2. Der Description.
 * <p>Die ist die Doku des Comm's. Lässt man sich alle Comm's vom {@link CommandLineReader} auflisten, so ist dies die Erklärung.</p>
 * 3. Der QueuedAction.
 * <p>Die {@link QueuedAction} beschreibt, was passieren soll, wenn der Comm ausgeführt wird.</p>
 */
public abstract class Comm  implements Serializable{

	private String identifier;
	private String description;

	@Override
	public String toString() {
		return "Comm{" +
				"identifier='" + identifier + '\'' +
				", description='" + description + '\'' +
				", options=" + options +
				", queuedAction=" + queuedAction +
				'}';
	}

	private ArrayList<Option> options;
	private QueuedAction queuedAction;

	/**
	 * Erstellt einen Comm, ohne Beschreibung.
	 *
	 * @param identifier Der Identifier dieses Comm's
	 * @param queuedAction Die QueuedAction für diesen Comm
	 */
	public Comm (String identifier, QueuedAction queuedAction) {
		this(identifier, queuedAction, "");
	}

	/**
	 * Erstellt einen Vollwertigen Comm.
	 * Ein Comm ist genau dann vollwertig, wenn er einen gesetzte Identifier, eine gesetzte Description und QueuedAction besitzt.
	 *
	 * @param identifier der Identifier dieses Comm's
	 * @param queuedAction die QueuedAction für diesen Comm
	 * @param description Die Beschreibung des Comm's
	 */
	public Comm (String identifier, QueuedAction queuedAction, String description) {
		this.identifier = identifier;
		this.description = description;
		this.queuedAction = queuedAction;
		this.options = new ArrayList<>();
	}

	/**
	 * Hier wird ein Comm ohne QueuedAction erstellt.
	 *
	 * @param identifier der Identifier
	 * @param description die Description
	 */
	public Comm (String identifier, String description) {
		this(identifier, () -> {}, description);
	}

	/**
	 * Der Identifier ist das Identifizierende des Comm's. Er ist der "Befehl" welchen man in eine Eingabe tippt.
	 *
	 * @return den Identifier
	 */
	public String getIdentifier () {
		return identifier;
	}

	/**
	 * Die Discription des Comm's ist die Doku eben jenes. Sollte hier nicht stehen, ist das nicht wild, kann aber verwirren.
	 *
	 * @return die Description
	 */
	public String getDescription () {
		return description;
	}

	/**
	 * Setzt nachträglich eine QueuedAction für diesen Comm.
	 *
	 * @param queuedAction die QueuedAction, welche der Comm ausfürhen soll, wenn {@link #action()} aufgerufen wird.
	 */
	public void setAction(QueuedAction queuedAction) {
		this.queuedAction = queuedAction;
	}

	/**
	 * Gibt alle, für diesen Comm gesetzten Options zurück.
	 * Das setzten übernimmt der CliParser, welcher die Option's aus dem Klartext filtert.
	 *
	 * @return eine Liste mit Options
	 */
	public List<Option> getOptions () {
		return new ArrayList<>(options);
	}

	/**
	 * Fügt eine beliebige Anzahl Options den internen Options hinzu.
	 * Die Options werden hierbei einzelnd durch ein "," getrennt.
	 *
	 * @param option eine bel. Anzahl an Option's
	 */
	public void addOption(Option... option) {
		Collections.addAll(options, option);
	}

	/**
	 * Fügt eine Liste an Options den internen Options hinzu.
	 *
	 * @param options eine Liste, welche intern hinzugefügt werden soll
	 */
	public void addOption(List<Option> options) {
		this.options.addAll(options);
	}

	/**
	 * Setzt intern die Option's auf die neuen, übergebenen Options
	 *
	 * @param options die Options, welche nun intern agehandelt werden sollen.
	 */
	public void setOptions(List<Option> options) {
		this.options = new ArrayList<>(options);
	}

	/**
	 * Löscht die internen Option's und erstetzt sie durch eine leere Liste.
	 */
	public void resetOptions() {
		this.options = new ArrayList<>();
	}

	/**
	 * Bei Aufruf dieser Methode, führt der Comm seine intern gesetzte QueuedAction aus.
	 */
	public void action() {
		queuedAction.doBefore();
		queuedAction.doAction();
		queuedAction.doAfter();
	}
}
