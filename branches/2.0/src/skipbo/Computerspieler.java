package skipbo;

/**
 */
public class Computerspieler
{

	static final private int KETTE_BESTEHT = 0;
	static final private int ZUG_GEMACHT = 1;
	static final private int KETTE_UNTERBROCHEN = 2;
	private SkipBo parent;
	private SpielstatusView vorherigeSpielstatus;
	private int deadlock_count = 0;
	private boolean deadlock = false; // Indikator, dass das Spiel sich

	// festgesetzt hat
	// private SpielstatusView spielstatus;

	public Computerspieler(SkipBo parent)
	{
		this.parent = parent;
	}

	public void macheZug(SpielstatusView spielstatus)
	{
		if (spielstatus.getSpielstapelkarte() == Karte.NOPE) return; // Das
		// Spiel
		// wurde
		// gewonnen
		int nr = kannAblegen(spielstatus.getSpielstapelkarte(), spielstatus);
		if (nr >= 0)
		{
			parent.zugSenden(spielstatus.getEigenname(), 4, spielstatus.getZentralFeldname(), nr/* ,spielstatus.getSpielstapelkarte() */);
			return;
		}
		try
		{
			// Wenn Spielkarte nicht direkt abgelegt werden kann, dann
			// versuchen sie mit Hilfe von verfuegbaren Karten ablegen
			spielkarteAblegen(spielstatus); // Versuche vollstaendige Zugkette
			// erstellen fuer ablegen oberen
			// Spielkarte
			handkartenAufraeumen(spielstatus); // UEberfluessige Handkarten
			// ablegen mit bilden von
			// volstaendigen Zugketten
			hilfskartenAufraeumen(spielstatus);
			zugBeenden(spielstatus);
		} catch (ZugGesendetEvent event)
		{
			deadlock = false;
			deadlock_count = 0;
			return;
		}
		// +++++ Diese Teil wird nur dann ausgefuehrt, wenn kein Zug gemacht
		// werden koennte, also Voraussetzung fuer ein Deadlock
		// wenn Deadlock-Zustand mehr als 2 volle Zugzyklen andauert, dann wird
		// das Spiel unterbrochen
		System.out.println("Ein Deadlock wurde erkannt.");
		deadlock_count++;
		deadlock = true;
		if (deadlock_count == 1)
		{
			macheZug(spielstatus); // Alle Berechnungen noch mal wiederholen
			// mit der gelockerten Bedinungen zum
			// Deadlock-Aufloesung
			return;
		}
		if (deadlock_count == 2)
		{
			vorherigeSpielstatus = spielstatus;
			parent.zugAbgeben(); // Zug vorerst weitergeben
			return;
		}
		if (vorherigeSpielstatus.equals(spielstatus)) // Der Spielstand hat
		// sich
		// nicht veraendert, also
		// ein nicht aufloesbare
		// Deadlock
		{
			parent.aktuellesSpielBeenden(Resource.getString("deadlock1"));
			return;
		} else vorherigeSpielstatus = spielstatus;
		parent.zugAbgeben(); // Zug vorerst weitergeben
	}

	private void hilfskartenAufraeumen(SpielstatusView status) throws ZugGesendetEvent
	{
		int[] hilfskarten = status.getHilfskarten();
		for (int j = 0; j < hilfskarten.length; j++)
		{
			if (!deadlock)
			{// Normale Verhaltensregeln
				// Leerfelder, Joker, guenstige fuer Gegner Karten und Karten
				// die
				// Differenz mit Spielkarte mehr als 5 haben, ueberspringen
				if (hilfskarten[j] == Karte.NOPE || hilfskarten[j] == Karte.JOKER || guenstigFuerGegner(hilfskarten[j], status) || SpielstatusView.differenz(status.getSpielstapelkarte(), hilfskarten[j]) > 5) continue;
			} else
			{
				System.out.println("Wegen des Deadlocks versuche alle Karten abzulegen");
				if (hilfskarten[j] == Karte.NOPE) continue; // Bei einem
				// Deadlock, alles
				// ablegen was nur
				// moeglich ist
			}
			SpielstatusView newStatus = new SpielstatusView(status);
			newStatus.removeHilfskarte(j);
			if (bildeVollstaendigeKette(hilfskarten[j], newStatus, false))
			{
				int ablage = kannAblegen(hilfskarten[j], status);
				if (ablage >= 0)
				{
					parent.zugSenden(status.getEigenname(), j, status.getZentralFeldname(), ablage/* ,hilfskarten[j] */);
					throw new ZugGesendetEvent();
				} else throw new Error("Unerwarteter Fehler bei ablegen der Handkarte.");
			}
		}
	}

