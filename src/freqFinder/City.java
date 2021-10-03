package freqFinder;

import java.util.ArrayList;

class City{
    
	City(long id, String name){
        this.id=id;
        this.name=name;
    }
	long id=-1;
    String name="";
    ArrayList<Frequenza> freqs=new ArrayList<>();
}