package de.thorbenkuck.netcom.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import de.thorbenkuck.netcom.datatypes.interfaces.Parser;

/**
 * Der CommandLineReader ist die Schnittstelle zwischen Console und eingabe.
 * Sie signalisiert (ähnlich wie eine *nix-shell) wenn sie bereit ist für eine Eingabe, sowie den Nutzer und die
 * aktuelle Machine, zu welcher dieser Nutzer angemeldet ist.
 */
public class CommandLineReader  implements Serializable{

	public static final String DEFAULT_STOP_COMMAND = "exit";

	@Override
	public String toString() {
		return "CommandLineReader{" +
				"bufferedReader=" + bufferedReader +
				", running=" + running +
				", stopCommand='" + stopCommand + '\'' +
				", listCommand='" + listCommand + '\'' +
				", cliParser=" + cliParser +
				'}';
	}

	public static final String DEFAULT_USER = "none";
	private static final String USER_SOURCE_CONNECTOR = "@";
	private static final String READY_FOR_INPUT_CHAR = "$";
	private static CountDownLatch countDownLatch;
	private static String prefix;
	private static String user;
	private static String connectedTo;
	private static boolean waitingForInput;
	private BufferedReader bufferedReader;
	private boolean running;
	private String stopCommand;
	private String listCommand;
	private Parser<String, Boolean> cliParser;

	/**
	 * Erstellt einen CommanLineReader mit dem default stopCommand.
	 */
	public CommandLineReader () {
		this(DEFAULT_STOP_COMMAND);
	}

	/**
	 * Erstellt einen CommandLineReader und setzt intern den StopCommand.
	 * Der StopCommand ist ein {@link Comm}, welcher diesen CommandLineReader beendet.
	 *
	 * @param stopCommand der CommandIdentifier, welcher den CommandLineReader stoppen soll.
	 */
	public CommandLineReader (String stopCommand) {
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		countDownLatch = new CountDownLatch(0);
		this.stopCommand = stopCommand;
		this.listCommand = "list";
		user = DEFAULT_USER;
		prefix = "#";
		connectedTo = "localhost";
		cliParser = new CliParser();
	}

	/**
	 * Diese Methode zentralisiert die Ausgabe an die Console.
	 * Dadurch kann hier u.a. zukünftig zentral geloggt werden.
	 *
	 * @param message    die Nachricht, welche Ausgegeben werden soll
	 * @param withPrefix boolean, ob der Prefix mit ausgegeben werden soll, oder nicht.
	 */
	public static synchronized void printMessage (String message, boolean withPrefix) {
		if (withPrefix) {
			System.out.println(prefix + message);
		} else {
			System.out.println(message);
		}
	}

	/**
	 * Diese Methode ist eine kürzere Schreibweise für printMessage(message, true);
	 * D.h. sie gibt den Prefix mit aus.
	 *
	 * @param message die Nachricht, welche Ausgegeben werden soll.
	 */
	public static void printMessage (String message) {
		printMessage(message, true);
	}

	/**
	 * Diese Methode stoppt die Eingabe kurzzeitig.
	 * Bis die Methode {@link #letMeResume()} getriggert wird.
	 * Hierbei wird intern ein CountDownLatch erstellt, auf welchen die Eingabe wartet.
	 */
	public static void letMeWait () {
		countDownLatch = new CountDownLatch(1);
	}

	/**
	 * Diese Methode lässt die Eingabeaufforderung wieder weiter vortfahren.
	 * Dabei kann, muss aber nicht {@link #letMeWait()} aufgerufen worden sein.
	 */
	public static void letMeResume () {
		if (waitingForInput) {
			printInputSignal();
		} else {
			countDownLatch.countDown();
		}
	}

	/**
	 * Setzt den prefix für die Console-Ausgabe.
	 *
	 * @param prefix Der Prefix, welcher für die Console nun verwendet werden soll
	 */
	public static void setPrefix (String prefix) {
		CommandLineReader.prefix = prefix;
	}

	/**
	 * Durch den Aufruf dieser Methode signalisiert der CommandLineReader, dass er für eine Eingabe bereit ist.
	 */
	private static void printInputSignal () {
		System.out.print(user + USER_SOURCE_CONNECTOR + connectedTo + READY_FOR_INPUT_CHAR + " ");
	}