	/**
	 * Rekursive Funktion zum Bilden der logischer Zugkette zum Ablegen einer
	 * Handkarte
	 */
	private void handkartenAufraeumen(SpielstatusView status) throws ZugGesendetEvent
	{
		int[] handkarten = status.getHandkarten();
		for (int j = 0; j < handkarten.length; j++)
		{
			if (handkarten[j] == Karte.NOPE || handkarten[j] == Karte.JOKER || guenstigFuerGegner(handkarten[j], status)) continue; // Leerfelder,
			// Joker
			// und
			// guenstige
			// fuer
			// Gegner
			// Karten
			// ueberspringen
			SpielstatusView newStatus = new SpielstatusView(status);
			newStatus.removeHandkarte(j);
			if (bildeVollstaendigeKette(handkarten[j], newStatus, false))
			{
				int ablage = kannAblegen(handkarten[j], status);
				if (ablage >= 0)
				{
					parent.zugSenden(status.getEigenname(), j + 5, status.getZentralFeldname(), ablage/* ,handkarten[j] */);
					throw new ZugGesendetEvent();
				} else throw new Error("Unerwarteter Fehler bei ablegen der Handkarte.");
			}
		}
	}

	private void spielkarteAblegen(SpielstatusView status) throws ZugGesendetEvent
	{
		if (!bildeVollstaendigeKette(status.getSpielstapelkarte(), status, true)) return; // Kann
		// Spielkarte
		// nicht
		// ablegen
		int ablage = kannAblegen(status.getSpielstapelkarte(), status);
		if (ablage >= 0)
		{
			parent.zugSenden(status.getEigenname(), 4, status.getZentralFeldname(), ablage/* ,status.getSpielstapelkarte() */);
			throw new ZugGesendetEvent();
		} else throw new Error("Unerwarteter Fehler bei ablegen der Spielkarte.");
	}

	/**
	 * Methode pr�ft ob es f�r den Gegner g�nstig wird, wenn die Karte auf die
	 * Ablage gelegt wird
	 * 
	 * @return true wenn die Karte g�nstig w�hre
	 */
	private boolean guenstigFuerGegner(int karte, SpielstatusView status)
	{
		int[] gegnerkarten = status.getKartenVonNaechstenSpieler();
		if (gegnerkarten[4] == Karte.JOKER) return false;
		return (gegnerkarten[4] - 1) == karte;
	}

