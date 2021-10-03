package freqFinder;

import java.util.ArrayList;

class Provincia{
	Provincia(long id, String nome){
		this.id=id;
		this.nome=nome;
	}
	long id;
    String nome;
    ArrayList<Frequenza> freqs=new ArrayList<>();
}