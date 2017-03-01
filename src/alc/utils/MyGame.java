package alc.utils;

import java.awt.geom.Rectangle2D;

public class MyGame {
	
	private Rectangle2D.Double battlefield;
	private Rectangle2D.Double innerBattlefield;

	
	public void setBattlefield(Rectangle2D.Double battlefield){
		this.battlefield = battlefield;
	}
	
	public void setInnerBattlefield(Rectangle2D.Double innerBattlefield){
		this.innerBattlefield = innerBattlefield;
	}
	
	public Rectangle2D.Double getBattlefield(){
		return battlefield;
	}
	
	public Rectangle2D.Double getInnerBattlefield(){
		return innerBattlefield;
	}
}