	/**
	 * Rekursive Funktion zum Bilden der vollst�ndige logische Zugkette zum
	 * Ablegen einer Karte
	 * 
	 * @param karte
	 *            Karte die letzendlich abgelegt werden soll
	 * @param mitJoker,
	 *            wenn true dann wird Joker in der Kette verwendet falls n�tig
	 */
	private boolean bildeVollstaendigeKette(int karte, SpielstatusView status, boolean mitJoker) throws ZugGesendetEvent
	{
		if (karte == Karte.NOPE) return false;
		int ablage = kannAblegen(karte, status);
		if (ablage >= 0)
		{ // Positive Abbruchkriterium fuer die Rekursion
			return true;
		}
		int[] karten;
		int feldnummer = status.habeHandkarte(SpielstatusView.decrementWert(karte));
		if (feldnummer >= 0)
		{// habe naechstniedrige Handkarte und versuche sie abzulegen
			SpielstatusView newView = new SpielstatusView(status);
			newView.removeHandkarte(feldnummer);
			if (bildeVollstaendigeKette(status.getHandkarten()[feldnummer], newView, mitJoker))
			{
				ablage = kannAblegen(status.getHandkarten()[feldnummer], status);
				if (ablage < 0) throw new Error("Unerwarteter Fehler bei bilden volst�ndiger logischer Kette.");
				parent.zugSenden(status.getEigenname(), feldnummer + 5, status.getZentralFeldname(), ablage/* ,status.getHandkarten()[feldnummer] */);
				throw new ZugGesendetEvent();
			} else return false;
		}
		feldnummer = status.habeHilfskarte(SpielstatusView.decrementWert(karte));
		karten = status.getHilfskarten();
		if (feldnummer >= 0)
		{// habe naechstniedrige Hilfskarte und versuche sie abzulegen
			SpielstatusView newView = new SpielstatusView(status);
			newView.removeHilfskarte(feldnummer);
			if (bildeVollstaendigeKette(karten[feldnummer], newView, mitJoker))
			{
				ablage = kannAblegen(karten[feldnummer], status);
				if (ablage < 0) throw new Error("Unerwarteter Fehler bei bilden volstaendiger logischer Kette.");
				parent.zugSenden(status.getEigenname(), feldnummer, status.getZentralFeldname(), ablage/* ,karten[feldnummer] */);
				throw new ZugGesendetEvent();
			} else return false;
		}
		if (!mitJoker) return false;
		// ##### Versuchen mit Hilfe von Joker die Kette zu bilden ###########
		feldnummer = status.habeHandkarte(Karte.JOKER);
		if (feldnummer >= 0)
		{// Zuerst den Joker von Handkarten benutzen
			SpielstatusView newView = new SpielstatusView(status);
			newView.removeHandkarte(feldnummer);
			if (bildeVollstaendigeKette(SpielstatusView.decrementWert(karte), newView, mitJoker))
			{
				ablage = kannAblegen(SpielstatusView.decrementWert(karte), status);
				if (ablage < 0) throw new Error("Unerwarteter Fehler bei bilden volstaendiger logischer Kette.");
				parent.zugSenden(status.getEigenname(), feldnummer + 5, status.getZentralFeldname(), ablage/* ,Karte.JOKER */);
				throw new ZugGesendetEvent();
			} else return false;
		}
		feldnummer = status.habeHilfskarte(Karte.JOKER);
		karten = status.getHilfskarten();
		if (feldnummer >= 0)
		{// Versuchen den Joker von Hilfskarten benutzen
			SpielstatusView newView = new SpielstatusView(status);
			newView.removeHilfskarte(feldnummer);
			if (bildeVollstaendigeKette(SpielstatusView.decrementWert(karte), newView, mitJoker))
			{
				ablage = kannAblegen(SpielstatusView.decrementWert(karte), status);
				if (ablage < 0) throw new Error("Unerwarteter Fehler bei bilden volstaendiger logischer Kette.");
				parent.zugSenden(status.getEigenname(), feldnummer, status.getZentralFeldname(), ablage/* ,Karte.JOKER */);
				throw new ZugGesendetEvent();
			} else return false;
		}
		return false;
	}

	/**
	 * Skannt die Ablagefelder und gibt den Feldnummer wo die Differenz zwischen
	 * karte und Ablagekarte am kleinstem ist (idealweise 1)
	 * 
	 * @param karte
	 *            Kartenwert fuer die Ablagefelder abgesucht werden
	 * @return Feldnummer der Ablage, wo die am naechsten gesuchten Wert liegt
	 */
	/*
	 * private int gibNaechsthoeheAblage(int karte, SpielstatusView spielstatus) {
	 * int best_diff = 12; // Beste zur Zeit gefundene Differenz int best_ablage =
	 * 0; // Feldnummer mit bester Differenz int diff; for(int i = 0; i <
	 * spielstatus.getAblegekarten().length; i++) { diff =
	 * SpielstatusView.differenz(spielstatus.getAblegekarten()[i],karte);
	 * if(diff < best_diff) { best_ablage = i; best_diff = diff; } } return
	 * best_ablage; }
	 */
	/**
	 * Prueft, ob Karte auf die Ablagastapel gelegt werden kann
	 * 
	 * @return Nummer des Feldes, wochen gelegt werden kann, oder -1 wenn Karte
	 *         kann nicht gelegt werden
	 */
	private int kannAblegen(int karte, SpielstatusView spielstatus)
	{
		if (karte == Karte.NOPE) throw new IllegalStateException("Kann leere Karte nicht ablegen.");
		if (karte == Karte.JOKER)
		{// Guenstigen Platz fuer Joker finden
			// Versuche erst doppelten Karten belegen
			int index = spielstatus.getMeistVorhandeneAblegekarte();
			if (index >= 0 && !guenstigFuerGegner(SpielstatusView.incrementWert(spielstatus.getAblegekarten()[index]), spielstatus)) return index;
			for (int i = 0; i < spielstatus.getAblegekarten().length; i++)
			{// In den ersten Feld ablegen der guenstig ist
				if (!guenstigFuerGegner(SpielstatusView.incrementWert(spielstatus.getAblegekarten()[i]), spielstatus)) return i;
			}
			return 0; // wenn alles oben nicht klappt dann einfach auf den
			// ersten Platz legen
		}
		for (int i = 0; i < spielstatus.getAblegekarten().length; i++)
		{
			if (SpielstatusView.differenz(karte, spielstatus.getAblegekarten()[i]) == 1) return i;
		}
		return -1;
	}

