package br.github.hebertfsoares.windChargePL.listeners;

import br.github.hebertfsoares.windChargePL.WindCharge;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Listener responsável por gerenciar eventos relacionados ao uso do item de WindCharge.
 *
 * <p>Esta classe lida com interações dos jogadores com o item WindCharge e com eventos de impacto de projéteis.</p>
 */
public class WindChargeListener implements Listener {
    private final WindCharge plugin;

    /**
     * Construtor para a classe WindChargeListener.
     *
     * @param plugin A instância do plugin WindCharge que está associada a este listener.
     */
    public WindChargeListener(WindCharge plugin) {
        this.plugin = plugin;
    }

    /**
     * Manipula o evento de interação do jogador com o item.
     *
     * <p>Quando o jogador interage com o item WindCharge, um projétil de WindCharge é lançado e efeitos são aplicados.</p>
     *
     * @param event O evento de interação do jogador.
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getInventory().getItemInMainHand().getType() == Material.WIND_CHARGE) {
                Projectile windCharge = player.launchProjectile(org.bukkit.entity.WindCharge.class);
                windCharge.setVelocity(player.getLocation().getDirection().multiply(plugin.getConfig().getDouble("projectile_speed")));

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Location location = windCharge.getLocation();
                        if (plugin.getConfig().getBoolean("show_particles")) {
                            location.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, location, 30, 0.5, 0.5, 0.5, 0.1);
                        }

                        for (Player target : plugin.getServer().getOnlinePlayers()) {
                            if (target.getLocation().distance(location) < 2) {
                                Vector knockback = target.getLocation().toVector().subtract(location.toVector()).normalize().multiply(plugin.getConfig().getInt("explosion_strength"));
                                target.setVelocity(knockback);
                                target.playSound(target.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0F, 1.0F);
                            }
                        }

                        if (windCharge.isDead() || !windCharge.isValid()) {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0, 1);
            }
        }
    }

    /**
     * Manipula o evento de impacto do projétil.
     *
     * <p>Quando um projétil de WindCharge atinge um alvo, partículas são geradas e efeitos são aplicados aos jogadores próximos.</p>
     *
     * @param event O evento de impacto do projétil.
     */
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getType() == EntityType.WIND_CHARGE) {
            Projectile windCharge = event.getEntity();
            Location location = windCharge.getLocation();

            if (plugin.getConfig().getBoolean("show_particles")) {
                location.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, location, 30, 0.5, 0.5, 0.5, 0.1);
            }

            // Aplica efeitos aos jogadores próximos
            for (Player target : plugin.getServer().getOnlinePlayers()) {
                if (target.getLocation().distance(location) < 2) {
                    target.setVelocity(new Vector(0, 1, 0)); // Faz o jogador pular mais alto
                    target.playSound(target.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1.0F, 1.0F);
                }
            }
        }
    }
}
