package scripts;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Midterm {
	void showSnippet(String collectionPos, String words) {

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
			File folder = new File(collectionPos);
			
			Document CheckID = docBuilder.parse(folder);
			
			NodeList docList = CheckID.getElementsByTagName("doc");
			StringBuilder allArrayBuild = new StringBuilder();
			String[] iBody = new String[docList.getLength()];
			for(int i = 0; i < docList.getLength(); i++) {
				NodeList BodyList = CheckID.getElementsByTagName("body").item(i).getChildNodes();
				Node Body = (Node) BodyList.item(0);
				String tempBodyData = Body.getNodeValue();
				iBody[i] = tempBodyData;
				//id = i 일때 String값
			}
			//snipet 만들기
			char[][] snipet = new char[docList.getLength()][30];
			
			//word에서 단어 추출하기
			KeywordExtractor ke = new KeywordExtractor();
			KeywordList kl = ke.extractKeyword(words, true);
			
			
			KeywordList[] kb = new KeywordList[5];
			ke.extractKeyword(iBody[0], true);
			String[] word = new String[kl.size()];
			int wordL = 0;
			for(int j = 0; j < kl.size(); j++) {
				int kwrdCheck = 0;
				Keyword kwrd = kl.get(j);
				for(int l = 0; l < j; l++) {
					if(kwrd == kl.get(l)) {
						kwrdCheck++; break;
					}
				}
				if(kwrdCheck == 0) {
					word[wordL] = kwrd.getString(); wordL++;
				}				
			}
			
			
			
			//resultSni에 각 title의최대값 입력
			int[] resultSni = new int[docList.getLength()];
			for(int gg = 0; gg < docList.getLength(); gg++) {
				int res = 0;
				String[] bodydata = iBody[gg].split("");
				for(int ss = 0; ss < bodydata.length; ss++) {
					for(int kk = 0; kk < wordL; kk++) {
						if(word[kk].equals(bodydata[ss])) {
							while(true) {
								
							}
						}
					}
				}
				
				
				resultSni[gg] = res;
			}
			//큰 순서대로정렬
			
			for(int ii = 0; ii < resultSni.length; ii++) {
				for(int jj =0; jj < ii; jj++) {
					if(resultSni[ii]<resultSni[jj]) {
						int temp = resultSni[ii];
						resultSni[ii] = resultSni[jj];
						resultSni[jj] = temp;
					}
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
