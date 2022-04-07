package scripts;


public class kuir {

	public static void main(String[] args) {
		String command =args[0];
		String path = args[1];
		
		// �Է� : -c ./data
		if(command.equals("-c")) {
			makeCollection collection = new makeCollection();
			collection.makeCollect(path);
		}
		// �Է� : -k ./collection.xml
		else if (command.equals("-k")) {
			makeKeyword keyword = new makeKeyword();
			keyword.makeIndex(path);
		}
		// �Է� : -i ./index.xml
		else if(command.equals("-i")) {
			makeIndex Index = new makeIndex();
			Index.Indexer(path);
		}
		// �Է� : -s ./index.post ./index.xml "���� �Է�"
		else if(command.equals("-s")){
			String indexPath = args[2];
			String words = args[3];
			searcher Searcher = new searcher();
			Searcher.CalcSim(path, indexPath,words);
			
		}
	}
}
