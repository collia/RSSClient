package RSS;

import java.util.List;

import RSS.data.Answer;
import RSS.data.RSSServer;

/**
 * Interface needed for realization pattern Strategy?
 * @author Nikolay Klimchuk
 *
 */

public interface RSSReader {
	/**
	 * Return last num entries for the server
	 * @param server - server
	 * @param num - number of entries
	 * @return - num entries
	 * @see RSSServer, Answer
	 */
	public Answer getLastRSS(RSSServer server, int num);
	/**
	 * Return last num entries for the each server from list
	 * @param server - list of servers
	 * @param num - number of entries
	 * @return - num entries
	 * @see RSSServer, Answer
	 */
	public Answer getLastRSS(List<RSSServer> server, List<Integer> num);

}
