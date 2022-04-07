package scripts;

<<<<<<< HEAD
=======
//feature�� searcher
>>>>>>> feature
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
<<<<<<< HEAD
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//master�� searcher
=======
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

>>>>>>> feature
public class searcher {
	//InnerProduct = ������ ���ϴ� �Լ�
	double[] InnerProduct(String path, String words) {
		try {
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
				//���� word�� kkma�м��� ���	
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
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked", "nls"})
	void CalcSim(String path, String indexPath, String words) {
		try {
			//�ؽ��� key + value�� ���� + �������� �������� 

			
			double[] result = InnerProduct(path, words);
				//���� ������ result[k]�� �����Ǿ�����.
				
				double[] Sim = new double[result.length];
				double[] Qroot = new double[result.length];
				double[] Wroot = new double[result.length];

				
				for(int t = 0; t < result.length; t++) {
					Qroot[t] = 0; Wroot[t] = 0;
					for(int ss =0; ss< wordsKey.length; ss++) {
						Qroot[t] += wordsValue[ss]*wordsValue[ss];
						for(int tt = 0; tt < )
					}
					
					Qroot[t] = Math.sqrt(Qroot[t]);
					Wroot[t] = Math.sqrt(Wroot[t]);
					//Wroot�� 0�ϰ�� �и� 0 => sim�� 0���� �����.
					if(Wroot[t] == 0) Sim[t] = 0;
					else {
						Sim[t] = result[t] / (Qroot[t] * Wroot[t]);
					}
					
					System.out.println(result[t] + " / ( " + Qroot[t] + " * " + Wroot[t] + ") = " + Sim[t]);
				}
				//���� ū���� ���� ���ڷ� ������ sort
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
				//index.xml �о ���� ��ȣ ��������
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				File folder = new File(indexPath);
				Document CheckID = docBuilder.parse(folder);
				
				for(int e= 0; e < 3; e++) {
					if(Sim[e] == 0) {
						if(e == 0) {
							System.out.println("�Է��� ���� ������ doc�� �����ϴ�."); break;
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
