package freqFinder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.json.simple.*;
import org.json.simple.parser.ParseException;

import freqFinder.City;
import java.io.File;

class FreqFinder{
    static ArrayList<City> cities=new ArrayList<>();
    static ArrayList<Provincia> provinces=new ArrayList<>();
    static ArrayList<Frequenza> freqs=new ArrayList<>();
    
    static void downloadApiWithNumericId(String url, String params){

    }

    static void downloadFreq() throws IOException, ParseException, java.text.ParseException{
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd+HHmmss");  
    	LocalDateTime now = LocalDateTime.now();
        JSONObject jsonProv = JsonUtilities.readJsonFromUrl("https://www.fm-world.it/frequenze-api.php?action=getAllProvinces");
        JSONObject jsonCity = JsonUtilities.readJsonFromUrl("https://www.fm-world.it/frequenze-api.php?action=getAllCities");
        /*File filePr = new File("dati","prov"+dtf.format(now)+".txt");
        //filePr.mkdirs(); // If the directory containing the file and/or its parent(s) does not exist
        filePr.createNewFile();
        File fileCt = new File("dati","city"+dtf.format(now)+".txt");
        //fileCt.mkdirs(); // If the directory containing the file and/or its parent(s) does not exist
        fileCt.createNewFile();
        FileWriter fileProv = new FileWriter(filePr);
        FileWriter fileCity = new FileWriter(fileCt);
        fileProv.write(jsonProv.toJSONString());
        fileCity.write(jsonCity.toJSONString());
        fileProv.close();
        fileCity.close();*/
        JSONObject jsonProvRes=(JSONObject)jsonProv.get("result");
        JSONObject jsonCityRes=(JSONObject)jsonCity.get("result");
        JSONArray jsonProvArr=(JSONArray)jsonProvRes.get("provinces");
        JSONArray jsonCitiesArr=(JSONArray)jsonCityRes.get("cities");
        System.out.println("Downloading cities and provinces data");
        for(int i=0;i<jsonProvArr.size();i++){
        	provinces.add(new Provincia((long)((JSONObject)jsonProvArr.get(i)).get("id"),(String)((JSONObject)jsonProvArr.get(i)).get("nome")));
            for(int j=0;j<jsonCitiesArr.size();j++){
            	JSONObject city=(JSONObject)jsonCitiesArr.get(j);
            	JSONObject prov=(JSONObject)jsonProvArr.get(i);
                if(((String)city.get("name")).equals((String)prov.get("name"))){
                	//System.out.println(city.get("id") +" "+(String)city.get("name"));
                    cities.add(new City((Long)city.get("id"),(String)city.get("name")));
                }
            }
        }
        System.out.println("Done... Downloading frequencies...");
        for(int i=0;i<cities.size();i++) {
        	JSONObject jsonFreq=JsonUtilities.readJsonFromUrl("https://www.fm-world.it/frequenze-api.php?action=getFrequencies&id="+cities.get(i).id);
        	JSONObject jsonFreqRes=(JSONObject)jsonFreq.get("result");
        	JSONArray jsonFreqArr=(JSONArray)jsonFreqRes.get("frequencies");
        	/*File fileFr = new File("dati",cities.get(i).name+"freqs"+dtf.format(now)+".txt");
        	//fileFr.mkdirs(); // If the directory containing the file and/or its parent(s) does not exist
        	fileFr.createNewFile();
        	FileWriter fileFreqCity = new FileWriter(fileFr);
    		fileFreqCity.write(jsonFreq.toJSONString());
    		fileFreqCity.close();*/
        	for(int j=0;j<jsonFreqArr.size();j++) {
        		long region_id=(long)((JSONObject)jsonFreqArr.get(j)).get("region_id");
        	    String region=(String)((JSONObject)jsonFreqArr.get(j)).get("region");        
        	    long province_id=(long)((JSONObject)jsonFreqArr.get(j)).get("province_id");      
        	    String province=(String)((JSONObject)jsonFreqArr.get(j)).get("province");          
        	    long city_id=(long)((JSONObject)jsonFreqArr.get(j)).get("city_id");        
        	    String city=(String)((JSONObject)jsonFreqArr.get(j)).get("city");           
        	    long station_id=(long)((JSONObject)jsonFreqArr.get(j)).get("station_id");      
        	    String station=(String)((JSONObject)jsonFreqArr.get(j)).get("station");
        	    long id=(long)((JSONObject)jsonFreqArr.get(j)).get("id");       
        	    double frequency=Double.parseDouble((String)((JSONObject)jsonFreqArr.get(j)).get("frequency"));    
        	    boolean stereo=(boolean)((JSONObject)jsonFreqArr.get(j)).get("stereo");
        	    long power=(long)((JSONObject)jsonFreqArr.get(j)).get("power");
        	    long radio_id=(long)((JSONObject)jsonFreqArr.get(j)).get("radio_id");
        	    String radio=(String)((JSONObject)jsonFreqArr.get(j)).get("radio");          
        	    long station_propagation_radius=(long)((JSONObject)jsonFreqArr.get(j)).get("station_propagation_radius");
        	    String created_on=(String)((JSONObject)jsonFreqArr.get(j)).get("created_on");
        	    String frequency_notes=(String)((JSONObject)jsonFreqArr.get(j)).get("frequency_notes");
        		freqs.add(new Frequenza(region_id,region,province_id,province,city_id,city,station_id,station,id,frequency,stereo,power,radio_id,radio,station_propagation_radius,created_on,frequency_notes));
        	}
        }
        System.out.println("Done... Updating cities and provincies freqs data");
        //update of the cities and provinces with the related frequencies for faster research of the freqs related to a place 
        for(int i=0;i<cities.size();i++) {
        	for(int j=0;j<freqs.size();j++) {
        		if(freqs.get(j).city_id==cities.get(i).id) {
        			cities.get(i).freqs.add(freqs.get(j));
        		}
        	}
        }
        
        for(int i=0;i<provinces.size();i++) {
        	for(int j=0;j<freqs.size();j++) {
        		if(freqs.get(j).city_id==provinces.get(i).id) {
        			provinces.get(i).freqs.add(freqs.get(j));
        		}
        	}
        }
        System.out.println("DONE");
    }
    static void elaborateFreq(){
    	//lista frequenze
    	ArrayList<Double> listaFrequenze=new ArrayList<> ();
    	ArrayList<Double> listaFrequenzeRis=new ArrayList<> ();
    	
    	//aggiunta frequenze fittizie per favorire il conto della prima e ultima frequenza disponibile
    	listaFrequenze.add(85.000);
    	listaFrequenze.add(84.999);
    	listaFrequenze.add(110.000);
    	listaFrequenze.add(110.001);
    	
    	for(int i=0;i<freqs.size();i++) {
    		if(!listaFrequenze.contains(freqs.get(i).frequency))listaFrequenze.add(freqs.get(i).frequency);
    	}
    	
    	Collections.sort(listaFrequenze);
    	
    	System.out.println("\n\n\nFrequenze FM(87,5 - 108 MHz)");
    	System.out.println("Frequenze in Italia libere");
    	
    	double distanzaSpanTraFreqMhz=0.2;
    	
    	System.out.println("\n\nfrequenze libere in italia:"+"(separate dalle altre da almeno "+distanzaSpanTraFreqMhz+"Mhz)");
    	for(int i=1;i<listaFrequenze.size()-1;i++) {
    		double frqPrima=listaFrequenze.get(i-1);
    		double frqAttuale=listaFrequenze.get(i);
    		double frqDopo=listaFrequenze.get(i+1);
    		if(frqAttuale-frqPrima>=distanzaSpanTraFreqMhz&&frqDopo-frqAttuale>=distanzaSpanTraFreqMhz) {
    			listaFrequenzeRis.add(frqAttuale);
    		}
    	}
    	for(int i=0;i<listaFrequenzeRis.size();i++) {
    		System.out.print(listaFrequenzeRis.get(i)+" ");
    	}
    	System.out.println("\n--FINE LISTA--");
    	
    	//lista frequenze libere per regione
    	ArrayList<Long> regioniId=new ArrayList<>();
    	for(int i=0;i<freqs.size();i++) {
    		if(!regioniId.contains(freqs.get(i).region_id))regioniId.add(freqs.get(i).region_id);
    	}
    	String regione="";
    	for(int j=0;j<regioniId.size();j++) {
    		for(int i=0;i<freqs.size();i++) {
        		if(!listaFrequenze.contains(freqs.get(i).frequency)&&freqs.get(i).region_id==regioniId.get(j)) {
        			listaFrequenze.add(freqs.get(i).frequency);
        			regione=freqs.get(i).region;
        		}
        	}
    		//System.out.println("--"+regioniId.get(j)+"-"+regione);
    		 
	        	System.out.println("\n\nfrequenze libere in "+regione+":"+"(separate dalle altre da almeno "+distanzaSpanTraFreqMhz+"Mhz)");
	        	listaFrequenze=new ArrayList<Double>(listaFrequenze.subList(0, 3));
	        	listaFrequenzeRis.clear();
	        	for(int i=0;i<freqs.size();i++) {
	        		if(!listaFrequenze.contains(freqs.get(i).frequency)&&freqs.get(i).region.equals(regione))listaFrequenze.add(freqs.get(i).frequency);
	        	}
	        	Collections.sort(listaFrequenze);
	        	for(int i=1;i<listaFrequenze.size()-1;i++) {
	        		double frqPrima=listaFrequenze.get(i-1);
	        		double frqAttuale=listaFrequenze.get(i);
	        		double frqDopo=listaFrequenze.get(i+1);
	        		if(frqAttuale-frqPrima>=distanzaSpanTraFreqMhz&&frqDopo-frqAttuale>=distanzaSpanTraFreqMhz) {
	        			listaFrequenzeRis.add(frqAttuale);
	        		}
	        	}
	        	for(int i=0;i<listaFrequenzeRis.size();i++) {
	        		System.out.print(listaFrequenzeRis.get(i)+" ");
	        	}
	        	System.out.println("\n--FINE LISTA--");
    		}
    	
    }
    public static void main(String[] args) throws IOException, ParseException, java.text.ParseException{
        // https://www.fm-world.it/frequenze-api.php?action=getAllProvinces
        // https://www.fm-world.it/frequenze-api.php?action=getAllCities
        String fmApi="https://www.fm-world.it/frequenze-api.php";
        String params="?action=getFrequencies&id=";
        downloadFreq();
        elaborateFreq();
    }
}