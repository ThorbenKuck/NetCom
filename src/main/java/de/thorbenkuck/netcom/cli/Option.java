package de.thorbenkuck.netcom.cli;

import java.io.Serializable;

/**
 * Eine Option ist ein Identifier - Parameter paar.
 * Traditionell fängt es mit einem "-" an. Also z.B.:
 * "-<i>identifier</i> <i>parameter</i>"
 * Der Parameter kann dabei auch leer sein.
 */
public class Option  implements Serializable{

	private String optionIdentifier;
	private String parameter;

	@Override
	public String toString() {
		return "Option{" +
				"optionIdentifier='" + optionIdentifier + '\'' +
				", parameter='" + parameter + '\'' +
				'}';
	}

	/**
	 * Erstelle eine Neue Option.
	 * Dafür wird zum einen der Identifier und der Parameter erfordert.
	 * Damit nicht über Refferenzen eine Option später manipuliert werden kann, wird hier kein Setter für die
	 * entsprechenden Parameter des Objects angeboten.
	 *
	 * @param optionIdentifier der Identifier dieser Option
	 * @param parameter        der Parameter dieser Option
	 */
	public Option (String optionIdentifier, String parameter) {
		this.optionIdentifier = optionIdentifier;
		this.parameter = parameter;
	}

	/**
	 * Der Identifier ist zur Unterscheidung unterschiedlicher Optionen da.
	 *
	 * @return den Identifier dieser Instanz.
	 */
	public String getOptionIdentifier () {
		return optionIdentifier;
	}

	/**
	 * Ein Parameter ist als zusätzliche Information für die Identifier unabhängig von Identifier und kann, muss aber
	 * nicht mit angegeben werden.
	 *
	 * @return den Parameter dieser Option
	 */
	public String getParameter () {
		return parameter;
	}
}