	/**
	 * Startet den CommandLineReader.
	 * Dabei werden einige Abhängigkeiten erstellt.
	 * Sollte ein Fehler auftreten, so wird dieser durch gereicht. Dies kännte abgefangen werden, muss aber nicht.
	 *
	 * @throws IOException          wenn der Reader unterbrochen wird
	 * @throws InterruptedException wenn das Warten abgebrochen wird.
	 */
	public void start () throws IOException, InterruptedException {
		addDefaultExitCommand();
		addDefaultListCommand();
		running = true;
		printMessage("\n\n\n#-----------------------------#\n" +
				"#Started the CommandLineReader#\n" +
				"#-----------------------------#\n");
		while ( running ) {
			try {
				countDownLatch.await(10, TimeUnit.SECONDS);
				readLine();
			} catch (Exception e) {
				printMessage("\n\nUnexpected error!!!!\n\n#-----------------#\n" + e.getClass().getSimpleName() + "\n", false);
				e.printStackTrace(System.out);
				printMessage("\n#-----------------#\n\n", false);
				executeCommand(stopCommand);
			}
		}
		printMessage("bye!");
	}

	/**
	 * Führt einen NetComCommand aus, nachdem dieser ausgelesen wurde.
	 *
	 * @param command der String (im Klartext) welcher ausgelesen wurde.
	 */
	private boolean executeCommand (String command) {
		return cliParser.parse(command);
	}

	/**
	 * Wartet auf die Eingabe von einem Nutzer.
	 *
	 * @throws IOException wenn er unterbrochen wird dabei.
	 */
	private void readLine () throws IOException {
		printInputSignal();
		waitingForInput = true;
		String command = bufferedReader.readLine();
		waitingForInput = false;
		if(!executeCommand(command)) {
			printMessage("Unknown command: " + command);
		}
	}

	/**
	 * Diese Methode fügt dem {@link CliParser} den NetComCommand hinzu, um den CommandLineReader zu beenden.
	 * Wichtig! Wenn ein persöhnlicher stopCommand genutzt werden soll, sollte dieser gesetzt werden vor Aufruf von
	 * {@link #start()}
	 */
	private void addDefaultExitCommand () {
		CliParser.addCommand(new DefaultComm(stopCommand, () -> {
			printMessage("exiting ..");
			System.exit(0);
		}, "Exit the program"));
	}

	/**
	 * Diese Methode fügt dem {@link CliParser} den NetComCommand hinzu, um alle Comm's auf zu listen.
	 * Wichtig! Wenn ein persöhnlicher listCommand genutzt werden soll, sollte dieser gesetzt werden vor Aufruf von
	 * {@link #start()}
	 */
	private void addDefaultListCommand () {
		CliParser.addCommand(new DefaultComm(listCommand, () -> {
			printMessage("\n#-#LIST#-#\nAll registered Comm's:", false);
			for ( Comm c : CliParser.getRegisteredCommands() ) {
				printMessage(c.getIdentifier() + getDescription(c));
			}
			printMessage("", false);
		}, "List all set Comm's"));
	}

	/**
	 * Gibt die Description eines Comm's organisiert zurück.
	 *
	 * @param comm der Comm, für welchen die Description gefragt ist.
	 *
	 * @return der Organisierte String der Description
	 */
	private String getDescription (Comm comm) {
		return (!comm.getDescription().equals("") ? " \"" + comm.getDescription() + "\"" : "");
	}

	/**
	 * Setzt den User(namen), welcher aktuell an dem CommandLineReader sitzt.
	 *
	 * @param user der Identifiziernde String des Users, welcher aktuell an der Konsole arbeitet.
	 */
	public void setUser (String user) {
		CommandLineReader.user = user;
	}

	/**
	 * Setzt intern den User(Namen) auf den default-Namen
	 */
	public void setToDefaultUser () {
		user = CommandLineReader.DEFAULT_USER;
	}

	/**
	 * Setzt die Verbindung, zu welcher der User verbunden ist.
	 *
	 * @param connectedTo die Verbindungs-Information in Form eines Strings.
	 */
	public void setConnectedTo (String connectedTo) {
		CommandLineReader.connectedTo = connectedTo.replace("127.0.0.1", "localhost");
	}

	/**
	 * Setzt den Identifier des ListCommands.
	 * ACHTUNG! Diese Methode hat nur eine Auswirkuing VOR dem aufruf von {@link #start()}
	 *
	 * @param listCommandName der neue Identifier
	 */
	public void setListCommand (String listCommandName) {
		this.listCommand = listCommandName;
	}

	/**
	 * Setzt den Identifier des ExistCommands.
	 * ACHTUNG! Diese Methode hat nur eine Auswirkuing VOR dem aufruf von {@link #start()}
	 *
	 * @param exitCommandName der neue Identifier
	 */
	public void setExitCommand (String exitCommandName) {
		this.stopCommand = exitCommandName;
	}
}
