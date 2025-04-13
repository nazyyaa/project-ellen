package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.DefectiveLight;

public class Wrench extends BreakableTool <DefectiveLight> implements Collectible {
    public Wrench() {
        super(2);
        Animation NormalAnimation = new Animation("sprites/wrench.png");
        setAnimation(NormalAnimation);
    }

    public Wrench(int remainingUses) {
        super(remainingUses);
    }

    @Override
    public void useWith(DefectiveLight actor) {
        if (actor == null) return;
        if (actor.repair()) super.useWith(actor);
    }

    @Override
    public Class<DefectiveLight> getUsingActorClass() {
        return DefectiveLight.class;
    }
}
