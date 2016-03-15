package de.cleancodingcosmos.csvparser;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Eine Beschreibung des CSV-Formats kann in https://de.wikipedia.org/wiki/CSV_(Dateiformat) nachgelesen werden.
 * Der Parser arbeitet mit einem endlichen Zustandsautomaten. Dabei wird der Eingabe-String Zeichen für
 * Zeichen von Anfang bis Ende gelesen. Je nach Zeichen gelangt man in einen anderen Zustand
 * Im folgenden Verfahren sind bei bestimmten Übergangen von einem in den anderen Zustand verschiedenen Aktionen
 * verbunden. Beispiel: Erscheint das Trennzeichen (;), so kann die bis dorthin gesammelte Zeichenkette (token)
 * als neues CVS-Token "ausgegeben" (d.h. zu result hinzugefügt) werden. Ein Ausnahme besteht dann, wenn sich
 * das Trennzeichen (;) innerhalb eines durch Anführungszeichen eingeklammerten Strings befindet. Dann soll das
 * Zeichen gerade nicht als Trenner dienen. Der Zustandsautomat befindet sich dann aber im Zustand IN_QUOTES (s.u.)
 * und kann entsprechend anders reagieren als im Normalfall (Zustand NO_QUOTES im Falle: ohne Klammerung
 * durch "...").
 */
public class CSVParserClean {

    public final static char DELIM = ';'; // Das Semikolon hat sich mittlerweile als Standard durchgesetzt.
    public final static char QUOTE = '"';

    /**
     * Zustände des Automaten. BASE ist der Anfangszustand. Alle Zustände sind auch Endzustände, auch
     * wenn dies für IN_QUOTES nicht sinnvoll erscheint. Bei diesem Parser wurde verlangt, dass er auch bei
     * Fehlern im Datenformat wie z.B. <code>Hallo;"Pe"ter;wie;geht;es</code> trotzdem eine Ausgabe macht
     * und nicht mit einer Fehlermeldung abbricht.
     */
    enum State {
        BASE, NO_QUOTES, IN_QUOTES, AFTER_QUOTES;
    }

    /**
     * Die eingelesene Zeile <code>str</code> wird in eine Liste von Tokens zerlegt, die als Ergebnis
     * zurückgegeben wird.
     * @param str  Eine CSV-Zeile, die zu parsen ist
     * @return Die Liste der Elemente
     * @throws ParseException
     */
    public ArrayList<String> parseLine(String str) {
        ArrayList<String> result = new ArrayList<>();
        String token = "";
        State state = State.BASE;
        final char[] chars = str.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            switch (state) {
                case BASE:
                    switch (ch) {
                        case QUOTE:
                            state = State.IN_QUOTES;
                            break;
                        case DELIM:
                            result.add("");
                            break;
                        default:
                            token += ch;
                            state = State.NO_QUOTES;
                            break;
                    }
                    break;

                case IN_QUOTES:
                    switch (ch) {
                        case QUOTE:
                            state = State.AFTER_QUOTES;
                            break;
                        default:
                            token += ch;
                    }
                    break;

                case AFTER_QUOTES:
                    switch (ch) {
                        case QUOTE:
                            token += QUOTE;
                            state = State.IN_QUOTES;
                            break;
                        case DELIM:
                            result.add(token);
                            token = "";
                            state = State.BASE;
                            break;
                        default:
                            token += "" + QUOTE + ch;
                            state = State.NO_QUOTES;
                    }
                    break;

                case NO_QUOTES:
                    switch (ch) {
                        case DELIM :
                            result.add(token);
                            token = "";
                            state = State.BASE;

                            break;
                        default:
                            token += ch;
                    }
                    break;

                default:
                    throw new IllegalStateException("Unexpected State " + state);
            }
        }
        result.add(token);
        return result;
    }

}
