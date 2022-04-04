package scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;

public class searcher {
	@SuppressWarnings({"rawtypes", "unchecked", "nls"})
	void makeSearch(String path, String words) {
		try {
			//해쉬맵 key + value값 추출 + 목차별로 나눠놓기 
			FileInputStream s = new FileInputStream(path);
			ObjectInputStream os = new ObjectInputStream(s);
			
			Object object = os.readObject();
			os.close();
			
			HashMap hashMap = (HashMap)object;
			Iterator<String> it = hashMap.keySet().iterator();
			String[] keyArr = new String[hashMap.size()];
			double[][] keyValue = new double[hashMap.size()][];
			
			
			int i = 0;
			while(it.hasNext()) {
				keyArr[i] = it.next();
				String value = (String)hashMap.get(keyArr[i]);
				String[] splitKeyValue = value.split(" ");
				keyValue[i] = new double[splitKeyValue.length];
				for(int a=0; a<splitKeyValue.length; a++) {
					keyValue[i][a] = Double.parseDouble(splitKeyValue[a]);
				}		
				i++;
			}
			
			
			
			
			
			//받은 word값 kkma분석기 사용	
			KeywordExtractor ke = new KeywordExtractor();
			KeywordList kl = ke.extractKeyword(words, true);
			
			String wordsKey[] = new String[kl.size()];
			int wordsValue[] = new int[kl.size()];
			for(int x =0 ; x<kl.size(); x++) {
				Keyword kwrd = kl.get(x);
				wordsKey[x] = kwrd.getString();
				wordsValue[x] = kwrd.getCnt();
			}
			
			double[] result = new double[keyValue[0].length];
			int[] resultIndex = new int[result.length];
			for (int w = 0; w < result.length; w++) {
				resultIndex[w] = w;
			}
			
			
			for(int j =0 ; j< wordsKey.length; j++) {
				for(int l = 0; l < keyArr.length; l++) {
					if(wordsKey[j].equals(keyArr[l])) {
						for(int k = 0; k < keyValue[l].length; k++) {
							result[k] += wordsValue[j]*keyValue[l][k];
						}
					}
				}
			}
			
			//가장 큰값이 낮은 숫자로 오도록 sort
			for(int end = 0; end < result.length; end++) {
				for(int end2 = end+1; end2 < result.length; end2++) {
					if(result[end] < result[end2]) {
						double temp = result[end];
						result[end] = result[end2];
						result[end2] = temp;
						
						int itemp = resultIndex[end];
						resultIndex[end] = resultIndex[end2];
						resultIndex[end2] = itemp;
					}
				}							
			}
			
			for(int e= 0; e < 3; e++) {
				if(resultIndex[e] == 0) {
					if(e == 0) {
						System.out.println("입력한 값과 유사한 doc이 없습니다.");
					}
					else break;
				}
				else System.out.println(e + " ");
			}
			
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
