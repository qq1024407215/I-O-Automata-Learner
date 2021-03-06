package cn.ac.ios.machine.ia;

import java.util.ArrayList;
import java.util.List;

import cn.ac.ios.words.APList;

public class IAImpl implements InterfaceAutomaton {
	protected State initialState;
	protected final APList iApList;
	protected final APList oApList;
	protected final List<State> states;
	protected Boolean deltaAdded;
	
	public IAImpl(APList iAps, APList oAps){
		this.iApList = iAps;
		this.oApList = oAps;
		this.states = new ArrayList<State>();
		this.deltaAdded = false;
	}
	
	@Override
	public State getInitial() {
		return this.initialState;
	}

	@Override
	public State getState(int index) {
		assert index < states.size();
		return states.get(index);
	}

	@Override
	public State createState() {
		State state = makeState();
		states.add(state);
		return state;
	}

	@Override
	public int getStateSize() {
		return states.size();
	}

	@Override
	public void setInitial(int state) {
		this.initialState = this.states.get(state);
	}
	
	private State makeState(){
		State newState = new StateImpl(this, this.states.size());
		return newState;
	}

	@Override
	public APList getInAPs() {
		return this.iApList;
	}

	@Override
	public APList getOutAPs() {
		return this.oApList;
	}

	@Override
	public int getInApSize() {
		return this.getInAPs().size();
	}

	@Override
	public int getTotalApSize() {
		return this.getInApSize() + this.getOutAPs().size();
	}

	@Override
	public void addDelta() {
		assert this.deltaAdded = false;
		for(int i = 0; i < this.getStateSize(); i++){
			if(this.getState(i).isQuiescent()){
				this.getState(i).addTransition(this.getTotalApSize(), i);
			}
		}
		this.deltaAdded = true;
	}

	@Override
	public void removeDelta() {
		assert this.deltaAdded = true;
		for(int i = 0; i < this.getStateSize(); i++){
			if(this.getState(i).isEnable(getTotalApSize())){
				this.getState(i).rmTransition(this.getTotalApSize());
			}
		}
		this.deltaAdded = false;
	}

	@Override
	public Boolean isDeterministic() {
		return false;
	}

	@Override
	public Boolean isDeltaAdded() {
		return this.deltaAdded;
	}

	@Override
	public int getSuccessor(int state, int letter) {
		System.out.println("get successor is only supported on DIA");
		return -1;
	}

	@Override
	public int getOutputLetter(int state) {
		System.out.println("get output letter only supported on DIA");
		return 0;
	}
	
	
}
