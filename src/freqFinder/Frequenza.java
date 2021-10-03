package freqFinder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class Frequenza{
	Frequenza(long region_id,String region,long province_id,String province,long city_id,String city,long station_id,String station,long id,Double frequency, boolean stereo,long power,long radio_id,String radio,long station_propagation_radius,String created,String frequency_note) throws ParseException{
		this.region_id=region_id;
		SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		this.region=region;
		this.province_id=province_id;
		this.province=province;
		this.city_id=city_id;
		this.city=city;
		this.station_id=station_id;
		this.station=station;
		this.id=id;
		this.frequency=frequency;
		this.stereo=stereo;
		this.power=power;
		this.radio_id=radio_id;
		this.radio=radio;
		this.station_propagation_radius=station_propagation_radius;
		this.created_on = dateFormat.parse(created);
		this.frequency_notes=frequency_note;
	}
	
	public long region_id;
    String region;         
    long province_id;      
    String province;          
    long city_id;         
    String city;            
    long station_id;       
    String station;
    long id;         
    Double frequency;    
    boolean stereo;
    long power;
    long radio_id;
    String radio;          
    long station_propagation_radius;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    Date created_on;
    String frequency_notes;
}