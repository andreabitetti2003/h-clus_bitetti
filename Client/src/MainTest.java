import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
/**
 * Classe Main del Client
 * Avvia un client
 * 
 * @author Gruppo A14
 */
public class MainTest {
	/**
	 * Stream di manipolazione 
	 * Utilizzati per serializzazione e deserializzazione
	 * 
	 */
	private ObjectOutputStream out;
	private ObjectInputStream in ;
	
	/**
	 * Costruttore
	 * Mediante il metodo statico InetAddress.getByName()
 	 * ottengo un oggetto InetAddress dall’indirizzo IP
	 * Creo un oggetto Socket
	 * usato dal client per inizializzare la connessione
	 *
	 * @param ip indirizzo ip del client/server
	 * @param port numero della porta dove è attivo il servizio	 
	 * @throws IOException eccezione sollevata nel caso in cui si verifica un errore di input/output 
	*/
	public MainTest(String ip, int port) throws IOException {
		InetAddress addr = InetAddress.getByName(ip); 
		System.out.println("addr = " + addr);
		Socket socket = new Socket(addr, port);
		System.out.println(socket);
		
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream()); 
	}
	
	/**
	 * Metodo menu
	 * Gestisce l'input dell'utente relativo alla scelta dell'operazione da effettuare
	 * L'utente decide se caricare dendrogramma da File o apprendere dendrogramma da database
	 *
	 * @return answer valore intero che corrisponde ad 1 o 2
	 */
	private int menu() {
		int answer = -1;
		boolean flag;
		System.out.println("Scegli una opzione");
		System.out.println("(1) Carica Dendrogramma da File");
		System.out.println("(2) Apprendi Dendrogramma da Database");
		do {
			flag = false;
			System.out.print("Risposta: ");
			try {
				answer = Keyboard.readInt();
				flag = true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} while (answer <= 0 || answer > 2 || flag == false);
		return answer;
		
	}
	
	/**
	 * Metodo loadDataOnServer
	 * Gestisce l'inserimento del nome della tabella da cui caricare il dendrogramma e lo invia al server
	 *
	 * @throws IOException eccezione sollevata nel caso in cui si verifica un errore di input/output
	 * @throws ClassNotFoundException eccezione sollevata nel caso in cui la classe dell'oggetto serializzato non viene trovata
	 */
	private void loadDataOnServer() throws IOException, ClassNotFoundException {
		boolean flag = false;
		String table_name = null;
		do {
			do {
				System.out.print("Inserire il nome della tabella: ");
				try {
					table_name = Keyboard.readString();
					flag = true;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} while(flag == false);
			flag = false;
			
			out.writeObject(0);		
			out.writeObject(table_name);
			String answer = (String)(in.readObject());
			if(answer.equals("OK"))
				flag = true;
			else System.out.println(answer);
		} while (flag == false);
	}
	
	/**
	 * Metodo loadDendrogramFromFileOnServer
	 * Gestisce l'inserimento del nome del file su cui caricare il dendrogramma e lo invia al server
	 *
	 * @throws IOException eccezione sollevata nel caso in cui si verifica un errore di input/output
	 * @throws ClassNotFoundException eccezione sollevata nel caso in cui la classe dell'oggetto serializzato non viene trovata
	 */
	private void loadDendrogramFromFileOnServer() throws IOException, ClassNotFoundException {
		boolean flag = false;
		String file_name = null;
		do {
			do {
				System.out.print("Inserire il nome dell'archivio (comprensivo di estensione): ");
				try {
					file_name = Keyboard.readString();
					flag = true;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} while(flag == false);
			
			flag = false;
			out.writeObject(2);
			out.writeObject(file_name);
			String answer = (String)(in.readObject());
			if(answer.equals("OK")) {	
				System.out.println(in.readObject());
				flag = true;
			}else 
				System.out.println(answer); 
		} while(flag == false);
	}
	
	/**
	 * Metodo mineDedrogramOnServer
	 * Gestisce l'inserimento della profondità del dendrogramma, tipo di algoritmo e nome del file su cui caricare il dendrogramma
	 *
	 * @throws IOException eccezione sollevata nel caso in cui si verifica un errore di input/output
	 * @throws ClassNotFoundException eccezione sollevata nel caso in cui la classe dell'oggetto serializzato non viene trovata
	 */
	private void mineDedrogramOnServer() throws IOException, ClassNotFoundException {
		boolean flag = false;
		int depth = -1;
		do {
			out.writeObject(1);
			
			do {
				System.out.print("Introdurre la profondita' del dendrogramma: ");
				try {
					depth = Keyboard.readInt();
					flag = true;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} while(flag == false);
			
			flag = false;
			out.writeObject(depth);
			String answer = (String)(in.readObject());
			if(answer.equals("OK")) 	
				flag = true;
			else
				System.out.println(answer);
		} while (flag == false);
		
		int d_type = -1;
		flag = false;
		do {
			System.out.print("Distanza: single-link (1), average-link (2): ");
			try {
				d_type = Keyboard.readInt();
				flag = true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} while (d_type<=0 || d_type>2 || flag == false);
		out.writeObject(d_type);

		String answer = (String)(in.readObject());
		if (answer.equals("OK")) {
			System.out.println(in.readObject()); 
			flag = false;
			do {
				String file_name = null;
				do {
					System.out.print("Inserire il nome dell'archivio (comprensivo di estensione): ");
					try {
						file_name = Keyboard.readString();
						flag = true;
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				} while (flag == false);
				
				flag = false;
				out.writeObject(file_name);
				answer = (String)(in.readObject());
				if (answer.equals("OK")) {
					flag = true;
				} else {
					System.out.println(answer);
					out.writeObject(1);
					out.writeObject(depth);
					in.readObject();
					out.writeObject(d_type);
					in.readObject();
					in.readObject();
				}
			} while (flag == false);
			System.out.println(in.readObject());
		} else 
			System.out.println(answer); 
	}
	
	/**
 	* Metodo main
 	* Punto di ingresso dell'applicazione lato Client
 	* 
 	* @param args argomenti passati da terminale
 	*/
	public static void main(String[] args) {
		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		MainTest main = null;
		try{
			main = new MainTest(ip,port);
			main.loadDataOnServer();
			int scelta = main.menu();
			if (scelta == 1) 	
				main.loadDendrogramFromFileOnServer();			
			else 
				main.mineDedrogramOnServer();
		} catch (IOException | ClassNotFoundException  e) {
			System.out.println(e);
			return;
		}
	}
}