	/**
	 * Durchfuhrt Vorbereitung zum Beenden des Zuges
	 */
	private void zugBeenden(SpielstatusView spielstatus) throws ZugGesendetEvent
	{
		int[] handkarten = spielstatus.getHandkarten();
		int[] hilfskarten = spielstatus.getHilfskarten();
		/*
		 * int z = spielstatus.getIndexVonGroessteHandkarte(); if(handkarten[z] ==
		 * Karte.NOPE) { System.out.println("Keine weiter Zuege moeglich.");
		 * parent.zugAbgeben(); // Keine Handkarten }
		 */// +++++ Karten mit gleichen Werten auf Hilfsfelder ablegen ++++++++
		for (int j = 5; j < handkarten.length + 5; j++)
		{
			for (int i = 0; i < hilfskarten.length; i++)
			{
				if (handkarten[j - 5] != Karte.NOPE && handkarten[j - 5] == hilfskarten[i])
				{
					parent.zugSenden(spielstatus.getEigenname(), j, spielstatus.getEigenname(), i/* ,handkarten[j-5] */);
					throw new ZugGesendetEvent();
				}
			}
		}
		// ++++ Handkarte, die mehr als 1 Mal vorkommt auf den ersten freien
		// Hilfsfeld ablegen
		int index_meiste_handkarte = spielstatus.getMeistVorhandeneHandkarte();
		int freie_hilfsfeld = spielstatus.getFreieHilfsfeld();
		if (freie_hilfsfeld >= 0 && index_meiste_handkarte >= 0)
		{
			parent.zugSenden(spielstatus.getEigenname(), index_meiste_handkarte + 5, spielstatus.getEigenname(), freie_hilfsfeld/* ,handkarten[index_meiste_handkarte] */);
			throw new ZugGesendetEvent();
		}
		// ++++ Groesste Handkarte auf den ersten freien Hilfsfeld ablegen
		if (freie_hilfsfeld >= 0)
		{
			int feld2 = spielstatus.getIndexVonGroessteHandkarte();
			if (feld2 >= 0)
			{
				parent.zugSenden(spielstatus.getEigenname(), feld2 + 5, spielstatus.getEigenname(), freie_hilfsfeld/* ,handkarten[feld2] */);
				throw new ZugGesendetEvent();
			}
		}
		// +++ Mehr als 1 Mal vorhandene Karte auf eine der Hilfskarten mit
		// einem grosserem Wert aber mit minimale Differenz ablegen +++++
		if (index_meiste_handkarte >= 0 && handkarten[index_meiste_handkarte] != Karte.NOPE)
		{
			int diff = 30;
			int index = -1;
			for (int i = 0; i < hilfskarten.length; i++)
			{
				int diff2 = hilfskarten[i] - handkarten[index_meiste_handkarte]; // einfache
				// arithmetische
				// Differenz
				if (diff2 < diff && diff2 > 0)
				{
					index = i;
					diff = diff2;
				}
			}
			if (index >= 0)
			{
				parent.zugSenden(spielstatus.getEigenname(), index_meiste_handkarte + 5, spielstatus.getEigenname(), index/* ,handkarten[index_meiste_handkarte] */);
				throw new ZugGesendetEvent();
			}
		}
		// ++++ Handkarte auf eine der Hilfskarten mit einem grosserem Wert
		// ablegen +++++
		for (int j = 0; j < handkarten.length; j++)
		{
			for (int i = 0; i < hilfskarten.length; i++)
			{
				if (handkarten[j] != Karte.NOPE && handkarten[j] < hilfskarten[i])
				{
					parent.zugSenden(spielstatus.getEigenname(), j + 5, spielstatus.getEigenname(), i/* ,handkarten[j] */);
					throw new ZugGesendetEvent();
				}
			}
		}
		// ++++ Als letzte Ausweg, die erste Hilfskarte mit erster Handkarte
		// bedecken +++++
		for (int j = 0; j < handkarten.length; j++)
		{
			if (handkarten[j] != Karte.NOPE)
			{
				parent.zugSenden(spielstatus.getEigenname(), j + 5, spielstatus.getEigenname(), 0/* ,handkarten[j] */);
				throw new ZugGesendetEvent();
			}
		}
	}
}