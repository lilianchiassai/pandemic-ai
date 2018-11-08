package objects;

import java.util.HashSet;
import java.util.Set;

public class Reserve {
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

	public void setCubeSet(Set<Cube> cubeSet) {
		this.cubeSet = cubeSet;
	}

	public Set<ResearchCenter> getResearchCenterSet() {
		return researchCenterSet;
	}

	public void setResearchCenterSet(Set<ResearchCenter> researchCenterSet) {
		this.researchCenterSet = researchCenterSet;
	}
}
