//
// server for PUT@UiO Spring 2020
//

package server_group_project;

import static java.lang.System.out;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.util.Base64;

import javax.imageio.ImageIO;


public class server {
	
	Gson gson;
	
	static final int PERSON_ARRAY_SIZE = 4;
	Person personArray[];
	
	static final int CLAIMS_ARRAY_SIZE = 4;
	Claims claimArray[];
	
	// also defined in files Claims.java and serverController.java
	static final int CLAIMS_ITEMS_ARRAY_SIZE = 5;
	
	public void server() {}
	
	public void start() {
		
		System.out.println("server.start> start");
		
		//Gson gson = new Gson();
		gson = new GsonBuilder().setPrettyPrinting().create();  //Toggle comment for Pretty print JSON
		
		// create in-memory data structure to store the users read to be read from person.json
		personArray = new Person[PERSON_ARRAY_SIZE];
		
		// read and parse the contents of the file person.json into personArray
		parseFilePerson("./data/person.json");
		
		// create in-memory data structure to store the users read to be read from claim.json
		claimArray = new Claims[CLAIMS_ARRAY_SIZE];
		
		// read and parse the contents of the file claim.json into claimnArray
		parseFileClaim("./data/claim.json");
		
		//debug_print_claimArray (claimArray, "called from server.start");
		
		System.out.println("server.start> end");
		
	}
	
	
	// ================================================================================	
	// Called from serverController.java when a POST call
	// is done for a login in done in the smartphone in which
	// there is no local data (in the smartphone) available
	Person methodPostRemoteLogin (String em, String ph) {
		System.out.println("server.methodPostRemoteLogin> start");	
		Person pp = null;
		pp = getInMemoryPerson(em);
		if (pp == null) {System.out.println("server.methodPostRemoteLogin> FATAL ERROR: Person is null");}
		else { 
			String ppEmail = pp.getEmail();
			String ppPh = pp.getPassHash();
			if (ppEmail.equals(em) && ppPh.equals(ph)) {
				System.out.println("server.methodPostRemoteLogin> email and passwd are the same");
			}
			else {
				System.out.println("server.methodPostRemoteLogin> email and passwd are NOT the same");
				pp = null;
			}
		}
		System.out.println("server.methodPostRemoteLogin> start");	
		return pp;
	}
	
	// ================================================================================	
	// this method is called from getMethodMyClaims
	// in the serverControllerClass when there is
	// call from the smartphone to get the claims for
	// a specific user (the user who is logged in)
	public Claims getMyClaims(String id) {
				
		int i, index=-1;
		Claims cl = null;
		
		System.out.println("server.getMyClaims> start");	
			
		// find the claimArray entry corresponding to the user identified by id
		int max = claimArray.length;
		for (i=0; i < max;i++) {
			// equals() method compares the two given strings based on the content of the string
			// if any character is not matched, it returns false; if all characters are matched, it returns true.
			String indexFromClaimArray = claimArray[i].getId();
			if (id.contentEquals(indexFromClaimArray)) {
				index = i;
				break;
			} 
		}
				
		// if there are no claims for the user return immediately and print and
		// error as there should always be an entry for any existing user
		if (index == -1) {
			System.out.println("getMyClaims> FATAL ERROR: there are no claims for the user indicated="+index);
			return cl;
		}
				
		//index is the entry for the current user
		int numberOfClaims = Integer.valueOf(claimArray[index].getNumberOfClaims());
			
		String[] cIdAux = new String[CLAIMS_ITEMS_ARRAY_SIZE];
		String[] cDeAux = new String[CLAIMS_ITEMS_ARRAY_SIZE];
		String[] sTsAux = new String[CLAIMS_ITEMS_ARRAY_SIZE];
		String[] locAux = new String[CLAIMS_ITEMS_ARRAY_SIZE];
		
		// copy the claims for a specific user (identified by id)
		for (i=0; i<numberOfClaims;i++) {
			cIdAux[i] = claimArray[index].getClaimId(i); // claim identifiers
			cDeAux[i] = claimArray[index].getClaimDes(i); // claim descriptions
			sTsAux[i] = claimArray[index].getClaimPhoto(i); // claim photo
			locAux[i] = claimArray[index].getClaimLocation(i); // claim locations
		}
		cl = claimArray[index];	
		System.out.println("server.getMyClaims> end");	
		return cl;
	}
	
