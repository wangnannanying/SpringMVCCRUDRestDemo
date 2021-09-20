package com.websystique.springmvc;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;

import org.springframework.web.client.RestTemplate;

import com.websystique.springmvc.model.User;

public class SpringRestTestClient {

//	public static final String REST_SERVICE_URI = "http://localhost:8080/Spring4MVCCRUDRestService";
//
//	/* GET */
//	@SuppressWarnings("unchecked")
//	private static void listAllUsers(){
//		System.out.println("Testing listAllUsers API-----------");
//
//		RestTemplate restTemplate = new RestTemplate();
//		List<LinkedHashMap<String, Object>> usersMap = restTemplate.getForObject(REST_SERVICE_URI+"/user/", List.class);
//
//		if(usersMap!=null){
//			for(LinkedHashMap<String, Object> map : usersMap){
//	            System.out.println("User : id="+map.get("id")+", Name="+map.get("name")+", Age="+map.get("age")+", Salary="+map.get("salary"));;
//	        }
//		}else{
//			System.out.println("No user exist----------");
//		}
//	}
//
//	/* GET */
//	private static void getUser(){
//		System.out.println("Testing getUser API----------");
//		RestTemplate restTemplate = new RestTemplate();
//        User user = restTemplate.getForObject(REST_SERVICE_URI+"/user/1", User.class);
//        System.out.println(user);
//	}
//
//	/* POST */
//    private static void createUser() {
//		System.out.println("Testing create User API----------");
//    	RestTemplate restTemplate = new RestTemplate();
//        User user = new User(0,"Sarah",51,134);
//        URI uri = restTemplate.postForLocation(REST_SERVICE_URI+"/user/", user, User.class);
//        System.out.println("Location : "+uri.toASCIIString());
//    }
//
//    /* PUT */
//    private static void updateUser() {
//		System.out.println("Testing update User API----------");
//        RestTemplate restTemplate = new RestTemplate();
//        User user  = new User(1,"Tomy",33, 70000);
//        restTemplate.put(REST_SERVICE_URI+"/user/1", user);
//        System.out.println(user);
//    }
//
//    /* DELETE */
//    private static void deleteUser() {
//		System.out.println("Testing delete User API----------");
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.delete(REST_SERVICE_URI+"/user/3");
//    }
//
//
//    /* DELETE */
//    private static void deleteAllUsers() {
//		System.out.println("Testing all delete Users API----------");
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.delete(REST_SERVICE_URI+"/user/");
//    }

	private static String parseDouble2String(double num) {
		String numStr = String.valueOf(num);
		if (numStr.contains(".")) {
			return numStr.substring(0, numStr.indexOf("."));
		} else {
			return numStr;
		}
	}
    public static void main(String args[]){
//		listAllUsers();
//		getUser();
//		createUser();
//		listAllUsers();
//		updateUser();
//		listAllUsers();
//		deleteUser();
//		listAllUsers();
//		deleteAllUsers();
//		listAllUsers();

		Double double1=new Double(1.0);
		String str1 = parseDouble2String(double1);
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put("count", str1);
		String requestBodyJson = JSONObject.fromObject(parameters).toString();
		System.out.println(requestBodyJson);

		String count = parameters.get("count");
        Long countnew = null == count ? 0L : Double.valueOf(count).longValue();
		System.out.println(countnew);


    }
}