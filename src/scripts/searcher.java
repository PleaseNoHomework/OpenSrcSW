package scripts;




import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//master의 searcher
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class searcher {
	//InnerProduct = 내적값 구하는 함수
	double[] InnerProduct(String[] wordsKey, int[] wordsValue, String[] keyArr, double[][] keyValue) {
		try {			
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
			
			return result;
		} catch (Exception e) {
			double[] error = {-1};
			e.printStackTrace();
			return error;
		}

		
	}
	
	@SuppressWarnings({"rawtypes", "unchecked", "nls"})
	void CalcSim(String path, String indexPath, String words) {
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
			
			
			double[] result = InnerProduct(wordsKey, wordsValue, keyArr, keyValue);
				//현재 내적값 result[k]에 보관되어있음.
				
				double[] Sim = new double[result.length];
				double[] Qroot = new double[result.length];
				double[] Wroot = new double[result.length];
				int[] resultIndex = new int[result.length];
				for(int h =0; h < resultIndex.length; h++) resultIndex[h] = h;
				
				for(int t = 0; t < result.length; t++) {
					Qroot[t] = 0; Wroot[t] = 0;
					for(int ss =0; ss< wordsKey.length; ss++) {
						Qroot[t] += wordsValue[ss]*wordsValue[ss];
					}
					for(int aa =0 ; aa< wordsKey.length; aa++) {
						for(int bb = 0; bb < keyArr.length; bb++) {
							if(wordsKey[aa].equals(keyArr[bb])) {
								for(int cc = 0; cc < keyValue[bb].length; cc++) {
										Wroot[cc] += keyValue[bb][cc]*keyValue[bb][cc];
								}
							}
						}
					}
					
					Qroot[t] = Math.sqrt(Qroot[t]);
					Wroot[t] = Math.sqrt(Wroot[t]);
					//Wroot가 0일경우 분모가 0 => sim값 0으로 취급함.
					if(Wroot[t] == 0) Sim[t] = 0;
					else {
						Sim[t] = result[t] / (Qroot[t] * Wroot[t]);
					}
					
					System.out.println(result[t] + " / ( " + Qroot[t] + " * " + Wroot[t] + ") = " + Sim[t]);
				}
				//가장 큰값이 낮은 숫자로 오도록 sort
				for(int end = 0; end < Sim.length; end++) {
					for(int end2 = end+1; end2 < Sim.length; end2++) {
						if(Sim[end] < Sim[end2]) {
							double temp = Sim[end];
							Sim[end] = Sim[end2];
							Sim[end2] = temp;
							
							int itemp = resultIndex[end];
							resultIndex[end] = resultIndex[end2];
							resultIndex[end2] = itemp;
						}
					}							
				}
				//index.xml 읽어서 문서 번호 가져오기
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				File folder = new File(indexPath);
				Document CheckID = docBuilder.parse(folder);
				
				for(int e= 0; e < 3; e++) {
					if(Sim[e] == 0) {
						if(e == 0) {
							System.out.println("입력한 값과 유사한 doc이 없습니다."); break;
						}
						else break;
					}
					else {
						NodeList titleList = CheckID.getElementsByTagName("title").item(resultIndex[e]).getChildNodes();
						Node Title = (Node) titleList.item(0);
						String titleData = Title.getNodeValue();
						System.out.println(titleData);
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}

	}
}
