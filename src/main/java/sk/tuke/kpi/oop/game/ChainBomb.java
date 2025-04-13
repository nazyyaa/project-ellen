package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;


public class ChainBomb extends TimeBomb {
    public ChainBomb(float time) {
        super(time);
    }

    @Override
    public void explode() {
        super.explode();

        Ellipse2D.Float Element = new Ellipse2D.Float(this.getPosX() - 50, this.getPosY() - 50, 102, 102);
        List<Actor> ActorList = getScene().getActors();
        for (Actor actor : ActorList) {
            if (actor instanceof ChainBomb && !((ChainBomb) actor).isActivated()) {
                Rectangle2D.Float ChainBombRadius = new Rectangle2D.Float(actor.getPosX() - actor.getWidth() / 2, actor.getPosY() - actor.getHeight() / 2, actor.getWidth(), actor.getHeight());
                if (Element.intersects(ChainBombRadius))
                    ((ChainBomb) actor).activate();
            }
        }
    }
}