	// ================================================================================	
	// Called from serverController.java when a POST call
	// is done resulting from a registration of a new claim
	// done in the smartphone; indexUpdateClaim is in fact equal to nOc
	public String insertNewClaim (String userId, String indexUpdateClaim, String newClaimDes, String newClaimPho, String newClaimLoc) {
			
		System.out.println("server.insertNewClaim> start");	
			
		String nOc = indexUpdateClaim;
		int i = Integer.valueOf(nOc);
		int userIdInt = Integer.parseInt(userId);
		int maxClaimIdInt = maxClaimIdInt(userIdInt);
			
		if (i != maxClaimIdInt || i >= CLAIMS_ITEMS_ARRAY_SIZE) {
	   		System.out.println("server.postInsertNewClaim FATAL ERROR with wrong indexUpdateClaim");
	   		return null;
	   	}
			
		int id = Integer.valueOf(userId);
		String auxNoC =  Integer.toString(i+1);
		    
			
		// insert new claim into the existing claimArray
		// in its entry corresponding to the entry which
		// is the user
		claimArray[id].setId(userId);
		claimArray[id].setNumberOfClaims(auxNoC);
		claimArray[id].setClaimId(i, indexUpdateClaim);
		claimArray[id].setClaimDes(i, newClaimDes); // claim descriptions
		claimArray[id].setClaimPhoto(i, newClaimPho); // claim photo
		claimArray[id].setClaimLocation(i, newClaimLoc); // claim locations
		
	   // write all the claims from claimArray into the file
		writeFileClaim (claimArray,"./data/claim.json");
				
		System.out.println("server.insertNewClaim> end");	
			
		return "OK";
				
	}
		
	// ================================================================================	
	// Called from serverController.java when a POST call
	// is done resulting from a update of an existing claim
	// done in the smartphone.
	public String updateClaim(String userId, String indexUpdateClaim, String updateClaimDes, String updateClaimPho, String updateClaimLoc) {
				
		System.out.println("server.updateClaim> start");	
			
		int id = Integer.valueOf(userId);
		int i = Integer.valueOf(indexUpdateClaim);
			
		if (i >= CLAIMS_ITEMS_ARRAY_SIZE) {
			System.out.println("server.updateClaim FATAL ERROR with indexUpdateClaim equal or bigger than CLAIMS_ITEMS_ARRAY_SIZE");
			return null;	   	
		}
			
		// update an existing claim in the claimArray
		// in its entry corresponding to the entry which
		// is the user
		claimArray[id].setId(userId);
		//claimArray[id].setNumberOfClaims(auxNoC);
		claimArray[id].setClaimId(i, indexUpdateClaim);
		claimArray[id].setClaimDes(i, updateClaimDes); // claim descriptions
		claimArray[id].setClaimPhoto(i, updateClaimPho); // claim descriptions
		claimArray[id].setClaimLocation(i, updateClaimLoc); // claim locations
			
		// write all the claims from claimArray into the file
		writeFileClaim (claimArray,"./data/claim.json");
		
		System.out.println("server.updateClaim> end");	
		
		return "OK";
			
	}
	
	
	
	////////////////////////////////////////////////////////////////////////////////
	// AUXILIARY METHODS
	///////////////////////////////////////////////////////////////////////////////
	
	// ================================================================================	
	// auxiliary method with an obvious functionality
	public int maxUserIdInt() {
		return PERSON_ARRAY_SIZE;
	}
	
