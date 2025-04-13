package sk.tuke.kpi.oop.game.characters;

import java.util.ArrayList;
import java.util.List;

public class Health {
    private int currentHealth;
    private int maxHealth;
    private List<FatigueEffect> fatEffect;
    private boolean fatigueApplied;

    public Health(int initialHealth, int maxHealth){
        this.currentHealth = initialHealth;
        this.maxHealth = maxHealth;
        this.fatEffect = new ArrayList<>();
        this.fatigueApplied = false;
    }

    public Health(int initialHealth) {
        this.currentHealth = initialHealth;
        this.maxHealth = initialHealth;
        this.fatEffect = new ArrayList<>();
        this.fatigueApplied = false;
    }

    public void refill (int amount) {
        this.currentHealth += amount;
        if (this.currentHealth > this.maxHealth) restore();
    }

    public void restore() {
        this.currentHealth = this.maxHealth;
    }

    public void drain (int amount) {
        this.currentHealth -= amount;
        if (this.currentHealth <= 0) exhaust();
    }

    public void exhaust() {
        if (this.currentHealth != 0) this.currentHealth = 0;
        if (!fatigueApplied) this.fatEffect.forEach(FatigueEffect::apply);
        fatigueApplied = true;
    }

    public int getValue() {
        return this.currentHealth;
    }

    public int getMaxValue() {
        return this.maxHealth;
    }

    public void setCurrentHealth(int amount) {
        this.currentHealth = amount;
    }

    public void setMaxHealth(int amount) {
        this.maxHealth = amount;
    }

    @FunctionalInterface
    public interface FatigueEffect {
        void apply();
    }

    public void onFatigued(FatigueEffect effect) {
        if (this.fatEffect == null) return;

        this.fatEffect.add(effect);
    }
}
