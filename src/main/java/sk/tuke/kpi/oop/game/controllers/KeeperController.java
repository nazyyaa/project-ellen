package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.actions.Drop;
import sk.tuke.kpi.oop.game.actions.Shift;
import sk.tuke.kpi.oop.game.actions.Take;
import sk.tuke.kpi.oop.game.actions.Use;
import sk.tuke.kpi.oop.game.items.Usable;

import java.util.Objects;

public class KeeperController implements KeyboardListener {
    private final Keeper keeper;

    public KeeperController(Keeper keeper) {
        this.keeper = keeper;
    }

    private void performance() {
        Usable<?> usable = Objects.requireNonNull(keeper.getScene()).getActors().stream().filter(Usable.class::isInstance).filter(keeper::intersects).map(Usable.class::cast).findFirst().orElse(null);
        if (usable != null) {
            new Use<>(usable).scheduleForIntersectingWith(keeper);
        }
    }

    private void backpackPerfomance() {
        if (keeper.getBackpack().peek() instanceof Usable) {
            Use<?> use=new Use<>((Usable<?>)keeper.getBackpack().peek());
            use.scheduleForIntersectingWith(keeper);
        }
    }

    @Override
    public void keyPressed(@NotNull Input.Key key) {
        if (key == Input.Key.ENTER) {
            new Take<>().scheduleFor(keeper);
        }
        if (key == Input.Key.BACKSPACE) {
            new Drop<>().scheduleFor(keeper);
        }
        if (key == Input.Key.S) {
            new Shift<>().scheduleFor(keeper);
        }
        if (key == Input.Key.U) {
            performance();
        }
        if (key == Input.Key.B) {
            backpackPerfomance();
        }
    }
}