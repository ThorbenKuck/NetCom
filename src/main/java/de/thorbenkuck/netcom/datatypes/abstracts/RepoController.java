package de.thorbenkuck.netcom.datatypes.abstracts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public abstract class RepoController  implements Serializable{

	private File repoFile;
	private List<String> repoFileInteriorCache;
	private boolean createIfAbsent;
	protected ReentrantLock lock;

	/**
	 * Erstelle einen RepoController, mit dem Pfad und der Anweisung, ob die Datei erstellt werden soll, wenn sie nicht existiert.
	 * @param pathToRepoFile der Pfad zum Repository
	 * @param createIfAbsent boolean, ob die Datei erstellt werden soll, wenn sie nicht existiert.
	 */
	public RepoController(String pathToRepoFile, boolean createIfAbsent) {
		this(pathToRepoFile);
		this.createIfAbsent = createIfAbsent;
	}

	/**
	 * Erstellt einen RepoController, mit dem Pfad zu dem gewünschten repository
	 * @param pathToRepoFile Pfad zu der Repository-Datei
	 */
	public RepoController(String pathToRepoFile) {
		this(new File(pathToRepoFile));
	}

	/**
	 * Erstellt einen RepoController, mit den einem File anstelle eines einfachen Pfades
	 * @param file java.io.File des Repo's
	 */
	public RepoController(File file) {
		lock = new ReentrantLock(true);
		this.repoFile = file;
		this.createIfAbsent = false;
	}

	public RepoController(File file, boolean createIfAbsent) {
		this(file);
		this.createIfAbsent = createIfAbsent;
	}

	/**
	 * Lädt den Inhalt der Datei in dieses Objekt.
	 * @return sich selbst, damit das Chainen von Events möglich ist.
	 * @throws IOException
	 */
	public RepoController loadFromFile() throws IOException {
		boolean creatingFile = false;
		lock.lock();
		try {
			if (!repoFile.exists() && createIfAbsent) {
				creatingFile = true;
				createFile();
			}
			this.repoFileInteriorCache = Files.readAllLines(repoFile.toPath());
		} finally {
			lock.unlock();
		}

		if(creatingFile) {
			setToDefault();
			safe();
		}

		return this;
	}

	/**
	 * Erstellt die, im Konstruktor übergebene Datei auf Basis des, im Konstruktor übergebenen Pfades, bzw. File's
	 * @throws IOException
	 */
	public boolean createFile() throws IOException {
		boolean suc;
		try {
			suc = repoFile.getParentFile().mkdirs() && repoFile.createNewFile();
		} catch (NullPointerException e) {
			suc = repoFile.createNewFile();
		}
		return suc;
	}

	/**
	 * gibt den Inhalt des Repo's zurück.
	 * Jede Zeile der Datei ist dabei eine Zeile der Liste
	 * @return eine Liste mit allen Zeilen als String
	 */
	public List<String> getRepoFileText() {
		return repoFileInteriorCache;
	}

	/**
	 * Setzt die interne Liste auf eine neue.
	 * @param list die neuen Zeilen
	 */
	public void setRepoFileInterior(List<String> list) {
		repoFileInteriorCache = list;
	}

	/**
	 * Speichert die interne Liste in das RepoFile
	 * @return true, wenn erfolgreich, sonst false
	 * @throws IOException
	 */
	public boolean safe() {
		lock.lock();
		boolean succ = false;
		try {
			FileWriter writer = new FileWriter(repoFile);
			for (String s : repoFileInteriorCache) {
				writer.write(String.format(s + "%n"));
			}
			writer.close();
			succ = true;
		} catch (IOException e) {
			succ = false;
		}
		lock.lock();
		return succ;
	}

	/**
	 * Die default Methode soll die interne Liste füllen, so dass, wenn diese Methode aufgerufen wird, ein ursprünglicher Zustand wieder hergestellt werden kann.
	 */
	public abstract void setToDefault();

	@Override
	public String toString() {
		return "RepoController{" +
				"repoFile=" + repoFile +
				", repoFileInteriorCache=" + repoFileInteriorCache +
				", createIfAbsent=" + createIfAbsent +
				'}';
	}
}
