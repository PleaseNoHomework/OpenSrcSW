package scripts;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.snu.ids.kkma.index.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class makeKeyword {
	
	public void makeIndex(String IndexPos) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			//Collection.xml문서를 읽고 doc로 index.xml만들기
			File folder = new File(IndexPos);
			Document CheckID = docBuilder.parse(folder);
			
			//새로 만들 xml doc 생셩
			Document doc = docBuilder.newDocument();
			
			
			NodeList docList = CheckID.getElementsByTagName("doc");
			Element Docs = doc.createElement("docs");
			doc.appendChild(Docs);			
			
			
			for(int i = 0; i < docList.getLength(); i++) {

				Element code = doc.createElement("doc");
				Docs.appendChild(code);
				int ID = i;
				String str = String.valueOf(ID);
				code.setAttribute("ID", str);
				//title 읽기
				NodeList titleList = CheckID.getElementsByTagName("title").item(i).getChildNodes();
				Node Title = (Node) titleList.item(0);
				String titleData = Title.getNodeValue();
				Element TIT = doc.createElement("title");
				code.appendChild(TIT);
				//body읽기
				NodeList BodyList = CheckID.getElementsByTagName("body").item(i).getChildNodes();
				Node Body = (Node) BodyList.item(0);
				String tempBodyData = Body.getNodeValue();

				
				//Collection의 body부분 파싱하여 꼬꼬마분석기 돌린후 집어넣기
				Element BOD = doc.createElement("body");
				code.appendChild(BOD);
				
				KeywordExtractor ke = new KeywordExtractor();
				KeywordList kl = ke.extractKeyword(tempBodyData, true);
				StringBuilder bodyData = new StringBuilder();
				for(int j = 0; j < kl.size(); j++) {
					Keyword kwrd = kl.get(j);
					String keyBodyData = kwrd.getString() + ":" +kwrd.getCnt()+ "#";
					bodyData.append(keyBodyData);
				}
				String Bodydata = bodyData.toString();
				
				TIT.appendChild(doc.createTextNode(titleData));
				BOD.appendChild(doc.createTextNode(Bodydata));
			}
					
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new FileOutputStream(new File("index.xml")));			
			transformer.transform(source, result);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
