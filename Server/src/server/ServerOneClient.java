package server;

import java.io.FileNotFoundException;
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import data.Data;
import data.InvalidSizeException;
import data.NoDataException;
import distance.AverageLinkDistance;
import distance.ClusterDistance;
import distance.SingleLinkDistance;
import clustering.ExtensionException;
import clustering.HierachicalClusterMiner;
import clustering.InvalidDepthException;
import clustering.InvalidNumberOfClusters;

/**
 * Classe ServerOneClient
 * Gestione delle operazioni effettuate dal singolo client
 * 
 * @author Gruppo A14
 *
 */
public class ServerOneClient extends Thread {
	/** Socket relativo al client */
	private final Socket socket;
	/** Istanza della classe ObjectInputStream per la lettura */
	private final ObjectInputStream in;
	/** Istanza della classe ObjectOutputStream per la scrittura */
    private final ObjectOutputStream out;
    /** Istanza della classe Data */
    private Data data;
    
    /**
	 * Costruttore
	 * Inizializza gli attributi di istanza ed avvia
	 * 
	 * @param s Socket relativo al client
	 * @throws IOException eccezione sollevata nel caso in cui si verifica un errore di input/output
	 */
    public ServerOneClient(Socket s) throws IOException {
        socket = s;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        start();
    }
    
    /**
	 * Metodo run
	 * Determina il metodo richiamato in funzione dell'input del client, 
	 * serve per gestire le richieste del client
	 * 
	 */
    public void run() {
        try {
            while (true) {
                int clientRequest = (int)in.readObject();

                switch (clientRequest) {
                    case 0: {
                    	loadTableDb();
                        break;
                    }
                    case 1: {
                    	saveDendrogram();
                        break;
                    }
                    case 2: {
                    	loadDendrogram();
                        break;
                    }
                    default: {
                        out.writeObject("Impossibile effettuare tale richiesta");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Disconnessione client: " + socket);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
                out.close();
                in.close();
            } catch (IOException e) {
                System.out.println("Errore, socket non chiuso");
            }
        }
    }
    
    /**
	 * Metodo loadTableDb
	 * Legge il nome della tabella inviato dal client
	 * Genera la tabella corrispondente leggendola da database
	 *
	 * @throws IOException eccezione sollevata nel caso in cui si verifica un errore di input/output
	 * @throws ClassNotFoundException eccezione sollevata nel caso in cui la classe dell'oggetto serializzato non viene trovata
	 */
    private void loadTableDb() throws IOException, ClassNotFoundException {
        String tableName = (String)in.readObject();
        try {
            data = new Data(tableName);
            out.writeObject("OK");
        } catch (NoDataException e) {
            out.writeObject(e.getMessage());
        }
    }
    
	/**
	 * Metodo saveDendrogram
	 * Legge la prondità della tabella, tipo di algoritmo e nome del file inviati dal client
	 * Salva il dendogramma sul file corrispondente
	 *
	 * @throws IOException eccezione sollevata nel caso in cui si verifica un errore di input/output
	 * @throws ClassNotFoundException eccezione sollevata nel caso in cui la classe dell'oggetto serializzato non viene trovata
	 */
    private void saveDendrogram() throws IOException, ClassNotFoundException {
        int depth = (int)in.readObject();
        
        try {
        	if (depth < 1 || depth > data.getNumberOfExamples())
				throw new InvalidDepthException("La profondità del dendrogramma deve essere compresa tra 1 e " + data.getNumberOfExamples () + "\n");
        	else 
        		out.writeObject("OK");
        	HierachicalClusterMiner clustering = new HierachicalClusterMiner(depth);
            ClusterDistance distance;
            int distanceType = (int)in.readObject();
            if (distanceType == 1) 
            	distance = new SingleLinkDistance();
            else
            	distance = new AverageLinkDistance();

            clustering.mine(data, distance);

            out.writeObject("OK");
            out.writeObject(clustering.toString(data));

            String fileName = (String)in.readObject();

            try {
                clustering.saveHierachicalClusterMiner(fileName);
                out.writeObject("OK");
                out.writeObject("Dendrogramma salvato in: " + fileName);
            } catch (FileNotFoundException | ExtensionException e) {
                out.writeObject(e.getMessage());
            } catch (IOException e) {
                out.writeObject(e.getMessage());
            }
        } catch (InvalidNumberOfClusters | InvalidSizeException | InvalidDepthException e) {
        	out.writeObject(e.getMessage());
        }
    }
    
    /**
	 * Metodo loadDendrogram
	 * Legge il dendogramma da file e lo invia al client
	 *
	 * @throws IOException eccezione sollevata nel caso in cui si verifica un errore di input/output
	 * @throws ClassNotFoundException eccezione sollevata nel caso in cui la classe dell'oggetto serializzato non viene trovata
	 */
    private void loadDendrogram() throws IOException, ClassNotFoundException {
        String fileName = (String)in.readObject();
        try {
            HierachicalClusterMiner clustering = HierachicalClusterMiner.loadHierachicalClusterMiner(fileName);
            
            if (clustering.getDepth() > data.getNumberOfExamples()) {
                throw new InvalidDepthException("La profondità del dendrogramma deve essere compresa tra 1 e " + data.getNumberOfExamples () + "\n");
            } else {
                out.writeObject("OK");
                out.writeObject(clustering.toString(data));
            }
        }catch(FileNotFoundException | ClassNotFoundException | IllegalArgumentException | ExtensionException e) {
        	out.writeObject(e.getMessage());
		} catch(IOException | InvalidDepthException e) {
			out.writeObject(e.getMessage());
		}
    }
}
