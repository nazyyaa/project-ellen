package sk.tuke.kpi.oop.game.behaviours;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.messages.Topic;

import java.util.Objects;
import java.util.function.Predicate;

public class Observing <A extends Actor, T> implements Behaviour<A> {
    private final Topic<T> topic;
    private final Predicate<T> predicate;
    private final Behaviour<A> delegate;
    private A actor;


    public Observing(Topic<T> topic, Predicate<T> predicate, Behaviour<A> delegate) {
        this.topic = topic;
        this.predicate = predicate;
        this.delegate = delegate;
        actor = null;
    }

    @Override
    public void setUp(A actor) {
        if (actor == null) return;
        this.actor = actor;

        Objects.requireNonNull(actor.getScene()).getMessageBus().subscribe(topic, this::enterActor);
    }

    private void enterActor(T message) {
        if (!predicate.test(message) || actor == null) return;

        delegate.setUp(actor);
    }
}