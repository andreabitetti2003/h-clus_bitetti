import java.util.StringTokenizer;
import java.io.InputStreamReader;
import java.io.BufferedReader;
/**
 * Classe Keyboard
 * Utilizzata per input da tastiera 
 * 
 * @author Gruppo A14
 */
public class Keyboard {
	
	/** booleano che verificare errori */
	private static boolean printErrors = true;
	/** errorCount numero di errori */
	private static int errorCount = 0;
	/** stringa letta */
	private static String current_token = null;
	/** istanza della classe StringTokenizer */
	private static StringTokenizer reader;
	/** istanza della classe BufferedReader per la lettura da tastiera */
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));


	// ************* Sezione per la gestione dell'errore **************************
	/**
	 * Metodo getErrorCount
	 * Restituisce il corrente numero di errori
	 *
	 * @return errorCount numero di errori
	 */
	public static int getErrorCount() {
		return errorCount;
	}

	/**
	 * Metodo resetErrorCount
	 * Resetta il corrente numero di errori a 0
	 *
	 * @param count numero errori
	 */
	public static void resetErrorCount(int count) {
		errorCount = 0;
	}

	/**
	 * Metodo getPrintErrors
	 * Restituisce un valore booleano che indica se 
	 * gli errori di input sono attualmente stampati sullo standard output
	 *
	 * @return printErrors
	 */
	public static boolean getPrintErrors() {
		return printErrors;
	}

	/**
	 * Metodo setPrintErrors
	 * Imposta un valore booleano che indica se gli errori di input devono essere stampati sullo standard output.	 
	 *
	 * @param flag booleano che indica se gli errori devono essere stampati 
	 *
	 */
	public static void setPrintErrors(boolean flag) {
		printErrors = flag;
	}

	/**
	 * Metodo error
	 * Incrementa il conteggio degli errori e stampa il messaggio di errore, se appropriato.	 
	 *
	 * @param str messaggio di errori
	 */
	private static void error(String str) {
		errorCount++;
		if (printErrors)
			System.out.println(str);
	}

	// ************* Sezione del flusso di input tokenizzato ******************
	
	/**
	 * Metodo getNextToken
	 * Ottiene il token di input successivo presupponendo che possa trovarsi su righe di input successive
	 *
	 * @return getNextToken token successivo
	 */
	private static String getNextToken() {
		return getNextToken(true);
	}

	/**
	 * Metodo getNextToken
	 * Ottiene il token di input successivo, che potrebbe essere già stato letto.
	 *
	 * @return token token successivo
	 * @param skip booleano
	*/
	private static String getNextToken(boolean skip) {
		String token;

		if (current_token == null)
			token = getNextInputToken(skip);
		else {
			token = current_token;
			current_token = null;
		}
		return token;
	}

	/**
	 * Metodo getNextInputToken
	 * Ottiene il token successivo dall'input, che può provenire dalla riga di input corrente o da una successiva
	 * Il parametro determina se vengono utilizzate le righe successive
	 *
	 * @return token token successivo
	 * @param skip booleano che determina se vengono utilizzate le righe successive 
	*/
	private static String getNextInputToken(boolean skip) {
		final String delimiters = " \t\n\r\f";
		String token = null;

		try {
			if (reader == null)
				reader = new StringTokenizer(in.readLine(), delimiters, true);

			while (token == null || ((delimiters.indexOf(token) >= 0) && skip)) {
				while (!reader.hasMoreTokens())
					reader = new StringTokenizer(in.readLine(), delimiters, true);
				token = reader.nextToken();
			}
		} catch (Exception exception) {
			token = null;
		}

		return token;
	}

	/**
	 * Metodo endOfLine
	 * Restituisce vero se non ci sono più token da leggere sulla riga di input corrente
	 *
	 * @return !reader.hasMoreTokens() booleano che indica che non ci sono più token da leggere
	*/
	private static boolean endOfLine() {
		return !reader.hasMoreTokens();
	}

	
	
	// ************* Sezione Lettura *********************************
	/**
	 * Metodo readString
	 * Restituisce una stringa letta dallo standard input
	 *
	 * @return str stringa letta
	 * @throws Exception eccezione generica sollevata qualora venga letto un tipo errato
	*/
	static String readString() throws Exception {
		String str;

		try {
			str = getNextToken(false);
			while (!endOfLine()) {
				str = str + getNextToken(false);
			}
		} catch (Exception exception) {
			str = null;
			throw new Exception("Errore durante la lettura dei dati di tipo string\n");
		} 
		return str;
	}

	/**
	 * Metodo readWord
	 * Restituisce una sottostringa delimitata da spazi (una parola) letta dallo standard input
	 *
	 * @return token parola letta
	 * @throws Exception eccezione generica sollevata qualora venga letto un tipo errato
	*/
	static String readWord() throws Exception {
		String token;
		try {
			token = getNextToken();
		} catch (Exception exception) {
			token = null;
			throw new Exception("Errore durante la lettura dei dati di tipo string\n");
		}
		return token;
	}

	/**
	 * Metodo readBoolean
	 * Restituisce un booleano letto dallo standard input
	 *
	 * @return bool booleano letto
	 * @throws Exception eccezione generica sollevata qualora venga letto un tipo errato
	*/
	static boolean readBoolean() throws Exception {
		String token = getNextToken();
		boolean bool;
		try {
			if (token.toLowerCase().equals("true"))
				bool = true;
			else if (token.toLowerCase().equals("false"))
				bool = false;
			else {
				error("Errore durante la lettura dei dati di tipo boolean");
				bool = false;
			}
		} catch (Exception exception) {
			bool = false;
			throw new Exception("Errore durante la lettura dei dati di tipo boolean\n");
		}
		return bool;
	}

	/**
	 * Metodo readChar
	 * Restituisce un carattere letto dallo standard input
	 *
	 * @return value carattere letto
	 * @throws Exception eccezione generica sollevata qualora venga letto un tipo errato
	*/
	static char readChar() throws Exception {
		String token = getNextToken(false);
		char value;
		try {
			if (token.length () > 1) {
				current_token = token.substring(1, token.length());
			} else
				current_token = null;
			value = token.charAt(0);
		} catch (Exception exception) {
			value = Character.MIN_VALUE;
			throw new Exception("Errore durante la lettura dei dati di tipo char\n");
		}
		return value;
	}

	/**
	 * Metodo readInt
	 * Restituisce un numero intero letto dallo standard input
	 *
	 * @return value intero letto
	 * @throws Exception eccezione generica sollevata qualora venga letto un tipo errato
	*/
	static int readInt() throws Exception {
		String token = getNextToken();
		int value;
		try {
			value = Integer.parseInt(token);
		} catch (Exception exception) {
			value = Integer.MIN_VALUE;
			throw new Exception("Errore durante la lettura dei dati di tipo int\n");
		}
		return value;
	}

	/**
	 * Metodo readLong
	 * Restituisce un intero lungo letto dallo standard input
	 *
	 * @return value intero lungo letto
	 * @throws Exception eccezione generica sollevata qualora venga letto un tipo errato
	*/
	static long readLong() throws Exception {
		String token = getNextToken();
		long value;
		try {
			value = Long.parseLong(token);
		} catch (Exception exception) {
			value = Long.MIN_VALUE;
			throw new Exception("Errore durante la lettura dei dati di tipo long\n");
		}
		return value;
	}

	/**
	 * Metodo readFloat
	 * Restituisce un float letto dallo standard input
	 *
	 * @return value float letto
	 * @throws Exception eccezione generica sollevata qualora venga letto un tipo errato
	*/
	static float readFloat() throws Exception {
		String token = getNextToken();
		float value;
		try {
			value = Float.parseFloat(token);
		} catch (Exception exception) {
			value = Float.NaN;
			throw new Exception("Errore durante la lettura dei dati di tipo float\n");
		}
		return value;
	}

	/**
	 * Metodo readDouble
	 * Restituisce un double letto dallo standard input
	 *
	 * @return value double letto
	 * @throws Exception eccezione generica sollevata qualora venga letto un tipo errato
	*/
	static double readDouble() throws Exception {
		String token = getNextToken();
		double value;
		try {
			value = Double.parseDouble(token);
		} catch (Exception exception) {
			value = Double.NaN;
			throw new Exception("Errore durante la lettura dei dati di tipo double\n");
		}
		return value;
	}
}
