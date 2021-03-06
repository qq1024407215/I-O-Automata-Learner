package cn.ac.ios.machine.ia.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.ac.ios.machine.ia.State;

public class StatePairImpl implements StatePair{
	
	public final State OState;
	public final State IState;
	
	public StatePairImpl(State o, State i){
		this.OState = o;
		this.IState = i;
	}
	
	private int getCEChara(int origin){
		if(origin < this.OState.getInApSize()){
			return origin;
		} else {
			return this.OState.getInApSize();
		}
	}
	
	private Boolean outputStep(int letter){
		if(this.OState.getSuccessors(letter).length() != 0){
			if(this.IState.getSuccessors(letter).length() == 0){
				return false;
			}
		}
		return true;
	}
	
	private Boolean inputStep(int letter){
		if(this.IState.getSuccessors(letter).length() != 0){
			if(this.OState.getSuccessors(letter).length() == 0){
				return false;
			}
		}
		return true;
	}
	
	private List<StatePairImpl> oStepPair(int letter, Boolean grid[][]){
		assert outputStep(letter);
		List<StatePairImpl> tempList = new ArrayList<StatePairImpl>();
			if(this.OState.getSuccessors(letter).length() != 0 && 
			   this.IState.getSuccessors(letter).length() != 0){
				for(int j = 0; j < this.OState.getSuccessors(letter).length(); j++){
					for(int k = 0; k < this.IState.getSuccessors(letter).length();k++){
						if(this.OState.getSuccessors(letter).get(j) && 
						   this.IState.getSuccessors(letter).get(k)){
							StatePairImpl p = new StatePairImpl(this.OState.getIA().getState(j),
													    this.IState.getIA().getState(k));
							if(!grid[p.OState.getIndex()][p.IState.getIndex()]){
								grid[p.OState.getIndex()][p.IState.getIndex()] = true;
								tempList.add(p);
							}
						}
					}
				}
			}
		return tempList;
	}
	
	private List<StatePairImpl> iStepPair(int letter, Boolean grid[][]){
		assert inputStep(letter);
		List<StatePairImpl> tempList = new ArrayList<StatePairImpl>();
			if(this.OState.getSuccessors(letter).length() != 0 && 
			   this.IState.getSuccessors(letter).length() != 0){
				for(int j = 0; j < this.OState.getSuccessors(letter).length(); j++){
					for(int k = 0; k < this.IState.getSuccessors(letter).length();k++){
						if(this.OState.getSuccessors(letter).get(j) && 
						this.IState.getSuccessors(letter).get(k)){
						StatePairImpl p = new StatePairImpl(this.OState.getIA().getState(j),
													this.IState.getIA().getState(k));
						if(!grid[p.OState.getIndex()][p.IState.getIndex()]){
							grid[p.OState.getIndex()][p.IState.getIndex()] = true;
							tempList.add(p);
						}
					}
				}
			}
		}
		return tempList;
	}
	
	private Boolean iAltSimCheck(Boolean grid[][], String[] CE){
		for(int i = 0; i < this.IState.getInApSize(); i++){
			Boolean result = this.inputStep(i);
			if(result){
				for(Iterator<StatePairImpl> itr = this.iStepPair(i,grid).iterator(); itr.hasNext();){
					StatePairImpl p = itr.next();
					result = result && p.oAltSimCheck(grid, CE) && p.iAltSimCheck(grid, CE);
					if(!result){
						CE[0] = getCEChara(i) + CE[0];
						return result;
					}
				}
			} else {
				CE[0] = getCEChara(i) + CE[0];
				return result;
			}
		}
		return true;
	}
	
	private Boolean oAltSimCheck(Boolean grid[][], String[] CE){
		for(int i = this.OState.getInApSize(); i < this.OState.getTotalApSize()+1; i++){
			Boolean result = this.outputStep(i);
			if(result){
				for(Iterator<StatePairImpl> itr = this.oStepPair(i, grid).iterator(); itr.hasNext();){
					StatePairImpl p = itr.next();
					result = result && p.oAltSimCheck(grid, CE) && p.iAltSimCheck(grid, CE);
					if(!result){
						CE[0] = getCEChara(i) + CE[0];
						return result;
					}
				}
			} else {
				CE[0] = getCEChara(i) + CE[0];
				return result;
			}
		}
		return true;
	}
	
