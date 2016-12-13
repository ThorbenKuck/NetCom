package de.thorbenkuck.netcom.datatypes.interfaces;

/**
 * Diese Klasse beschreibt einen Parser, welcher etwas von einem in ein anderes Format überführt. Das Verfahren kann
 * dabei variieren und ist für diese Abstrakte Klasse irrelevant. Hier wird ausschließlich beschrieben, was wohin
 * überführt werden soll.
 * Da dies hier eine einfache beschreibende Klasse ist, gibt es hierbei keine Hooks und auch keine weiteren Methoden.
 * Dabei gibt es 2 Typen. F und T. F (From) ist das Format, welches geparsed werden soll und T (To) ist der Typ in
 * welches das Ergebnis zurück gegeben werden soll.
 *
 * @param <F> Der Typ, in con welchem etwas überführt werden soll
 * @param <T> Der Typ, welcher das Ergebnis des parsings sein soll
 */
public interface Parser<F, T> {
	/**
	 * Mit dem Aufrufen dieser Methode soll der übergebene Typ in einen anderen Typ überführt werden.
	 *
	 * @param f das Object, welches überführt werden soll, mit dem Typ F
	 *
	 * @return das parsed Object als der Typ T
	 */
	T parse(F f);
}