	// ================================================================================	
	// auxiliary method with an obvious functionality
	public int maxClaimIdInt(int userIdInt) {
		String maxNumberOfClaims = claimArray[userIdInt].getNumberOfClaims();
		int maxNumberOfClaimsInt = Integer.parseInt(maxNumberOfClaims);
		return maxNumberOfClaimsInt;
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// METHODS THAT READ THE EXISTING PERSON.JSON AND CLAIMS.JSON FILES
	// INTO A IN-MEMORY DATA STRUCTURE WHEN THE SERVER STARTS RUNNING
	/////////////////////////////////////////////////////////////////////////////////
	
	// ================================================================================	
	// read file ./data/person.json which contains
	// a list of persons in Json and create 
	// a in-memory data structure with the contentsstart> claimArray.length=
	// of the JSON file person.json (fileName)
	public void parseFilePerson(String fileName) {		       		
		System.out.println("server.parseFilePerson> start");
		try {
			String data = readFileAsString(fileName);				
			personArray = gson.fromJson(data, Person[].class); // Convert JSON String to personArray
		 }
	     catch (JsonIOException       e) {e.printStackTrace();} 
	     catch (JsonSyntaxException   e) {e.printStackTrace();}
		 catch (Exception             e) {e.printStackTrace();}		
		System.out.println("server.parseFilePerson> end");
	}
	
	// ================================================================================	
	// read a file with a name indicated in the
	// argument and returns its contents as a string
	public static String readFileAsString(String fileName) { 
		System.out.println("server.readFileAsString> start");
		String data = ""; 
		try {data = new String(Files.readAllBytes(Paths.get(fileName)));}
		catch (Exception e) {e.printStackTrace();}	
		System.out.println("server.readFileAsString> end");
	    return data; 
	} 
	
	// ================================================================================	
	// read file ./data/claim.json which contains
	// a list of claims in Json and create 
	// a in-memory data structure with the contents
	// of the JSON file claim.json (fileName)
	public void parseFileClaim(String fileName) {
		System.out.println("server.parseFileClaim> start");	
		try {
			String data = readFileAsString(fileName);
			claimArray = gson.fromJson(data, Claims[].class); // Convert JSON String to claimArray
		}
	    catch (JsonIOException       e) {e.printStackTrace();} 
	    catch (JsonSyntaxException   e) {e.printStackTrace();}
		catch (Exception             e) {e.printStackTrace();}	
		System.out.println("server.parseFileClaim> end");	
	}
	
	// ================================================================================	
	// write a string in a file;
	// file has name indicated in the
	// argument and string as well
	public static void writeStringIntoFile(String fileName, String data) {  	
		System.out.println("server.writeStringIntoFile> start with fileName="+fileName);	
		try {
			File file = new File(fileName);
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(data);
			fileWriter.flush();
			fileWriter.close();
		}
		catch (IOException        e) {
			System.out.println("server.writeStringIntoFile> FATAL ERROR when writing filename="+fileName);	
			e.printStackTrace();
		} 
		System.out.println("server.writeStringIntoFile> end");	
	} 
	
	// ================================================================================	
	// write into file claim.json the contents
	// of the in-memory data structure claimArray
	// (which has been modified)
	private void writeFileClaim(Claims[] cl, String fileName) {			
		System.out.println("server.writeFileClaim> start");	
		try {
			String json;
			json = gson.toJson(cl) ; 
			writeStringIntoFile(fileName, json);
		}
			catch (JsonIOException       e) {e.printStackTrace();} 
			catch (JsonSyntaxException   e) {e.printStackTrace();}
			catch (Exception             e) {e.printStackTrace();}	
		System.out.println("server.writeFileClaim> end");	
	}
	
	
	// ================================================================================	
	// return the Person contents if the email passed
	// as argument is found the in-memory data structure
	public Person getInMemoryPerson(String email) {
		int i = 0, index = -1;
		int max = personArray.length;
		Person pp = null;
		
		System.out.println("server.getInMemoryPerson> start");	
		
		for (i = 0; i<max; i++) {
			if (personArray[i].getEmail().equals(email) == true) {
				index = i; 
				break;
			}
			if (index != -1) break;
		}
		
		
		if (index != -1) {
			pp = personArray[index];		
			System.out.println("server.getInMemoryPerson> end");	
			return pp;
		}
		else {
			System.out.println("getInMemoryPerson> FATAL ERROR: person not found in memory !");
			return null;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	//THE METHODS BELOW ARE JUST FOR TESTING
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// TESTING***********************************************************************
	// invoked from the method getMethodTesting
	public void getMethodTesting() { 
		out.println("server.getMethodTesting> OK");
	} 
		
	// TESTING***********************************************************************
	// invoked from the method postMethodTesting
	public void postMethodTesting() { 
		out.println("server.postMethodTesting> OK");
	} 
	
	// debug method: show the contents of the
	// claimArray in-memory data structure
	public void debug_print_claimArray(Claims[] cl, String debug_sentence) {
					
		System.out.println("debug_print_claimArray> cl="+cl);
		System.out.println("debug_print_claimArray> ="+debug_sentence);
		
		for (int i=0; i<cl.length;i++) {
			System.out.println("server.debug_print_claimArray>i="+i);
			System.out.println("server.debug_print_claimArray> cl[i].getId()="+cl[i].getId()); 
			System.out.println("server.debug_print_claimArray> cl[i].getNumberOfClaims()="+cl[i].getNumberOfClaims()); 
				
			String noc = cl[i].getNumberOfClaims();
			if (noc == null) return;
			int numberOfClaims = Integer.valueOf(noc);
			for (int j=0; j<numberOfClaims;j++) {
				try {
					System.out.println("server.debug_print_claimArray> i =" +i); 
					System.out.println("server.debug_print_claimArray> j =" +j); 
					System.out.println("server.debug_print_claimArray> cl[i].getClaimId(j)       ="+cl[i].getClaimId(j)); 
					System.out.println("server.debug_print_claimArray> cl[i].getClaimDes(j)      ="+cl[i].getClaimDes(j)); 
					System.out.println("server.debug_print_claimArray> cl[i].getClaimPhoto(j)    ="+cl[i].getClaimPhoto(j)); 
					System.out.println("server.debug_print_claimArray> cl[i].getClaimLocation(j) ="+cl[i].getClaimLocation(j)); 
				}	
				catch (JsonIOException       e) {e.printStackTrace();} 
		        catch (JsonSyntaxException   e) {e.printStackTrace();}
				catch (Exception             e) {e.printStackTrace();}		
			}
		}	
	}
	
}
