package de.thorbenkuck.netcom.cli;

import de.thorbenkuck.netcom.datatypes.interfaces.Parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse ist eine Klasse, welche genutzt werden kann, um aus Strings, welche in eine "CommandLine" eingegeben
 * wurden, den Befehl und alle Options zu Extrahieren und.
 * Die Befehle müssen dabei registriert werden, damit diese Klasse sie kennt.
 * Optionen werden dann dynamisch geparsed und dem Befehl übergeben.
 * Leider ist es nicht möglich, in dem Konstruktor eines Befehls diese Optionen direkt aus zu lesen, da die effektiv
 * erst NACH dem Aufruf des Konstruktors existieren.
 * Außerdem sollte angemerkt werden, dass diese Klasse absolut nicht für Multi-Threaded-Environments geignet ist.
 * Dafür bedarf es einer Aggregator-Klasse, welche Zufriffe Synchronisiert.
 */
public class CliParser implements Parser<String, Boolean>, Serializable{

	/**
	 * Der eingegebene Text
	 */
	private String text;
	/**
	 * Der Letzte, ausgeführte NetComCommand als String.
	 * Wird gesetzt, wenn der NetComCommand korrekt geparsed wurde und ermöglicht so das Einsetzten von !!
	 */
	private String lastCommand;
	/**
	 * Eine ArrayList mit allen bekannten Commands
	 */
	private static ArrayList<Comm> commands = new ArrayList<>();

	/**
	 * Nimmt einen String und durchläuft ihn, um ihn nach Options zu überprüfen.
	 * Dabei wird eine Liste mit {@see Option}'s zurück gegeben.
	 *
	 * @param command Der NetComCommand, wie er aus dem Eingabe-Feld kam
	 *
	 * @return Alle Optionen, welche sich in dem NetComCommand befinden.
	 */
	public ArrayList<Option> parseAllOptions (String command) {
		ArrayList<Option> options = new ArrayList<>();
		text = command;
		while ( textContainsMoreOptions(text) ) {
			options.add(getNextOption(text));
		}
		return options;
	}

	/**
	 * Parsed die nächste Option aus dem NetComCommand, welcher ihr gegeben wird.
	 *
	 * @param command der NetComCommand, welcher aus einem Eingabe-Feld stammt
	 *
	 * @return die nächste Option
	 */
	public Option getNextOption (String command) {

		text = getTextWithoutIdentifier(command);
		if (textContainsMoreOptions(text)) {
			if (containsMoreOptionPairs(text)) {
				return parseNextOptionPair();
			} else {
				return parseNextBareOption();
			}
		}
		return new Option("", "");
	}

	/**
	 * Der Identifier ist der String, welcher einen Comm eindeutig identifiziert.
	 * Um sauber parsen zu können, schneidet diese Methode den Identifier aus dem NetComCommand
	 *
	 * @param text Der Text, aus welchem der Identifier heraus geschnitten werden soll
	 *
	 * @return der gegebene Text, lediglich ohne den Identifier. Sollte sich allerding kein Identifier in dem Text
	 * befinden, oder der Text keine anderen Anzeichen dafür zeigen, dass er ein richtiger Comm ist, wird der Original
	 * text zurück gegeben
	 */
	private String getTextWithoutIdentifier (String text) {
		if (textWithIdentifier(text)) {
			if (textContainsMoreOptions(text)) {
				text = text.substring(text.indexOf(" ") + 1);
			}
		}
		return text;
	}

	/**
	 * Eine BareOption ist ein Option, welche keinen hinterlegten Parameter hat.
	 * Dabei ist wichtig, dass die nächste Option eine BareOption ist, ansonsten wir der Parameter des Option ignoriert.
	 *
	 * @return die nächste BareOption aus dem Text.
	 */
	private Option parseNextBareOption () {
		if (containsMoreBareOptions(text)) {
			String textToReturn = text.substring(0, text.contains(" ") ? text.indexOf(" ") : text.length());
			textToReturn = textToReturn.replace("-", "");
			deleteNextStringPart();
			return new Option(textToReturn, "");
		}
		return new Option("", "");
	}

	/**
	 * Parsed das nächste OptionPair. Ein OptionPair ist dabei eine BareOption gefolgt von einem Parameter.
	 * Dabei ist wichtig, dass die nächste Option eine PairOption ist, ansonsten kann es zu Problemen kommen.
	 *
	 * @return das nächste OptionPair
	 */
	private Option parseNextOptionPair () {
		if (containsMoreOptionPairs(text)) {
			String textToReturn1 = text.substring(0, text.contains(" ") ? text.indexOf(" ") : text.length());
			textToReturn1 = textToReturn1.replace("-", "");
			deleteNextStringPart();
			String textToReturn2 = text.substring(0, text.contains(" -") ? text.indexOf(" -") : text.length());
			deleteNextStringPart();
			return new Option(textToReturn1, textToReturn2);
		}
		return new Option("", "");
	}

