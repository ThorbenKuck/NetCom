package de.thorbenkuck.netcom.datatypes.interfaces;

/**
 * Dieses Interface wird vom ThreadLockable genutzt, damit eine art EventQueue abgearbeitet werden kann.
 */
public interface SynchronizedCallElement {
    /**
     * Diese Methode wird von der "EventQueue" aufgerufen.
     * Dafür wird zu erst der Zugriff gelockt und anschließend unlocked.
     */
    void call();
}
