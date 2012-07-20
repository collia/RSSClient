package RSS;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import main.RSSLogger;

import RSS.data.Answer;
import RSS.data.RSSEntry;
import RSS.data.RSSServer;


public class RSSThread implements RSSReader {
	
	private static Logger logger = RSSLogger.loggerRSSThread;
	
	private RSSReader reader;
	public static final int NUM_OF_THREADS = 3;
	
	Answer result = new Answer(new HashMap<RSSServer, List<RSSEntry>>(), 
								new HashMap<RSSServer, Exception>());
	
	public RSSThread(){
		reader = new RSSNet();
		logger.fine("Constructor");
	}
	public RSSThread(RSSReader reader)
	{
		this.reader = reader;
		logger.fine("Constructor(RSSReader reader)");
	}

	@Override
	public Answer getLastRSS(RSSServer server, int num)	{
		logger.fine("getLastRSS(RSSServer server, int num)");
		return reader.getLastRSS(server, num);
	}

	@Override
	public Answer getLastRSS(List<RSSServer> servers,List<Integer> num)  
	{
		logger.entering(this.getClass().getName(), 
				"getLastRSS(List<RSSServer> servers,List<Integer> num)");
		logger.fine("Loading " + servers.size() + " servers");
		if(servers.size() < NUM_OF_THREADS){
			return reader.getLastRSS(servers, num);
		}
		else {
			while(num.size() < servers.size())
			{
				num.add(-1);
			}
			List<RSSServer> subServers;
			List<Integer> subNum;
			int size = servers.size();
			Thread[] t = new Thread[NUM_OF_THREADS];
			for(int i = 0; i < NUM_OF_THREADS; i++){
				int begin = i*size/NUM_OF_THREADS;
				int end = (i+1)*size/NUM_OF_THREADS;
				logger.fine("Loading from" + begin + " to " + end);
				
				subServers = servers.subList(begin, end);
				subNum = num.subList(begin, end);
				t[i] =  new RSSThreadQuery(subServers, subNum);
				t[i].start();
			}
			for(int i = 0; i < NUM_OF_THREADS; i++)
				try {
					t[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
		logger.exiting(this.getClass().getName(), 
			"getLastRSS(List<RSSServer> servers,List<Integer> num)");
		return result;
	}
	
	private class RSSThreadQuery extends Thread
	{
		private List<RSSServer> servers;
		private List<Integer> num;
		
		RSSThreadQuery(List<RSSServer> servers,
					   List<Integer> num)
		{
			this.servers = servers;
			this.num = num;
		}
		@Override
		public void run(){
				addResult(	reader.getLastRSS(servers, num));
		}
	}
	
	synchronized private void addResult(Answer res){
		result.add(res);
	}
	
}
