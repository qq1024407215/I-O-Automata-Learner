package cn.ac.ios.ia;

import cn.ac.ios.machine.ia.DIAImpl;
import cn.ac.ios.machine.ia.IAExporterDOT;
import cn.ac.ios.machine.ia.InterfaceAutomaton;
import cn.ac.ios.words.Alphabet;

public class IAExporterTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Alphabet input = new Alphabet(Integer.class);
		Alphabet output = new Alphabet(Integer.class);
		input.addLetter(0);
		input.addLetter(1);
		
		output.addLetter(0);
		output.addLetter(1);
		output.addLetter(2);
		InterfaceAutomaton IA = new DIAImpl(input.getAPs(), output.getAPs());
		for(int i = 0; i < 4; i++){
			IA.createState();
		}
		IA.setInitial(0);
		IA.getInitial().addTransition(0, 1);
		IA.getState(1).addTransition(input.getAPSize(), 2);
		IA.getState(1).addTransition(0, 3);
		IA.getState(2).addTransition(1, 2);
		IA.getState(3).addTransition(0, 3);
		IA.getState(2).addTransition(input.getAPSize() + 1, 3);
		IA.getState(2).addTransition(input.getAPSize() + 2, 1);
		
		InterfaceAutomaton IAs = IA;
		IAs.addDelta();
		IAs.removeDelta();
		IAExporterDOT.export(IA);
		IAExporterDOT.export(IAs);
		System.out.println(IA.isDeterministic());
		System.out.println(IA.getState(1).isOutputDetermined());
		System.out.println("It is not possible that " + IA.getState(2).isOutputDetermined());
	}
	
}
