package server;
import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;
/**
 * Classe MultiServer
 * Creazione del socket relativo al server e gestione delle richieste client
 * 
 * @author Gruppo A14
 *
 */
public class MultiServer {
	/** Istanza di Serversocket, socket relativo al server */
	private static ServerSocket s;
	/** Booleano che verifica se il server è stato attivato */
	private static boolean serverOn = false;
	/** Numero di porta del server per la connessione */
	private final int PORT;

	/**
	 * Costruttore
	 * Crea il socket relativo al server e gestisce le richieste client
	 * Per ogni richiesta da parte di un nuovo client viene creata un'istanza di ServerOneClient
	 * gestendo la comunicazione su un Thread separato
	 * 
	 * @param port numero di porta per la connessione
	 */
    private MultiServer(int port) {
    	serverOn = true;
    	this.PORT = port;
    	try {
	        s = new ServerSocket(PORT);
	        System.out.println("Server avviato sulla porta " + PORT);
			while (true) {
				Socket socket = s.accept();
				System.out.println("Inizializzo connessione al client: " + socket);
				try {
					new ServerOneClient(socket);
				} catch (IOException e) {
					System.out.println("Errore durante la creazione del socket: " + socket);
					socket.close();
				}
			}
    	} catch (IOException e) {
    		System.out.println("Errore durante l'avvio del server");
    	} finally {
    		try {
    			if (s != null && !s.isClosed()) {
                    s.close();
                }
            } catch (IOException e) {
                System.out.println("Errore durante la chiusura del server");
            }
    	}
    }
    
    /**
	 * Metodo MultiServerOn
	 * Verifica se il server è attivo, se non lo è lo attiva.
	 * Questo per far si che venga creata una sola volta istanza di Serversocket
	 *
	 * @param port numero di porta sulla quale avvirare il server
	 */
    public static void MultiServerOn(int port) {
        if (serverOn == false)
            new MultiServer(port);
    }
}