	/**
	 * Löscht den nächsten Teil des Strings. Dabei wird der neu entstandene Text zurück gegeben.
	 *
	 * @return den neu entstandenen String.
	 */
	public String deleteNextStringPart () {
		int start = text.contains(" ") ? text.indexOf(" ") + 1 : (text.contains(" -") ? text.indexOf(" -") + 1 : text.length());
		if (text.length() >= start) {
			text = text.substring(start);
		}
		return text;
	}

	/**
	 * Gibt zurück, ob der text einen Comm Identifiern beinhaltet
	 *
	 * @param text Der text, welcher überprüft werden soll
	 *
	 * @return true, wenn Identifier in dem Text ansonsten false
	 */
	public boolean textWithIdentifier (String text) {
		return !text.startsWith("-");
	}

	/**
	 * Gibt zurück, ob der String weitere, potentielle Optionen enthält
	 *
	 * @param text der Text, welcher überprüft werden soll
	 *
	 * @return true, wenn möglicherweise weiter Optionen im Text sind, sonst false
	 */
	public boolean textContainsMoreOptions (String text) {
		return text.contains("-");
	}

	/**
	 * Gibt zurück, ob der Text weitere OptionPairs enthält.
	 *
	 * @param text der Text, welcher überprüft werden soll
	 *
	 * @return true, wenn mehr OptionPairs im Text sind, sonst false.
	 */
	public boolean containsMoreOptionPairs (String text) {
		return text.contains(" ") && !containsMoreBareOptions(text);
	}

	/**
	 * Gibt zurück, ob der Text weitere BareOptions enthällt
	 *
	 * @param text der Text, welcher überprüft werden soll
	 *
	 * @return true, wenn mehre BareOptions im Text sind, sonst false.
	 */
	public boolean containsMoreBareOptions (String text) {
		return !text.contains(" ") || text.substring(text.indexOf(" ")).startsWith("-");
	}

	/**
	 * Registriert einen neuen NetComCommand, welcher dem CliParser bekannt gemacht werden soll.
	 * Damit hier keine Verwirrung bei den Namens-Konventionen entstehen, heißt die Methode send();
	 *
	 * @param comm der Comm, welcher dem CliParser hinzugefügt werden soll.
	 */
	public static void addCommand (Comm comm) {
		commands.add(comm);
	}

	/**
	 * Gibt alle registrierten Comm's zurück.
	 * Da ich hier verhindern möchte, dass jemand von außen diese Liste Manipuliert, gebe ich eine neue Instanz zurück
	 *
	 * @return Eine Liste mit allen Comm's
	 */
	public static List<Comm> getRegisteredCommands () {
		return new ArrayList<>(commands);
	}

	/**
	 * Diese Methode ist eine Schnittstelle um einen String zu übergeben und die korrespondierende Methode zu parsen und
	 * direkt aus zu führen.
	 * Dabei werden alle Options geparsed und der NetComCommand ausgeführt, sofern er in der Liste ist.
	 *
	 * @param enteredText Der Text, direkt aus der Eingabe des Nutzers.
	 */
	public synchronized Boolean parse (String enteredText) {
		try {
			enteredText = preParse(enteredText);
			for ( Comm c : commands ) {
				try {
					if (enteredText.equals(c.getIdentifier()) || enteredText. substring(0,enteredText.indexOf(' ')).equals(c.getIdentifier())) {
						c.setOptions(parseAllOptions(enteredText));
						c.action();
						return true;
					}
				} catch (IndexOutOfBoundsException ignore) {
				}
			}
			CommandLineReader.printMessage("Unknown command: " + enteredText);
			return false;
		} finally {
			lastCommand = enteredText;
		}
	}

	@Override
	public String toString() {
		return "CliParser{" +
				"text='" + text + '\'' +
				", lastCommand='" + lastCommand + '\'' +
				'}';
	}

	/**
	 * Hier können Routinen ausgeführt werden, bevor der Text tatsächlich geparsed wird.
	 * Es ist empfehlens-wert, wenn mehr als nur !! gewünscht ist, dieses vielleicht zu abstrahieren und ähnlich wie die
	 * Comm's von außen zugänglich machen.
	 *
	 * @param enteredText der Eingegebene Text
	 *
	 * @return der Text, nach dem er preParsed wurde.
	 */
	private String preParse (String enteredText) {
		String toReturn = enteredText;
		if (enteredText.contains("!!")) {
			toReturn = enteredText.replace("!!", lastCommand);
			CommandLineReader.printMessage(toReturn, false);
		}
		return toReturn;
	}

	/**
	 * Gibt den letzten, geparsten NetComCommand zurück.
	 *
	 * @return den Letzten eingegebenen Comm als String.
	 */
	public String getLastCommand () {
		return lastCommand;
	}
}
