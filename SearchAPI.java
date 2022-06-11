package week15;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
public class SearchAPI {
	public static void main(String[] args) {

		
		try {
			String clID = "sXS1r93FLMFtTZgzOU4B";
			String clPa = "ZfpRagVqoz";
			
			Scanner sc = new Scanner(System.in);
			String find;
			System.out.print("검색어를 입력하세요: ");
			find = sc.next();
			
			String text = URLEncoder.encode(find, "UTF-8");
			
			String apiURL = "https://openapi.naver.com/v1/search/movie?query=" + text; 
			URL ur1 = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection)ur1.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", clID);
			con.setRequestProperty("X-Naver-Client-Secret", clPa);
			int responseCode = con.getResponseCode();
			
			BufferedReader br;
			if(responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			}
			else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine = br.readLine()) != null) {
				response.append(inputLine);
				response.append("\n");
			}
			br.close();
			String tes = response.toString();
			JSONParser josnParser = new JSONParser();
			JSONObject jOB = (JSONObject) josnParser.parse(tes);
			JSONArray infoArr = (JSONArray) jOB.get("items");
			
			for(int i = 0; i < infoArr.size(); i++) {
				System.out.println("=item_" + i + "======================================");
				JSONObject iOB = (JSONObject) infoArr.get(i);
				System.out.println("title:      "+iOB.get("title"));
				System.out.println("subtitle:   "+iOB.get("subtitle"));
				System.out.println("director:   "+iOB.get("director"));
				System.out.println("actor:      "+iOB.get("actor"));
				System.out.println("userRating: "+iOB.get("userRating"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
