import server.MultiServer;
/**
 * Classe Main del Server
 * Avviare un server
 * 
 * @author Gruppo A14
 *
 */
public class Main {
    /**
     * main
     * Punto di ingresso dell'applicazione lato Server
     * 
     * @param args argomenti passati da terminale
     */
    public static void main(String[] args) {
    	int port = validatePort(args);
        if (port != -1) 
            MultiServer.MultiServerOn(port);
        else 
        	System.exit(1);
    }
    
    /**
     * Metodo validatePort
     * Si assicura che la porta fortina sia valida e gestisce i casi in cui l'input non è valido
     * 
     * @param args argomenti passati da terminale
     * @return ritorna il numero di porta se è valido altrimenti -1
     */
    private static int validatePort(String[] args) {
    	if (args.length == 0) {
            System.err.println("Il numero di porta non e' inserito");
            return -1;
        }

        int port;
        try {
            port = Integer.parseInt(args[0]);
            if (port < 0 || port > 65535) {
                System.err.println("Numero di porta " + args[0] + " non valido");
                return -1;
            }
            return port;
        } catch (NumberFormatException e) {
        	System.err.println("Numero di porta " + args[0] + " non valido");
        	return -1;
        }
    }
}