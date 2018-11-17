package objects;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import util.GameUtil;

public class Reserve implements Serializable {
	private Set<Cube> cubeSet;
	private Set<ResearchCenter> researchCenterSet;
	
	public Reserve(Set<Desease> deseaseSet) {
		this.cubeSet = new HashSet<Cube>();
		for(Desease desease : deseaseSet) {
			for(int i = 0; i<24 ; i++) {
				cubeSet.add(new Cube(desease));
			}
		}
		this.researchCenterSet = new HashSet<ResearchCenter>();
		for(int i = 0; i<6 ; i++) {
			researchCenterSet.add(new ResearchCenter());
		}
	}
	

	public Set<Cube> getCubeSet() {
		return cubeSet;
	}

	public Set<ResearchCenter> getResearchCenterSet() {
		return researchCenterSet;
	}

	public ResearchCenter getResearchCenter() {
		if(researchCenterSet != null) {
			Iterator<ResearchCenter> it = researchCenterSet.iterator();
			if(it.hasNext()) {
				ResearchCenter researchCenter = it.next();
				it.remove();
				return researchCenter;
			}
		}
		return null;
	}


	public Cube getCube(Desease desease) {
		Set<Cube> deseaseCubeSet = (Set<Cube>) cubeSet.stream().filter(GameUtil.getCubePredicate(desease)).collect(Collectors.toSet());
		if(deseaseCubeSet != null && deseaseCubeSet.size()>0) {
			Iterator<Cube> it = deseaseCubeSet.iterator();
			Cube cube = it.next();
			it.remove();
			cubeSet.remove(cube);
			return cube;
		}
		return null;
	}


	public void addCube(Cube cube) {
		cubeSet.add(cube);
	}


	public void addCube(Set<Cube> cubeSet2) {
		cubeSet.addAll(cubeSet2);
	}
}
