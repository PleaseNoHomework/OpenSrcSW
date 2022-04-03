package scripts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class makeIndex {

	public void Indexer(String postIndexPos) {
		try {
			FileOutputStream fileStream = new FileOutputStream("index.post");
			
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
	
			//키워드 개수 있는 파일 가져오기
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			File folder = new File(postIndexPos);
			Document CheckID = docBuilder.parse(folder);
			
			NodeList docList = CheckID.getElementsByTagName("doc");
			StringBuilder allArrayBuild = new StringBuilder();
			String[] iBody = new String[docList.getLength()];
			
			for(int i = 0; i < docList.getLength(); i++) {
				NodeList BodyList = CheckID.getElementsByTagName("body").item(i).getChildNodes();
				Node Body = (Node) BodyList.item(0);
				String tempBodyData = Body.getNodeValue();
				//id = i 일때 String값
				iBody[i] = tempBodyData;
				//모든 Body값
				allArrayBuild.append(tempBodyData);
			}
			//전체 name, num값
			String allBody = allArrayBuild.toString();
			String[] allArray = allBody.split(":|#");
			String[] allArrayName = new String[allArray.length/2];
			int[] allArrayNum= new int[allArray.length/2];
			int ar = 0;
			for(int a = 0; a<allArray.length; a+=2) {
				allArrayName[ar] = allArray[a];
				allArrayNum[ar] = Integer.parseInt(allArray[a+1]);
				ar++;
			}	
			
			
			//doc id = i일때의 name과 num
			String[] iArray;
			String[][] iArrayName;
			int[][] iArrayNum;
			iArrayName = new String[docList.getLength()][];
			iArrayNum = new int[docList.getLength()][];
			for(int a = 0; a < docList.getLength(); a++) {
				int ir = 0;
				iArray = iBody[a].split(":|#");
				iArrayName[a] = new String[iArray.length/2];
				iArrayNum[a] = new int[iArray.length/2];
				for(int c = 0; c < iArray.length; c+=2) {
					iArrayName[a][ir] = iArray[c];
					iArrayNum[a][ir] = Integer.parseInt(iArray[c+1]);
					ir++;
				}
			}

			//가중치 구하기
			//dfx
			int dfx[] = new int[allArrayName.length];
			for(int t = 0; t < allArrayName.length; t++) {
				int check = 0;
				for(int x = 0; x <docList.getLength(); x++) {
					for(int y = 0; y < iArrayName[x].length; y++) {
						if(allArrayName[t].equals(iArrayName[x][y])) {
							check++;
						}
					}				
				}
				dfx[t] = check;
			}
			//tfx,y는 iArrayNum과 같다.
			int checkdfx =0;
			double Wxy[][] = new double[docList.getLength()][];
			
			for(int o = 0; o < iArrayName.length; o++) {
				Wxy[o] = new double[iArrayName[o].length];
				for (int p = 0; p <iArrayName[o].length; p++) {
					double tempWxy = iArrayNum[o][p] * Math.log(docList.getLength()/dfx[checkdfx]);
					Wxy[o][p] = Math.round(tempWxy * 100) / 100.0;
					checkdfx++;
				}
			}
			
			HashMap<String, String> rkwndcl = new HashMap();

			for(int checkHashMap = 0; checkHashMap<allArrayName.length; checkHashMap++) {
//				HashMap<Integer, String> tempRkwndcl = new HashMap();
				StringBuilder keyRkwndcl = new StringBuilder();		
					for(int j = 0; j <docList.getLength(); j++) {

						int isCheck = 0;
						for(int l = 0; l < iArrayName[j].length; l++) {
							if(iArrayName[j][l].equals(allArrayName[checkHashMap])) {
								keyRkwndcl.append(Wxy[j][l] + " ");
								isCheck++;
								break;
							}
						}
						if(isCheck == 0) keyRkwndcl.append("0.0 ");
					}
					rkwndcl.put(allArrayName[checkHashMap], keyRkwndcl.toString());
					System.out.println(allArrayName[checkHashMap] +" " + keyRkwndcl.toString());
			}

			
			
			objectOutputStream.writeObject(rkwndcl);
			objectOutputStream.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