	public Boolean alterSimCheck(Boolean grid[][], String[] CE){
		assert this.OState.isInitial(this.OState.getIndex());
		assert this.IState.isInitial(this.IState.getIndex());
		return this.iAltSimCheck(grid, CE) && this.oAltSimCheck(grid, CE);
	}
	
	
	
	
	
	
	
	private Boolean iAISimCheck(Boolean grid[][], String[] CE){
		for(int i = 0; i < this.IState.getInApSize(); i++){
			Boolean result = this.inputStep(i);
			if(result){
				for(Iterator<StatePairImpl> itr = this.iStepPair(i,grid).iterator(); itr.hasNext();){
					StatePairImpl p = itr.next();
					result = result && p.aAISimCheck(grid, CE) && p.iAISimCheck(grid, CE);
					if(!result){
						CE[0] = getCEChara(i) + CE[0];
						return result;
					}
				}
			} else {
				CE[0] = getCEChara(i) + CE[0];
				return result;
			}
		}
		return true;
	}
	
	private Boolean aAISimCheck(Boolean grid[][], String[] CE){
		for(int i = 0; i < this.OState.getTotalApSize()+1; i++){
			Boolean result = this.outputStep(i);
			if(result){
				for(Iterator<StatePairImpl> itr = this.oStepPair(i, grid).iterator(); itr.hasNext();){
					StatePairImpl p = itr.next();
					result = result && p.oAltSimCheck(grid,CE) && p.iAltSimCheck(grid, CE);
					if(!result){
						CE[0] = getCEChara(i) + CE[0];
						return result;
					}
				}
			} else {
				CE[0] = getCEChara(i) + CE[0];
				return result;
			}
		}
		return true;
	}
	
	public Boolean AISimCheck(Boolean grid[][], String[] CE){
		assert this.OState.isInitial(this.OState.getIndex());
		assert this.IState.isInitial(this.IState.getIndex());
		return this.iAISimCheck(grid, CE) && this.aAISimCheck(grid, CE);
	}
	
	
	
	
	private Boolean iBiSimCheck(Boolean grid[][], String[] CE){
		for(int i = 0; i < this.IState.getTotalApSize()+1; i++){
			Boolean result = this.inputStep(i);
			if(result){
				for(Iterator<StatePairImpl> itr = this.iStepPair(i,grid).iterator(); itr.hasNext();){
					StatePairImpl p = itr.next();
					result = result && p.aAISimCheck(grid, CE) && p.iAISimCheck(grid,CE);
					if(!result){
						CE[0] = getCEChara(i) + CE[0];
						return result;
					}
				}
			} else {
				CE[0] = getCEChara(i) + CE[0];
				return result;
			}
		}
		return true;
	}
	
	private Boolean oBiSimCheck(Boolean grid[][], String[] CE){
		for(int i = 0; i < this.OState.getTotalApSize()+1; i++){
			Boolean result = this.outputStep(i);
			if(result){
				for(Iterator<StatePairImpl> itr = this.oStepPair(i, grid).iterator(); itr.hasNext();){
					StatePairImpl p = itr.next();
					result = result && p.oAltSimCheck(grid, CE) && p.iAltSimCheck(grid, CE);
					
					if(!result){
						CE[0] = getCEChara(i) + CE[0];
						return result;
					}
				}
			} else {
				CE[0] = getCEChara(i) + CE[0];
				return result;
			}
		}
		return true;
	}
	
	public Boolean bisimulationCheck(Boolean grid[][], String[] CE){
		assert this.OState.isInitial(this.OState.getIndex());
		assert this.IState.isInitial(this.IState.getIndex());
		return this.iBiSimCheck(grid, CE) && this.oBiSimCheck(grid,CE);
	}
	
}
