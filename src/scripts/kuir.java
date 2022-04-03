package scripts;


public class kuir {

	public static void main(String[] args) {
		String command =args[0];
		String path = args[1];
		
		if(command.equals("-c")) {
			makeCollection collection = new makeCollection();
			collection.makeCollect(path);
//			System.out.println("collection.xml 积己 肯丰");
		}
		else if (command.equals("-k")) {
			makeKeyword keyword = new makeKeyword();
			keyword.makeIndex(path);
//			System.out.println("index.xml 积己 肯丰");
		}
		else if(command.equals("-i")) {
			makeIndex Index = new makeIndex();
			Index.Indexer(path);
		}
		else if(command.equals("-s")){
			String words = args[3];
			searcher Searcher = new searcher();
			Searcher.makeSearch(path, words);
			
		}
	}
}
