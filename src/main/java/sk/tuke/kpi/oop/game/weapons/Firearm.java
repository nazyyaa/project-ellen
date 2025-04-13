package sk.tuke.kpi.oop.game.weapons;

public abstract class Firearm {
    private int initialAmmo;
    private int maxAmmo;

    public Firearm(int initialAmmo, int maxAmmo) {
        this.initialAmmo = initialAmmo;
        this.maxAmmo = maxAmmo;
    }

    public Firearm(int initialAmmo) {
        this.initialAmmo = initialAmmo;
        this.maxAmmo = initialAmmo;
    }

    public void reload(int newAmmo) {
        initialAmmo = Math.min(initialAmmo + newAmmo, maxAmmo);
    }

    public int getAmmo() {
        return initialAmmo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public void setMaxAmmo(int ammo) {
        this.maxAmmo = ammo;
    }

    public Fireable fire() {
        if (initialAmmo == 0) return null;

        decreaseAmmo(1);
        return createBullet();
    }

    public void decreaseAmmo(int ammo) {
        initialAmmo -= ammo;
        if (initialAmmo < 0) initialAmmo = 0;
    }

    protected abstract Fireable createBullet();
}